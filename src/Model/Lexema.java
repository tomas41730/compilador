package Model;

public class Lexema
{
    private String valor;
    private String token;
    private int fila;
    private int columna;
    private boolean error;

    public Lexema()
    {

    }
    public Lexema(String valor, String token, int fila, int columna, boolean error)
    {
        this.valor = valor;
        this.token = token;
        this.fila = fila;
        this.columna = columna;
        this.error = error;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Lexema{" +
                "valor='" + valor + '\'' +
                ", token='" + token + '\'' +
                ", fila=" + fila +
                ", columna=" + columna +
                ", error=" + error +
                '}';
    }

    public String detalles(){ //Servira de variable auxiliar cuando lea desde
        //el AnalizadorLexico
        return this.valor + "\t\t\t\t" +
                this.token + "\t\t\t\t"+
                this.fila + "\t\t\t"+
                this.columna + "\t\t\t"+
                this.error;
                
    }
}
