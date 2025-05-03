package projekt_PC2T;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import projekt_PC2T_pokyny.VlozeniPokynu;
import projekt_PC2T_pokyny.SmazPokyny;
import projekt_PC2T_pokyny.StudentAkce;

public class VypisKonzole {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		List<StudentAkce> ulozeneAkce = new ArrayList<>();
		
		
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
				System.out.println("7 ... Výpis obecného studijního průměru studenta");
				System.out.println("8 ... Vypiš celkový počet zapsaných studentů v DB");
				System.out.println("9 ... Vypiš počet studentů v jednotlivých skupinách tli, kb");
				System.out.println("10... Ulož studenta do souboru");
				System.out.println("11... Načti studenta ze souboru");
				System.out.println("12... Ulož akci na smazání studenta do fronty");
				System.out.println("13... Proveď všechny uložené akce");

				
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
					 System.out.println("Zadejte ID studenta k vymazání:");
					    int id = sc.nextInt();
					    StudentAkce akce = new SmazPokyny(id);
					    try {
					        akce.proved();
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
				case 7:
				    VlozeniPokynu pokyny7 = new VlozeniPokynu();
				    pokyny7.vypocitejPrumerZnámek();
				    break;
				case 8:
				    VlozeniPokynu pokyny8 = new VlozeniPokynu();
				    pokyny8.vypisPocetStudentu();
				    break;
				case 9:
				    VlozeniPokynu pokyny9 = new VlozeniPokynu();
				    pokyny9.vypisPocetStudentuVeSkupinach();
				    break;
				case 10:
				    System.out.println("Zadejte ID studenta k uložení:");
				    int idUloz = sc.nextInt();
				    System.out.println("Zadejte název souboru (např. student.txt):");
				    String souborUloz = sc.next();
				    VlozeniPokynu pokynUloz = new VlozeniPokynu();
				    pokynUloz.ulozStudentaDoSouboru(idUloz, souborUloz);
				    break;
				case 11:
				    System.out.println("Zadejte název souboru k načtení:");
				    String souborNacti = sc.next();
				    VlozeniPokynu pokynNacti = new VlozeniPokynu();
				    pokynNacti.nactiStudentaZeSouboru(souborNacti);
				    break;
				case 12:
				    System.out.println("Zadejte ID studenta, kterého chcete SMAZAT později:");
				    int idSmaz = sc.nextInt();
				    ulozeneAkce.add(new SmazPokyny(idSmaz));
				    System.out.println("Akce uložena.");
				    break;
				case 13:
				    System.out.println("Provádím všechny uložené akce:");
				    for (StudentAkce a : ulozeneAkce) {
				        try {
				            a.proved();
				        } catch (SQLException e) {
				            System.out.println("Chyba při provádění akce: " + e.getMessage());
				        }
				    }
				    ulozeneAkce.clear();
				    System.out.println("Všechny akce byly provedeny.");
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
	
