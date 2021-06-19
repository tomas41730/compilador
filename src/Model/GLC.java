package Model;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Arrays;

public class GLC {

    private Dictionary terminales;
    private Dictionary noTerminales;
    private Dictionary reglasGLCporLetra;
    private List<GLCRule> reglasGLCporNumero;
    private GLCTerm dolar;
    private List<String> alreadyLooking;
    private GLCRule initialRule;

    public GLC() {

        this.crearTerminales();
        this.crearNoTerminales();
        this.crearReglasGLC();
        this.alreadyLooking = new ArrayList<>();
        this.genPrimeros();
        this.dolar = new GLCTerm(true, "$", "");
        this.alreadyLooking = new ArrayList<>();
        this.genSiguientes();

        for (GLCRule regla : this.reglasGLCporNumero) {

            regla.setSiguientes(((GLCRule) this.reglasGLCporLetra.get(regla.getId())).getSiguientes());

        }

        ((Hashtable) this.reglasGLCporLetra).forEach((k, v) -> {
            System.out.println("Para la regla : " + k + " se tienen los primeros: { " + ((GLCRule) v).printPrimeros() + " } y  los siguientes: { " + ((GLCRule) v).printSegundos() + " }");
        });
    }

    private void genPrimeros(){

        /*((Hashtable) this.reglasGLCporLetra).forEach((k, v) -> {
            this.genPrimerosOfRule((GLCRule) v);
        });*/

        List<GLCRule> pending = new ArrayList<>(this.reglasGLCporNumero);
        while (pending.size() > 0) {

            //System.out.println("Pending size " + pending.size());
            GLCRule regla = pending.get(0);
            //System.out.println(regla.getId());
            pending.remove(regla);

            this.genPrimerosOfRule((GLCRule) this.reglasGLCporLetra.get(regla.getId()));

            if (((GLCRule) this.reglasGLCporLetra.get(regla.getId())).getPrimeros().isEmpty()) {

                pending.add(regla);

            }

        }

    }

    private void genSiguientes(){

        List<GLCRule> pending = new ArrayList<>(this.reglasGLCporNumero);

        while (!(pending.isEmpty())) {

            //System.out.println("Pending size " + pending.size());
            GLCRule regla = pending.get(0);
            //System.out.println(regla.getId());
            pending.remove(regla);
            this.genSiguientesOfRule((GLCRule) this.reglasGLCporLetra.get(regla.getId()));

            if (((GLCRule) this.reglasGLCporLetra.get(regla.getId())).getPrimeros().isEmpty()) {

                pending.add(regla);

            }

        }

    }

    private void genSiguientesOfRule(GLCRule rule) {

        if (rule.getSiguientes().size() == 0){

            //System.out.println("Siguientes de : " + rule.getId());
            this.alreadyLooking.add(rule.getId());

            //System.out.println("Already looking: " + this.alreadyLooking.toString());

            //System.out.println("Generando los siguientes de: " + rule.getId());
            List<GLCTerm> nexts = new ArrayList<>();

            if (rule.isInicial() || this.alreadyLooking.contains(this.initialRule.getId())) {

                nexts.add(this.dolar);

            }

            List<GLCoption> opciones = this.getRuleOccurrence(rule);

            for (GLCoption opcion : opciones) {

                //System.out.println("Para la regla : " + rule.getId() + " estamos en la opcion: " + opcion.printOpcion() + " de la regla " + opcion.getRegla().getId());
                int index = opcion.getTerminos().indexOf((GLCTerm) this.noTerminales.get(rule.getId()));

                //System.out.println("Relga " + rule.getId() + " en la posicion " + index + " de la opcion " + opcion.getId() + " de " + opcion.getRegla().getId());

                if (index == opcion.getTerminos().size()-1) {

                    //if (opcion.getRegla() != rule){
                        if (!(this.alreadyLooking.contains(opcion.getRegla().getId()))) {

                            this.genSiguientesOfRule(opcion.getRegla());
                            for (GLCTerm termino : opcion.getRegla().getSiguientes()) {

                                if (!(nexts.contains(termino))) {

                                    nexts.add(termino);

                                }

                            }
                        }
                    //}

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

                        for (GLCTerm termino : ((GLCRule) this.reglasGLCporLetra.get((opcion.getTerminos().get(index)).getId())).getPrimeros()) {

                            if (!(nexts.contains(termino))) {

                                nexts.add(termino);

                            }

                        }



                        while (nexts.contains(this.terminales.get("eps"))) {

                            nexts.remove(this.terminales.get("eps"));
                            index++;
                            if (index == opcion.getTerminos().size()) {
                                //System.out.println("if");
                                //if (opcion.getRegla() != rule){

                                    if (!(this.alreadyLooking.contains(opcion.getRegla().getId()))) {

                                        this.genSiguientesOfRule(opcion.getRegla());
                                        for (GLCTerm termino : opcion.getRegla().getSiguientes()) {

                                            if (!(nexts.contains(termino))) {

                                                nexts.add(termino);

                                            }

                                        }

                                    }

                                //}

                            } else {
                                //System.out.println("else");
                                if (opcion.getTerminos().get(index).isTerminal()) {

                                    if (!(nexts.contains(opcion.getTerminos().get(index)))) {

                                        nexts.add(opcion.getTerminos().get(index));

                                    }
                                } else {

                                    if (!(this.alreadyLooking.contains(opcion.getTerminos().get(index).getId()))) {
                                        this.genSiguientesOfRule((GLCRule) this.reglasGLCporLetra.get(opcion.getTerminos().get(index).getId()));
                                        for (GLCTerm termino : ((GLCRule) this.reglasGLCporLetra.get(opcion.getTerminos().get(index).getId())).getSiguientes()) {

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

            }

            rule.setSiguientes(nexts);
            this.alreadyLooking.remove(rule.getId());
            //System.out.println("Para la regla " + rule.getId() + " se tienen los siguientes: " + rule.printSegundos());

        }

    }

    private List<GLCoption> getRuleOccurrence(GLCRule rule){

        List<GLCoption> ocurrencias = new ArrayList<>();

        ((Hashtable) this.reglasGLCporLetra).forEach((k, v) -> {

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

            this.alreadyLooking.add(rule.getId());
            //System.out.println("Primeros de la relga: " + rule.getId());

            //System.out.println("Already looking for: " + this.alreadyLooking.toString());

            List<GLCTerm> firsts = new ArrayList<>();

            for(GLCoption option: rule.getOpciones()){

                //System.out.println("Buscando primeros de la relga " + rule.getId() + " en la opcion: " + option.printOpcion());

                if (option.getPrimerTermino().isTerminal()) {

                    if (!(firsts.contains(option.getPrimerTermino()))) {
                        firsts.add(option.getPrimerTermino());
                    }

                } else {
                    int nextOptionIndex = 0;

                    if (!(this.alreadyLooking.contains(option.getPrimerTermino().getId()))) {
                        this.genPrimerosOfRule((GLCRule) this.reglasGLCporLetra.get(option.getPrimerTermino().getId()));
                        for (GLCTerm termino : ((GLCRule) this.reglasGLCporLetra.get(option.getPrimerTermino().getId())).getPrimeros()) {

                            if (!(firsts.contains(termino))) {
                                firsts.add(termino);
                            }

                        }
                    }
                    while (firsts.contains(this.terminales.get("eps"))){

                        firsts.remove(this.terminales.get("eps"));
                        nextOptionIndex++;

                        if (option.getTerminos().get(nextOptionIndex).isTerminal()) {

                            if (!(firsts.contains(option.getTerminos().get(nextOptionIndex)))) {
                                firsts.add(option.getTerminos().get(nextOptionIndex));
                            }

                        }else{

                            if (!(this.alreadyLooking.contains(option.getTerminos().get(nextOptionIndex).getId()))) {
                                this.genPrimerosOfRule((GLCRule) this.reglasGLCporLetra.get(option.getTerminos().get(nextOptionIndex).getId()));
                                for (GLCTerm termino : ((GLCRule) this.reglasGLCporLetra.get(option.getTerminos().get(nextOptionIndex).getId())).getPrimeros()) {

                                    if (!(firsts.contains(termino))) {
                                        firsts.add(termino);
                                    }

                                }

                            }

                        }

                    }


                }

            }

            rule.setPrimeros(firsts);
            this.alreadyLooking.remove(rule.getId());

        }

    }

    private void crearTerminales(){

        this.terminales = new Hashtable();
        this.terminales.put("a", new GLCTerm(true, "a"));
        this.terminales.put("b", new GLCTerm(true, "b"));
        this.terminales.put("c", new GLCTerm(true, "c"));
        this.terminales.put("d", new GLCTerm(true, "d"));
        this.terminales.put("e", new GLCTerm(true, "e"));
        this.terminales.put("f", new GLCTerm(true, "f"));
        this.terminales.put("g", new GLCTerm(true, "g"));
        this.terminales.put("i", new GLCTerm(true, "i"));
        this.terminales.put("j", new GLCTerm(true, "j"));
        this.terminales.put("k", new GLCTerm(true, "k"));
        this.terminales.put("l", new GLCTerm(true, "l"));
        this.terminales.put("m", new GLCTerm(true, "m"));
        this.terminales.put("n", new GLCTerm(true, "n"));
        this.terminales.put("o", new GLCTerm(true, "o"));
        this.terminales.put("p", new GLCTerm(true, "p"));
        this.terminales.put("q", new GLCTerm(true, "q"));
        this.terminales.put("r", new GLCTerm(true, "r"));
        this.terminales.put("s", new GLCTerm(true, "s"));
        this.terminales.put("t", new GLCTerm(true, "t"));
        this.terminales.put("u", new GLCTerm(true, "u"));
        this.terminales.put("v", new GLCTerm(true, "v"));
        this.terminales.put("w", new GLCTerm(true, "w"));
        this.terminales.put("x", new GLCTerm(true, "x"));
        this.terminales.put("y", new GLCTerm(true, "y"));
        this.terminales.put("z", new GLCTerm(true, "z"));
        this.terminales.put("a1", new GLCTerm(true, "a1"));
        this.terminales.put("b1", new GLCTerm(true, "b1"));
        this.terminales.put("c1", new GLCTerm(true, "c1"));

        this.terminales.put("eps", new GLCTerm(true, "eps", ""));

    }

    private void crearNoTerminales() {

        this.noTerminales = new Hashtable();

        this.noTerminales.put("S", new GLCTerm(false, "S", ".*"));
        this.noTerminales.put("A", new GLCTerm(false, "A", ".*"));
        this.noTerminales.put("B", new GLCTerm(false, "B", ".*"));
        this.noTerminales.put("C", new GLCTerm(false, "C", ".*"));
        this.noTerminales.put("D", new GLCTerm(false, "D", ".*"));
        this.noTerminales.put("E", new GLCTerm(false, "E", ".*"));
        this.noTerminales.put("F", new GLCTerm(false, "F", ".*"));
        this.noTerminales.put("G", new GLCTerm(false, "G", ".*"));
        this.noTerminales.put("H", new GLCTerm(false, "H", ".*"));
        this.noTerminales.put("I", new GLCTerm(false, "I", ".*"));
        this.noTerminales.put("J", new GLCTerm(false, "J", ".*"));
        this.noTerminales.put("K", new GLCTerm(false, "K", ".*"));
        this.noTerminales.put("L", new GLCTerm(false, "L", ".*"));
        this.noTerminales.put("N", new GLCTerm(false, "N", ".*"));
        this.noTerminales.put("P", new GLCTerm(false, "P", ".*"));
        this.noTerminales.put("U", new GLCTerm(false, "U", ".*"));
        this.noTerminales.put("V", new GLCTerm(false, "V", ".*"));
        this.noTerminales.put("W", new GLCTerm(false, "W", ".*"));
        this.noTerminales.put("X", new GLCTerm(false, "X", ".*"));
        this.noTerminales.put("Y", new GLCTerm(false, "Y", ".*"));
        this.noTerminales.put("Z", new GLCTerm(false, "Z", ".*"));
        this.noTerminales.put("A1", new GLCTerm(false, "A1", ".*"));
        this.noTerminales.put("B1", new GLCTerm(false, "B1", ".*"));
        this.noTerminales.put("C1", new GLCTerm(false, "C1", ".*"));
        this.noTerminales.put("D1", new GLCTerm(false, "D1", ".*"));
        this.noTerminales.put("E1", new GLCTerm(false, "E1", ".*"));
        this.noTerminales.put("F1", new GLCTerm(false, "F1", ".*"));
        this.noTerminales.put("G1", new GLCTerm(false, "G1", ".*"));
        this.noTerminales.put("H1", new GLCTerm(false, "H1", ".*"));
        this.noTerminales.put("I1", new GLCTerm(false, "I1", ".*"));
        this.noTerminales.put("J1", new GLCTerm(false, "J1", ".*"));
        this.noTerminales.put("K1", new GLCTerm(false, "K1", ".*"));
        this.noTerminales.put("L1", new GLCTerm(false, "L1", ".*"));
        this.noTerminales.put("M1", new GLCTerm(false, "M1", ".*"));


    }

    public Dictionary getTerminales() {
        return terminales;
    }

    public void setTerminales(Dictionary terminales) {
        this.terminales = terminales;
    }

    public Dictionary getNoTerminales() {
        return noTerminales;
    }

    public void setNoTerminales(Dictionary noTerminales) {
        this.noTerminales = noTerminales;
    }

    private void agregarRegla(String reglaID, List<String> contenido)
    {

        GLCRule regla = new GLCRule(reglaID);
        GLCRule reglaN = new GLCRule(reglaID);
        List<GLCTerm> auxterms = new ArrayList<>();
        for(String termino: contenido) {

            if (this.noTerminales.get(termino) != null) {

                auxterms.add((GLCTerm) this.noTerminales.get(termino));

            } else if (this.terminales.get(termino) != null) {

                auxterms.add((GLCTerm) this.terminales.get(termino));

            }

        }

        regla.addOptionWithTerms(auxterms);
        reglaN.addOptionWithTerms(auxterms);
        this.reglasGLCporNumero.add(reglaN);

        if (this.reglasGLCporLetra.get(reglaID) == null) {

            this.reglasGLCporLetra.put(reglaID, regla);

        } else {

            ((GLCRule) this.reglasGLCporLetra.get(reglaID)).addOptionWithTerms(auxterms);

        }

    }

    private void agregarRegla(String reglaID, List<String> contenido, boolean inicial)
    {

        GLCRule regla = new GLCRule(reglaID);
        GLCRule reglaN = new GLCRule(reglaID);
        List<GLCTerm> auxterms = new ArrayList<>();
        for(String termino: contenido) {

            if (this.noTerminales.get(termino) != null) {

                auxterms.add((GLCTerm) this.noTerminales.get(termino));

            } else if (this.terminales.get(termino) != null) {

                auxterms.add((GLCTerm) this.terminales.get(termino));

            }

        }

        regla.addOptionWithTerms(auxterms);
        reglaN.addOptionWithTerms(auxterms);

        if (this.reglasGLCporLetra.get(reglaID) == null) {

            regla.setInicial(inicial);
            reglaN.setInicial(inicial);

            this.initialRule = regla;

            this.reglasGLCporLetra.put(reglaID, regla);

        } else {

            ((GLCRule) this.reglasGLCporLetra.get(reglaID)).addOptionWithTerms(auxterms);

        }

        this.reglasGLCporNumero.add(reglaN);

    }

    private void printReglas() {

        System.out.println("Reglas por letra: ");
        ((Hashtable) this.reglasGLCporLetra).forEach((k, v) -> {

            System.out.println(((GLCRule) v).printRegla());

        });

        System.out.println("Reglas separadas: ");
        int x = 0;
        for (GLCRule regla: this.reglasGLCporNumero) {
            System.out.print(x + ". ");
            System.out.println(regla.printRegla());
            x++;
        }


    }

    private void crearReglasGLC(){

        this.reglasGLCporLetra = new Hashtable();
        this.reglasGLCporNumero = new ArrayList<>();

        // condicion -> expresion operador logico expresion
        this.agregarRegla("E", Arrays.asList("G", "e", "G"), true);

        // expresion -> operacion | constante | callmethod()

        this.agregarRegla("G", Arrays.asList("Z"));
        this.agregarRegla("G", Arrays.asList("X"));
        //this.agregarRegla("G", Arrays.asList("H"));

        // operacion -> operacion_aritmetica | condicion

        this.agregarRegla("Z", Arrays.asList("A1"));
        this.agregarRegla("Z", Arrays.asList("E"));

        // operacion_aritmetica -> Expresion operador aritmetico Expresion

        this.agregarRegla("A1", Arrays.asList("G", "d", "G"));

        // constante -> entero | string | bool | lista

        this.agregarRegla("X", Arrays.asList("b"));
        this.agregarRegla("X", Arrays.asList("g"));
        this.agregarRegla("X", Arrays.asList("W"));
        // this.agregarRegla("X", Arrays.asList("E1"));

        // bool -> true | false
        this.agregarRegla("W", Arrays.asList("a1"));
        this.agregarRegla("W", Arrays.asList("b1"));

        this.printReglas();

    }

    public List<GLCRule> getReglasGLCporNumero() {
        return reglasGLCporNumero;
    }

    public void setReglasGLCporNumero(List<GLCRule> reglasGLCporNumero) {
        this.reglasGLCporNumero = reglasGLCporNumero;
    }

    public GLCRule getInitialRule() {
        return initialRule;
    }

    public void setInitialRule(GLCRule initialRule) {
        this.initialRule = initialRule;
    }
}
