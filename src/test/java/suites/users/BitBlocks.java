package suites.users;

public class BitBlocks {

    private  int blockNumber ;
    private  String hash ;
    private  int time ;
    private  String bits ;
    private  String transactions ;
    private  int size ;
    private  int height ;

    public BitBlocks (){
    }

    public BitBlocks (int blockNumber, String hash, int time, String bits, String transactions, int size, int height ){
        this.blockNumber = blockNumber;
        this.hash = hash;
        this.time = time;
        this.bits = bits;
        this.transactions = transactions;
        this.size = size;
        this.height = height;

    }

    public  int getBlockNumber(){
        return blockNumber;
    }

    public  String getHash(){
        return hash;
    }

    public  int getTime(){
        return time;
    }

    public  String getBits(){
        return bits;
    }

    public  String getTransactions(){
        return transactions;
    }

    public  int getSize(){
        return size;
    }

    public  int getHeight(){
        return height;
    }

    @Override
    public String toString()
    {
        return "BitBlocks: = " + blockNumber + ", time=" + time + "";
    }
}
