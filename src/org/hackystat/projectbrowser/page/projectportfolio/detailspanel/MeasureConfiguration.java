package org.hackystat.projectbrowser.page.projectportfolio.detailspanel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Configuration for project portfolio measures.
 * 
 * @author Shaoxuan Zhang
 * 
 */
public class MeasureConfiguration implements Serializable {
  /** Support serialization. */
  private static final long serialVersionUID = -6799287509742157998L;
  
  /** The parameter separator. */
  private static final String PARAMETER_SEPARATOR = ",";

  /** The name of the measure. */
  private String measureName;
  /** If this measure is colorable. */
  private boolean colorable;
  /** If this measure is enabled. */
  private boolean enabled = true;

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

  /**
   * Create an instance with default colors green yellow red. higher color will be green if
   * isHigherTheBetter is true, otherwise will be red. lower color will be red if isHigherTheBetter
   * is true, otherwise will be green.
   * 
   * @param name The name of the measure.
   * @param colorable If this measure is colorable.
   * @param higherThreshold The threshold of high value.
   * @param lowerThreshold The threshold of high value.
   * @param higherBetter If higher value means better.
   * @param dataModel The data model this measure belongs to.
   */
  public MeasureConfiguration(String name, boolean colorable, double lowerThreshold,
      double higherThreshold, boolean higherBetter, ProjectPortfolioDataModel dataModel) {
    this.measureName = name;
    this.colorable = colorable;
    this.lowerThreshold = lowerThreshold;
    this.higherThreshold = higherThreshold;
    this.higherBetter = higherBetter;
    this.dataModel = dataModel;
  }

  /**
   * Load configuration from a PortfolioMeasure object.
   * @param measure the PortfolioMeasure object.
   */
  public void loadFrom(PortfolioMeasure measure) {
    this.colorable = measure.isColorable();
    this.enabled = measure.isEnabled();
    this.higherBetter = measure.isHigherBetter();
    this.higherThreshold = measure.getHigherThreshold();
    this.lowerThreshold = measure.getLowerThreshold();
    String parametersString = measure.getParameters();
    this.parameters.clear();
    for (String parameter : parametersString.split(PARAMETER_SEPARATOR)) {
      this.parameters.add(new Model(parameter));
    }
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
   * @param measureName the measureName to set
   */
  public void setMeasureName(String measureName) {
    this.measureName = measureName;
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
}
