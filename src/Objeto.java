import java.util.HashMap;
import java.util.Stack;
public class Objeto {
    static HashMap<String, String> hexadecimalToBinary;
    public static void objeto() {
        hexadecimalToBinary = new HashMap<>();
        hexadecimalToBinary.put("0", "0000");
        hexadecimalToBinary.put("1", "0001");
        hexadecimalToBinary.put("2", "0010");
        hexadecimalToBinary.put("3", "0011");
        hexadecimalToBinary.put("4", "0100");
        hexadecimalToBinary.put("5", "0101");
        hexadecimalToBinary.put("6", "0110");
        hexadecimalToBinary.put("7", "0111");
        hexadecimalToBinary.put("8", "1000");
        hexadecimalToBinary.put("9", "1001");
        hexadecimalToBinary.put("a", "1010");
        hexadecimalToBinary.put("b", "1011");
        hexadecimalToBinary.put("c", "1100");
        hexadecimalToBinary.put("d", "1101");
        hexadecimalToBinary.put("e", "1110");
        hexadecimalToBinary.put("f", "1111");

        String codeobj ="";
        String instrucciones [] = Intermedio.puntoCode.toString().split("\n");
        for (int i = 1; i < instrucciones.length; i++) {
            if (instrucciones[i].length()==0) 
                continue;
            String parametros []= instrucciones[i].replaceAll(",","").split("\t");
            if(parametros[0].equals("mov")){
                if(parametros[1].equals("ax") && parametros[2].matches(".*[a-zA-Z].*")){
                    codeobj += "mov ax, "+parametros[2]+ "\t=>\n\t1010 0001 " + ObtenerSegmento(parametros[2])+"\n";
                    continue;
                }
                if(parametros[1].equals("ax") && parametros[2].matches("^-?\\d+$")){
                    codeobj += "mov ax, "+parametros[2]+ "\t=>\n\t1011 1000 " + ObtenerBinaro(parametros[2])+"\n";
                    continue;
                }
                if(parametros[2].equals("ax") && parametros[1].matches(".*[a-zA-Z].*")){
                    codeobj += "mov "+parametros[1]+", ax "+ "\t=>\n\t1010 0011 " + ObtenerSegmento(parametros[1])+"\n";
                    continue;
                }
                if(parametros[1].matches(".*[a-zA-Z].*") && parametros[2].matches("^-?\\d+$")){
                    codeobj += "mov "+parametros[1]+", "+parametros[2]+ "\t=>\n\t1100 0111 0000 0110 " + 
                    ObtenerSegmento(parametros[1]) +
                    ObtenerBinaro(parametros[2]) +"\n";
                    continue;
                }
            }
            if(parametros[0].equals("add")){
                if(parametros[1].equals("ax") && parametros[2].matches(".*[a-zA-Z].*")){
                    codeobj += "add ax, "+parametros[2]+ "\t=>\n\t0000 0011 0000 0110 " + ObtenerSegmento(parametros[2])+"\n";
                    continue;
                }
                if(parametros[1].equals("ax") && parametros[2].matches("^-?\\d+$")){
                    codeobj += "add ax, "+parametros[2]+ "\t=>\n\t0000 0101 " + ObtenerBinaro(parametros[2])+"\n";
                    continue;
                }
            }
            if(parametros[0].equals("sub")){
                if(parametros[1].equals("ax") && parametros[2].matches(".*[a-zA-Z].*")){
                    codeobj += "sub ax, "+parametros[2]+ "\t=>\n\t0010 1011 0000 0110 " + ObtenerSegmento(parametros[2])+"\n";
                    continue;
                }
                if(parametros[1].equals("ax") && parametros[2].matches("^-?\\d+$")){
                    codeobj += "sub ax, "+parametros[2]+ "\t=>\n\t0010 1101 " + ObtenerBinaro(parametros[2])+"\n";
                    continue;
                }
            }
            if(parametros[0].equals("mul")){
                codeobj += "mul ax \t=>\n\t1111 0111 1110 0000 " +"\n";
                continue;
            }
            if(parametros[0].equals("div")){
                codeobj += "div ax \t=>\n\t1111 0111 1111 0000 " +"\n";
                continue;
            }
        }
        App.txtBin.setText(codeobj);
    }

    private static String ObtenerBinaro(String contadorSegmento) {
        Stack<String> pila = new Stack<>();
        String bin ="";
        int divisor = Integer.parseInt(contadorSegmento);
        do {
            pila.push(""+divisor % 2);
            divisor = (int) Math.floor(divisor / 2);
        } while (divisor != 0);
        for (int i = 0; i < 16; i++) {
            if(pila.isEmpty())
                bin = 0 + bin;
            else
                bin += pila.pop();
        }
        String partebaja = bin.substring(8);
        String partealta = bin.substring(0,8);
        return partebaja.substring(0,4) +" "+
                partebaja.substring(4) +" "+
                partealta.substring(0,4) +" "+
                partealta.substring(4);
    }

    private static String ObtenerSegmento(String variable) {
        String hexa = Intermedio.segmento.get(variable);
        String binario="";
        binario += hexadecimalToBinary.get(""+hexa.charAt(2))+" ";
        binario += hexadecimalToBinary.get(""+hexa.charAt(3))+" ";
        binario += hexadecimalToBinary.get(""+hexa.charAt(0))+" ";
        binario += hexadecimalToBinary.get(""+hexa.charAt(1))+" ";
        return binario;
    }
}
