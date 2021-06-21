package Model;

import Config.DictManager;

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

    private boolean acepted;

    private List<Error> erroresSintacticos;

    public SLR () {

        this.analisis ="";

        this.erroresSintacticos = new ArrayList<>();

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

        this.acepted = true;

        List<Lexema> ejemplo = new ArrayList<>();
        /*//ejemplo.add( new Lexema("(", "(", "d", 0, 0, false));
        ejemplo.add( new Lexema("(", "(", "k", 0, 0, false));
        ejemplo.add( new Lexema("1", "int", "b", 0, 1, false));
        ejemplo.add( new Lexema("+", "Op. Aritmetico", "d", 0, 2, false));
        ejemplo.add( new Lexema("1", "int", "b", 0, 3, false));
        ejemplo.add( new Lexema(")", ")", "l", 0, 4, false));
        ejemplo.add( new Lexema("==", "Op. Logico", "e", 5, 1, false));
        ejemplo.add( new Lexema("2", "int", "b", 0, 6, false));
        ///
        ejemplo.add( new Lexema("(", "(", "k", 0, 0, false));
        ejemplo.add( new Lexema("1", "int", "b", 0, 1, false));
        ejemplo.add( new Lexema("+", "Op. Aritmetico", "d", 0, 2, false));
        ejemplo.add( new Lexema("1", "int", "b", 0, 3, false));
        ejemplo.add( new Lexema(")", ")", "l", 0, 4, false));
        ejemplo.add( new Lexema("==", "Op. Aritmetico", "d", 5, 1, false));
        ejemplo.add( new Lexema("2", "int", "b", 0, 6, false));


        this.analizarCadena(ejemplo);
        */
    }

    public boolean isAcepted() {
        return acepted;
    }

    public List<Error> getErroresSintacticos() {
        return erroresSintacticos;
    }

    public void setErroresSintacticos(List<Error> erroresSintacticos) {
        this.erroresSintacticos = erroresSintacticos;
    }

    public void setAcepted(boolean acepted) {
        this.acepted = acepted;
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

    private void borrarN(Object lista, int n) {

        for (int i =0; i < n; i++) {

            ((List) lista).remove(((List) lista).size()-1);

        }

    }

    public void analizarCadena(List<Lexema> cadenaDesignada){

        this.acepted = true;

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


        List<Lexema> cadena = new ArrayList<>(cadenaDesignada);
        boolean t = true;
        while (t) {

            if (cadena.size() == 0 ) {

                cadena.add(new Lexema("$", "$", "$", cadenaDesignada.get(cadenaDesignada.size()-1).getFila(), cadenaDesignada.get(cadenaDesignada.size()-1).getColumna()+1, false));

            }

            Lexema lex = cadena.get(0);
            String token = lex.getSimbolo();


            if (((Hashtable)tablaSLR.get(estados.get(estados.size()-1))).get(lex.getSimbolo()) != null) {

                OperacionSRL v1 = (OperacionSRL) ((Hashtable)tablaSLR.get(estados.get(estados.size()-1))).get(lex.getSimbolo());
                int k = v1.getEstadoFuturo();

                switch (v1.getTipo()) {
                    case "move":
                        estados.add((int) k);
                        analisis = analisis + token + estados.get(estados.size()-1);
                        break;

                    case "shift":
                        cadena.remove(0);
                        estados.add((int) k);
                        lexemas.add(lex);
                        analisis = analisis.substring(1) + token + estados.get(estados.size()-1);
                        analisis = analisis.substring(0, cadena.size() + 10) + " { " + estados.toString() + " , " + lexemas.toString() + " }";
                        System.out.println(analisis);
                        break;

                    case "reduce":
                        GLCRule reglaDeReduccion = this.glc.getReglasGLCporNumero().get(k);
                        int numeroTerminosABorrar = reglaDeReduccion.getOpciones().get(0).getTerminos().size();

                        this.borrarN(estados, numeroTerminosABorrar);
                        this.borrarN(lexemas, numeroTerminosABorrar);

                        lexemas.add(new Lexema(reglaDeReduccion.getId(), reglaDeReduccion.getId(), reglaDeReduccion.getId(), -1,-1, false));

                        if (((OperacionSRL) ((Hashtable) this.tablaSLR.get(estados.get(estados.size()-1))).get(lexemas.get(lexemas.size()-1).getSimbolo())) != null) {
                            int nuevoEstado = (int) ((OperacionSRL) ((Hashtable) this.tablaSLR.get(estados.get(estados.size()-1))).get(lexemas.get(lexemas.size()-1).getSimbolo())).getEstadoFuturo();

                            estados.add(nuevoEstado);

                            analisis = analisis.substring(0, cadena.size() + 10) + " { " + estados.toString() + " , " + lexemas.toString() + " }";
                            System.out.println(analisis);

                        } else {

                            //error de tipo 2, no hay move operacion luego de un reduce


                            List<String> expected = new ArrayList<>();

                            String errorMessage = "Error: Expecting {";

                            ((Hashtable) this.tablaSLR.get(estados.get(estados.size()-1))).forEach((key,value) -> {

                                if ( ((OperacionSRL) value).getTipo().equals("move") ) {

                                    expected.add((String) key);

                                }

                            });

                            for(String substring : expected) {

                                errorMessage = errorMessage + (String) DictManager.noTerminalToWord.get(substring) + ", ";

                            }

                            errorMessage = errorMessage.substring(0, errorMessage.length()-2);
                            errorMessage = errorMessage + " } can't continue with sintax analysis.";

                            this.erroresSintacticos.add(new Error("Sintactico", cadena.get(0).getFila(), cadena.get(0).getColumna(), errorMessage));

                            break;

                        }

                        break;

                    case "accept":
                        estados.add((int) k);
                        System.out.println("Analisis sintactico finalizado con exito.");

                        if (this.acepted) {

                            System.out.println("Cadena Aceptada!");

                        } else {

                            System.out.println("Cadena Rechazada");

                        }

                        t = false;
                        break;

                    default:

                        break;
                }


            } else {

                System.out.println("Error en caracter: " + cadena.get(0).getValor() + " locacion: " + cadena.get(0).getFila() + "," + cadena.get(0).getColumna());

                this.acepted = false;

                //Error tipo 1 no hay operacion (s o r) para un terminal dado

                String fakeInput = "";
                String errorMessage = "";

                final List<String> maybeMeant = new ArrayList<>();

                int tipo = -1;

                // tipo 0 : sobra caracter
                // tipo 1: falta caracter
                // tipo 2: caracter erroneo
                // tipo -1 : erro no identificado, no se puede continuar

                if (cadena.size() == 1) {

                    // cadena de 1 caracter pendiente

                    ((Hashtable) this.tablaSLR.get(estados.get(estados.size()-1))).forEach((k,v) -> {

                        if( ((OperacionSRL) v).getTipo().equals("accept") ) {

                            //asumimos que el caracter sobra

                            maybeMeant.add("accept");

                        }else if ( (((OperacionSRL) v).getTipo().equals("shift") || ((OperacionSRL) v).getTipo().equals("reduce") )) {

                            int sigEstado = ((OperacionSRL)v).getEstadoFuturo();

                            if ( ((Hashtable) this.tablaSLR.get(sigEstado)).get("$") != null ) {

                                maybeMeant.add((String) k);

                            }

                        }

                    });

                    if (cadena.get(0).getSimbolo() == "$") {

                        tipo = 1;

                        String expectations = "{ ";

                        for (String pos : maybeMeant) {
                            expectations = expectations + DictManager.simbolToToken().get(pos) + ", ";
                        }

                        expectations = expectations.substring(0, expectations.length()-2);
                        expectations = expectations + " }";

                        errorMessage = "Expected " + expectations + " character at line " + cadena.get(0).getFila() + " and column: " + cadena.get(0).getColumna() + ".";
                        System.out.println(errorMessage);

                        this.erroresSintacticos.add(new Error("Sintactico", cadena.get(0).getFila(), cadena.get(0).getColumna(), errorMessage));

                        cadena.add(0, new Lexema(maybeMeant.get(0), (String) DictManager.simbolToToken().get(maybeMeant.get(0)), maybeMeant.get(0), -1, -1, true));

                    } else if (maybeMeant.contains("accept")) {
                        //sobra
                        tipo = 0;
                        errorMessage = "Unexpected " + cadena.get(0).getValor() + " character at line " + cadena.get(0).getFila() + " and column: " + cadena.get(0).getColumna() + ".";
                        System.out.println(errorMessage);
                        this.erroresSintacticos.add(new Error("Sintactico", cadena.get(0).getFila(), cadena.get(0).getColumna(), errorMessage));
                        cadena.remove(0);

                    } else if (maybeMeant.size() != 0) {
                        // caracter erroneo
                        tipo = 2;

                        String expectations = "{ ";

                        for (String pos : maybeMeant) {
                            expectations = expectations + DictManager.simbolToToken().get(pos) + ", ";
                        }

                        expectations = expectations.substring(0, expectations.length()-2);
                        expectations = expectations + " }";

                        errorMessage = "Expected " + expectations + " character at line " + cadena.get(0).getFila() + " and column: " + cadena.get(0).getColumna() + ", but received " + cadena.get(0) + ".";
                        System.out.println(errorMessage);
                        this.erroresSintacticos.add(new Error("Sintactico", cadena.get(0).getFila(), cadena.get(0).getColumna(), errorMessage));

                        cadena.get(0).setSimbolo(maybeMeant.get(0));

                    }

                    // No se pued eidentificar el error
                    if (tipo == -1) {

                        errorMessage = "Error on character: " + cadena.get(0).getValor() + " at line: " + cadena.get(0).getFila()  + " and column: " + cadena.get(0).getColumna() + ", Unable to continue with sintax analysis." ;
                        System.out.println(errorMessage);
                        this.erroresSintacticos.add(new Error("Sintactico", cadena.get(0).getFila(), cadena.get(0).getColumna(), errorMessage));
                        break;
                    }


                } else if (cadena.size() == 2)
                {

                    // Cadena de 2 caracteres
                    String nextSimbol = cadena.get(1).getSimbolo();

                    if( ((Hashtable) this.tablaSLR.get(estados.get(estados.size()-1))).get(nextSimbol) != null ){

                        tipo = 0;
                        errorMessage = "Unexpected " + cadena.get(0).getValor() + " character at line " + cadena.get(0).getFila() + " and column: " + cadena.get(0).getColumna() + ".";
                        System.out.println(errorMessage);
                        this.erroresSintacticos.add(new Error("Sintactico", cadena.get(0).getFila(), cadena.get(0).getColumna(), errorMessage));
                        cadena.remove(0);

                    }

                    if (tipo == -1) {

                        ((Hashtable) this.tablaSLR.get(estados.get(estados.size()-1))).forEach((k,v) -> {


                                if ( ((OperacionSRL) v).getTipo().equals("shift") || ((OperacionSRL) v).getTipo().equals("reduce") ){

                                        maybeMeant.add((String) k);

                                }


                        });

                        if (maybeMeant.size() != 0) {

                            int fakestate = ((OperacionSRL) ((Hashtable) this.tablaSLR.get(estados.get(estados.size()-1))).get(maybeMeant.get(0))).getEstadoFuturo();

                            if ( ((Hashtable) this.tablaSLR.get(fakestate)).get(nextSimbol) != null ) {

                                tipo = 1;
                                String expectations = "{ ";

                                for (String pos : maybeMeant) {
                                    expectations = expectations + DictManager.simbolToToken().get(pos) + ", ";
                                }

                                expectations = expectations.substring(0, expectations.length()-2);
                                expectations = expectations + " }";

                                errorMessage = "Expected " + expectations + " character at line " + cadena.get(0).getFila() + " and column: " + cadena.get(0).getColumna() + ".";
                                System.out.println(errorMessage);

                                this.erroresSintacticos.add(new Error("Sintactico", cadena.get(0).getFila(), cadena.get(0).getColumna(), errorMessage));

                                cadena.add(0, new Lexema(maybeMeant.get(0), (String) DictManager.simbolToToken().get(maybeMeant.get(0)), maybeMeant.get(0), -1, -1, true));

                            } else {

                                tipo = 2;
                                errorMessage = "Expected " + DictManager.simbolToToken().get(maybeMeant.get(0)) + " character at line " + cadena.get(0).getFila() + " and column: " + cadena.get(0).getColumna() + ", but received " + cadena.get(0) + ".";
                                System.out.println(errorMessage);

                                this.erroresSintacticos.add(new Error("Sintactico", cadena.get(0).getFila(), cadena.get(0).getColumna(), errorMessage));

                                cadena.get(0).setSimbolo(maybeMeant.get(0));


                            }

                        }

                        // No se pued eidentificar el error
                        if (tipo == -1) {

                            errorMessage = "Error on character: " + cadena.get(0).getValor() + " at line: " + cadena.get(0).getFila()  + " and column: " + cadena.get(0).getColumna() + ", Unable to continue with sintax analysis." ;
                            System.out.println(errorMessage);
                            this.erroresSintacticos.add(new Error("Sintactico", cadena.get(0).getFila(), cadena.get(0).getColumna(), errorMessage));
                            break;
                        }

                    }

                } else {

                    // cadena de 3 caracteres o mas

                    String nextSimbol2 = cadena.get(2).getSimbolo();
                    String nextSimbol = cadena.get(1).getSimbolo();

                    if( ((Hashtable) this.tablaSLR.get(estados.get(estados.size()-1))).get(nextSimbol) != null ) {

                        int fakestate = ((OperacionSRL) ((Hashtable) this.tablaSLR.get(estados.get(estados.size()-1))).get(nextSimbol)).getEstadoFuturo();

                        if( ((Hashtable) this.tablaSLR.get(fakestate)).get(nextSimbol2) != null ) {

                            tipo = 0;
                            errorMessage = "Unexpected " + cadena.get(0).getValor() + " character at line " + cadena.get(0).getFila() + " and column: " + cadena.get(0).getColumna() + ".";
                            System.out.println(errorMessage);
                            this.erroresSintacticos.add(new Error("Sintactico", cadena.get(0).getFila(), cadena.get(0).getColumna(), errorMessage));
                            cadena.remove(0);

                        }

                    }

                    if (tipo == -1) {

                        // revisamos con cual input se podria analizar el siguiente lexema

                        ((Hashtable) this.tablaSLR.get(estados.get(estados.size()-1))).forEach((k,v) -> {

                                if ( ((OperacionSRL) v).getTipo().equals("shift") || ((OperacionSRL) v).getTipo().equals("reduce") ){


                                    int sigEstado = ((OperacionSRL)v).getEstadoFuturo();

                                    if ( ( (Hashtable) this.tablaSLR.get(sigEstado) ).get(nextSimbol) != null ) {

                                        maybeMeant.add((String) k);

                                    }


                                }

                        });

                        if (maybeMeant.size() > 0) {

                            // verificamos 2 lexemas a futuro si agregando este caracter faltante no hay problemas

                            //avanzamos del estado actual con maybe meant
                            int fakestate = ((OperacionSRL) ((Hashtable) this.tablaSLR.get(estados.get(estados.size()-1))).get(maybeMeant)).getEstadoFuturo();

                            // verificamos si el caracter actual funciona en el fake state
                            if ( ((Hashtable) this.tablaSLR.get(fakestate)).get(cadena.get(0).getSimbolo()) != null ) {

                                // avanzamos el fake state
                                fakestate = ( (OperacionSRL) ((Hashtable) this.tablaSLR.get(fakestate)).get(cadena.get(0).getSimbolo()) ).getEstadoFuturo();

                                // verificamos si el siguiente caracter funcionaria en el nuevo fake state
                                if (((Hashtable) this.tablaSLR.get(fakestate)).get(nextSimbol) != null) {

                                    tipo = 1;
                                    String expectations = "{ ";

                                    for (String pos : maybeMeant) {
                                        expectations = expectations + DictManager.simbolToToken().get(pos) + ", ";
                                    }

                                    expectations = expectations.substring(0, expectations.length()-2);
                                    expectations = expectations + " }";

                                    errorMessage = "Expected " + expectations + " character at line " + cadena.get(0).getFila() + " and column: " + cadena.get(0).getColumna() + ".";
                                    System.out.println(errorMessage);

                                    this.erroresSintacticos.add(new Error("Sintactico", cadena.get(0).getFila(), cadena.get(0).getColumna(), errorMessage));

                                    cadena.add(0, new Lexema(maybeMeant.get(0), (String) DictManager.simbolToToken().get(maybeMeant.get(0)), maybeMeant.get(0), -1, -1, true));

                                }

                            }

                            // verificamos 2 lexemas a futuro si el caracter actual es erroneo y deberia ser reemplazado por maybe meant

                            if (tipo == -1) {

                                //avanzamos del estado actual con maybe meant
                                fakestate = ((OperacionSRL) ((Hashtable) this.tablaSLR.get(estados.get(estados.size()-1))).get(maybeMeant)).getEstadoFuturo();

                                // veirficamos si el siguiente caracter funcionaria con el fakestate
                                if ( ((Hashtable) this.tablaSLR.get(fakestate)).get(nextSimbol) != null ) {

                                    // avanzamos el fake state
                                    fakestate = ( (OperacionSRL) ((Hashtable) this.tablaSLR.get(fakestate)).get(nextSimbol) ).getEstadoFuturo();

                                    // verificamos si 2 caracteres a futuro si el analisis siguen siendo posible el analisis

                                    if ( ((Hashtable) this.tablaSLR.get(fakestate)).get(nextSimbol2) != null ) {

                                        tipo = 2;

                                        String expectations = "{ ";

                                        for (String pos : maybeMeant) {
                                            expectations = expectations + DictManager.simbolToToken().get(pos) + ", ";
                                        }

                                        expectations = expectations.substring(0, expectations.length()-2);
                                        expectations = expectations + " }";

                                        errorMessage = "Expected " + expectations + " character at line " + cadena.get(0).getFila() + " and column: " + cadena.get(0).getColumna() + ", but received " + cadena.get(0) + ".";
                                        System.out.println(errorMessage);

                                        this.erroresSintacticos.add(new Error("Sintactico", cadena.get(0).getFila(), cadena.get(0).getColumna(), errorMessage));

                                        cadena.get(0).setSimbolo(maybeMeant.get(0));

                                    }


                                }

                            }

                        }


                        // No se pued eidentificar el error
                        if (tipo == -1) {

                            errorMessage = "Error on character: " + cadena.get(0).getValor() + " at line: " + cadena.get(0).getFila()  + " and column: " + cadena.get(0).getColumna() + ", Unable to continue with sintax analysis." ;
                            System.out.println(errorMessage);
                            this.erroresSintacticos.add(new Error("Sintactico", cadena.get(0).getFila(), cadena.get(0).getColumna(), errorMessage));
                            break;
                        }

                    }

                }

            }
        }
    }

}
