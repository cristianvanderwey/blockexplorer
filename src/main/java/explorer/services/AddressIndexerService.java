package explorer.services;

import explorer.events.AddressesIndexedEvent;
import explorer.events.BlockHashReceivedEvent;
import explorer.model.BlockIncludingTransactionDetails;
import explorer.model.BlockCounter;
import explorer.repository.BlockCounterRepository;
import explorer.model.Utxo;
import explorer.repository.UtxoRepository;
import explorer.model.UtxoSpent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class AddressIndexerService implements ApplicationListener<BlockHashReceivedEvent> {

    Logger logger = Logger.getLogger(AddressIndexerService.class.getName());
    @Autowired
    BitcoinRpcService bitcoinRpcService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private BlockCounterRepository blockCounterRepository;

    @Autowired
    private UtxoRepository utxoRepository;

    private static int startBlock = 2_410_000;

    @EventListener(ApplicationReadyEvent.class)
    private void startSyncingBlockExplorer() {
        startAddressIndexer();
    }

    //After our AddressIndexer is finished we will receive events from BlockchainListener with new block hashes
    @Override
    public void onApplicationEvent(BlockHashReceivedEvent blockHashReceivedEvent) {
        String hash = blockHashReceivedEvent.getHash();
        logger.info("RECEIVED BLOCK HASH : " + hash);
        saveTransactionOutputsFromBlock(hash);
        updateTransactionInputsFromBlock(hash);

        saveBlockCounter(bitcoinRpcService.getBlockIncludingTransactions(hash).height);

        logger.info("INDEXED ADDRESSES FOR BLOCK : " + hash);
    }

    //AddressIndexer will process blocks from startBlock to the latest block known from bitcoin node in batches of the batchSize.
    //After address indexing has finished we will publish an AddressesIndexedEvent and we will listen to new incoming blocks from BlockExplorerService
    private void startAddressIndexer() {

        int batchSize = 10000;

        startBlock = getStartBlock();
        logger.info("INDEXING ADDRESSES FROM START BLOCK TO END BLOCK : " + startBlock + " / " + bitcoinRpcService.getBlockCount());

        List<Integer> blocks = new ArrayList<>();
        // Retrieve all utxo's from blocks between our startblock and end block
        for (int i = startBlock; i <= bitcoinRpcService.getBlockCount(); ) {
            blocks.add(i);
            i++;
            if (i % batchSize == 0 && i != 0) {
                saveAndUpdateUtxos(blocks);
                blocks = new ArrayList<>();
            }
        }

        saveAndUpdateUtxos(blocks);

        logger.info("FINISHED INDEXING ADDRESSES");

        applicationEventPublisher.publishEvent(new AddressesIndexedEvent(this));

    }


    //First save the outputs from a batch of blocks. After that update outputs that have been spent.
    //To improve performance we use a parallel stream to process the batch quicker
    //Save the latest block number indexed to database
    private void saveAndUpdateUtxos(List<Integer> blocks) {

        Instant startTime = Instant.now();

        blocks.parallelStream().forEach(blockNumber -> {
            saveTransactionOutputsFromBlock(bitcoinRpcService.getBlockHash(blockNumber));
        });
        blocks.parallelStream().forEach(blockNumber -> {
            updateTransactionInputsFromBlock(bitcoinRpcService.getBlockHash(blockNumber));
        });

        Instant endTime = Instant.now();

        saveBlockCounter(blocks.get(blocks.size() - 1));
        printDuration(startTime, endTime, startBlock, bitcoinRpcService.getBlockCount(), blocks.get(0), blocks.get(blocks.size() - 1));
    }

    //Save all the utxos
    private void saveTransactionOutputsFromBlock(String blockHash) {
        List<Utxo> utxos = getTransactionOutputsFromBlock(blockHash);
        utxoRepository.saveAll(utxos);
    }

    //Update the utxos that have been used as inputs these utxo's have been spent
    private void updateTransactionInputsFromBlock(String blockHash) {
            List<UtxoSpent> utxosSpent = getTransactionInputsFromBlock(blockHash);
            List<Utxo> utxos = new ArrayList<>();
            utxosSpent.forEach(utxoSpent -> {
                        Optional<Utxo> utxoOptional = utxoRepository.findByUtxoId(utxoSpent.getUtxoId());
                        if (utxoOptional.isPresent()) {
                            Utxo utxo = utxoOptional.get();
                            utxo.setSpentTxId(utxoSpent.getSpentTxId());
                            utxo.setSpent(true);
                            utxos.add(utxo);
                        }
                    });
            utxoRepository.saveAll(utxos);
    }

    //Get the corresponding block related to the block hash and get all the Utxo's from VOUT in a transaction
    private List<Utxo> getTransactionOutputsFromBlock(String blockHash) {
        BlockIncludingTransactionDetails blockIncludingTransactionDetails = bitcoinRpcService.getBlockIncludingTransactions(blockHash);
        List<Utxo> outPuts = blockIncludingTransactionDetails.tx.stream().map(transaction -> TransactionParser.getOutputsFromTransaction(transaction))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return outPuts;
    }

    //Get the corresponding block related to the block hash and get all the Utxo's from VIN in a transaction
    private List<UtxoSpent> getTransactionInputsFromBlock(String blockhash) {
        BlockIncludingTransactionDetails blockIncludingTransactionDetails = bitcoinRpcService.getBlockIncludingTransactions(blockhash);
        List<UtxoSpent> utxos = blockIncludingTransactionDetails.tx.stream().map(transaction -> TransactionParser.getInputsFromTransaction(transaction))
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return utxos;
    }

    private int getStartBlock() {
        Optional<BlockCounter> blockCounterOptional = blockCounterRepository.findById(BlockCounter.BLOCK_COUNTER);
        if (blockCounterOptional.isPresent()) {
            startBlock = blockCounterOptional.get().getEndBlock();
        } else {
            startBlock = this.startBlock;
            blockCounterRepository.save(new BlockCounter(BlockCounter.BLOCK_COUNTER, startBlock));
        }
        return startBlock;
    }

    private void saveBlockCounter(int blockNumber) {
        BlockCounter blockCounter = blockCounterRepository.findById(BlockCounter.BLOCK_COUNTER).get();
        blockCounter.setEndBlock(blockNumber);
        blockCounterRepository.save(blockCounter);
    }

    private void printDuration(Instant startTime, Instant endTime, Integer startBlock, Integer endBlock, Integer batchStartBlock, Integer batchEndBlock) {
        Long l = endTime.getEpochSecond() - startTime.getEpochSecond();
        logger.info("FINISHED INDEXING ADDRESSES FOR BLOCKS : " + batchStartBlock + " / " + batchEndBlock + "");
        logger.info("DURATION " + l + " SECONDS");
        logger.info("BLOCKS TO BE INDEXED : " + batchEndBlock + " /  " + endBlock);
    }
}
