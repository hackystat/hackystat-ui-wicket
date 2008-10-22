package org.hackystat.projectbrowser.test;

import java.util.Properties;
import javax.xml.datatype.XMLGregorianCalendar;
import org.hackystat.projectbrowser.ProjectBrowserProperties;
import org.junit.BeforeClass;
import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.sensorbase.client.SensorBaseClientException;
import org.hackystat.sensorbase.client.SensorBaseClient.InvitationReply;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.sensorbase.resource.projects.jaxb.ProjectRef;
import org.hackystat.simdata.SimData;
import org.hackystat.utilities.tstamp.Tstamp;

/**
 * Supports JUnit testing of the ProjectBrowser by creating test instances of the sensorbase,
 * dailyprojectdata, and telemetry services. 
 * @author Philip Johnson
 *
 */
public class ProjectBrowserTestHelper {
  /** The Sensorbase server used in these tests. */
  private static org.hackystat.sensorbase.server.Server sensorbaseServer;
  /** The DailyProjectData server used in these tests. */
  private static org.hackystat.dailyprojectdata.server.Server dpdServer;  
  /** The Telemetry server used in these tests. */
  private static org.hackystat.telemetry.service.server.Server telemetryServer;  

  /** The test domain for all users in this simulation. */
  private static final String testdomain = "@hackystat.org";

  /**
   * Constructor.
   */
  public ProjectBrowserTestHelper () {
    // Does nothing.
  }
  
  /** 
   * Starts the servers going for these tests. 
   * @throws Exception If problems occur setting up the server. 
   */
  @BeforeClass 
  public static void setupServer() throws Exception {
    // Create testing versions of the Sensorbase, DPD, and Telemetry servers.
    ProjectBrowserTestHelper.sensorbaseServer = 
      org.hackystat.sensorbase.server.Server.newTestInstance();
    ProjectBrowserTestHelper.dpdServer = 
      org.hackystat.dailyprojectdata.server.Server.newTestInstance(); 
    ProjectBrowserTestHelper.telemetryServer = 
      org.hackystat.telemetry.service.server.Server.newTestInstance();
  }

  /**
   * Returns the hostname associated with this Telemetry test server. 
   * @return The host name, including the context root. 
   */
  protected String getTelemetryHostName() {
    return ProjectBrowserTestHelper.telemetryServer.getHostName();
  }
  
  /**
   * Returns the sensorbase hostname that this Telemetry server communicates with.
   * @return The host name, including the context root. 
   */
  protected String getSensorBaseHostName() {
    return ProjectBrowserTestHelper.sensorbaseServer.getHostName();
  }
  
  /**
   * Returns the sensorbase hostname that this Telemetry server communicates with.
   * @return The host name, including the context root. 
   */
  protected String getDailyProjectDataHostName() {
    return ProjectBrowserTestHelper.dpdServer.getHostName();
  }
  
  /**
   * Returns a Properties instance with the SensorBase, DPD, and Telemetry services 
   * ProjectBrowserProperties set to test values. 
   * @return A Properties instances with the service host names set to test values. 
   */
  protected Properties getTestProperties() {
    Properties testProperties = new Properties();
    testProperties.put(ProjectBrowserProperties.SENSORBASE_HOST_KEY, getSensorBaseHostName());
    testProperties.put(ProjectBrowserProperties.DAILYPROJECTDATA_HOST_KEY, 
        getDailyProjectDataHostName());
    testProperties.put(ProjectBrowserProperties.TELEMETRY_HOST_KEY, getTelemetryHostName());
    testProperties.put(ProjectBrowserProperties.WICKET_CONFIGURATION_KEY, "development");
    return testProperties;
  }

  /**
   * Generate a set of data for testing.
   *      Effort is decrease 1 per day, end with 3.
   *      Size increases by 50 LOC per day, end with 1000.
   *      Complexity decrease by 1 per day, end with 3.
   *      Builds and unit tests success/pass number decrease by 1 per day, end with 2.
   *      Both fail twice a day.
   *      Coverage decrease by 3% per day, end with 60%.
   *      Commits twice a day, churn decrease by 5 per time, 10 per day, end with 20.
   *      Code issues decrease by 2 per day, end with 0.
   * @param user name of the test user. 
   *          DONOT include the "@hackystat.org", this method will add that in.
   * @param projectName name of the test project.
   * @param endTime end time of the test data.
   * @param days number of days before endTime that this data will cover.
   */
  protected void generateSimData(String user, String projectName, 
                                 XMLGregorianCalendar endTime, int days) {
    try {
      
      SimData simData = new SimData(getSensorBaseHostName());
      XMLGregorianCalendar start = Tstamp.incrementDays(endTime, -100);
      XMLGregorianCalendar end = Tstamp.incrementDays(endTime, 60);
      String SUCCESS = "Success";
      String FAILURE = "Failure";
      String PASS = "pass";
      String FAIL = "fail";
      String userDir = "/" + user + "/src/org/hackystat/" + projectName + "/Testing/";
      String userFile = userDir + "testFile.java";
      String LOGPREFIX = "ProjectBrowserTestHelper: Making data for day: ";
      
      simData.makeUser(user);
      simData.makeProject(projectName, user, start, end, "*/" + projectName + "/*");
      
      simData.getLogger().info(LOGPREFIX + endTime);

      for (int i = 0; i < days; ++i) {
        XMLGregorianCalendar day = Tstamp.incrementDays(endTime, -i);
        // Effort is decrease 1 per day, end with 3.
        simData.addDevEvents(user, day, (12 * (3 + i)), userFile);
        
        // Size increases by 50 LOC per day, end with 1000.
        int joeFileSize = 1000 - (i * 50); 
        if (joeFileSize < 100) {
          joeFileSize = 100;
        }
        simData.addFileMetric(user, day, userFile, joeFileSize, day);
        
        // Complexity decrease by 1 per day, end with 3.
        simData.addComplexity(user, day, userFile, joeFileSize, day, 3 + i);
        
        // Builds and unit tests success/pass number decrease by 1 per day, end with 2.
        // Both fail twice a day.
        simData.addBuilds(user, day, userDir, SUCCESS, 2 + i);
        simData.addBuilds(user, day, userDir, FAILURE, 2);
        simData.addUnitTests(user, day, userFile, PASS, 2 + i);
        simData.addUnitTests(user, day, userFile, FAIL, 2);
        
        // Coverage decrease by 3% per day, end with 60%.
        int joeCoverage = 50 + i * 3;
        if (joeCoverage > 95) {
          joeCoverage = 95;
        }
        simData.addCoverage(user, day, userFile, joeCoverage, joeFileSize,  day);
        
        // Commits twice a day, churn decrease by 5 per time, 10 per day, end with 20.
        simData.addCommit(user, day, userFile, 20 + i * 5);
        simData.addCommit(user, day, userFile, 20 + i * 5);
        
        // Code issues decrease by 2 per day, end with 0.
        simData.addCodeIssues(user, day, userFile, i * 2);
        
      }
      simData.quitShells();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Adds newMember to projectName owned by owner. 
   * The newMember is invited by the owner, and then newMember accepts. 
   * @param projectName The name of the project.
   * @param owner The owner of the project (without domain name).
   * @param newMember The member to be added (without domain name). 
   */

  public void addMember(String projectName, String owner, String newMember) {
    String ownerEmail = owner + testdomain;
    String newMemberEmail = newMember + testdomain;
    SensorBaseClient ownerClient = 
      new SensorBaseClient(getSensorBaseHostName(), ownerEmail, ownerEmail);
    SensorBaseClient newMemberClient = 
      new SensorBaseClient(getSensorBaseHostName(), newMemberEmail, newMemberEmail);
    try {
      Project project = ownerClient.getProject(ownerEmail, projectName);
      project.getInvitations().getInvitation().add(newMemberEmail);
      ownerClient.putProject(project);
      newMemberClient.reply(ownerEmail, projectName, InvitationReply.ACCEPT);
    }
    catch (SensorBaseClientException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Clear data associated with the given user.
   * @param user the given user.
   */
  public void clearData(String user) {
    SensorBaseClient client = new SensorBaseClient(getSensorBaseHostName(), user, user);
    try {
      client.authenticate();
      client.deleteSensorData(user);
      for (ProjectRef ref : client.getProjectIndex(user).getProjectRef()) {
        Project project = client.getProject(ref);
        System.err.println("Removing project " + project.getName() + project.getOwner());
        if (user.equals(project.getOwner()) && !"Default".equals(project.getName())) {
          client.deleteProject(user, project.getName());
        }
      }
      client.deleteUser(user);
    }
    catch (SensorBaseClientException e) {
      System.out.println("Errors when clearing data associated with " + user + ": " + 
          e.getMessage());
      e.printStackTrace();
    }
  }
}
