package suites.users;

import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static common.ProjConstants.GDAX_URL;
import static io.restassured.RestAssured.given;

public class ToolsBox {

    public static void main(String... args) throws Exception {

       // System.out.println(convertTimestamp(1536235980));

        //getInstantPrice("2017-06-30T16:56:00");
        getDailyPrice("2017-06-30");
    }

    public static String convertTimestamp(int unixSeconds){
        Date date = new java.util.Date(unixSeconds*1000L);
        // the format of your date
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        String str = sdf.format(date);
        return str.substring(0,10) + "T" + str.substring(11,16) + ":00";
    }

    /**
     *
     * @param dateString, format MUST be:  "2009-01-09 23:40:18 GMT"
     * @return
     * @throws Exception
     */
    public static long convertDateToUnixTimestamp(String dateString) throws  Exception{   //"2009-01-09 23:40:18 GMT"
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        Date date = format.parse(dateString);
        long timeStamp = date.getTime()/1000;
        return timeStamp;
    }


    public static String getInstantPrice(String time) {

        Response response ;
        System.out.println( GDAX_URL + "products/BTC-USD/candles?start=" + time + "&end=" + time.substring(0, time.length() - 2) + "05");
            response = given().
                    when().
                    get(GDAX_URL + "products/BTC-USD/candles?start=" + time + "&end=" + time.substring(0, time.length() - 2) + "05").
                    then().
                    statusCode(200).
                    extract().response();

        if (response.statusCode() == 200) {
            String str = "";
            String responseAsString = response.asString();

            if (responseAsString.length()<=3){
                str = getDailyPrice(time.substring(0,10));
            } else {
                List<String> priceList = Arrays.asList(responseAsString.split(","));
                str = priceList.get(1);
            }
            //System.out.println("+---IjsonAsString+" + str);
            return str;
        }
        return "";
    }



    public static String getDailyPrice(String date) {

        Response response ;

       // System.out.println("https://api.coindesk.com/v1/bpi/historical/close.json?start=" + date + "&end="+ date);

        response = given().
                when().
                get("https://api.coindesk.com/v1/bpi/historical/close.json?start=" + date + "&end="+ date).
                then().
                statusCode(200).
                extract().response();

        if (response.statusCode() == 200) {
            String responseAsString = response.asString();

            JSONObject obj = new JSONObject(responseAsString);
            JSONObject objBpi = obj.getJSONObject("bpi");
            String price = objBpi.optString(date);

            return price ;
        }
        return "";
    }

    public static Response getResponseWithRetry(String url) throws Exception{
        Response response = null;
        Response tempresponse;
        for(int i = 0; i < 5; i++) {
            tempresponse = given().
                    when().
                    get(url).
                    then().
                    extract().response();

            if (tempresponse.getStatusCode() == 200) {
                response = tempresponse;
                break;
            } else {

                Thread.sleep(10000);
            }
        }return response;
    }
}
