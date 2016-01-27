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
package at.tugraz.sss.serv.util;

import at.tugraz.sss.serv.datatype.enums.*;
import java.io.*;
import java.util.*;
import org.apache.log4j.*;

public class SSLogU{
  
  private static Logger log = null;
  
  public static void init(final String workDirPath) throws FileNotFoundException, IOException{
    
    final Properties p = new Properties();
    
    p.setProperty("log4j.rootLogger", "trace, console, file, eval");
    
    p.setProperty("log4j.appender.console", "org.apache.log4j.ConsoleAppender");
    p.setProperty("log4j.appender.console.layout", "org.apache.log4j.PatternLayout");
    p.setProperty("log4j.appender.console.layout.ConversionPattern", "%d %5p - %m%n");
    p.setProperty("log4j.appender.console.threshold", "INFO");
    
    p.setProperty("log4j.appender.file", "org.apache.log4j.RollingFileAppender");
    p.setProperty("log4j.appender.file.File", workDirPath + SSFileU.fileNameSSSLog);
    p.setProperty("log4j.appender.file.MaxFileSize", "10000KB");
    p.setProperty("log4j.appender.file.MaxBackupIndex", "1");
    p.setProperty("log4j.appender.file.layout", "org.apache.log4j.PatternLayout");
    p.setProperty("log4j.appender.file.layout.ConversionPattern", "%d %5p - %m%n");
    p.setProperty("log4j.appender.file.threshold", "INFO");
    
    p.setProperty("log4j.appender.eval", "org.apache.log4j.RollingFileAppender");
    p.setProperty("log4j.appender.eval.File", workDirPath + SSFileU.fileNameSSSEvalLog);
    p.setProperty("log4j.appender.eval.MaxFileSize", "10000KB");
    p.setProperty("log4j.appender.eval.MaxBackupIndex", "1000");
    p.setProperty("log4j.appender.eval.layout", "org.apache.log4j.PatternLayout");
    p.setProperty("log4j.appender.eval.layout.ConversionPattern", "%m%n");
    p.setProperty("log4j.appender.eval.filter.1", "org.apache.log4j.varia.LevelMatchFilter");
    p.setProperty("log4j.appender.eval.filter.1.levelToMatch", "TRACE");
    p.setProperty("log4j.appender.eval.filter.2", "org.apache.log4j.varia.DenyAllFilter");
    
    PropertyConfigurator.configure(p);
    
    log = Logger.getLogger(SSStrU.empty);
  }
  
  private SSLogU(){}
  
  private static String getMsg(final String logText){
    
    return
      "thread: "   + Thread.currentThread().getId()                            +
      " class: "   + Thread.currentThread().getStackTrace()[4].getClassName()  +
      " method: "  + Thread.currentThread().getStackTrace()[4].getMethodName() +
      " line: "    + Thread.currentThread().getStackTrace()[4].getLineNumber() + SSStrU.backslashRBackslashN +
      logText;
  }
  
  private static String getMsg(final Exception error){
    
    return
      "thread: "   + Thread.currentThread().getId()                            +
      " class: "   + Thread.currentThread().getStackTrace()[4].getClassName()  +
      " method: "  + Thread.currentThread().getStackTrace()[4].getMethodName() +
      " line: "    + Thread.currentThread().getStackTrace()[4].getLineNumber() + SSStrU.backslashRBackslashN +
      error;
  }
  
  private static String getMsg(final Exception error, final String logText){
    
    return
      "thread: "   + Thread.currentThread().getId()                            +
      " class: "   + Thread.currentThread().getStackTrace()[4].getClassName()  +
      " method: "  + Thread.currentThread().getStackTrace()[4].getMethodName() +
      " line: "    + Thread.currentThread().getStackTrace()[4].getLineNumber() + SSStrU.backslashRBackslashN +
      logText                                                                  + SSStrU.backslashRBackslashN +
      error;
  }
  
  public static void info(final String logText){
    log.info(getMsg(logText));
  }
  
  public static void info(
    final String    logText, 
    final Exception error){
    
    log.info(getMsg(error, logText));
  }
  
  public static void trace(
    final String  logText,
    final boolean provideRuntimeInfo){
    
    if(provideRuntimeInfo){
      log.trace(getMsg(logText));
    }else{
      log.trace(logText);
    }
  }
  
  public static void debug(final String logText){
    log.debug(getMsg(logText));
  }
  
  public static void warn(
    final Object    warning,
    final Exception error){
    
    log.warn(getMsg(SSStrU.toStr(warning) + SSStrU.blank + SSStrU.toStr(error)));
  }
  
  public static void warn(
    final SSWarnE warning){
    
    log.warn(getMsg(SSStrU.toStr(warning)));
  }
  
  public static void warn(
    final Object    logText){
    
    log.warn(getMsg(SSStrU.toStr(logText)));
  }
  
  public static void err(
    final Exception error, 
    final String    logText){
    
    if(error == null){
      return;
    }
    
    log.error(getMsg(error, logText));
    
//    error.printStackTrace();
  }
  
  public static void err(final SSErrE code){
    
    if(code == null){
      return;
    }
    
    log.error(getMsg(code.toString()));
    
//    error.printStackTrace();
  }
  
  public static void err(final Exception error){
    
    if(error == null){
      return;
    }
    
    log.error(getMsg(error));
    
//    error.printStackTrace();
  }
  
  public static void err(final String errorMsg){
    
    if(errorMsg == null){
      return;
    }
    
    log.error(getMsg(errorMsg));
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
}