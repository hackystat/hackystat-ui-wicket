package org.hackystat.projectbrowser.page.todate.detailpanel;

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
public class ToDateMeasureConfiguration implements Serializable {
  /** Support serialization. */
  private static final long serialVersionUID = -6799287509742157998L;
  
  /** The parameter separator. */
  private static final String PARAMETER_SEPARATOR = ",";

  /** The name of the measure. */
  private String measureName;
  /** The alias of this measure, which will be used as display name. */
  private String alias;
  /** If this measure is enabled. */
  private boolean enabled = true;

  /** The data model this measure belongs to. */
  private final ToDateDataModel dataModel;

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
   * @param dataModel The data model this measure belongs to.
   */
  public ToDateMeasureConfiguration(String name, ToDateDataModel dataModel) {
    this.measureName = name;
    this.dataModel = dataModel;
  }

  /**
   * Create an instance with alias.
   * 
   * @param name The name of the measure.
   * @param alias The alias of this measure, which will be used as display name.
   * @param dataModel The data model this measure belongs to.
   */
  public ToDateMeasureConfiguration(String name, String alias, ToDateDataModel dataModel) {
    this(name, dataModel);
    this.alias = alias;
  }

  
  /**
   * Load configuration from a PortfolioMeasure object.
   * @param measure the PortfolioMeasure object.
   */
  public void loadFrom(ToDateMeasure measure) {
    this.setEnabled(measure.isEnabled());
    String parametersString = measure.getParameters();
    this.parameters.clear();
    for (String parameter : parametersString.split(PARAMETER_SEPARATOR)) {
      this.parameters.add(new Model(parameter));
    }
  }

  /**
   * @return the measureName
   */
  public String getName() {
    return measureName;
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
   * @return the dataModel
   */
  public ToDateDataModel getDataModel() {
    return dataModel;
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

}
