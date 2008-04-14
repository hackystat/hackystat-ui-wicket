package org.hackystat.projectbrowser.imageurl;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.Model;

/**
 * Provides a component for displaying a URL in an image tag. Taken from:
 * http://cwiki.apache.org/WICKET/how-to-load-an-external-image.html
 * 
 * @author Philip Johnson
 * 
 */
public class ImageUrl extends WebComponent {

  /** The serialization id. */
  private static final long serialVersionUID = 1L;

  /**
   * Construct the component for displaying an image URL.
   * 
   * @param id The wicket:id.
   * @param imageUrl The URL to display.
   */
  public ImageUrl(String id, String imageUrl) {
    super(id);
    add(new AttributeModifier("src", true, new Model(imageUrl)));
    setVisible(!(imageUrl == null || imageUrl.equals("")));
  }

  /**
   * How to replace the markup.
   * @param tag The wicket:id.
   */
  @Override
  protected void onComponentTag(ComponentTag tag) {
    super.onComponentTag(tag);
    checkComponentTag(tag, "img");
  }

}