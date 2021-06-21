package View;

import Controller.FormsOperations;
import Controller.ManejoArchivos;
import Model.AnalizadorLexico;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class mainGUIController implements Initializable
{
    @FXML
    private DialogPane dialogPane;
    @FXML
    private TabPane tabPane;
    @FXML
    private TableView tbvDetalles;
    @FXML
    private TableView tbvErrores;
    String path = "";
    public String codigo = "";

    private FXMLLoader loader;

    public mainGUIController()
    {
        tabPane = new TabPane();
        tbvDetalles = new TableView();

    }
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        inicializarTBVDetalles();
        inicializarTBVErrores();
    }
    @FXML
    public void openFileWindow()
    {
        AtomicInteger i = new AtomicInteger();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload File Path");
        File file = fileChooser.showOpenDialog(dialogPane.getScene().getWindow());
        path = file.getPath();
        System.out.println(path);
        ManejoArchivos.leerArchivo(path).forEach(linea -> codigo =  codigo + linea + "\n");
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
        anaLex.getListaErrores().forEach(error ->  tbvErrores.getItems().add(error));
    }

    @FXML
    public void btnEvaluar()
    {
        analex();
    }
    @FXML
    public void btnArbol()
    {
        FormsOperations formsOperations = new FormsOperations();
        FXMLLoader fXMLLoader = formsOperations.OpenForm("Arbol de derivacion", "/View/arbol.fxml");
        ControllerArbolGUI arbolDerivacion = fXMLLoader.getController();
    }
    public void inicializarTBVDetalles()
    {
        TableColumn tcValor;
        TableColumn tcToken;
        TableColumn tcSimbolo;
        TableColumn tcFila;
        TableColumn tcColumna;
        TableColumn tcError;
        tbvDetalles.setEditable(true);

        tcValor = new TableColumn("Valor");
        tcToken = new TableColumn("Token");
        tcSimbolo = new TableColumn("Simbolo");
        tcFila = new TableColumn("Fila");
        tcColumna = new TableColumn("Columna");
        tcError = new TableColumn("Error");

        tbvDetalles.getColumns().addAll(tcValor, tcToken, tcSimbolo, tcFila, tcColumna, tcError);

        tcValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        tcToken.setCellValueFactory(new PropertyValueFactory<>("token"));
        tcSimbolo.setCellValueFactory(new PropertyValueFactory<>("simbolo"));
        tcFila.setCellValueFactory(new PropertyValueFactory<>("fila"));
        tcColumna.setCellValueFactory(new PropertyValueFactory<>("columna"));
        tcError.setCellValueFactory(new PropertyValueFactory<>("error"));

        tcValor.setMinWidth(80.0);
        tcFila.setMaxWidth(45.0);
        tcColumna.setMaxWidth(45.0);
        tcError.setMaxWidth(45.0);
    }
    public void inicializarTBVErrores()
    {
        TableColumn tcTipo;
        TableColumn tcFila;
        TableColumn tcColumna;
        TableColumn tcDescripcion;
        TableColumn tcMensajeError;
        tbvErrores.setEditable(true);

        tcTipo = new TableColumn("Tipo");
        tcFila = new TableColumn("Fila");
        tcColumna = new TableColumn("Columna");
        tcDescripcion = new TableColumn("Descripcion");
        tcMensajeError = new TableColumn("MensajeError");

        tbvErrores.getColumns().addAll(tcTipo, tcFila, tcColumna, tcDescripcion, tcMensajeError);

        tcTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        tcFila.setCellValueFactory(new PropertyValueFactory<>("fila"));
        tcColumna.setCellValueFactory(new PropertyValueFactory<>("columna"));
        tcDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        tcMensajeError.setCellValueFactory(new PropertyValueFactory<>("mensajeError"));

        tcDescripcion.setMinWidth(100);
    }
}
