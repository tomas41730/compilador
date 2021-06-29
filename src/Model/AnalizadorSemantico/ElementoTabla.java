package Model.AnalizadorSemantico;

import Model.AnalizadorLexico.Lexema;

public class ElementoTabla {

    private Lexema lexema;

    private String identificador;

    private String tipo;

    private boolean metodo;

    private boolean inicializado;


    public ElementoTabla(Lexema lexema, String tipo, boolean metodo) {
        this.lexema = lexema;
        this.identificador = lexema.getValor();
        this.tipo = tipo;
        this.metodo = metodo;
        this.inicializado = false;
    }

    public boolean isMetodo() {
        return metodo;
    }

    public void setMetodo(boolean metodo) {
        this.metodo = metodo;
    }

    public boolean isInicializado() {
        return inicializado;
    }

    public void setInicializado(boolean inicializado) {
        this.inicializado = inicializado;
    }

    public Lexema getLexema() {
        return lexema;
    }

    public void setLexema(Lexema lexema) {
        this.lexema = lexema;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
