package explorer.services;

import explorer.model.Tx;
import explorer.model.UtxoSpent;
import explorer.model.Vin;
import explorer.model.Utxo;
import explorer.model.UtxoId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


class TransactionParser {

    public static List<Utxo> getOutputsFromTransaction(Tx tx){
        List<Utxo> utxos = tx.vout.stream().map( vout -> {

            String addressString = vout.scriptPubKey.address;
            if(addressString == null){
                addressString = vout.scriptPubKey.hex;}

            if(addressString.length() > 500){
                addressString = addressString.substring(0, 500);
            }

            return new Utxo(addressString, new UtxoId(tx.txid, vout.n), vout.value, false, null);

        }).collect(Collectors.toList());

        return utxos;
    }
    public static List<UtxoSpent> getInputsFromTransaction(Tx tx){
        List<UtxoSpent> utxos = new ArrayList<>();

        for(int i = 0 ; i < tx.vin.size(); i++){
            Vin vin = tx.vin.get(i);

            UtxoId utxoId = new UtxoId();
            utxoId.setVout(i);
            utxoId.setTxId(vin.txid);

            utxos.add(new UtxoSpent(utxoId, tx.txid));
        }
        return utxos;
    }


}
