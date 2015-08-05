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
package at.kc.tugraz.ss.serv.job.i5cloud.impl;

import at.tugraz.sss.serv.SSEncodingU;
import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSHTMLU;
import at.tugraz.sss.serv.SSJSONU;
import at.tugraz.sss.serv.SSMimeTypeE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSServPar;
import at.kc.tugraz.ss.serv.job.i5cloud.api.SSI5CloudClientI;
import at.kc.tugraz.ss.serv.job.i5cloud.api.SSI5CloudServerI;
import at.kc.tugraz.ss.serv.job.i5cloud.conf.SSI5CloudConf;
import at.kc.tugraz.ss.serv.job.i5cloud.datatypes.par.SSI5CloudAchsoSemanticAnnotationsSetGetPar;
import at.kc.tugraz.ss.serv.job.i5cloud.datatypes.par.SSI5CloudAuthPar;
import at.kc.tugraz.ss.serv.job.i5cloud.datatypes.par.SSI5CloudFileDownloadPar;
import at.kc.tugraz.ss.serv.job.i5cloud.datatypes.par.SSI5CloudFileUploadPar;
import at.kc.tugraz.ss.serv.job.i5cloud.impl.las.SSI5CloudLASConnector;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import i5.las.httpConnector.client.TimeoutException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class SSI5CloudImpl 
extends 
  SSServImplWithDBA 
implements 
  SSI5CloudClientI, 
  SSI5CloudServerI{

  private SSI5CloudLASConnector lasCon = null;
  
  public SSI5CloudImpl(final SSConfA conf) throws Exception{
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
  }
  
  @Override
  public Map<String, String> i5CloudAuth(final SSServPar parA) throws Exception{
    
    final SSI5CloudAuthPar    par         = new SSI5CloudAuthPar(parA);
    final Map<String, String> messageBody = new HashMap<>();
    final HttpURLConnection   con;
    OutputStream              out         = null;
    
    try{
      messageBody.put(SSVarNames.username, ((SSI5CloudConf)conf).userLabel);
      messageBody.put(SSVarNames.password, ((SSI5CloudConf)conf).pass);
      
      con = (HttpURLConnection) new URL(SSFileU.correctDirPath(((SSI5CloudConf)conf).uri) + "auth").openConnection();
      
      con.setDoOutput(true);
      con.setRequestProperty(SSHTMLU.acceptCharset, SSEncodingU.utf8.toString());
      con.setRequestProperty(SSHTMLU.contentType,   SSMimeTypeE.applicationJson.toString());
      
      out = con.getOutputStream();
      
      out.write(SSJSONU.jsonStr(messageBody).getBytes(SSEncodingU.utf8.toString()));
      
      return SSJSONU.jsonMap(SSFileU.readStreamText(con.getInputStream()));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
     
      if(out != null){
        out.close();
      }
    }
  }
  
  @Override
  public Boolean i5CloudFileUpload(final SSServPar parA) throws Exception{
    
    try{
      final SSI5CloudFileUploadPar par        = new SSI5CloudFileUploadPar(parA);
      final HttpURLConnection      i5CloudCon = 
        (HttpURLConnection) new URL(
          SSFileU.correctDirPath(((SSI5CloudConf)conf).uri) + 
            "storage/"   + 
            par.space    + 
            SSStrU.slash +  
            par.label).openConnection();
      
      i5CloudCon.setDoOutput         (true);
      i5CloudCon.setRequestMethod    (SSHTMLU.put);
      i5CloudCon.setRequestProperty  (SSHTMLU.xAuthToken,  par.authToken);
      i5CloudCon.setRequestProperty  (SSHTMLU.contentType, SSMimeTypeE.multipartFormData.toString());
      
      SSFileU.readFileBytes(
        i5CloudCon.getOutputStream(),
        SSFileU.openFileForRead(SSFileU.dirWorkingTmp() + par.label));
      
      SSFileU.readStreamText(i5CloudCon.getInputStream());
      
      return true;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public Boolean i5CloudFileDownload(final SSServPar parA) throws Exception{
    
    final SSI5CloudFileDownloadPar par   = new SSI5CloudFileDownloadPar(parA);
    final HttpURLConnection        i5CloudCon;     
    
    try{
      
      i5CloudCon = (HttpURLConnection) new URL(SSFileU.correctDirPath(((SSI5CloudConf)conf).uri) + "storage/" + par.space + SSStrU.slash + par.label).openConnection();

      i5CloudCon.setRequestProperty  (SSHTMLU.xAuthToken,  par.authToken);
    
      SSFileU.writeFileBytes(
        SSFileU.openOrCreateFileWithPathForWrite(SSFileU.dirWorkingTmp() + par.label), 
        i5CloudCon.getInputStream());

      return true;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public String i5CloudAchsoVideoInformationGet(final SSServPar parA) throws Exception{
    
    try{
      if(
        lasCon == null){
        lasCon = getLASCon();
      }
      
      return (String) lasCon.invocationHelper(
        "videoinformation", 				        
        "getVideoInformationConditional",
        SSStrU.empty, 
        SSStrU.empty, 
        SSStrU.empty, 
        SSStrU.empty);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public List<String> i5CloudAchsoSemanticAnnotationsSetGet(final SSServPar parA) throws Exception{
    
    try{
      final SSI5CloudAchsoSemanticAnnotationsSetGetPar par                     = new SSI5CloudAchsoSemanticAnnotationsSetGetPar(parA);
      final List<String>                               finalAnnotations        = new ArrayList<>();
      
      if(
        lasCon == null){
        lasCon = getLASCon();
      }
      
      String[] annotations = null;
        
      try{
        annotations =
          (String[]) lasCon.invoke(
            "videoinformation",
            "getSemanticAnnotationsSet",
            new Object[]{SSStrU.toArray(par.ids)});
      }catch(TimeoutException error){
        
        if(lasCon != null){
          lasCon.disconnect();
          lasCon = null;
        }
        
        return i5CloudAchsoSemanticAnnotationsSetGet(parA);
      }
      
      for(String annotation : SSStrU.distinctWithoutEmptyAndNull(annotations)){
        finalAnnotations.addAll(SSStrU.split(annotation, SSStrU.comma));
      }
      
      return SSStrU.distinctWithoutEmptyAndNull(finalAnnotations);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private SSI5CloudLASConnector getLASCon(){
    return new SSI5CloudLASConnector(((SSI5CloudConf)conf).lasUser, ((SSI5CloudConf)conf).lasPass);
  }
}