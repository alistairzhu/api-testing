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


public class TransactionsByAccount extends TestBase {


        public Set all_Accounts = new HashSet();

        private ServiceContract currentTestContract;
        private static String testContractId;

        //private  int indexNumber = 540881;

        private  int indexNumber =541235;

        private ArrayList aList;

        public static Response response;


        @Test
        public void tranactionByAccount() throws Exception {
            // String address = "3PvethXtsqaD9CkMrj28HtK31aXKM6FfuY";
            //String address = "1M8s2S5bgAzSSzVTeL7zruvMPLvzSkEAuv";
             String address = "1GmPZeLn93CJnu8r4hdeFfRtQyF3mXNRQy";  // Why 


            //String address = "3D2oetdNuZUqQHPJmcMDDHYoqkyNVsFk9r";
            //String  address = "16ftSEQ4ctQFDtVZiUBusQUjRrGhM3JYwe"; //--only Buy
            //String address = "3Cbq7aT1tY8kMxWLbitaG7yT6bPbKChq64";
             //String address = "183hmJGRuTEi2YDCWy5iozY8rZtFwVgahM";
            //String address = "1FeexV6bAHb8ybZjqQMjJrcCrHGW9sb6uF";
            //String address = "18rnfoQgGo1HqvVQaAN4QnxjYE7Sez9eca";
            //String address = "1HQ3Go3ggs8pFnXuHVHRytPCq5fGG8Hbhx";

            // String address = "3PvethXtsqaD9CkMrj28HtK31aXKM6FfuY";
            // String address = "3PvethXtsqaD9CkMrj28HtK31aXKM6FfuY";
            // String address = "3PvethXtsqaD9CkMrj28HtK31aXKM6FfuY";
            // String address = "3PvethXtsqaD9CkMrj28HtK31aXKM6FfuY";
            // String address = "16g2TW2hbFoov3DThSRBdnETaS94bFvq5v";


            //String address = "1NDyJtNTjmwk5xPNhjgAMu4HDHigtobu1s"; // robot- big?
            //String address = "1CMbVZV7xbAiFFauuAf3tgZRhwLKXmMUZL";
            //String address = "3PvethXtsqaD9CkMrj28HtK31aXKM6FfuY";
            //String address = "15NQthxeLSwMtEaXJFM7YUCf59LzmFjkeH";
            //String address = "1923qxU74HWWz75LgWTsPE4FT9Zyd6n6bv";--?machine--?
            //String address = "1DWxysF7GPRYGShNxL5ux2N2JLRa9rbE6k";- ?
           // address = "19G5kkYvjawZiYKhFeh8WmfBN31pdP94Jr";-!
            //String address = "3D15C8yuFRiMYPfptEP6EJH1GKAqE6F1oG"; !
            //String address = "3MWqbpfzxgojEAah6PMZoZPdUPUTuyTpan";--
           // String address = "33ZNiyx5Z5CMkULX7ENvcKKxFNCzGJv5vQ";--
           // String address = "1aXzEKiDJKzkPxTZy9zGc3y1nCDwDPub2";--
            //String address = "1F34duy2eeMz5mSrvFepVzy7Y1rBsnAyWC";-----------
            //String address = "39HtpgpJUAWvTD3tdDTyE3HTZNnR7YkaXf";

            //String address = "12Xbk56k2vEPd2cp9jPErPptQNR5E5Da6K";

            try {
                response = given().
                        when().
                        get("addr/" + address).
                        then().
                        statusCode(200).
                        extract().response();
            }catch (Exception e){

            }
            String jsonAsString = response.asString();

            // System.out.println("+---IjsonAsString+" + jsonAsString);

            JSONObject obj = new JSONObject(jsonAsString);
            //JSONArray txsArray = obj.getJSONArray("transactions");

            //System.out.println("+++++++object txs +++++++" + txsArray.length());
            ArrayList<String> list = new ArrayList<String>();

            JSONArray jsonArray = obj.getJSONArray("transactions");
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    list.add(jsonArray.get(i).toString());
                }
            }

            List<Transaction> transaction =  new ArrayList();

            // Reorder list with begin with the oldest
            Collections.reverse(list);

            // Open and process each transaction
            for (int i = 0; i < list.size(); i++) {
                String buyFlag = "";
                String txid = list.get(i);
                try {
                    response = given().
                        when().
                        get("tx/" + txid).
                        then().
                        //statusCode(200).
                                extract().response();
                } catch (Exception e) {
                    continue;
                }

                if (response.statusCode() == 200) {
                    String jsonTransAsString = response.asString();

                    // System.out.println("+---IjsonAsString+" + jsonAsString);
                    JSONObject objTrans = new JSONObject(jsonTransAsString);

                    JSONArray vinArray = objTrans.getJSONArray("vin");
                    JSONArray voutArray = objTrans.getJSONArray("vout");
                    double valueOut = objTrans.getDouble("valueOut");
                    int time = objTrans.getInt("time");

                    int vinLength = vinArray.length();
                    int voutLength = voutArray.length();

                    double vinValuePerTrans = 0;

                    //check vin

                    for (int j = 0; j < vinLength; j++) {
                        try {
                            //if ((vinArray.getJSONObject(j).get("addr") != null)) {
                            if (address.equals(vinArray.getJSONObject(j).get("addr"))) {

                                buyFlag = "-";
                                break;
                            }
                        } catch (StringIndexOutOfBoundsException e) {
                            continue;
                        }
                    }
                    //  System.out.println("+++++vinValuePerTrans++++++" + vinValuePerTrans);

                    //vout
                    for (int m = 0; m < voutLength; m++) {
                        JSONObject object = voutArray.getJSONObject(m);

                        //String str = object.getJSONObject("scriptPubKey").getJSONArray("addresses") ;
                        try {
                            String str = object.getJSONObject("scriptPubKey").optString("addresses");
                            String strTrimed = str.substring(2, str.length() - 2);
                            //System.out.println("+++++str++++++" + strTrimed);

                            if (address.equals(strTrimed)) {
                                buyFlag = "+";
                                break;
                            }

                        } catch (StringIndexOutOfBoundsException e) {
                            continue;
                        }
                    }
                    String price = getInstantPrice(convertTimestamp(time));
                    System.out.println("+++++Trans++++++" + convertTimestamp(time) + "--" + txid + "---" + buyFlag + "---" + valueOut  + "--" + price);

                    // Adding result to the list
                    transaction.add(new Transaction(convertTimestamp(time), txid, buyFlag, valueOut, price));
                }else{
                    continue;
                }

                }

                //Write result to Excel
                ExcelWriter.writeToExcel(transaction, address);
        }


    }
