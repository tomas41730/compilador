package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class ArbolGUIController implements Initializable {

    @FXML
    private AnchorPane arbolView;

    @FXML
    private Pane canvas;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        crearNodo(100, 100, "tg");
    }

    private void crearNodo(float x, float y, String element)
    {
        Circle circle = new Circle();
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(25);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);

        Text text = new Text(element);
        text.setX(x-17);
        text.setY(y+2);
        text.setFill(Color.BLACK);

        canvas.getChildren().add(circle);
        canvas.getChildren().add(text);

    }
}
