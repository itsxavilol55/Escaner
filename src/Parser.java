import java.awt.Color;
import java.util.ArrayList;

public class Parser {
    private static int cont;
    private static ArrayList<String[]> lista;
    private static boolean valido;

    public static void Parsear() {
        cont = 0;
        valido = false;
        lista = Scanner.tokens;
        if (lista.size() == 0) {
            App.error.setForeground(Color.red);
            App.error.setText("No se puede hacer parser sin hacer el Escaner");
            return;
        }
        try {
            program();
        } catch (Exception e) {

        }
        if (!valido) {
            App.error.setForeground(Color.red);
            App.error.setText("El programa no es correcto");
            return;
        }
        App.error.setForeground(Color.green);
        App.error.setText("El programa es Correcto");
    }

    private static void program() {
        if (!lista.get(0)[0].equals("program"))
            return;
        cont++;
        if (!lista.get(cont)[1].equals("Identificador"))
            return;
        cont++;
        if (!bloque())
            return;
        if (cont != lista.size() - 1)
            return;
        valido = true;
    }

    private static boolean bloque() {
        if (!lista.get(cont)[1].equals("left_curly_bracket"))
            return false;
        cont++;
        if (!instr())
            return false;
        if (!lista.get(cont)[1].equals("right_curly_bracket"))
            return false;
        return true;
    }

    private static boolean instr() {
        if (!(asig() || leer() || impr() || si() || ciclo()))
            return false;
        cont++;
        if (!lista.get(cont)[1].equals("right_curly_bracket"))
            return instr();
        else
            return true;
    }

    private static boolean ciclo() {
        if (!lista.get(cont)[0].equals("for"))
            return false;
        cont++;
        if (!lista.get(cont)[1].equals("left_parenthesis"))
            return false;
        cont++;
        if (!asig())
            return false;
        cont++;
        if (!cond())
            return false;
        cont++;
        if (!lista.get(cont)[0].equals(";"))
            return false;
        cont++;
        if (!lista.get(cont)[1].equals("Identificador"))
            return false;
        cont++;
        if (!(lista.get(cont)[0].equals("++") ||
                lista.get(cont)[0].equals("--")))
            return false;
        cont++;
        if (!lista.get(cont)[1].equals("right_parenthesis"))
            return false;
        cont++;
        if (!bloque())
            return false;
        return true;
    }

    private static boolean si() {
        if (!lista.get(cont)[0].equals("if"))
            return false;
        cont++;
        if (!lista.get(cont)[1].equals("left_parenthesis"))
            return false;
        cont++;
        if (!cond())
            return false;
        cont++;
        if (!lista.get(cont)[1].equals("right_parenthesis"))
            return false;
        cont++;
        if (!bloque())
            return false;
        cont++;
        if (lista.get(cont)[0].equals("else")) {
            cont++;
            if (!bloque())
                return false;
        } else
            cont--;
        return true;
    }

    private static boolean cond() {
        if (!(lista.get(cont)[1].equals("Identificador") ||
                lista.get(cont)[1].equals("numero"))) {
            return false;
        }
        cont++;
        if (!lista.get(cont)[1].equals("Operador Relacional"))
            return false;
        cont++;
        if (!(lista.get(cont)[1].equals("Identificador") ||
                lista.get(cont)[1].equals("numero"))) {
            return false;
        }
        return true;
    }

    private static boolean impr() {
        if (!lista.get(cont)[0].equals("imprimir"))
            return false;
        cont++;
        if (!lista.get(cont)[1].equals("left_parenthesis"))
            return false;
        cont++;
        if (lista.get(cont)[1].equals("Identificador")) {
            cont++;
            if (!lista.get(cont)[1].equals("right_parenthesis"))
                return false;
            cont++;
            if (lista.get(cont)[0].equals(";"))
                return true;
            return false;
        }
        if (!lista.get(cont)[0].equals("\""))
            return false;
        cont++;
        if (!lista.get(cont)[1].equals("cadena"))
            return false;
        cont++;
        if (!lista.get(cont)[0].equals("\""))
            return false;
        cont++;
        if (lista.get(cont)[1].equals("right_parenthesis")) {
            cont++;
            if (lista.get(cont)[0].equals(";"))
                return true;
            return false;
        }
        if (!lista.get(cont)[1].equals("comma"))
            return false;
        cont++;
        if (!lista.get(cont)[1].equals("Identificador"))
            return false;
        cont++;
        if (!lista.get(cont)[1].equals("right_parenthesis"))
            return false;
        cont++;
        if (lista.get(cont)[0].equals(";"))
            return true;
        return false;
    }

    private static boolean leer() {
        if (!lista.get(cont)[1].equals("Identificador"))
            return false;
        cont++;
        if (!lista.get(cont)[0].equals("="))
            return false;
        cont++;
        if (!lista.get(cont)[0].equals("leerdato"))
            return false;
        cont++;
        if (!lista.get(cont)[1].equals("left_parenthesis"))
            return false;
        cont++;
        if (!lista.get(cont)[1].equals("right_parenthesis"))
            return false;
        cont++;
        if (lista.get(cont)[0].equals(";"))
            return true;
        return false;
    }

    private static boolean asig() {
        if (!lista.get(cont)[1].equals("Identificador"))
            return false;
        cont++;
        if (!lista.get(cont)[0].equals("=")) {
            cont--;
            return false;
        }
        cont++;
        if (!calc())
            return false;
        return true;
    }

    private static boolean calc() {
        if (!(lista.get(cont)[1].equals("Identificador") ||
                lista.get(cont)[1].equals("numero"))) {
            cont -= 2;
            return false;
        }
        cont++;
        if (lista.get(cont)[0].equals(";"))
            return true;
        if (!(lista.get(cont)[1].equals("Operador Aritmetico"))) {
            cont--;
            return false;
        }
        cont++;
        if (!(lista.get(cont)[1].equals("Identificador") ||
                lista.get(cont)[1].equals("numero"))) {
            cont--;
            return false;
        }
        cont++;
        if (lista.get(cont)[0].equals(";"))
            return true;
        cont *= -1;
        return false;
    }
}