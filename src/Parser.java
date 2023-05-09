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
        program();
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
        bloque();
    }

    private static void bloque() {
        if (!lista.get(cont)[1].equals("left_curly_bracket"))
            return;
        cont++;
        if (!instr())
            return;
        if (!lista.get(cont)[1].equals("right_curly_bracket"))
            return;
        valido = true;
    }

    private static boolean instr() {
        if (!asig())
            return false;
        cont++;
        if (!lista.get(cont)[1].equals("right_curly_bracket"))
            return instr();
        else
            return true;
    }

    private static boolean asig() {
        if (!lista.get(cont)[1].equals("Identificador"))
            return false;
        cont++;
        if (!lista.get(cont)[0].equals("="))
            return false;
        cont++;
        if (!calc())
            return false;
        return true;
    }

    private static boolean calc() {
        if (!(lista.get(cont)[1].equals("Identificador") ||
                lista.get(cont)[1].equals("numero")))
            return false;
        cont++;
        if ((lista.get(cont)[0].equals(";")))
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
        if ((lista.get(cont)[0].equals(";")))
            return true;
        return false;
    }
}