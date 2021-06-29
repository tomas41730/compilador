package Model;

public class Error
{
    private String tipo;
    private int fila;
    private int columna;
    private String descripcion;

    public Error(String tipo, int fila, int columna, String descripcion)
    {
        this.tipo = tipo;
        this.fila = fila;
        this.columna = columna;
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Error{" +
                "tipo='" + tipo + '\'' +
                ", fila=" + fila +
                ", columna=" + columna +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
