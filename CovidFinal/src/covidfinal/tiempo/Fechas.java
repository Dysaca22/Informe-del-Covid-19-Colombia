package covidfinal.tiempo;

import covidfinal.Constante;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Fechas {

    private final DateFormat fechaHora;

    private String[] personas;

    private Date fecha;

    private int activos;
    private int infectados;
    private int recuperados;
    private int muertos;

    public Fechas(Date fecha, String[] personas) {

        fechaHora = new SimpleDateFormat("yyyy-MM-dd");

        this.personas = personas;

        this.fecha = fecha;

        activos = 0;
        infectados = 0;
        recuperados = 0;
        muertos = 0;

        obtenerDatos();

    }

    private void obtenerDatos() {

        for (int i = 0; i < personas.length; i++) {

            String[] infoPersona = personas[i].split(",");

            String[] fechaNot = infoPersona[2].split(":");
            String[] estado = infoPersona[6].split(":");
            String[] fechaFis = infoPersona[12].split(":");

            Date fechaTemporal1 = null;
            try {
                if (!fechaFis[1].equals("\"-   -\"") && !fechaFis[1].equals("\"Asintomático\"") && !estado[1].equals("\"N/A\"")) {

                    fechaTemporal1 = fechaHora.parse(fechaFis[1].substring(1, 11));

                } else {
                    fechaTemporal1 = fechaHora.parse(fechaNot[1].substring(1, 11));
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(fechaTemporal1);
                    calendar.add(Calendar.DAY_OF_YEAR, -Constante.TIEMPOEXTRAASINTOMATICOS);
                    fechaTemporal1 = calendar.getTime();
                }
            } catch (ParseException ex) {
                Logger.getLogger(Fechas.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (fecha.equals(fechaTemporal1)) {
                infectados++;
            }

            if (infoPersona.length == 17) {

                String[] fechaMuerte = infoPersona[13].split(":");
                String[] fechaRecu = infoPersona[15].split(":");

                Date fechaTemporal2 = null, fechaTemporal3 = null;
                try {

                    if (estado[1].equals("\"Fallecido\"") && !fechaMuerte[1].equals("\"-   -\"")) {
                        fechaTemporal2 = fechaHora.parse(fechaMuerte[1].substring(1, 11));
                        if (fecha.equals(fechaTemporal2)) {
                            muertos++;
                        }
                    }
                    if (estado[1].equals("\"Recuperado\"") && !fechaRecu[1].equals("\"-   -\"")) {
                        fechaTemporal3 = fechaHora.parse(fechaRecu[1].substring(1, 11));
                        if (fecha.equals(fechaTemporal3)) {
                            recuperados++;
                        }
                    }
                } catch (ParseException ex) {
                    ex.getStackTrace();
                }
            }
        }
    }

    public int getActivos() {
        return activos;
    }

    public void setActivos(int activos) {
        this.activos = activos;
    }

    public int getInfectados() {
        return infectados;
    }

    public int getRecuperados() {
        return recuperados;
    }

    public int getMuertos() {
        return muertos;
    }

    public Date getFecha() {
        return fecha;
    }

}
