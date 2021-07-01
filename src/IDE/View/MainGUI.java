package IDE.View;

import Controller.ManejoArchivos;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.util.List;
//import netscape.javascript.JSObject;


public class MainGUI extends Application {

    @FXML
    private WebView webView;
    @FXML
    private TabPane tabPane;

    //private JSObject javascriptConnector;
    //private JavaConnector javaConnector = new JavaConnector();
    private String CUT_VALUE;
    private int CUT_STATE = 0;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception
    {

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Equipo Maravilla IDE");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();



    }
}
