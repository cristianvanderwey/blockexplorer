package explorer.services;

import explorer.model.AddressBalance;
import explorer.model.Utxo;
import explorer.repository.UtxoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class BlockExplorerService {

    @Autowired
    UtxoRepository utxoRepository;

    public AddressBalance getAddressBalance(String address){

        ArrayList<Utxo> utxos = utxoRepository.findByAddress(address);

        ArrayList<Utxo> spentUtxos = utxos.stream()
                .filter(utxo -> utxo.getSpent() == true)
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Utxo> unspentUtxos = utxos.stream()
                .filter(utxo -> utxo.getSpent() == false)
                .collect(Collectors.toCollection(ArrayList::new));

        BigDecimal totalReceived = utxos.stream()
                .map(utxo -> utxo.getValue())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSpend = spentUtxos.stream()
                .map(utxo -> utxo.getValue())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalUnspent = unspentUtxos.stream()
                .map(utxo -> utxo.getValue())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new AddressBalance(utxos, spentUtxos, totalReceived, totalSpend, totalUnspent);
    }

}
