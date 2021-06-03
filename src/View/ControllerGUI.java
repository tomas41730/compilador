package View;

import Controller.ManejoArchivos;
import Model.AnalizadorLexico;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class ControllerGUI implements Initializable
{
    @FXML
    private DialogPane dialogPane;
    @FXML
    private TabPane tabPane;
    @FXML
    private TableView tbvDetalles;
    String path = "";
    public String codigo = "";
    TableColumn tcValor;
    TableColumn tcToken;
    TableColumn tcFila;
    TableColumn tcColumna;
    TableColumn tcError;

    public ControllerGUI()
    {
        tabPane = new TabPane();
        tbvDetalles = new TableView();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        inicializarTBVDetalles();
    }
    @FXML
    public void openFileWindow()
    {
        AtomicInteger i = new AtomicInteger();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload File Path");
        File file = fileChooser.showOpenDialog(dialogPane.getScene().getWindow());
        path = file.getPath();
        ManejoArchivos.leerArchivo(path).forEach(linea -> codigo =  codigo + String.valueOf(i.getAndIncrement())+"\t"+linea + "\n");
        TextArea txtAreaCodigo = new TextArea();
        txtAreaCodigo.setPrefSize(916.0, 681.0);
        txtAreaCodigo.setText(codigo);
        Tab fileTab = new Tab(file.getName());
        fileTab.setContent(txtAreaCodigo);
        tabPane.getTabs().add(fileTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        codigo = "";

        this.analex();

    }

    private void analex () {

        // ex eval
        tbvDetalles.getItems().clear();
        List<String> lineas = ManejoArchivos.leerArchivo(path);
        AnalizadorLexico anaLex = new AnalizadorLexico();
        anaLex.AnalizarCodigo(lineas);
        anaLex.imprimirLexemas();
        anaLex.getListaLexemas().forEach(lexema -> tbvDetalles.getItems().add(lexema));

    }

    @FXML
    public void btnEvaluar()
    {
        analex();
    }
    public void inicializarTBVDetalles()
    {
        tbvDetalles.setEditable(true);

        tcValor = new TableColumn("Valor");
        tcToken = new TableColumn("Token");
        tcFila = new TableColumn("Fila");
        tcColumna = new TableColumn("Columna");
        tcError = new TableColumn("Error");

        tbvDetalles.getColumns().addAll(tcValor, tcToken, tcFila, tcColumna, tcError);

        tcValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        tcToken.setCellValueFactory(new PropertyValueFactory<>("token"));
        tcFila.setCellValueFactory(new PropertyValueFactory<>("fila"));
        tcColumna.setCellValueFactory(new PropertyValueFactory<>("columna"));
        tcError.setCellValueFactory(new PropertyValueFactory<>("error"));

        tcValor.setMinWidth(180.0);
        tcFila.setMaxWidth(45.0);
        tcColumna.setMaxWidth(45.0);
        tcError.setMaxWidth(45.0);
    }
}
