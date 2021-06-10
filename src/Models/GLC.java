package Models;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class GLC {

    private Dictionary terminales;
    private Dictionary noTerminales;
    private Dictionary reglasGLC;
    private GLCTerm dolar;

    public GLC() {

        this.crearTerminales();
        this.crearNoTerminales();
        this.crearReglasGLC();
        this.genPrimeros();
        this.dolar = new GLCTerm(true, "$", "");
        this.genSiguientes();
    }

    private void genPrimeros(){

        ((Hashtable) this.reglasGLC).forEach((k,v) -> {
            this.genPrimerosOfRule((GLCRule) v);
        });
    }

    private void genSiguientes(){

        ((Hashtable) this.reglasGLC).forEach((k,v) -> {
            this.genSiguientesOfRule((GLCRule) v);
        });
    }

    private void genSiguientesOfRule(GLCRule rule) {

        if (rule.getSiguientes().size() == 0){

            //System.out.println("Generando los siguientes de: " + rule.getId());
            List<GLCTerm> nexts = new ArrayList<>();

            if (rule.isInicial()) {

                nexts.add(this.dolar);

            }

            List<GLCoption> opciones = this.getRuleOccurrence(rule);

            for (GLCoption opcion : opciones) {

                int index = opcion.getTerminos().indexOf((GLCTerm) this.noTerminales.get(rule.getId()));

                //System.out.println("Relga " + rule.getId() + " en la posicion " + index + " de la opcion " + opcion.getId() + " de " + opcion.getRegla().getId());

                if (index == opcion.getTerminos().size()-1) {

                    if (opcion.getRegla() != rule){

                        this.genSiguientesOfRule(opcion.getRegla());
                        for (GLCTerm termino: opcion.getRegla().getSiguientes()){

                            if (!(nexts.contains(termino))) {

                                nexts.add(termino);

                            }

                        }

                    }

                } else {

                    index++;

                    //System.out.println("de la regla " + opcion.getRegla().getId() + " opcion " + opcion.getId());
                    //System.out.println("opcion: " + opcion.printOpcion() + " index " + index);
                    if (opcion.getTerminos().get(index).isTerminal()) {

                        if (!(nexts.contains(opcion.getTerminos().get(index)))) {

                            nexts.add(opcion.getTerminos().get(index));

                        }

                    } else {

                        //System.out.println("ELSE en la relga " + rule.getId() + " de la opcion " + opcion.printOpcion());

                        for (GLCTerm termino : ((GLCRule) this.reglasGLC.get((opcion.getTerminos().get(index)).getId())).getPrimeros()) {

                            if (!(nexts.contains(termino))) {

                                nexts.add(termino);

                            }

                        }



                        while (nexts.contains(this.terminales.get("lambda"))) {

                            nexts.remove(this.terminales.get("lambda"));
                            index++;
                            if (index == opcion.getTerminos().size()) {
                                //System.out.println("if");
                                if (opcion.getRegla() != rule){

                                    this.genSiguientesOfRule(opcion.getRegla());
                                    for (GLCTerm termino: opcion.getRegla().getSiguientes()){

                                        if (!(nexts.contains(termino))) {

                                            nexts.add(termino);

                                        }

                                    }

                                }

                            } else {
                                //System.out.println("else");
                                if (opcion.getTerminos().get(index).isTerminal()) {

                                    if (!(nexts.contains(opcion.getTerminos().get(index)))) {

                                        nexts.add(opcion.getTerminos().get(index));

                                    }
                                } else {

                                    this.genSiguientesOfRule((GLCRule) this.reglasGLC.get(opcion.getTerminos().get(index).getId()));
                                    for (GLCTerm termino : ((GLCRule) this.reglasGLC.get(opcion.getTerminos().get(index).getId())).getSiguientes()) {

                                        if (!(nexts.contains(termino))) {

                                            nexts.add(termino);

                                        }

                                    }
                                }
                            }
                        }

                    }

                }

            }



            rule.setSiguientes(nexts);
            System.out.println("Para la regla " + rule.getId() + " se tienen los siguientes: " + rule.printSegundos());

        }

    }

    private List<GLCoption> getRuleOccurrence(GLCRule rule){

        List<GLCoption> ocurrencias = new ArrayList<>();

        ((Hashtable) this.reglasGLC).forEach((k,v) -> {

            for (GLCoption option: ((GLCRule) v).getOpciones()){

                if (option.hasTerm((GLCTerm) this.noTerminales.get(rule.getId()))) {

                    ocurrencias.add(option);

                }

            }

        });

        //System.out.println("Occurrences for  " + rule.getId() + " are " + ocurrencias.size());

        return ocurrencias;

    }

    private void genPrimerosOfRule(GLCRule rule) {

        if (rule.getPrimeros().size() == 0){

            List<GLCTerm> firsts = new ArrayList<>();

            for(GLCoption option: rule.getOpciones()){

                if (option.getPrimerTermino().isTerminal()) {

                    if (!(firsts.contains(option.getPrimerTermino()))) {
                        firsts.add(option.getPrimerTermino());
                    }

                } else {
                    int nextOptionIndex = 0;
                    this.genPrimerosOfRule((GLCRule) this.reglasGLC.get(option.getPrimerTermino().getId()));
                    for(GLCTerm termino : ((GLCRule) this.reglasGLC.get(option.getPrimerTermino().getId())).getPrimeros()){

                        if (!(firsts.contains(termino))) {
                            firsts.add(termino);
                        }

                    }

                    while (firsts.contains(this.terminales.get("lambda"))){

                        firsts.remove(this.terminales.get("lambda"));
                        nextOptionIndex++;

                        if (option.getTerminos().get(nextOptionIndex).isTerminal()) {

                            if (!(firsts.contains(option.getTerminos().get(nextOptionIndex)))) {
                                firsts.add(option.getTerminos().get(nextOptionIndex));
                            }

                        }else{

                            this.genPrimerosOfRule((GLCRule) this.reglasGLC.get(option.getTerminos().get(nextOptionIndex).getId()));
                            for(GLCTerm termino : ((GLCRule) this.reglasGLC.get(option.getTerminos().get(nextOptionIndex).getId())).getPrimeros()){

                                if (!(firsts.contains(termino))) {
                                    firsts.add(termino);
                                }

                            }

                        }

                    }


                }

            }

            rule.setPrimeros(firsts);
            System.out.println("Para la regla " + rule.getId() + " se tienen los primeros: " + rule.printPrimeros());
        }

    }

    private void crearTerminales(){

        this.terminales = new Hashtable();
        terminales.put("+", new GLCTerm(true, "+", "\\+"));
        terminales.put("-", new GLCTerm(true, "-", "-"));
        terminales.put("*", new GLCTerm(true, "*", "\\*"));
        terminales.put("/", new GLCTerm(true, "/", "\\/"));

        terminales.put("(", new GLCTerm(true, "(", "\\("));
        terminales.put(")", new GLCTerm(true, ")", "\\)"));

        terminales.put("num", new GLCTerm(true, "num", "num"));
        terminales.put("id", new GLCTerm(true, "id", "id"));
        terminales.put("lambda", new GLCTerm(true, "lambda", ""));

    }

    private void crearNoTerminales() {

        this.noTerminales = new Hashtable();

        this.noTerminales.put("E", new GLCTerm(false, "E", ".*"));
        this.noTerminales.put("E'", new GLCTerm(false, "E'", ".*"));
        this.noTerminales.put("T", new GLCTerm(false, "T", ".*"));
        this.noTerminales.put("T'", new GLCTerm(false, "T'", ".*"));
        this.noTerminales.put("F", new GLCTerm(false, "F", ".*"));

    }

    private void crearReglasGLC(){

        this.reglasGLC = new Hashtable();

        // E

        this.reglasGLC.put("E", new GLCRule("E"));
        List<GLCTerm> auxterms = new ArrayList<>();
        auxterms.add((GLCTerm) this.noTerminales.get("T"));
        auxterms.add((GLCTerm) this.noTerminales.get("E'"));
        ((GLCRule) this.reglasGLC.get("E")).addOption(new GLCoption(auxterms, (GLCTerm) auxterms.get(0), (GLCRule) this.reglasGLC.get("E"), ".*", 0));

        ((GLCRule) this.reglasGLC.get("E")).setInicial(true);

        // E'

        this.reglasGLC.put("E'", new GLCRule("E'"));
        auxterms = new ArrayList<>();
        auxterms.add((GLCTerm) this.terminales.get("+"));
        auxterms.add((GLCTerm) this.noTerminales.get("T"));
        auxterms.add((GLCTerm) this.noTerminales.get("E'"));
        ((GLCRule) this.reglasGLC.get("E'")).addOption(new GLCoption(auxterms, (GLCTerm) auxterms.get(0), (GLCRule) this.reglasGLC.get("E'"), "\\+.*", 0));

        auxterms = new ArrayList<>();
        auxterms.add((GLCTerm) this.terminales.get("-"));
        auxterms.add((GLCTerm) this.noTerminales.get("T"));
        auxterms.add((GLCTerm) this.noTerminales.get("E'"));
        ((GLCRule) this.reglasGLC.get("E'")).addOption(new GLCoption(auxterms, (GLCTerm) auxterms.get(0), (GLCRule) this.reglasGLC.get("E'"), "-.*", 1));

        auxterms = new ArrayList<>();
        auxterms.add((GLCTerm) this.terminales.get("lambda"));
        ((GLCRule) this.reglasGLC.get("E'")).addOption(new GLCoption(auxterms, (GLCTerm) auxterms.get(0), (GLCRule) this.reglasGLC.get("E'"), ".*", 2));

        // T

        this.reglasGLC.put("T", new GLCRule("T"));
        auxterms = new ArrayList<>();
        auxterms.add((GLCTerm) this.noTerminales.get("F"));
        auxterms.add((GLCTerm) this.noTerminales.get("T'"));

        ((GLCRule) this.reglasGLC.get("T")).addOption(new GLCoption(auxterms, (GLCTerm) auxterms.get(0), (GLCRule) this.reglasGLC.get("T"), ".*", 0));

        // T'

        this.reglasGLC.put("T'", new GLCRule("T'"));
        auxterms = new ArrayList<>();
        auxterms.add((GLCTerm) this.terminales.get("*"));
        auxterms.add((GLCTerm) this.noTerminales.get("F"));
        auxterms.add((GLCTerm) this.noTerminales.get("T'"));

        ((GLCRule) this.reglasGLC.get("T'")).addOption(new GLCoption(auxterms, (GLCTerm) auxterms.get(0), (GLCRule) this.reglasGLC.get("T'"), "\\*.*", 0));

        auxterms = new ArrayList<>();
        auxterms.add((GLCTerm) this.terminales.get("/"));
        auxterms.add((GLCTerm) this.noTerminales.get("F"));
        auxterms.add((GLCTerm) this.noTerminales.get("T'"));

        ((GLCRule) this.reglasGLC.get("T'")).addOption(new GLCoption(auxterms, (GLCTerm) auxterms.get(0), (GLCRule) this.reglasGLC.get("T'"), "\\/.*", 1));

        auxterms = new ArrayList<>();
        auxterms.add((GLCTerm) this.terminales.get("lambda"));


        ((GLCRule) this.reglasGLC.get("T'")).addOption(new GLCoption(auxterms, (GLCTerm) auxterms.get(0), (GLCRule) this.reglasGLC.get("T'"), ".*", 2));

        // F

        this.reglasGLC.put("F", new GLCRule("F"));

        auxterms = new ArrayList<>();
        auxterms.add((GLCTerm) this.terminales.get("("));
        auxterms.add((GLCTerm) this.noTerminales.get("E"));
        auxterms.add((GLCTerm) this.terminales.get(")"));

        ((GLCRule) this.reglasGLC.get("F")).addOption(new GLCoption(auxterms, (GLCTerm) auxterms.get(0), (GLCRule) this.reglasGLC.get("F"), "\\(.*\\)", 0));

        auxterms = new ArrayList<>();
        auxterms.add((GLCTerm) this.terminales.get("num"));
        ((GLCRule) this.reglasGLC.get("F")).addOption(new GLCoption(auxterms, (GLCTerm) auxterms.get(0), (GLCRule) this.reglasGLC.get("F"), "num", 1));

        auxterms = new ArrayList<>();
        auxterms.add((GLCTerm) this.terminales.get("id"));
        ((GLCRule) this.reglasGLC.get("F")).addOption(new GLCoption(auxterms, (GLCTerm) auxterms.get(0), (GLCRule) this.reglasGLC.get("F"), "id", 2));

    }

}
