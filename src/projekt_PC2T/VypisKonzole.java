package projekt_PC2T;
import java.sql.SQLException;
import java.util.Scanner;
import projekt_PC2T_pokyny.VlozeniPokynu;
import projekt_PC2T_pokyny.VolbaPokynu;

public class VypisKonzole {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int volba = 0;
		boolean kod = true;
		int iterace = 0;
		
		while (kod) {
			if (iterace++ > 0) { 
				System.out.println(System.lineSeparator());
			}
				System.out.println("Vyberte požadovaný úkon");
				System.out.println("1 ... Vložení studenta s přiřazením skupiny");
				System.out.println("2 ... Vložení nové známky podle ID studenta");
				System.out.println("3 ... Nalezení studentů podle ID");
				System.out.println("4 ... Propouštění studenta z univerzity");
				System.out.println("5 ... Spuštění dovedností dle zadáného ID");
				System.out.println("6 ... Výpis abecedně seřazených studentů jednotlivých skupin");
				System.out.println("7 ... Vypis celkové počtu studentů v jednotlivých skupinách");
				System.out.println("8 ... Uložení vybraného studenta do souboru");
				System.out.println("9 ... Načtení vybraného studenta ze souboru");
				
				System.out.println("Zadejte číslo volby: ");
				volba = sc.nextInt();
				
				switch (volba) {
				case 1:
					System.out.println("Zadejte jméno studenta");
					String jmeno = sc.next();
					System.out.println("Zadejte příjmení studenta");
					String prijmeni = sc.next();
					System.out.println("Zadejte rok narození");
					int rokNarozeni = sc.nextInt();
					System.out.println("Zadejte Skupinu: tli nebo kb");
					String obor = sc.next();
					
					String vlozeniPokynu = "INSERT INTO uzivatel (jmeno, prijmeni, rokNarozeni, obor) VALUES ('" + jmeno + "', '" + prijmeni + "', " + rokNarozeni + ", '" + obor + "')";

                    VlozeniPokynu vp = new VlozeniPokynu();
                    vp.provedeniVlozeniPokynu(vlozeniPokynu);
                    
               
                    
                    
                    break;
					
				case 2:
				    System.out.println("Zadejte ID studenta:");
				    int ID1 = sc.nextInt();
				    System.out.println("Vložte známku uživateli:");
				    int znamka = sc.nextInt();

				    // Zavoláš metodu pridejZnamku
				    VlozeniPokynu pokyny = new VlozeniPokynu();
				    try {
				        pokyny.pridejZnamku(ID1, znamka);
				    } catch (SQLException e) {
				        e.printStackTrace();
				    }

				    break;
				    
				case 3:
					 VlozeniPokynu pokyny3 = new VlozeniPokynu();
				        try {
				            pokyny3.vypisStudenty();
				        } catch (SQLException e) {
				            e.printStackTrace();
				        }
					break;
					
				case 4:
				    System.out.println("Zadejte ID studenta, kterého chcete propustit:");
				    int idMazani = sc.nextInt();

				    VlozeniPokynu pokyny4 = new VlozeniPokynu();
				    try {
				        pokyny4.smazaniStudenta(idMazani);
				    } catch (SQLException e) {
				        e.printStackTrace();
				    }
				    break;
				case 5:
					System.out.print("Zadejte ID studenta: ");
		            int idDovednost = sc.nextInt();
		            sc.nextLine();
		            VlozeniPokynu.spustDovednostStudenta(idDovednost);
		            break;
				case 6:
					VlozeniPokynu pokyny5 = new VlozeniPokynu();
					try {
						pokyny5.vypisStudentyTliKbSerazene();
					} catch(SQLException e) {
						e.printStackTrace();
					}
					break;
				case 0:
					kod = false;
					System.out.println("Program ukončen");
					break;
				default:
					System.out.println("Neplatná volba");
					break;
		}
	
					}
				

}
}
	
