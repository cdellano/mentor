package saul.pdf.renderer;

import java.awt.*;

/**
 * Configuración para gráficos JFreeChart en PDF.
 * Define tipo de gráfico, dimensiones, colores y estilos.
 */
public class ChartConfig {

    /**
     * Tipos de gráficos disponibles.
     */
    public enum ChartType {
        /** Gráfico de barras verticales */
        BAR_VERTICAL,
        /** Gráfico de barras horizontales */
        BAR_HORIZONTAL,
        /** Gráfico de pastel */
        PIE,
        /** Gráfico de dona (pie con hueco) */
        DONUT,
        /** Gráfico de líneas */
        LINE,
        /** Gráfico de área */
        AREA,
        /** Gráfico de barras apiladas */
        STACKED_BAR
    }

    private ChartType chartType;
    private float width;
    private float height;
    private String title;
    private String xAxisLabel;
    private String yAxisLabel;
    private boolean showLegend;
    private boolean showLabels;
    private boolean show3D;
    private Color backgroundColor;
    private Color[] seriesColors;
    private float labelFontSize;
    private float titleFontSize;

    /**
     * Constructor con valores por defecto.
     */
    public ChartConfig() {
        this.chartType = ChartType.BAR_VERTICAL;
        this.width = 400f;
        this.height = 300f;
        this.title = "";
        this.xAxisLabel = "";
        this.yAxisLabel = "";
        this.showLegend = true;
        this.showLabels = true;
        this.show3D = false;
        this.backgroundColor = Color.WHITE;
        this.seriesColors = defaultColors();
        this.labelFontSize = 10f;
        this.titleFontSize = 14f;
    }

    private Color[] defaultColors() {
        return new Color[] {
            new Color(79, 129, 189),   // Azul
            new Color(192, 80, 77),    // Rojo
            new Color(155, 187, 89),   // Verde
            new Color(128, 100, 162),  // Púrpura
            new Color(75, 172, 198),   // Cian
            new Color(247, 150, 70),   // Naranja
            new Color(0, 176, 80),     // Verde oscuro
            new Color(255, 192, 0)     // Amarillo
        };
    }

    // ==================== MÉTODOS FLUIDOS ====================

    public ChartConfig type(ChartType type) {
        this.chartType = type;
        return this;
    }

    public ChartConfig barVertical() {
        this.chartType = ChartType.BAR_VERTICAL;
        return this;
    }

    public ChartConfig barHorizontal() {
        this.chartType = ChartType.BAR_HORIZONTAL;
        return this;
    }

    public ChartConfig pie() {
        this.chartType = ChartType.PIE;
        return this;
    }

    public ChartConfig donut() {
        this.chartType = ChartType.DONUT;
        return this;
    }

    public ChartConfig line() {
        this.chartType = ChartType.LINE;
        return this;
    }

    public ChartConfig area() {
        this.chartType = ChartType.AREA;
        return this;
    }

    public ChartConfig stackedBar() {
        this.chartType = ChartType.STACKED_BAR;
        return this;
    }

    public ChartConfig size(float width, float height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public ChartConfig title(String title) {
        this.title = title;
        return this;
    }

    public ChartConfig axisLabels(String xLabel, String yLabel) {
        this.xAxisLabel = xLabel;
        this.yAxisLabel = yLabel;
        return this;
    }

    public ChartConfig xAxisLabel(String label) {
        this.xAxisLabel = label;
        return this;
    }

    public ChartConfig yAxisLabel(String label) {
        this.yAxisLabel = label;
        return this;
    }

    public ChartConfig showLegend(boolean show) {
        this.showLegend = show;
        return this;
    }

    public ChartConfig hideLegend() {
        this.showLegend = false;
        return this;
    }

    public ChartConfig showLabels(boolean show) {
        this.showLabels = show;
        return this;
    }

    public ChartConfig show3D(boolean show) {
        this.show3D = show;
        return this;
    }

    public ChartConfig backgroundColor(Color color) {
        this.backgroundColor = color;
        return this;
    }

    public ChartConfig colors(Color... colors) {
        this.seriesColors = colors;
        return this;
    }

    public ChartConfig labelFontSize(float size) {
        this.labelFontSize = size;
        return this;
    }

    public ChartConfig titleFontSize(float size) {
        this.titleFontSize = size;
        return this;
    }

    // ==================== CONFIGURACIONES PREDEFINIDAS ====================

    public static ChartConfig barChart(String title) {
        return new ChartConfig()
                .barVertical()
                .title(title);
    }

    public static ChartConfig pieChart(String title) {
        return new ChartConfig()
                .pie()
                .title(title)
                .size(350f, 300f);
    }

    public static ChartConfig lineChart(String title) {
        return new ChartConfig()
                .line()
                .title(title);
    }

    public static ChartConfig areaChart(String title) {
        return new ChartConfig()
                .area()
                .title(title);
    }

    public static ChartConfig small() {
        return new ChartConfig().size(250f, 200f);
    }

    public static ChartConfig medium() {
        return new ChartConfig().size(400f, 300f);
    }

    public static ChartConfig large() {
        return new ChartConfig().size(550f, 400f);
    }

    // ==================== GETTERS Y SETTERS ====================

    public ChartType getChartType() {
        return chartType;
    }

    public void setChartType(ChartType chartType) {
        this.chartType = chartType;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getXAxisLabel() {
        return xAxisLabel;
    }

    public void setXAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    public String getYAxisLabel() {
        return yAxisLabel;
    }

    public void setYAxisLabel(String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
    }

    public boolean isShowLegend() {
        return showLegend;
    }

    public void setShowLegend(boolean showLegend) {
        this.showLegend = showLegend;
    }

    public boolean isShowLabels() {
        return showLabels;
    }

    public void setShowLabels(boolean showLabels) {
        this.showLabels = showLabels;
    }

    public boolean isShow3D() {
        return show3D;
    }

    public void setShow3D(boolean show3D) {
        this.show3D = show3D;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color[] getSeriesColors() {
        return seriesColors;
    }

    public void setSeriesColors(Color[] seriesColors) {
        this.seriesColors = seriesColors;
    }

    public float getLabelFontSize() {
        return labelFontSize;
    }

    public void setLabelFontSize(float labelFontSize) {
        this.labelFontSize = labelFontSize;
    }

    public float getTitleFontSize() {
        return titleFontSize;
    }

    public void setTitleFontSize(float titleFontSize) {
        this.titleFontSize = titleFontSize;
    }
}

