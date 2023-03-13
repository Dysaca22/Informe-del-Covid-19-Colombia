package covidfinal.tiempo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Asintomatico {

    DateFormat fechaHora;

    private double tiempoPromedioAdicionalAsintomatico;
    private int numAsintomaticos;
    private double tiempoAsintomatico;

    private double tiempoConInfeccion;

    private String[] infoPersonas;

    public Asintomatico(String[] infoPersonas) {

        fechaHora = new SimpleDateFormat("yyyy-MM-dd");

        tiempoPromedioAdicionalAsintomatico = 0;
        numAsintomaticos = 0;
        tiempoAsintomatico = 0;

        tiempoConInfeccion = 0;

        this.infoPersonas = infoPersonas;

        obtenerTiempoAsintomaticos();
    }

    private void obtenerTiempoAsintomaticos() {

        for (int i = 0; i < infoPersonas.length; i++) {

            String[] campoPersona = infoPersonas[i].split(",");

            if (campoPersona.length == 17) {
                String[] fis = campoPersona[12].split(":");
                String[] atencion = campoPersona[6].split(":");

                if (fis[1].equals("\"Asintomático\"")) {

                    int sw = 0;

                    String[] notificacion = campoPersona[2].split(":");
                    String[] recuperado = campoPersona[15].split(":");
                    String[] muerto = campoPersona[13].split(":");

                    String fechaRM = "";
                    if (atencion[1].equals("\"Recuperado\"") && !recuperado[1].equals("\"-   -\"")) {
                        fechaRM = recuperado[1].substring(1, 11);
                        sw = 1;
                    } else if (atencion[1].equals("\"Fallecido\"")) {
                        fechaRM = muerto[1].substring(1, 11);
                        sw = 1;
                    }

                    if (sw == 1) {

                        Date fechaFinal = null, fechaInicial = null;
                        try {
                            fechaFinal = fechaHora.parse(fechaRM);
                            fechaInicial = fechaHora.parse(notificacion[1].substring(1, 11));
                        } catch (ParseException ex) {
                            ex.getStackTrace();
                        }

                        tiempoAsintomatico += ((fechaFinal.getTime() - fechaInicial.getTime()) / 86400000);
                        numAsintomaticos++;
                    }
                }
            }
        }

        tiempoInfeccion tci = new tiempoInfeccion(infoPersonas);
        tiempoConInfeccion = tci.getTiempoConInfeccion();

        tiempoPromedioAdicionalAsintomatico = tiempoAsintomatico * Math.pow(numAsintomaticos, -1);
        tiempoPromedioAdicionalAsintomatico = tiempoConInfeccion - tiempoPromedioAdicionalAsintomatico;
    }

    public double getTiempoPromedioAdicionalAsintomatico() {
        return tiempoPromedioAdicionalAsintomatico;
    }

    public double getTiempoConInfeccion() {
        return tiempoConInfeccion;
    }

}
