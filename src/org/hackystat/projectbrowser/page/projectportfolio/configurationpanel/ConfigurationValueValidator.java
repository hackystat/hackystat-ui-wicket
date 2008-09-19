package org.hackystat.projectbrowser.page.projectportfolio.configurationpanel;

import java.util.Map;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

/**
 * Validator that valid values in configuration form.
 * Make sure the higher value is no smaller than the lower value.
 * @author Shaoxuan Zhang
 *
 */
public class ConfigurationValueValidator extends AbstractFormValidator {
  /** Support serialization. */
  private static final long serialVersionUID = -1148173388419682530L;
  /** The TextField with higher value to validate. */
  private final TextField higherValueTextField;
  /** The TextField with lower value validate. */
  private final TextField lowerValueTextField;
  /** The name of the measure the two values belong to. */
  private final String measureName;
  
  /**
   * @param measureName The name of the measure the two values belong to.
   * @param higherValueTextField The TextField with higher value.
   * @param lowerValueTextField The TextField with lower value.
   */
  public ConfigurationValueValidator(String measureName, TextField higherValueTextField,
      TextField lowerValueTextField) {
    if (higherValueTextField == null) {
      throw new IllegalArgumentException("Higher value TextField cannot be null");
    }
    if (lowerValueTextField == null) {
      throw new IllegalArgumentException("Lower value TextField cannot be null");
    }
    this.measureName = measureName;
    this.higherValueTextField = higherValueTextField;
    this.lowerValueTextField = lowerValueTextField;
  }

  /**
   * @return array of FormComponents that this validator depends on
   */
  public FormComponent[] getDependentFormComponents() {
    return new FormComponent[]{higherValueTextField, lowerValueTextField};
  }
  
  /**
   * This method is ran if all components returned by getDependentFormComponents() are valid.
   * @param form - form this validator is added to
   */
  public void validate(Form form) {
    double higherValue = Double.valueOf(higherValueTextField.getInput().replaceAll(",", ""));
    double lowerValue = Double.valueOf(lowerValueTextField.getInput().replaceAll(",", ""));
    if (higherValue <= lowerValue) {
      error(higherValueTextField, "HigherValueSmallerThanLowerValue");
    }
  }

  /**
   * Gets the default variables for interpolation.
   * @return a map with the variables for interpolation
   */
  @Override 
  @SuppressWarnings("unchecked")
  protected Map variablesMap() {        
      Map map = super.variablesMap();               
      // add keys and values for interpolation in error message 
      map.put("measure", measureName);        
      map.put("higherField", higherValueTextField.getId());
      map.put("lowerField", lowerValueTextField.getId());
      return map;                                               
  } 

}
