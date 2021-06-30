package Controller;

import Controller.ArbolGUIController;
import Controller.FormsOperations;
import Controller.ManejoArchivos;
import Model.AnalizadorLexico;
import Model.CodeArea;
import Model.SLR;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    TextArea txtAreaCodigo;
    private CodeArea codeArea;
    String path = "SampleCode/code2.txt";
    @FXML
    private Label errorMessage;

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
        System.out.println();
        crearTabFichero(new File(path));
    }
    @FXML
    public void openFileWindow()
    {
        AtomicInteger i = new AtomicInteger();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload File Path");
        File file = fileChooser.showOpenDialog(dialogPane.getScene().getWindow());
        path = file.getPath();
        //crearTabFichero(file);
        this.analex();

    }


    private AnalizadorLexico analex () {
        // ex eval
        tbvDetalles.getItems().clear();
        tbvErrores.getItems().clear();
        List<String> lineas = ManejoArchivos.leerArchivo(path);
        AnalizadorLexico anaLex = new AnalizadorLexico();
        anaLex.AnalizarCodigo(lineas);
        anaLex.imprimirLexemas();
        anaLex.getListaLexemas().forEach(lexema -> tbvDetalles.getItems().add(lexema));
        anaLex.getListaErrores().forEach(error ->  tbvErrores.getItems().add(error));

        return anaLex;
    }

    private void anasin () {

        AnalizadorLexico anaLex = this.analex();

        if ( anaLex.getListaErrores().size() == 0 ) {

            tbvErrores.getItems().clear();
            SLR slr = new SLR();
            slr.analizarCadena(anaLex.getListaLexemas());

            if (!slr.isAcepted()) {
                this.errorMessage.setText("Error Sintactico");
            } else {

                this.errorMessage.setText("Correcto!");

            }
            slr.getErroresSintacticos().forEach(error -> tbvErrores.getItems().add(error));

        } else {

            this.errorMessage.setText("Error Lexico");

        }

    }

    @FXML
    public void btnEvaluar()
    {
        anasin();
    }
    @FXML
    public void btnArbol()
    {
        FormsOperations formsOperations = new FormsOperations();
        FXMLLoader fXMLLoader = formsOperations.OpenForm("Arbol de derivacion", "/View/arbol.fxml");
        ArbolGUIController arbolDerivacion = fXMLLoader.getController();
    }
    public void crearTabFichero(File file)
    {
        ManejoArchivos.leerArchivo(file.getPath()).forEach(linea -> codigo =  codigo + linea + "\n");
        WebView webView = new WebView();
        webView.getEngine().load(getClass().getResource("Editor/Ace.html").toExternalForm());

        System.out.println("antes");
        codeArea = new CodeArea(webView);
        System.out.println("despues");
        codeArea.getCodeArea().setPrefSize(916.0, 681.0);
        //codeArea.setText(codigo);
//        txtAreaCodigo = new TextArea();
//        txtAreaCodigo.setPrefSize(916.0, 681.0);
//        txtAreaCodigo.setText(codigo);
        Tab fileTab = new Tab(file.getName());
        System.out.println(fileTab.getText());
        //fileTab.setContent(txtAreaCodigo);
        fileTab.setContent(codeArea.getCodeArea());
        tabPane.getTabs().add(fileTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        codigo = "";
        String code = (String) codeArea.getCodeArea().getEngine().executeScript("editor.getValue()");

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

        tbvErrores.getColumns().addAll(tcTipo, tcFila, tcColumna, tcDescripcion);

        tcTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        tcFila.setCellValueFactory(new PropertyValueFactory<>("fila"));
        tcColumna.setCellValueFactory(new PropertyValueFactory<>("columna"));
        tcDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        tcDescripcion.setMinWidth(1000);
    }
    @FXML
    public void btnSave() throws IOException {
        ManejoArchivos.writeStringToFile(path,txtAreaCodigo.getText());
        System.out.println(tabPane.getSelectionModel().getSelectedItem().getText());
    }
}
