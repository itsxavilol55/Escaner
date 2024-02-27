import java.awt.Color;
import java.util.ArrayList;

public class Semantico {
    public static void Semantico() {
        ArrayList<String> IDs = new ArrayList<>();
        for (Declaracion declaracion : Parser.declaraciones) {// analiza declaracion por declaracion
            if (declaracion.tipoDato != null)
            // si tipo de dato es diferente de null significa que es una declaracion de variable(int a = 0;)
            {
                if (IDs.contains(declaracion.identificador)) {
                    App.error.setForeground(Color.red);
                    App.error
                            .setText("Error semantico, esta variable: " + declaracion.identificador + " ya se declaro");
                    return;
                }
                IDs.add(declaracion.identificador);
            }
            if (!IDs.contains(declaracion.identificador)) {//a = 0;
                App.error.setForeground(Color.red);
                App.error.setText("Error semantico, esta variable: " + declaracion.identificador + " no se ha declaro");
                return;
            }
            if (declaracion.valor.matches(".*[a-zA-Z].*") && !IDs.contains(declaracion.valor)) {//int a = b;
                App.error.setForeground(Color.red);
                App.error.setText("Error semantico, esta variable: " + declaracion.valor + " no se ha declaro");
                return;
            }
        }
        App.error.setForeground(Color.green);
        App.error.setText("Programa correcto semanticamente");  
    }
}
