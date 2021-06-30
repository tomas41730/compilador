package IDE.Models;


import javafx.fxml.FXML;
import javafx.scene.web.WebView;

public class CodeArea
{
    private WebView codeArea;
    public CodeArea(WebView codeArea)
    {
        this.codeArea = codeArea;
        //this.codeArea.getEngine().load(getClass().getResource("Controller/Editor/Ace.html").toExternalForm());
    }
    @FXML
    public String getText()
    {
        String code = (String) this.codeArea.getEngine().executeScript("editor.getValue()");
        return code;
    }
    public void setText(String content){
        content = content.replace("'", "\\'");
        content = content.replace(System.getProperty("line.separator"), "\\n");
        content = content.replace("\n", "\\n");
        content = content.replace("\r", "\\n");
        this.codeArea.getEngine().executeScript("editor.setValue('"+content+"')");
    }

    public WebView getCodeArea() {
        return codeArea;
    }

    public void setCodeArea(WebView codeArea) {
        this.codeArea = codeArea;
    }
}
