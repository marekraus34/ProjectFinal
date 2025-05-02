package DBPripojeni;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.sqlite.SQLiteDataSource;

public class Databaze {
    private static volatile Connection databaze;

    private Databaze() {}

    public static Connection getDBPripojeni() {
        if (databaze == null) {
        	System.out.println("Aktuální pracovní složka je: " + System.getProperty("user.dir"));
            synchronized (Databaze.class) {
                if (databaze == null) {
                    try {
                        SQLiteDataSource ds = new SQLiteDataSource();
                        ds.setUrl("jdbc:sqlite:uzivatel.db");
                        databaze = ds.getConnection();
                        
                        try (Statement stmt = databaze.createStatement()) {
                        	String query = "CREATE TABLE IF NOT EXISTS uzivatel ( " +
                        			"ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                    "jmeno TEXT NOT NULL, " +
                                    "prijmeni TEXT NOT NULL, " +
                                    "rokNarozeni INTEGER NOT NULL, " +
                                    "obor TEXT NOT NULL )";
                            int rv = stmt.executeUpdate(query);
                            System.out.println("executeUpdate() returned " + rv);
                        }
                        System.out.println("Created database successfully");                                                                                                                                                          
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(0);
                    }
                   try (Statement stmt = databaze.createStatement()) {
                	   String query1 = "CREATE TABLE IF NOT EXISTS znamky ( " +
                               "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                               "uzivatel_id INTEGER NOT NULL, " +
                               "znamka INTEGER NOT NULL, " +
                               "FOREIGN KEY (uzivatel_id) REFERENCES uzivatel(ID))";
                	   int rv2 = stmt.executeUpdate(query1);
                       System.out.println("Vytvoření tabulky znamky: executeUpdate() returned " + rv2);
                       System.out.println("Created database successfully");
                   }catch (Exception e) {
                       e.printStackTrace();
                       System.exit(0);
                }
            }
        }
        }
        return databaze;
    }

    public static void closeConnection() {
        if (databaze != null) {
            try {
                databaze.close();
                databaze = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
