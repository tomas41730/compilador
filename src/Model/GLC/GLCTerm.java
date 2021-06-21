package Model.GLC;

public class GLCTerm {

    private boolean terminal;
    private String id;
    private String reg;

    public GLCTerm(boolean terminal, String id, String reg) {
        this.terminal = terminal;
        this.id = id;
        this.reg = reg;
    }

    public GLCTerm(boolean terminal, String id) {
        this.terminal = terminal;
        this.id = id;
        this.reg = id;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }
}
