package explorer.model;

import org.springframework.data.annotation.Id;

public class BlockCounter {

    public static final String BLOCK_COUNTER = "blockCounter";

    @Id
    private String blockCounter = BLOCK_COUNTER;

    private int startBlock;
    private int endBlock;

    public BlockCounter(String blockCounter, int startBlock) {

        this.blockCounter = blockCounter;
        this.startBlock = startBlock;

    }

    public String getBlockCounter() {
        return blockCounter;
    }

    public void setBlockCounter(String blockCounter) {
        this.blockCounter = blockCounter;
    }

    public int getStartBlock() {
        return startBlock;
    }

    public void setStartBlock(int startBlock) {
        this.startBlock = startBlock;
    }

    public int getEndBlock() {
        return endBlock;
    }

    public void setEndBlock(int endBlock) {
        this.endBlock = endBlock;
    }

    
}
