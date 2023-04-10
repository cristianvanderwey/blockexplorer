package explorer.model;

public class UtxoSpent {

    UtxoId utxoId;

    String spentTxId;

    public UtxoId getUtxoId() {
        return utxoId;
    }

    public void setUtxoId(UtxoId utxoId) {
        this.utxoId = utxoId;
    }

    public String getSpentTxId() {
        return spentTxId;
    }

    public void setSpentTxId(String spentTxId) {
        this.spentTxId = spentTxId;
    }

    public UtxoSpent(UtxoId utxoId, String spentTxId) {
        this.utxoId = utxoId;
        this.spentTxId = spentTxId;
    }
}
