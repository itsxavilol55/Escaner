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
            }
            if (declaracion.identificador == null) {
                if (variableDeclarada(declaracion.valor)) {
                    return;
                } else {
                    mensajeCorrecto();
                    continue;
                }
            }
            if (variableDeclarada(declaracion.identificador))
                return;
            if (declaracion.valor.matches(".*[a-zA-Z].*") && !declaracion.valor.equals("cadena")) {
                String[] operacion = declaracion.valor.split(" ");
                for (String valor : operacion) {
                    if (valor.matches(".*[a-zA-Z].*"))
                        if (variableDeclarada(valor))
                            return;
                }
            }
        }
        mensajeCorrecto();
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
