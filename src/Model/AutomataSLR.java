package Model;

import java.util.ArrayList;
import java.util.List;

public class AutomataSLR {

    private List<EstadoSLR> estados;
    private List<GLCRule> reglas;

    public AutomataSLR (GLCRule reglaInicial, List<GLCRule> reglas) {

        this.estados = new ArrayList<>();
        this.reglas = reglas;

        this.estados.add( new EstadoSLR(0, reglaInicial, this.reglas));

        this.printAutomataSlr();

    }

    public void printAutomataSlr () {

        for (EstadoSLR estado : this.estados) {

            System.out.println("Estado : " + estado.getId() + ", Reglas: " + estado.printReglas());

        }

    }

}
