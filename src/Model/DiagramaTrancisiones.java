package Model;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiagramaTrancisiones
{
    private Dictionary expresionesRegulares;
    private Dictionary tablaPalabrasReservadas;
    private Dictionary tablaDelimitadores;

    public DiagramaTrancisiones()
    {
        this.inicializarPR();
        this.inicializarDelimitadores();
        this.inicializarER();
    }
    private void inicializarER() //Inicializar Expresiones Regulares
    {
        this.expresionesRegulares = new Hashtable();
        this.expresionesRegulares.put("letra", "[a-zA-Z]");
        this.expresionesRegulares.put("identificadorChar", "[_a-zA-Z0-9]");
        this.expresionesRegulares.put("numero", "[0-9]");
        this.expresionesRegulares.put("Op. Aritmetico", "[\\+|-|\\*|\\/|%]");
        this.expresionesRegulares.put("igual", "=");
        this.expresionesRegulares.put("and", "&");
        this.expresionesRegulares.put("or", "\\|");
        this.expresionesRegulares.put("dif", "!");
        this.expresionesRegulares.put("mayorOMenor", "[><]");
        this.expresionesRegulares.put("delimitadores", "[;|,|\\(|\\)|\\[|\\]|\\{|\\}]");
        this.expresionesRegulares.put("puntoDecimal", "\\.");
        this.expresionesRegulares.put("comillas", '"');
        this.expresionesRegulares.put("cadena", "[^\"]");

        //System.out.println(evaluarExpresion(" ", (String) this.expresionesRegulares.get("cadena")));
    }

    private void inicializarPR() // Inicializar palabras reservadas
    {

        this.tablaPalabrasReservadas = new Hashtable();
        this.tablaPalabrasReservadas.put("int", "int");
        this.tablaPalabrasReservadas.put("real", "real");
        this.tablaPalabrasReservadas.put("bool", "bool");
        this.tablaPalabrasReservadas.put("if", "if");
        this.tablaPalabrasReservadas.put("else", "else");
        this.tablaPalabrasReservadas.put("for", "for");
        this.tablaPalabrasReservadas.put("while", "while");
        this.tablaPalabrasReservadas.put("string", "string");
        this.tablaPalabrasReservadas.put("void", "void");
        this.tablaPalabrasReservadas.put("return", "return");
        this.tablaPalabrasReservadas.put("true", "true");
        this.tablaPalabrasReservadas.put("false", "false");
    }

    private void inicializarDelimitadores()
    {
        this.tablaDelimitadores = new Hashtable();
        this.tablaDelimitadores.put(";",";");
        this.tablaDelimitadores.put(",",",");
        this.tablaDelimitadores.put("(","(");
        this.tablaDelimitadores.put(")",")");
        this.tablaDelimitadores.put("[","[");
        this.tablaDelimitadores.put("]","]");
        this.tablaDelimitadores.put("{","{");
    }

    public void analizarLexema(String lexema) //cambiar void por tupla de ser posible, caso contrario cambiarlo a List<String, String>
    {

    }
    public static boolean evaluarExpresion(String lexema, String expresionRegular) {
        boolean valido = false;
        Pattern pat = Pattern.compile(expresionRegular);
        Matcher mat = pat.matcher(lexema);
        if (mat.matches()) {
            System.out.println("Validado");
            valido = true;
        }
        else {
            System.out.println("No validado");
            valido = false;
        }
        return valido;
    }
}
