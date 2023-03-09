import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class App extends JFrame {
    private JTextArea txt;
    private JButton escanear;
    private JPanel listado;

    public static void main(String[] args) throws Exception {
        new App();
    }

    public App() {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        JLabel label = new JLabel("Listado de tokens");
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
        txt.setFont(new Font("Consola", 10, 15));
        listado.setLayout(new FlowLayout(1));
        add(txt);
        add(escanear);
        add(label);
        add(listado);
        setVisible(true);
    }
}
