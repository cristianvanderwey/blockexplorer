package explorer.model;

import java.math.BigDecimal;
import java.util.ArrayList;

public class AddressBalance {

    ArrayList<Utxo> utxosIn;

    ArrayList<Utxo> utxosOut;

    BigDecimal totalReceived;

    BigDecimal totalSpend;

    BigDecimal totalUnspent;

    public ArrayList<Utxo> getUtxosIn() {
        return utxosIn;
    }

    public void setUtxosIn(ArrayList<Utxo> utxosIn) {
        this.utxosIn = utxosIn;
    }

    public ArrayList<Utxo> getUtxosOut() {
        return utxosOut;
    }

    public void setUtxosOut(ArrayList<Utxo> utxosOut) {
        this.utxosOut = utxosOut;
    }

    public BigDecimal getTotalReceived() {
        return totalReceived;
    }

    public void setTotalReceived(BigDecimal totalReceived) {
        this.totalReceived = totalReceived;
    }

    public BigDecimal getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(BigDecimal totalSpend) {
        this.totalSpend = totalSpend;
    }

    public BigDecimal getTotalUnspent() {
        return totalUnspent;
    }

    public void setTotalUnspent(BigDecimal totalUnspent) {
        this.totalUnspent = totalUnspent;
    }

    public AddressBalance(ArrayList<Utxo> utxosIn, ArrayList<Utxo> utxosOut, BigDecimal totalReceived, BigDecimal totalSpend, BigDecimal totalUnspent) {
        this.utxosIn = utxosIn;
        this.utxosOut = utxosOut;
        this.totalReceived = totalReceived;
        this.totalSpend = totalSpend;
        this.totalUnspent = totalUnspent;
    }
}
