package Model;

public class Lexema
{
    private String valor;
    private String token;
    private String simbolo;
    private int fila;
    private int columna;
    private boolean error;


    public Lexema(String valor, String token, String simbolo, int fila, int columna, boolean error) {
        this.valor = valor;
        this.token = token;
        this.simbolo = simbolo;
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

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
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

    public String detalles(){ //Servira de variable auxiliar cuando lea desde
        //el AnalizadorLexico
        return this.valor + "\t\t\t\t" +
                this.token + "\t\t\t\t"+
                this.simbolo + "\t\t\t"+
                this.fila + "\t\t\t"+
                this.columna + "\t\t\t"+
                this.error;
                
    }

    @Override
    public String toString() {
        return this.simbolo;
    }
}
