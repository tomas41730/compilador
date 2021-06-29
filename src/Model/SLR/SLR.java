package Model.SLR;

import Config.DictManager;
import Model.Error;
import Model.GLC.GLC;
import Model.GLC.GLCRule;
import Model.GLC.GLCTerm;
import Model.GLC.GLCoption;
import Model.AnalizadorLexico.Lexema;

import java.util.*;

public class SLR {

    private GLC glc;

    private GLCRule reglaInicial;

    private AutomataSLR automata;

    private Dictionary tablaSLR;

    private String analisis;

    private boolean acepted;

    private List<Error> erroresSintacticos;

    private ArbolSintactico arbol;

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

    private List<String> obtenerPosibilidades(Hashtable tabla) {

        List<String> list = new ArrayList<>();

        tabla.forEach((k,v) -> {

            list.add((String) k);

        });

        return list;

    }

    private List<String> transformar(List<String> lista) {

        for (String word : lista) {

            String aux;

            aux = (String) DictManager.simbolToToken().get(word);
            if (aux == null) {

                aux = (String) DictManager.noTerminalToDescription().get(word);

            }

            lista.set( lista.indexOf(word), aux );

        }

        return lista;

    }

    public void evaluarCadena(List<Lexema> cadenaAEvaluar) {

        this.acepted = true;

        cadenaAEvaluar.add(new Lexema("$","$","$", cadenaAEvaluar.get(cadenaAEvaluar.size()-1).getFila(), cadenaAEvaluar.get(cadenaAEvaluar.size()-1).getColumna()-1, false));
        List<Integer> estados = new ArrayList<>(Arrays.asList(0));
        List<Lexema> stack = new ArrayList<>();
        List<ArbolSintactico> nodosStack = new ArrayList<>();

        int idNodos = 0;

        List<Lexema> cadenaActual = new ArrayList<>(cadenaAEvaluar);

        boolean checking = true;

        System.out.println("Empezando Evaluacion: ");

        while (checking) {

            System.out.println("Cadena: " + cadenaActual.toString() + ", estados: " + estados.toString() + ", Stack: " + stack.toString());

            Lexema lexemaActual = cadenaActual.get(0);

            int estado_actual = estados.get(estados.size() - 1 );

            Hashtable diccionario_estado_actual = ((Hashtable) this.tablaSLR.get(estado_actual));

            OperacionSRL operacion = (OperacionSRL) diccionario_estado_actual.get(lexemaActual.getSimbolo());

            if ( operacion != null){

                switch (operacion.getTipo()) {

                    case "move":
                        estados.add(operacion.getEstadoFuturo());
                        break;

                    case "shift":
                        cadenaActual.remove(0);
                        estados.add(operacion.getEstadoFuturo());
                        stack.add(lexemaActual);
                        nodosStack.add(new ArbolSintactico(lexemaActual, idNodos));
                        idNodos++;

                        break;

                    case "reduce":
                        GLCRule reglaDeReduccion = this.glc.getReglasGLCporNumero().get(operacion.getEstadoFuturo());
                        int nroABorrar = reglaDeReduccion.getOpciones().get(0).getTerminos().size();

                        List<ArbolSintactico> hijos = new ArrayList<>();

                        for (int i = 0; i < nroABorrar; i++) {

                            hijos.add(0, nodosStack.get(nodosStack.size()-1-i));

                        }

                        this.borrarN(estados, nroABorrar);
                        this.borrarN(stack, nroABorrar);
                        this.borrarN(nodosStack, nroABorrar);

                        stack.add(new Lexema(reglaDeReduccion.getId(), reglaDeReduccion.getId(), reglaDeReduccion.getId(), -1, -1, false));

                        nodosStack.add(new ArbolSintactico(stack.get(stack.size()-1), idNodos));
                        idNodos++;

                        nodosStack.get(nodosStack.size()-1).agregarHijos(hijos);

                        Hashtable nuevo_estado_dict = ((Hashtable) this.tablaSLR.get( estados.get(estados.size()-1) ));
                        OperacionSRL nuevaOperacion = (OperacionSRL) nuevo_estado_dict.get(stack.get(stack.size()-1).getSimbolo());

                        if ( nuevaOperacion != null) {

                            estados.add(nuevaOperacion.getEstadoFuturo());

                        } else {

                            this.acepted = false;

                            List<String> posibilidades = this.obtenerPosibilidades(nuevo_estado_dict);

                            posibilidades = this.transformar(posibilidades);

                            String errorMessage = "Expected: " + posibilidades.toString() + ", but received: " + lexemaActual.getValor() + " at line " + lexemaActual.getFila() + " and column " + lexemaActual.getColumna() + ".";
                            this.erroresSintacticos.add(new Error("Sintactico", lexemaActual.getFila(), lexemaActual.getColumna(), errorMessage));
                            checking = false;
                            break;
                        }

                        break;

                    case "accept":

                        System.out.println("Cadena: " + cadenaActual.toString() + ", estados: " + estados.toString() + ", Stack: " + stack.toString());
                        System.out.println("Finished, status: " + this.acepted);
                        checking = false;

                        this.arbol = nodosStack.get(0);

                        System.out.println("Arbol Sintactico: " + this.arbol.toString());

                        break;

                }

            } else {

                this.acepted = false;

                System.out.println("El estado: " + estado_actual + " no tiene una transicion con el simbolo " + lexemaActual.getValor());

                int cadena_size = cadenaActual.size();

                boolean fixed = false;

                // Primera verificacion:
                if (!fixed) {

                    System.out.println("Verificando si el caracter actual esta demas.");
                    // Verificando si el caracter actual sobra

                    if (cadena_size > 1) {

                        Lexema next = cadenaActual.get(1);

                        OperacionSRL op_aux = (OperacionSRL) diccionario_estado_actual.get(next.getSimbolo());

                        if (op_aux != null) {

                            if (cadena_size == 2) {

                                fixed = true;
                                String errorMessage = "Unexpected " + lexemaActual.getValor() + " character at line " + lexemaActual.getFila() + " and column: " + lexemaActual.getColumna() + ".";
                                System.out.println(errorMessage);
                                this.erroresSintacticos.add(new Error("Sintactico", lexemaActual.getFila(), lexemaActual.getColumna(), errorMessage));
                                cadenaActual.remove(0);

                            } else {

                                int estado_f = op_aux.getEstadoFuturo();

                                if (op_aux.getTipo() == "shift") {

                                    next = cadenaActual.get(2);

                                } else {

                                    next = cadenaActual.get(1);

                                    if (op_aux.getTipo() == "reduce") {

                                        GLCRule reglaDeReduccion = this.glc.getReglasGLCporNumero().get(op_aux.getEstadoFuturo());
                                        int nroABorrar = reglaDeReduccion.getOpciones().get(0).getTerminos().size();

                                        int newEnd = estados.size()- 1 - nroABorrar;

                                        int preEstado = estados.get(newEnd);

                                        estado_f = ( ((OperacionSRL)((Hashtable) this.tablaSLR.get(preEstado)).get(reglaDeReduccion.getId())).getEstadoFuturo() );

                                    }

                                }

                                Hashtable new_dict = (Hashtable) this.tablaSLR.get(estado_f);

                                if (new_dict.get(next.getSimbolo()) != null) {

                                    fixed = true;
                                    String errorMessage = "Unexpected " + lexemaActual.getValor() + " character at line " + lexemaActual.getFila() + " and column: " + lexemaActual.getColumna() + ".";
                                    System.out.println(errorMessage);
                                    this.erroresSintacticos.add(new Error("Sintactico", lexemaActual.getFila(), lexemaActual.getColumna(), errorMessage));
                                    cadenaActual.remove(0);

                                }

                            }

                        }

                    }

                }


                // Segunda verificacion

                if (!fixed) {

                    // falta un caracter

                    System.out.println("Verificando si falta un caracter.");

                    List<String> posibilidades = this.obtenerPosibilidades(diccionario_estado_actual);
                    List<Integer> estados_futuros = new ArrayList<>();
                    List<String> tipo_Ops = new ArrayList<>();

                    for (String pos: posibilidades) {

                        estados_futuros.add(((OperacionSRL) diccionario_estado_actual.get(pos)).getEstadoFuturo());
                        tipo_Ops.add(((OperacionSRL) diccionario_estado_actual.get(pos)).getTipo());
                    }

                    System.out.println("0:" + posibilidades.toString());

                    for (int i = 0; i < posibilidades.size(); i++) {

                        String aRevisar;

                        if (tipo_Ops.get(i) == "shift") {

                            aRevisar = lexemaActual.getSimbolo();

                        } else {

                            aRevisar = posibilidades.get(i);

                            if (tipo_Ops.get(i) == "reduce") {

                                GLCRule reglaDeReduccion = this.glc.getReglasGLCporNumero().get(estados_futuros.get(i));
                                int nroABorrar = reglaDeReduccion.getOpciones().get(0).getTerminos().size();

                                int newEnd = estados.size()- 1 - nroABorrar;

                                int preEstado = estados.get(newEnd);

                                estados.set(i, ( ((OperacionSRL)((Hashtable) this.tablaSLR.get(preEstado)).get(reglaDeReduccion.getId())).getEstadoFuturo() ));

                            }

                        }



                        Hashtable dic_aux = (Hashtable) this.tablaSLR.get(estados_futuros.get(i));

                        OperacionSRL op_aux = (OperacionSRL) dic_aux.get(aRevisar);

                        if (op_aux == null) {

                            posibilidades.remove(i);
                            estados_futuros.remove(i);
                            tipo_Ops.remove(i);
                            i--;

                        }

                    }

                    System.out.println("1:" + posibilidades.toString());

                    if (posibilidades.size() > 0) {

                            fixed = true;

                            cadenaActual.add(0, new Lexema(posibilidades.get(0), (String) DictManager.simbolToToken().get(posibilidades.get(0)), posibilidades.get(0), -1, -1, true));

                            posibilidades = this.transformar(posibilidades);

                            String errorMessage = "Expected '" + posibilidades.toString() + "'  at line " + lexemaActual.getFila() + " and column: " + lexemaActual.getColumna() + ".";
                            System.out.println(errorMessage);

                            this.erroresSintacticos.add(new Error("Sintactico", lexemaActual.getFila(), lexemaActual.getColumna(), errorMessage));


                    }

                }


                // tercera

                if (!fixed) {

                    // caracter equivocado

                    System.out.println("Verificando si el caracter es incorrecto");

                    if (cadena_size > 1) {

                        String next = cadenaActual.get(1).getSimbolo();

                        List<String> posibilidades = this.obtenerPosibilidades(diccionario_estado_actual);
                        List<Integer> estados_futuros = new ArrayList<>();
                        List<String> tipo_Ops = new ArrayList<>();

                        for (String pos: posibilidades) {

                            estados_futuros.add(((OperacionSRL) diccionario_estado_actual.get(pos)).getEstadoFuturo());
                            tipo_Ops.add(((OperacionSRL) diccionario_estado_actual.get(pos)).getTipo());

                        }

                        for (int i = 0; i < posibilidades.size(); i++) {

                            Hashtable dic_aux = (Hashtable) this.tablaSLR.get(estados_futuros.get(i));

                            String aRevisar;

                            if (tipo_Ops.get(i) == "shift") {

                                aRevisar = next;

                            } else {

                                aRevisar = posibilidades.get(i);

                                if (tipo_Ops.get(i) == "reduce") {

                                    GLCRule reglaDeReduccion = this.glc.getReglasGLCporNumero().get(estados_futuros.get(i));
                                    int nroABorrar = reglaDeReduccion.getOpciones().get(0).getTerminos().size();

                                    int newEnd = estados.size()- 1 - nroABorrar;

                                    int preEstado = estados.get(newEnd);

                                    estados.set(i, ( ((OperacionSRL)((Hashtable) this.tablaSLR.get(preEstado)).get(reglaDeReduccion.getId())).getEstadoFuturo() ));

                                }

                            }

                            OperacionSRL op_aux = (OperacionSRL) dic_aux.get(aRevisar);

                            if (op_aux== null) {

                                posibilidades.remove(i);
                                estados_futuros.remove(i);
                                tipo_Ops.remove(i);
                            }

                        }

                        if (posibilidades.size() > 0) {

                            fixed = true;

                            cadenaActual.get(0).setSimbolo(posibilidades.get(0));

                            posibilidades = this.transformar(posibilidades);

                            String errorMessage = "Expected '" + posibilidades.toString() + "'  at line " + lexemaActual.getFila() + " and column: " + lexemaActual.getColumna() + " but received " + lexemaActual.getValor() + ".";
                            System.out.println(errorMessage);

                            this.erroresSintacticos.add(new Error("Sintactico", lexemaActual.getFila(), lexemaActual.getColumna(), errorMessage));


                        }

                    }


                }


                // no se puede identificar

                if (!fixed) {

                    String errorMessage = "No se pudo identificar el error en la fila: " + lexemaActual.getFila() + " y columna: " + lexemaActual.getColumna() + ", lexema: " + lexemaActual.getValor();
                    System.out.println(errorMessage);
                    this.erroresSintacticos.add(new Error("Sintactico", lexemaActual.getFila(), lexemaActual.getColumna(), errorMessage));
                    System.out.println("Deteniendo");
                    checking = false;
                    break;

                }

            }

        }


    }

    public ArbolSintactico getArbol() {
        return arbol;
    }

    public void setArbol(ArbolSintactico arbol) {
        this.arbol = arbol;
    }
}
