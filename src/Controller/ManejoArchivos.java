package Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManejoArchivos
{
    public static List<String> leerArchivo(String path)
    {
        String linea = "";
        List<String> listaLineas = new ArrayList<String>();
        try
        {
            File archivo = new File(path);
            Scanner scanner = new Scanner(archivo);
            while (scanner.hasNextLine())
            {
                linea = scanner.nextLine();
                listaLineas.add(linea);
            }
            scanner.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Error, archivo no encontrado.");
            e.printStackTrace();
        }
        return listaLineas;
    }
}
