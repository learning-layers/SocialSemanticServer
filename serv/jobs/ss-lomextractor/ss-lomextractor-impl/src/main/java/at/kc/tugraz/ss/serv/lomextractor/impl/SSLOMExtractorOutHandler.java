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
package at.kc.tugraz.ss.serv.lomextractor.impl;

import at.kc.tugraz.socialserver.utils.SSFileU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSEncodingU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagLabel;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.lomextractor.conf.SSLOMExtractorConf;
import at.kc.tugraz.ss.serv.lomextractor.datatypes.SSLOMConceptRelation;
import at.kc.tugraz.ss.serv.lomextractor.datatypes.SSLOMKeyword;
import at.kc.tugraz.ss.serv.lomextractor.datatypes.SSLOMResource;
import at.kc.tugraz.ss.serv.lomextractor.datatypes.SSLOMUser;
import at.kc.tugraz.ss.serv.serv.api.SSServImplA;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.tag.datatypes.SSTag;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSLOMExtractorOutHandler{
  
  public SSLOMExtractorOutHandler(final SSServImplA serv){
  }
  
  public void writeResourcesToLangFile(
    Map<String, SSLOMResource> resources,
    SSLOMExtractorConf         conf,
    String                     langToPrint) throws Exception{ 
    
    FileOutputStream   out       = new FileOutputStream   (SSFileU.dirWorkingDataCsv() + langToPrint + SSStrU.underline + conf.outputResourceLangFileName);
    OutputStreamWriter writer    = new OutputStreamWriter (out, Charset.forName(SSEncodingU.utf8));
    CSVWriter          csvWriter = new CSVWriter(writer, SSStrU.comma.charAt(0));
    List<String>       lineParts;
    String             descString;
    
    writer.write("uri,id,lang,title,description" + SSStrU.backslashN);
      
    for(SSLOMResource resource : resources.values()){
      
      resource.distinctValues();
      
      if(notValidToBeWritten(resource)){
        continue;
      }
      
      lineParts     = new ArrayList<>();
      descString    = SSStrU.empty;
      
      lineParts.add(resource.uri);
      lineParts.add(resource.id);
      lineParts.add(langToPrint);
      lineParts.add(resource.titles.get(0).titleInLang(resource.titles, langToPrint).label);                            
      
      if(resource.descs.size() >= 1){

        descString = resource.descs.get(0).descInLang(resource.descs,  langToPrint).label;
        descString = SSStrU.replaceAllLineFeedsWithTextualRepr(descString);
      }
      
      lineParts.add(descString);
      
      csvWriter.writeNext((String[]) lineParts.toArray(new String[lineParts.size()]));
    }
    
    csvWriter.close();
  }
  
  public void writeResourcesToVizInputFile(
    Map<String, SSLOMResource> resources,
    SSLOMExtractorConf         conf) throws Exception{
    
    FileOutputStream   out       = new FileOutputStream   (SSFileU.dirWorkingDataCsv() + conf.outputResourceMetadataFileName);
    OutputStreamWriter writer    = new OutputStreamWriter (out, Charset.forName(SSEncodingU.utf8));
    CSVWriter          csvWriter = new CSVWriter(writer, SSStrU.semiColon.charAt(0));
    List<String>       lineParts;
    String             keywordString;
    String             conceptString;
    String             descString;
    String             titleString;
    
    writer.write("id;title;description;tags;concepts;uri" + SSStrU.backslashN);
      
    for(SSLOMResource resource : resources.values()){
      
      resource.distinctValues();
      
      if(notValidToBeWritten(resource)){
        continue;
      }
      
      lineParts     = new ArrayList<>();
      keywordString = SSStrU.empty;
      conceptString = SSStrU.empty;
      descString    = SSStrU.empty;
      
      lineParts.add(resource.id);
      
      titleString = resource.titles.get(0).titleInLangWithDefault(resource.titles, SSStrU.valueEn).label;
      titleString = SSStrU.replaceAllLineFeedsWithTextualRepr(titleString);
      
      lineParts.add(titleString);                           
      
      if(resource.descs.size() >= 1){

        descString = resource.descs.get(0).descInLangWithDefault  (resource.descs,  SSStrU.valueEn).label;
        descString = SSStrU.replaceAllLineFeedsWithTextualRepr(descString);
      }
      
      lineParts.add(descString);
      
      for(SSLOMKeyword keyword : resource.keywords){
        keywordString += keyword.label + SSStrU.semiColon;
      }
      
      keywordString = SSStrU.removeTrailingString(keywordString, SSStrU.semiColon);
      
      lineParts.add(keywordString);
      
      for(SSLOMConceptRelation conceptRelation : resource.conceptRelatios){
        conceptString += conceptRelation.concept + SSStrU.semiColon;
      }
      
      conceptString = SSStrU.removeTrailingString(conceptString, SSStrU.semiColon);
      
      lineParts.add(conceptString);
      lineParts.add(resource.uri);
      
      csvWriter.writeNext((String[]) lineParts.toArray(new String[lineParts.size()]));
    }
    
    csvWriter.close();
  }
  
  public void writeResourcesToRecommInputFile(
    Map<String, SSLOMResource> resources,
    SSLOMExtractorConf         conf) throws Exception{
    
    List<SSTag>               tags;
    Map<String, List<String>> tagsPerEntities;
    Map<String, List<String>> categoriesPerEntities;
    
    for(SSLOMResource resource : resources.values()){
      
      resource.distinctValues();
      
      if(notValidToBeWritten(resource)){
        continue;
      }
      
      tags = new ArrayList<>();
      
      for(String keyword : SSStrU.distinctWithoutEmptyAndNull(resource.keywords)){
        
        tags.add(
          SSTag.get(
            null,
            SSUri.get(resource.id),
            null,
            null,
            SSTagLabel.get(keyword)));
      }
      
      tagsPerEntities       = SSTag.getTagLabelsPerEntities(tags);
      categoriesPerEntities = new HashMap<>();
        
      categoriesPerEntities.put(resource.id, SSLOMConceptRelation.concepts (resource.conceptRelatios));
      
      for(SSLOMUser user : resource.users){
       
        SSServCaller.dataExportUserEntityTagCategoryTimestamps(
          SSUri.get(SSServCaller.vocURICreate("user") + user.fullName), 
          tagsPerEntities, 
          categoriesPerEntities, 
          0L, 
          conf.outputResourceRecommFileName,
          false);
      }
      
      SSServCaller.dataExportUserEntityTagCategoryTimestamps(
          null, 
          new HashMap<>(), 
          new HashMap<>(), 
          null, 
          conf.outputResourceRecommFileName,
          true);
    }
  }
  
  public static boolean notValidToBeWritten(SSLOMResource resource){
    return !validToBeWritten(resource);
  }
  
  private static boolean validToBeWritten(SSLOMResource resource){
    
    if(
      SSObjU.isNull  (resource)       ||
      SSStrU.isEmpty (resource.uri)   ||
      resource.keywords.size() <= 0   ||
      resource.users.size()    <= 0   ||
      resource.titles.size()   <= 0){
      return false;
    }
    
    return true;
  }
}
