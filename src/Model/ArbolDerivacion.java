package Model;

import java.util.Dictionary;
import java.util.List;

public class ArbolDerivacion {

    private String id;
    private boolean terminal;
    private Dictionary reglas;
    private List<ArbolDerivacion> hijos;

    public ArbolDerivacion(String id, boolean terminal, Dictionary reglas) {
        this.id = id;
        this.terminal = terminal;
        this.reglas = reglas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    public Dictionary getReglas() {
        return reglas;
    }

    public void setReglas(Dictionary reglas) {
        this.reglas = reglas;
    }
}
