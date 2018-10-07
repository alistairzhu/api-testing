package Blocks;
import java.math.BigDecimal;
import java.util.List;
import com.google.gson.Gson;

import javax.xml.crypto.Data;

public class TransactionsByAccount {



    private static int pagesTotal;

    private List<Data> vin;


    public int getPagesTotal() {
        return this.pagesTotal;
    }
    public void setPagesTotal(int num) {
        this.pagesTotal = num;
    }

    public void setVin(List<Data> vin) { this.vin = vin; }
    public List<Data> getVin() { return vin; }


    public String toString() {
        return String.format("pagesTotal:%d,vin:%s", pagesTotal, vin);
    }


    public static void printAny() {
        System.out.println(pagesTotal);
    }
}
