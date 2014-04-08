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

import at.kc.tugraz.socialserver.utils.SSEncodingU;
import at.kc.tugraz.socialserver.utils.SSFileU;
import at.kc.tugraz.socialserver.utils.SSHTMLU;
import at.kc.tugraz.socialserver.utils.SSJSONU;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSMimeTypeU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.job.i5cloud.api.SSI5CloudClientI;
import at.kc.tugraz.ss.serv.job.i5cloud.api.SSI5CloudServerI;
import at.kc.tugraz.ss.serv.job.i5cloud.conf.SSI5CloudConf;
import at.kc.tugraz.ss.serv.job.i5cloud.datatypes.par.SSI5CloudAuthPar;
import at.kc.tugraz.ss.serv.job.i5cloud.datatypes.par.SSI5CloudFileDownloadPar;
import at.kc.tugraz.ss.serv.job.i5cloud.datatypes.par.SSI5CloudFileUploadPar;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplMiscA;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class SSI5CloudImpl extends SSServImplMiscA implements SSI5CloudClientI, SSI5CloudServerI{

  public SSI5CloudImpl(final SSServConfA conf) throws Exception{
    super(conf);
  }
  
  /* SSServRegisterableImplI */
  @Override
  public List<SSMethU> publishClientOps() throws Exception{

    List<SSMethU> clientOps = new ArrayList<SSMethU>();

    Method[] methods = SSI5CloudClientI.class.getMethods();

    for(Method method : methods){
      clientOps.add(SSMethU.get(method.getName()));
    }

    return clientOps;
  }

  @Override
  public List<SSMethU> publishServerOps() throws Exception{

    List<SSMethU> serverOps = new ArrayList<SSMethU>();

    Method[] methods = SSI5CloudServerI.class.getMethods();

    for(Method method : methods){
      serverOps.add(SSMethU.get(method.getName()));
    }

    return serverOps;
  }

  @Override
  public void handleClientOp(SSSocketCon sSCon, SSServPar par) throws Exception{
    SSI5CloudClientI.class.getMethod(SSMethU.toStr(par.op), SSSocketCon.class, SSServPar.class).invoke(this, sSCon, par);
  }

  @Override
  public Object handleServerOp(SSServPar par) throws Exception{
    return SSI5CloudServerI.class.getMethod(SSMethU.toStr(par.op), SSServPar.class).invoke(this, par);
  }

   /*  SSI5CloudServerI */
   
  @Override
  public Map<String, String> i5CloudAuth(final SSServPar parA) throws Exception{
    
    final SSI5CloudAuthPar    par         = new SSI5CloudAuthPar(parA);
    final Map<String, String> messageBody = new HashMap<String, String>();
    final HttpURLConnection   con;
    OutputStream              out         = null;
    
    try{
      
      messageBody.put(SSVarU.username,((SSI5CloudConf)conf).userLabel);
      messageBody.put(SSVarU.password, ((SSI5CloudConf)conf).pass);
      
      con = (HttpURLConnection) new URL(SSFileU.correctDirPath(((SSI5CloudConf)conf).uri) + "auth").openConnection();
      
      con.setDoOutput(true);
      con.setRequestProperty(SSHTMLU.acceptCharset, SSEncodingU.utf8);
      con.setRequestProperty(SSHTMLU.contentType,  SSMimeTypeU.applicationJson);
      
      out = con.getOutputStream();
      
      out.write(SSJSONU.jsonStr(messageBody).getBytes(SSEncodingU.utf8));
      
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
    
    final SSI5CloudFileUploadPar par = new SSI5CloudFileUploadPar(parA);
    final HttpURLConnection      i5CloudCon;
    
    try{
      
      i5CloudCon = (HttpURLConnection) new URL(SSFileU.correctDirPath(((SSI5CloudConf)conf).uri) + "storage/" + par.space + SSStrU.slash +  par.fileName).openConnection();

      i5CloudCon.setDoOutput         (true);
      i5CloudCon.setRequestMethod    (SSHTMLU.put);
      i5CloudCon.setRequestProperty  (SSHTMLU.xAuthToken,  par.xAuthToken);
      i5CloudCon.setRequestProperty  (SSHTMLU.contentType, SSMimeTypeU.multipartFormData);
    
      SSFileU.readFileBytes(
        i5CloudCon.getOutputStream(), 
        SSFileU.openFileForRead(SSFileU.dirWorkingTmp() + par.fileName));
     
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
      
      i5CloudCon = (HttpURLConnection) new URL(SSFileU.correctDirPath(((SSI5CloudConf)conf).uri) + "storage/" + par.space + SSStrU.slash + par.fileName).openConnection();

      i5CloudCon.setRequestProperty  (SSHTMLU.xAuthToken,  par.xAuthToken);
    
      SSFileU.writeFileBytes(
        SSFileU.openOrCreateFileWithPathForWrite(SSFileU.dirWorkingTmp() + par.fileName), 
        i5CloudCon.getInputStream());

      return true;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}