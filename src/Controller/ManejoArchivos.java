package Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
    public static void writeStringToFile(String path, String data) throws IOException {
        Files.write( Paths.get(path), data.getBytes());
    }
    public static String file2str(String path)
    {
        String linea = "";
        try
        {
            File archivo = new File(path);
            Scanner scanner = new Scanner(archivo);
            while (scanner.hasNextLine())
            {
                linea = linea + scanner.nextLine() + "\n";
            }
            scanner.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Error, archivo no encontrado.");
            e.printStackTrace();
        }
        return linea;
    }

    public static List<String> devolverIDs() {

        List<String> lineas = leerArchivo("src/Config/gramatica.txt");

        List<String> ids = new ArrayList<>();

        for (int i =0; i< lineas.size(); i++){

            if (i % 2 == 0 && !lineas.get(i).equals("")) {

                String linea = lineas.get(i);
                String definicion = lineas.get(i+1);
                definicion = definicion.replace(" ", "");
                definicion = definicion.replace("\t", "");


                String[] opcionesB = definicion.split("\\|");
                List<String> opciones = Arrays.asList(opcionesB);

                //System.out.println(opciones.toString());

                for (String opcion : opciones) {

                    //System.out.println("op " + opcion);

                    ids.add(linea);

                    //System.out.println(definicion);

                }

            }



        }

        return ids;

    }

    public static List<List<String>> devolverProducciones(){

        // [["A","C"],["c","A]]

        List<String> lineas = leerArchivo("src/Config/gramatica.txt");

        List<List<String>> producciones = new ArrayList<>();

        for (int i =0; i< lineas.size(); i++){

            if (i % 2 != 0 && !lineas.get(i).equals("")) {

                String linea = lineas.get(i);
                linea = linea.replace(" ", "");
                linea = linea.replace("\t", "");

                String[] opcionesB = linea.split("\\|");
                List<String> opciones = Arrays.asList(opcionesB);

                for (String opcion : opciones) {

                    String[] terminosB = opcion.split(",");
                    List<String> terminos = Arrays.asList(terminosB);
                    producciones.add(terminos);

                }

            }



        }

        return producciones;

    }

}
