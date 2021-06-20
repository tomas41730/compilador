package Model;

public class TransicionSLR {

    private int destino;
    private String input;

    public TransicionSLR (int destino, String input) {

        this.destino = destino;
        this.input = input;


    }

    public int getDestino() {
        return destino;
    }

    public void setDestino(int destino) {
        this.destino = destino;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
