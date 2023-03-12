import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App extends JFrame implements ActionListener {
    private JTextArea txt;
    private JButton escanear;
    private JPanel listado;
    private String alfabeto = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String numeros = "0123456789";
    private String rels = "><!=";
    private Font fuente = new Font("Tahoma", 15, 15);

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
        String Id = "";
        String OpRel = "";
        String numero = "";
        String token = "";
        boolean IdLeido = false;
        int length = linea.length();
        linea = linea.trim();
        if (length <= 1)
            mensajeError("No se permiten cadenas tan cortas");
        for (int i = 0; i < length; i++) {
            char actual = linea.charAt(i);
            if (actual == ' ' || i == length - 1) {
                Pattern patron;
                Matcher matcher;
                if (Id.length() == 0) {
                    patron = Pattern.compile("[a-zA-Z]+");
                    matcher = patron.matcher(token);
                    if (!matcher.matches())
                        mensajeError("Error solo se permiten letras en el ID");
                    Id = token;
                    token = "";
                    continue;

                }
                if (OpRel.length() == 0) {
                    patron = Pattern.compile("(==|!=|<|<=|>|>=){1}");
                    matcher = patron.matcher(token);
                    if (!matcher.matches())
                        mensajeError("Error, no es operador valido");
                    OpRel = token;
                    token = "";
                    continue;
                }
                if (i == length - 1)
                    token += actual;
                if (numero.length() == 0) {
                    patron = Pattern.compile("[0-9]+\\.?[0-9]*");
                    matcher = patron.matcher(token);
                    if (!matcher.matches())
                        mensajeError("Error, no es numero valido");
                    numero = token;
                    token = "";
                    continue;
                }
            }
            token += actual;
        }
        JLabel[] labels = {
                new JLabel(Id),
                new JLabel("ID"),
                new JLabel(OpRel),
                new JLabel("Operador relacional"),
                new JLabel(numero),
                new JLabel("numero")
        };
        for (JLabel label : labels) {
            label.setMinimumSize(new Dimension(100, 25));
            label.setMaximumSize(new Dimension(100, 25));
            label.setPreferredSize(new Dimension(100, 25));
            label.setFont(fuente);
            listado.add(label);
        }
    }

    private void mensajeError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje);
        listado.removeAll();
        revalidate();
        throw new IllegalArgumentException(mensaje);
    }
}