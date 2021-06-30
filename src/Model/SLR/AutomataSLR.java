package Model.SLR;

import Model.GLC.GLCRule;
import Model.GLC.GLCTerm;

import java.util.*;

public class AutomataSLR {

    private List<EstadoSLR> estados;
    private List<GLCRule> reglas;
    private int nextID;

    public AutomataSLR (GLCRule reglaInicial, List<GLCRule> reglas) {

        this.estados = new ArrayList<>();
        this.reglas = reglas;
        this.nextID = 0;

        this.estados.add( new EstadoSLR(nextID, reglaInicial, this.reglas));

        this.nextID++;

        this.generarEstados();

        this.printAutomataSlr();

    }

    private int contains (EstadoSLR estadoB) {

        int index = -1;

        for (EstadoSLR estado: this.estados) {
            index ++;
            if (estado.equals(estadoB)) {
                return index;
            }

        }

        return -1;

    }

    public List<EstadoSLR> getEstados() {
        return estados;
    }

    public void setEstados(List<EstadoSLR> estados) {
        this.estados = estados;
    }

    private void generarEstados(){

        List<EstadoSLR> pending  = new ArrayList<>(estados);
        while (pending.size() > 0) {
            //System.out.println(pending.size() + " estados pendientes");
            EstadoSLR actual = pending.get(0);
            int saveIndex = this.estados.indexOf(actual);

            //System.out.println("Generando Estados y transiciones a partir del estado: " + actual.getId());
            pending.remove(actual);
            //System.out.println("Analizando un estado dejando " + pending.size() + " estados pendientes");
            Dictionary transitions = new Hashtable();
            for (ReglaSLR regla: actual.getReglas()) {

                if (regla.getIndex() < regla.getRegla().getOpciones().get(0).getTerminos().size()) {
                    String transitionVal = ((GLCTerm) regla.getTransitionVal()).getId();
                    if (transitions.get(transitionVal) == null) {
                        transitions.put(transitionVal, new ArrayList<ReglaSLR>());
                    }

                    ((List<ReglaSLR>) transitions.get(transitionVal)).add(regla);
                    //System.out.println("Transicion del estado " + actual.getId() + " con la input " + transitionVal);

                }

            }

            ((Hashtable) transitions).forEach((k,v) -> {
                List<ReglaSLR> aux = new ArrayList<ReglaSLR>();
                for (ReglaSLR x : (List<ReglaSLR>) v) {
                    aux.add(new ReglaSLR(x.getRegla(), x.getNumeroRegla(), (x.getIndex()+1)));
                }

                EstadoSLR newEstado = new EstadoSLR(this.nextID, this.reglas, aux);
                int index = this.contains(newEstado);
                if (index == -1) {
                    //System.out.println("Desde el estado " + actual.getId() + " generamos el nuevo estado " + newEstado.getId() + " con la input " + k);
                // nuevo estado
                    this.nextID++;
                    this.estados.add(newEstado);
                    pending.add(newEstado);
                    //System.out.println("Agregando estado, pending size:  " + pending.size());
                    actual.agregarTransicion(newEstado.getId(), (String) k);

                    //System.out.println("Nuevo automata: ");
                    //this.printAutomataSlr();

                } else {
                // estado ya existe

                    //System.out.println("Agregamos una nueva transicion desde el estado " + actual.getId() + " al estado: " + index );
                    actual.agregarTransicion(this.estados.get(index).getId(), (String) k);

                }

            });

            this.estados.set(saveIndex, actual);

        }

    }

    public void printAutomataSlr () {

        for (EstadoSLR estado : this.estados) {

            System.out.println("Estado : " + estado.getId() + ", Reglas: " + estado.printReglas());

        }

    }

}
