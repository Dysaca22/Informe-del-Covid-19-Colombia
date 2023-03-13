package covidfinal.estadisticas;

import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Graficas {

    private XYSeries serieSuavisado;
    private XYSeries serieDiario;
    private XYSeries serieRtNewCast;
    private XYSeries uno;

    private double[] Rt;
    private int numRt;
    private double[] infectados;

    private String nombre;
    private String datoNombre;

    public Graficas(double[] Rt, int numRt, double[] infectados, String departamento, String datoNombre) {

        serieSuavisado = new XYSeries("Suavisado");
        serieDiario = new XYSeries("Diario");
        serieRtNewCast = new XYSeries("Rt NewCast");
        uno = new XYSeries("Rt 1");

        this.Rt = Rt;
        this.numRt = numRt;
        this.infectados = infectados;

        nombre = departamento;
        this.datoNombre = datoNombre;
        ponerValoresRt();
    }

    public Graficas(double[] log, int numLog, String departamento, String datoNombre) {

        serieDiario = new XYSeries("Diario");

        this.Rt = log;
        this.numRt = numLog;

        nombre = departamento;
        this.datoNombre = datoNombre;
        ponerValoresLog();
    }

    private void ponerValoresLog() {

        for (int i = 0; i < numRt; i++) {

            serieDiario.add(i, Rt[i]);
        }
        hacerGraficaLog();
    }

    private void hacerGraficaLog() {

        XYSeriesCollection dato = new XYSeriesCollection();
        dato.addSeries(serieDiario);

        JFreeChart grafica = ChartFactory.createXYLineChart(nombre, "Días", datoNombre, dato, PlotOrientation.VERTICAL, true, true, false);

        final XYPlot plot = grafica.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        plot.setRenderer(renderer);

        ChartFrame frame = new ChartFrame("Gráfica valores " + datoNombre + " " + nombre, grafica);
        frame.pack();
        frame.setSize(1300, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void ponerValoresRt() {

        double suma = 0;
        double sumaNewCast = 0;
        int num = 0;
        double numtemp = 0;

        for (int i = 0; i < numRt; i++) {

            if (i == 0 || i == (numRt - 1)) {
                suma = Rt[i];
            } else {
                suma = (Rt[i - 1] + Rt[i + 1] + Rt[i]) * Math.pow(3, -1);
            }
            if (i == numRt - 26) {
                numtemp = suma;
                serieRtNewCast.add(i, suma);
            }
            if (i >= numRt - 25) {
                if (i == numRt - 25) {
                    sumaNewCast = (infectados[num + 1] + infectados[num] + suma) * Math.pow(3, -1);
                    num++;
                } else if (i == (numRt - 1)) {

                } else {
                    sumaNewCast = (infectados[num + 1] + infectados[num] + infectados[num - 1]) * Math.pow(3, -1);
                    num++;
                }
                serieRtNewCast.add(i, sumaNewCast);
            }
            uno.add(i, 1);
            serieDiario.add(i, Rt[i]);
            serieSuavisado.add(i, suma);

        }
        hacerGraficaRt();
    }

    private void hacerGraficaRt() {

        XYSeriesCollection dato = new XYSeriesCollection();
        dato.addSeries(serieSuavisado);
//        dato.addSeries(serieDiario);
        dato.addSeries(serieRtNewCast);
        dato.addSeries(uno);

        JFreeChart grafica = ChartFactory.createXYLineChart(nombre, "Días", datoNombre, dato, PlotOrientation.VERTICAL, true, true, false);

        final XYPlot plot = grafica.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLACK);
//        renderer.setSeriesPaint(1, Color.LIGHT_GRAY);
        renderer.setSeriesPaint(1, Color.RED);
        renderer.setSeriesPaint(2, Color.BLUE);
        plot.setRenderer(renderer);

        ChartFrame frame = new ChartFrame("Gráfica valores " + datoNombre + " " + nombre, grafica);
        frame.pack();
        frame.setSize(1300, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
