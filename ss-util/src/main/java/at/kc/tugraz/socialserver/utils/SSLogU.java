/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
* For a list of contributors see the AUTHORS file at the top-level directory of this distribution.
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
 package at.kc.tugraz.socialserver.utils;

import at.kc.tugraz.ss.log.datatypes.SSLogLevelEnum;
import org.apache.log4j.Logger;

public class SSLogU{

  private static Logger log = Logger.getLogger(SSStrU.empty);

  private SSLogU(){}
  
  private static String getMsg(final String logText){
    
    return
      "thread: "   + Thread.currentThread().getId()                            +
      " class: "   + Thread.currentThread().getStackTrace()[3].getClassName()  +
      " method: "  + Thread.currentThread().getStackTrace()[3].getMethodName() +
      " line: "    + Thread.currentThread().getStackTrace()[3].getLineNumber() + SSStrU.backslashRBackslashN +
      logText;
  }
  
  private static String getMsg(final Exception error){
    
    return 
      "thread: "   + Thread.currentThread().getId()                            +
      " class: "   + Thread.currentThread().getStackTrace()[3].getClassName()  +
      " method: "  + Thread.currentThread().getStackTrace()[3].getMethodName() +
      " line: "    + Thread.currentThread().getStackTrace()[3].getLineNumber() + SSStrU.backslashRBackslashN +
      error;
  }
  
  private static String getMsg(final Exception error, final String logText){
    
    return 
      "thread: "   + Thread.currentThread().getId()                            +
      " class: "   + Thread.currentThread().getStackTrace()[3].getClassName()  +
      " method: "  + Thread.currentThread().getStackTrace()[3].getMethodName() +
      " line: "    + Thread.currentThread().getStackTrace()[3].getLineNumber() + SSStrU.backslashRBackslashN +
      logText                                                                  + SSStrU.backslashRBackslashN + 
      error;
  }
  
  public static void info(final String logText){
    log.info(getMsg(logText));
  }
  
  public static void trace(final String logText){
    log.trace(getMsg(logText));
  }
  
  public static void debug(final String logText){
    log.debug(getMsg(logText));
  }
  
  public static void warn(final String logText){
    log.warn(getMsg(logText));
  }
  
  public static void err(final Exception error, final String logText){
    
    if(error == null){
      return;
    }
      
    log.error(getMsg(error, logText));
    
//    error.printStackTrace();
  }
  
  public static void err(final Exception error){
    
    if(error == null){
      return;
    }
    
    log.error(getMsg(error));
    
//    error.printStackTrace();
  }
  
  public static void errThrow(final Exception error) throws Exception{
    
    if(error == null){
      throw new Exception("error to log null");
    }
    
    log.error(getMsg(error));
    
//    error.printStackTrace();
    
    throw error;
  }
  
  public static void fatalThrow(final Exception error) throws Exception{
    
    if(error == null){
      throw new Exception("error to log null");
    }
    
    log.fatal(getMsg(error));
    
//    error.printStackTrace();
  }
  
  public static Boolean isNotSame(final SSLogLevelEnum level1, final SSLogLevelEnum level2){
    return !isSame(level1, level2);
  }
  
  public static Boolean isSame(final SSLogLevelEnum level1, final SSLogLevelEnum level2){
    
    if(
      level1 == null ||
      level2 == null){
      return false;
    }
    
    return level1.toString().equals(level2.toString());
  }
}