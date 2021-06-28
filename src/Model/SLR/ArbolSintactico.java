package Model.SLR;

import Model.AnalizadorLexico.Lexema;

import java.util.ArrayList;
import java.util.List;

public class ArbolSintactico {

    private Lexema lexema;
    private List<ArbolSintactico> hijos;
    private int id;
    private String data;

    public ArbolSintactico(Lexema lexema, int id) {

        this.lexema = lexema;
        this.id = id;
        this.hijos = new ArrayList<>();
        this.data = lexema.getSimbolo();

    }

    @Override
    public String toString() {

        String hijos = "[";

        for (ArbolSintactico hijo: this.hijos) {

            hijos = hijos + " " + hijo.toString() + ",";

        }

        //hijos = hijos.substring(0, hijos.length()-1);
        hijos = hijos + " ]";

        return "Nodo{" +
                "id=" + id +
                ", simbolo='" + data + '\'' +
                ", hijos: " + hijos +
                " }";
    }

    public void agregarHijo(ArbolSintactico hijo) {

        this.hijos.add(hijo);

    }

    public void agregarHijos(List<ArbolSintactico> hijoNuevos) {

        for (ArbolSintactico i : hijoNuevos) {
            this.hijos.add(i);
        }

    }

    public Lexema getLexema() {
        return lexema;
    }

    public void setLexema(Lexema lexema) {
        this.lexema = lexema;
    }

    public List<ArbolSintactico> getHijos() {
        return hijos;
    }

    public void setHijos(List<ArbolSintactico> hijos) {
        this.hijos = hijos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
