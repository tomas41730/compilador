package Model;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class SLR {

    private GLC glc;

    private GLCRule reglaInicial;

    private AutomataSLR automata;

    private Dictionary tablaSLR;

    private String analisis;

    public SLR () {

        this.analisis ="";
        // ((HashTable) tablaslr.get(0)).get("E") = null error sintaxis
        this.glc = new GLC();
        // generando regla inicial con el id reservado INICIAL
        this.reglaInicial= new GLCRule("INICIAL");
        List<GLCTerm> auxterms = new ArrayList<>();
        auxterms.add((GLCTerm) glc.getNoTerminales().get(glc.getInitialRule().getId()));
        this.reglaInicial.addOption(new GLCoption(auxterms, auxterms.get(0), this.reglaInicial, ".*", 0));
        //
        this.automata = new AutomataSLR(this.reglaInicial, glc.getReglasGLCporNumero());
        this.generarTabla();
        this.printTablaSLR();


        List<Lexema> ejemplo = new ArrayList<>();
        ejemplo.add( new Lexema("1", "int", "k", 0, 1, false));
        ejemplo.add( new Lexema("1", "int", "b", 0, 1, false));
        ejemplo.add( new Lexema("1", "int", "e", 0, 1, false));
        this.analizarCadena(this.tablaSLR, ejemplo);
    }

    private void generarTabla() {
        this.tablaSLR = new Hashtable();
        for (EstadoSLR estado : this.automata.getEstados()) {
            tablaSLR.put(estado.getId(), new Hashtable<>());
            for (TransicionSLR transicion : estado.getTransiciones()) {
                String operacion = "";
                if (this.esTerminal(transicion.getInput())) {
                    operacion = "shift";
                } else {
                    operacion = "move";
                }
                ((Hashtable) this.tablaSLR.get(estado.getId())).put(transicion.getInput(), new OperacionSRL(operacion, transicion.getDestino()));
            }

            for (ReglaSLR regla : estado.getReglas()) {
                if (regla.getRegla().getOpciones().get(0).getTerminos().size() == regla.getIndex()) {
                    if (regla.getNumeroRegla() == -1) {
                        ((Hashtable) this.tablaSLR.get(estado.getId())).put("$", new OperacionSRL());
                    } else {
                        for (GLCTerm siguiente :  regla.getRegla().getSiguientes()) {
                            ((Hashtable) this.tablaSLR.get(estado.getId())).put(siguiente.getId(), new OperacionSRL("reduce", regla.getNumeroRegla()));
                        }
                    }
                }
            }
        }
    }
    private void printTablaSLR() {

        System.out.println("--------------------------------------Tabla SLR--------------------------------------");

        ((Hashtable) this.tablaSLR).forEach((k,v) -> {
            System.out.println("Para el estado " + k + " se tienen las operaciones:: ");

            ((Hashtable) v).forEach((k1, v1) -> {

                System.out.println("Con la input " + k1 + " " + v1);

            });

        });

    }
    private boolean esTerminal(String x) {
        if (this.glc.getTerminales().get(x) != null) {
            return true;
        } else {
            return false;
        }
    }

    private void analizarCadena(Dictionary tabla, List<Lexema> cadenaDesignada){
        System.out.println();
        List<Integer> estados = new ArrayList<>();
        List<Lexema> lexemas = new ArrayList<>();
        estados.add(0);

        String cadenaSimbolos = "";
        for (Lexema auxLex: cadenaDesignada) {
            cadenaSimbolos = cadenaSimbolos + auxLex.getSimbolo();
        }
        analisis = cadenaSimbolos+"      |   0";
        System.out.println(analisis);

        int contador = 0;

        List<Lexema> cadena = new ArrayList<>(cadenaDesignada);
        boolean t = true;
        while (t) {
            Lexema lex = cadena.get(contador);
            String token = lex.getSimbolo();

            if (((Hashtable)tabla.get(estados.get(estados.size()-1))).get(lex.getSimbolo()) != null) {

                OperacionSRL v1 = (OperacionSRL) ((Hashtable)tabla.get(estados.get(estados.size()-1))).get(lex.getSimbolo());
                int k = v1.getEstadoFuturo();

                switch (v1.getTipo()) {
                    case "move":
                        estados.add((int) k);


                    case "shift":
                        estados.add((int) k);
                        lexemas.add(lex);
                        analisis = analisis.substring(1) + token + estados.get(estados.size()-1);
                        System.out.println(analisis);

                    case "reduce":
                        estados.add((int) k);

                    case "accept":
                        estados.add((int) k);
                        t = false;

                    default:

                        break;
                }
                contador++;
            }
        }
    }

}
