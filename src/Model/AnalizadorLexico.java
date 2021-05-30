package Model;

import java.util.*;

public class AnalizadorLexico
{
    private Dictionary tablaPalabrasReservadas;
    private DiagramaTrancisiones dt;
    private final List<Lexema> listaLexemas;

    public AnalizadorLexico()
    {
        this.tablaPalabrasReservadas = inicializarPR();
        this.listaLexemas = null;
    }

    private Dictionary inicializarPR() // Inicializar palabras reservadas
    {

        tablaPalabrasReservadas = new Hashtable();
        //1. Value Type Keywords
        tablaPalabrasReservadas.put("bool", "VALUE_TYPE");
        tablaPalabrasReservadas.put("double", "VALUE_TYPE");
        tablaPalabrasReservadas.put("long", "VALUE_TYPE");
        tablaPalabrasReservadas.put("unit", "VALUE_TYPE");
        tablaPalabrasReservadas.put("byte", "VALUE_TYPE");
        tablaPalabrasReservadas.put("enum", "VALUE_TYPE");
        tablaPalabrasReservadas.put("sbyte", "VALUE_TYPE");
        tablaPalabrasReservadas.put("ulong", "VALUE_TYPE");
        tablaPalabrasReservadas.put("char", "VALUE_TYPE");
        tablaPalabrasReservadas.put("float", "VALUE_TYPE");
        tablaPalabrasReservadas.put("short", "VALUE_TYPE");
        tablaPalabrasReservadas.put("ushort", "VALUE_TYPE");
        tablaPalabrasReservadas.put("decimal", "VALUE_TYPE");
        tablaPalabrasReservadas.put("int", "VALUE_TYPE");
        tablaPalabrasReservadas.put("struct", "VALUE_TYPE");

        //2. Reference Type Keywords
        tablaPalabrasReservadas.put("class", "REFERENCE_TYPE");
        tablaPalabrasReservadas.put("delegate", "REFERENCE_TYPE");
        tablaPalabrasReservadas.put("interface", "REFERENCE_TYPE");
        tablaPalabrasReservadas.put("object", "REFERENCE_TYPE");
        tablaPalabrasReservadas.put("string", "REFERENCE_TYPE");
        tablaPalabrasReservadas.put("void", "REFERENCE_TYPE");

        //3. Modifiers Keywords
        tablaPalabrasReservadas.put("public", "MODIFIERS");
        tablaPalabrasReservadas.put("private", "MODIFIERS");
        tablaPalabrasReservadas.put("internal", "MODIFIERS");
        tablaPalabrasReservadas.put("protected", "MODIFIERS");
        tablaPalabrasReservadas.put("abstract", "MODIFIERS");
        tablaPalabrasReservadas.put("const", "MODIFIERS");
        tablaPalabrasReservadas.put("event", "MODIFIERS");
        tablaPalabrasReservadas.put("extern", "MODIFIERS");
        tablaPalabrasReservadas.put("new", "MODIFIERS");
        tablaPalabrasReservadas.put("override", "MODIFIERS");
        tablaPalabrasReservadas.put("partial", "MODIFIERS");
        tablaPalabrasReservadas.put("readonly", "MODIFIERS");
        tablaPalabrasReservadas.put("sealed", "MODIFIERS");
        tablaPalabrasReservadas.put("static", "MODIFIERS");
        tablaPalabrasReservadas.put("unsafe", "MODIFIERS");
        tablaPalabrasReservadas.put("virtual", "MODIFIERS");
        tablaPalabrasReservadas.put("volatile", "MODIFIERS");

        //4. Statements Keywords
        tablaPalabrasReservadas.put("if", "STATEMENT");
        tablaPalabrasReservadas.put("else", "STATEMENT");
        tablaPalabrasReservadas.put("switch", "STATEMENT");
        tablaPalabrasReservadas.put("do", "STATEMENT");
        tablaPalabrasReservadas.put("for", "STATEMENT");
        tablaPalabrasReservadas.put("foreach", "STATEMENT");
        tablaPalabrasReservadas.put("in", "STATEMENT");
        tablaPalabrasReservadas.put("while", "STATEMENT");
        tablaPalabrasReservadas.put("break", "STATEMENT");
        tablaPalabrasReservadas.put("continue", "STATEMENT");
        tablaPalabrasReservadas.put("goto", "STATEMENT");
        tablaPalabrasReservadas.put("return", "STATEMENT");
        tablaPalabrasReservadas.put("throw", "STATEMENT");
        tablaPalabrasReservadas.put("try", "STATEMENT");
        tablaPalabrasReservadas.put("catch", "STATEMENT");
        tablaPalabrasReservadas.put("finally", "STATEMENT");
        tablaPalabrasReservadas.put("checked", "STATEMENT");
        tablaPalabrasReservadas.put("unchecked", "STATEMENT");

        //5. Method Parameters Keywords
        tablaPalabrasReservadas.put("params", "MET_PARAM");
        tablaPalabrasReservadas.put("in", "MET_PARAM");
        tablaPalabrasReservadas.put("ref", "MET_PARAM");
        tablaPalabrasReservadas.put("out", "MET_PARAM");

        //6. Namespace Keywords
        tablaPalabrasReservadas.put("namespace", "NAMESPACE");
        tablaPalabrasReservadas.put("using", "NAMESPACE");
        tablaPalabrasReservadas.put("extern", "NAMESPACE");


        //7. Operator Keywords
        tablaPalabrasReservadas.put("as", "OPERATOR");
        tablaPalabrasReservadas.put("is", "OPERATOR");
        tablaPalabrasReservadas.put("new", "OPERATOR");
        tablaPalabrasReservadas.put("sizeof", "OPERATOR");
        tablaPalabrasReservadas.put("typeof", "OPERATOR");
        tablaPalabrasReservadas.put("true", "OPERATOR");
        tablaPalabrasReservadas.put("false", "OPERATOR");
        tablaPalabrasReservadas.put("stackalloc", "OPERATOR");

        //8. Conversion Keywords
        tablaPalabrasReservadas.put("explicit", "CONVERSION");
        tablaPalabrasReservadas.put("implicit", "CONVERSION");
        tablaPalabrasReservadas.put("operator", "CONVERSION");

        //9. Access Keywords
        tablaPalabrasReservadas.put("base", "ACCESS");
        tablaPalabrasReservadas.put("this", "ACCESS");

        //10. Literal Keywords
        tablaPalabrasReservadas.put("null", "LITERAL");
        tablaPalabrasReservadas.put("default", "LITERAL");

        //11. Contextual Keywords
        tablaPalabrasReservadas.put("add", "CONTEXTUAL");
        tablaPalabrasReservadas.put("equals", "CONTEXTUAL");
        tablaPalabrasReservadas.put("nameof", "CONTEXTUAL");
        tablaPalabrasReservadas.put("value", "CONTEXTUAL");
        tablaPalabrasReservadas.put("alias", "CONTEXTUAL");
        tablaPalabrasReservadas.put("from", "CONTEXTUAL");
        tablaPalabrasReservadas.put("on", "CONTEXTUAL");
        tablaPalabrasReservadas.put("var", "CONTEXTUAL");
        tablaPalabrasReservadas.put("ascending", "CONTEXTUAL");
        tablaPalabrasReservadas.put("get", "CONTEXTUAL");
        tablaPalabrasReservadas.put("orderby", "CONTEXTUAL");
        tablaPalabrasReservadas.put("when", "CONTEXTUAL");
        tablaPalabrasReservadas.put("async", "CONTEXTUAL");
        tablaPalabrasReservadas.put("global", "CONTEXTUAL");
        tablaPalabrasReservadas.put("partial", "CONTEXTUAL");
        tablaPalabrasReservadas.put("where", "CONTEXTUAL");
        tablaPalabrasReservadas.put("await", "CONTEXTUAL");
        tablaPalabrasReservadas.put("group", "CONTEXTUAL");
        tablaPalabrasReservadas.put("by", "CONTEXTUAL");
        tablaPalabrasReservadas.put("into", "CONTEXTUAL");
        tablaPalabrasReservadas.put("remove", "CONTEXTUAL");
        tablaPalabrasReservadas.put("yield", "CONTEXTUAL");
        tablaPalabrasReservadas.put("descending", "CONTEXTUAL");
        tablaPalabrasReservadas.put("join", "CONTEXTUAL");
        tablaPalabrasReservadas.put("select", "CONTEXTUAL");
        tablaPalabrasReservadas.put("dynamic", "CONTEXTUAL");
        tablaPalabrasReservadas.put("let", "CONTEXTUAL");
        tablaPalabrasReservadas.put("set", "CONTEXTUAL");






        return null;
    }

    public void AnalizarCodigo(List<String> lineasCodigo) //Para cada palabra en cada linea analizar con metodos
    {                                                     //del diagrama de transiciones e insertar en lista lexemas

    }
    public void imprimirLexemas()
    {
        for (Lexema lexema:listaLexemas)
        {
            System.out.println(lexema.toString());
        }
    }
}
