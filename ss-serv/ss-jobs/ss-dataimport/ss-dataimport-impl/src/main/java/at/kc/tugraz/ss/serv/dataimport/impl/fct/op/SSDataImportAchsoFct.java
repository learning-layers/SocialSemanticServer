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
package at.kc.tugraz.ss.serv.dataimport.impl.fct.op;

import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.activity.ss.i5cloud.util.SSI5CloudU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.job.i5cloud.datatypes.SSi5CloudAchsoVideo;
import at.kc.tugraz.ss.serv.job.i5cloud.datatypes.enums.SSI5CloudAchsoVideoMetaDataE;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class SSDataImportAchsoFct{
  
  public static List<SSi5CloudAchsoVideo> getVideoObjs(
    final SSUri  userUri, 
    final String vidXML) throws Exception{
    
    try{
      final Document                  document     = DocumentHelper.parseText(vidXML);
      final Element                   rootElement  = document.getRootElement();
      final Iterator                  vidIterator  = rootElement.elementIterator();
      final List<SSi5CloudAchsoVideo> videos       = new ArrayList<SSi5CloudAchsoVideo>();
      Iterator                        vidContentIterator;
      Element                         vid;
      Element                         vidContent;
      String                          title;
      String                          video_uri;
      String                          creator;
      Long                            created_at;
      List<String>                    annotations;
      List<String>                    keywords;
      
      while(vidIterator.hasNext()){
        
        vid                = (Element) vidIterator.next();
        vidContentIterator = vid.elementIterator();
        title              = null;
        video_uri          = null;
        creator            = null;
        created_at         = null;
        annotations        = new ArrayList<String>();
        keywords           = new ArrayList<String>();
        
        while(vidContentIterator.hasNext()){
          
          vidContent = (Element) vidContentIterator.next();
          
          switch(SSI5CloudAchsoVideoMetaDataE.get(vidContent.getName())){
            
            case title:         title       = (String) vidContent.getData();                     break;
            case video_uri:     video_uri   = (String) vidContent.getData();                     break;
            case creator:       creator     = (String) vidContent.getData();                     break;
            case created_at:    created_at  = getCreationTime ((String) vidContent.getData());   break;
            case keywords:      keywords    = getKeywords     (vidContent.nodeIterator());       break;
            case annotations:   annotations = getAnnotations  (vidContent.nodeIterator());       break;
          }
        }
        
        try{
          videos.add(
            SSi5CloudAchsoVideo.get(
              SSLabel.get  (title),
              SSUri.get    (video_uri),
              SSLabel.get  (creator),
              created_at,
              keywords,
              annotations));
          
        }catch(Exception error){
          SSLogU.warn("couldnt add vid: " + video_uri);
        }
      }
      
      return videos;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private static List<String> getKeywords(Iterator keywordsIterator){
    
    final List<String> keywords = new ArrayList<String>();
    
    while(keywordsIterator.hasNext()){
      keywords.add((String)((Element) keywordsIterator.next()).getData());
    }
    
    return SSStrU.distinctWithoutEmptyAndNull(keywords);
  }
  
  private static List<String> getAnnotations(final Iterator annotationsIterator) throws Exception{
    
    final List<String> annotations = new ArrayList<String>();
    Iterator           annotContentIterator;
    Element            annotation;
    
    while(annotationsIterator.hasNext()){
      
      annotContentIterator = ((Element) annotationsIterator.next()).nodeIterator();
      
      while(annotContentIterator.hasNext()){
        
        annotation = (Element) annotContentIterator.next();
        
        try{
          switch(SSI5CloudAchsoVideoMetaDataE.get(annotation.getName())){
            case semanticRefId: annotations.add(annotation.getText()); break;
          }              
        }catch(Exception error){}
      }
    }
    
    return new ArrayList<String>();
      
      //TODO: dtheiler include annotations again
//      SSStrU.distinctWithoutEmptyAndNull(
//      SSStrU.toList(
//        SSServCaller.i5CloudAchsoSemanticAnnotationsSetGet(
//        SSStrU.toStringArray(
//          SSStrU.distinctWithoutEmptyAndNull(annotations)))));
  }
  
  private static Long getCreationTime(
    final String dateAsString) throws Exception{
    return new SimpleDateFormat(SSI5CloudU.achsoDateFormat, Locale.US).parse(dateAsString).getTime();
  }
}
