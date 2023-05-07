import java.util.ArrayList;

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
        program(lista.get(0)[0]);
        if (!valido) {
            System.out.println("Eso esta mal");
            return;
        }
        System.out.println("Eso esta bien");
    }

    private static void program(String token) {
        if (!token.equals("program"))
            return;
        cont++;
        if (!lista.get(cont)[1].equals("Identificador"))
            return;
        cont++;
        bloque(lista.get(cont)[1]);
    }

    private static void bloque(String token) {
        if (!token.equals("left_curly_bracket"))
            return;
        cont++;
        if (!asig(lista.get(cont)[1]))
            return;
        cont++;
        if (!lista.get(cont)[1].equals("right_curly_bracket"))
            return;
        valido = true;
    }

    private static boolean asig(String token) {
        if (!token.equals("Identificador"))
            return false;
        cont++;
        if (!lista.get(cont)[0].equals("="))
            return false;
        cont++;
        if (!calc(lista.get(cont)[1]))
            return false;
        return true;
    }

    private static boolean calc(String string) {
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