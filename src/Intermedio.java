import java.util.Hashtable;

public class Intermedio {
    static StringBuilder puntoData;
    static StringBuilder puntoCode;
    private static Hashtable<String, String> tiposDato;

    static void intermedio() {
        tiposDato = new Hashtable<String, String>();
        tiposDato.put("int", "dw");
        tiposDato.put("double", "dd");
        tiposDato.put("boolean", "db");
        tiposDato.put("string", "db");
        puntoData = new StringBuilder("\t.Data\n");
        puntoCode = new StringBuilder("\t.Code\n");
        App.txtAsm.setText("");
        for (Declaracion declaracion : Parser.declaraciones) {
            if (declaracion.tipoDato != null)
                definirVariable(declaracion);
            else if (declaracion.identificador != null)
                definirAsignacion(declaracion);
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
        for (int i = operaciones.length-1; i >= 0; i--) {
            System.out.println("------"+operaciones[i]);
        }
    }

    private static void definirVariable(Declaracion declaracion) {
        puntoData.append(declaracion.identificador + "\t");
        puntoData.append(tiposDato.get(declaracion.tipoDato) + "\t");
        if (declaracion.valor.equals("true"))
            puntoData.append(1 + "\n");
        else if (declaracion.valor.equals("false"))
            puntoData.append(0 + "\n");
        else if (declaracion.valor.equals("cadena"))
            puntoData.append("'" + declaracion.cadena + "$'" + "\n");
        else
            puntoData.append(declaracion.valor + "\n");
    }
}
