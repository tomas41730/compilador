package IDE.Controller;

import Model.AnalizadorLexico;
import Model.SLR;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    @FXML
    private WebView webView;
    @FXML
    private DialogPane dialogPane;
    @FXML
    private Label status_info;

    private TableView tbvDetalles;

    private TableView tbvErrores;
    @FXML
    private VBox main_app_container;
    @FXML
    private VBox vboxAreaCode;
    @FXML
    private SplitPane splitPane;
    @FXML
    private TreeView file_tree;
    Image file = new Image(getClass().getResourceAsStream("/IDE/Controller/resources/fileIcon.png"));
    Image folder = new Image(getClass().getResourceAsStream("/IDE/Controller/resources/folderIcon.png"));
    TableColumn tcValor;
    TableColumn tcToken;
    TableColumn tcSimbolo;
    TableColumn tcFila;
    TableColumn tcColumna;
    TableColumn tcError;
    TableColumn tcTipo;
    TableColumn tcFilaE;
    TableColumn tcColumnaE;
    TableColumn tcDescripcion;



    String path = "SampleCode";
    String content = "";
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        webView.getEngine().load(getClass().getResource("Editor/Ace.html").toExternalForm());
        tbvErrores = new TableView();
        tbvDetalles = new TableView();
        file_tree.setRoot(getNodesForDirectory(new File(path)));
        file_tree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->  {

            content = ManejoArchivos.file2str(path+ "\\" +((TreeItem) file_tree.getSelectionModel().getSelectedItem()).getValue());
            content = content.replace("'", "\\'");
            content = content.replace(System.getProperty("line.separator"), "\\n");
            content = content.replace("\n", "\\n");
            content = content.replace("\r", "\\n");
            webView.getEngine().executeScript("editor.setValue('"+content+"')");
            showOutputSection();
            anasin();
            System.out.println(content);
        });

    }

    @FXML
    public void btnRun() {
        vboxAreaCode.getChildren().remove(vboxAreaCode.getChildren().size()-1);
        content = content.replace("'", "\\'");
        content = content.replace(System.getProperty("line.separator"), "\\n");
        content = content.replace("\n", "\\n");
        content = content.replace("\r", "\\n");
        webView.getEngine().executeScript("editor.setValue('"+content+"')");
        showOutputSection();
        anasin();
    }

    void showOutputSection() {
        main_app_container.setMinHeight(main_app_container.getHeight() + 200);
        TabPane tabPane = new TabPane();
        tabPane.setPrefWidth(200);
        tabPane.setPrefHeight(200);
        Tab lex = new Tab("Lexico");
        Tab sin = new Tab("Sintactico");
        inicializarTBVDetalles();
        inicializarTBVErrores();
        lex.setContent(tbvDetalles);
        sin.setContent(tbvErrores);
        tabPane.getTabs().add(lex);
        tabPane.getTabs().add(sin);
        vboxAreaCode.getChildren().add(tabPane);
        autoResizeColumn(tcValor, tbvDetalles);
        autoResizeColumn(tcDescripcion, tbvErrores);


    }
    @FXML
    public void openFile() throws IOException
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload File Path");
        File file = fileChooser.showOpenDialog(dialogPane.getScene().getWindow());
        path = file.getPath();
        crearTab();
        anasin();
        showOutputSection();
    }
    public void crearTab(){
        String codigo = ManejoArchivos.file2str(path);
        codigo = codigo.replace("'", "\\'");
        codigo = codigo.replace(System.getProperty("line.separator"), "\\n");
        codigo = codigo.replace("\n", "\\n");
        codigo = codigo.replace("\r", "\\n");
        webView.getEngine().executeScript("editor.setValue('"+codigo+"')");

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
                this.status_info.setText("Error Sintactico");
            } else {

                this.status_info.setText("Correcto!");

            }
            slr.getErroresSintacticos().forEach(error -> tbvErrores.getItems().add(error));

        } else {

            this.status_info.setText("Error Lexico");

        }

    }
    public void inicializarTBVDetalles()
    {
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


    }
    public void inicializarTBVErrores()
    {
        tbvErrores.setEditable(true);

        tcTipo = new TableColumn("Tipo");
        tcFilaE = new TableColumn("Fila");
        tcColumnaE = new TableColumn("Columna");
        tcDescripcion = new TableColumn("Descripcion");

        tbvErrores.getColumns().addAll(tcTipo, tcFilaE, tcColumnaE, tcDescripcion);

        tcTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        tcFilaE.setCellValueFactory(new PropertyValueFactory<>("fila"));
        tcColumnaE.setCellValueFactory(new PropertyValueFactory<>("columna"));
        tcDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    }
    public void autoResizeColumn(TableColumn column, TableView table){
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
    }
    @FXML
    public void openRecent()
    {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(System.getProperty("user.home")));
        File choice = dc.showDialog(null);
        path = choice.getPath();
        if(choice == null || ! choice.isDirectory()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Could not open directory");
            alert.setContentText("The file is invalid.");

            alert.showAndWait();
        }
        else {
            file_tree.setRoot(getNodesForDirectory(choice));
        }
    }
    public TreeItem<String> getNodesForDirectory(File directory) { //Returns a TreeItem representation of the specified directory
        TreeItem<String> root = new TreeItem<String>(directory.getName(), new ImageView(folder));
        for(File f : directory.listFiles()) {
            System.out.println("Loading " + f.getName());
            if(f.isDirectory()) { //Then we call the function recursively
                root.getChildren().add(getNodesForDirectory(f));
            } else {
                root.getChildren().add(new TreeItem<String>(f.getName(), new ImageView(file)));
            }
        }
        return root;
    }

}
