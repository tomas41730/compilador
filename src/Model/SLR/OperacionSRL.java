package Model.SLR;

public class OperacionSRL {

    private String tipo;
    private int estadoFuturo;

    public OperacionSRL(String tipo, int estadoFuturo) {
        this.tipo = tipo;
        this.estadoFuturo = estadoFuturo;
    }

    public OperacionSRL() {
        this.tipo = "accept";
        this.estadoFuturo = 0;

    }

    @Override
    public String toString() {
        return "[ " + this.tipo + " to " + this.estadoFuturo + " ]";
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getEstadoFuturo() {
        return estadoFuturo;
    }

    public void setEstadoFuturo(int estadoFuturo) {
        this.estadoFuturo = estadoFuturo;
    }
}
