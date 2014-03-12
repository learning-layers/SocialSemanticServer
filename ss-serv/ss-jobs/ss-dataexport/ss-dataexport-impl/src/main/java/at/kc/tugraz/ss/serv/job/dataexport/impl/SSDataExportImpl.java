/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.serv.job.dataexport.impl;

import at.kc.tugraz.socialserver.utils.SSEncodingU;
import at.kc.tugraz.socialserver.utils.SSFileU;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.job.dataexport.api.SSDataExportClientI;
import at.kc.tugraz.ss.serv.job.dataexport.api.SSDataExportServerI;
import at.kc.tugraz.ss.serv.job.dataexport.datatypes.par.SSDataExportUserResourceTimestampTagsCategoriesPar;
import at.kc.tugraz.ss.serv.serv.api.SSServImplMiscA;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class SSDataExportImpl extends SSServImplMiscA implements SSDataExportClientI, SSDataExportServerI{

  private CSVWriter fileWriterUserResourceTimestampTagsCategories = null;
  
  public SSDataExportImpl(final SSServConfA conf) throws Exception{
    super(conf);
  }
  
  /****** SSServRegisterableImplI ******/
  
  @Override
  public List<SSMethU> publishClientOps() throws Exception{
    
    List<SSMethU> clientOps = new ArrayList<SSMethU>();
    
    Method[] methods = SSDataExportClientI.class.getMethods();
    
    for(Method method : methods){
      clientOps.add(SSMethU.get(method.getName()));
    }
    
    return clientOps;
  }
  
  @Override
  public List<SSMethU> publishServerOps() throws Exception{
    
    List<SSMethU> serverOps = new ArrayList<SSMethU>();
    
    Method[] methods = SSDataExportServerI.class.getMethods();
    
    for(Method method : methods){
      serverOps.add(SSMethU.get(method.getName()));
    }
    
    return serverOps;
  }
  
  @Override
  public void handleClientOp(SSSocketCon sSCon, SSServPar par) throws Exception{
    SSDataExportClientI.class.getMethod(SSMethU.toStr(par.op), SSSocketCon.class, SSServPar.class).invoke(this, sSCon, par);
  }
  
  @Override
  public Object handleServerOp(SSServPar par) throws Exception{
    return SSDataExportServerI.class.getMethod(SSMethU.toStr(par.op), SSServPar.class).invoke(this, par);
  }

  /****** SSDataExportServerI ******/
  
  @Override
  public void dataExportUserResourceTimestampTagsCategories(final SSServPar parA) throws Exception{
    
    final SSDataExportUserResourceTimestampTagsCategoriesPar par = new SSDataExportUserResourceTimestampTagsCategoriesPar(parA);
    
    if(par.wasLastLine){
      
      if(fileWriterUserResourceTimestampTagsCategories != null){
        fileWriterUserResourceTimestampTagsCategories.close();
        fileWriterUserResourceTimestampTagsCategories = null;
      }
      
      return;
    }
    
    if(fileWriterUserResourceTimestampTagsCategories == null){
    
      final FileOutputStream   out                     = SSFileU.openOrCreateFileWithPathForWrite (SSFileU.dirWorkingDataCsv() + par.fileName);
      final OutputStreamWriter writer                  = new OutputStreamWriter   (out,    Charset.forName(SSEncodingU.utf8));
      fileWriterUserResourceTimestampTagsCategories    = new CSVWriter            (writer, SSStrU.semiColon.charAt(0));
    }
    
    final List<String>                              lineParts                    = new ArrayList<String>();
    final Iterator<Map.Entry<String, List<String>>> entitiesTagsIterator         = par.tagsPerEntities.entrySet().iterator();
    final Iterator<Map.Entry<String, List<String>>> entitiesCategoriesIterator   = par.categoriesPerEntities.entrySet().iterator();
    Map.Entry<String, List<String>>                 entityAndTags;
    Map.Entry<String, List<String>>                 entityAndCategories;
    
    while(entitiesTagsIterator.hasNext()){
      
      entityAndTags       = entitiesTagsIterator.next();
      entityAndCategories = entitiesCategoriesIterator.next();

      lineParts.clear();
      
      lineParts.add(SSUri.toStr     (par.user));
      lineParts.add(entityAndTags.getKey());
      lineParts.add(Long.toString   (par.timestamp));
      lineParts.add(StringUtils.join(entityAndTags.getValue(),       SSStrU.comma));
      lineParts.add(StringUtils.join(entityAndCategories.getValue(), SSStrU.comma));

      fileWriterUserResourceTimestampTagsCategories.writeNext((String[]) lineParts.toArray(new String[lineParts.size()]));
    }
  }
}
