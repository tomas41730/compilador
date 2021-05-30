package Model;
import java.util.*;

public class EstadoTransicion {

    private int id;
    private String valorRetorno;
    private List<Path> transiciones;
    private boolean esFinal = false;

    public EstadoTransicion(int id, String valorRetorno, boolean esFinal) {
        this.id = id;
        this.valorRetorno = valorRetorno;
        this.transiciones = new ArrayList<Path>();
        this.esFinal = esFinal;
    }

    public int getId() {
        return id;
    }

    public String getValorRetorno() {
        return valorRetorno;
    }

    public List<Path> getTransiciones() {
        return transiciones;
    }

    public boolean isEsFinal() {
        return esFinal;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setValorRetorno(String valorRetorno) {
        this.valorRetorno = valorRetorno;
    }

    public void addTransicion(Path transicion) {
        this.transiciones.add(transicion);
    }

    @Override
    public String toString() {
        return "EstadoTransicion{" +
                "id=" + id +
                ", valorRetorno='" + valorRetorno + '\'' +
                ", transiciones=" + transiciones +
                ", esFinal=" + esFinal +
                '}';
    }

    public void setEsFinal(boolean esFinal) {
        this.esFinal = esFinal;
    }
}
