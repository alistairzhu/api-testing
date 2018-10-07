package suites.users;


class Transaction {
    private String time;
    private String txid;
    private String buyFlag;
    private double valueOut;
    private String price;

    Transaction(String time, String txid, String buyFlag, double valueOut, String price){
        this.time = time;
        this.txid = txid;
        this.buyFlag = buyFlag;
        this.valueOut = valueOut;
        this.price = price;

    }

    public  String getTime(){
        return time;
    }

    public  String getTxid(){
        return txid;
    }

    public  String getBuyFlag(){
        return buyFlag;
    }

    public  double getValueOut(){
        return valueOut;
    }

    public  String getPrice(){ return price;  }

}