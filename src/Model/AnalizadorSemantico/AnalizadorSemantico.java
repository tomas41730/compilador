package Model.AnalizadorSemantico;

import Model.AnalizadorLexico.Lexema;
import Model.Error;
import Model.SLR.ArbolSintactico;

import javax.lang.model.element.Element;
import java.lang.reflect.GenericArrayType;
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
        this.printTablas();
    }

    private TablaScopes generarTablas(ArbolSintactico arbol, int padre, int id) {

        TablaScopes tabla = new TablaScopes(id, padre);

        if (arbol.getData().equals("Y")) {

            ElementoTabla declaracion = this.analizarDeclaracion(arbol);
            tabla.agregarElemento(declaracion);

        } else if (arbol.getData().equals("K") || arbol.getData().equals("I") ||arbol.getData().equals("J") ||arbol.getData().equals("V")) {

            if (arbol.getData().equals("K")) {

                Lexema lex = arbol.getHijos().get(1).getLexema();
                String tipo = arbol.getHijos().get(0).getHijos().get(0).getData();

                ElementoTabla metodo = new ElementoTabla(lex, tipo, true);
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

            ArbolSintactico subhijo = hijo.getHijos().get(0);

            if (hijo.getData() == "U") {
                //U

                tipo = determinarTipo(hijo);


            } else {
                //H1
                if (subhijo.getData() == "a") {

                    lex = subhijo.getLexema();

                }else {
                    f = true;
                    for (ArbolSintactico nieto : subhijo.getHijos()) {

                       if (nieto.getData() == "a") {

                           lex = nieto.getLexema();

                       }

                    }

                }

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
