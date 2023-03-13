package covidfinal.cargadorRecursos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CargadorDatosTexto {

    public static String leerArchivoTexto(final String ruta) {

        String contenido = "";

        InputStream entradaBytes = ClassLoader.class.getResourceAsStream(ruta);
        BufferedReader lector = new BufferedReader(new InputStreamReader(entradaBytes));

        String linea;

        try {
            while ((linea = lector.readLine()) != null) {
                contenido += linea;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (entradaBytes != null) {
                    entradaBytes.close();
                }
                if (lector != null) {
                    lector.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return contenido;
    }
}
