package Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class FormsOperations {
    public FXMLLoader OpenForm(String title, String url) {
        FXMLLoader fXMLLoader = null;
        try {
            fXMLLoader = new FXMLLoader(getClass().getResource(url));

            AnchorPane root = fXMLLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root,root.getPrefWidth(),root.getPrefHeight());
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return fXMLLoader;
    }
}
