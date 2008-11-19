package org.hackystat.projectbrowser.page.projectportfolio.detailspanel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.hackystat.projectbrowser.page.projectportfolio.detailspanel.chart.
         EnhancedStreamTrendClassifier;
import org.hackystat.projectbrowser.page.projectportfolio.detailspanel.chart.
         SimpleStreamTrendClassifier;
import org.hackystat.projectbrowser.page.projectportfolio.detailspanel.chart.StreamTrend;

/**
 * Configuration for project portfolio measures.
 * 
 * @author Shaoxuan Zhang
 * 
 */
public class PortfolioMeasureConfiguration implements Serializable {
  /** Support serialization. */
  private static final long serialVersionUID = -6799287509742157998L;
  
  /** The parameter separator. */
  private static final String PARAMETER_SEPARATOR = ",";

  /** The name of the measure. */
  private String measureName;
  /** The alias of this measure, which will be used as display name. */
  private String alias;
  /** If this measure is colorable. */
  private boolean colorable;
  /** If this measure is enabled. */
  private boolean enabled = true;
  /** The method to merge multiple streams. */
  private String merge;

  /** If higher value means better. */
  private boolean higherBetter;
  
  /** The threshold of high value. */
  private double higherThreshold;
  /** The threshold of low value. */
  private double lowerThreshold;

  /** The data model this measure belongs to. */
  private final ProjectPortfolioDataModel dataModel;

  /** This meausre's parameter list. */
  //private List<String> parameters = new ArrayList<String>();
  /** The parameters for telemetry chart. */
  private final List<IModel> parameters = new ArrayList<IModel>();
  
  /** The simple stream trend classifier.*/
  private SimpleStreamTrendClassifier streamTrendClassifier = new EnhancedStreamTrendClassifier();

  /**
   * Create an instance.
   * 
   * @param name The name of the measure.
   * @param alias The alias of this measure, which will be used as display name.
   * @param colorable If this measure is colorable.
   * @param higherThreshold The threshold of high value.
   * @param lowerThreshold The threshold of high value.
   * @param higherBetter If higher value means better.
   * @param merge The method to merge multiple streams. Can be sum, avg, min or max.
   * @param dataModel The data model this measure belongs to.
   */
  public PortfolioMeasureConfiguration(String name, String alias, boolean colorable, 
      double lowerThreshold, double higherThreshold, boolean higherBetter, String merge, 
      ProjectPortfolioDataModel dataModel) {
    this.measureName = name;
    this.colorable = colorable;
    this.lowerThreshold = lowerThreshold;
    this.higherThreshold = higherThreshold;
    this.higherBetter = higherBetter;
    this.alias = alias;
    this.merge = merge;
    this.dataModel = dataModel;
  }
  
  /**
   * Load configuration from a PortfolioMeasure object.
   * @param measure the PortfolioMeasure object.
   */
  public void loadFrom(PortfolioMeasure measure) {
    this.setColorable(measure.isColorable());
    this.setEnabled(measure.isEnabled());
    this.setHigherBetter(measure.isHigherBetter());
    this.setHigherThreshold(measure.getHigherThreshold());
    this.setLowerThreshold(measure.getLowerThreshold());
    String parametersString = measure.getParameters();
    this.parameters.clear();
    for (String parameter : parametersString.split(PARAMETER_SEPARATOR)) {
      this.parameters.add(new Model(parameter));
    }
  }

  /**
   * Parse the list of data and produce a StreamTrend result.
   * @param stream the input list of data
   * @return StreamTrend enumeration. 
   */
  public StreamTrend getStreamTrend(List<Double> stream) {
    return this.streamTrendClassifier.getStreamTrend(stream);
  }
  
  /**
   * @param higherThreshold the higherThreshold to set
   */
  public void setHigherThreshold(double higherThreshold) {
    this.higherThreshold = higherThreshold;
  }

  /**
   * @return the higherThreshold
   */
  public double getHigherThreshold() {
    return higherThreshold;
  }

  /**
   * @param lowerThreshold the lowerThreshold to set
   */
  public void setLowerThreshold(double lowerThreshold) {
    this.lowerThreshold = lowerThreshold;
  }

  /**
   * @return the lowerThreshold
   */
  public double getLowerThreshold() {
    return lowerThreshold;
  }

  /**
   * @return the measureName
   */
  public String getName() {
    return measureName;
  }

  /**
   * @param colorable the colorable to set
   */
  public void setColorable(boolean colorable) {
    this.colorable = colorable;
  }

  /**
   * @return the colorable
   */
  public boolean isColorable() {
    return colorable;
  }

  /**
   * @param enabled the enable to set
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * @return the enable
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Return the color for higher value or increasing trend.
   * @return the color
   */
  public String getHigherColor() {
    if (this.isHigherBetter()) {
      return dataModel.getGoodColor();
    }
    else {
      return dataModel.getBadColor();
    }
  }
  
  /**
   * Return the color for middle value or unstable trend.
   * @return the color
   */
  public String getMiddleColor() {
    return dataModel.getSosoColor();
  }

  /**
   * Return the color for lower value or decreasing trend.
   * @return the color
   */
  public String getLowerColor() {
    if (this.isHigherBetter()) {
      return dataModel.getBadColor();
    }
    else {
      return dataModel.getGoodColor();
    }
  }

  /**
   * Return the color for stable trend.
   * @return the color
   */
  public String getStableColor() {
    return dataModel.getGoodColor();
  }
  
  /**
   * Return the color for unstable trend.
   * @return the color
   */
  public String getUnclassifiedTrendColor() {
    return dataModel.getSosoColor();
  }

  /**
   * Return the parameters in a single String.
   * 
   * @return parameters separated by ',' in a single String.
   */
  public String getParamtersString() {
    if (this.parameters.isEmpty()) {
      return "";
    }
    StringBuffer param = new StringBuffer();
    for (int i = 0; i < this.parameters.size(); ++i) {
      IModel model = this.parameters.get(i);
      if (model != null) {
        param.append(model.getObject());
        if (i < this.parameters.size() - 1) {
          param.append(PARAMETER_SEPARATOR);
        }
      }
    }
    return param.toString();
  }

  /**
   * @return the parameters
   */
  public List<IModel> getParameters() {
    return this.parameters;
  }

  /**
   * @param higherBetter the higherBetter to set
   */
  public void setHigherBetter(boolean higherBetter) {
    this.higherBetter = higherBetter;
  }

  /**
   * @return the higherBetter
   */
  public boolean isHigherBetter() {
    return higherBetter;
  }

  /**
   * @return the dataModel
   */
  public ProjectPortfolioDataModel getDataModel() {
    return dataModel;
  }

  /**
   * @param streamTrendClassifier the streamTrendClassifier to set
   */
  public void setStreamTrendClassifier(SimpleStreamTrendClassifier streamTrendClassifier) {
    this.streamTrendClassifier = streamTrendClassifier;
  }

  /**
   * @return the streamTrendClassifier
   */
  public SimpleStreamTrendClassifier getStreamTrendClassifier() {
    return streamTrendClassifier;
  }

  /**
   * @param alias the alias to set
   */
  public void setAlias(String alias) {
    this.alias = alias;
  }

  /**
   * @return the alias
   */
  public String getAlias() {
    return alias;
  }
  
  /**
   * Return the display name of this measure.
   * If alias is available, alias will be return. Otherwise, measure name will be return.
   * @return the display name.
   */
  public String getDisplayName() {
    if (this.alias != null && this.alias.length() > 0) {
      return this.getAlias();
    }
    return this.getName();
  }

  /**
   * @param merge the merge to set
   */
  public void setMerge(String merge) {
    this.merge = merge;
  }

  /**
   * @return the merge
   */
  public String getMerge() {
    return merge;
  }
}
