import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App extends JFrame implements ActionListener {
    private JMenuItem Nuevo, Abrir, Guardar;
    private JTextArea txt;
    private JScrollPane scrollListado;
    private JButton escanear;
    private JPanel listado;
    private JLabel error;
    private Font fuente = new Font("Tahoma", 16, 15);
    private Hashtable<String, String> tokens = new Hashtable<String, String>();
    private String[] palabrasReservadas = {
            "Program", "if", "else", "while", "for",
            "switch", "case", "break", "default",
            "return", "int" };
    private File archivo;
    private int LineaCont = 1;
    private boolean isError = false;

    public static void main(String[] args) throws Exception {
        new App();
    }

    public App() {
        interfaz();
        eventos();
    }

    private void interfaz() {
        setTitle("Supra");
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        JLabel label = new JLabel("Listado de Tokens");
        JMenuBar BarraPrincipal = new JMenuBar();
        JMenu MenuArchivo = new JMenu("Archivo");
        error = new JLabel("");
        listado = new JPanel();
        txt = new JTextArea();
        scrollListado = new JScrollPane(listado);
        escanear = new JButton("Escanear");// Agregar botones y menu
        Nuevo = new JMenuItem("Nuevo");
        Abrir = new JMenuItem("Abrir");
        Guardar = new JMenuItem("Guardar");
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
        MenuArchivo.add(Nuevo);
        MenuArchivo.add(Abrir);
        MenuArchivo.add(Guardar);
        BarraPrincipal.add(MenuArchivo);
        setJMenuBar(BarraPrincipal);
        add(txt);
        add(escanear);
        add(label);
        add(scrollListado);
        add(error);
        setVisible(true);
    }

    private void eventos() {
        escanear.addActionListener(this);
        Nuevo.addActionListener(this);
        Abrir.addActionListener(this);
        Guardar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == escanear) {
            escanear();
            return;
        }
        if (e.getSource() == Nuevo) {
            txt.setText("");
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

    private void escanear() {
        LineaCont = 1;
        isError = false;
        tokens.clear();
        listado.removeAll();
        revalidate();
        validate();
        listado.update(listado.getGraphics());
        error.setText("");
        String[] lineas = txt.getText().split("\\r?\\n");
        for (String linea : lineas) {
            validarToken(linea);
            LineaCont++;
            if (isError)
                return;
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
        error.setText("");
        revalidate();
        validate();
        listado.update(listado.getGraphics());
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
                if (validaID(tokenAux))
                    ;
                else if (validaOprel(tokenAux))
                    ;
                else if (validaNumero(tokenAux))
                    ;
                else if (validaComentario(tokenAux))
                    return;
                else {
                    mensajeError("No es un token valido: '" + tokenAux + "'");
                    return;
                }
                continue;
            }
            token += actual;
        }
    }

    private boolean validaComentario(String token) {
        Pattern patron = Pattern.compile("\\/\\/.*");
        Matcher matcher = patron.matcher(token);
        if (!matcher.matches())
            return false;
        token = "";
        return true;
    }

    private boolean validaID(String token) {
        Pattern patron = Pattern.compile("[a-zA-Z]+");
        Matcher matcher = patron.matcher(token);
        if (!matcher.matches())
            return false;
        token = token.toLowerCase();
        if (Arrays.asList(palabrasReservadas).contains(token))
            tokens.put(token, "Palabra reservada");
        else
            tokens.put(token, "Identificador");
        token = "";
        return true;
    }

    private boolean validaOprel(String token) {
        Pattern patron = Pattern.compile("(==|!=|<|<=|>|>=){1}");
        Matcher matcher = patron.matcher(token);
        if (!matcher.matches())
            return false;
        tokens.put(token, "Operador Relacional");
        token = "";
        return true;
    }

    private boolean validaNumero(String token) {
        Pattern patron = Pattern.compile("-?[0-9]+\\.?[0-9]*");
        Matcher matcher = patron.matcher(token);
        if (!matcher.matches())
            return false;
        tokens.put(token, "Numero");
        token = "";
        return true;
    }

    private void mensajeError(String mensaje) {
        isError = true;
        error.setText(mensaje + " -Linea: " + LineaCont);
        tokens.clear();
        listado.removeAll();
        revalidate();
        validate();
        listado.update(listado.getGraphics());
    }
}