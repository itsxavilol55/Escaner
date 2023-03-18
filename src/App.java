import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App extends JFrame implements ActionListener {
    private JTextArea txt;
    private JButton escanear;
    private JPanel listado;
    private Font fuente = new Font("Tahoma", 15, 15);
    private Hashtable<String, String> tokens = new Hashtable<String, String>();

    public static void main(String[] args) throws Exception {
        new App();
    }

    public App() {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        JLabel label = new JLabel("Listado de Tokens");
        listado = new JPanel();
        txt = new JTextArea();
        escanear = new JButton("Escanear");
        JScrollPane scrollListado = new JScrollPane(listado);
        {
            escanear.setBounds(160, 480, 120, 40);
            label.setBounds(400, 25, 200, 10);
            scrollListado.setBounds(400, 50, 380, 400);
            txt.setBounds(20, 50, 380, 400);
        }
        txt.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txt.setFont(fuente);
        listado.setLayout(new GridLayout(0, 2));
        listado.setAlignmentY(Component.TOP_ALIGNMENT);
        escanear.addActionListener(this);
        add(txt);
        add(escanear);
        add(label);
        add(scrollListado);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == escanear) {
            listado.removeAll();
            String[] lineas = txt.getText().split("\\r?\\n");
            for (String linea : lineas)
                validarToken(linea);
            revalidate();
        }
    }

    private void validarToken(String linea) {
        String token = "";
        linea = linea.trim();
        linea += " ";
        int length = linea.length();
        if (length <= 1)
            mensajeError("No se permiten cadenas tan cortas");
        for (int i = 0; i < length; i++) {
            char actual = linea.charAt(i);
            if (actual == ' ') {
                String tokenAux = token;
                token = "";
                if (validaID(tokenAux))
                    continue;
                if (validaOprel(tokenAux))
                    continue;
                if (validaNumero(tokenAux))
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
        Pattern patron = Pattern.compile("[a-zA-Z]+");
        Matcher matcher = patron.matcher(token);
        if (!matcher.matches()) {
            // mensajeError("Error solo se permiten letras en el ID");
            return false;
        }
        tokens.put(token, "Identificador");
        token = "";
        return true;
    }

    private boolean validaOprel(String token) {
        Pattern patron = Pattern.compile("(==|!=|<|<=|>|>=){1}");
        Matcher matcher = patron.matcher(token);
        if (!matcher.matches())
            // mensajeError("Error, no es operador valido");
            return false;
        tokens.put(token, "Operador Relacional");
        token = "";
        return true;
    }

    private boolean validaNumero(String token) {
        Pattern patron = Pattern.compile("[0-9]+\\.?[0-9]*");
        Matcher matcher = patron.matcher(token);
        if (!matcher.matches())
            // mensajeError("Error, no es numero valido");
            return false;
        tokens.put(token, "Numero");
        token = "";
        return true;
    }

    private void mensajeError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje);
        listado.removeAll();
        revalidate();
        validate();
        throw new IllegalArgumentException(mensaje);
    }
}