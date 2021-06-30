package Model.AnalizadorSemantico;

import Model.AnalizadorLexico.Lexema;
import Model.SLR.ArbolSintactico;

public class ElementoTabla {

    private Lexema lexema;

    private String identificador;

    private String tipo;

    private boolean metodo;

    private boolean inicializado;

    private ArbolSintactico referenciaArbol;


    public ElementoTabla(Lexema lexema, String tipo, boolean metodo, ArbolSintactico arbol) {
        this.lexema = lexema;
        this.identificador = lexema.getValor();
        this.tipo = tipo;
        this.metodo = metodo;
        this.inicializado = false;
        this.referenciaArbol = arbol;
    }

    public ElementoTabla(Lexema lexema, String tipo, boolean metodo) {
        this.lexema = lexema;
        this.identificador = lexema.getValor();
        this.tipo = tipo;
        this.metodo = metodo;
        this.inicializado = false;
        this.referenciaArbol = null;
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

    public ArbolSintactico getReferenciaArbol() {
        return referenciaArbol;
    }

    public void setReferenciaArbol(ArbolSintactico referenciaArbol) {
        this.referenciaArbol = referenciaArbol;
    }
}
