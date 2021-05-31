package Model;

public class Path {
    public EstadoTransicion siguienteEstado;
    public String input;

    public Path(EstadoTransicion siguienteEstado, String input) {
        this.siguienteEstado = siguienteEstado;
        this.input = input;
    }

    @Override
    public String toString() {
        return "Path{" +
                "siguienteEstado=" + siguienteEstado.getId() +
                ", input='" + input + '\'' +
                '}';
    }
}
