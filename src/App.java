import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class App extends JFrame implements ActionListener, MouseListener {
    private JMenuItem Nuevo, Abrir, Guardar;
    static JTextArea txt;
    private JScrollPane scrollListado;
    static JPanel listado;
    static JLabel error;
    private static JFrame panel;
    private Font fuente = new Font("Tahoma", 16, 17);
    private File archivo;
    private JMenu Escaner, parser;

    public static void main(String[] args) throws Exception {
        new App();
    }

    public App() {
        interfaz();
        eventos();
    }

    private void interfaz() {
        panel = this;
        setTitle("Supra");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        JLabel label = new JLabel("Listado de Tokens");
        JMenuBar BarraPrincipal = new JMenuBar();
        JMenu MenuArchivo = new JMenu("Archivo");
        Escaner = new JMenu("Escaner");
        parser = new JMenu("Parser");
        error = new JLabel("");
        listado = new JPanel();
        txt = new JTextArea();
        scrollListado = new JScrollPane(listado);
        Nuevo = new JMenuItem("Nuevo"); // Agregar botones y menu
        Abrir = new JMenuItem("Abrir");
        Guardar = new JMenuItem("Guardar");
        {
            label.setBounds(600, 25, 200, 10);
            scrollListado.setBounds(600, 50, 570, 600);
            txt.setBounds(20, 50, 550, 600);
            error.setBounds(20, 650, 2600, 30);
        }
        txt.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txt.setFont(fuente);
        error.setFont(fuente);
        error.setForeground(Color.red);
        listado.setLayout(new GridLayout(0, 2));
        listado.setAlignmentY(Component.TOP_ALIGNMENT);
        MenuArchivo.add(Nuevo);
        MenuArchivo.add(Abrir);
        MenuArchivo.add(Guardar);
        BarraPrincipal.add(MenuArchivo);
        BarraPrincipal.add(Escaner);
        BarraPrincipal.add(parser);
        setJMenuBar(BarraPrincipal);
        add(txt);
        add(label);
        add(scrollListado);
        add(error);
        setVisible(true);
    }

    private void eventos() {
        Nuevo.addActionListener(this);
        Abrir.addActionListener(this);
        Guardar.addActionListener(this);
        Escaner.addMouseListener(this);
        parser.addMouseListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Nuevo) {
            txt.setText("");
            Limpiar();
            return;
        }
        if (e.getSource() == Abrir) {
            Abrir();
            return;
        }
        if (e.getSource() == Guardar) {
            Guardar();
            return;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == Escaner) {
            Scanner.escanear();
            return;
        }
        if (e.getSource() == parser) {
            Parser.Parsear();
            return;
        }
    }

    private void Abrir() {
        JFileChooser Abrir = new JFileChooser();
        Abrir.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
        Abrir.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter Filtro = new FileNameExtensionFilter(".txt", "txt");
        Abrir.setFileFilter(Filtro);
        int Seleccionar = Abrir.showOpenDialog(null);
        if (Seleccionar == JFileChooser.APPROVE_OPTION) {
            archivo = Abrir.getSelectedFile();
            try (FileReader fr = new FileReader(archivo)) {
                String cadena = "";
                int valor = fr.read();
                while (valor != -1) {
                    cadena = cadena + (char) valor;
                    valor = fr.read();
                }
                txt.setText(cadena);
            } catch (IOException ex) {

            }
        }
    }

    private void Guardar() {
        JFileChooser guardar = new JFileChooser();
        guardar.setFileSelectionMode(JFileChooser.FILES_ONLY);
        guardar.setFileFilter(new FileNameExtensionFilter(".txt", "txt"));
        int returnVal = guardar.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = guardar.getSelectedFile();
            if (!file.getName().endsWith(".txt")) {
                file = new File(file.getPath() + ".txt");
            }
            try {

                FileWriter writer = new FileWriter(file);
                writer.write(txt.getText());
                writer.close();
                JOptionPane.showMessageDialog(null, "Archivo guardado correctamente");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error. No se pudo guardar el archivo");
                ex.printStackTrace();
            }
        }
    }

    static void Limpiar() {
        listado.removeAll();
        valida();
        listado.update(listado.getGraphics());
    }

    static void valida() {
        panel.revalidate();
        panel.validate();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}