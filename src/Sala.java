import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Sala gestioneaza elevii inscrisi in ea si respecta capacitatea declarata. */
public class Sala {
    private int id;
    private String denumire;
    private int capacitate;
    private final List<Elev> elevi;

    public Sala() {
        this(1, "Sala noua", 1, Collections.emptyList());
    }

    public Sala(int id, String denumire) {
        this(id, denumire, 30, Collections.emptyList());
    }

    public Sala(int id, String denumire, int capacitate, List<Elev> elevi) {
        this.elevi = new ArrayList<>();
        setId(id);
        setDenumire(denumire);
        setCapacitate(capacitate);
        setElevi(elevi);
    }

    public int getId() { return id; }
    public void setId(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID-ul salii trebuie sa fie pozitiv.");
        this.id = id;
    }

    public String getDenumire() { return denumire; }
    public void setDenumire(String denumire) {
        if (denumire == null || denumire.trim().isEmpty()) throw new IllegalArgumentException("Denumirea salii nu poate fi goala.");
        this.denumire = denumire.trim();
    }

    public int getCapacitate() { return capacitate; }
    public void setCapacitate(int capacitate) {
        if (capacitate <= 0) throw new IllegalArgumentException("Capacitatea trebuie sa fie pozitiva.");
        if (capacitate < elevi.size()) throw new IllegalArgumentException("Capacitatea nu poate fi mai mica decat numarul elevilor inscrisi.");
        this.capacitate = capacitate;
    }

    public List<Elev> getElevi() { return Collections.unmodifiableList(elevi); }

    public void setElevi(List<Elev> elevi) {
        if (elevi == null) throw new IllegalArgumentException("Lista de elevi nu poate fi null.");
        if (elevi.size() > capacitate) throw new IllegalArgumentException("Numarul elevilor depaseste capacitatea salii.");
        List<Elev> copie = new ArrayList<>();
        for (Elev elev : elevi) {
            if (elev == null) throw new IllegalArgumentException("Lista nu poate contine elevi null.");
            if (copie.contains(elev)) throw new IllegalArgumentException("Un elev nu poate aparea de doua ori in aceeasi sala.");
            copie.add(elev);
        }
        this.elevi.clear();
        this.elevi.addAll(copie);
    }

    public void adaugaElev(Elev elev) {
        if (elev == null) throw new IllegalArgumentException("Elevul nu poate fi null.");
        if (elevi.contains(elev)) throw new IllegalArgumentException("Elevul este deja inscris in aceasta sala.");
        if (elevi.size() >= capacitate) throw new IllegalStateException("Sala a ajuns la capacitatea maxima.");
        elevi.add(elev);
    }

    public boolean eliminaElev(int idElev) {
        return elevi.removeIf(elev -> elev.getId() == idElev);
    }

    public boolean contineElev(int idElev) {
        return elevi.stream().anyMatch(elev -> elev.getId() == idElev);
    }

    @Override
    public String toString() {
        return "Sala{id=" + id + ", denumire='" + denumire + "', capacitate=" + capacitate
                + ", elevi=" + elevi.size() + "/" + capacitate + ", lista=" + elevi + '}';
    }
}
