package IDE.View;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
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




//
//
//
//
//        webView.setContextMenuEnabled(false);
//        createContextMenu(webView);
//
//
//
//        // set up the listener
//        webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
//            if (Worker.State.SUCCEEDED == newValue) {
//
//
//                // Capture Ctrl+V event and process it
//                // set an interface object named 'javaConnector' in the web engine's page
//                JSObject window = (JSObject) webView.getEngine().executeScript("window");
//                window.setMember("javaConnector", javaConnector);
//
//
//                // get the Javascript connector object.
//                javascriptConnector = (JSObject) webView.getEngine().executeScript("getJsConnector()");
//                //javaConnector.getValueFromJavascript("abcd");
//            }
//        });
//
//
//
//        webView.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
//            if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.X) {
//                System.out.println("CUTTING:");
//                String selectedText = (String) webView.getEngine().executeScript("editor.getSelectedText()");
//                System.out.println(selectedText);
//                CUT_VALUE = selectedText;
//                CUT_STATE = 1;
//            }
//        });
//        /*
//        webView.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
//            if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.C){
//                System.out.println("COPyING:");
//            }
//        });
//        */
//
//        webView.setOnKeyPressed(e ->
//        {
//
//
//            //System.out.println("Key Pressed");
//            if (e.isControlDown() && e.getCode() == KeyCode.C) {
//                System.out.println("COPyING:");
//
//
//            }
//        });
//
//
//        // retrieve copy event via javascript:alert
//        webView.getEngine().setOnAlert((WebEvent<String> we) -> {
//            System.out.println("Found Data from Context Menu: " + we.getData());
//
//            if (we.getData() != null && we.getData().startsWith(":copy ")) {
//                System.out.println("ALERT GOT COPY TEXT");
//
//
//            }
//        });
//    }
//
//    private void createContextMenu(WebView webView) {
//        ContextMenu contextMenu = new ContextMenu();
//
//        MenuItem reload = new MenuItem("Reload");
//        reload.setOnAction(e ->
//                webView.getEngine().reload()
//        );
//
//        MenuItem copyToClipboard = new MenuItem("copyToClipboard");
//        copyToClipboard.setOnAction(e -> {
//                    System.out.println("copyToClipboard...");
//
//                    String s = (String) webView.getEngine().executeScript("editor.getSelectedText()");
//                    System.out.println(s);
//
//                    Clipboard clipboard = Clipboard.getSystemClipboard();
//                    ClipboardContent content = new ClipboardContent();
//                    content.putString(s);
//                    clipboard.setContent(content);
//                }
//        );
//
//        MenuItem pasteFromClipboard = new MenuItem("pasteFromClipboard");
//        pasteFromClipboard.setOnAction(e -> {
//                    System.out.println("pasteFromClipboard...");
//
//
//                    Clipboard clipboard = Clipboard.getSystemClipboard();
//                    Object content = clipboard.getContent(DataFormat.PLAIN_TEXT);
//
//
//                    if (CUT_STATE == 1) {
//                        javascriptConnector = (JSObject) webView.getEngine().executeScript("window");
//                        javascriptConnector.setMember("MEMBER", CUT_VALUE);
//
//                        webView.getEngine().executeScript("pasteFunction()");
//                        System.out.println((String) webView.getEngine().executeScript("MEMBER"));
//
//
//                        CUT_STATE = 0;
//                    } else {
//                        javascriptConnector = (JSObject) webView.getEngine().executeScript("window");
//                        javascriptConnector.setMember("MEMBER", content);
//
//                        if (content != null) {
//                            webView.getEngine().executeScript("pasteFunction()");
//                            webView.getEngine().executeScript("MEMBER");
//                        }
//
//                        //System.out.println((String) webView.getEngine().executeScript("MEMBER"));
//                        //System.out.println(content);
//                    }
//
//
//                }
//        );
//
//        MenuItem cutToClipboard = new MenuItem("cutToClipboard");
//        cutToClipboard.setOnAction(e -> {
//                    System.out.println("cutToClipboard...");
//
////            System.out.println((String) webView.getEngine().executeScript("editor.getSession().doc.getTextRange(editor.selection.getRange());"));
//                    String selectedText = (String) webView.getEngine().executeScript("editor.getSelectedText()");
//                    webView.getEngine().executeScript("cutFunc();");
//
//
//                    Clipboard clipboard = Clipboard.getSystemClipboard();
//                    ClipboardContent content = new ClipboardContent();
//                    content.putString(selectedText);
//                    clipboard.setContent(content);
//                }
//        );
//
//
//
//        Menu parentMenu = new Menu("Font Size");
//        MenuItem fontSize10 = new MenuItem("10pt");
//        parentMenu.getItems().add(fontSize10);
//        MenuItem fontSize12 = new MenuItem("12pt");
//        parentMenu.getItems().add(fontSize12);
//        MenuItem fontSize14 = new MenuItem("14pt");
//        parentMenu.getItems().add(fontSize14);
//
//
//        fontSize10.setOnAction(event -> {
//            javaConnector.changeFontSize("10pt");
//        });
//        fontSize12.setOnAction(event -> {
//            javaConnector.changeFontSize("12pt");
//        });
//        fontSize14.setOnAction(event -> {
//            javaConnector.changeFontSize("14pt");
//        });
//
//
//
//
//
//
//        MenuItem runCode = new MenuItem("Run Code");
//        runCode.setOnAction(event -> {
//            javaConnector.valueReturnTest("runCode");
//        });
//
//
//
//        contextMenu.getItems().addAll(reload, cutToClipboard, copyToClipboard, pasteFromClipboard, parentMenu, runCode);
//
//        webView.setOnMousePressed(e -> {
//            if (e.getButton() == MouseButton.SECONDARY) {
//                contextMenu.show(webView, e.getScreenX(), e.getScreenY());
//            } else {
//                contextMenu.hide();
//            }
//        });
//    }
//
//
//    public class JavaConnector {
//        /**
//         * called when the JS side wants a String to be converted.
//         *
//         * @param value the String to convert
//         */
//        public void getValueFromJavascript(String value) {
//            //System.out.println(value);
//            CUT_VALUE = value;
//
//            System.out.println("TRYING TO SHOW STATUS");
//            System.out.println(javascriptConnector.call("showResult", value.toLowerCase()));
//        }
//
//
//        public  void changeFontSize(String value){
//            System.out.println(javascriptConnector.call("changeFontSize", value));
//        }
//
//        public void valueReturnTest(String value){
//            System.out.println(javascriptConnector.call("valueReturnTest", value));
//
//
//            List<Window> open = Stage.getWindows().filtered(window -> window.isShowing());
//            System.out.println(open);
//            //Label label = (Label) primaryStage.getScene().lookup("#status_info");
//            //label.setText(value);
//        }
//
    }
}
