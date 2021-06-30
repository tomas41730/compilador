package Model.AnalizadorSemantico;

import Model.AnalizadorLexico.Lexema;

import java.util.ArrayList;
import java.util.List;

public class TablaScopes {

    private List<ElementoTabla> elementosDelScope;

    private List<TablaScopes> subScopes;
    private int identificador;

    private int padre;

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public TablaScopes(int identificador, int padre) {

        this.elementosDelScope = new ArrayList<>();
        this.subScopes = new ArrayList<>();
        this.identificador = identificador;
        this.padre = padre;
    }

    public List<ElementoTabla> getElementosDelScope() {
        return elementosDelScope;
    }

    public List<TablaScopes> getSubScopes() {
        return subScopes;
    }

    public void agregarElemento(ElementoTabla nuevoElemento) {

        this.elementosDelScope.add(nuevoElemento);

    }

    public void agregarListaElementos(List<ElementoTabla> nuevosElementos) {

        for(ElementoTabla elemento: nuevosElementos) {

            this.elementosDelScope.add(elemento);


        }

    }

    public void agregarSubScope(TablaScopes tablaNueva) {

        this.subScopes.add(tablaNueva);

    }

    public void agregarListaSubscope(List<TablaScopes> scopes) {

        for (TablaScopes scope: scopes) {

            this.subScopes.add(scope);

        }

    }

    public int getPadre() {
        return padre;
    }

    public void setPadre(int padre) {
        this.padre = padre;
    }

    public void setElementosDelScope(List<ElementoTabla> elementosDelScope) {
        this.elementosDelScope = elementosDelScope;
    }

    public void setSubScopes(List<TablaScopes> subScopes) {
        this.subScopes = subScopes;
    }
}
