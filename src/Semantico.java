import java.awt.Color;
import java.util.Hashtable;

public class Semantico {
    static Hashtable<String, String> IDs;

    public static void Semantico() {
        IDs = new Hashtable<>();
        for (Declaracion declaracion : Parser.declaraciones) {// analiza declaracion por declaracion
            if (declaracion.tipoDato != null)
            // si tipo de dato es diferente de null significa que es una declaracion de
            // variable(int a = 0;)
            {
                if (IDs.containsKey(declaracion.identificador)) {
                    App.error.setForeground(Color.red);
                    App.error
                            .setText("Error semantico, esta variable: " + declaracion.identificador + " ya se declaro");
                    return;
                }
                IDs.put(declaracion.identificador, declaracion.tipoDato);
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
                if (!declaracion.valor.equals("cadena") && !declaracion.valor.matches("false|true")) {
                    if (declaracion.tipoDato.equals(IDs.get(declaracion.valor))) {
                        mensajeCorrecto();
                        continue;
                    }
                    if(!validaOperacion(declaracion.valor, declaracion)){
                        mensajeCorrecto();
                        continue;
                    }
                }
                App.error.setForeground(Color.red);
                App.error.setText("Error semantico, la variable " + declaracion.identificador
                        + " no es el del tipo correcto");
                return;

            }
            if (declaracion.identificador == null) {
                if (declaracion.valor != null && validaOperacion(declaracion.valor, declaracion)) {
                    return;
                } else {
                    mensajeCorrecto();
                    continue;
                }
            }
            if (variableDeclarada(declaracion.identificador))
                return;
            if (declaracion.valor != null && validaOperacion(declaracion.valor, declaracion))
                return;
        }
        mensajeCorrecto();
    }

    private static boolean validaOperacion(String operacion, Declaracion declaracion) {
        if (operacion.matches(".*[a-zA-Z].*") && !operacion.equals("cadena")) {
            String[] operaciones = operacion.split(" ");
            for (String valor : operaciones) {
                if (valor.matches(".*[a-zA-Z].*")) {
                    if (variableDeclarada(valor))
                        return true;
                    if (declaracion.identificador != null && IDs.get(valor).equals(IDs.get(declaracion.identificador))) {
                        mensajeCorrecto();
                    }
                    else{
                        App.error.setForeground(Color.red);
                        App.error.setText("Error semantico, la variable " + declaracion.identificador
                        + " no es el del tipo correcto");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static void mensajeCorrecto() {
        App.error.setForeground(Color.green);
        App.error.setText("Programa correcto semanticamente");
    }

    private static boolean variableDeclarada(String id) {
        if (!IDs.containsKey(id)) {
            App.error.setForeground(Color.red);
            App.error.setText("Error semantico, esta variable: " + id + " no se ha declaro");
            return true;
        }
        return false;
    }
}
