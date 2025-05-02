package projekt_PC2T_pokyny;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class VolbaPokynu {
    public VolbaPokynu() {}
	private Connection conn;

    public VolbaPokynu(Connection conn) {
        this.conn = conn;
    }

    public boolean testUserExistence(String firstName, String lastName) {
        String sql = "SELECT 1 FROM student WHERE jmeno = ? AND prijmeni = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); 
        } catch (SQLException e) {
            System.err.println("Chyba při ověřování existence studenta:");
            e.printStackTrace();
            return false;
        }
    }
    public void ZvolitID() {
    	 Connection conn = DBConnection.getDBConnection();
    	 String ZvolitID = "SELECT ID, "
    }
}