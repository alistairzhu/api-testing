package suites.users;

import java.util.HashSet;
import java.util.Set;

public class AccountBook {
    private  String address;
    private  String transactionId ;

    public AccountBook (){
    }

    public AccountBook (String name, String transactionId){
        this.address = name;
        this.transactionId = transactionId;
    }

    public  String getAddress(){
        return address;
    }

    public  String getTransactionId(){
        return transactionId;
    }

    @Override
    public String toString()
    {
        return "AccountBook: [address=" + address + ", transactionId=" + transactionId + "]";
    }
}
