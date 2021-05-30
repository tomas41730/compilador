package Model;

import java.util.*;

public class AnalizadorLexico
{

    private DiagramaTrancisiones dt;
    private final List<Lexema> listaLexemas;

    public AnalizadorLexico()
    {

        this.listaLexemas = null;
    }

    public void AnalizarCodigo(List<String> lineasCodigo) //Para cada palabra en cada linea analizar con metodos
    {                                                     //del diagrama de transiciones e insertar en lista lexemas

    }
    public void imprimirLexemas()
    {
        for (Lexema lexema:listaLexemas)
        {
            System.out.println(lexema.toString());
        }
    }
}
