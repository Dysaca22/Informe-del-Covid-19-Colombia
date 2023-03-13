package covidfinal;

import covidfinal.colombia.Colombia;
import covidfinal.estadisticas.Datos;
import java.awt.Color;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Covid19 {

    private final DateFormat fechaHora;

    private String[] personas;

    private Date[] fechas;
    private int numFechas;

    Colombia colombia;

    public Covid19() {

        fechaHora = new SimpleDateFormat("yyyy-MM-dd");

        this.personas = Constante.PERSONAS;

        fechas = new Date[365];
        numFechas = 0;

        dias();
        colombia = new Colombia(fechas, numFechas);
    }

    private void dias() {

        for (int i = 0; i < personas.length; i++) {

            String[] infoPersona = personas[i].split(",");

            String[] estado = infoPersona[6].split(":");
            if (!estado[1].equals("\"N/A\"") && infoPersona.length == 17) {

                String[] fechaNot = infoPersona[2].split(":");
                String[] fechaFis = infoPersona[12].split(":");
                String[] fechaMuerte = infoPersona[13].split(":");
                String[] fechaRecu = infoPersona[15].split(":");
                String[] fechaWeb = infoPersona[16].split(":");

                Date fechaTemporal1 = null, fechaTemporal2 = null, fechaTemporal3 = null, fechaTemporal4 = null, fechaTemporal5 = null;
                try {
                    fechaTemporal1 = fechaHora.parse(fechaNot[1].substring(1, 11));
                    if (!fechaFis[1].equals("\"-   -\"") && !fechaFis[1].equals("\"Asintomático\"")) {
                        fechaTemporal2 = fechaHora.parse(fechaFis[1].substring(1, 11));
                    } else {
                        fechaTemporal2 = fechaHora.parse(fechaNot[1].substring(1, 11));
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(fechaTemporal2);
                        calendar.add(Calendar.DAY_OF_YEAR, -Constante.TIEMPOEXTRAASINTOMATICOS);
                        fechaTemporal2 = calendar.getTime();
                    }
                    if (!fechaMuerte[1].equals("\"-   -\"")) {
                        fechaTemporal3 = fechaHora.parse(fechaMuerte[1].substring(1, 11));
                        if (comprobarFecha(fechaTemporal3)) {
                            fechas[numFechas] = fechaTemporal3;
                            numFechas++;
                        }
                    }
                    if (!fechaRecu[1].equals("\"-   -\"")) {
                        fechaTemporal4 = fechaHora.parse(fechaRecu[1].substring(1, 11));
                        if (comprobarFecha(fechaTemporal4)) {
                            fechas[numFechas] = fechaTemporal4;
                            numFechas++;
                        }
                    }
                    fechaTemporal5 = fechaHora.parse(fechaWeb[1].substring(1, 11));
                } catch (ParseException ex) {
                    ex.getStackTrace();
                }

                if (comprobarFecha(fechaTemporal1)) {
                    fechas[numFechas] = fechaTemporal1;
                    numFechas++;
                }
                if (comprobarFecha(fechaTemporal2)) {
                    fechas[numFechas] = fechaTemporal2;
                    numFechas++;
                }
                if (comprobarFecha(fechaTemporal5)) {
                    fechas[numFechas] = fechaTemporal5;
                    numFechas++;
                }
            }
        }
        ordenarFechas();
    }

    private boolean comprobarFecha(Date fechaComprobar) {

        for (int i = 0; i < numFechas; i++) {

            if (fechaComprobar.equals(fechas[i])) {

                return false;
            }
        }
        return true;
    }

    private void ordenarFechas() {

        for (int i = 0; i < numFechas; i++) {

            Date fechaTemp = null;
            for (int j = i + 1; j < numFechas; j++) {

                if (fechas[i].after(fechas[j])) {

                    fechaTemp = fechas[i];
                    fechas[i] = fechas[j];
                    fechas[j] = fechaTemp;
                }
            }
        }
    }

    public Colombia getColombia() {
        return colombia;
    }

    public static void main(String[] args) {

        Covid19 covid = new Covid19();

        Datos d = new Datos(covid.getColombia());
        d.setDefaultCloseOperation(Datos.EXIT_ON_CLOSE);
        d.getContentPane().setBackground(Color.darkGray);
        d.setSize(800, 600);
        d.setResizable(false);
        d.setLocationRelativeTo(null);
        d.setVisible(true);
    }
}
