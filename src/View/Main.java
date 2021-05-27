package View;

import Controller.ManejoArchivos;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args)
    {
        //List<String> lista = ManejoArchivos.leerArchivo("D:\\Tomas\\Documentos\\Compilación\\Proyecto\\ficheros\\ejemplo.txt");
        //lista.forEach(System.out::println);
        launch(args);
    }
}
