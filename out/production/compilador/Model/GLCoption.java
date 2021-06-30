package Model;

import java.util.List;

public class GLCoption {

    private List<GLCTerm> terminos;
    private GLCTerm primerTermino;
    private GLCRule regla;
    private String reg;
    private int id;

    public GLCoption(List<GLCTerm> terminos, GLCTerm primerTermino, GLCRule regla, String reg, int id) {
        this.terminos = terminos;
        this.primerTermino = primerTermino;
        this.regla = regla;
        this.reg = reg;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public List<GLCTerm> getTerminos() {
        return terminos;
    }

    public void setTerminos(List<GLCTerm> terminos) {
        this.terminos = terminos;
    }

    public GLCTerm getPrimerTermino() {
        return primerTermino;
    }

    public void setPrimerTermino(GLCTerm primerTermino) {
        this.primerTermino = primerTermino;
    }

    public GLCRule getRegla() {
        return regla;
    }

    public void setRegla(GLCRule regla) {
        this.regla = regla;
    }

    public boolean hasTerm(GLCTerm term){

        return this.terminos.contains(term);

    }

    public String printOpcion(){

        String aux = "";

        for (GLCTerm term: this.terminos){
            aux = aux + term.getId();
        }

        return  aux;

    }
}
