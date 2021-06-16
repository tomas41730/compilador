package Model;

public class ReglaSLR {

    private GLCRule regla;
    private int numeroRegla;
    private String identificador;
    private int index;

    public ReglaSLR (GLCRule regla, int numeroRegla, int index) {

        this.regla = regla;
        this.numeroRegla = numeroRegla;
        this.identificador = regla.getId();
        this.index = index;

    }

    public GLCRule getRegla() {
        return regla;
    }

    public void setRegla(GLCRule regla) {
        this.regla = regla;
    }

    public int getNumeroRegla() {
        return numeroRegla;
    }

    public void setNumeroRegla(int numeroRegla) {
        this.numeroRegla = numeroRegla;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "["  + numeroRegla +
                "." + identificador + ", index=" + index +
                ']';
    }
}
