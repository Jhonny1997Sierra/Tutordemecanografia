import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class TutorMecanografia extends JFrame implements ActionListener {
    private JTextArea textArea;
    private JButton[] botones;
    private JLabel pangramaLabel;
    private String[] pangramas;
    private int indicePangramaActual = -1;
    private String pangramaActual;
    private int correctas = 0;
    private int incorrectas = 0;
    private String teclasDificiles = "";
    private Map<Character, Integer> teclasDificilesMap = new HashMap<>();
    private boolean bloquearEntrada = false;
    private boolean borrarPangrama = true;
    private final String qwertyLayout = "QWERTYUIOPASDFGHJKLZXCVBNM ";

    public TutorMecanografia() {
        setTitle("Tutor de Mecanografía");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea(5, 20);
        textArea.setEditable(true);
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) avanzarPangrama();
                else if (bloquearEntrada) e.consume();
                else if (borrarPangrama) {
                    textArea.setText("");
                    borrarPangrama = false;
                } else {
                    char caracterIngresado = e.getKeyChar();
                    char caracterPangrama = pangramaActual.charAt(textArea.getText().length());
                    if (caracterIngresado != caracterPangrama) {
                        incorrectas++;
                        char caracter = e.getKeyChar();
                        if (!teclasDificiles.contains(String.valueOf(caracter))) {
                            teclasDificiles += caracter + " ";
                            if (teclasDificilesMap.containsKey(caracter)) {
                                int count = teclasDificilesMap.get(caracter);
                                teclasDificilesMap.put(caracter, count + 1);
                            } else {
                                teclasDificilesMap.put(caracter, 1);
                            }
                        }
                    }
                }
            }
        });
        add(new JScrollPane(textArea), BorderLayout.NORTH);

        JPanel panelTeclado = new JPanel(new GridLayout(4, 11));
        botones = new JButton[27];

        for (int i = 0; i < 27; i++) {
            if (i < 26) botones[i] = new JButton(String.valueOf(qwertyLayout.charAt(i)));
            else botones[i] = new JButton("SPACE");
            botones[i].addActionListener(this);
            panelTeclado.add(botones[i]);
        }

        pangramaLabel = new JLabel("");
        add(pangramaLabel, BorderLayout.CENTER);

        cargarPangramasDesdeArchivo("pangramas.txt");
        avanzarPangrama();

        add(panelTeclado, BorderLayout.SOUTH);
        requestFocusInWindow();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TutorMecanografia());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton botonPresionado = (JButton) e.getSource();
        String letra = botonPresionado.getText();
        if (letra.equals("SPACE")) textArea.append(" ");
        else textArea.append(letra);
        checkPangrama();
    }

    private void cargarPangramasDesdeArchivo(String nombreArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            ArrayList<String> pangramasList = new ArrayList<>();
            String linea;
            while ((linea = br.readLine()) != null) pangramasList.add(linea);
            pangramas = pangramasList.toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se pudo cargar el archivo de pangramas.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void avanzarPangrama() {
        if (indicePangramaActual < pangramas.length - 1) {
            indicePangramaActual++;
            pangramaActual = pangramas[indicePangramaActual];
            pangramaLabel.setText(pangramaActual);
            textArea.setText("");
            bloquearEntrada = false;
            borrarPangrama = true;
            correctas++;
        } else {
            mostrarInformeDificultades();
            System.exit(0);
        }
    }

    private void checkPangrama() {
        String textoIngresado = textArea.getText().trim();
        if (pangramaActual.equals(textoIngresado)) {
            bloquearEntrada = true;
            avanzarPangrama();
        }
    }

    private void mostrarInformeDificultades() {
        StringBuilder informe = new StringBuilder("Pulsaciones Correctas: " + correctas +
                "\nPulsaciones Incorrectas: " + incorrectas +
                "\nTeclas Dificiles: " + teclasDificiles);

        informe.append("\n\nFrecuencia de Teclas Difíciles:\n");
        for (char tecla : teclasDificilesMap.keySet()) {
            informe.append(tecla).append(": ").append(teclasDificilesMap.get(tecla)).append(" veces\n");
        }

        JOptionPane.showMessageDialog(this, informe.toString(),
                "Informe de Dificultades", JOptionPane.INFORMATION_MESSAGE);
    }
}
