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
                    App.errorSema.setForeground(Color.red);
                    App.errorSema
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
                if(!(declaracion.valor.matches("^-?\\d+$") || declaracion.valor.matches("^-?\\d+\\.\\d+$")))
                {
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
                }
                App.errorSema.setForeground(Color.red);
                App.errorSema.setText("Error semantico, la variable " + declaracion.identificador
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
        if (declaracion.valor.matches("^.*(\\-|\\+|\\*|\\/|\\^|\\%).*$") || (operacion.matches(".*[a-zA-Z].*") && !operacion.equals("cadena"))) {
            String[] operaciones = operacion.split(" ");
            int cont = 0;
            for (String valor : operaciones) {
                cont++;
                if (valor.matches(".*[a-zA-Z].*")) {
                    if (variableDeclarada(valor))
                        return true;
                    if (declaracion.identificador != null && IDs.get(valor).equals(IDs.get(declaracion.identificador))) {
                        mensajeCorrecto();
                        continue;
                    }
                    else{
                        App.errorSema.setForeground(Color.red);
                        App.errorSema.setText("Error semantico, la variable " + declaracion.identificador
                        + " no es el del tipo correcto");
                        return true;
                    }
                }
                if(valor.matches("[-+*/^%]")){
                    continue;
                }
                if(!validaNoVariables(valor,IDs.get(declaracion.identificador))){
                    if(cont==3)
                        return false;
                    else
                        continue;
                }
                else{
                    App.errorSema.setForeground(Color.red);
                    App.errorSema.setText("Error semantico, la variable " + declaracion.identificador
                        + " no es el del tipo correcto");
                    return true;
                }
            }
            return false;
        }
        if(!validaNoVariables(declaracion.valor,IDs.get(declaracion.identificador)))
            return false;
        App.errorSema.setForeground(Color.red);
        App.errorSema.setText("Error semantico, la variable " + declaracion.identificador
        + " no es el del tipo correcto");
        return true;
    }

    private static boolean validaNoVariables(String valor1, String valor2) {
        if (valor1.matches("^-?\\d+$") && valor2.equals("int")) {
            mensajeCorrecto();
            return false;
        }
        if (valor1.matches("^-?\\d+\\.\\d+$") && valor2.equals("double")) {
            mensajeCorrecto();
            return false;
        }
        if (valor1.equals("cadena") && valor2.equals("string")) {
            mensajeCorrecto();
            return false;
        }
        return true;
    }

    private static void mensajeCorrecto() {
        App.errorSema.setForeground(Color.green);
        App.errorSema.setText("Programa correcto semanticamente");
    }

    private static boolean variableDeclarada(String id) {
        if (!IDs.containsKey(id)) {
            App.errorSema.setForeground(Color.red);
            App.errorSema.setText("Error semantico, esta variable: " + id + " no se ha declaro");
            return true;
        }
        return false;
    }
}
