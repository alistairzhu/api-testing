package Blocks;

import common.JDBCHelper;
import common.ProjConstants;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Reporter;
import suites.users.AccountBook;
import suites.users.BitBlocks;
import suites.users.ToolsBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static common.ProjConstants.BLOCKEXPLORER_URL;
import static io.restassured.RestAssured.given;
import static suites.users.JDBCDaoImpl.SELECT_SQL_QUERY;
import static suites.users.JDBCDaoImpl.batchinsertAccountBook;

public class BlockTools {


    public static void main(String[] args){
        //new BlockTools().getBlockHashByIndex();
        try {
            findBlocksBetweenTimestamp("2009-01-09 03:16:28 GMT", "2009-01-09 04:12:40 GMT");  //"2009-01-09 03:16:28 GMT"
        }catch (Exception e){}
    }

    /**
     * This function returns an array of BlockHash by Block number range
     * @param startBlock
     * @param processNumber
     * @return
     */
    public String [] getBlockHashByIndex(int startBlock, int processNumber) throws Exception{

        String [] hashArray = new String [processNumber];

        for (int i = 0;  i < processNumber; i++ ){
            Reporter.log("Read BlockHash ByIndex", true);
            int index = startBlock + i;

            Response response = ToolsBox.getResponseWithRetry(ProjConstants.BLOCKEXPLORER_URL + "block-index/" + index);
            String jsonAsString = response.asString();
            JSONObject obj = new JSONObject(jsonAsString);
            String hash = obj.getString("blockHash");
           // System.out.println("=====BlockHash are: " + hash);

            hashArray[i] = hash ;
        }
        return hashArray;
    }

    public static String [][] getBlockNumberAndHash(int startBlock, int processNumber) throws Exception{

        String [][] hashArray = new String [processNumber] [2];

        for (int i = 0;  i < processNumber; i++ ){
            Reporter.log("Read BlockHash ByIndex", true);
            int index = startBlock + i;

            Response response = ToolsBox.getResponseWithRetry(ProjConstants.BLOCKEXPLORER_URL + "block-index/" + index);
            String jsonAsString = response.asString();
            JSONObject obj = new JSONObject(jsonAsString);
            String hash = obj.getString("blockHash");
            // System.out.println("=====BlockHash are: " + hash);

            hashArray[i][0] = String.format ("%d", index) ;
            hashArray[i][1] = hash ;
        }

        return hashArray;
    }

    /**
     * This function return an arrayList of transactions of a block
     * @param blockHash
     * @return
     * @throws Exception
     */
    public ArrayList getTranactionsFromBlock(String blockHash ) throws Exception{
        ArrayList aList;

        Reporter.log("getTranactionsFromBlock---", true);
/*
        Response response = ToolsBox.getResponseWithRetry(ProjConstants.BLOCKEXPLORER_URL + "block/" + blockHash );
        String jsonAsString = response.asString();
        JSONObject obj = new JSONObject(jsonAsString);
        String hash = obj.getString("tx");
*/
        String hash =  given().
                when().
                get(ProjConstants.BLOCKEXPLORER_URL + "block/" + blockHash ).
                then().
                statusCode(200).
                extract().
                path("tx").toString();

        String hashTrim = hash.substring(1, hash.length()-1).replaceAll("\\s","");
        aList= new ArrayList(Arrays.asList(hashTrim.split(",")));
        System.out.println(" transactions list ...hashTrim........."+aList);

        return aList;
    }

    public  String getItemFromBlock(String blockHash , String itemName) throws Exception{
        ArrayList aList;

        Reporter.log("getItemFromBlock---", true);
/*
        Response response = ToolsBox.getResponseWithRetry(ProjConstants.BLOCKEXPLORER_URL + "block/" + blockHash );
        String jsonAsString = response.asString();
        JSONObject obj = new JSONObject(jsonAsString);
        String hash = obj.getString("tx");
*/
        String time =  given().
                when().
                get(ProjConstants.BLOCKEXPLORER_URL + "block/" + blockHash ).
                then().
                statusCode(200).
                extract().
                path(itemName).toString();

        //String hashTrim = hash.substring(1, hash.length()-1).replaceAll("\\s","");
        //aList= new ArrayList(Arrays.asList(hash.split(",")));
        System.out.println(" time........."+ time );
        return time;

    }

    public static List<BitBlocks>  getObjectFromBlock(String [][] blockHash ) throws Exception{
        List<BitBlocks> bitBlockObjects = new ArrayList<>();

        for (int i = 0; i < blockHash.length; i++) {

            System.out.println(" urls........."+ BLOCKEXPLORER_URL + "block/" + blockHash[i][1]);
            Response response = ToolsBox.getResponseWithRetry(BLOCKEXPLORER_URL + "block/" + blockHash[i][1]);
            String jsonAsString = response.asString();

            // Just use 300 to check if the block contains enough information or not.
            if (jsonAsString.length() > 300) {

                JSONObject obj = new JSONObject(jsonAsString);

                int blockNumber = Integer.parseInt(blockHash[i][0]);
                JSONArray transactionArray = obj.getJSONArray("tx");
                String transactions = transactionArray.toString();
                int time = obj.getInt("time");
                String bits = obj.getString("bits");
                int size = obj.getInt("size");
                int height = obj.getInt("height");
                String hash = blockHash[i][1];
                //
                List<AccountBook> accountBook = new ArrayList();

                BitBlocks bitBlocks = new BitBlocks (blockNumber, hash, time, bits, transactions, size, height );

                bitBlockObjects.add(bitBlocks);

            } else {
                System.out.println("+++++API response not contains enough information for block : " + blockHash[i]);
                continue;
            }
        }


        return bitBlockObjects;
    }


    public static ArrayList findBlocksBetweenTimestamp (String startTime, String endTime) throws Exception {

        ArrayList <String> hashArray = new ArrayList();


        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        long startUnixTimestamp = ToolsBox.convertDateToUnixTimestamp(startTime);  //"yyyy-MM-dd HH:mm:ss"


        long endUnixTimestamp = ToolsBox.convertDateToUnixTimestamp(endTime);  //"yyyy-MM-dd HH:mm:ss"

        System.out.println( "startUnixTimestamp--- " +startUnixTimestamp );
        System.out.println( "endtUnixTimestamp--- " + endUnixTimestamp );

        try
        {
            con = JDBCHelper.getConnection();
            if ( con == null )
            {
                System.out.println( "Error getting the connection. Please check if the DB server is running" );
                return hashArray ;
            }
            ps = con.prepareStatement( "SELECT BLOCK_HASH FROM BITBLOCKS WHERE BLOCK_TIME BETWEEN " + startUnixTimestamp + " AND " + endUnixTimestamp );

            rs = ps.executeQuery();
            System.out.println( "retrivePerson => " + ps.toString() );
            while ( rs.next() )
            {
                String blockHash = rs.getString( "BLOCK_HASH" ) ;
                hashArray.add(blockHash);
            }

        }
        catch ( SQLException e )
        {
            throw e;

        }

        finally
        {
            try
            {
                JDBCHelper.closeResultSet( rs );
                JDBCHelper.closePrepaerdStatement( ps );
                JDBCHelper.closeConnection( con );
            }
            catch ( SQLException e )
            {
                throw e;
            }
        }
        System.out.println( "hashArray.toString---- => " + hashArray.toString());
        return hashArray;

    }



    public static void writeBlocksInTable () throws Exception {
        int startBlock = 1;
        int numberOfBlocks = 100;

        // Get array of block hash
        BlockTools blockTools = new BlockTools();
        String [] hashArray = blockTools.getBlockHashByIndex(startBlock, numberOfBlocks);

        // In each block, get Tranactions array
        for (int i = 0; i < hashArray.length; i++) {
            String blockTime = blockTools.getItemFromBlock(hashArray[i],"time");


        }
    }



}
