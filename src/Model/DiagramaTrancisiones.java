package Model;

import Config.DictManager;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Config.DictManager;

public class DiagramaTrancisiones
{
    private Dictionary expresionesRegulares;
    private Dictionary tablaPalabrasReservadas;
    private Dictionary estados;
    private EstadoTransicion estadoActual;

    public DiagramaTrancisiones()
    {
        this.inicializarPR();
        this.inicializarER();
        this.inicializarDiagrama();

    }

    private void inicializarDiagrama()
    {

        //Definimos los estados del diagrama de transiciones

        this.estados = new Hashtable();
        this.estados.put(0, (new EstadoTransicion(0, "", false)));
        //id states
        this.estados.put(1, (new EstadoTransicion(1, "identificador", true)));
        this.estados.put(2, (new EstadoTransicion(2, "identificador", true)));
        this.estados.put(3, (new EstadoTransicion(3, "identificador", true)));
        this.estados.put(4, (new EstadoTransicion(4, "identificador", true)));
        this.estados.put(5, (new EstadoTransicion(5, "identificador", true)));
        this.estados.put(6, (new EstadoTransicion(6, "identificador", true)));
        this.estados.put(7, (new EstadoTransicion(7, "identificador", true)));
        this.estados.put(8, (new EstadoTransicion(8, "identificador", true)));
        this.estados.put(9, (new EstadoTransicion(9, "identificador", true)));
        this.estados.put(10, (new EstadoTransicion(10, "identificador", true)));

        // Op. Arit
        this.estados.put(11, (new EstadoTransicion(11, "Op. Aritmetico", true)));

        // Asignacion
        this.estados.put(12, (new EstadoTransicion(12, "igualdad", true)));

        // Op. Logico
        this.estados.put(13, (new EstadoTransicion(13, "Op. Logico", true)));
        this.estados.put(14, (new EstadoTransicion(14, "Op. Logico", true)));
        this.estados.put(15, (new EstadoTransicion(15, "", false)));
        this.estados.put(16, (new EstadoTransicion(16, "", false)));
        this.estados.put(17, (new EstadoTransicion(17, "", false)));

        //delimitadores
        this.estados.put(18, (new EstadoTransicion(18, "delimitador", true)));

        //numeros
        this.estados.put(19, (new EstadoTransicion(19, "Numero entero", true)));
        this.estados.put(20, (new EstadoTransicion(20, "", false)));
        this.estados.put(21, (new EstadoTransicion(21, "Numero real", true)));

        //string
        this.estados.put(22, (new EstadoTransicion(22, "", false)));
        this.estados.put(23, (new EstadoTransicion(23, "cadena", true)));

        //error
        this.estados.put(50, (new EstadoTransicion(50, "error", false)));


        //Agregamos las transiciones

        //q0
        ((EstadoTransicion)this.estados.get(0)).addTransicion(new Path(((EstadoTransicion) this.estados.get(1)),(String)this.expresionesRegulares.get("letra")));
        ((EstadoTransicion)this.estados.get(0)).addTransicion(new Path(((EstadoTransicion) this.estados.get(11)),(String)this.expresionesRegulares.get("Op. Aritmetico")));
        ((EstadoTransicion)this.estados.get(0)).addTransicion(new Path(((EstadoTransicion) this.estados.get(12)),(String)this.expresionesRegulares.get("igual")));
        ((EstadoTransicion)this.estados.get(0)).addTransicion(new Path(((EstadoTransicion) this.estados.get(13)),(String)this.expresionesRegulares.get("mayorOMenor")));
        ((EstadoTransicion)this.estados.get(0)).addTransicion(new Path(((EstadoTransicion) this.estados.get(15)),(String)this.expresionesRegulares.get("dif")));
        ((EstadoTransicion)this.estados.get(0)).addTransicion(new Path(((EstadoTransicion) this.estados.get(16)),(String)this.expresionesRegulares.get("and")));
        ((EstadoTransicion)this.estados.get(0)).addTransicion(new Path(((EstadoTransicion) this.estados.get(17)),(String)this.expresionesRegulares.get("or")));
        ((EstadoTransicion)this.estados.get(0)).addTransicion(new Path(((EstadoTransicion) this.estados.get(18)),(String)this.expresionesRegulares.get("delimitadores")));
        ((EstadoTransicion)this.estados.get(0)).addTransicion(new Path(((EstadoTransicion) this.estados.get(19)),(String)this.expresionesRegulares.get("numero")));
        ((EstadoTransicion)this.estados.get(0)).addTransicion(new Path(((EstadoTransicion) this.estados.get(22)),(String)this.expresionesRegulares.get("comillas")));

        //q1 a q10
        ((EstadoTransicion)this.estados.get(1)).addTransicion(new Path(((EstadoTransicion) this.estados.get(2)),(String)this.expresionesRegulares.get("identificadorChar")));
        ((EstadoTransicion)this.estados.get(2)).addTransicion(new Path(((EstadoTransicion) this.estados.get(3)),(String)this.expresionesRegulares.get("identificadorChar")));
        ((EstadoTransicion)this.estados.get(3)).addTransicion(new Path(((EstadoTransicion) this.estados.get(4)),(String)this.expresionesRegulares.get("identificadorChar")));
        ((EstadoTransicion)this.estados.get(4)).addTransicion(new Path(((EstadoTransicion) this.estados.get(5)),(String)this.expresionesRegulares.get("identificadorChar")));
        ((EstadoTransicion)this.estados.get(5)).addTransicion(new Path(((EstadoTransicion) this.estados.get(6)),(String)this.expresionesRegulares.get("identificadorChar")));
        ((EstadoTransicion)this.estados.get(6)).addTransicion(new Path(((EstadoTransicion) this.estados.get(7)),(String)this.expresionesRegulares.get("identificadorChar")));
        ((EstadoTransicion)this.estados.get(7)).addTransicion(new Path(((EstadoTransicion) this.estados.get(8)),(String)this.expresionesRegulares.get("identificadorChar")));
        ((EstadoTransicion)this.estados.get(8)).addTransicion(new Path(((EstadoTransicion) this.estados.get(9)),(String)this.expresionesRegulares.get("identificadorChar")));
        ((EstadoTransicion)this.estados.get(9)).addTransicion(new Path(((EstadoTransicion) this.estados.get(10)),(String)this.expresionesRegulares.get("identificadorChar")));

        // q11 none
        // q12, q13, q15, q16, q17 a q14 (q14 no paths)
        ((EstadoTransicion)this.estados.get(12)).addTransicion(new Path(((EstadoTransicion) this.estados.get(14)),(String)this.expresionesRegulares.get("igual")));
        ((EstadoTransicion)this.estados.get(13)).addTransicion(new Path(((EstadoTransicion) this.estados.get(14)),(String)this.expresionesRegulares.get("igual")));
        ((EstadoTransicion)this.estados.get(15)).addTransicion(new Path(((EstadoTransicion) this.estados.get(14)),(String)this.expresionesRegulares.get("igual")));
        ((EstadoTransicion)this.estados.get(16)).addTransicion(new Path(((EstadoTransicion) this.estados.get(14)),(String)this.expresionesRegulares.get("and")));
        ((EstadoTransicion)this.estados.get(17)).addTransicion(new Path(((EstadoTransicion) this.estados.get(14)),(String)this.expresionesRegulares.get("or")));

        // q18 none

        // q19, q20, q21

        ((EstadoTransicion)this.estados.get(19)).addTransicion(new Path(((EstadoTransicion) this.estados.get(19)),(String)this.expresionesRegulares.get("numero")));
        ((EstadoTransicion)this.estados.get(19)).addTransicion(new Path(((EstadoTransicion) this.estados.get(20)),(String)this.expresionesRegulares.get("puntoDecimal")));

        ((EstadoTransicion)this.estados.get(20)).addTransicion(new Path(((EstadoTransicion) this.estados.get(21)),(String)this.expresionesRegulares.get("numero")));
        ((EstadoTransicion)this.estados.get(21)).addTransicion(new Path(((EstadoTransicion) this.estados.get(21)),(String)this.expresionesRegulares.get("numero")));

        // q22, q23
        ((EstadoTransicion)this.estados.get(22)).addTransicion(new Path(((EstadoTransicion) this.estados.get(22)),(String)this.expresionesRegulares.get("cadena")));
        ((EstadoTransicion)this.estados.get(22)).addTransicion(new Path(((EstadoTransicion) this.estados.get(23)),(String)this.expresionesRegulares.get("comillas")));

        // q50
        ((EstadoTransicion)this.estados.get(50)).addTransicion(new Path(((EstadoTransicion) this.estados.get(50)),(String)this.expresionesRegulares.get("error")));

        // identificadores al error ej. ass.123
        ((EstadoTransicion)this.estados.get(1)).addTransicion(new Path(((EstadoTransicion) this.estados.get(50)),(String)this.expresionesRegulares.get("error")));
        ((EstadoTransicion)this.estados.get(2)).addTransicion(new Path(((EstadoTransicion) this.estados.get(50)),(String)this.expresionesRegulares.get("error")));
        ((EstadoTransicion)this.estados.get(3)).addTransicion(new Path(((EstadoTransicion) this.estados.get(50)),(String)this.expresionesRegulares.get("error")));
        ((EstadoTransicion)this.estados.get(4)).addTransicion(new Path(((EstadoTransicion) this.estados.get(50)),(String)this.expresionesRegulares.get("error")));
        ((EstadoTransicion)this.estados.get(5)).addTransicion(new Path(((EstadoTransicion) this.estados.get(50)),(String)this.expresionesRegulares.get("error")));
        ((EstadoTransicion)this.estados.get(6)).addTransicion(new Path(((EstadoTransicion) this.estados.get(50)),(String)this.expresionesRegulares.get("error")));
        ((EstadoTransicion)this.estados.get(7)).addTransicion(new Path(((EstadoTransicion) this.estados.get(50)),(String)this.expresionesRegulares.get("error")));
        ((EstadoTransicion)this.estados.get(8)).addTransicion(new Path(((EstadoTransicion) this.estados.get(50)),(String)this.expresionesRegulares.get("error")));
        ((EstadoTransicion)this.estados.get(9)).addTransicion(new Path(((EstadoTransicion) this.estados.get(50)),(String)this.expresionesRegulares.get("error")));
        ((EstadoTransicion)this.estados.get(10)).addTransicion(new Path(((EstadoTransicion) this.estados.get(50)),(String)this.expresionesRegulares.get("error")));

        //numeros al error ej. 12asa
        ((EstadoTransicion)this.estados.get(19)).addTransicion(new Path(((EstadoTransicion) this.estados.get(50)),(String)this.expresionesRegulares.get("error")));
        ((EstadoTransicion)this.estados.get(20)).addTransicion(new Path(((EstadoTransicion) this.estados.get(50)),(String)this.expresionesRegulares.get("error")));
        ((EstadoTransicion)this.estados.get(21)).addTransicion(new Path(((EstadoTransicion) this.estados.get(50)),(String)this.expresionesRegulares.get("error")));

        this.estadoActual = (EstadoTransicion) this.estados.get(0);
        //System.out.println(this.estados.get(22));
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
        this.expresionesRegulares.put("comillas", "\"");
        this.expresionesRegulares.put("cadena", "[^\"]");
        this.expresionesRegulares.put("error", "[^\\+\\-\\*\\/\\%=&\\|!><;,\\(\\)\\[\\]\\{\\}\"]");


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

    public List<Lexema> analizarLexema(String lexema, int fila, int columna_ini, List<Error> errores) //cambiar void por tupla de ser posible, caso contrario cambiarlo a List<String, String>
    {

        this.estadoActual = (EstadoTransicion) this.estados.get(0);
        List<Lexema> lexemas = new ArrayList<Lexema>();
        boolean otro = true;
        String lex = "";
        char ch = ' ';
        int columna = columna_ini;

        // para cada caracter
        for (int i = 0; i < lexema.length(); i++)
        {
            ch = lexema.charAt(i);
            // otro representa si no hay match y el siguiente caracter es un separador, representa el fin de un lexema
            otro = true;
            //verificamos si hay match o no, si lo hay, avanzamos en estado, anotmaos el caracter y cambiamos otro a false
            for(Path p: this.estadoActual.getTransiciones()){
                if (evaluarExpresion(Character.toString(ch), p.input))
                {
                    otro = false;
                    lex += ch;
                    //System.out.println("match  " + ch + " with " + p.input);
                    this.estadoActual = p.siguienteEstado;
                    break;
                }
            }
            // caso en que el ultimo caracter de la caden haga match, cambiamos otro a true para poder guardarlo como lexema
            if (!otro && i == lexema.length()-1)
            {
                otro = true;
            }
            else if (otro) // si el caracter no hizo match
            {
                // si el caracter es el ultimo y no hay nada almacenado previo ej en abs' es para cuando se analice ', luego de guardar abs como lexema
                if (lex == "" && i == lexema.length()-1)
                {
                    lex += ch; // agregamos ' a lex para guardarlo como error
                }
                else
                {
                    // caso contrario tenemos lex=abs y ch=', queremos guardar abs y analizar ' desde un inicio por ende i--
                    i--;
                }
            }

            if (otro)
            {
                String returnVal = this.estadoActual.getValorRetorno();
                if (estadoActual.isEsFinal())
                {
                    // si es un estado fianl, el lexema es valido y es guardado como tal

                    if (returnVal == "identificador" && this.tablaPalabrasReservadas.get(lex) != null)
                    {
                        returnVal = lex;
                    }
                    else if (returnVal == "delimitador")
                    {
                        returnVal = lex;
                    }

                    //System.out.println("lexema: " + lex);
                    lexemas.add(new Lexema(lex, returnVal, (String) DictManager.tokenToSimbol().get(returnVal), fila, columna, false));
                }
                else
                {
                    returnVal = "error";
                    // si no estamos en un estado final, es un error
                    //System.out.println("error: " + lex);
                    lexemas.add(new Lexema(lex, returnVal, (String) DictManager.tokenToSimbol().get(returnVal), fila, columna, true));
                    errores.add(new Error("Lexico", fila, columna, lex));
                }
                columna++;
                this.estadoActual = (EstadoTransicion) this.estados.get(0);
                lex = "";
            }
        }
        return lexemas;
    }

    public static boolean evaluarExpresion(String lexema, String expresionRegular)
    {
        boolean valido = false;
        Pattern pat = Pattern.compile(expresionRegular);
        Matcher mat = pat.matcher(lexema);
        if (mat.matches())
        {
            valido = true;
        }
        else
        {
            valido = false;
        }
        return valido;
    }
}
