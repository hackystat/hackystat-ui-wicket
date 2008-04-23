package org.hackystat.projectbrowser.page.popupwindow;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * A panel that contains a popup window.
 * @author Shaoxuan Zhang
 */
public class PopupWindowPanel extends Panel {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** The modal window inside this panel. */
  private final ModalWindow modalWindow;
  /** The link that bring up the popup window. */
  private final AjaxLink link;

  /**
   * @param id the wicket component id.
   * @param title the title of the popup window.
   */
  public PopupWindowPanel(String id, String title) {
    super(id);
    modalWindow = new ModalWindow("modalWindow");
    add(modalWindow);

    modalWindow.setCookieName("modalWindow");
    modalWindow.setTitle(title);

    modalWindow.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;
        public boolean onCloseButtonClicked(AjaxRequestTarget target) {
            return true;
        }
    });
    link = new AjaxLink("showModalWindow") {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;
        public void onClick(AjaxRequestTarget target) {
          modalWindow.show(target);
        }
    };
    link.add(new Image("icon", 
   new ResourceReference(PopupWindowPanel.class, "question.gif")));
    add(link);
  }

  /**
   * @return the modalWindow
   */
  public ModalWindow getModalWindow() {
    return modalWindow;
  }

}
