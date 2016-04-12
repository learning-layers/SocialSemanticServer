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
package at.tugraz.sss.servs.entity.datatype;

import at.tugraz.sss.servs.util.SSStrU;
import java.util.ArrayList;
import java.util.List;

public class SSErr extends Exception{
  
  public String          id        = null;
  public final String    className;
  public final String    message;
  public final Long      threadWhereThrown;
  public final String    classWhereThrown;
  public final String    methodWhereThrown;
  public final Integer   lineWhereThrown;
  
  public SSErrE    code      = null;
  public Exception exception = null;
  
  public String getCode(){
    return SSStrU.toStr(code);
  }
  
  public static SSErr get(final SSErrE code){
    return new SSErr(code);
  }
  
  public static SSErr get(
    final SSErrE    code,
    final Exception exception){
    
    return new SSErr(code, exception);
  }
    
  private SSErr(final SSErrE code){
    
    super(SSStrU.toStr(code));
    
    this.code               = code;
    this.id                 = code.toString();
    this.className          = getClass().getName();
    this.message            = code.toString();
    this.threadWhereThrown  = Thread.currentThread().getId();
    this.classWhereThrown   = Thread.currentThread().getStackTrace()[2].getClassName();
    this.methodWhereThrown  = Thread.currentThread().getStackTrace()[2].getMethodName();
    this.lineWhereThrown    = Thread.currentThread().getStackTrace()[2].getLineNumber();
  }
  
  private SSErr(
    final SSErrE    code,
    final Exception originalError){
    
    super(code + SSStrU.colon + SSStrU.blank + originalError.getMessage());
    
    this.code               = code;
    this.id                 = code.toString();
    this.exception          = originalError;
    this.className          = originalError.getClass().getName();
    this.message            = originalError.getMessage();
    this.threadWhereThrown  = Thread.currentThread().getId();
    this.classWhereThrown   = Thread.currentThread().getStackTrace()[4].getClassName();
    this.methodWhereThrown  = Thread.currentThread().getStackTrace()[4].getMethodName();
    this.lineWhereThrown    = Thread.currentThread().getStackTrace()[4].getLineNumber();
  }
  
  public static List<String> linesWhereThrown(List<SSErr> errors) {
    
    List<String> linesWhereThrown = new ArrayList<>();
    
    if(errors == null){
      return linesWhereThrown;
    }
    
    for(SSErr error: errors){
      linesWhereThrown.add(SSStrU.toStr(error.lineWhereThrown));
    }
    
    return linesWhereThrown;
  }
  
  public static List<String> methodsWhereThrown(List<SSErr> errors) {
    
    List<String> methodsWhereThrown = new ArrayList<>();
    
    if(errors == null){
      return methodsWhereThrown;
    }
    
    for(SSErr error: errors){
      methodsWhereThrown.add(SSStrU.toStr(error.methodWhereThrown));
    }
    
    return methodsWhereThrown;
  }
  
  public static List<String> classesWhereThrown(List<SSErr> errors) {
    
    List<String> classesWhereThrown = new ArrayList<>();
    
    if(errors == null){
      return classesWhereThrown;
    }
    
    for(SSErr error: errors){
      classesWhereThrown.add(SSStrU.toStr(error.classWhereThrown));
    }
    
    return classesWhereThrown;
  }
  
  public static List<String> threadsWhereThrown(List<SSErr> errors) {
    
    List<String> threadsWhereThrown = new ArrayList<>();
    
    if(errors == null){
      return threadsWhereThrown;
    }
    
    for(SSErr error: errors){
      threadsWhereThrown.add(SSStrU.toStr(error.threadWhereThrown));
    }
    
    return threadsWhereThrown;
  }
  
  public static List<String> messages(List<SSErr> errors) {
    
    List<String> messages = new ArrayList<>();
    
    if(errors == null){
      return messages;
    }
    
    for(SSErr error: errors){
      messages.add(error.message);
    }
    
    return messages;
  }
  
  public static List<String> classNames(List<SSErr> errors) {
    
    List<String> classNames = new ArrayList<>();
    
    if(errors == null){
      return classNames;
    }
    
    for(SSErr error : errors){
      classNames.add(error.className);
    }
    
    return classNames;
  }
}