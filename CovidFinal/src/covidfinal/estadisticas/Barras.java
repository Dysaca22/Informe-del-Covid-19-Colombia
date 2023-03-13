package covidfinal.estadisticas;

import covidfinal.tiempo.Fechas;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Barras extends JFrame {

    private static final long serialVersionUID = 1L;

    double[] porcentajeAcumulado;
    Fechas[] fechas;
    int numFechas;
    String nombreBarra;
    String departamento;

    public Barras(double[] porcentajeAcumulado, Fechas[] fechas, int numFechas, String nombreBarra, String departamento) {

        super(departamento);

        this.porcentajeAcumulado = porcentajeAcumulado;
        this.fechas = fechas;
        this.numFechas = numFechas;
        this.nombreBarra = nombreBarra;
        this.departamento = departamento;

        DefaultCategoryDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);

        ChartFrame frame = new ChartFrame("Gráfica valores " + nombreBarra + " " + departamento, chart);
        frame.pack();
        frame.setSize(1300, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset result = new DefaultCategoryDataset();

        int num = 25;
        for (int i = 0; i < numFechas; i++) {

            result.setValue(fechas[i].getInfectados(), "Infectados", fechas[i].getFecha().toString().substring(0, 10));
            if (i < numFechas - 25) {
                result.setValue(fechas[i].getInfectados(), "InfectadosNewCast", fechas[i].getFecha().toString().substring(0, 10));
            } else {
                result.setValue(fechas[i].getInfectados() * Math.pow(porcentajeAcumulado[num], -1), "InfectadosNewCast", fechas[i].getFecha().toString().substring(0, 10));
                num--;
            }
        }
        return result;
    }

    private JFreeChart createChart(DefaultCategoryDataset dataset) {

        JFreeChart chart = ChartFactory.createBarChart3D("Gráfica valores " + nombreBarra + " " + departamento, "Dias", "Infectados", dataset, PlotOrientation.VERTICAL, true, true, false);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        CategoryAxis xAxis = (CategoryAxis) plot.getDomainAxis();
        xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        plot.setBackgroundAlpha(0.5f);
        return chart;
    }

}
