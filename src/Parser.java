import java.awt.Color;
import java.util.ArrayList;

public class Parser {
    private static int cont;
    private static ArrayList<String[]> lista;
    private static boolean valido;
    private static boolean esDeclaracion = false;
    static ArrayList<Declaracion> declaraciones;

    public static void Parsear() {
        cont = 0;
        valido = false;
        lista = Scanner.tokens;
        declaraciones = new ArrayList<>();
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
        App.error.setText("El programa es Correcto Sintaticamente");
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
        esDeclaracion = false;
        if (!(asig() || declarar() || impr() || si() || ciclo()))
            return false;
        cont++;
        if (!lista.get(cont)[1].equals("right_curly_bracket"))
            return instr();
        else
            return true;
    }

    private static boolean declarar() {
        if (!lista.get(cont)[1].equals("Tipo de Dato"))
            return false;
        esDeclaracion = true;
        declaraciones.add(new Declaracion());
        declaraciones.get(declaraciones.size() - 1).tipoDato = lista.get(cont)[0];
        cont++;
        return asig();
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
        declaraciones.add(new Declaracion());
        cont++;
        if (!lista.get(cont)[1].equals("left_parenthesis"))
            return false;
        cont++;
        if (lista.get(cont)[1].equals("Identificador")) {
            declaraciones.get(declaraciones.size() - 1).valor = lista.get(cont)[0];
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
        declaraciones.get(declaraciones.size() - 1).valor = lista.get(cont)[0];
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
        if (!esDeclaracion)
            declaraciones.add(new Declaracion());
        declaraciones.get(declaraciones.size() - 1).identificador = lista.get(cont)[0];
        cont++;
        if (!lista.get(cont)[0].equals("=")) {
            cont--;
            return false;
        }
        cont++;
        if (!(calc() || cadena() || leer()))
            return false;
        return true;
    }

    private static boolean cadena() {
        if (!lista.get(cont)[0].equals("\""))
            return false;
        cont++;
        if (!lista.get(cont)[1].equals("cadena"))
            return false;
        declaraciones.get(declaraciones.size() - 1).valor = lista.get(cont)[1];
        cont++;
        if (!lista.get(cont)[0].equals("\""))
            return false;
        cont++;
        if (lista.get(cont)[0].equals(";"))
            return true;
        return false;
    }

    private static boolean calc() {
        if (!(lista.get(cont)[1].equals("Identificador") ||
                lista.get(cont)[1].equals("numero"))) {
            return false;
        }
        cont++;
        if (lista.get(cont)[0].equals(";")) {
            declaraciones.get(declaraciones.size() - 1).valor = lista.get(cont-1)[0];
            return true;
        }
        String operacion = lista.get(cont-1)[0] + " ";
        if (!(lista.get(cont)[1].equals("Operador Aritmetico"))) {
            cont--;
            return false;
        }
        operacion += lista.get(cont)[0];
        cont++;
        if (!(lista.get(cont)[1].equals("Identificador") ||
                lista.get(cont)[1].equals("numero"))) {
            cont--;
            return false;
        }
        declaraciones.get(declaraciones.size() - 1).valor = operacion + " " +lista.get(cont)[0];
        cont++;
        if (lista.get(cont)[0].equals(";"))
            return true;
        cont *= -1;
        return false;
    }

    private static boolean leer() {
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

}