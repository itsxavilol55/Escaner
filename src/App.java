import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class App extends JFrame {
    private JTextArea txt;
    private JButton escanear;

    public static void main(String[] args) throws Exception {
        new App();
    }

    public App() {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        txt = new JTextArea();
        txt.setBounds(20, 50, 380, 400);
        txt.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txt.setFont(new Font("Consola", 10, 16));
        add(txt);
        setVisible(true);
    }
}
