package suites.users;

import Blocks.BlockTools;
import common.JdbcConnection;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import org.testng.Assert;
import org.testng.annotations.Test;

import static common.ProjConstants.BLOCKEXPLORER_URL;
import static common.ProjConstants.GDAX_URL;
import static io.restassured.RestAssured.given;
import static suites.users.JDBCDaoImpl.batchinsertAccountBook;


public class FilterAccountsFromTranaction {


    /**
     * Get transactions from blocks, extract account addresses and group them by users, then write into DB.
     * @throws Exception
     */
    @Test
    //@Test(retryAnalyzer = RetryAnalyzer.class)
    public static void check () throws Exception {

        // Get array of block hash
        BlockTools blockTools = new BlockTools();
        String [] hashArray = blockTools.getBlockHashByIndex(113224, 12);

        // In each block, get Tranactions array
        for (int i = 0; i < hashArray.length; i++) {
            ArrayList aList = blockTools.getTranactionsFromBlock(hashArray[i]);

            // In each Tranaction, get account address and write into DB
            for(int j = 0; j<aList.size(); j++){
                filterAccounts(aList.get(j).toString());
            }
        }
    }


    /**
     * This function open Transaction through GET by API.
     * @param transactionId
     * @throws Exception
     */

    public static void filterAccounts(String transactionId) throws Exception {

        Response response = ToolsBox.getResponseWithRetry(BLOCKEXPLORER_URL + "tx/" + transactionId);

        if (!response.equals(null)) {

            String jsonAsString = response.asString();
            JSONObject obj = new JSONObject(jsonAsString);
            JSONArray inArray = obj.getJSONArray("vin");
            JSONArray outArray = obj.getJSONArray("vout");

            //
            List<AccountBook> accountBook = new ArrayList();

            // Fliter "vin" only when the section is not empty
            if (inArray.getJSONObject(0).optString("addr").length() > 1) {
                String accountInString = "";

                for (int i = 0; i < inArray.length(); i++) {
                    String addr = inArray.getJSONObject(i).optString("addr");
                    //Only adding when it does not contains the addr
                    if (!accountInString.contains(addr)) {
                        accountInString = accountInString + ", " + addr;
                    }
                }
                accountInString = accountInString.replaceAll("\\s","");

                accountBook.add(new AccountBook(accountInString, transactionId));
            }

            // Fliter "vout" only when the section is not empty
            if (outArray.getJSONObject(0).getJSONObject("scriptPubKey").optString("addresses").length() > 1) {
                String accountOutName = outArray.getJSONObject(0).getJSONObject("scriptPubKey").optString("addresses");
                String accountOutString = "";

                for (int j = 0; j < outArray.length(); j++) {
                    JSONObject object = outArray.getJSONObject(j);

                    try {
                        String str = object.getJSONObject("scriptPubKey").optString("addresses");
                        String strTrimed = str.substring(2, str.length() - 2);
                        //System.out.println("++++vout++++++" + strTrimed);
                        accountOutString = accountOutString + "," + strTrimed;
                        accountOutString = accountOutString.replaceAll("\\s","");
                    } catch (StringIndexOutOfBoundsException e) {
                        continue;
                    }
                }

                accountBook.add(new AccountBook(accountOutString, transactionId));
            }

            // Insert accountBook into DB
            try {
                batchinsertAccountBook(accountBook);
            } catch (SQLException e) {

            }

        } else {System.out.println("+++++API response is NULL !!!!" );}

    }
}
