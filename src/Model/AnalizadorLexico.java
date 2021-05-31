package Model;

import java.util.*;
import java.util.stream.*;

public class AnalizadorLexico
{

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
        System.out.println("┌───────────────────────────────┬─────────────────┬──────┬──────┬────────┐");
        System.out.format("│%30s │%15s  │%4s  │%4s  │ %4s  │%n","VALOR            ", "TOKEN   ", "FILA", "COL", "ERROR");
        System.out.println("├───────────────────────────────┼─────────────────┼──────┼──────┼────────┤");
        for (Lexema lexema:listaLexemas)
        {
            System.out.format("│%30s │%15s  │%4s  │%4s  │ %5s  │%n",lexema.getValor(), lexema.getToken(),
                    String.valueOf(lexema.getFila()), String.valueOf(lexema.getColumna()), lexema.isError());
        }
        System.out.println("└───────────────────────────────┴─────────────────┴──────┴──────┴────────┘");
    }
    
    public void imprimirDetalles(){ //Este metodo intenta replicar el modelo de tabla
        //como el ejemplo en trello del COM-06
        System.out.println("Lexema\t\t\tToken\t\t\tFila\t\t\tColumna\t\t\tError");
        for (Lexema lexema : listaLexemas) {
            System.out.println(lexema.detalles());
        }
    }
}
