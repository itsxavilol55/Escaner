import java.util.Hashtable;
import java.util.Stack;

public class Intermedio {
    static StringBuilder puntoData;
    static StringBuilder puntoCode;
    static int contadorSegmento = 0;
    private static Hashtable<String, String> tiposDato,operacionesT;
    private static Hashtable<Integer, String> hexadecimal;

    static void intermedio() {
        contadorSegmento = 0;
        tiposDato = new Hashtable<String, String>();
        operacionesT = new Hashtable<String, String>();
        hexadecimal = new Hashtable<Integer, String>();
        tiposDato.put("int", "dw");
        tiposDato.put("double", "dd");
        tiposDato.put("boolean", "db");
        tiposDato.put("string", "db");
        operacionesT.put("+", "add");
        operacionesT.put("-", "sub");
        operacionesT.put("*", "mul");
        operacionesT.put("/", "div");
        operacionesT.put("^", "exp");
        operacionesT.put("%", "mod");
        hexadecimal.put(0, "0");
        hexadecimal.put(1, "1");
        hexadecimal.put(2, "2");
        hexadecimal.put(3, "3");
        hexadecimal.put(4, "4");
        hexadecimal.put(5, "5");
        hexadecimal.put(6, "6");
        hexadecimal.put(7, "7");
        hexadecimal.put(8, "8");
        hexadecimal.put(9, "9");
        hexadecimal.put(10, "a");
        hexadecimal.put(11, "b");
        hexadecimal.put(12, "c");
        hexadecimal.put(13, "d");
        hexadecimal.put(14, "e");
        hexadecimal.put(15, "f");
        puntoData = new StringBuilder("\t.Data\n");
        puntoCode = new StringBuilder("\t.Code\n");
        App.txtAsm.setText("");
        for (Declaracion declaracion : Parser.declaraciones) {
            if (declaracion.tipoDato != null)
                definirVariable(declaracion);
            else if (declaracion.identificador != null){
                definirAsignacion(declaracion);
                puntoCode.append("\n");
            }
        }
        App.txtAsm.setText(puntoData.append(puntoCode).toString());
    }

    private static void definirAsignacion(Declaracion declaracion) {
        puntoCode.append("mov\t");
        if (declaracion.valor.matches("^-?\\d+$") || declaracion.valor.matches("^-?\\d+\\.\\d+$")) {
            puntoCode.append(declaracion.identificador + ",\t");
            puntoCode.append(declaracion.valor + "\n");
            return;
        }
        if (declaracion.valor.equals("true")) {
            puntoCode.append(declaracion.identificador + ",\t");
            puntoCode.append(1 + "\n");
            return;
        }
        if (declaracion.valor.equals("false")){
            puntoCode.append(declaracion.identificador + ",\t");
            puntoCode.append(0 + "\n");
            return;
        }
        if(declaracion.valor.equals("cadena")){
            puntoCode.append("eax,\t");
            puntoCode.append("'"+declaracion.cadena + "'$\n");
            puntoCode.append("mov\t");
            puntoCode.append(declaracion.identificador + ",\t");
            puntoCode.append("eax\n");
            return;
        }
        if (!declaracion.valor.matches(".*\\s+.*")){
            puntoCode.append("ax,\t");
            puntoCode.append(declaracion.valor + "\n");
            puntoCode.append("mov\t");
            puntoCode.append(declaracion.identificador + ",\t");
            puntoCode.append("ax\n");
            return;
        }
        String[] operaciones = declaracion.valor.split(" ");
        puntoCode.append("ax,\t");
        puntoCode.append(operaciones[0] + "\n");
        puntoCode.append(operacionesT.get(operaciones[1])+"\t");
        puntoCode.append("ax,\t");
        puntoCode.append(operaciones[2] + "\n");
        puntoCode.append("mov\t");
        puntoCode.append(declaracion.identificador + ",\t");
        puntoCode.append("ax\n");
    }

    private static void definirVariable(Declaracion declaracion) {
        puntoData.append("0000:"+decAhex(contadorSegmento) + "\t");
        puntoData.append(declaracion.identificador + "\t");
        puntoData.append(tiposDato.get(declaracion.tipoDato) + "\t");
        if (declaracion.valor.equals("true")){
            puntoData.append(1 + "\n");
            contadorSegmento++;
        }
        else if (declaracion.valor.equals("false")){
            puntoData.append(0 + "\n");
            contadorSegmento++;
        }
        else if (declaracion.valor.equals("cadena")){
            puntoData.append("'" + declaracion.cadena.trim() + "$'" + "\n");
            contadorSegmento += declaracion.cadena.length();
        }
        else{
            puntoData.append(declaracion.valor + "\n");
            contadorSegmento+=2;
        }
    }

    private static String decAhex(int contadorSegmento) {
        Stack<String> pila = new Stack<>();
        String hex ="";
        int divisor = contadorSegmento;
        do {
            pila.push(hexadecimal.get(divisor % 16));
            divisor = (int) Math.floor(divisor / 16);
        } while (divisor != 0);
        int total = pila.size();
        for (int i = 0; i < total; i++) {
            hex += pila.pop();
        }
        return hex;
    }
}
