public class Semantico {
    public static void Semantico() {
        for (Declaracion declaracion : Parser.declaraciones) {
            System.out.println(declaracion.identificador);
            System.out.println(declaracion.tipoDato);
            System.out.println(declaracion.valor);
        }
    }
}
