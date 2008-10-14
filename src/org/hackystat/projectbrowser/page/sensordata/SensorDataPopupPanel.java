package org.hackystat.projectbrowser.page.sensordata;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.hackystat.projectbrowser.ProjectBrowserSession;

/**
 * A panel that contains a popup window.
 * This one differs from PopupWindowPanel because the link is a regular text label, not an img.
 * Since the HTML markup page is slightly different, it's not clear to me how we could 
 * avoid these two classes that are almost identical except for the link label creation code. 
 * @author Shaoxuan Zhang
 * @author Philip Johnson
 */
public class SensorDataPopupPanel extends Panel {
  /** Support serialization. */
  private static final long serialVersionUID = 1L;
  /** The modal window inside this panel. */
  private final ModalWindow modalWindow;
  /** The link that bring up the popup window. */
  private long start;
  private String sdt;
  private String tool;
  
  /**
   * A panel containing sensor data information for the given tool and type.
   * @param id the wicket component id.
   * @param title the title of the popup window.
   * @param linkLabel The label to go in the link that pops up this panel.
   * @param sdtName The name of the SDT.
   * @param toolName The name of the tool that generated this sensor data. 
   * @param startTime The start time for this data. 
   */
  public SensorDataPopupPanel(String id, String title, String linkLabel, String sdtName, 
      String toolName, long startTime) {
    super(id);
    this.sdt = sdtName;
    this.tool = toolName;
    this.start = startTime;
    
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
    AjaxLink link = new AjaxLink("showModalWindow") {
      /** Support serialization. */
      private static final long serialVersionUID = 1L;
        @Override
        public void onClick(AjaxRequestTarget target) {
          SensorDataSession session = ProjectBrowserSession.get().getSensorDataSession();
          session.getSensorDataDetailsProvider().setSensorDataDetailsProvider(sdt, tool, start);
          session.setSdtName(sdt);
          session.setTool(tool);
          modalWindow.setContent(new SensorDataDetailsPanel(modalWindow.getContentId()));
          modalWindow.show(target);
        }
    };
    link.add(new Label("LinkLabel", linkLabel)); 
    add(link);
  }

  /**
   * @return the modalWindow
   */
  public ModalWindow getModalWindow() {
    return modalWindow;
  }

}
