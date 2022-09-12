package edu.cmu.cs514.hw6.plugin;

import edu.cmu.cs514.hw6.framework.processedData;
import org.icepear.echarts.Scatter;
import org.icepear.echarts.render.Engine;

import java.util.List;

/**
 * Implementation the interface of {@link VisualPlugin}
 * and provide a scatter plot
 */
public class ScatterPlotPlugin implements VisualPlugin {

    /**
     * Use echarts to draw the scatter plot of our processed data
     * @param data processed data before
     * @return a scatter plot regarding the processed data
     */
    @Override
    public String draw(List<processedData> data) {
        String[] xAxis = new String[data.size()];
        Number[] yAxis = new Number[data.size()];
        for (int i = 0; i < data.size(); i++) {
            xAxis[i] = data.get(i).getTimestamp().toString();
            yAxis[i] = data.get(i).getScore();
        }
        Scatter scatter = new Scatter()
                .setLegend()
                .setTooltip("item")
                .setTitle("Scatter Plot")
                .addXAxis(xAxis)
                .addYAxis()
                .addSeries("Sentiment Score", yAxis);
        Engine engine = new Engine();
        String jsonStr = engine.renderJsonOption(scatter);
        return jsonStr;
    }

}
