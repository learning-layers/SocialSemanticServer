/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.sss.adapter.rest.v2;

import at.tugraz.sss.serv.util.*;
import javax.servlet.*;

public class SSRestMainV2Initializer implements ServletContextListener{

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    
    SSLogU.err("!HERE");
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
