package explorer.model;

import java.math.BigDecimal;
import java.util.List;

public class Tx {
    public String txid;
    public String hash;
    public int version;
    public int size;
    public int vsize;
    public int weight;
    public BigDecimal locktime;
    public List<Vin> vin;
    public List<Vout> vout;
    public String hex;
    public double fee;
}
