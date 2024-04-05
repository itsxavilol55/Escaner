import java.util.Hashtable;
public class Intermedio {
    static StringBuilder puntoData;
    static StringBuilder puntoCode;
    private static Hashtable<String, String> tiposDato;
    static void intermedio(){
        tiposDato = new Hashtable<String, String>();
        tiposDato.put("int", "dw");
        tiposDato.put("double", "dd");
        tiposDato.put("boolean", "db");
        tiposDato.put("string", "db");
        puntoData = new StringBuilder("\t.Data\n");
        puntoCode = new StringBuilder("\t.Code\n");
        App.txtAsm.setText("");
        for (Declaracion declaracion : Parser.declaraciones){
            if(declaracion.tipoDato != null)
                definirVariable(declaracion);
            else if(declaracion.identificador != null)
                definirAsignacion(declaracion);
            System.out.println(declaracion.tipoDato);
            System.out.println(declaracion.identificador);
            System.out.println(declaracion.valor);
        }
        App.txtAsm.setText(puntoData.append(puntoCode).toString());
    }
    private static void definirAsignacion(Declaracion declaracion) {
        if(declaracion.valor.matches("^-?\\d+$") || declaracion.valor.matches("^-?\\d+\\.\\d+$")){
            puntoCode.append("mov\t");
            puntoCode.append(declaracion.identificador +",\t");
            puntoCode.append(declaracion.valor + "\n");
        }
    }
    private static void definirVariable(Declaracion declaracion) {
        puntoData.append(declaracion.identificador +"\t");
        puntoData.append(tiposDato.get(declaracion.tipoDato) + "\t");
        if(declaracion.valor.equals("true"))
            puntoData.append(1 + "\n");
        else if(declaracion.valor.equals("false"))
            puntoData.append(0 + "\n");
        else if(declaracion.valor.equals("cadena"))
            puntoData.append("'"+declaracion.cadena+"$'" + "\n");
        else
            puntoData.append(declaracion.valor + "\n");
    }
}
