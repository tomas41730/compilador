package Config;

import Controller.ManejoArchivos;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class DictManager {

    public static Dictionary tokenToTerminal = null;
    public static Dictionary terminalToToken = null;
    public static Dictionary noTerminalToWord = null;

    public static Dictionary simbolToToken()
    {
        if (terminalToToken == null)
        {
            terminalToToken = new Hashtable();
            terminalToToken.put("a","identificador");
            terminalToToken.put("b","Numero entero");
            terminalToToken.put("c","Numero real");
            terminalToToken.put("d","Op. Aritmetico");
            terminalToToken.put("e","Op. Logico");
            terminalToToken.put("f","igualdad");

            terminalToToken.put("g","cadena");
            terminalToToken.put("i",";");
            terminalToToken.put("j",",");
            terminalToToken.put("k","(");
            terminalToToken.put("l",")");

            terminalToToken.put("m","[");
            terminalToToken.put("n","]");
            terminalToToken.put("o","{");
            terminalToToken.put("p","}");

            terminalToToken.put("q","int");
            terminalToToken.put("r","real");
            terminalToToken.put("s","bool");
            terminalToToken.put("t","if");
            terminalToToken.put("u","else");
            terminalToToken.put("v","for");
            terminalToToken.put("w","while");
            terminalToToken.put("x","string");
            terminalToToken.put("y","void");
            terminalToToken.put("z","return");
            terminalToToken.put("a1","true");
            terminalToToken.put("b1","false");
            terminalToToken.put("eps","eps");
            terminalToToken.put("c1","error");
            terminalToToken.put("d1","end");

        }

        return terminalToToken;
    }

    public static Dictionary tokenToSimbol()
    {
        if (tokenToTerminal == null)
        {
            tokenToTerminal = new Hashtable();
            tokenToTerminal.put("identificador", "a");
            tokenToTerminal.put("Numero entero", "b");
            tokenToTerminal.put("Numero real", "c");
            tokenToTerminal.put("Op. Aritmetico", "d");
            tokenToTerminal.put("Op. Logico", "e");
            tokenToTerminal.put("igualdad", "f");
            tokenToTerminal.put("cadena", "g");

            //Delimitadores
            tokenToTerminal.put(";", "i");
            tokenToTerminal.put(",", "j");
            tokenToTerminal.put("(", "k");
            tokenToTerminal.put(")", "l");
            tokenToTerminal.put("[", "m");
            tokenToTerminal.put("]", "n");
            tokenToTerminal.put("{", "o");
            tokenToTerminal.put("}", "p");

            //Palabras Reservadas
            tokenToTerminal.put("int", "q");
            tokenToTerminal.put("real", "r");
            tokenToTerminal.put("bool", "s");
            tokenToTerminal.put("if", "t");
            tokenToTerminal.put("else", "u");
            tokenToTerminal.put("for", "v");
            tokenToTerminal.put("while", "w");
            tokenToTerminal.put("string", "x");
            tokenToTerminal.put("void", "y");
            tokenToTerminal.put("return", "z");
            tokenToTerminal.put("true", "a1");
            tokenToTerminal.put("false", "b1");
            tokenToTerminal.put("eps", "eps");
            tokenToTerminal.put("error", "c1");
            tokenToTerminal.put("end", "d1");

        }

        return tokenToTerminal;
    }

    public static Dictionary noTerminalToDescription() {

        if (noTerminalToWord == null) {
            noTerminalToWord = new Hashtable();

            noTerminalToWord.put("X", "Constante (Entero, real, booleano, string)");
            noTerminalToWord.put("A1", "Operacion Aritmetica");
            noTerminalToWord.put("Z", "Operacion");
            noTerminalToWord.put("E", "Operacion Logica");
            noTerminalToWord.put("G", "Expresion (Operacion o Constante)");
            noTerminalToWord.put("W", "Valor booleano");


        }

        return noTerminalToWord;

    }

}
