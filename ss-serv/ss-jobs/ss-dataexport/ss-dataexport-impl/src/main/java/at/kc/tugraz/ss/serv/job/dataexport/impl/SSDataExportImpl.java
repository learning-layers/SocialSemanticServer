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
package at.kc.tugraz.ss.serv.job.dataexport.impl;

import at.kc.tugraz.socialserver.utils.SSEncodingU;
import at.kc.tugraz.socialserver.utils.SSFileU;
import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.adapter.socket.datatypes.SSSocketCon;
import at.kc.tugraz.ss.serv.serv.api.SSServConfA;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.job.dataexport.api.SSDataExportClientI;
import at.kc.tugraz.ss.serv.job.dataexport.api.SSDataExportServerI;
import at.kc.tugraz.ss.serv.job.dataexport.datatypes.par.SSDataExportUserEntityTagCategoriesPar;
import at.kc.tugraz.ss.serv.job.dataexport.datatypes.par.SSDataExportUserEntityTagTimestampsPar;
import at.kc.tugraz.ss.serv.job.dataexport.datatypes.par.SSDataExportUserEntityTagsPar;
import at.kc.tugraz.ss.serv.job.dataexport.datatypes.par.SSDataExportUserEntityTagCategoryTimestampPar;
import at.kc.tugraz.ss.serv.serv.api.SSConfA;
import at.kc.tugraz.ss.serv.serv.api.SSServImplMiscA;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class SSDataExportImpl extends SSServImplMiscA implements SSDataExportClientI, SSDataExportServerI{

  private CSVWriter fileWriterUserEntityTags                      = null;
  private CSVWriter fileWriterUserEntityTagTimestamps             = null;
  private CSVWriter fileWriterUserEntityTagCategories             = null;
  private CSVWriter fileWriterUserEntityTagCategoryTimestamps     = null;
  
  public SSDataExportImpl(final SSConfA conf) throws Exception{
    super(conf);
  }
  
  /*  SSDataExportServerI  */
  
  @Override
  public void dataExportUserEntityTags(final SSServPar parA) throws Exception{
    
    final SSDataExportUserEntityTagsPar par = new SSDataExportUserEntityTagsPar(parA);
    
    if(par.wasLastLine){
      
      if(fileWriterUserEntityTags != null){
        fileWriterUserEntityTags.close();
        fileWriterUserEntityTags = null;
      }
      
      return;
    }
    
    if(fileWriterUserEntityTags == null){
    
      final FileOutputStream   out                     = SSFileU.openOrCreateFileWithPathForWrite (SSFileU.dirWorkingDataCsv() + par.fileName);
      final OutputStreamWriter writer                  = new OutputStreamWriter   (out,    Charset.forName(SSEncodingU.utf8));
      fileWriterUserEntityTags                         = new CSVWriter            (writer, SSStrU.semiColon.charAt(0));
    }
    
    final List<String>                              lineParts                    = new ArrayList<String>();
    final Iterator<Map.Entry<String, List<String>>> entitiesTagsIterator         = par.tagsPerEntities.entrySet().iterator();
    Map.Entry<String, List<String>>                 entityAndTags;
    
    while(entitiesTagsIterator.hasNext()){
      
      entityAndTags = entitiesTagsIterator.next();

      lineParts.clear();
      
      lineParts.add(SSUri.toStr     (par.user));
      lineParts.add(entityAndTags.getKey());
      lineParts.add(StringUtils.join(entityAndTags.getValue(),       SSStrU.comma));

      fileWriterUserEntityTags.writeNext((String[]) lineParts.toArray(new String[lineParts.size()]));
    }
  }
  
  @Override
  public void dataExportUserEntityTagTimestamps(final SSServPar parA) throws Exception{
    
    final SSDataExportUserEntityTagTimestampsPar par = new SSDataExportUserEntityTagTimestampsPar(parA);
    
    if(par.wasLastLine){
      
      if(fileWriterUserEntityTagTimestamps != null){
        fileWriterUserEntityTagTimestamps.close();
        fileWriterUserEntityTagTimestamps = null;
      }
      
      return;
    }
    
    if(fileWriterUserEntityTagTimestamps == null){
    
      final FileOutputStream   out                     = SSFileU.openOrCreateFileWithPathForWrite (SSFileU.dirWorkingDataCsv() + par.fileName);
      final OutputStreamWriter writer                  = new OutputStreamWriter   (out,    Charset.forName(SSEncodingU.utf8));
      fileWriterUserEntityTagTimestamps                = new CSVWriter            (writer, SSStrU.semiColon.charAt(0));
    }
    
    final List<String>                              lineParts                    = new ArrayList<String>();
    final Iterator<Map.Entry<String, List<String>>> entitiesTagsIterator         = par.tagsPerEntities.entrySet().iterator();
    Map.Entry<String, List<String>>                 entityAndTags;
    
    while(entitiesTagsIterator.hasNext()){
      
      entityAndTags       = entitiesTagsIterator.next();

      lineParts.clear();
      
      lineParts.add(SSUri.toStr     (par.user));
      lineParts.add(entityAndTags.getKey());
      lineParts.add(Long.toString   (par.timestamp));
      lineParts.add(StringUtils.join(entityAndTags.getValue(),       SSStrU.comma));

      fileWriterUserEntityTagTimestamps.writeNext((String[]) lineParts.toArray(new String[lineParts.size()]));
    }
  }
  
  @Override
  public void dataExportUserEntityTagCategories(final SSServPar parA) throws Exception{
    
    final SSDataExportUserEntityTagCategoriesPar par = new SSDataExportUserEntityTagCategoriesPar(parA);
    
    if(par.wasLastLine){
      
      if(fileWriterUserEntityTagCategories != null){
        fileWriterUserEntityTagCategories.close();
        fileWriterUserEntityTagCategories = null;
      }
      
      return;
    }
    
    if(fileWriterUserEntityTagCategories == null){
    
      final FileOutputStream   out                     = SSFileU.openOrCreateFileWithPathForWrite (SSFileU.dirWorkingDataCsv() + par.fileName);
      final OutputStreamWriter writer                  = new OutputStreamWriter   (out,    Charset.forName(SSEncodingU.utf8));
      fileWriterUserEntityTagCategories    = new CSVWriter            (writer, SSStrU.semiColon.charAt(0));
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
      lineParts.add(StringUtils.join(entityAndTags.getValue(),       SSStrU.comma));
      lineParts.add(StringUtils.join(entityAndCategories.getValue(), SSStrU.comma));

      fileWriterUserEntityTagCategories.writeNext((String[]) lineParts.toArray(new String[lineParts.size()]));
    }
  }
  
  @Override
  public void dataExportUserEntityTagCategoryTimestamps(final SSServPar parA) throws Exception{
    
    final SSDataExportUserEntityTagCategoryTimestampPar par = new SSDataExportUserEntityTagCategoryTimestampPar(parA);
    
    if(par.wasLastLine){
      
      if(fileWriterUserEntityTagCategoryTimestamps != null){
        fileWriterUserEntityTagCategoryTimestamps.close();
        fileWriterUserEntityTagCategoryTimestamps = null;
      }
      
      return;
    }
    
    if(fileWriterUserEntityTagCategoryTimestamps == null){
    
      final FileOutputStream   out                     = SSFileU.openOrCreateFileWithPathForWrite (SSFileU.dirWorkingDataCsv() + par.fileName);
      final OutputStreamWriter writer                  = new OutputStreamWriter   (out,    Charset.forName(SSEncodingU.utf8));
      fileWriterUserEntityTagCategoryTimestamps    = new CSVWriter            (writer, SSStrU.semiColon.charAt(0));
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

      fileWriterUserEntityTagCategoryTimestamps.writeNext((String[]) lineParts.toArray(new String[lineParts.size()]));
    }
  }
}
