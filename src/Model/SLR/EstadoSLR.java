package Model.SLR;

import Model.GLC.GLCRule;

import java.util.ArrayList;
import java.util.List;

public class EstadoSLR {

    private int id;
    private List<TransicionSLR> transiciones;
    private List<ReglaSLR> reglas;

    public EstadoSLR (int id, GLCRule reglaIncial, List<GLCRule> listaReglas) {

        this.id = id;
        this.reglas = new ArrayList<>();
        this.reglas.add(new ReglaSLR(reglaIncial, -1, 0));
        this.transiciones = new ArrayList<>();
        this.expandir(listaReglas);

    }

    public EstadoSLR (int id, List<GLCRule> listaReglas, List<ReglaSLR> reglasIniciales) {
        this.id = id;
        this.reglas = new ArrayList<>(reglasIniciales);
        this.transiciones = new ArrayList<>();
        this.expandir(listaReglas);
    }

    private boolean containsRule(ReglaSLR reglaB) {

        for (ReglaSLR regla: this.reglas) {
            if (regla.getNumeroRegla() == reglaB.getNumeroRegla() && regla.getIndex() == reglaB.getIndex()){

                return true;

            }
        }

        return false;

    }

    public boolean equals(EstadoSLR otroEstado){

        boolean bool = true;
        int size = 0;
        for (ReglaSLR regla: otroEstado.getReglas()){
            size ++;
            if (!(this.containsRule(regla))){
                bool = false;
                break;
            }

        }

        if (size != this.reglas.size()){
            bool = false;
        }

        return bool;
    }

    public  String printReglas() {

        String s = "{";

        for (ReglaSLR regla: this.reglas) {

            s = s + regla.toString() + " ";

        }

        s = s + "}";

        return s;

    }

    public void agregarTransicion (int destino, String input) {

        this.transiciones.add(new TransicionSLR(destino, input));

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<TransicionSLR> getTransiciones() {
        return transiciones;
    }

    public void setTransiciones(List<TransicionSLR> transiciones) {
        this.transiciones = transiciones;
    }

    public List<ReglaSLR> getReglas() {
        return reglas;
    }

    public void setReglas(List<ReglaSLR> reglas) {
        this.reglas = reglas;
    }

    private void expandir (List<GLCRule> listaReglas) {

        //System.out.println("expandiendo el estado : " + this.id);
        List<ReglaSLR> pending = new ArrayList<>(this.reglas);

        List<String> done = new ArrayList<>();

        while (pending.size() > 0) {

            //System.out.println(pending.size() + " reglas pendientes.");
            ReglaSLR analizando = pending.get(0);
            pending.remove(0);

            if (analizando.getIndex() < analizando.getRegla().getOpciones().get(0).getTerminos().size()) {

                if (!(analizando.getRegla().getOpciones().get(0).getTerminos().get(analizando.getIndex()).isTerminal())) {

                    if (!(done.contains(analizando.getRegla().getOpciones().get(0).getTerminos().get(analizando.getIndex()).getId()))) {
                        //System.out.println("Buscando reglas a agregar relacionadas con: " + analizando.getRegla().getOpciones().get(0).getTerminos().get(analizando.getIndex()).getId());
                        // si el termino no es terminal

                        done.add(analizando.getRegla().getOpciones().get(0).getTerminos().get(analizando.getIndex()).getId());

                        int index = -1;

                        for (GLCRule reglaposible : listaReglas) {

                            index++;

                            //System.out.println("Comparando con " + reglaposible.getId());

                            if (reglaposible.getId().equals(analizando.getRegla().getOpciones().get(0).getTerminos().get(analizando.getIndex()).getId())) {
                                //System.out.println("Agregando Regla");
                                ReglaSLR nuevaregla = new ReglaSLR(reglaposible, index, 0);
                                if (!(this.reglas.contains(nuevaregla))) {
                                    this.reglas.add(nuevaregla);
                                    pending.add(new ReglaSLR(reglaposible, index, 0));
                                }
                            }

                        }
                    }
                }
            }
        }

    }

}
