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
package at.kc.tugraz.ss.serv.dataimport.impl.evernote;

import at.kc.tugraz.socialserver.utils.SSFileExtE;
import at.kc.tugraz.socialserver.utils.SSFileU;
import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import com.evernote.edam.type.Note;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class SSDataImportEvernoteNoteContentHandler{
  
  private final SSUri   user;
  private final SSUri   noteUri;
  private final Note    note;
  private final String  localWorkPath;
  
  public SSDataImportEvernoteNoteContentHandler(
    final SSUri   user,
    final Note    note,
    final SSUri   noteUri,
    final String  localWorkPath){
    
    this.user          = user;
    this.note          = note;
    this.noteUri       = noteUri;
    this.localWorkPath = localWorkPath;
  }
  
   //TODO dtheiler: currently works with local file repository only (not web dav or any remote stuff; even dont if localWorkPath != local file repo path)
  public void handleNoteContent() throws Exception{
    
    String xhtmlFilePath = null;
    SSUri  fileUri       = null;
    String pdfFilePath;
    String textFilePath;
    
    try{
      
      try{
        
        xhtmlFilePath    = localWorkPath + SSServCaller.fileIDFromURI (user, SSServCaller.vocURICreate(SSFileExtE.xhtml));
        fileUri          = SSServCaller.vocURICreate                  (SSFileExtE.pdf);
        pdfFilePath      = localWorkPath + SSServCaller.fileIDFromURI (user, fileUri);
        
        SSFileU.writeStr              (note.getContent(), xhtmlFilePath);
        
        try{
          SSFileU.writePDFFromXHTML     (pdfFilePath,       xhtmlFilePath);
        }catch(Exception error){
          
          textFilePath = xhtmlFilePath;
          
          SSLogU.warn("PDF creation failed from evernote note content");
          
          String parsedXHTML = parseXHTML(xhtmlFilePath);
          
          SSFileU.writeFileText(new File(textFilePath), parsedXHTML);
          
          try{
            SSFileU.writePDFFromText     (pdfFilePath,  textFilePath);
          }catch(Exception error1){
            SSLogU.warn("PDF creation from parsed XHTML (now text) failed from evernote note content");
            throw error1;
          }
        }
      }catch(Exception error){
        SSServErrReg.regErrThrow(error);
        return;
      }finally{
        
        try{
          SSFileU.delFile(xhtmlFilePath);
        }catch(Exception error){}
      }
      
      SSServCaller.entityEntityToPrivCircleAdd(
        user, 
        fileUri, 
        SSEntityE.file, 
        null, 
        null, 
        null, 
        false);
       
      for(SSUri file : SSServCaller.entityFilesGet(user, noteUri)){

        SSServCaller.entityRemove(file, false);
        
        try{
          SSFileU.delFile(localWorkPath + SSServCaller.fileIDFromURI (user, file));  
        }catch(Exception error){
          SSLogU.warn("evernote note file couldnt be removed");
        }
      }
      
      SSServCaller.entityFileAdd(
        user,
        noteUri,
        fileUri,
        false);
      
      SSDataImportEvernoteThumbHelper.addThumbFromFile(
        user,
        localWorkPath,
        noteUri,
        fileUri,
        false);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static String parseXHTML(final String path) throws Exception{
    
    BufferedReader br     = null;
    String         result = SSStrU.empty;
    
    try{
      
      String line, text, tag, tmpTag, href, title;
      int tagIndex, tagEndIndex, hrefIndex, hrefEndIndex, titleIndex, titleEndIndex;

      br = new BufferedReader(new FileReader(new File(path)));
      
      while((line = br.readLine()) != null){
        
        line = line.trim();
        
        while(line.contains("<")){
          
          if(!line.contains(">")){
            throw new Exception("xhtml invalid");
          }
          
          tagIndex    = line.indexOf("<");
          tagEndIndex = line.indexOf(">");
          tag         = line.substring(tagIndex, tagEndIndex + 1);
          tmpTag = line.substring(tagIndex, tagEndIndex + 1);
          
          if(tagIndex != 0){
            
            text = line.substring(0, tagIndex).replace("&amp;nbsp;", SSStrU.empty).replace("Â", SSStrU.empty).trim();
            
            if(!text.isEmpty()){
              result += text + SSStrU.backslashRBackslashN;
            }
          }
          
          while(tmpTag.contains("href=\"")){
            
            hrefIndex = tmpTag.indexOf("href=\"");
            hrefEndIndex = tmpTag.indexOf("\"", hrefIndex + 6);
            href = tmpTag.substring(hrefIndex + 6, hrefEndIndex);
            
            if(tmpTag.contains("title=\"")){
              
              titleIndex = tmpTag.indexOf("title=\"");
              titleEndIndex = tmpTag.indexOf("\"", titleIndex + 7);
              title = tmpTag.substring(titleIndex + 7, titleEndIndex);
              
              result += title.replace("&amp;nbsp;", SSStrU.empty).replace("Â", SSStrU.empty) + ": " + SSStrU.backslashRBackslashN;
              
              tmpTag = tmpTag.substring(0, titleIndex) + tmpTag.substring(titleEndIndex + 1, tmpTag.length() - 1);
              
              hrefIndex = tmpTag.indexOf("href=\"");
              hrefEndIndex = tmpTag.indexOf("\"", hrefIndex + 6);
              href = tmpTag.substring(hrefIndex + 6, hrefEndIndex);
              
              tmpTag = tmpTag.substring(0, hrefIndex) + tmpTag.substring(hrefEndIndex + 1, tmpTag.length() - 1);
              
              result += href + SSStrU.backslashRBackslashN;
              
            }else{
              result += "link" + ": " + href + SSStrU.backslashRBackslashN;
              
              tmpTag = tmpTag.substring(0, hrefIndex) + tmpTag.substring(hrefEndIndex + 1, tmpTag.length() - 1);
            }
          }
          
          line = line.replace(tag, SSStrU.empty).replace("&amp;nbsp;", SSStrU.empty).replace("Â", SSStrU.empty).trim();
        }
        
        line = line.replace("&amp;nbsp;", SSStrU.empty).replace("Â", SSStrU.empty).trim();
        
        if(!line.isEmpty()){
          result += line + SSStrU.backslashRBackslashN;
        }
      }
      
      return result;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      if(br != null){
        br.close();
      }
    }
  }
}