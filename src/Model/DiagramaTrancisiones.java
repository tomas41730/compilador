package Model;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiagramaTrancisiones
{
    private Dictionary expresionesRegulares;


    public DiagramaTrancisiones()
    {
        inicializarER();
    }
    private void inicializarER() //Inicializar Expresiones Regulares
    {
        System.out.println("Equipo Maravilla");
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
