package suites.users;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Blocks.BlockTools;
import entity.Person;
import common.JDBCHelper;

/**
 * SQL:
 * create table PERSON (ID bigint not null, EMAIL varchar(255), FIRST_NAME varchar(255), JOINED_DATE date, LAST_NAME varchar(255), primary key (id))
 * This class is used to test CRUD operations on DB.
 *
 * @author preetham
 */
public class JDBCDaoImpl
{
    public static final String CREAT_SQL_QUERY     =  "CREATE TABLE bitBlocks (id SERIAL PRIMARY KEY, block_Number INTEGER NOT NULL, block_Hash  VARCHAR(100) NOT NULL, block_Time INTEGER NOT NULL, block_Bits TEXT NOT NULL, block_Transactions TEXT NOT NULL, block_Size INTEGER NOT NULL, block_Height INTEGER NOT NULL)";

    public static final String INSERT_BITBLOCKS_SQL_QUERY     =  "INSERT INTO bitBlocks (block_Number, block_Hash, block_Time, block_Bits, block_Transactions, block_Size, block_Height) VALUES(?,?,?,?,?,?,?);";

    public static final String INSERT_SQL_QUERY     = "INSERT INTO ACCOUNTADDR (ADDRESS ,TRANSACTION_ID) VALUES(?,?);";
    //public static final String INSERT_ACCOUNTBOOK_SQL_QUERY     = "INSERT INTO accounts(\"accoutName\",\"addressSet\") VALUES(?,?);";
    //public static final String INSERT_ACCOUNTBOOK_SQL_QUERY     = "INSERT INTO accounts(\"accoutName\",\"addressSet\") VALUES('qq41234','{qwer}')";
   // public static final String INSERT_SQL_QUERY     = "INSERT INTO PERSON(ID,FIRST_NAME,LAST_NAME,EMAIL,JOINED_DATE) VALUES(?,?,?,?,?)";
    public static final String UPDATE_SQL_QUERY     = "UPDATE PERSON SET FIRST_NAME=? WHERE ID=?";
    public static final String SELECT_SQL_QUERY     = "SELECT ID,FIRST_NAME,LAST_NAME,EMAIL,JOINED_DATE FROM PERSON WHERE ID=?";
    public static final String SELECT_ALL_SQL_QUERY = "SELECT ID,FIRST_NAME,LAST_NAME,EMAIL,JOINED_DATE FROM PERSON";
    public static final String DELETE_SQL_QUERY     = "DELETE FROM PERSON WHERE ID=?";
    public static final String DELETE_ALL_SQL_QUERY = "DELETE FROM PERSON";

    public static void main( String[] args )
    {
        try {
            String[][] blockHash = BlockTools.getBlockNumberAndHash(6000, 1000);
            List<BitBlocks>  blockObjects =  BlockTools.getObjectFromBlock(blockHash);
            batchInsertBitBlock( blockObjects );

        } catch (Exception e){}
    }

    private static void deleteAllRecords() throws SQLException
    {
        Connection con = null;
        PreparedStatement ps = null;
        try
        {
            con = JDBCHelper.getConnection();
            if ( con == null )
            {
                System.out.println( "Error getting the connection. Please check if the DB server is running" );
                return;
            }
            ps = con.prepareStatement( DELETE_ALL_SQL_QUERY );
            ps.execute();
            System.out.println( "deleteAllRecords => " + ps.toString() );
        }
        catch ( SQLException e )
        {
            throw e;

        }

        finally
        {
            try
            {
                JDBCHelper.closePrepaerdStatement( ps );
                JDBCHelper.closeConnection( con );
            }
            catch ( SQLException e )
            {
                throw e;
            }
        }
    }

    private static void deletePerson( int id ) throws SQLException
    {
        Connection con = null;
        PreparedStatement ps = null;
        try
        {
            con = JDBCHelper.getConnection();
            ps = con.prepareStatement( DELETE_SQL_QUERY );
            ps.setLong( 1, id );
            ps.execute();
            System.out.println( "deletePerson => " + ps.toString() );
        }
        catch ( SQLException e )
        {
            throw e;
        }

        finally
        {
            try
            {
                JDBCHelper.closePrepaerdStatement( ps );
                JDBCHelper.closeConnection( con );
            }
            catch ( SQLException e )
            {
                throw e;
            }
        }
    }

    private static Person retrivePerson( long id ) throws SQLException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Person person = new Person();
        try
        {
            con = JDBCHelper.getConnection();
            if ( con == null )
            {
                System.out.println( "Error getting the connection. Please check if the DB server is running" );
                return person;
            }
            ps = con.prepareStatement( SELECT_SQL_QUERY );
            ps.setLong( 1, id );
            rs = ps.executeQuery();
            System.out.println( "retrivePerson => " + ps.toString() );
            while ( rs.next() )
            {
                person.setId( rs.getLong( "ID" ) );
                person.setFirstName( rs.getString( "FIRST_NAME" ) );
                person.setLastName( rs.getString( "LAST_NAME" ) );
                person.setEmail( rs.getString( 4 ) ); // this is to show that we can retrive using index of the column
                person.setJoinedDate( rs.getDate( "JOINED_DATE" ) );
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
        return person;
    }

    private static void updatePersonFirstName( Person person ) throws SQLException
    {
        Connection con = null;
        PreparedStatement ps = null;

        try
        {
            con = JDBCHelper.getConnection();
            if ( con == null )
            {
                System.out.println( "Error getting the connection. Please check if the DB server is running" );
                return;
            }
            con.setAutoCommit( false );
            ps = con.prepareStatement( UPDATE_SQL_QUERY );
            ps.setString( 1, person.getFirstName() );
            ps.setLong( 2, person.getId() );
            ps.execute();
            System.out.println( "updatePersonFirstName => " + ps.toString() );
            con.commit();

        }
        catch ( SQLException e )
        {
            try
            {
                if ( con != null )
                {
                    con.rollback();
                    throw e;
                }
            }
            catch ( SQLException e1 )
            {
                throw e1;
            }
        }
        finally
        {
            try
            {
                JDBCHelper.closePrepaerdStatement( ps );
                JDBCHelper.closeConnection( con );
            }
            catch ( SQLException e )
            {
                throw e;
            }
        }

    }

    private static List<Person> retrivePersons() throws SQLException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Person> persons = new ArrayList<Person>();
        try
        {
            con = JDBCHelper.getConnection();
            if ( con == null )
            {
                System.out.println( "Error getting the connection. Please check if the DB server is running" );
                return persons;
            }
            ps = con.prepareStatement( SELECT_ALL_SQL_QUERY );
            rs = ps.executeQuery();
            System.out.println( "retrivePersons => " + ps.toString() );
            while ( rs.next() )
            {
                Person p = new Person();
                p.setId( rs.getLong( "ID" ) );
                p.setFirstName( rs.getString( "FIRST_NAME" ) );
                p.setLastName( rs.getString( "LAST_NAME" ) );
                p.setEmail( rs.getString( 4 ) ); // this is to show that we can retrive using index of the column
                p.setJoinedDate( rs.getDate( "JOINED_DATE" ) );
                persons.add( p );

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
        return persons;
    }


    public static void insertAccountBook( AccountBook accountBook ) throws SQLException
    {
        Connection con = null;
        PreparedStatement ps = null;
        try
        {
            con = JDBCHelper.getConnection();
            if ( con == null )
            {
                System.out.println( "Error getting the connection. Please check if the DB server is running" );
                return;
            }
            con.setAutoCommit( false );
            ps = con.prepareStatement( INSERT_SQL_QUERY );
            ps.setString( 1, accountBook.getAddress() );
            String setString = accountBook.getTransactionId();
            ps.setString( 2, setString );
            System.out.println( "..........." +  setString );
            System.out.println( "insertPerson => " + ps );

          //  ps = con.prepareStatement( "INSERT INTO accounts(\"accoutName\",\"addressSet\") VALUES('1FFUJF2fTfKoLz5CXDK3TbfZYVFZRKKuhX','{1FFUJF2fTfKoLz5CXDK3TbfZYVFZRKKuhX}')" );
            ps.execute();
            //System.out.println( "insertPerson => " + ps.toString() );
            con.commit();

        }
        catch ( SQLException e )
        {
            try
            {
                if ( con != null )
                {
                    con.rollback();
                }
            }
            catch ( SQLException e1 )
            {
                throw e1;
            }
            throw e;
        }
        finally
        {
            try
            {
                JDBCHelper.closePrepaerdStatement( ps );
                JDBCHelper.closeConnection( con );
            }
            catch ( SQLException e )
            {
                throw e;
            }
        }

    }

    /**
     * Batch insert a list data into DB.
     * @param accountBook
     * @throws SQLException
     */
    public static void batchinsertAccountBook( List<AccountBook> accountBook ) throws SQLException {

        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            dbConnection = JDBCHelper.getConnection();
            preparedStatement = dbConnection.prepareStatement(INSERT_SQL_QUERY);

            dbConnection.setAutoCommit(false);

            for (AccountBook book : accountBook) {
                preparedStatement.setString(1, book.getAddress());
                preparedStatement.setString(2, book.getTransactionId());

                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            dbConnection.commit();
            System.out.println("Record is inserted into DBUSER table!");

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            dbConnection.rollback();

        } finally {

            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }

    /**
     * Batch insert a list bitBlock data into DB BitBlock table .
     * @param bitBlock
     * @throws SQLException
     */
    public static void batchInsertBitBlock( List<BitBlocks> bitBlock ) throws SQLException {

        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            dbConnection = JDBCHelper.getConnection();
            if ( dbConnection == null )
            {
                System.out.println( "Error getting the connection. Please check if the DB server is running" );
                return;
            }

            preparedStatement = dbConnection.prepareStatement(INSERT_BITBLOCKS_SQL_QUERY);

            dbConnection.setAutoCommit(false);

            for (BitBlocks blook : bitBlock) {
                preparedStatement.setInt(1, blook.getBlockNumber());
                preparedStatement.setString(2, blook.getHash());
                preparedStatement.setInt(3, blook.getTime());
                preparedStatement.setString(4, blook.getBits());
                preparedStatement.setString(5, blook.getTransactions());
                preparedStatement.setInt(6, blook.getSize());
                preparedStatement.setInt(7, blook.getHeight());

                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            dbConnection.commit();
            System.out.println("Records inserted into table -- bitBlocks ");

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            dbConnection.rollback();

        } finally {

            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }
}
