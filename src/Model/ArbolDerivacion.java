package Model;

import java.util.Dictionary;
import java.util.List;

public class ArbolDerivacion {

    private TerminoGLC termino;
    private Dictionary reglas;
    private List<ArbolDerivacion> hijos;

    public ArbolDerivacion(TerminoGLC termino, Dictionary reglas, List<ArbolDerivacion> hijos) {
        this.termino = termino;
        this.reglas = reglas;
        this.hijos = hijos;
    }

    public TerminoGLC getTermino() {
        return termino;
    }

    public void setTermino(TerminoGLC termino) {
        this.termino = termino;
    }

    public Dictionary getReglas() {
        return reglas;
    }

    public void setReglas(Dictionary reglas) {
        this.reglas = reglas;
    }

    public List<ArbolDerivacion> getHijos() {
        return hijos;
    }

    public void setHijos(List<ArbolDerivacion> hijos) {
        this.hijos = hijos;
    }
}
