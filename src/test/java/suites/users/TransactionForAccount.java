package suites.users;

import common.TestBase;
import data.ServiceContract;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.*;

import static io.restassured.RestAssured.given;
import static suites.users.ToolsBox.convertTimestamp;
import static suites.users.ToolsBox.getInstantPrice;


public class TransactionForAccount extends TestBase {


    public Set all_Accounts = new HashSet();

    private ServiceContract currentTestContract;
    private static String testContractId;

    //private  int indexNumber = 540881;

    private  int indexNumber =541235;

    private ArrayList aList;

    public static Response response;


    @Test
    public void tranactionByAddress() throws Exception {

        //String[] addArray = {"3PvethXtsqaD9CkMrj28HtK31aXKM6FfuY"};
        //String address = "3D2oetdNuZUqQHPJmcMDDHYoqkyNVsFk9r";
        //String[] addArray = {"16ftSEQ4ctQFDtVZiUBusQUjRrGhM3JYwe"};
        //String address = "3Cbq7aT1tY8kMxWLbitaG7yT6bPbKChq64";
       // String address = "183hmJGRuTEi2YDCWy5iozY8rZtFwVgahM";
        String address = "1FeexV6bAHb8ybZjqQMjJrcCrHGW9sb6uF";

        //    String address = "3D2oetdNuZUqQHPJmcMDDHYoqkyNVsFk9r";

        //String address = "3PvethXtsqaD9CkMrj28HtK31aXKM6FfuY";

            response = given().
                    when().
                    get("txs/?address=" + address).
                    then().
                    statusCode(200).
                    extract().response();

            String jsonAsString = response.asString();

            // System.out.println("+---IjsonAsString+" + jsonAsString);

            JSONObject obj = new JSONObject(jsonAsString);
            JSONArray txsArray = obj.getJSONArray("txs");

            //System.out.println("+++++++object txs +++++++" + txsArray.length());

            double totalValue = 0;
        List<Transaction> transaction =  new ArrayList();
            //vin
            for (int i = 0; i < txsArray.length(); i++) {


               String  txid = txsArray.getJSONObject(i).optString("txid");

                String buyFlag = "";

                JSONObject txsObject = txsArray.getJSONObject(i);
                JSONArray vinArray = txsObject.getJSONArray("vin");
                JSONArray voutArray = txsObject.getJSONArray("vout");
                double valueOut = txsObject.getDouble("valueOut");
                double valueIn = txsObject.getDouble("valueIn");
                int time = txsObject.getInt("time");


                int vinLength = vinArray.length();
                int voutLength = voutArray.length();

                double vinValuePerTrans = 0;
                //check vin


                for (int j = 0; j < vinLength; j++) {
                    if (address.equals(vinArray.getJSONObject(j).get("addr"))) {
                        buyFlag = "-";
                        break;
                    }
                }
                //  System.out.println("+++++vinValuePerTrans++++++" + vinValuePerTrans);

                //vout



                for (int m = 0; m < voutLength; m++) {
                    JSONObject object = voutArray.getJSONObject(m);

                    //String str = object.getJSONObject("scriptPubKey").getJSONArray("addresses") ;
                    try{
                        String str = object.getJSONObject("scriptPubKey").optString("addresses") ;
                        String strTrimed = str.substring(2, str.length() - 2);
                        //System.out.println("+++++str++++++" + strTrimed);

                        if (address.equals(strTrimed)) {
                           buyFlag = "+";
                           break;
                        }

                    }catch(StringIndexOutOfBoundsException e){
                        continue;
                    }

                }
                String price = getInstantPrice(convertTimestamp(time));
                System.out.println("+++++Trans++++++" + time + "--" + txid +"---" +  buyFlag + "---" + valueOut + "--" + price) ;
                    transaction.add(new Transaction(convertTime(time), txid, buyFlag,valueOut, price));

            }

            for( int n=0; n<transaction.size(); n++) {
                System.out.println("Total OUT++++++" + transaction.get(n).getTxid());
            }

        ExcelWriter.writeToExcel(transaction, address );
            //System.out.println("Total In++++++" + voutValue);

    }

    static String convertTime(int unixSeconds){

    // convert seconds to milliseconds
    Date date = new java.util.Date(unixSeconds*1000L);
    // the format of your date
    SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        return sdf.format(date);

    }

}
