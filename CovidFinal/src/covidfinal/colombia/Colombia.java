package covidfinal.colombia;

import covidfinal.estadisticas.Barras;
import covidfinal.Constante;
import covidfinal.estadisticas.Graficas;
import covidfinal.tiempo.FechaTardanzaReporte;
import covidfinal.tiempo.Fechas;
import covidfinal.tiempo.tiempoInfeccion;
import java.util.Date;
import javax.swing.JOptionPane;

public class Colombia {

    private String[] personas;

    private int numDepartamentos;
    private Departamento[] departamentos;

    private int numFechas;
    private Date[] fechas;
    private Fechas[] fechasDatos;

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

    public Colombia(Date[] fechas, int numFechas) {

        this.personas = Constante.PERSONAS;

        this.numDepartamentos = 0;
        this.departamentos = new Departamento[50];

        this.numFechas = numFechas;
        this.fechas = fechas;
        this.fechasDatos = new Fechas[365];

        this.activos = 0;
        this.infectados = 0;
        this.recuperados = 0;
        this.muertos = 0;
        this.tiempoConInfeccion = 0;

        ftr = new FechaTardanzaReporte(personas);
        porcentajeAcumulado = ftr.getPorcentajeAcumulado();

        activosNewCast = new double[26];
        infectadosSumados = new double[26];

        Rt = new double[365];
        contRt = 0;
        RtInfectados = new double[26];
        log = new double[365];
        contLog = 0;
        setFechas();
    }

    private void setFechas() {

        for (int i = 0; i < numFechas; i++) {

            fechasDatos[i] = new Fechas(fechas[i], Constante.PERSONAS);
        }
        obtenerDatosColombia();
    }

    private void obtenerDatosColombia() {

        for (int i = 0; i < numFechas; i++) {

            infectados += fechasDatos[i].getInfectados();
            recuperados += fechasDatos[i].getRecuperados();
            muertos += fechasDatos[i].getMuertos();
            activos = infectados - recuperados - muertos;

            fechasDatos[i].setActivos(activos);
        }

        tiempoInfeccion tci = new tiempoInfeccion(personas);
        tiempoConInfeccion = tci.getTiempoConInfeccion();
        obtenerDepartamentos();
    }

    private void obtenerDepartamentos() {

        for (int i = 0; i < personas.length; i++) {

            String[] infoPersona = personas[i].split(",");

            String[] departamento = infoPersona[5].split(":");

            if (comprobarDepartamento(departamento[1].split("\"")[1])) {

                departamentos[numDepartamentos] = new Departamento(fechas, numFechas, departamento[1].split("\"")[1]);
                numDepartamentos++;
            }
        }
        darPersonasDepartamento();
    }

    private boolean comprobarDepartamento(String departamento) {

        for (int i = 0; i < numDepartamentos; i++) {

            if (departamento.equals(departamentos[i].getNombre())) {

                return false;
            }
        }
        return true;
    }

    private void darPersonasDepartamento() {

        for (int i = 0; i < personas.length; i++) {

            String[] infoPersona = personas[i].split(",");

            String[] departamento = infoPersona[5].split(":");

            for (int j = 0; j < numDepartamentos; j++) {

                if (departamento[1].split("\"")[1].equals(departamentos[j].getNombre())) {

                    departamentos[j].setPersonas(personas[i]);
                }
            }
        }
        iniciarDepartamentos();
    }

    private void iniciarDepartamentos() {

        for (int i = 0; i < numDepartamentos; i++) {

            departamentos[i].reducirTamanho();
        }
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

    public void graficarRtProfesor(String departamento) {

        if (departamento.equals("Colombia")) {

            JOptionPane.showMessageDialog(null, "Colombia tiene " + activos + " activos, tiene " + infectados + " infectados, tiene " + recuperados + " recuperados, " + muertos + " muertos y porta la infección " + (int) tiempoConInfeccion + " días aproximadamente.");
            Graficas g = new Graficas(Rt, contRt, RtInfectados, "Colombia", "Rt");
        } else {

            for (int i = 0; i < numDepartamentos; i++) {

                if (departamentos[i].getNombre().equals(departamento)) {

                    int infec = departamentos[i].getInfectados();
                    int recup = departamentos[i].getRecuperados();
                    int muer = departamentos[i].getMuertos();
                    int acti = departamentos[i].getActivos();
                    int numDiasInfectado = (int) departamentos[i].getTiempoConInfeccion();

                    JOptionPane.showMessageDialog(null, "El departamento de " + departamento + " tiene " + acti + " activos, tiene " + infec + " infectados, tiene " + recup + " recuperados, " + muer + " muertos y porta la infección " + numDiasInfectado + " días aproximadamente.");
                    Graficas g = new Graficas(departamentos[i].getRt(), departamentos[i].getContRt(), departamentos[i].getRtInfectados(), departamento, "Rt");
                }
            }
        }
    }

    public void graficarLogInfectados(String departamento) {

        if (departamento.equals("Colombia")) {

            JOptionPane.showMessageDialog(null, "Colombia tiene " + activos + " activos, tiene " + infectados + " infectados, tiene " + recuperados + " recuperados, " + muertos + " muertos y porta la infección " + (int) tiempoConInfeccion + " días aproximadamente.");
            Graficas g = new Graficas(log, contLog, "Colombia", "Log Infectados");
        } else {

            for (int i = 0; i < numDepartamentos; i++) {

                if (departamentos[i].getNombre().equals(departamento)) {

                    int infec = departamentos[i].getInfectados();
                    int recup = departamentos[i].getRecuperados();
                    int muer = departamentos[i].getMuertos();
                    int acti = departamentos[i].getActivos();
                    int numDiasInfectado = (int) departamentos[i].getTiempoConInfeccion();

                    JOptionPane.showMessageDialog(null, "El departamento de " + departamento + " tiene " + acti + " activos, tiene " + infec + " infectados, tiene " + recup + " recuperados y " + muer + " muertos y porta la infección " + numDiasInfectado + " días aproximadamente.");
                    Graficas g = new Graficas(departamentos[i].getLog(), departamentos[i].getContLog(), departamento, "Log Infectados");
                }
            }
        }
    }

    public void graficasActivos(String departamento) {

        if (departamento.equals("Colombia")) {

            JOptionPane.showMessageDialog(null, "Colombia tiene " + activos + " activos, tiene " + infectados + " infectados, tiene " + recuperados + " recuperados, " + muertos + " muertos y porta la infección " + (int) tiempoConInfeccion + " días aproximadamente.");
            Barras b = new Barras(porcentajeAcumulado, fechasDatos, numFechas, "Infectados", departamento);
        } else {

            for (int i = 0; i < numDepartamentos; i++) {

                if (departamentos[i].getNombre().equals(departamento)) {

                    int infec = departamentos[i].getInfectados();
                    int recup = departamentos[i].getRecuperados();
                    int muer = departamentos[i].getMuertos();
                    int acti = departamentos[i].getActivos();
                    int numDiasInfectado = (int) departamentos[i].getTiempoConInfeccion();

                    JOptionPane.showMessageDialog(null, "El departamento de " + departamento + " tiene " + acti + " activos, tiene " + infec + " infectados, tiene " + recup + " recuperados y " + muer + " muertos y porta la infección " + numDiasInfectado + " días aproximadamente.");
                    Barras b = new Barras(departamentos[i].getPorcentajeAcumulado(), departamentos[i].getFechasDatos(), departamentos[i].getNumFechas(), "Infectados", departamento);
                }
            }
        }
    }
}
