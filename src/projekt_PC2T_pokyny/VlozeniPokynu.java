package projekt_PC2T_pokyny;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import DBPripojeni.Databaze;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.List;



public class VlozeniPokynu {
	public VlozeniPokynu() {}

public void provedeniVlozeniPokynu(String VlozeniPokynu) {
	if (VlozeniPokynu == null) {
		throw new NullPointerException("Data nesmí být nulové");
	}
	else if(VlozeniPokynu.isEmpty()){
		throw new IllegalArgumentException("Data musí něco obsahovat");
	}
	Connection conn = Databaze.getDBPripojeni();

    try (PreparedStatement prStmt = conn.prepareStatement(VlozeniPokynu)) {
        int radku = prStmt.executeUpdate();
        System.out.println("Student byl úspěšně vložen do databáze. Počet záznamů: " + radku);
    } catch (SQLException e) {
        System.out.println("Chyba při vkládání studenta: " + e.getMessage());
    }
}
public int vlozeniuzivatele(String jmeno, String prijmeni, int rokNarozeni, String obor) {
    if (jmeno == null || prijmeni == null || obor == null)
        throw new NullPointerException("Jméno, příjmení a obor musí být vyplněny");

    Connection conn = Databaze.getDBPripojeni();
    String Vlozeniuzivatele = "INSERT INTO uzivatel (jmeno, prijmeni, rokNarozeni, obor) VALUES (?, ?, ?, ?)";

    try (PreparedStatement prStmt = conn.prepareStatement(Vlozeniuzivatele, Statement.RETURN_GENERATED_KEYS)) {
        prStmt.setString(1, jmeno);
        prStmt.setString(2, prijmeni);
        prStmt.setInt(3, rokNarozeni);
        prStmt.setString(4, obor);
        prStmt.executeUpdate();
        System.out.println("Nový uživatel: " + jmeno + " byl úspěšně vložen do databáze.");

        // Získání vygenerovaného ID
        try (ResultSet rs = prStmt.getGeneratedKeys()) {
            if (rs.next()) {
                int noveId = rs.getInt(1);
                System.out.println("ID nového uživatele: " + noveId);
                return noveId;  // vrátí ID nového studenta
            }
        }
    } catch (SQLException e) {
        System.out.println("Chyba při vkládání uživatele: " + e.getMessage());
    }

    return -1; // Pokud se nepodařilo vložit
}



public void pridejZnamku(int uzivatelId, int znamka) throws SQLException {
    Connection conn = Databaze.getDBPripojeni();
    String sql = 
        "INSERT INTO znamky (uzivatel_id, znamka) " +
        "VALUES (?, ?)";

    try (PreparedStatement prStmt = conn.prepareStatement(sql)) {
        prStmt.setInt(1, uzivatelId);
        prStmt.setInt(2, znamka);
        prStmt.executeUpdate();
        System.out.println(
            "Známka " + znamka + " byla přidána studentovi s ID = " + uzivatelId
        );
    }
}



//vypis studentu s idčkama
public void vypisStudenty() throws SQLException{
	Connection conn = Databaze.getDBPripojeni();
	String sql = "SELECT * FROM uzivatel";
	
	try(PreparedStatement pstmt = conn.prepareStatement(sql);
			 ResultSet rs = pstmt.executeQuery()) {

			        System.out.println("Seznam studentů:");
			        System.out.println("--------------------------------------");

			        while (rs.next()) {
			            int id = rs.getInt("ID");
			            String jmeno = rs.getString("jmeno");
			            String prijmeni = rs.getString("prijmeni");
			            int rokNarozeni = rs.getInt("rokNarozeni");
			            String obor = rs.getString("obor");

			            System.out.println("ID: " + id + ", Jméno: " + jmeno + " " + prijmeni +
			                               ", Rok narození: " + rokNarozeni + ", Obor: " + obor);
			        }

			        System.out.println("--------------------------------------");
			    }





}

//mazaní studentu
public void smazaniStudenta(int uzivatelId) throws SQLException {
    Connection conn = Databaze.getDBPripojeni();

    // Nejprve smažeme všechny známky, které patří uživateli (kvůli cizím klíčům)
    String deleteZnamky = "DELETE FROM znamky WHERE uzivatel_id = ?";
    try (PreparedStatement pstmtZnamky = conn.prepareStatement(deleteZnamky)) {
        pstmtZnamky.setInt(1, uzivatelId);
        pstmtZnamky.executeUpdate();
    }

    // Pak smažeme samotného uživatele
    String deleteUzivatel = "DELETE FROM uzivatel WHERE ID = ?";
    try (PreparedStatement pstmtUzivatel = conn.prepareStatement(deleteUzivatel)) {
        pstmtUzivatel.setInt(1, uzivatelId);
        int affectedRows = pstmtUzivatel.executeUpdate();

        if (affectedRows > 0) {
            System.out.println("Student s ID " + uzivatelId + " byl úspěšně smazán.");
        } else {
            System.out.println("Student s ID " + uzivatelId + " nebyl nalezen.");
        }
    }
}

//samotné funkce morseovky a hashe


public static String prevedNaMorseovku(String text) {
    String abeceda = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ";
    String[] morse = {
        ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", // A-I
        ".---", "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", // J-R
        "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..", "|" // S-Z + mezera
    };

    text = text.toUpperCase();
    StringBuilder vysledek = new StringBuilder();

    for (char c : text.toCharArray()) {
        int index = abeceda.indexOf(c);
        if (index >= 0) {
            vysledek.append(morse[index]).append(" ");
        }
    }

    return vysledek.toString().trim();
}
public static String vytvorHash(String text) {
    try {
        java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(text.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    } catch (Exception e) {
        return "Chyba při vytváření hashe.";
    }
}





//vyběr studenta podle jejich ID a skupiny a následný převod příjmení a jména MORSE





public static void spustDovednostStudenta(int id) {
    Connection conn = Databaze.getDBPripojeni();
    String sql = "SELECT jmeno, prijmeni, obor FROM uzivatel WHERE id = ?";

    try (PreparedStatement prStmt = conn.prepareStatement(sql)) {
        prStmt.setInt(1, id);
        ResultSet rs = prStmt.executeQuery();

        if (rs.next()) {
            String jmeno = rs.getString("jmeno");
            String prijmeni = rs.getString("prijmeni");
            String obor = rs.getString("obor");

            String vysledek;
            if (obor.equalsIgnoreCase("Telekomunikace")) {
                vysledek = prevedNaMorseovku(jmeno + " " + prijmeni);
                System.out.println("Dovednost (Morseovka): " + vysledek);
            } else if (obor.equalsIgnoreCase("Kyberbezpečnost")) {
                vysledek = vytvorHash(jmeno + prijmeni);
                System.out.println("Dovednost (Hash): " + vysledek);
            } else {
                System.out.println("Obor není podporován.");
            }
        } else {
            System.out.println("Student s ID " + id + " nebyl nalezen.");
        }

    } catch (SQLException e) {
        System.out.println("Chyba při načítání studenta: " + e.getMessage());
    }
}

public void vypisStudentyTliKbSerazene() throws SQLException {
    Connection conn = Databaze.getDBPripojeni();

    String sql = "SELECT * FROM uzivatel " +
                 "WHERE LOWER(obor) IN ('tli', 'kb') " +
                 "ORDER BY prijmeni ASC, jmeno ASC";

    try (PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        System.out.println("Seznam studentů z oborů TLI a KB (seřazeno abecedně):");
        System.out.println("------------------------------------------------------");

        while (rs.next()) {
            int id = rs.getInt("ID");
            String jmeno = rs.getString("jmeno");
            String prijmeni = rs.getString("prijmeni");
            int rok = rs.getInt("rokNarozeni");
            String obor = rs.getString("obor");

            System.out.println("ID: " + id + ", " +
                               "Jméno: " + jmeno + " " + prijmeni + ", " +
                               "Rok narození: " + rok + ", " +
                               "Obor: " + obor);
        }

        System.out.println("------------------------------------------------------");
    }
}




}
