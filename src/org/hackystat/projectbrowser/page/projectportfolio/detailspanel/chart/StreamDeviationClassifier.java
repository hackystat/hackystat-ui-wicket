package org.hackystat.projectbrowser.page.projectportfolio.detailspanel.chart;

import java.io.Serializable;
import java.util.List;
import org.apache.wicket.markup.html.panel.Panel;
import org.hackystat.projectbrowser.page.projectportfolio.jaxb.Measures.Measure;
import org.hackystat.projectbrowser.page.projectportfolio.jaxb.Measures.Measure.DeviationParameters;

/**
 * Classify the trend according to its deviation. Higher deviation is worse.
 * Expectation value is not considered when coloring trend.
 * Value is colored according to its deviation to the expectation value.
 * 
 * @author Shaoxuan
 *
 */
public class StreamDeviationClassifier implements Serializable, StreamClassifier {

  /** Support serialization. */
  private static final long serialVersionUID = 2104163143505110271L;
  /** Name of this classifier. */
  public static final String name = "Deviation";

  /** The deviation within which the trend is considered healthy. */
  private double moderateDeviation;
  /** The deviation beyond which the trend is considered unhealthy. */
  private double unacceptableDeviation;
  /** The expectation value. */
  private double expectationValue;
  /** If the condition scale with granularity. */
  private boolean scaleWithGranularity;
  /**
   * 
   * @param moderateDeviation The deviation within which the trend is considered healthy.
   * @param unacceptableDeviation The deviation beyond which the trend is considered unhealthy.
   * @param expectationValue The expectation value.
   * @param scaleWithGranularity If the condition will scale with granularity.
   */
  public StreamDeviationClassifier(double moderateDeviation, double unacceptableDeviation, 
      double expectationValue, boolean scaleWithGranularity) {
    this.moderateDeviation = moderateDeviation;
    this.unacceptableDeviation = unacceptableDeviation;
    this.expectationValue = expectationValue;
    this.scaleWithGranularity = scaleWithGranularity;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Panel getConfigurationPanel(String id) {
    return new StreamDeviationClassifierConfigurationPanel(id, this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PortfolioCategory getStreamCategory(MiniBarChart chart) {
    double moderateDeviation = this.moderateDeviation;
    double unacceptableDeviation = this.unacceptableDeviation;
    if ("Week".equals(chart.granularity)) {
      moderateDeviation *= 7;
    }
    else if ("Month".equals(chart.granularity)) {
      moderateDeviation *= 30;
    }
    List<Double> streamData = chart.streamData;
    double sum = 0;
    double sumSqt = 0;
    for (int i = 0; i < streamData.size(); i++) {
      if (streamData.get(i).isNaN() || streamData.get(i) < 0) {
        streamData.remove(i);
        i--;
      }
      else {
        double d = streamData.get(i);
        sum += d;
        sumSqt += d * d;
      }
    }
    int size = streamData.size();
    if (size > 0) {
      //compute deviation
      double deviation = (sumSqt - (sum * sum) / size) / size;
      deviation = Math.sqrt(deviation);
      if (deviation <= moderateDeviation) {
        return PortfolioCategory.GOOD;
      }
      else if (deviation <= unacceptableDeviation) {
        return PortfolioCategory.AVERAGE;
      }
      else {
        return PortfolioCategory.POOR;
      }
    }
    return PortfolioCategory.NA;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PortfolioCategory getValueCategory(double value) {
    if (value < 0) {
      return PortfolioCategory.NA;
    }
    double deviation = Math.abs(value - this.expectationValue);
    if (deviation <= this.moderateDeviation) {
      return PortfolioCategory.GOOD;
    }
    else if (deviation <= this.unacceptableDeviation) {
      return PortfolioCategory.AVERAGE;
    }
    else {
      return PortfolioCategory.POOR;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void saveSetting(Measure measure) {
    DeviationParameters param = new DeviationParameters();
    param.setUnacceptableDeviation(this.unacceptableDeviation);
    param.setModerateDeviation(this.moderateDeviation);
    param.setExpectationValue(this.expectationValue);
    param.setScaleWithGranularity(this.scaleWithGranularity);
    measure.setDeviationParameters(param);
  }

}
