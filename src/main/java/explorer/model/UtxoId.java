package explorer.model;

import java.util.Objects;

public class UtxoId {

    private String txId;

    private Integer vout;

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public Integer getVout() {
        return vout;
    }

    public void setVout(Integer vout) {
        this.vout = vout;
    }

    public UtxoId() {
    }

    public UtxoId(String txId, Integer vout) {
        this.txId = txId;
        this.vout = vout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UtxoId utxoId = (UtxoId) o;
        return this.txId.equals(utxoId.txId) && vout.equals(utxoId.vout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(txId, vout);
    }
}


