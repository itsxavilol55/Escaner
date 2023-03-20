import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App extends JFrame implements ActionListener {
    private JTextArea txt;
    private JButton escanear;
    private JPanel listado;
    private JLabel error;
    private Font fuente = new Font("Tahoma", 16, 15);
    private Hashtable<String, String> tokens = new Hashtable<String, String>();
    private String[] palabrasReservadas = {
            "if", "else", "while", "for",
            "switch", "case", "break", "default",
            "return", "int" };
    private String[] mensajes = {
            "No hay error",
            "Error solo se permiten letras en el ID",
            "Error, no es operador valido",
            "Error, no es numero valido" };
    private int LineaCont = 0;
    private int numeroError = 0;
    private boolean isError = false;

    public static void main(String[] args) throws Exception {
        new App();
    }

    public App() {
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        JLabel label = new JLabel("Listado de Tokens");
        error = new JLabel("");
        listado = new JPanel();
        txt = new JTextArea();
        escanear = new JButton("Escanear");
        JScrollPane scrollListado = new JScrollPane(listado);
        {
            escanear.setBounds(160, 580, 120, 40);
            label.setBounds(500, 25, 200, 10);
            scrollListado.setBounds(500, 50, 480, 500);
            txt.setBounds(20, 50, 450, 500);
            error.setBounds(20, 650, 2600, 30);
        }
        txt.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txt.setFont(fuente);
        error.setFont(fuente);
        error.setForeground(Color.red);
        listado.setLayout(new GridLayout(0, 2));
        listado.setAlignmentY(Component.TOP_ALIGNMENT);
        escanear.addActionListener(this);
        add(txt);
        add(escanear);
        add(label);
        add(scrollListado);
        add(error);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == escanear) {
            tokens.clear();
            LineaCont = 0;
            listado.removeAll();
            error.setText("");
            String[] lineas = txt.getText().split("\\r?\\n");
            for (String linea : lineas) {
                validarToken(linea);
                LineaCont++;
                if (isError)
                    return;
            }
            error.setText("");
            revalidate();
        }
    }

    private void validarToken(String linea) {
        String token = "";
        linea = linea.trim();
        linea += " ";
        int length = linea.length();
        if (length <= 1) {
            mensajeError("No se permiten cadenas tan cortas");
            return;
        }
        for (int i = 0; i < length; i++) {
            char actual = linea.charAt(i);
            if (actual == ' ') {
                String tokenAux = token;
                token = "";
                System.out.println("-" + tokenAux + "-");
                if (validaID(tokenAux))
                    ;
                else if (validaOprel(tokenAux))
                    ;
                else if (validaNumero(tokenAux))
                    ;
                if (numeroError != 0) {
                    mensajeError(mensajes[numeroError]);
                    numeroError = 0;
                }
                continue;
            }
            token += actual;
        }
        ArrayList<JLabel> labels = new ArrayList<JLabel>();
        for (Entry<String, String> entry : tokens.entrySet()) {
            labels.add(new JLabel(entry.getKey()));
            labels.add(new JLabel(entry.getValue()));
        }
        for (JLabel label : labels) {
            label.setMinimumSize(new Dimension(100, 25));
            label.setMaximumSize(new Dimension(100, 25));
            label.setPreferredSize(new Dimension(100, 25));
            label.setFont(fuente);
            listado.add(label);
        }
    }

    private boolean validaID(String token) {
        System.out.println("valida ID " + token);
        Pattern patron = Pattern.compile("[a-zA-Z]+");
        Matcher matcher = patron.matcher(token);
        if (!matcher.matches()) {
            numeroError = (numeroError == 0) ? 1 : numeroError;
            return false;
        }
        numeroError = 0;
        token = token.toLowerCase();
        if (Arrays.asList(palabrasReservadas).contains(token))
            tokens.put(token, "Palabra reservada");
        else
            tokens.put(token, "Identificador");
        token = "";
        return true;
    }

    private boolean validaOprel(String token) {
        System.out.println("valida opRel " + token);
        Pattern patron = Pattern.compile("(==|!=|<|<=|>|>=){1}");
        Matcher matcher = patron.matcher(token);
        if (!matcher.matches()) {
            numeroError = (numeroError == 0) ? 2 : numeroError;
            return false;
        }
        numeroError = 0;
        tokens.put(token, "Operador Relacional");
        token = "";
        return true;
    }

    private boolean validaNumero(String token) {
        System.out.println("valida numero " + token);
        Pattern patron = Pattern.compile("[0-9]+\\.?[0-9]*");
        Matcher matcher = patron.matcher(token);
        if (!matcher.matches()) {
            numeroError = (numeroError == 0) ? 3 : numeroError;
            return false;
        }
        numeroError = 0;
        tokens.put(token, "Numero");
        token = "";
        return true;
    }

    private void mensajeError(String mensaje) {
        isError = true;
        error.setText(mensaje + " -Linea: " + LineaCont);
        listado.removeAll();
        revalidate();
        validate();
    }
}