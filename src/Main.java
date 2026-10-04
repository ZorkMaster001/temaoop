import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/** Meniu de consola pentru administrarea elevilor si salilor. */
public class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final List<Elev> elevi = new ArrayList<>();
    private final List<Sala> sali = new ArrayList<>();

    public static void main(String[] args) {
        new Main().ruleaza();
    }

    private void ruleaza() {
        boolean continua = true;
        while (continua) {
            afiseazaMeniu();
            int optiune = citesteInt("Alege optiunea: ");
            try {
                switch (optiune) {
                    case 0: case 18: continua = false; System.out.println("Aplicatia s-a inchis."); break;
                    case 1: adaugaElev(); break;
                    case 2: creeazaSala(); break;
                    case 3: adaugaElevInSala(); break;
                    case 4: afiseazaElevi(elevi); break;
                    case 5: afiseazaSali(); break;
                    case 6: afiseazaEleviiUneiSali(); break;
                    case 7: cautaElevDupaId(); break;
                    case 8: mutaElev(); break;
                    case 9: stergeElev(); break;
                    case 10: stergeSala(); break;
                    case 11: filtreazaPromovati(true); break;
                    case 12: filtreazaPromovati(false); break;
                    case 13: filtreazaDupaMedie(); break;
                    case 14: filtreazaDupaVarsta(); break;
                    case 15: cautaDupaNume(); break;
                    case 16: afiseazaStatistici(); break;
                    case 17: sorteazaElevii(); break;
                    default: System.out.println("Optiune inexistenta. Alege un numar din meniu.");
                }
            } catch (IllegalArgumentException | IllegalStateException exceptie) {
                System.out.println("Eroare: " + exceptie.getMessage());
            }
            if (continua) System.out.println();
        }
    }

    private void afiseazaMeniu() {
        System.out.println("========== GESTIONARE ELEVI SI SALI ==========");
        System.out.println("1. Adauga elev                         10. Sterge sala");
        System.out.println("2. Creeaza sala                        11. Filtreaza elevii promovati");
        System.out.println("3. Adauga elev intr-o sala             12. Filtreaza elevii nepromovati");
        System.out.println("4. Afiseaza toti elevii                13. Filtreaza elevii dupa interval de medie");
        System.out.println("5. Afiseaza toate salile               14. Filtreaza elevii dupa varsta");
        System.out.println("6. Afiseaza elevii unei sali           15. Cauta elevi dupa nume");
        System.out.println("7. Cauta elev dupa ID                  16. Afiseaza statistici");
        System.out.println("8. Muta elev dintr-o sala in alta      17. Sorteaza elevii");
        System.out.println("9. Sterge elev                         18. Inchide aplicatia");
        System.out.println("0. Iesire");
    }

    private void adaugaElev() {
        int id = citesteInt("ID elev: ");
        if (gasesteElev(id) != null) throw new IllegalArgumentException("Exista deja un elev cu acest ID.");
        String nume = citesteText("Nume: ");
        int varsta = citesteInt("Varsta (5-100): ");
        double medie = citesteDouble("Medie (1-10): ");
        elevi.add(new Elev(id, nume, varsta, medie));
        System.out.println("Elevul a fost adaugat.");
    }

    private void creeazaSala() {
        int id = citesteInt("ID sala: ");
        if (gasesteSala(id) != null) throw new IllegalArgumentException("Exista deja o sala cu acest ID.");
        String denumire = citesteText("Denumire: ");
        int capacitate = citesteInt("Capacitate: ");
        sali.add(new Sala(id, denumire, capacitate, new ArrayList<>()));
        System.out.println("Sala a fost creata.");
    }

    private void adaugaElevInSala() {
        Sala sala = cereSala("ID sala: ");
        Elev elev = cereElev("ID elev: ");
        if (esteInscris(elev.getId())) throw new IllegalArgumentException("Elevul este deja inscris intr-o sala.");
        sala.adaugaElev(elev);
        System.out.println("Elevul a fost inscris in sala.");
    }

    private void afiseazaElevi(List<Elev> lista) {
        if (lista.isEmpty()) { System.out.println("Nu exista elevi de afisat."); return; }
        lista.forEach(System.out::println);
    }

    private void afiseazaSali() {
        if (sali.isEmpty()) { System.out.println("Nu exista sali."); return; }
        sali.forEach(System.out::println);
    }

    private void afiseazaEleviiUneiSali() {
        Sala sala = cereSala("ID sala: ");
        System.out.println("Elevii din " + sala.getDenumire() + ":");
        afiseazaElevi(sala.getElevi());
    }

    private void cautaElevDupaId() {
        Elev elev = gasesteElev(citesteInt("ID elev: "));
        System.out.println(elev == null ? "Nu exista un elev cu acest ID." : elev);
    }

    private void mutaElev() {
        int idElev = citesteInt("ID elev: ");
        Sala sursa = salaElevului(idElev);
        if (sursa == null) throw new IllegalArgumentException("Elevul nu este inscris intr-o sala.");
        Sala destinatie = cereSala("ID sala destinatie: ");
        if (sursa == destinatie) throw new IllegalArgumentException("Elevul se afla deja in sala aleasa.");
        Elev elev = gasesteElev(idElev);
        destinatie.adaugaElev(elev);
        sursa.eliminaElev(idElev);
        System.out.println("Elevul a fost mutat.");
    }

    private void stergeElev() {
        int id = citesteInt("ID elev de sters: ");
        Elev elev = gasesteElev(id);
        if (elev == null) { System.out.println("Elevul nu a fost gasit."); return; }
        for (Sala sala : sali) sala.eliminaElev(id);
        elevi.remove(elev);
        System.out.println("Elevul a fost sters.");
    }

    private void stergeSala() {
        Sala sala = cereSala("ID sala de sters: ");
        sali.remove(sala);
        System.out.println("Sala a fost stearsa. Elevii ei raman inregistrati si pot fi repartizati din nou.");
    }

    private void filtreazaPromovati(boolean promovati) {
        List<Elev> rezultat = elevi.stream().filter(e -> e.estePromovat() == promovati).collect(Collectors.toList());
        System.out.println(promovati ? "Elevi promovati:" : "Elevi nepromovati:");
        afiseazaElevi(rezultat);
    }

    private void filtreazaDupaMedie() {
        double minim = citesteDouble("Media minima: ");
        double maxim = citesteDouble("Media maxima: ");
        if (minim < 1 || maxim > 10 || minim > maxim) throw new IllegalArgumentException("Intervalul trebuie sa fie valid si cuprins intre 1 si 10.");
        afiseazaElevi(elevi.stream().filter(e -> e.getMedie() >= minim && e.getMedie() <= maxim).collect(Collectors.toList()));
    }

    private void filtreazaDupaVarsta() {
        int minim = citesteInt("Varsta minima: "), maxim = citesteInt("Varsta maxima: ");
        if (minim < 5 || maxim > 100 || minim > maxim) throw new IllegalArgumentException("Intervalul de varsta nu este valid.");
        afiseazaElevi(elevi.stream().filter(e -> e.getVarsta() >= minim && e.getVarsta() <= maxim).collect(Collectors.toList()));
    }

    private void cautaDupaNume() {
        String cautare = citesteText("Nume sau fragment de nume: ").toLowerCase();
        afiseazaElevi(elevi.stream().filter(e -> e.getNume().toLowerCase().contains(cautare)).collect(Collectors.toList()));
    }

    private void afiseazaStatistici() {
        if (elevi.isEmpty()) { System.out.println("Nu exista date pentru statistici."); return; }
        double mediaClasei = elevi.stream().mapToDouble(Elev::getMedie).average().orElse(0);
        long promovati = elevi.stream().filter(Elev::estePromovat).count();
        long inscrisi = sali.stream().mapToLong(s -> s.getElevi().size()).sum();
        System.out.printf("Elevi: %d | Promovati: %d | Nepromovati: %d | Media generala: %.2f%n",
                elevi.size(), promovati, elevi.size() - promovati, mediaClasei);
        System.out.printf("Sali: %d | Locuri: %d | Locuri ocupate: %d%n", sali.size(),
                sali.stream().mapToInt(Sala::getCapacitate).sum(), inscrisi);
    }

    private void sorteazaElevii() {
        System.out.println("1. Nume  2. Medie descrescator  3. Varsta");
        int criteriu = citesteInt("Criteriu: ");
        List<Elev> sortati = new ArrayList<>(elevi);
        switch (criteriu) {
            case 1: sortati.sort(Comparator.comparing(Elev::getNume, String.CASE_INSENSITIVE_ORDER)); break;
            case 2: sortati.sort(Comparator.comparingDouble(Elev::getMedie).reversed()); break;
            case 3: sortati.sort(Comparator.comparingInt(Elev::getVarsta)); break;
            default: throw new IllegalArgumentException("Criteriu de sortare inexistent.");
        }
        afiseazaElevi(sortati);
    }

    private Elev gasesteElev(int id) { return elevi.stream().filter(e -> e.getId() == id).findFirst().orElse(null); }
    private Sala gasesteSala(int id) { return sali.stream().filter(s -> s.getId() == id).findFirst().orElse(null); }
    private boolean esteInscris(int idElev) { return salaElevului(idElev) != null; }
    private Sala salaElevului(int idElev) { return sali.stream().filter(s -> s.contineElev(idElev)).findFirst().orElse(null); }

    private Elev cereElev(String mesaj) {
        Elev elev = gasesteElev(citesteInt(mesaj));
        if (elev == null) throw new IllegalArgumentException("Elevul nu a fost gasit.");
        return elev;
    }

    private Sala cereSala(String mesaj) {
        Sala sala = gasesteSala(citesteInt(mesaj));
        if (sala == null) throw new IllegalArgumentException("Sala nu a fost gasita.");
        return sala;
    }

    private String citesteText(String mesaj) {
        System.out.print(mesaj);
        return scanner.nextLine().trim();
    }

    private int citesteInt(String mesaj) {
        while (true) {
            System.out.print(mesaj);
            try { return Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException exceptie) { System.out.println("Introdu un numar intreg valid."); }
        }
    }

    private double citesteDouble(String mesaj) {
        while (true) {
            System.out.print(mesaj);
            try { return Double.parseDouble(scanner.nextLine().trim().replace(',', '.')); }
            catch (NumberFormatException exceptie) { System.out.println("Introdu un numar valid."); }
        }
    }
}
