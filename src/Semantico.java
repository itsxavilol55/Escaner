import java.awt.Color;
import java.util.ArrayList;

public class Semantico {
    static ArrayList<String> IDs;

    public static void Semantico() {
        IDs = new ArrayList<>();
        for (Declaracion declaracion : Parser.declaraciones) {// analiza declaracion por declaracion
            if (declaracion.tipoDato != null)
            // si tipo de dato es diferente de null significa que es una declaracion de
            // variable(int a = 0;)
            {
                if (IDs.contains(declaracion.identificador)) {
                    App.error.setForeground(Color.red);
                    App.error
                            .setText("Error semantico, esta variable: " + declaracion.identificador + " ya se declaro");
                    return;
                }
                IDs.add(declaracion.identificador);
                if (declaracion.tipoDato.equals("string") && declaracion.valor.equals("cadena")) {
                    mensajeCorrecto();
                    continue;
                }
                if (declaracion.tipoDato.equals("int") && declaracion.valor.matches("^-?\\d+$")) {
                    mensajeCorrecto();
                    continue;
                }
                if (declaracion.tipoDato.equals("double") && declaracion.valor.matches("^-?\\d+\\.\\d+$")) {
                    mensajeCorrecto();
                    continue;
                }
                if (declaracion.tipoDato.equals("boolean") && declaracion.valor.matches("false|true")) {
                    mensajeCorrecto();
                    continue;
                }
                App.error.setForeground(Color.red);
                App.error.setText("Error semantico, la variable " + declaracion.identificador
                        + " no es el del tipo correcto");
                return;

            }
            if (declaracion.identificador == null) {
                if (declaracion.valor != null && validaOperacion(declaracion.valor)) {
                    return;
                } else {
                    mensajeCorrecto();
                    continue;
                }
            }
            if (variableDeclarada(declaracion.identificador))
                return;
            if (declaracion.valor != null && validaOperacion(declaracion.valor))
                return;
        }
        mensajeCorrecto();
    }

    private static boolean validaOperacion(String operacion) {
        if (operacion.matches(".*[a-zA-Z].*") && !operacion.equals("cadena")) {
            String[] operaciones = operacion.split(" ");
            for (String valor : operaciones) {
                if (valor.matches(".*[a-zA-Z].*"))
                    if (variableDeclarada(valor))
                        return true;
            }
        }
        return false;
    }

    private static void mensajeCorrecto() {
        App.error.setForeground(Color.green);
        App.error.setText("Programa correcto semanticamente");
    }

    private static boolean variableDeclarada(String id) {
        if (!IDs.contains(id)) {
            App.error.setForeground(Color.red);
            App.error.setText("Error semantico, esta variable: " + id + " no se ha declaro");
            return true;
        }
        return false;
    }
}
