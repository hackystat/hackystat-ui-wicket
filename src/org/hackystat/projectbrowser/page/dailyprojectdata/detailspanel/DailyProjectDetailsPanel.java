package org.hackystat.projectbrowser.page.dailyprojectdata.detailspanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * A panel that contains a link representing some DPD information. When clicked, the link generates
 * a popup window with further details about the DPD information.
 * 
 * @author Philip Johnson
 * @author Shaoxuan Zhang
 */
public class DailyProjectDetailsPanel extends Panel {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** The modal window inside this panel. */
  private final ModalWindow modalWindow;

  /**
   * Provides a panel that displays details about a portion of the DailyProjectData. The panel
   * consists of a link which, when clicked, will display a modal window with details about the data
   * represented by the link. After creating this panel, you can call getModalWindow() to obtain the
   * model window instance and set its content.
   * 
   * @param id The wicket component id.
   * @param title The title of the window.
   * @param linkLabel The label to be associated with the link.
   */
  public DailyProjectDetailsPanel(String id, String title, String linkLabel) {
    super(id);
    // Create, configure, and add the modal window to this panel.
    this.modalWindow = new ModalWindow("modalWindow");
    add(modalWindow);

    this.modalWindow.setCookieName("modalWindow");
    this.modalWindow.setTitle(title);

    this.modalWindow.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      public boolean onCloseButtonClicked(AjaxRequestTarget target) {
        return true;
      }
    });
    // Create, configure, and add the link that displays the modal window to this panel.
    AjaxLink link = new AjaxLink("showModalWindow") {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;

      @Override
      public void onClick(AjaxRequestTarget target) {
        modalWindow.show(target);
      }
    };
    link.add(new Label("detailsLink", linkLabel));
    add(link);
  }

  /**
   * @return the modalWindow
   */
  public ModalWindow getModalWindow() {
    return modalWindow;
  }

}
