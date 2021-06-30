package View;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainGUI.fxml"));
        primaryStage.setTitle("ANALIZADOR LEXICO");
        primaryStage.setScene(new Scene(root, 1264, 757));
        primaryStage.show();

    }
    public static void main(String[] args)
    {
        //SLR slr = new SLR();
        launch(args);
    }
}
