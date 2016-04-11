/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.sss.serv.conf;

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.reg.*;
import java.util.*;
import java.util.concurrent.*;

public class SSSchedules {

  protected static final List<ScheduledExecutorService> schedulers = new ArrayList<>();  

  public void regScheduler(final ScheduledExecutorService scheduler) throws SSErr{
    
    try{
      
      if(schedulers.contains(scheduler)){
        return;
      }
      
      schedulers.add(scheduler);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  public void clear() {
    
    for(ScheduledExecutorService scheduler : schedulers){
      scheduler.shutdown();
    }
  }
}