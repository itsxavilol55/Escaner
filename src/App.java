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
    static JTextArea txt,txtAsm;
    private JScrollPane scrollListado,scrolltxt,scrolltxtAsm;
    static JPanel listado;
    static JLabel errorSint, errorSema;
    private static JFrame panel;
    private Font fuente = new Font("Tahoma", 16, 19);
    private Font fuente2 = new Font("Tahoma", 16, 17);
    private File archivo;
    private JMenu Escaner, parser,semantico,intermedio;

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
        setSize(1430, 1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        JLabel label = new JLabel("Listado de Tokens");
        JLabel labelSint = new JLabel("Analizador Sintatico");
        JLabel labelSema = new JLabel("Analizador Semantico");
        JMenuBar BarraPrincipal = new JMenuBar();
        JMenu MenuArchivo = new JMenu("Archivo");
        Escaner = new JMenu("Escaner");
        parser = new JMenu("Parser");
        semantico = new JMenu("Semantico");
        intermedio = new JMenu("Intermedio");
        errorSint = new JLabel("");
        errorSema = new JLabel("");
        listado = new JPanel();
        txt = new JTextArea();
        txtAsm = new JTextArea();
        scrollListado = new JScrollPane(listado);
        scrolltxt = new JScrollPane(txt);
        scrolltxtAsm = new JScrollPane(txtAsm);
        Nuevo = new JMenuItem("Nuevo"); // Agregar botones y menu
        Abrir = new JMenuItem("Abrir");
        Guardar = new JMenuItem("Guardar");
        {
            label.setBounds(530, 10, 300, 10);
            labelSint.setBounds(1000, 10, 200, 30);
            labelSema.setBounds(1000, 70, 200, 30);
            scrollListado.setBounds(530, 25, 450, 400);
            scrolltxt.setBounds(20, 25, 500, 400);
            scrolltxtAsm.setBounds(20, 450, 650, 450);
            errorSint.setBounds(1000, 40, 500, 30);
            errorSema.setBounds(1000, 110, 500, 30);
        }
        txt.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txt.setFont(fuente);
        txtAsm.setFont(fuente);
        txtAsm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txtAsm.setEditable(false);
        labelSint.setFont(fuente);
        labelSema.setFont(fuente);
        Escaner.setFont(fuente2);
        parser.setFont(fuente2);
        semantico.setFont(fuente2);
        intermedio.setFont(fuente2);
        MenuArchivo.setFont(fuente2);
        errorSint.setOpaque(true);
        errorSint.setFont(fuente2);
        errorSint.setBackground(Color.white);
        errorSint.setForeground(Color.red);
        errorSema.setOpaque(true);
        errorSema.setFont(fuente2);
        errorSema.setBackground(Color.white);
        errorSema.setForeground(Color.red);
        listado.setLayout(new GridLayout(0, 2));
        listado.setAlignmentY(Component.TOP_ALIGNMENT);
        MenuArchivo.add(Nuevo);
        MenuArchivo.add(Abrir);
        MenuArchivo.add(Guardar);
        BarraPrincipal.add(MenuArchivo);
        BarraPrincipal.add(Escaner);
        BarraPrincipal.add(parser);
        BarraPrincipal.add(semantico);
        BarraPrincipal.add(intermedio);
        setJMenuBar(BarraPrincipal);
        add(scrolltxt);
        add(scrolltxtAsm);
        add(label);
        add(errorSema);
        add(labelSint);
        add(labelSema);
        add(scrollListado);
        add(errorSint);
        setVisible(true);
    }

    private void eventos() {
        Nuevo.addActionListener(this);
        Abrir.addActionListener(this);
        Guardar.addActionListener(this);
        Escaner.addMouseListener(this);
        parser.addMouseListener(this);
        semantico.addMouseListener(this);
        intermedio.addMouseListener(this);
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
        if (e.getSource() == semantico) {
            Semantico.Semantico();
            return;
        }
        if (e.getSource() == intermedio) {
            Intermedio.intermedio();
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