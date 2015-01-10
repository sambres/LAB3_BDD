package bank;

import java.sql.*;
import java.util.Date;
import java.util.List;

/**
 * A simple implementation of the ReservationManager interface. Each object of
 * this class must create a dedicated connection to the database.
 * <p>
 * <b>Note: DO NOT alter this class's interface.</b>
 * 
 * @author Busca
 * 
 */
public class BankManagerImpl implements BankManager {

    // CLASS FIELDS
    //
    // example of a create table statement executed by createDB()
    private static final String CREATE_TABLE_DUMMY = "create table DUMMY (" + 
	    "ATT int, " + 
	    "primary key (ATT)" + 
	    ")";

    private Connection conn;
    private Statement stmt=null;
    private ResultSet rset;
    private ResultSetMetaData rsetMeta;
    
    /**
     * Creates a new ReservationManager object. This creates a new connection to
     * the specified database.
     * 
     * @param url
     *            the url of the database to connect to
     * @param user
     *            the login name of the user
     * @param password
     *            his password
     */
    public BankManagerImpl(String url, String user, String password) throws SQLException {
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(ClassNotFoundException exc)
        {
            System.out.println("Erreur de chargement du driver: \n"+exc.getMessage());
        }
        //création d'une connexion JDBC à la base
        conn = DriverManager.getConnection(url,user,password);

        // création d'un ordre SQL (statement)
        stmt = conn.createStatement();
        
        System.out.println("Connexion reussie");
    }
    
    /**
     * Creates the schema of the bank's database. This includes all the schema
     * elements : tables, and possibly triggers. The database is empty after
     * this method returns.
     * <p>
     * The method will execute a sequence of create table / trigger statements.
     * The statements are hard-coded, as shown in the example in the
     * <code>BankManagerImpl</code> class.
     * 
     * @throws SQLException
     *             if an SQL exception occurs
     */
    ///Contraintes d'intégrité: solde d'un compte bancaire ne doit pas etre négatif (add Balance)
    ///doit automatiquement journaliser les opérations eddectuées sur les comptes (getOperation())
    @Override
    public void createDB() throws SQLException {
	// TODO Auto-generated method stub
        String requete_creation1 = "CREATE TABLE Operations("
                + "Id INT AUTO_INCREMENT,"
                + "Transfer INT,"
                + "Account INT,"
                + "Date DATE,"
                + "PRIMARY KEY(Id),"
                + "FOREIGN KEY(Account) REFERENCES Account(Id));"; 
        String requete_creation2 = "CREATE TABLE Account("
                + "Id INT, "
                + "Balance REAL CHECK ( Balance >= 0.00),"
                + "primary key (Id));";
        
        String requete_trigger = "CREATE TRIGGER OperationTrig "
                + "AFTER UPDATE ON Account "
                + "FOR EACH ROW "
               + "INSERT INTO `operations`(`Transfer`, `Account`, `Date`) VALUES( NEW.Balance - OLD.Balance, OLD.Id,CURDATE()) ";
     
        try{
            stmt.executeUpdate(requete_creation2);
        }
        catch(SQLException exc)
        {
            System.out.println("Erreur de création de la table: \n"+exc.getMessage());
        }
        try{
            stmt.executeUpdate(requete_creation1);
        }
        catch(SQLException exc)
        {
            System.out.println("Erreur de création de la table: \n"+exc.getMessage());
        }
        
        stmt.executeUpdate(requete_trigger);
        
    }
    
     /**
     * Creates a new account with the specified number.
     * 
     * @param number
     *            the number of the account
     * @return <code>true</code> if the method succeeds and <code>false</code>
     *         otherwise
     * @throws SQLException
     *             if an SQL exception occurs
     * 
     */
    @Override
    public boolean createAccount(int number) throws SQLException {
	// TODO Auto-generated method stub
	String requete = "INSERT INTO Account values ("+ number + ",0);";
        int retour;
        
        retour = stmt.executeUpdate(requete);
        
        if(retour != 0)
            return true;
        else return false;
    }

    /**
     * Returns the balance of the specified account.
     * 
     * @param number
     *            the number of the account
     * @return the balance of the account
     * @throws SQLException
     *             if an SQL exception occurs
     */
    @Override
    public double getBalance(int number) throws SQLException {
	// TODO Auto-generated method stub
        rset = stmt.executeQuery("SELECT Balance FROM Account WHERE Id = " + number);
        
        if(rset.next())
        {
            return Double.parseDouble(rset.getString(1));
        }
        
	return 0;
    }

    /**
     * Adds the specified amount to the specified account. A call to this method
     * performs a deposit if the amount is a positive value, and a withdrawal
     * otherwise. A debit operation without insufficient funds must be refused.
     * 
     * @param number
     *            the number of the account
     * @param amount
     *            the amount to add to the account's balance
     * @return the new balance of the account, or -1.0 if the withdrawal could
     *         not be performed
     * @throws SQLException
     *             if an SQL exception occurs
     */
    @Override
    public double addBalance(int number, double amount) throws SQLException {
	// TODO Auto-generated method stub
	return 0;
    }

     /**
     * Transfers the specified amount between the specified accounts.
     * 
     * @param from
     *            the number of the debited account
     * @param to
     *            the number of the credited account
     * @param amount
     *            the amount to transfert
     * @return <code>true</code> if the method succeeds and <code>false</code>
     *         otherwise
     * @throws SQLException
     *             if an SQL exception occurs
     */
    @Override
    public boolean transfer(int from, int to, double amount) throws SQLException {
	// TODO Auto-generated method stub
	return false;
    }

    /**
     * Returns the list of operations on the specified account in the specified
     * time interval.
     * 
     * @param number
     *            the number of the account
     * @param from
     *            start date/time (inclusive)
     * @param to
     *            end date/time (inclusive)
     * @return the list of operations on the account
     * @throws SQLException
     *             if an SQL exception occurs
     */
    @Override
    public List<Operation> getOperations(int number, Date from, Date to) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

}
