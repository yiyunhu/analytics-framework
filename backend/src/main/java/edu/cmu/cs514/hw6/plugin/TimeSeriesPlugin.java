package edu.cmu.cs514.hw6.plugin;

import edu.cmu.cs514.hw6.framework.SchoolFramework;
import edu.cmu.cs514.hw6.framework.processedData;
import org.icepear.echarts.Line;
import org.icepear.echarts.render.Engine;

import java.util.List;

/**
 * Implementation the interface of {@link VisualPlugin}
 * and provide time series drawing
 */
public class TimeSeriesPlugin implements VisualPlugin {

    /**
     * Use echarts to draw the line chart of our processed data
     * @param data processed data before
     * @return a line chart regarding the processed data
     */
    @Override
    public String draw(List<processedData> data) {
        String[] xAxis = new String[data.size()];
        Number[] yAxis = new Number[data.size()];
        for (int i = 0; i < data.size(); i++) {
            xAxis[i] = data.get(i).getTimestamp().toString();
            yAxis[i] = data.get(i).getScore();
        }
        Line lineChart = new Line()
                .setLegend()
                .setTooltip("item")
                .setTitle("Line Chart")
                .addXAxis(xAxis)
                .addYAxis()
                .addSeries("Sentiment Score",yAxis);
        Engine engine = new Engine();
        String jsonStr = engine.renderJsonOption(lineChart);
        return jsonStr;

    }

}
