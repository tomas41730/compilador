package Model;

import java.util.ArrayList;
import java.util.List;

public class SLR {

    private GLC glc;

    private GLCRule reglaInicial;

    private AutomataSLR automata;

    public SLR () {

        this.glc = new GLC();

        // generando regla inicial con el id reservado INICIAL
        this.reglaInicial= new GLCRule("INICIAL");

        List<GLCTerm> auxterms = new ArrayList<>();
        auxterms.add((GLCTerm) glc.getNoTerminales().get(glc.getInitialRule().getId()));

        this.reglaInicial.addOption(new GLCoption(auxterms, auxterms.get(0), this.reglaInicial, ".*", 0));
        //

        this.automata = new AutomataSLR(this.reglaInicial, glc.getReglasGLCporNumero());

    }

}
