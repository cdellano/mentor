package saul.pdf.renderer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * Utilidad para generar gráficos JFreeChart.
 * Soporta gráficos de barras, pastel, líneas y área.
 */
public class ChartGenerator {

    /**
     * Datos para gráfico de categorías (barras, líneas, área).
     */
    public static class CategoryData {
        private final String series;
        private final String category;
        private final Number value;

        public CategoryData(String series, String category, Number value) {
            this.series = series;
            this.category = category;
            this.value = value;
        }

        public String getSeries() { return series; }
        public String getCategory() { return category; }
        public Number getValue() { return value; }
    }

    /**
     * Datos para gráfico de pastel.
     */
    public static class PieData {
        private final String label;
        private final Number value;

        public PieData(String label, Number value) {
            this.label = label;
            this.value = value;
        }

        public String getLabel() { return label; }
        public Number getValue() { return value; }
    }

    /**
     * Crea un gráfico según la configuración y datos proporcionados.
     *
     * @param config configuración del gráfico
     * @param data datos (CategoryData para barras/líneas, PieData para pastel)
     * @return gráfico JFreeChart configurado
     */
    public JFreeChart createChart(ChartConfig config, List<?> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Los datos no pueden estar vacíos");
        }

        switch (config.getChartType()) {
            case BAR_VERTICAL:
                return createBarChart(config, data, PlotOrientation.VERTICAL);
            case BAR_HORIZONTAL:
                return createBarChart(config, data, PlotOrientation.HORIZONTAL);
            case PIE:
                return createPieChart(config, data, false);
            case DONUT:
                return createPieChart(config, data, true);
            case LINE:
                return createLineChart(config, data);
            case AREA:
                return createAreaChart(config, data);
            case STACKED_BAR:
                return createStackedBarChart(config, data);
            default:
                return createBarChart(config, data, PlotOrientation.VERTICAL);
        }
    }

    /**
     * Convierte un gráfico JFreeChart a BufferedImage.
     *
     * @param chart gráfico a convertir
     * @param width ancho de la imagen
     * @param height alto de la imagen
     * @return imagen del gráfico
     */
    public BufferedImage chartToImage(JFreeChart chart, int width, int height) {
        return chart.createBufferedImage(width, height);
    }

    /**
     * Crea un gráfico de barras.
     */
    private JFreeChart createBarChart(ChartConfig config, List<?> data, PlotOrientation orientation) {
        CategoryDataset dataset = createCategoryDataset(data);

        JFreeChart chart = ChartFactory.createBarChart(
                config.getTitle(),
                config.getXAxisLabel(),
                config.getYAxisLabel(),
                dataset,
                orientation,
                config.isShowLegend(),
                true,
                false
        );

        applyChartStyle(chart, config);
        applyBarColors(chart, config);
        return chart;
    }

    /**
     * Crea un gráfico de pastel o dona.
     */
    @SuppressWarnings("unchecked")
    private JFreeChart createPieChart(ChartConfig config, List<?> data, boolean ring) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        for (Object item : data) {
            if (item instanceof PieData) {
                PieData pd = (PieData) item;
                dataset.setValue(pd.getLabel(), pd.getValue());
            } else if (item instanceof Map.Entry) {
                Map.Entry<String, Number> entry = (Map.Entry<String, Number>) item;
                dataset.setValue(entry.getKey(), entry.getValue());
            }
        }

        JFreeChart chart;
        if (ring) {
            chart = ChartFactory.createRingChart(
                    config.getTitle(),
                    dataset,
                    config.isShowLegend(),
                    true,
                    false
            );
        } else {
            chart = ChartFactory.createPieChart(
                    config.getTitle(),
                    dataset,
                    config.isShowLegend(),
                    true,
                    false
            );
        }

        applyChartStyle(chart, config);
        applyPieColors(chart, config, dataset);

        if (config.isShowLabels()) {
            configurePieLabels(chart);
        }

        return chart;
    }

    /**
     * Crea un gráfico de líneas.
     */
    private JFreeChart createLineChart(ChartConfig config, List<?> data) {
        CategoryDataset dataset = createCategoryDataset(data);

        JFreeChart chart = ChartFactory.createLineChart(
                config.getTitle(),
                config.getXAxisLabel(),
                config.getYAxisLabel(),
                dataset,
                PlotOrientation.VERTICAL,
                config.isShowLegend(),
                true,
                false
        );

        applyChartStyle(chart, config);
        applyLineColors(chart, config);
        return chart;
    }

    /**
     * Crea un gráfico de área.
     */
    private JFreeChart createAreaChart(ChartConfig config, List<?> data) {
        CategoryDataset dataset = createCategoryDataset(data);

        JFreeChart chart = ChartFactory.createAreaChart(
                config.getTitle(),
                config.getXAxisLabel(),
                config.getYAxisLabel(),
                dataset,
                PlotOrientation.VERTICAL,
                config.isShowLegend(),
                true,
                false
        );

        applyChartStyle(chart, config);
        applyAreaColors(chart, config);
        return chart;
    }

    /**
     * Crea un gráfico de barras apiladas.
     */
    private JFreeChart createStackedBarChart(ChartConfig config, List<?> data) {
        CategoryDataset dataset = createCategoryDataset(data);

        JFreeChart chart = ChartFactory.createStackedBarChart(
                config.getTitle(),
                config.getXAxisLabel(),
                config.getYAxisLabel(),
                dataset,
                PlotOrientation.VERTICAL,
                config.isShowLegend(),
                true,
                false
        );

        applyChartStyle(chart, config);
        applyStackedBarColors(chart, config);
        return chart;
    }

    /**
     * Crea un dataset de categorías desde la lista de datos.
     */
    @SuppressWarnings("unchecked")
    private CategoryDataset createCategoryDataset(List<?> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Object item : data) {
            if (item instanceof CategoryData) {
                CategoryData cd = (CategoryData) item;
                dataset.addValue(cd.getValue(), cd.getSeries(), cd.getCategory());
            } else if (item instanceof Map.Entry) {
                Map.Entry<String, Number> entry = (Map.Entry<String, Number>) item;
                dataset.addValue(entry.getValue(), "Serie 1", entry.getKey());
            }
        }

        return dataset;
    }

    /**
     * Aplica estilos generales al gráfico.
     */
    private void applyChartStyle(JFreeChart chart, ChartConfig config) {
        chart.setBackgroundPaint(config.getBackgroundColor());

        if (chart.getTitle() != null) {
            chart.getTitle().setFont(new Font("SansSerif", Font.BOLD, (int) config.getTitleFontSize()));
        }

        if (chart.getLegend() != null) {
            chart.getLegend().setItemFont(new Font("SansSerif", Font.PLAIN, (int) config.getLabelFontSize()));
        }
    }

    /**
     * Aplica colores a gráfico de barras.
     */
    private void applyBarColors(JFreeChart chart, ChartConfig config) {
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        Color[] colors = config.getSeriesColors();
        for (int i = 0; i < colors.length; i++) {
            renderer.setSeriesPaint(i, colors[i]);
        }
    }

    /**
     * Aplica colores a gráfico de pastel.
     */
    @SuppressWarnings("rawtypes")
    private void applyPieColors(JFreeChart chart, ChartConfig config, DefaultPieDataset dataset) {
        PiePlot plot;
        if (chart.getPlot() instanceof RingPlot) {
            plot = (RingPlot) chart.getPlot();
        } else {
            plot = (PiePlot) chart.getPlot();
        }

        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);

        Color[] colors = config.getSeriesColors();
        List keys = dataset.getKeys();
        for (int i = 0; i < keys.size(); i++) {
            Comparable key = (Comparable) keys.get(i);
            plot.setSectionPaint(key, colors[i % colors.length]);
        }
    }

    /**
     * Configura etiquetas del gráfico de pastel.
     */
    @SuppressWarnings("rawtypes")
    private void configurePieLabels(JFreeChart chart) {
        PiePlot plot;
        if (chart.getPlot() instanceof RingPlot) {
            plot = (RingPlot) chart.getPlot();
        } else {
            plot = (PiePlot) chart.getPlot();
        }

        PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
                "{0}: {1} ({2})",
                new DecimalFormat("0"),
                new DecimalFormat("0.0%")
        );
        plot.setLabelGenerator(labelGenerator);
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 10));
    }

    /**
     * Aplica colores a gráfico de líneas.
     */
    private void applyLineColors(JFreeChart chart, ChartConfig config) {
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        Color[] colors = config.getSeriesColors();
        for (int i = 0; i < colors.length; i++) {
            renderer.setSeriesPaint(i, colors[i]);
            renderer.setSeriesStroke(i, new BasicStroke(2.0f));
            renderer.setSeriesShapesVisible(i, true);
        }
    }

    /**
     * Aplica colores a gráfico de área.
     */
    private void applyAreaColors(JFreeChart chart, ChartConfig config) {
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setForegroundAlpha(0.7f);

        AreaRenderer renderer = (AreaRenderer) plot.getRenderer();
        Color[] colors = config.getSeriesColors();
        for (int i = 0; i < colors.length; i++) {
            renderer.setSeriesPaint(i, colors[i]);
        }
    }

    /**
     * Aplica colores a gráfico de barras apiladas.
     */
    private void applyStackedBarColors(JFreeChart chart, ChartConfig config) {
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        StackedBarRenderer renderer = (StackedBarRenderer) plot.getRenderer();
        Color[] colors = config.getSeriesColors();
        for (int i = 0; i < colors.length; i++) {
            renderer.setSeriesPaint(i, colors[i]);
        }
    }

    // ==================== MÉTODOS DE CONVENIENCIA ====================

    /**
     * Crea datos de categoría simples desde un mapa.
     */
    public static List<CategoryData> fromMap(String seriesName, Map<String, Number> data) {
        return data.entrySet().stream()
                .map(e -> new CategoryData(seriesName, e.getKey(), e.getValue()))
                .toList();
    }

    /**
     * Crea datos de pastel desde un mapa.
     */
    public static List<PieData> pieFromMap(Map<String, Number> data) {
        return data.entrySet().stream()
                .map(e -> new PieData(e.getKey(), e.getValue()))
                .toList();
    }
}

