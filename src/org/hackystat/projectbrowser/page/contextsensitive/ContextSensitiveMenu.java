package org.hackystat.projectbrowser.page.contextsensitive;

import java.io.Serializable;
import java.util.List;

/**
 * Provides state information for an individual label/menu pair that appears within a 
 * ContextSensitivePanel.
 * 
 * @author Philip Johnson
 */
public class ContextSensitiveMenu implements Serializable {
  
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  
  /** The value this user has selected. */
  private String selectedValue;
  
  /** The list of valuesType possibilities. */
  private List<String> values;
  
  /** The name of this menu. */
  private String name;
  
  /** Whether this menu should be visible or not. */
  private boolean isVisible = true;

  
  /**
   * Creates this context sensitive menu.
   * @param defaultValue The initial selected value, or else null for no selected value. 
   * @param values The list of values to appear. 
   * @param name The label to be associated with this pane.  This label is also used to 
   * identify the menu, so it should be unique within the enclosing ContextSensitivePanel.
   * @param isVisible True if this pane should start out being visible or not visible.
   */
  public ContextSensitiveMenu(String name, String defaultValue, List<String> values, 
      boolean isVisible) {
    this.selectedValue = defaultValue;
    this.values = values;
    this.name = name;
    this.isVisible = isVisible;
  }
  
  /**
   * Returns the currently selected value of this menu, or null if none selected.
   * @return The selected value.
   */
  public String getSelectedValue() {
    return this.selectedValue;
  }
  
  /**
   * Sets the selected value associated with this menu.
   * @param value The new selected value.
   */
  public void setSelectedValue(String value) {
    this.selectedValue = value;
  }
  
  /**
   * Returns the list of strings to be displayed in the menu.
   * @return The values list. 
   */
  public List<String> getValues() {
    return this.values;
  }
  
  
  /**
   * True if this label/menu pair should be displayed. 
   * @return True if displayed.
   */
  public boolean isVisible() {
    return this.isVisible;
  }
  
  /**
   * Sets whether this label/menu pair should be displayed.
   * @param visibility True if displayed.
   */
  public void setVisible(boolean visibility) {
    this.isVisible = visibility;
  }
  
  /**
   * Returns the name of this label/menu pair. Should be unique within a panel.
   * @return The name. 
   */
  public String getName() {
    return this.name;
  }
}
