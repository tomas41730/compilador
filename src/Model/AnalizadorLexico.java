package Model;

import java.util.*;
import java.util.stream.*;

public class AnalizadorLexico
{
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[40m";
    private DiagramaTrancisiones dt;
    private final List<Lexema> listaLexemas;

    public List<Lexema> getListaLexemas() {
        return listaLexemas;
    }

    public AnalizadorLexico()
    {
        this.dt = new DiagramaTrancisiones();
        this.listaLexemas = new LinkedList<Lexema>();
    }

    public void AnalizarCodigo(List<String> lineasCodigo) //Para cada palabra en cada linea analizar con metodos
    {                                                     //del diagrama de transiciones e insertar en lista lexemas
        int fila = -1;
        int columna = 0;
        for(String linea: lineasCodigo){
            fila++;
            columna = 0;
            String[] palabras  = linea.split("[\\s\\t]");

            for (String palabra: palabras) {
                if (palabra != "") {
                    List<Lexema> aux = this.dt.analizarLexema(palabra, fila, columna);
                    columna += aux.size();
                    this.listaLexemas.addAll(aux);
                    //System.out.println("aux: " + aux);
                    //System.out.println("lexemas: " + this.listaLexemas);
                }
            }
        }
    }
    public void imprimirLexemas()
    {
        System.out.println(ANSI_BLACK+"┌───────────────────────────────┬─────────────────┬──────┬──────┬───────┐"+ANSI_RESET);
        System.out.println(ANSI_BLACK+"│             VALOR             │      TOKEN      │ FILA │ COL. │ ERROR │"+ANSI_RESET);
        System.out.println(ANSI_BLACK+"├───────────────────────────────┼─────────────────┼──────┼──────┼───────┤"+ANSI_RESET);
        for (Lexema lexema:listaLexemas)
        {
            System.out.format("│%30s │%15s  │%4s  │%4s  │%6s │%n",lexema.getValor(), lexema.getToken(),
                    String.valueOf(lexema.getFila()), String.valueOf(lexema.getColumna()), lexema.isError());
            System.out.println("├───────────────────────────────┼─────────────────┼──────┼──────┼───────┤");
        }
        System.out.println("└───────────────────────────────┴─────────────────┴──────┴──────┴───────┘");
    }
    
    public void imprimirDetalles(){ //Este metodo intenta replicar el modelo de tabla
        //como el ejemplo en trello del COM-06
        System.out.println("Lexema\t\t\tToken\t\t\tFila\t\t\tColumna\t\t\tError");
        for (Lexema lexema : listaLexemas) {
            System.out.println(lexema.detalles());
        }
    }
}
