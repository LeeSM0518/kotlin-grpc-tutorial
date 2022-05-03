import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.io.IOException;

public class Profiler extends ApplicationFrame {

  private static final long serialVersionUID = 1L;

  public interface Timeable {
    void setup(int n);

    void timeMe(int n) throws IOException;
  }

  private Timeable timeable;

  public Profiler(String title, Timeable timeable) {
    super(title);
    this.timeable = timeable;
  }

  public XYSeries timingLoop(int startN, int endMillis) throws IOException {
    final XYSeries series = new XYSeries("Time (ms)");

    int n = startN;
    for (int i = 0; i < 20; i++) {
      // run it once to warm up
      timeIt(n);

      // then start timing
      long total = 0;

      // run 10 times and add up total runtime
      for (int j = 0; j < n; j++) {
        total += timeIt(n);
      }
      System.out.println(n + ", " + total);

      // don't store data until we get to 4ms
      if (total > 4) {
        series.add(n, total);
      }

      // stop when the runtime exceeds the end threshold
      if (total > endMillis) {
        break;
      }
      // otherwise double the size and continue
      n *= 2;
    }
    return series;
  }

  public long timeIt(int n) throws IOException {
    timeable.setup(n);
    final long startTime = System.currentTimeMillis();
    timeable.timeMe(n);
    final long endTime = System.currentTimeMillis();
    return endTime - startTime;
  }

  public void plotResults(XYSeries series) {
    double slope = estimateSlope(series);
    System.out.println("Estimated slope= " + slope);

    final XYSeriesCollection dataset = new XYSeriesCollection();
    dataset.addSeries(series);

    final JFreeChart chart = ChartFactory.createXYLineChart(
        "",          // chart title
        "",               // domain axis label
        "",                  // range axis label
        dataset,                  // data
        PlotOrientation.VERTICAL,
        false,                     // include legend
        true,
        false
    );

    final XYPlot plot = chart.getXYPlot();
    final NumberAxis domainAxis = new LogarithmicAxis("The number of requests (n)");
    final NumberAxis rangeAxis = new LogarithmicAxis("Runtime (ms)");
    plot.setDomainAxis(domainAxis);
    plot.setRangeAxis(rangeAxis);
    chart.setBackgroundPaint(Color.white);
    plot.setOutlinePaint(Color.black);
    final ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new Dimension(1000, 600));
    setContentPane(chartPanel);
    pack();
    RefineryUtilities.centerFrameOnScreen(this);
    setVisible(true);
  }

  public double estimateSlope(XYSeries series) {
    SimpleRegression regression = new SimpleRegression();

    for (Object item : series.getItems()) {
      XYDataItem xy = (XYDataItem) item;
      regression.addData(Math.log(xy.getXValue()), Math.log(xy.getYValue()));
    }
    return regression.getSlope();
  }
}
