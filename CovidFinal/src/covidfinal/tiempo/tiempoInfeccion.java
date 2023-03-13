package covidfinal.tiempo;

import covidfinal.Constante;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class tiempoInfeccion {

    private final DateFormat fechaHora;

    private String[] personas;

    private int contPersonas;
    private double contDias;

    private double tiempoConInfeccion;

    public tiempoInfeccion(String[] personas) {

        fechaHora = new SimpleDateFormat("yyyy-MM-dd");

        this.personas = personas;

        contPersonas = 0;
        contDias = 0;

        tiempoConInfeccion = 0;

        calcularTiempoPromedioConInfeccion();
    }

    private void calcularTiempoPromedioConInfeccion() {

        for (int i = 0; i < personas.length; i++) {

            String[] infoPersona = personas[i].split(",");

            if (infoPersona.length == 17) {

                String[] fechaNot = infoPersona[2].split(":");
                String[] estado = infoPersona[6].split(":");
                String[] fechaFis = infoPersona[12].split(":");
                String[] fechaMuerte = infoPersona[13].split(":");
                String[] fechaRecu = infoPersona[15].split(":");

                Date fechaFIS = null, FechaMuerte = null, fechaRecuperacion = null;
                try {
                    if (!fechaFis[1].equals("\"-   -\"") && !fechaFis[1].equals("\"Asintomático\"") && !estado[1].equals("\"N/A\"")) {
                        fechaFIS = fechaHora.parse(fechaFis[1].substring(1, 11));
                    } else {
                        fechaFIS = fechaHora.parse(fechaNot[1].substring(1, 11));
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(fechaFIS);
                        calendar.add(Calendar.DAY_OF_YEAR, -Constante.TIEMPOEXTRAASINTOMATICOS);
                        fechaFIS = calendar.getTime();
                    }
                    if (estado[1].equals("\"Fallecido\"") && !fechaMuerte[1].equals("\"-   -\"")) {
                        FechaMuerte = fechaHora.parse(fechaMuerte[1].substring(1, 11));
                        contDias += ((FechaMuerte.getTime() - fechaFIS.getTime()) / 86400000);
                        contPersonas++;
                    } else if (estado[1].equals("\"Recuperado\"") && !fechaRecu[1].equals("\"-   -\"")) {
                        fechaRecuperacion = fechaHora.parse(fechaRecu[1].substring(1, 11));
                        contDias += ((fechaRecuperacion.getTime() - fechaFIS.getTime()) / 86400000);
                        contPersonas++;
                    }
                } catch (ParseException ex) {
                    ex.getStackTrace();
                }
            }
        }

        if (contPersonas == 0) {
            tiempoConInfeccion = 0;
        } else {
            tiempoConInfeccion = contDias * Math.pow(contPersonas, -1);
        }
    }

    public double getTiempoConInfeccion() {
        return tiempoConInfeccion;
    }
}
