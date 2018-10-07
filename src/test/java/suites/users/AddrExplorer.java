package suites.users;

import common.TestBase;
import data.ServiceContract;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;

public class AddrExplorer extends TestBase {


    public Set all_Accounts = new HashSet();

    private ServiceContract currentTestContract;
    private static String testContractId;

    //private  int indexNumber = 540881;

    private  int indexNumber =541235;

    private ArrayList aList;

    public static Response response;


    @Test
    public void getTranactionByAddress() throws Exception {

        //String[] addArray = {"3PvethXtsqaD9CkMrj28HtK31aXKM6FfuY"};
        String[] addArray = {"3D2oetdNuZUqQHPJmcMDDHYoqkyNVsFk9r"};
        //String[] addArray = {"16ftSEQ4ctQFDtVZiUBusQUjRrGhM3JYwe"};




        for (int k=0; k<addArray.length; k++){


            String address = addArray[k];

            System.out.println(".....address......." + address + ".....address......");

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

            double vinValue = 0;
            double voutValue = 0;
    //vin
            for (int i = 0; i < txsArray.length(); i++) {

                JSONObject txsObject = txsArray.getJSONObject(i);
                JSONArray vinArray = txsObject.getJSONArray("vin");
                int vinLength = vinArray.length();

                double vinValuePerTrans = 0;
                //check vin
                for (int j = 0; j < vinLength; j++) {
                    if (address.equals(vinArray.getJSONObject(j).get("addr"))) {
                        vinValuePerTrans = vinValuePerTrans + vinArray.getJSONObject(j).getDouble("value");
                    }
                }
                //  System.out.println("+++++vinValuePerTrans++++++" + vinValuePerTrans);
                vinValue = vinValue + vinValuePerTrans;
         //vout
                JSONArray voutArray = txsObject.getJSONArray("vout");
                int voutLength = voutArray.length();

                double voutValuePerTrans = 0;

                for (int m = 0; m < voutLength; m++) {
                    JSONObject object = voutArray.getJSONObject(m);

                    //String str = object.getJSONObject("scriptPubKey").getJSONArray("addresses") ;
                    try{
                        String str = object.getJSONObject("scriptPubKey").optString("addresses") ;
                        String strTrimed = str.substring(2, str.length() - 2);
                        //System.out.println("+++++str++++++" + strTrimed);

                        if (address.equals(strTrimed)) {
                            voutValuePerTrans = voutValuePerTrans + object.getDouble("value");
                        }

                    }catch(StringIndexOutOfBoundsException e){
                        continue;
                    }

                }

                   System.out.println("+++++voutValuePerTrans++++++" + voutValuePerTrans);
                voutValue = voutValue + voutValuePerTrans;

            }

            System.out.println("Total OUT++++++" + vinValue);
            System.out.println("Total In++++++" + voutValue);


        }


    }
}
