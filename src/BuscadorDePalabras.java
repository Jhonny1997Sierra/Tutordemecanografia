import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BuscadorDePalabras {
    public static void main(String[] args) {
        // Ruta del archivo de texto a buscar
        String rutaArchivo = C:\Users\gemad\Downloads\des\Palabras clave.txt;

        // Palabras y sus respectivos puntos de probabilidad
        Map<String, Integer> palabrasClave = new HashMap<>();
        palabrasClave.put("oferta", 1);   // Ejemplo: 1 punto (poco probable)
        palabrasClave.put("urgente", 2);  // Ejemplo: 2 puntos (algo probable)
        palabrasClave.put("ganador", 3);  // Ejemplo: 3 puntos (muy probable)
        // Agrega aquí más palabras clave y sus puntos según la estimación

        int totalPuntos = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                for (Map.Entry<String, Integer> entry : palabrasClave.entrySet()) {
                    String palabraClave = entry.getKey();
                    int puntos = entry.getValue();
                    int ocurrencias = contarOcurrencias(linea, palabraClave);
                    if (ocurrencias > 0) {
                        totalPuntos += puntos * ocurrencias;
                        System.out.println("Palabra clave: " + palabraClave + ", Ocurrencias: " + ocurrencias + ", Puntos: " + (puntos * ocurrencias));
                    }
                }
            }
        } catch (IOException e) {
        }

        System.out.println("Total de puntos: " + totalPuntos);
    }

    private static int contarOcurrencias(String texto, String palabraClave) {
        int lastIndex = 0;
        int count = 0;

        while (lastIndex != -1) {
            lastIndex = texto.indexOf(palabraClave, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += palabraClave.length();
            }
        }
        return count;
    }
}
