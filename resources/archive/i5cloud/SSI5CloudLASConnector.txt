/**
 * Copyright 2014 Chair for Information Systems and Databases, RWTH Aachen University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.kc.tugraz.ss.serv.job.i5cloud.impl.las;

import java.util.Date;
import i5.las.httpConnector.client.AccessDeniedException;
import i5.las.httpConnector.client.AuthenticationFailedException;
import i5.las.httpConnector.client.Client;
import i5.las.httpConnector.client.ConnectorClientException;
import i5.las.httpConnector.client.NotFoundException;
import i5.las.httpConnector.client.ServerErrorException;
import i5.las.httpConnector.client.TimeoutException;
import i5.las.httpConnector.client.UnableToConnectException;

public class SSI5CloudLASConnector {
  
  public static final String CONNECTION_PROBLEM = "An error accoured when connectiong to web please check your internet conenction settings";
  public static final String AUTHENTICATION_PROBLEM = "wrong username or password, please try again";
  public static final String UNDEFINED_PROBLEM = "an exception occured, please try again";
  // FIELDS
  // -------------------------------------------------------------------
  private Client client;
  private String sessionId;
  
  /**
   * Creates a new instance of this class with empty values.
   */
  public SSI5CloudLASConnector(String username, String password) {
    connect(username, password);
  }
  
  
  /**
   * Invokes the specified LAS service method.
   *
   * @param service the service.
   * @param method  the method.
   * @param params  the parameter list.
   * @return the object returned by the service method.
   */
  public Object invoke(String service, String method, Object... params) throws TimeoutException {
    
    if (client == null) {
      System.out.println("LasConnection Missing client.");
      return null;
    }
    
    try {
      Date before = new Date();
      Object res = client.invoke(service, method, params);
      Date after = new Date();
      
      String s = (after.getTime() - before.getTime()) + "ms: " + service
        + "." + method;
      System.out.println("LasConnection " + s);
      
      return res;
    } catch (AccessDeniedException e) {
      e.printStackTrace();
    } catch (AuthenticationFailedException e) {
      e.printStackTrace();
    } catch (UnableToConnectException e) {
      e.printStackTrace();
    } catch (ServerErrorException e) {
      e.printStackTrace();
    } catch (TimeoutException e) {
      e.printStackTrace();
      throw e;
    } catch (NotFoundException e) {
      e.printStackTrace();
    } catch (ConnectorClientException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return null;
  }
  
  public String invocationHelper(String service, String method, Object... params){
    String xmlString = null;
    try {
      
      // this.instantiateDBContext();
      
      // returns a String with the XML containing all the videos and the
      // corresponding data for each video
      xmlString = (String) this.invoke(service, method, params);
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return xmlString;
  }
  
  /**
   * Instantiates the database context
   *
   * @return true, if successful
   */
  public Boolean instantiateDBContext() {
    boolean isInstantiated = false;
    
    try {
      String appCode = "vc"; //$NON-NLS-1$
      String constraints = "v2"; //$NON-NLS-1$
      
      Object[] params = {appCode, constraints};
      
      this.invoke("xmldbxs-context-service", "instantiateContext", params);
      isInstantiated = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return isInstantiated;
    
  }
  
  /**
   * Establishes a connection with the LAS server for the specified user. If
   * the connection cannot be established an error code will be returned:
   * <ul>
   * <li>{@code ERR15} - the port number could not be parse.</li>
   * <li>{@code -1} - Client could not connect to LAS.</li>
   * <li>{@code -2} - User could not be authenticated.</li>
   * <li>{@code -3} - Any other error occurred when connecting.</li>
   * </ul>
   *
   * @param username the username
   * @param password the user's password.
   * @return the session ID or an error code as specified above.
   */
  public String connect(String username, String password) {
    String lasHostname;
    int lasPort;
    long timeOut;
    
    lasHostname = "steen.informatik.rwth-aachen.de";
    lasPort = 9914;
    timeOut = 2;
    
    
    System.out.println("LasConnection Connecting LAS Host: " +lasHostname + " port: " + lasPort);
    
    // userService.setCurrentUser(new User());
    
    client = new Client(lasHostname, lasPort, timeOut * 1000, username,
      password);
    client.getMobileContext().setApplicationCode("AchSo");
    
    System.out.println("LasConnection CLIENT TIMEOUT: " + client.getTimeoutMs());
    try {
      // connect to LAS server
      client.connect();
      
      this.setSessionId(client.getSessionId());
      // parameters used for determining the database connection to be
      // used to store the metadata
      // below, we use the same DB tables as SeViAnno2.0 version
      String appCode = "vc";
      String constraints = "v2";
      
      Object[] params = {appCode, constraints};
      
      this.invoke("xmldbxs-context-service", "instantiateContext", params);
      
    } catch (UnableToConnectException e) {
      e.printStackTrace();
      return CONNECTION_PROBLEM;
    } catch (AuthenticationFailedException e) {
      e.printStackTrace();
      return AUTHENTICATION_PROBLEM;
    } catch (Exception e) {
      e.printStackTrace();
      return UNDEFINED_PROBLEM;
    }
    
    return client.getSessionId();
  }
  
  /**
   * Disconnect.
   *
   * @return true, if successful
   */
  public boolean disconnect() {
    try {
      client.disconnect();
      return true;
    } catch (Exception e) {
      return false;
    }
  }
  
  public String getSessionId() {
    return sessionId;
  }
  
  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }
  
}