import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class App extends JFrame implements ActionListener {
    private JTextArea txt;
    private JButton escanear;
    private JPanel listado;

    public static void main(String[] args) throws Exception {
        new App();
    }

    public App() {
        Font fuente = new Font("Tahoma", 15, 15);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        JLabel label = new JLabel("Listado de Tokens");
        listado = new JPanel();
        txt = new JTextArea();
        escanear = new JButton("Escanear");
        {
            escanear.setBounds(160, 480, 120, 40);
            label.setBounds(400, 25, 200, 10);
            listado.setBounds(400, 50, 380, 400);
            txt.setBounds(20, 50, 380, 400);
        }
        txt.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txt.setFont(fuente);
        listado.setLayout(new BoxLayout(listado, 1));
        escanear.addActionListener(this);
        add(txt);
        add(escanear);
        add(label);
        add(listado);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == escanear) {
            String[] lineas = txt.getText().split("\\r?\\n");
            for (String linea : lineas) {
                listado.add(new JLabel(linea));
            }
            revalidate();
        }
    }
}
