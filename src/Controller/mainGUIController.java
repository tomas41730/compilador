package Controller;

import Model.AnalizadorLexico.AnalizadorLexico;
import Model.SLR.SLR;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
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
    TextArea txtAreaCodigo;
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
        crearTabFichero(file);
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
            slr.evaluarCadena(anaLex.getListaLexemas());

            if (!slr.isAcepted()) {
                this.errorMessage.setText("Error Sintactico");
            } else {

                this.errorMessage.setText("Correcto!");

            }
            slr.getErroresSintacticos().forEach(error -> tbvErrores.getItems().add(error));

        } else {

            this.errorMessage.setText("Error Lexico");

        }
        autoResizeColumns(tbvErrores);
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
        txtAreaCodigo = new TextArea();
        txtAreaCodigo.setPrefSize(916.0, 681.0);
        txtAreaCodigo.setText(codigo);
        Tab fileTab = new Tab(file.getName());
        System.out.println(fileTab.getText());
        fileTab.setContent(txtAreaCodigo);
        tabPane.getTabs().add(fileTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        codigo = "";
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
        //tcDescripcion.setMinWidth(1000);
    }
    @FXML
    public void btnSave() throws IOException {
        ManejoArchivos.writeStringToFile(path,txtAreaCodigo.getText());
        System.out.println(tabPane.getSelectionModel().getSelectedItem().getText());
    }
    public static void autoResizeColumns(TableView<?> table)
    {
        //Set the right policy
        table.setColumnResizePolicy( TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getColumns().stream().forEach( (column) ->
        {
            //Minimal width = columnheader
            Text t = new Text( column.getText() );
            double max = t.getLayoutBounds().getWidth();
            for ( int i = 0; i < table.getItems().size(); i++ )
            {
                //cell must not be empty
                if ( column.getCellData( i ) != null )
                {
                    t = new Text( column.getCellData( i ).toString() );
                    double calcwidth = t.getLayoutBounds().getWidth();
                    //remember new max-width
                    if ( calcwidth > max )
                    {
                        max = calcwidth;
                    }
                }
            }
            //set the new max-widht with some extra space
            column.setPrefWidth( max + 10.0d );
        } );
    }
}
