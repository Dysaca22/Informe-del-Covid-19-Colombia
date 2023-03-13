package covidfinal;

import covidfinal.cargadorRecursos.CargadorDatosTexto;
import covidfinal.tiempo.Asintomatico;

public class Constante {

    public static final String INFOTOTAL = CargadorDatosTexto.leerArchivoTexto("/Datos.txt");
    public static final String[] PERSONAS = INFOTOTAL.split("}");

    public static final Asintomatico A = new Asintomatico(PERSONAS);
    public static int TIEMPOEXTRAASINTOMATICOS = (int) A.getTiempoPromedioAdicionalAsintomatico();
    public static double TIEMPOCONINFECCION = A.getTiempoConInfeccion();
}
