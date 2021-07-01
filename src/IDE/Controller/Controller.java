package IDE.Controller;

import Model.AnalizadorLexico.AnalizadorLexico;
import Model.AnalizadorSemantico.AnalizadorSemantico;
import Model.SLR.SLR;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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

import Controller.ManejoArchivos;
public class Controller implements Initializable
{
    @FXML
    private WebView webView;
    @FXML
    private DialogPane dialogPane;
    @FXML
    private Label status_info;
    @FXML
    private Label lbRowColumn;

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
    @FXML
    private Tab mainTab;
    @FXML
    private Button btnProject;
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



    String directoryPath = "SampleCode";
    String filePath = "";
    String content = "";
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        webView.getEngine().load(getClass().getResource("Editor/Ace.html").toExternalForm());
        tbvErrores = new TableView();
        tbvDetalles = new TableView();
        inicializarTBVDetalles();
        inicializarTBVErrores();
        file_tree.setRoot(getNodesForDirectory(new File(directoryPath)));
        file_tree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->  {
            filePath = directoryPath + "\\" +((TreeItem) file_tree.getSelectionModel().getSelectedItem()).getValue();
            content = ManejoArchivos.file2str(directoryPath + "\\" +((TreeItem) file_tree.getSelectionModel().getSelectedItem()).getValue());
            setAreaCodeText(content);
            //showOutputSection();
            //anasin();
            File choice =  new File(filePath);
            mainTab.setText(choice.getName());
            System.out.println(content);
        });
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                int row = (int) webView.getEngine().executeScript("editor.selection.getCursor().row")+1;
                int column = (int) webView.getEngine().executeScript("editor.selection.getCursor().column")+1;
                String position = row + ":" + column;
                System.out.println("Cursor Postion -> " + position );
                lbRowColumn.setText(position);
            }
        };
        webView.addEventFilter(MouseEvent.MOUSE_CLICKED,eventHandler);

    }


    @FXML
    public void btnRun() throws IOException {
        String codigo = webView.getEngine().executeScript("editor.getValue()").toString();
        ManejoArchivos.writeStringToFile(filePath, codigo);
        System.out.println(filePath);
        if(vboxAreaCode.getChildren().size() == 0)
        {
            showOutputSection();
            anaSem();

        }
        else
        {
            vboxAreaCode.getChildren().remove(0);
            //System.out.println(vboxAreaCode.getChildren().get(0).toString());
            showOutputSection();
            anaSem();
        }
        autoResizeColumn(tcValor, tbvDetalles);
        autoResizeColumn(tcDescripcion, tbvErrores);
    }

    void showOutputSection() {
        //main_app_container.setMinHeight(main_app_container.getHeight() + 200);
        TabPane tabPane = new TabPane();
        tabPane.setPrefWidth(200);
        tabPane.setPrefHeight(200);
        Tab lex = new Tab("Tabla de Lexemas");
        Tab sin = new Tab("Tabla de Errores");
        lex.setContent(tbvDetalles);
        sin.setContent(tbvErrores);
        tabPane.getTabs().add(lex);
        tabPane.getTabs().add(sin);
        vboxAreaCode.getChildren().add(tabPane);
    }
    @FXML
    public void openFile() throws IOException
    {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Upload File Path");
//        File file = fileChooser.showOpenDialog(dialogPane.getScene().getWindow());
//        directoryPath = file.getPath();
//        content = ManejoArchivos.file2str(directoryPath);
//        setAreaCodeText(content);
//        anaSem();
//        showOutputSection();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload File Path");
        File choice = fileChooser.showOpenDialog(dialogPane.getScene().getWindow());
        filePath = choice.getPath();
        btnProject.setText(choice.getName());
        if(choice == null || ! choice.isFile()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Could not open file");
            alert.setContentText("The file is invalid.");

            alert.showAndWait();
        }
        else {
            //file_tree.setRoot(getNodesForDirectory(choice));
            directoryPath = choice.getParent();
            file_tree.setRoot(new TreeItem(choice.getName()));
            mainTab.setText(choice.getName());
            setAreaCodeText(ManejoArchivos.file2str(filePath));
            System.out.println(filePath);
        }
    }
    public void setAreaCodeText(String codigo){
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
        List<String> lineas = ManejoArchivos.leerArchivo(filePath);
        AnalizadorLexico anaLex = new AnalizadorLexico();
        anaLex.AnalizarCodigo(lineas);
        anaLex.imprimirLexemas();
        anaLex.getListaLexemas().forEach(lexema -> tbvDetalles.getItems().add(lexema));
        anaLex.getListaErrores().forEach(error ->  tbvErrores.getItems().add(error));

        return anaLex;
    }

    private SLR anasin (AnalizadorLexico anaLex) {

        SLR slr = null;

        if ( anaLex.getListaErrores().size() == 0 ) {

            System.out.println("Sin errores lexicos");

            tbvErrores.getItems().clear();
            slr = new SLR();
            slr.evaluarCadena(anaLex.getListaLexemas());

            if (!slr.isAcepted()) {
                this.status_info.setText("Error Sintactico en " + filePath);
            } else {

                this.status_info.setText("Correcto!");

            }
            slr.getErroresSintacticos().forEach(error -> tbvErrores.getItems().add(error));

        } else {

            this.status_info.setText("Error Lexico en " + filePath);

        }
        //autoResizeColumn(tcDescripcion);

        return slr;

    }

    private void anaSem() {

        AnalizadorLexico anaLex = this.analex();
        SLR slr = this.anasin(anaLex);

        if (slr != null && slr.getErroresSintacticos().size() == 0) {

            AnalizadorSemantico anasem = new AnalizadorSemantico(slr.getArbol());

            anasem.getErroresSemanticos().forEach(error -> tbvErrores.getItems().add(error));
            //autoResizeColumns(tbvErrores);
            if(anasem.getErroresSemanticos().size() > 0)
            {
                //status_info.setTextFill(new Color());
                status_info.setText("Errores semanticos en " + filePath);
            }
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
        directoryPath = choice.getPath();
        btnProject.setText(choice.getName());
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
    @FXML
    public void getCursorPosition(){

    }
}
