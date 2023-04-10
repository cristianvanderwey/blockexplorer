package explorer.model;

import java.util.ArrayList;

public class BlockIncludingTransactionDetails {
    public String hash;
    public int confirmations;
    public int height;
    public int version;
    public String versionHex;
    public String merkleroot;
    public int time;
    public int mediantime;
    public long nonce;
    public String bits;
    public double difficulty;
    public String chainwork;
    public int nTx;
    public String previousblockhash;
    public String nextblockhash;
    public int strippedsize;
    public int size;
    public int weight;
    public ArrayList<Tx> tx;
}



