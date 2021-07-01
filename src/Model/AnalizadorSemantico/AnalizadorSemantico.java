package Model.AnalizadorSemantico;

import Config.DictManager;
import Model.AnalizadorLexico.Lexema;
import Model.Error;
import Model.SLR.ArbolSintactico;

import java.util.ArrayList;
import java.util.List;

public class AnalizadorSemantico {

    private ArbolSintactico arbolSintatico;
    private TablaScopes tabla;
    private List<Error> erroresSemanticos;

    public  AnalizadorSemantico(ArbolSintactico arbolSintatico) {

        this.erroresSemanticos = new ArrayList<>();
        this.arbolSintatico = arbolSintatico;
        this.tabla = this.generarTablas(this.arbolSintatico, -1, this.arbolSintatico.getId());
        this.actualizarConPadres(this.tabla);
        this.finalizarAnalisis(this.arbolSintatico, this.tabla);
        this.buscarMain();
        this.printTablas();
    }

    private void buscarMain() {

        ElementoTabla main= null;

        for (ElementoTabla elementoTabla : this.tabla.getElementosDelScope()){

            if(elementoTabla.getIdentificador().equals("main")) {

                main = elementoTabla;
                break;

            }

        }

        if (main == null) {

            this.erroresSemanticos.add(new Error("Semantico", 0, 0, "No se encontro el metodo principal 'main'"));

        } else if (!main.isMetodo()) {

            this.erroresSemanticos.add(new Error("Semantico", main.getLexema().getFila(), main.getLexema().getColumna(), "El identificador main solo debe ser usado para el metodo principal"));

        }

    }

    private void finalizarAnalisis(ArbolSintactico arbol, TablaScopes tablaDelScope){

        TablaScopes tablaHeredada = tablaDelScope;

        if (arbol.getData().equals("K") || arbol.getData().equals("I") || arbol.getData().equals("J") || arbol.getData().equals("V")) {

            TablaScopes nuevaTabla = this.buscarSubscope(tablaDelScope, arbol.getId());
            if (nuevaTabla != null) {

                //System.out.println("Cambiando de Scope de: " + tablaDelScope.getIdentificador() + " a " + nuevaTabla.getIdentificador() );
                tablaHeredada = nuevaTabla;

            } else {

                System.out.println("Unexpected error");

            }

            if ( arbol.getData().equals("K") ) {

                // verificar el return

                List<ArbolSintactico> returns = this.buscarReturns(arbol);

                Lexema identificador = arbol.getHijos().get(1).getLexema();
                ElementoTabla variable = this.buscarIdentificador(nuevaTabla, identificador.getValor());

                if (variable.getTipo().equals("y")) {

                    if (returns.size() > 0) {

                        for (ArbolSintactico r : returns) {

                            this.erroresSemanticos.add(new Error("Semantico", r.getLexema().getFila(), r.getLexema().getColumna(), "No se esperaba un return para el tipo void"));

                        }

                    }

                } else  if (returns.size() > 0) {

                    for (ArbolSintactico r : returns) {

                        String tipoErrDer = this.determinarTipoExpresion(r, nuevaTabla);

                        if (!variable.getTipo().equals( tipoErrDer )) {

                            String tipoErrIzq = variable.getTipo();

                            if (tipoErrDer.length() == 1) {

                                tipoErrDer = (String) DictManager.simbolToToken().get(tipoErrDer);

                            } else if (!tipoErrDer.equals("[any]")) {

                                tipoErrDer = "[" + (String) DictManager.simbolToToken().get(tipoErrDer.substring(1,2)) + "]";

                            }

                            if (tipoErrIzq.length() == 1) {

                                tipoErrIzq = (String) DictManager.simbolToToken().get(tipoErrIzq);

                            } else if (!tipoErrIzq.equals("[any]")) {

                                tipoErrIzq = "[" + (String) DictManager.simbolToToken().get(tipoErrIzq.substring(1,2)) + "]";

                            }

                            this.erroresSemanticos.add(new Error("Semantico", r.getLexema().getFila(), r.getLexema().getColumna(), "Se esperaba un return del tipo '" + tipoErrIzq + "', pero se recibio el tipo '" + tipoErrDer + "'"));

                        }

                    }

                } else {

                    this.erroresSemanticos.add(new Error("Semantico", identificador.getFila(), identificador.getColumna(), "No se encontro un return para el metodo '" + identificador.getValor() + "'"));

                }


            } else if (arbol.getData().equals("I")) {

                //for
                this.determinarTipoExpresion( arbol.getHijos().get(4) ,nuevaTabla);
                this.determinarTipoExpresion( arbol.getHijos().get(6) ,nuevaTabla);

            } else if (arbol.getData().equals("J")) {

                //while
                this.determinarTipoExpresion( arbol.getHijos().get(2) ,nuevaTabla);

            } else if (arbol.getData().equals("V")) {

                //while
                this.determinarTipoExpresion( arbol.getHijos().get(2) ,nuevaTabla);

            }


        } else if (arbol.getData().equals("F")) {

            // Asignacion

            Lexema identificador = arbol.getHijos().get(0).getLexema();
            ElementoTabla variable = this.buscarIdentificador(tablaDelScope, identificador.getValor());
            ////

            if (this.variableCorrecta(variable, identificador)) {

                variable.setInicializado(true);

                // verificamos asignacion

                String tipoDerecho = this.determinarTipoExpresion(arbol.getHijos().get(2), tablaDelScope);

                if (!variable.getTipo().equals(tipoDerecho)) {

                    String tipoErrDer = tipoDerecho;

                    String tipoErrIzq = variable.getTipo();

                    if (tipoErrDer.length() == 1) {

                        tipoErrDer = (String) DictManager.simbolToToken().get(tipoErrDer);

                    } else if (!tipoErrDer.equals("[any]")) {

                        tipoErrDer = "[" + (String) DictManager.simbolToToken().get(tipoErrDer.substring(1,2)) + "]";

                    }

                    if (tipoErrIzq.length() == 1) {

                        tipoErrIzq = (String) DictManager.simbolToToken().get(tipoErrIzq);

                    } else if (!tipoErrIzq.equals("[any]")) {

                        tipoErrIzq = "[" + (String) DictManager.simbolToToken().get(tipoErrIzq.substring(1,2)) + "]";

                    }

                    this.erroresSemanticos.add(new Error("Semantico", arbol.getHijos().get(1).getLexema().getFila(), arbol.getHijos().get(1).getLexema().getColumna(), "No se puede asignar el tipo '" + tipoErrDer + "'  a la variable '" + identificador.getValor() + "' del tipo '" + tipoErrIzq + "'" ));

                }

            }

            ////

        } else if (arbol.getData().equals("H")) {

            Lexema identificador = arbol.getHijos().get(0).getLexema();
            ElementoTabla variable = this.buscarIdentificador(tablaDelScope, identificador.getValor());

            // verificamos que el id exista y corresponda a un metodo

            if (this.metodoCorrecto(variable, identificador)) {

                System.out.println("Analizando method call " + identificador.getValor());

                // verificamos parametros

                this.vefificarParametros(arbol.getHijos().get(2), tablaDelScope, variable, identificador.getFila(), identificador.getColumna());

            }

        }

        for(ArbolSintactico hijo : arbol.getHijos()) {

            this.finalizarAnalisis(hijo, tablaHeredada);

        }

    }

    private List<ArbolSintactico> buscarReturns(ArbolSintactico arbol){

        List<ArbolSintactico> returns = new ArrayList<>();

        if (arbol.getData().equals("C") || arbol.getData().equals("K")) {

            for (ArbolSintactico hijo : arbol.getHijos()) {

                returns.addAll(this.buscarReturns(hijo));

            }

        } else if (arbol.getData().equals("D")) {

            if (arbol.getHijos().get(0).getData().equals("z")) {

                returns.add(arbol.getHijos().get(1));

            }

        }

        return returns;

    }

    private void vefificarParametros(ArbolSintactico arbol, TablaScopes tablaScope, ElementoTabla metodo, int fila,  int columna) {

        List<String> parametrosCall = this.getTipoParametros(arbol, tablaScope);
        List<String> parametrosMethod = this.getParametrosFromMethod(metodo.getReferenciaArbol().getHijos().get(3));

        System.out.println("Received: " + parametrosCall.size() + " expected: " + parametrosMethod.size());

        if (parametrosCall.size() == parametrosMethod.size()) {

            for (int i = 0; i < parametrosMethod.size(); i++) {

                if (!parametrosCall.get(i).equals(parametrosMethod.get(i))) {

                    for (int j = 0; j < parametrosMethod.size(); j++) {

                        String tipoErr1 = parametrosCall.get(j);

                        if (tipoErr1.length() == 1) {

                            tipoErr1 = (String) DictManager.simbolToToken().get(tipoErr1);

                        } else if (!tipoErr1.equals("[any]")){

                            tipoErr1 = "[" + (String) DictManager.simbolToToken().get(tipoErr1.substring(1,2)) + "]";

                        }

                        parametrosCall.set(j, tipoErr1);

                        String tipoErr2 = parametrosMethod.get(j);

                        if (tipoErr2.length() >= 1) {

                            tipoErr2 = (String) DictManager.simbolToToken().get(tipoErr2);

                        } else if (!tipoErr2.equals("[any]")){

                            tipoErr2 = "[" + (String) DictManager.simbolToToken().get(tipoErr2.substring(1,2)) + "]";

                        }

                        parametrosMethod.set(j, tipoErr2);

                    }

                    this.erroresSemanticos.add(new Error("Semantico", fila, columna, "Se esperaban parametros de la forma (" + parametrosMethod.toString().substring(1,parametrosMethod.toString().length()-1) + ") , pero se recibieron (" + parametrosCall.toString().substring(1, parametrosCall.toString().length()-1) + ")"));
                    break;
                }

            }

        } else {

            this.erroresSemanticos.add(new Error("Semantico", fila, columna, "Se esperaban " + parametrosMethod.size() + " parametros, pero se recibieron " + parametrosCall.size()));

        }



    }

    private List<String> getTipoParametros(ArbolSintactico arbol, TablaScopes tablaScope){

        List<String> tipos = new ArrayList<>();

        if(arbol.getData().equals("K1")) {

            tipos.add(this.determinarTipoExpresion(arbol, tablaScope));

        } else if (arbol.getData().equals("L1") || arbol.getData().equals("M1")) {

            for (ArbolSintactico hijo : arbol.getHijos()) {

                tipos.addAll(this.getTipoParametros(hijo, tablaScope));

            }

        }

        return tipos;

    }

    private List<String> getParametrosFromMethod(ArbolSintactico arbol){


        List<String> parametros = new ArrayList<>();

        if (arbol.getData().equals("J1") || arbol.getData().equals("D1")) {

            for (ArbolSintactico hijo : arbol.getHijos()) {

                parametros.addAll(this.getParametrosFromMethod(hijo));

            }

        } else if (arbol.getData().equals("B1")) {

            ElementoTabla declaracion = this.analizarDeclaracion(arbol);
            parametros.add(declaracion.getTipo());

        }

        return parametros;

    }

    private boolean variableCorrecta(ElementoTabla variable, Lexema identificador){

        if (variable != null) {

            // verificamos que el identificador no sea de un metodo

            if (variable.isMetodo()) {

                this.erroresSemanticos.add(new Error("Semantico", identificador.getFila(), identificador.getColumna(), "El identificador '" + identificador.getValor() + "' corresponde a un metodo, por ende no se puede asignar un valor."));
                return false;

            } else {

                return true;

            }

        } else {

            this.erroresSemanticos.add(new Error("Semantico", identificador.getFila(), identificador.getColumna(), "El identificador '" + identificador.getValor() + "' no ha sido declarado."));
            return false;
        }

    }

    private boolean metodoCorrecto(ElementoTabla variable, Lexema identificador) {

        if (variable != null) {

            // verificamos que corresponda a un metodo

            if (!variable.isMetodo()) {

                this.erroresSemanticos.add(new Error("Semantico", identificador.getFila(), identificador.getColumna(), "El identificador '" + identificador.getValor() + "' corresponde a una variable."));
                return false;

            } else {

                return true;

            }

        } else {

            this.erroresSemanticos.add(new Error("Semantico", identificador.getFila(), identificador.getColumna(), "El identificador '" + identificador.getValor() + "' no ha sido declarado."));

            return false;

        }

    }

    private String determinarTipoExpresion(ArbolSintactico arbol, TablaScopes tablaScope) {

        //System.out.println("determinando el tipo de: " + arbol.getData());
        String tipo = "error";

        if (arbol.getData().equals("G") || arbol.getData().equals("Z") || arbol.getData().equals("X") || arbol.getData().equals("K1")) {

            //System.out.println("0");

            if (arbol.getHijos().size() ==1 ) {

                tipo = this.determinarTipoExpresion(arbol.getHijos().get(0), tablaScope);

            } else {

                tipo = this.determinarTipoExpresion(arbol.getHijos().get(1), tablaScope);

            }

        } else if ( arbol.getData().equals("a") ) {

            //System.out.println("1");

            Lexema identificador = arbol.getLexema();
            ElementoTabla variable = this.buscarIdentificador(tablaScope, identificador.getValor());

            if (this.variableCorrecta(variable, identificador)) {

                //verificar inicializacion

                if (!variable.isInicializado()) {

                    this.erroresSemanticos.add(new Error("Semantico", identificador.getFila(), identificador.getColumna(), "La variable '" + identificador.getValor() + "', no ha sido inicializada"));

                }

                //

                return variable.getTipo();

            }

        } else if ( arbol.getData().equals("b") ) {


            return "q";

        } else if ( arbol.getData().equals("c") ) {

            return "r";

        } else if ( arbol.getData().equals("g") ) {

            return "x";

        } else if ( arbol.getData().equals("W") ) {

            return "s";

        } else if ( arbol.getData().equals("E1") ) {

            ArbolSintactico C1 = arbol.getHijos().get(1);
            tipo = "[" + this.determinarTipoExpresion(C1.getHijos().get(0), tablaScope) + "]";

        } else if ( arbol.getData().equals("n") ) {

            return "any";

        } else if ( arbol.getData().equals("H") ) {

            Lexema identificador = arbol.getHijos().get(0).getLexema();
            ElementoTabla variable = this.buscarIdentificador(tablaScope, identificador.getValor());

            if (this.metodoCorrecto(variable, identificador)) {

                return variable.getTipo();

            }

        } else if ( arbol.getData().equals("E") ) {

            System.out.println("En la condicion " + arbol.getId());

            String tipo1 = this.determinarTipoExpresion(arbol.getHijos().get(0), tablaScope);
            String tipo2 = this.determinarTipoExpresion(arbol.getHijos().get(2), tablaScope);
            String operador = arbol.getHijos().get(1).getLexema().getValor();

            System.out.println(tipo1 + " " + operador + " " + tipo2);

            if (!tipo2.equals("error") && !tipo1.equals("error") && (tipo1.equals(tipo2) || (tipo1.equals("[any]") && tipo2.length() > 1) || (tipo2.equals("[any]") && tipo1.length() > 1))) {

                if (operador.equals("&&") || operador.equals("||")) {

                    if (tipo1.equals("s") && tipo2.equals("s")) {

                        tipo = "s";

                    } else {

                        String tipoErr1 = tipo1;
                        String tipoErr2 = tipo2;

                        if (tipoErr1.length() == 1) {

                            tipoErr1 = (String) DictManager.simbolToToken().get(tipoErr1);

                        } else if (!tipoErr1.equals("[any]")){

                            tipoErr1 = "[" + (String) DictManager.simbolToToken().get(tipoErr1.substring(1,2)) + "]";

                        }

                        if (tipoErr2.length() == 1) {

                            tipoErr2 = (String) DictManager.simbolToToken().get(tipoErr2);

                        } else if (!tipoErr2.equals("[any]")){

                            tipoErr2 = "[" + (String) DictManager.simbolToToken().get(tipoErr2.substring(1,2)) + "]";

                        }
                        this.erroresSemanticos.add(new Error("Semantico", arbol.getHijos().get(1).getLexema().getFila(), arbol.getHijos().get(1).getLexema().getColumna(), "El operador '" + operador + "' no permite los tipos ('" + tipoErr1 + "', " + tipoErr2 + "')"));

                    }

                } else {

                    tipo = "s";

                }


            } else if (!tipo1.equals("error") && !tipo2.equals("error")){

                String tipoErr1 = tipo1;
                String tipoErr2 = tipo2;

                if (tipoErr1.length() == 1) {

                    tipoErr1 = (String) DictManager.simbolToToken().get(tipoErr1);

                } else if (!tipoErr1.equals("[any]")){

                    tipoErr1 = "[" + (String) DictManager.simbolToToken().get(tipoErr1.substring(1,2)) + "]";

                }

                if (tipoErr2.length() == 1) {

                    tipoErr2 = (String) DictManager.simbolToToken().get(tipoErr2);

                } else if (!tipoErr2.equals("[any]")){

                    tipoErr2 = "[" + (String) DictManager.simbolToToken().get(tipoErr2.substring(1,2)) + "]";

                }

                this.erroresSemanticos.add(new Error("Semantico", arbol.getHijos().get(1).getLexema().getFila(), arbol.getHijos().get(1).getLexema().getColumna(), "El operador '" + operador +"', no permite los tipos ('" + tipoErr1 + "', '" + tipoErr2 + "')"));

            }

        } else if ( arbol.getData().equals("A1") ) {


            String tipo1 = this.determinarTipoExpresion(arbol.getHijos().get(0), tablaScope);
            String tipo2 = this.determinarTipoExpresion(arbol.getHijos().get(2), tablaScope);
            String operador = arbol.getHijos().get(1).getLexema().getValor();

            boolean realInt = (tipo1.equals("q") && tipo2.equals("r")) || (tipo1.equals("r") && tipo2.equals("q"));

            if ((!tipo1.equals("error") && !tipo2.equals("error")) && (tipo1.equals(tipo2) || (tipo1.equals("[any]") && tipo2.length() > 1) || (tipo2.equals("[any]") && tipo1.length() > 1) || realInt )){

                if (!tipo1.equals("s") && !tipo1.equals("y")) {

                    if (operador.equals("+")) {

                        if ( realInt ) {

                            tipo = "r";

                        } else if (!tipo1.equals("[any]")) {

                            tipo = tipo1;
                        }else if (!tipo2.equals("[any]")) {

                            tipo = tipo2;

                        }else {

                            tipo = "[any]";

                        }

                    } else {

                        if ( realInt ) {

                            tipo = "r";

                        } else if ( (tipo1.length() == 1) && (tipo2.length() == 1) && tipo1.equals(tipo2) && (tipo1.equals("q") || tipo1.equals("r")) ) {

                            tipo = tipo1;

                        } else {

                            String tipoErr1 = tipo1;
                            String tipoErr2 = tipo2;

                            if (tipoErr1.length() == 1) {

                                tipoErr1 = (String) DictManager.simbolToToken().get(tipoErr1);

                            } else if (!tipoErr1.equals("[any]")){

                                tipoErr1 = "[" + (String) DictManager.simbolToToken().get(tipoErr1.substring(1,2)) + "]";

                            }

                            if (tipoErr2.length() == 1) {

                                tipoErr2 = (String) DictManager.simbolToToken().get(tipoErr2);

                            } else if (!tipoErr2.equals("[any]")){

                                tipoErr2 = "[" + (String) DictManager.simbolToToken().get(tipoErr2.substring(1,2)) + "]";

                            }

                            this.erroresSemanticos.add(new Error("Semantico", arbol.getHijos().get(1).getLexema().getFila(), arbol.getHijos().get(1).getLexema().getColumna(), "El operador '" + operador + "', no acepta la combinacion de tipos ('" + tipoErr1 + "', '" + tipoErr2 + "')"));

                        }

                    }

                } else {

                    String tipoErr1 = tipo1;
                    String tipoErr2 = tipo2;

                    if (tipoErr1.length() == 1) {

                        tipoErr1 = (String) DictManager.simbolToToken().get(tipoErr1);

                    } else if (!tipoErr1.equals("[any]")){

                        tipoErr1 = "[" + (String) DictManager.simbolToToken().get(tipoErr1.substring(1,2)) + "]";

                    }

                    if (tipoErr2.length() == 1) {

                        tipoErr2 = (String) DictManager.simbolToToken().get(tipoErr2);

                    } else if (!tipoErr2.equals("[any]")){

                        tipoErr2 = "[" + (String) DictManager.simbolToToken().get(tipoErr2.substring(1,2)) + "]";

                    }

                    this.erroresSemanticos.add(new Error("Semantico", arbol.getHijos().get(1).getLexema().getFila(), arbol.getHijos().get(1).getLexema().getColumna(), "El operador '" + operador + "', no acepta la combinacion de tipos ('" + tipoErr1 + "', '" + tipoErr2 + "')"));

                }

            } else if (!tipo1.equals("error") && !tipo2.equals("error")) {

                String tipoErr1 = tipo1;
                String tipoErr2 = tipo2;

                if (tipoErr1.length() == 1) {

                    tipoErr1 = (String) DictManager.simbolToToken().get(tipoErr1);

                } else if (!tipoErr1.equals("[any]")){

                    tipoErr1 = "[" + (String) DictManager.simbolToToken().get(tipoErr1.substring(1,2)) + "]";

                }

                if (tipoErr2.length() == 1) {

                    tipoErr2 = (String) DictManager.simbolToToken().get(tipoErr2);

                } else if (!tipoErr2.equals("[any]")){

                    tipoErr2 = "[" + (String) DictManager.simbolToToken().get(tipoErr2.substring(1,2)) + "]";

                }

                this.erroresSemanticos.add(new Error("Semantico", arbol.getHijos().get(1).getLexema().getFila(), arbol.getHijos().get(1).getLexema().getColumna(), "El operador '" + operador + "', no acepta la combinacion de tipos ('" + tipoErr1 + "', '" + tipoErr2 + "')"));

            }

        }

        //System.out.println("Tipo de : " + arbol.getData() + " es " + tipo);

        return tipo;

    }

    private ElementoTabla buscarIdentificador(TablaScopes tabla, String identificador) {

        for (ElementoTabla elementoTabla : tabla.getElementosDelScope()) {

            if (identificador.equals(elementoTabla.getIdentificador())) {

                return elementoTabla;

            }

        }

        return null;

    }

    private TablaScopes buscarSubscope(TablaScopes tablaPadre, int id) {

        for (TablaScopes subScope : tablaPadre.getSubScopes()) {

            if (subScope.getIdentificador() == id) {

                return subScope;

            }

        }

        return  null;

    }

    private TablaScopes generarTablas(ArbolSintactico arbol, int padre, int id) {

        TablaScopes tabla = new TablaScopes(id, padre);

        if (arbol.getData().equals("Y") || arbol.getData().equals("B1")) {

            ElementoTabla declaracion = this.analizarDeclaracion(arbol);

            if (arbol.getData().equals("B1")) {

                declaracion.setInicializado(true);

            }

            tabla.agregarElemento(declaracion);


        }  else if (arbol.getData().equals("K") || arbol.getData().equals("I") ||arbol.getData().equals("J") ||arbol.getData().equals("V")) {

            if (arbol.getData().equals("K")) {

                Lexema lex = arbol.getHijos().get(1).getLexema();
                String tipo = arbol.getHijos().get(0).getHijos().get(0).getData();

                ElementoTabla metodo = new ElementoTabla(lex, tipo, true, arbol);
                metodo.setInicializado(true);

                tabla.agregarElemento(metodo);

            }

            //System.out.println("Generando tabla: " + arbol.getHijos().get(0).getId() + " con padre: " + tabla.getIdentificador());

            TablaScopes nuevaTabla =   this.generarTablas(arbol.getHijos().get(0), tabla.getIdentificador(), arbol.getId());

            for (int i = 1; i < arbol.getHijos().size(); i++) {

                nuevaTabla = joinTablas(nuevaTabla, (this.generarTablas(arbol.getHijos().get(i), tabla.getIdentificador(), arbol.getId())));

            }

            tabla.agregarSubScope(nuevaTabla);

        } else {

          for (ArbolSintactico hijo: arbol.getHijos()){

              this.joinTablas(tabla, generarTablas(hijo, -1, id));

          }

        }

        return tabla;

    }

    private void actualizarConPadres(TablaScopes tabla){

        //System.out.println("Actualizando: " + tabla.getIdentificador());

        //System.out.println("Numero de elementos actual: " + tabla.getElementosDelScope().size());

        //System.out.println("Buscando padre: " + tabla.getPadre());

        TablaScopes padre = this.getTablaById(tabla.getPadre(), this.tabla);

        if (padre != null) {

            //System.out.println("Numero de elementos del padre: " + padre.getElementosDelScope().size());

            List<ElementoTabla> elements= new ArrayList<>(padre.getElementosDelScope());

            elements = joinElementos(elements, tabla.getElementosDelScope());

            tabla.setElementosDelScope(elements);


        }

        //System.out.println("Nuevo Numero de elementos: " + tabla.getElementosDelScope().size());

        //System.out.println("Ordenando actualizacion en hijos: " + tabla.getSubScopes().size());

        for (TablaScopes subScope: tabla.getSubScopes()){

            this.actualizarConPadres(subScope);

        }

    }

    private TablaScopes getTablaById(int id, TablaScopes tabla) {

        if (id == -1) {

            return null;

        } else if (id == tabla.getIdentificador()) {

            return tabla;

        } else {

            TablaScopes aux = null;

            for (TablaScopes hijo : tabla.getSubScopes()) {

                TablaScopes aux1 = this.getTablaById(id, hijo);
                if (aux1 != null) {

                    aux = aux1;

                }

            }

            return aux;

        }

    }

    public void printTablas() {

        this.printTablasR(this.tabla);

    }

    private void printTablasR(TablaScopes tabla){

        System.out.println("----------------- Tabla: " + tabla.getIdentificador() + "--------------------");
        System.out.println("Elementos: ");
        for(ElementoTabla elemento: tabla.getElementosDelScope()) {

            System.out.println("tipo: " + elemento.getTipo() + ", identificador: " + elemento.getIdentificador() + ", fila: " + elemento.getLexema().getFila() + ", columna: " + elemento.getLexema().getColumna() + ", metodo: " + elemento.isMetodo() + ", inicializado: " + elemento.isInicializado());

        }

        System.out.println("SubScopes: ");

        for(TablaScopes subscope: tabla.getSubScopes()) {

            System.out.println("- " + subscope.getIdentificador());

        }

        for(TablaScopes subscope: tabla.getSubScopes()) {

            this.printTablasR(subscope);

        }

    }

    private TablaScopes joinTablas(TablaScopes tabla1, TablaScopes tabla2) {

        if (tabla1 != null) {

            tabla1.setElementosDelScope(this.joinElementos(tabla1.getElementosDelScope(), tabla2.getElementosDelScope()));

            for (TablaScopes subscope: tabla2.getSubScopes()) {
                tabla1.agregarSubScope(subscope);
            }


            return tabla1;

        } else {

            return tabla2;

        }

    }

    private boolean identificadorEnUso(String id, List<ElementoTabla> elementos) {

        //System.out.println("Verificando para " + id + " en una tabla de elemntos: " + tabla.getElementosDelScope().size());

        for (ElementoTabla elemento : elementos) {

            if (elemento.getIdentificador().equals(id)) {

                return true;

            }

        }

        return false;
    }

    private List<ElementoTabla> joinElementos(List<ElementoTabla> elementos1, List<ElementoTabla> elementos2) {

        for (ElementoTabla elemento: elementos2){

            if (!this.identificadorEnUso(elemento.getIdentificador(), elementos1)) {

                elementos1.add(elemento);

            } else {

                this.erroresSemanticos.add(new Error("Semantico", elemento.getLexema().getFila(), elemento.getLexema().getColumna(), "El identificador '" + elemento.getIdentificador() + "' ya esta en uso."));

            }

        }

        return elementos1;

    }

    private String determinarTipo(ArbolSintactico arbol) {

        String tipo = "";

        if (arbol.getHijos().size() > 1) {

            for (ArbolSintactico hijo : arbol.getHijos()) {

                if (!hijo.getLexema().getSimbolo().equals("m") && !hijo.getLexema().getSimbolo().equals("n")) {

                    tipo = "[" + determinarTipo(hijo) + "]";

                }

            }

        } else {

            tipo = arbol.getHijos().get(0).getLexema().getSimbolo();

        }

        return tipo;

    }

    private ElementoTabla analizarDeclaracion (ArbolSintactico declaracion) {

        String tipo = "";

        Lexema lex = null;

        boolean f = false;

        for (ArbolSintactico hijo: declaracion.getHijos()) {

            if (hijo.getData().equals("U")) {
                //U

                tipo = determinarTipo(hijo);


            } else if (hijo.getData().equals("H1")){
                //H1

                ArbolSintactico subhijo = hijo.getHijos().get(0);

                if (subhijo.getData().equals("a")) {

                    lex = subhijo.getLexema();

                }else {
                    f = true;
                    for (ArbolSintactico nieto : subhijo.getHijos()) {

                       if (nieto.getData().equals("a")) {

                           lex = nieto.getLexema();

                       }

                    }

                }

            } else {

                // a

                lex = hijo.getLexema();

            }

        }

        ElementoTabla elementoTabla = new ElementoTabla(lex, tipo, false);
        elementoTabla.setInicializado(f);

        return elementoTabla;


    }

    public List<Error> getErroresSemanticos() {
        return erroresSemanticos;
    }

    public void setErroresSemanticos(List<Error> erroresSemanticos) {
        this.erroresSemanticos = erroresSemanticos;
    }
}
