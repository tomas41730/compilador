package Model;

public class Error
{
    private String tipo;
    private int fila;
    private int columna;
    private String descripcion;
    private String mensajeError;

    public Error(String tipo, int fila, int columna, String descripcion, String mensajeError)
    {
        this.tipo = tipo;
        this.fila = fila;
        this.columna = columna;
        this.descripcion = descripcion;
        this.mensajeError = mensajeError;
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

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
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
