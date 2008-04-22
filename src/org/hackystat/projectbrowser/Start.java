package org.hackystat.projectbrowser;

import org.apache.wicket.protocol.http.WicketServlet;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

/**
 * Provides a mechanism for running Project Browser with Jetty.
 * 
 * @author Philip Johnson
 */
public class Start {

  /**
   * Starts up Jetty and points it at Wicket.
   * Jetty will check every five seconds for keyboard input from the console, and if it gets some,
   * it will shutdown.
   * @param args Ignored.
   * @throws Exception If things go wrong. 
   */
  public static void main(String[] args) throws Exception {
    ProjectBrowserProperties properties = new ProjectBrowserProperties();
    int port = properties.getPort();
    String contextPath = properties.getContextRoot();
    Server server = new Server(port);
    Context context = new Context(server, "/" + contextPath, Context.SESSIONS);

    ServletHolder servletHolder = new ServletHolder(new WicketServlet());
    servletHolder.setInitParameter("applicationClassName",
        "org.hackystat.projectbrowser.ProjectBrowserApplication");
    servletHolder.setInitOrder(1);
    context.addServlet(servletHolder, "/*");
    try {
      server.start();
      System.out.println(properties.getHost() + " is running. Press return to stop server.");
      while (System.in.available() == 0) {
        Thread.sleep(5000);
      }
      server.stop();
      server.join();
    }
    catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}
