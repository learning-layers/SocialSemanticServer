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
import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

public class SSDataImportEvernoteNoteContentHandler{
  
  private final SSUri   user;
  private final SSUri   noteUri;
  private final Note    note;
  private final NoteStoreClient noteStore;
  private final String  localWorkPath;
  
  public SSDataImportEvernoteNoteContentHandler(
    final SSUri   user,
    final Note    note,
    final SSUri   noteUri,
    final NoteStoreClient noteStore,
    final String  localWorkPath){
    
    this.user          = user;
    this.note          = note;
    this.noteUri       = noteUri;
    this.noteStore     = noteStore;
    this.localWorkPath = localWorkPath;
  }
  
  //TODO dtheiler: currently works with local file repository only (not web dav or any remote stuff; even dont if localWorkPath != local file repo path)
  public void handleNoteContent() throws Exception{
    
    String                    xhtmlFilePath;
    SSUri                     fileUri;
    String                    pdfFilePath;
    
    try{
      
      xhtmlFilePath    = localWorkPath + SSServCaller.fileIDFromURI (user, SSServCaller.vocURICreate(SSFileExtE.xhtml));
      fileUri          = SSServCaller.vocURICreate                  (SSFileExtE.pdf);
      pdfFilePath      = localWorkPath + SSServCaller.fileIDFromURI (user, fileUri);
      
      SSFileU.writeStr(
        note.getContent(), 
        xhtmlFilePath);
      
      try{
        
        SSFileU.writeStr(
          fillXHTMLWithImageLinks(xhtmlFilePath), 
          xhtmlFilePath);
        
        SSFileU.writePDFFromXHTML(
          pdfFilePath,
          xhtmlFilePath, 
          true);
        
      }catch(Exception error){
        
        SSServErrReg.reset();
        SSLogU.warn("PDF from XHTML failed");
        
        try{
          SSFileU.writeStr(
            parseXHTML(xhtmlFilePath), 
            xhtmlFilePath);
          
          SSFileU.writeStr(
            fillXHTMLWithImageLinks(xhtmlFilePath), 
            xhtmlFilePath);
          
          SSFileU.writePDFFromXHTML(
            pdfFilePath,
            xhtmlFilePath, 
            true);
          
        }catch(Exception error1){
          
          SSServErrReg.reset();
          SSLogU.warn("PDF from reduced XHTML failed");
          return;
        }
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
      
      result +=
        "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n" +
        "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
        "\n" +
        "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
        "\n" +
        "<head>\n" +
        "  <title>Title of document</title>\n" +
        "</head>\n" +
        "\n" +
        "<body>\n";
      
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
          tmpTag      = line.substring(tagIndex, tagEndIndex + 1);
          
          if(tagIndex != 0){
            
            text = line.substring(0, tagIndex).replace("&amp;nbsp;", SSStrU.empty).replace("Â", SSStrU.empty).trim();
            
            if(!text.isEmpty()){
              result += text + SSStrU.backslashRBackslashN;
            }
          }
          
          String mediaTag;
          if(tmpTag.contains("<en-media")){
            
            tagIndex    = tmpTag.indexOf("<en-media");
            tagEndIndex = tmpTag.indexOf(">");
            
            if(tagEndIndex != -1){
              mediaTag    = tmpTag.substring(tagIndex, tagEndIndex);
              result += mediaTag  + "></en-media>";
            }
          }
          
          while(tmpTag.contains("href=\"")){
            
            hrefIndex    = tmpTag.indexOf("href=\"");
            hrefEndIndex = tmpTag.indexOf("\"", hrefIndex + 6);
            href         = tmpTag.substring(hrefIndex + 6, hrefEndIndex);
            
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
      
      result += 
        "</body>\n"
        + "\n"
        + "</html>";
      
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
  
  private String fillXHTMLWithImageLinks(
    final String              path) throws Exception{
    
    BufferedReader br     = null;
    String         result = SSStrU.empty;
    
    try{
      
      String line, mediaTag, hash;
      int tagIndex, tagEndIndex, hashIndex, hashEndIndex;
      
      br = new BufferedReader(new FileReader(new File(path)));
      
      while((line = br.readLine()) != null){
        
        line = line.trim();
        
        if(!line.contains("<en-media")){
          result += line + SSStrU.backslashRBackslashN;
          continue;
        }
        
        if(!line.contains("</en-media>")){
          result += line + SSStrU.backslashRBackslashN;
          continue;
          //throw new Exception("xhtml invalid"); ///>
        }
        
        tagIndex    = line.indexOf("<en-media");
        tagEndIndex = line.indexOf("</en-media>");
        mediaTag    = line.substring(tagIndex + 10, tagEndIndex);
        
        if(!line.contains("type=\"image/png\"")){ //application/pdf  //application/vnd.openxmlformats-officedocument.presentationml.presentation //"image/jpeg" //application/msword //application/vnd.openxmlformats-officedocument.wordprocessingml.document
          result += line + SSStrU.backslashRBackslashN;
          continue;
        }
        
        if(!line.contains("hash=\"")){
          result += line + SSStrU.backslashRBackslashN;
          continue;
        }
        
        hashIndex    = line.indexOf("hash=\"");
        hashEndIndex = line.indexOf("\"", hashIndex + 6);
        hash         = line.substring(hashIndex + 6, hashEndIndex);
        
        String fileURI         = SSStrU.toStr(SSServCaller.vocURICreate(SSFileExtE.png));
        String fileID          = SSServCaller.fileIDFromURI(user, SSUri.get(fileURI));
        
        Resource resource =
          SSServCaller.evernoteResourceByHashGet(
            user,
            noteStore,
            note.getGuid(),
            hash);
        
        SSFileU.writeFileBytes(
          new FileOutputStream(localWorkPath + fileID),
          resource.getData().getBody(),
          resource.getData().getSize());
        
        //<div style=\"width:200px;height:200px;\"> </div>
        String hashReplacement = "<img class=\"xmyImagex\" width=\"" + resource.getWidth() + "\" height=\"" + resource.getHeight() + "\" src=\"" + localWorkPath + fileID + "\"/>";
        
//        String hashReplacement =
////          "<div style=\"position:absolute;top:0;left:0;border-color:transparent;\">" +
//          "<div class=\"xmyImagex\" width=\"" +
//          resource.getWidth() +
//          "\" height=\"" +
//          resource.getHeight() +
//          "\" href=\"" +
//          localWorkPath + fileID +
//          "\"/>"; //+
////          "</div>;";
        
        line =
          line.substring(0, tagIndex) +
          hashReplacement +
          line.substring(tagEndIndex + 11, line.length());
        
        result += line + SSStrU.backslashRBackslashN;
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