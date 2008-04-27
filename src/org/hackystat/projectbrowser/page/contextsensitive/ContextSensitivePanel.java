package org.hackystat.projectbrowser.page.contextsensitive;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

/**
 * Provides a panel containing a set of label/drop-down menu pairs, each of whose visibility 
 * can be controlled via Ajax callbacks. 
 * @author Philip Johnson
 */
public class ContextSensitivePanel extends Panel {

  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** The list of ContextSensitiveMenus, each containing a label and drop-down menu. */
  private List<ContextSensitiveMenu> menus;
  
  /**
   * Create the ContextSensitivePanel with the supplied ContextSensitiveMenus.
   * @param id The wicket:id for this panel.
   * @param menus The list of ContextSensitiveMenus.
   */
  public ContextSensitivePanel(String id, List<ContextSensitiveMenu> menus) {
    super(id);
    this.setOutputMarkupPlaceholderTag(true);
    this.menus = menus;
    add(new ListView("ContextSensitiveMenus", new PropertyModel(this, "menus")) {

      /** Support serialization. */
     private static final long serialVersionUID = 1L;

     /**
      * Used to generate the list of label/menu pairs.
      */
      @Override
      protected void populateItem(ListItem item) {
        ContextSensitiveMenu menu = (ContextSensitiveMenu) item.getModelObject();
        Label label = new Label("ContextSensitiveMenuLabel", menu.getName());
        label.setVisible(menu.isVisible());
        label.setOutputMarkupPlaceholderTag(true);
        item.add(label);
        DropDownChoice ddMenu = 
          new DropDownChoice ("ContextSensitiveMenu", 
              new PropertyModel(menu, "selectedValue"),
              new PropertyModel(menu, "values"));
        ddMenu.setRequired(false);
        ddMenu.setVisible(menu.isVisible());
        ddMenu.setOutputMarkupPlaceholderTag(true);
        item.add(ddMenu);
      }
    });
  }
  
  /**
   * Gets the list of ContextSensitiveMenus associated with this panel.
   * @return The list of ContextSensitiveMenus.
   */
  public List<ContextSensitiveMenu> getMenus() {
    return this.menus;
  }
  
  /**
   * Returns the ContextSensitiveMenu with the passed name, or null if not found.
   * @param name The menu name to search for.
   * @return The menu, or null if not found.
   */
  public ContextSensitiveMenu getMenu(String name) {
    for (ContextSensitiveMenu menu : this.menus) {
      if (menu.getName().equals(name)) {
        return menu;
      }
    }
    return null;
  }
  
  /**
   * Makes the specified ContextSensitiveMenu names visible. All others in the panel will be 
   * invisible.
   * @param target The Component displaying this Panel.
   * @param names The list of ContextSensitiveMenu names that should be made visible.
   */
  public void setVisible(AjaxRequestTarget target, String... names) {
    List<String> namesList = Arrays.asList(names);
    for (ContextSensitiveMenu menu : menus) {
      menu.setVisible(namesList.contains(menu.getName()));
      }
    target.addComponent(this);
  }
}
