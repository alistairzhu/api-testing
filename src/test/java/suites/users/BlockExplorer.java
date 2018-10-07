package suites.users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.google.gson.Gson;
import data.ServiceContract;
import data.User;

import io.restassured.http.*;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import io.restassured.RestAssured;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.Reporter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import common.TestBase;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class BlockExplorer extends TestBase {
    public static final String CookieValue = ".ASPXANONYMOUS=QczrAYbODPBvRI50YzJsoYkPYM_dmfORqN0m5P7aTXVYQKqcU-5bNQzVEisMz3rDDahHmNYoIQ7eTbsUuz7ibfruBcQ0Xd8bBafqGgnPK87G_8o26ZmDWuSK-YpHE5ee8pOleA2; lfSessionKey_.laserfiche.com=Bw9xW1%2fzwzPhwlVTx6QX31kBxC0f1DMp2jg0eW3mJgR%2fmH4ftAQnuggqrYV0CsaG; acsApiRootUrl_.laserfiche.com=https%3a%2f%2fv-tor-2016c-2.laserfiche.com%2fACS%2f; lfCloud_.laserfiche.com=N4IgkgdgzgLghhAxgUyiAXAbVGAIhgZgBoQAlZAEwEsAnZRGAVRoBsMQALGGABynQD0AuAFdq3GnCosAdDFQwZLOFGQ0AZlUQdkMxAHsAtgICCYqjAAqk6QJAlcqRDSo8YVfRHZnxAAmtSbCRgBhB47KLiMDZsAL5EOPjoAIwk5NR0DMxs6JzcfIICAO7IAEZwiChQUHIKSipqmtq6BsbKqhpaOgKlNPpFqjIqPAAeAPx0PPoAvDQAtABspeoAHADsyABMyPYgjlDOru6e7Lj6iCKGyBAwaMGh4bnq%2biwUanNQL1QUIPGJGAAWNKUWj0JisdhcXj8ITPGiGGryWD1DpNHR6IwCdRzZIUBYUAELAg7BxOFxuDxeXIAMX08Lu4AeSRAcIRvwS4CSK2BGTB2Uh%2bUKUA4KngKMaXRamNKPEMu32hwpJ1yAAU%2blUoL4zDAjHBjl57p5HiAZXK%2fpyMABOHmgrIQ9AQEQsFikg7k%2fXsFUiUosLS%2bFV0%2bAsXz7KgAcwgal2ISNzJ43t9iCmNCDbyg4cjNHZ%2f3QmxtmXBOTy0MK8yWqw221qyPaEuaGOMAHUyiYKIYqBA7K7FR7cuQpumdTQAJ5atsdqiwSQew1hZklcrjrzmx4ABnzfPtxZ4hQAbnMh3NNqvkgtEEfxZ1660BABhESwIxqAMpuAsUxLyfRPWUgDEwiXAQVgWAEAFY1k2UCVlAgQeGUA09jJI5KW8Sp9BEG5oyZCI0IwmBswtdBQI3O0iyhHchH3Q9j1Pc9NkvNEpWMe9HyuGgXyDD92wgL9pz%2fAQO3kGgIDfAQLlYtQGQVd0UNyFidTYhkYzndgwz6EQeAIx4FhIwsBV4PcDzpI8TzPC9ayvdEb3kp92MDUTW243if08f99F3NRdyoZAinlJClSpEAAHkPJoLyfKw2N2EQFh0J%2bFckjWXT%2bVyciYQEChzgYyUGwECzGJkTKLiuG4XM7a4w19YUMvOKABEcdRRBYRQuEMX9mxYVpkEsfQABkGks3RWr8t1kOVEAAAlkBYTTZ2NHQZt%2bABdWIgA%3d%3d%3d; BPMSTS=77u/PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgiPz48U2VjdXJpdHlDb250ZXh0VG9rZW4gcDE6SWQ9Il81ZGMxZmY4Zi0wMDAxLTRhMTQtYTE1OC1kN2YyNzhlNGY0NDAtM0ExMUE1M0M4ODhCNTAwODkzQUQyODE2ODhGMDA4RDYiIHhtbG5zOnAxPSJodHRwOi8vZG9jcy5vYXNpcy1vcGVuLm9yZy93c3MvMjAwNC8wMS9vYXNpcy0yMDA0MDEtd3NzLXdzc2VjdXJpdHktdXRpbGl0eS0xLjAueHNkIiB4bWxucz0iaHR0cDovL2RvY3Mub2FzaXMtb3Blbi5vcmcvd3Mtc3gvd3Mtc2VjdXJlY29udmVyc2F0aW9uLzIwMDUxMiI+PElkZW50aWZpZXI+Qnc5eFcxL3p3elBod2xWVHg2UVgzMWtCeEMwZjFETXAyamcwZVczbUpnUi9tSDRmdEFRbnVnZ3FyWVYwQ3NhRzwvSWRlbnRpZmllcj48L1NlY3VyaXR5Q29udGV4dFRva2VuPg==; ASP.NET_SessionId=foomvdhh3kdgnwatyww0mr5m";

    public Set all_Accounts = new HashSet();

    private ServiceContract currentTestContract;
    private static String testContractId;

    //private  int indexNumber = 540881;

    private  int indexNumber =541235;

    private ArrayList aList;

    public static Response response;

    String [] hashArray = new String [3];

    @BeforeClass
    public void setUp() {
        RestAssured.basePath = "";
        Reporter.log("Set Up", true);
        currentTestContract = new ServiceContract("TestServContract-a-c17","descriptiondd");
    }

    @Test
    public void getBlockHashByIndex() {

        for (int i = 0;  i < 3; i++ ){
            Reporter.log("Read Service Contract", true);
            int index = indexNumber + i;

            String hash =  given().cookie("Cookie", CookieValue).
                    when().
                    get("block-index/" + index ).
                    then().
                    statusCode(200).
                    extract().
                    path("blockHash").toString();
                    //body("data.type", containsString("EventVersionInfo`1"));
                    // body("data.props.version.major", containsString("[1]"));
                    // body("data.props..data.data.", equalTo("[1]"));
                   hashArray [i] = hash ;
                    //        body("blockHash", containsString("0000000000000000000df2cc375707a423e3be646ae4ed6580f327b3587292b8"));
        }

        for (int i = 0;  i < 3; i++ ){

            System.out.println( hashArray [i]  + "---");
        }
    }
/*
    @DataProvider(name="seasonsAndNumberOfRaces")
    public Object[][] createTestDataRecords() {
        return new Object[][] {
                {"2017",20},
                {"2016",21},
                {"1966",9}
        };
    }
*/

    @Test ( dependsOnMethods = "getBlockHashByIndex")
    public void getBlockByHash() throws Exception{



        Thread.sleep(4000);
        Reporter.log("Update updateServiceContract", true);


        String hash =  given().cookie("Cookie", CookieValue).
                when().
                get("block/" + hashArray [0] ).
                then().
                statusCode(200).
                extract().
                path("tx").toString();

        String hashTrim = hash.substring(1, hash.length()-1);


        aList= new ArrayList(Arrays.asList(hashTrim.split(",")));


        for(int i=0;i<aList.size();i++)
        {
            //System.out.println("............"+aList.get(i));
        }


    }




   // @Test ( dependsOnMethods = "getBlockByHash")

    @Test ( dependsOnMethods = "getBlockByHash")
    public void getTranactionById() throws Exception{

        //String transactionId = aList.get(1).toString().trim() ; //"5bf665dab35e96e43a2b9d401abc863e37a905b7b150d0f27a1d41b142e7efed";
        String transactionId = "5bf665dab35e96e43a2b9d401abc863e37a905b7b150d0f27a1d41b142e7efed";

        System.out.println(".....transactionId......."+transactionId +".....transactionId.......");

        Thread.sleep(1000);
        Reporter.log("Get all versions ServiceContract", true);

       // currentTestContract = new ServiceContract("TestServContract-a-29e-dd","description-a");
        //currentTestContract.setName("TestEdi-b--2ted-a-01");
        //    ResponseSpecification userSpec = currentTestContract.getContractSpec();

        response =  given().cookie("Cookie", CookieValue).
                when().
                get("tx/" + transactionId).
                then().
                statusCode(200).
                extract().response();

        String jsonAsString = response.asString();

        List<String> txs = response.path("vin.addr");

        List<String> rideStates = response.path("vin.addr");

        List<Float> inAmount = response.path("vin.value");

        List<Float> outAmount = response.path("vout.value");

        Float inValue = 0f;

        for (Float flt: inAmount){

            inValue = inValue + flt ;
        }

        String str = Float.toString(inValue);
        System.out.println("+---IjsonAsString+" + jsonAsString);


        final JSONObject obj = new JSONObject(jsonAsString);

        System.out.println( "+++++++object txs +++++++"+ obj.length());

        final JSONArray vin = obj.getJSONArray("vin");


        double vin_Total = 0;

        for (int i = 0; i < vin.length(); i++){
            JSONObject vin_addr = vin.getJSONObject(i);
            double  amount = vin_addr.getDouble("value");
            vin_Total = vin_Total +  amount;

            System.out.println(i + "+++++++addr+++++++++"+ vin_addr.getString("addr"));
            System.out.println(i + "+++++++vin_value+++++++++"+ vin_addr.getDouble("value"));

        }
        System.out.println("---vin_Total---" + vin_Total);

        final JSONArray vout = obj.getJSONArray("vout");



     //   final JSONObject vin_addr = vin.getJSONObject(0);


        final JSONObject vout_value = vout.getJSONObject(0);
        final JSONObject key = vout.getJSONObject(1);


        String addresses = vout_value.getJSONObject("scriptPubKey").getJSONArray("addresses").get(0).toString();


     //   System.out.println("+++++++addresses+++++++++"+ addresses);
     //   System.out.println("+++++++vout_value+++++++++"+ vout_value.getDouble("value"));





    }

public Float sumList ( List<Float> outAmount){

    Float inValue = 0f;

    for (Float flt: outAmount){

        inValue = inValue + flt ;
    }
return inValue;

}



    public Float sumAmount( List<Float> floatList ){
        Float inValue = 0f;

        for (Float flt: floatList){

            inValue = inValue + flt ;
        }

        return inValue;
    }


}
