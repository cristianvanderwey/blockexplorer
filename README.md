# blockexplorer
A simple bitcoin address indexer written in spring-boot

When you are running a Bitcoin-core node there is an option to index txid's but not an option to index addresses.
This is unfortunate because we can't query address information from our Bitcoin node to get the balance related to addresses.
There are options to import watch addresses in our Bitcoin-core wallet but there are no solutions to get address information at scale from our bitcoin node.
There have been some proposals to implement address indexing in Bitcoin but it hasn't been implemented yet since there is a lot of debate about it.
read:

(https://bitcoincore.reviews/14053)

This repo consists of a simple implementation on how to index Bitcoin addresses using mongodb in a spring-boot application connected to a bitcoin-node.
The entrypoint is the AddressIndexerService which starts indexing from startBlock by querying the blockchain and finishes at the lastblock by calling getblockcount from bitcoin-rpc. It will save the latest block indexed in case of rebooting the application.
After our latest block has been indexed we listen to bitcoin node's zmq to retrieve the latest blocks.

To get the transactions related to an address:
```sh
localhost:8080/api/address-info?address={String address}
```

Prerequisites:

 1. Synced bitcoin node
 2. MongoDb
 3. Change application.properties-example to application.properties to set the urls for zmq and bitcoin-node
