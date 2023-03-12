import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class App extends JFrame implements ActionListener {
    private JTextArea txt;
    private JButton escanear;
    private JPanel listado;
    private String alfabeto = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ";
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
        linea = linea.trim();
        boolean esLetra = true;
        for (int i = 0; i < linea.length() - 1; i++) {
            char actual = linea.charAt(i);
            char siguiente = linea.charAt(i + 1);
            if (!alfabeto.contains("" + actual) && !numeros.contains("" + actual) && !rels.contains("" + actual))
                throw new IllegalArgumentException("Carácter no permitido: " + actual);
            else if (actual == ' ' && (!rels.contains("" + siguiente) || !numeros.contains("" + siguiente))) {
                throw new IllegalArgumentException("Error: los tokens no están en el orden correcto o faltan algunos.");
            }
            if (alfabeto.contains("" + actual) && esLetra) {
                esLetra = true;
                Id += actual;
                continue;
            } else
                esLetra = false;
            if (rels.contains("" + actual)) {
                OpRel = "" + actual;
                if (siguiente == '=') {
                    OpRel = "" + actual + siguiente;
                    i++;
                }
            }
            if (numeros.contains("" + actual)) {
                numero += actual;
                if (i == linea.length() - 2)
                    numero += siguiente;
            }
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
}
