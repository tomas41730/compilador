package Model;

import java.util.ArrayList;
import java.util.List;

public class GLCRule {

    private List<GLCoption> opciones;
    private String id;
    private List<GLCTerm> primeros;
    private List<GLCTerm> siguientes;
    private boolean inicial;

    public boolean isInicial() {
        return inicial;
    }

    public void setInicial(boolean inicial) {
        this.inicial = inicial;
    }

    public GLCRule(String id) {
        this.id = id;
        this.opciones = new ArrayList<>();
        this.primeros = new ArrayList<>();
        this.siguientes = new ArrayList<>();
        this.inicial = false;
    }

    public List<GLCTerm> getSiguientes() {
        return siguientes;
    }

    public void setSiguientes(List<GLCTerm> siguientes) {
        this.siguientes = siguientes;
    }

    public List<GLCTerm> getPrimeros() {
        return primeros;
    }

    public void setPrimeros(List<GLCTerm> primeros) {
        this.primeros = primeros;
    }

    public List<GLCoption> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<GLCoption> opciones) {
        this.opciones = opciones;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addOption(GLCoption newOp) {

        this.opciones.add(newOp);

    }

    public String printRegla(){

        String s  = this.id + " -> ";
        for (GLCoption opcion : this.opciones) {

            s = s + " | " + opcion.printOpcion();

        }

        return s;
    }

    public void addOptionWithTerms(List<GLCTerm> terminos) {

        GLCoption newOp = new GLCoption(terminos, (GLCTerm) terminos.get(0), (GLCRule) this, ".*", this.opciones.size() + 1);
        this.opciones.add(newOp);

    }

    public String printPrimeros() {

        String aux = "";

        for(GLCTerm termino : this.primeros)
        {
            aux =  aux + " " + termino.getId();
        }
        return aux;

    }

    public String printSegundos() {

        String aux = "";

        for(GLCTerm termino : this.siguientes)
        {
            aux =  aux + " " + termino.getId();
        }
        return aux;

    }

}
