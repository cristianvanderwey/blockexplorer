package explorer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Vin {
    public String coinbase;
    @JsonIgnore
    public String txinwitness;
    public Object sequence;
    public String txid;
    public int vout;
    public ScriptSig scriptSig;
}
