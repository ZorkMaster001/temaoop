import java.util.Objects;

/** Reprezinta un elev inscris in aplicatie. */
public class Elev {
    private int id;
    private String nume;
    private int varsta;
    private double medie;

    public Elev() {
        this(1, "Necunoscut", 1, 1.0);
    }

    public Elev(int id, String nume) {
        this(id, nume, 1, 1.0);
    }

    public Elev(int id, String nume, int varsta, double medie) {
        setId(id);
        setNume(nume);
        setVarsta(varsta);
        setMedie(medie);
    }

    public int getId() { return id; }
    public void setId(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID-ul elevului trebuie sa fie pozitiv.");
        this.id = id;
    }

    public String getNume() { return nume; }
    public void setNume(String nume) {
        if (nume == null || nume.trim().isEmpty()) throw new IllegalArgumentException("Numele nu poate fi gol.");
        this.nume = nume.trim();
    }

    public int getVarsta() { return varsta; }
    public void setVarsta(int varsta) {
        if (varsta < 5 || varsta > 100) throw new IllegalArgumentException("Varsta trebuie sa fie intre 5 si 100 de ani.");
        this.varsta = varsta;
    }

    public double getMedie() { return medie; }
    public void setMedie(double medie) {
        if (!Double.isFinite(medie) || medie < 1 || medie > 10)
            throw new IllegalArgumentException("Media trebuie sa fie intre 1 si 10.");
        this.medie = medie;
    }

    public boolean estePromovat() { return medie >= 5; }

    @Override
    public String toString() {
        return String.format("Elev{id=%d, nume='%s', varsta=%d, medie=%.2f, status=%s}",
                id, nume, varsta, medie, estePromovat() ? "promovat" : "nepromovat");
    }

    @Override
    public boolean equals(Object obiect) {
        if (this == obiect) return true;
        if (!(obiect instanceof Elev)) return false;
        return id == ((Elev) obiect).id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
