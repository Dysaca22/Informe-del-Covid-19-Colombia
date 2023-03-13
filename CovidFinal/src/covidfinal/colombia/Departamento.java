package covidfinal.colombia;

import covidfinal.Constante;
import covidfinal.tiempo.FechaTardanzaReporte;
import covidfinal.tiempo.Fechas;
import covidfinal.tiempo.tiempoInfeccion;
import java.util.Date;

public class Departamento {

    private Date[] fechas;
    private int numFechas;
    private Fechas[] fechasDatos;

    private String nombre;

    private String[] personasGeneral;
    private String[] personas;
    private int numPersonas;

    private int activos;
    private int infectados;
    private int recuperados;
    private int muertos;
    private double tiempoConInfeccion;

    FechaTardanzaReporte ftr;
    private double[] porcentajeAcumulado;

    double[] activosNewCast;
    double[] infectadosSumados;

    private double[] Rt;
    private int contRt;
    private double[] RtInfectados;
    private double[] log;
    private int contLog;

    public Departamento(Date[] fechas, int numFechas, String nombre) {

        this.fechas = fechas;
        this.numFechas = numFechas;
        this.fechasDatos = new Fechas[365];

        this.nombre = nombre;

        personasGeneral = new String[10000];
        this.numPersonas = 0;

        this.activos = 0;
        this.infectados = 0;
        this.recuperados = 0;
        this.muertos = 0;
        this.tiempoConInfeccion = 0;

        activosNewCast = new double[26];
        infectadosSumados = new double[26];

        Rt = new double[365];
        contRt = 0;
        RtInfectados = new double[26];
        log = new double[365];
        contLog = 0;
    }

    public void reducirTamanho() {

        this.personas = new String[numPersonas];
        System.arraycopy(personasGeneral, 0, personas, 0, numPersonas);
        setFechas();
    }

    private void setFechas() {

        for (int i = 0; i < numFechas; i++) {

            fechasDatos[i] = new Fechas(fechas[i], personas);
        }
        obtenerDatosDepartamento();
    }

    private void obtenerDatosDepartamento() {

        for (int i = 0; i < numFechas; i++) {

            infectados += fechasDatos[i].getInfectados();
            recuperados += fechasDatos[i].getRecuperados();
            muertos += fechasDatos[i].getMuertos();
            activos = infectados - recuperados - muertos;

            fechasDatos[i].setActivos(activos);
        }
        tiempoInfeccion tci = new tiempoInfeccion(personas);
        tiempoConInfeccion = tci.getTiempoConInfeccion();
        if (tiempoConInfeccion == 0) {
            tiempoConInfeccion = Constante.TIEMPOCONINFECCION;
        }

        ftr = new FechaTardanzaReporte(personas);
        porcentajeAcumulado = ftr.getPorcentajeAcumulado();

        obtenerInfectadosFaltantes();
    }

    private void obtenerInfectadosFaltantes() {

        int[] infectadosAcumulados = new int[numFechas], recuperadosAcumulados = new int[numFechas], muertosAcumulados = new int[numFechas];
        for (int i = 0; i < numFechas; i++) {
            if (i > 0) {
                infectadosAcumulados[i] = infectadosAcumulados[i - 1] + fechasDatos[i].getInfectados();
                recuperadosAcumulados[i] = recuperadosAcumulados[i - 1] + fechasDatos[i].getRecuperados();
                muertosAcumulados[i] = muertosAcumulados[i - 1] + fechasDatos[i].getMuertos();
            }
        }

        activosNewCast[25] = fechasDatos[numFechas - 26].getActivos();
        infectadosSumados[25] = infectadosAcumulados[numFechas - 26];
        for (int i = 25; i > 0; i--) {
            infectadosSumados[i - 1] = infectadosSumados[i] + (fechasDatos[numFechas - i].getInfectados() * Math.pow(porcentajeAcumulado[i], -1));
            activosNewCast[i - 1] = infectadosSumados[i - 1] - muertosAcumulados[numFechas - i] - recuperadosAcumulados[numFechas - i];
        }

        int num = 0;
        for (int i = 24; i >= 0; i--) {

            RtInfectados[num] = tiempoConInfeccion * Math.log(activosNewCast[i] * Math.pow(activosNewCast[i + 1], -1)) + 1;
            num++;
        }

        obtenerEstadisticas();
    }

    private void obtenerEstadisticas() {

        for (int i = 0; i < numFechas; i++) {

            if (i > 0) {
                if (fechasDatos[i].getActivos() != 0) {
                    if (fechasDatos[i - 1].getActivos() != 0) {
                        Rt[contRt] = tiempoConInfeccion * Math.log(fechasDatos[i].getActivos() * Math.pow(fechasDatos[i - 1].getActivos(), -1)) + 1;
                        contRt++;
                    }
                    log[contLog] = Math.log(fechasDatos[i].getActivos());
                    contLog++;
                }
            }
        }
    }

    public String getNombre() {
        return nombre;
    }

    public int getActivos() {
        return activos;
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

    public double getTiempoConInfeccion() {
        return tiempoConInfeccion;
    }

    public void setPersonas(String personas) {
        this.personasGeneral[numPersonas] = personas;
        this.numPersonas++;
    }

    public double[] getRt() {
        return Rt;
    }

    public int getContRt() {
        return contRt;
    }

    public double[] getRtInfectados() {
        return RtInfectados;
    }

    public double[] getLog() {
        return log;
    }

    public int getContLog() {
        return contLog;
    }

    public double[] getPorcentajeAcumulado() {
        return porcentajeAcumulado;
    }

    public int getNumFechas() {
        return numFechas;
    }

    public Fechas[] getFechasDatos() {
        return fechasDatos;
    }

}
