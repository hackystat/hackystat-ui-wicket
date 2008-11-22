package org.hackystat.projectbrowser.page.projectportfolio.detailspanel.chart;

import org.apache.wicket.markup.html.panel.Panel;
import org.hackystat.projectbrowser.page.projectportfolio.jaxb.Measures.Measure;

/**
 * Interface of stream classifier.
 * 
 * @author Shaoxuan Zhang
 *
 */
public interface StreamClassifier {

  /**
   * Parse the given MiniBarChart and produce a {@link PortfolioCategory} result.
   * @param chart the input chart
   * @return StreamCategory enumeration. 
   */
  public PortfolioCategory getStreamCategory(MiniBarChart chart);
  
  /**
   * Return the panel for users to configure this classifier.
   * @param id The Wicket component id.
   * @return a Panel
   */
  public Panel getConfigurationPanel(String id);
  
  /**
   * Parse the given value and produce a {@link PortfolioCategory} result.
   * @param value a double value.
   * @return a {@link PortfolioCategory}.
   */
  public PortfolioCategory getValueCategory(double value);

  /**
   * @return the name of this classifier.
   */
  public String getName();


  /**
   * Save classifier's setting into the given {@link Measure} instance.
   * @param measure the given {@link Measure} instance
   */
  public void saveSetting(Measure measure);
}
