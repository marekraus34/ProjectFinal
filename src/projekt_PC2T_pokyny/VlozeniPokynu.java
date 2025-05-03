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

        
        try (ResultSet rs = prStmt.getGeneratedKeys()) {
            if (rs.next()) {
                int noveId = rs.getInt(1);
                System.out.println("ID nového uživatele: " + noveId);
                return noveId;  
            }
        }
    } catch (SQLException e) {
        System.out.println("Chyba při vkládání uživatele: " + e.getMessage());
    }

    return -1; 
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


public void smazaniStudenta(int uzivatelId) throws SQLException {
    Connection conn = Databaze.getDBPripojeni();


    String deleteZnamky = "DELETE FROM znamky WHERE uzivatel_id = ?";
    try (PreparedStatement pstmtZnamky = conn.prepareStatement(deleteZnamky)) {
        pstmtZnamky.setInt(1, uzivatelId);
        pstmtZnamky.executeUpdate();
    }


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




public static String prevedNaMorseovku(String text) {
    String abeceda = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ";
    String[] morse = {
        ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..",
        ".---", "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", 
        "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..", "|" 
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
            if (obor.equalsIgnoreCase("tli")) {
                vysledek = prevedNaMorseovku(jmeno + " " + prijmeni);
                System.out.println("Dovednost (Morseovka): " + vysledek);
            } else if (obor.equalsIgnoreCase("kb")) {
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

public void vypocitejPrumerZnámek() {
    Connection conn = Databaze.getDBPripojeni();
    String sql = "SELECT AVG(znamka) AS prumer FROM znamky";

    try (PreparedStatement prStmt = conn.prepareStatement(sql);
         ResultSet rs = prStmt.executeQuery()) {

        if (rs.next()) {
            double prumer = rs.getDouble("prumer");
            System.out.printf("Průměr všech známek: %.2f%n", prumer);
        } else {
            System.out.println("Nebyla nalezena žádná známka.");
        }

    } catch (SQLException e) {
        System.out.println("Chyba při výpočtu průměru známek: " + e.getMessage());
    }
}

public void vypisPocetStudentu() {
    Connection conn = Databaze.getDBPripojeni();
    String sql = "SELECT COUNT(*) AS pocet FROM uzivatel";

    try (PreparedStatement prStmt = conn.prepareStatement(sql);
         ResultSet rs = prStmt.executeQuery()) {

        if (rs.next()) {
            int pocet = rs.getInt("pocet");
            System.out.println("Celkový počet studentů: " + pocet);
        } else {
            System.out.println("Nepodařilo se načíst počet studentů.");
        }

    } catch (SQLException e) {
        System.out.println("Chyba při výpočtu počtu studentů: " + e.getMessage());
    }
}

public void vypisPocetStudentuVeSkupinach() {
    Connection conn = Databaze.getDBPripojeni();
    String sql = "SELECT LOWER(obor) AS obor, COUNT(*) AS pocet " +
                 "FROM uzivatel " +
                 "WHERE LOWER(obor) IN ('tli', 'kb') " +
                 "GROUP BY LOWER(obor)";

    try (PreparedStatement prStmt = conn.prepareStatement(sql);
         ResultSet rs = prStmt.executeQuery()) {

        System.out.println("Počet studentů v jednotlivých skupinách:");
        System.out.println("-----------------------------------------");

        while (rs.next()) {
            String obor = rs.getString("obor").toUpperCase();
            int pocet = rs.getInt("pocet");
            System.out.println("Skupina: " + obor + " – " + pocet + " studentů");
        }

        System.out.println("-----------------------------------------");

    } catch (SQLException e) {
        System.out.println("Chyba při výpisu počtu studentů ve skupinách: " + e.getMessage());
    }
    }
    public void ulozStudentaDoSouboru(int idStudenta, String nazevSouboru) {
        Connection conn = Databaze.getDBPripojeni();
        String sql = "SELECT * FROM uzivatel WHERE id = ?";

        try (PreparedStatement prStmt = conn.prepareStatement(sql)) {
            prStmt.setInt(1, idStudenta);
            ResultSet rs = prStmt.executeQuery();

            if (rs.next()) {
                String jmeno = rs.getString("jmeno");
                String prijmeni = rs.getString("prijmeni");
                int rok = rs.getInt("rokNarozeni");
                String obor = rs.getString("obor");

                try (java.io.PrintWriter writer = new java.io.PrintWriter(nazevSouboru)) {
                    writer.println("ID: " + idStudenta);
                    writer.println("Jméno: " + jmeno);
                    writer.println("Příjmení: " + prijmeni);
                    writer.println("Rok narození: " + rok);
                    writer.println("Obor: " + obor);
                    System.out.println("Student byl uložen do souboru " + nazevSouboru);
                } catch (java.io.IOException e) {
                    System.out.println("Chyba při zápisu do souboru: " + e.getMessage());
                }

            } else {
                System.out.println("Student s ID " + idStudenta + " nebyl nalezen.");
            }

        } catch (SQLException e) {
            System.out.println("Chyba při načítání studenta: " + e.getMessage());
        }
    

}

    
    
    public void nactiStudentaZeSouboru(String nazevSouboru) {
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(nazevSouboru))) {
            System.out.println("Načtený student ze souboru:");
            System.out.println("---------------------------");
            String radek;
            while ((radek = reader.readLine()) != null) {
                System.out.println(radek);
            }
            System.out.println("---------------------------");
        } catch (java.io.IOException e) {
            System.out.println("Chyba při načítání souboru: " + e.getMessage());
        }
    }
}
