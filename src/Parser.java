import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Parser {
    private static int cont;
    private static ArrayList<String[]> lista;
    private static boolean valido;

    public static void Parsear() {
        cont = 0;
        valido = false;
        lista = Scanner.tokens;
        if (lista.size() == 0)
            return;
        try {
            program();
        } catch (Exception e) {

        }
        if (!valido) {
            JOptionPane.showMessageDialog(null, "El Programa no es correcto");
            return;
        }
        JOptionPane.showMessageDialog(null, "El Programa es correcto");
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
        if (!(asig() || leer() || impr()))
            return false;
        cont++;
        if (!lista.get(cont)[1].equals("right_curly_bracket"))
            return instr();
        else
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