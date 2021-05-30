package Model;

import java.util.*;

public class AnalizadorLexico
{
    private Dictionary tablaPalabrasReservadas;
    private DiagramaTrancisiones dt;
    private List<Lexema> listaLexemas;

    public AnalizadorLexico()
    {
        this.tablaPalabrasReservadas = inicializarPR();
        this.listaLexemas = null;
    }

    private Dictionary inicializarPR() // Inicializar palabras reservadas
    {
        return null;
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
    
    public void imprimirDetalles(){ //Este metodo intenta replicar el modelo de tabla
        //como el ejemplo en trello del COM-06
        System.out.println("Lexema      Token       Fila        Columna        Error");
        for (Lexema lexema : listaLexemas) {
            System.out.println(lexema.detalles();
        }
    }
}
