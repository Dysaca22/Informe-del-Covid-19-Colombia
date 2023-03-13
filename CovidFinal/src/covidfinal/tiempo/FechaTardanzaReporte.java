package covidfinal.tiempo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FechaTardanzaReporte {

    private final DateFormat fechaHora;
    private String[] personas;

    private int[] diasTardanza;
    private int numPersonas;

    private int[] numCasos;
    private int numCasosTotal;

    private double[] porcentraje;
    private double[] porcentrajeAcumulado;

    public FechaTardanzaReporte(String[] personas) {

        fechaHora = new SimpleDateFormat("yyyy-MM-dd");
        this.personas = personas;

        diasTardanza = new int[100000];
        numPersonas = 0;

        numCasos = new int[26];
        numCasosTotal = 0;

        porcentraje = new double[26];
        porcentrajeAcumulado = new double[26];
        obtenerDatos();
    }

    private void obtenerDatos() {

        for (int i = 0; i < personas.length; i++) {

            String[] infoPersona = personas[i].split(",");

            if (infoPersona.length == 17) {

                String[] estado = infoPersona[6].split(":");
                if (!estado[1].equals("\"N/A\"")) {

                    String[] fechaWeb = infoPersona[16].split(":");
                    String[] fechaFis = infoPersona[12].split(":");

                    Date fechaTemporal1 = null, fechaTemporal2 = null;
                    try {
                        if (!fechaFis[1].equals("\"-   -\"") && !fechaFis[1].equals("\"Asintomático\"")) {
                            fechaTemporal1 = fechaHora.parse(fechaFis[1].substring(1, 11));
                            fechaTemporal2 = fechaHora.parse(fechaWeb[1].substring(1, 11));

                            diasTardanza[numPersonas] = (int) ((fechaTemporal2.getTime() - fechaTemporal1.getTime()) / 86400000);
                            numPersonas++;
                        }
                    } catch (ParseException ex) {
                        ex.getStackTrace();
                    }
                }
            }
        }
        numeroCasos();
    }

    private void numeroCasos() {

        for (int i = 1; i < 26; i++) {
            int temp = 0;
            for (int j = 0; j < numPersonas; j++) {
                if (i == 25) {
                    if (diasTardanza[j] >= i) {
                        temp++;
                        numCasosTotal++;
                    }
                } else {
                    if (diasTardanza[j] == i) {
                        temp++;
                        numCasosTotal++;
                    }
                }
            }
            numCasos[i] = temp;
        }
        porcentrajes();
    }

    private void porcentrajes() {

        for (int i = 1; i < 26; i++) {

            porcentraje[i] = (numCasos[i] * Math.pow(numCasosTotal, -1));
            if (i == 1) {
                porcentrajeAcumulado[1] = porcentraje[1];
            } else {
                porcentrajeAcumulado[i] = porcentrajeAcumulado[i - 1] + porcentraje[i];
            }
        }
    }

    public double[] getPorcentajeAcumulado() {
        return porcentrajeAcumulado;
    }
}
