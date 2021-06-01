package View;

import Controller.ManejoArchivos;
import Model.AnalizadorLexico;
import Model.DiagramaTrancisiones;
import Model.Lexema;
import dnl.utils.text.table.TextTable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("ANALIZADOR LEXICO");
        primaryStage.setScene(new Scene(root, 1264, 757));
        primaryStage.show();
    }
    public static void main(String[] args)
    {
        //List<String> lineas = ManejoArchivos.leerArchivo("D:\\Tomas\\Documentos\\Compilación\\Proyecto\\ficheros\\ejemplo.txt");
        //AnalizadorLexico anaLex = new AnalizadorLexico();
        //anaLex.AnalizarCodigo(lineas);
        //anaLex.imprimirDetalles();
        //anaLex.imprimirLexemas();
        launch(args);
    }
}
