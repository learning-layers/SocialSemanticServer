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
package at.tugraz.sss.serv;

import java.util.ArrayList;
import java.util.List;

public class SSServErrReg {
  
  private static final ThreadLocal<List<SSErr>> servImplErrors = new ThreadLocal<List<SSErr>>(){
    
    @Override
    protected List<SSErr> initialValue(){
      
      try{
        return new ArrayList<>();
      }catch (Exception error){
        SSLogU.err(error);
        return null;
      }
    }
  };
  
  public static Boolean containsErr(final SSErrE code){
    return servImplErrors.get().stream().anyMatch((error) -> (SSStrU.equals(error.code, code)));
  }
  
  public static Boolean containsErr(final Exception exception){
    
    if(exception instanceof SSErr){
      return containsErr(((SSErr) exception).code);
    }
    
    return servImplErrors.get().stream().anyMatch((error) -> (error.exception.getClass() == exception.getClass()));
  }
  
  public static void regErr(final Exception originalError){
    
    try{
      
      if(!SSStrU.contains(servImplErrors.get(), originalError)){
        SSLogU.err(originalError);
      }
      
      if(originalError instanceof SSErr){
        servImplErrors.get().add(SSErr.get(((SSErr) originalError).code, originalError));
      }else{
        servImplErrors.get().add(SSErr.get(SSErrE.defaultErr, originalError));
      }
      
    }catch(Exception error1){
      
      SSLogU.err(error1);
      
      try{
        servImplErrors.get().add(SSErr.get(SSErrE.defaultErr, error1));
      }catch(Exception error2){
        SSLogU.err(error2);
      }
    }
  }
  
  public static void regErr(
    final Exception originalError,
    final Boolean   log){
    
    try{
      
      if(
        log &&
        !SSStrU.contains(servImplErrors.get(), originalError)){
        
        SSLogU.err(originalError);
      }
      
      if(originalError instanceof SSErr){
        servImplErrors.get().add(SSErr.get(((SSErr) originalError).code, originalError));
      }else{
        servImplErrors.get().add(SSErr.get(SSErrE.defaultErr, originalError));
      }
      
    }catch(Exception error1){
      
      SSLogU.err(error1);
      
      try{
        servImplErrors.get().add(SSErr.get(SSErrE.defaultErr, error1));
      }catch(Exception error2){
        SSLogU.err(error2);
      }
    }
  }
  
  public static void regErr(
    final Exception originalError,
    final String    logText){
    
    try{
      
      if(!containsErr(originalError)){
        SSLogU.err(originalError, logText);
      }
      
      if(originalError instanceof SSErr){
        servImplErrors.get().add(SSErr.get(((SSErr) originalError).code, originalError));
      }else{
        servImplErrors.get().add(SSErr.get(SSErrE.defaultErr, originalError));
      }
      
    }catch(Exception error1){
      
      SSLogU.err(error1);
      
      try{
        servImplErrors.get().add(SSErr.get(SSErrE.defaultErr, error1));
      }catch(Exception error2){
        SSLogU.err(error2);
      }
    }
  }
  
  public static void regErrThrow(
    final Exception originalError,
    final Boolean   log) throws SSErr{
    
    SSErr errorToThrow = null;
    
    try{
      
      if(
        log &&
        !containsErr(originalError)){
        
        SSLogU.err(originalError);
      }
      
      if(originalError instanceof SSErr){
        errorToThrow = (SSErr) originalError;
        
        servImplErrors.get().add(SSErr.get(errorToThrow.code, errorToThrow));
        
      }else{
        errorToThrow = SSErr.get(SSErrE.defaultErr, originalError);
        
        servImplErrors.get().add(errorToThrow);
      }
      
    }catch(Exception error1){
      
      SSLogU.err(error1);
      
      try{
        servImplErrors.get().add(SSErr.get(SSErrE.defaultErr, error1));
      }catch(Exception error2){
        SSLogU.err(error2);
      }
    }
    
    throw errorToThrow;
  }
  
  public static void regErrThrow(
    final SSErrE    code,
    final Exception originalError) throws SSErr{
    
    SSErr errorToThrow = null;
    
    try{
      
      if(!containsErr(code)){
        SSLogU.err(code.toString());
      }
      
      if(originalError == null){
        errorToThrow = SSErr.get(code);
      }else{
        errorToThrow = SSErr.get(code, originalError);
      }
      
      servImplErrors.get().add(errorToThrow);
    }catch(Exception error1){
      
      SSLogU.err(error1);
      
      try{
        servImplErrors.get().add(SSErr.get(SSErrE.defaultErr, error1));
      }catch(Exception error2){
        SSLogU.err(error2);
      }
    }
    
    throw errorToThrow;
  }
  
  public static void regErrThrow(final Exception error) throws SSErr{
    
    SSErr errorToThrow = null;
    
    try{
      
      if(!containsErr(error)){
        SSLogU.err(error);
      }
      
      if(error instanceof SSErr){
        errorToThrow = (SSErr) error;
        
        servImplErrors.get().add(SSErr.get(errorToThrow.code, errorToThrow));
      }else{
        errorToThrow = SSErr.get(SSErrE.defaultErr, error);
        
        servImplErrors.get().add(errorToThrow);
      }
      
    }catch(Exception error1){
      
      SSLogU.err(error1);
      
      try{
        servImplErrors.get().add(SSErr.get(SSErrE.defaultErr, error1));
      }catch(Exception error2){
        SSLogU.err(error2);
      }
    }
    
    throw errorToThrow;
  }
  
  public static void logAndReset(final Boolean log){
    
    try{
      
      if(!log){
        
        reset();
        return;
      }
      
      for(SSErr error : servImplErrors.get()){
        
        SSLogU.err(
          error.threadWhereThrown + SSStrU.blank
            +  error.classWhereThrown  + SSStrU.blank
            +  error.methodWhereThrown + SSStrU.blank
            +  error.lineWhereThrown   + SSStrU.blank
            +  error.className         + SSStrU.blank
            +  error.message);
      }
      
      reset();
      
    }catch(Exception error1){
      SSLogU.err(error1);
    }
  }
  
  public static List<SSErr> getErrors(){
    return new ArrayList<>(servImplErrors.get());
  }
  
  public static void reset(){
    servImplErrors.get().clear();
  }
}

