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
import at.kc.tugraz.socialserver.utils.SSMimeTypeU;
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
import java.util.HashMap;
import java.util.Map;

public class SSDataImportEvernoteNoteContentHandler{
  
  private static final Map<String, SSUri> hashsPerFileURIs = new HashMap<>();
  
  private final SSUri           user;
  private final SSUri           noteUri;
  private final Note            note;
  private final NoteStoreClient noteStore;
  private final String          localWorkPath;
  
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
          downnloadNoteResourcesAndFillXHTMLWithLocalImageLinks(xhtmlFilePath),
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
            reduceXHTMLToTextAndImage(xhtmlFilePath),
            xhtmlFilePath);
          
          SSFileU.writeStr(
            downnloadNoteResourcesAndFillXHTMLWithLocalImageLinks(xhtmlFilePath),
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
  
  public static String reduceXHTMLToTextAndImage(final String path) throws Exception{
    
    BufferedReader br     = null;
    String         result = SSStrU.empty;
    String         mediaTag;
    int            mediaStartIndex;
    int            mediaEndIndex;
    
    try{
      
      result +=
        "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n" +
        "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
        "\n" +
        "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
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
              result += "<div>" + text + "</div>" + SSStrU.backslashRBackslashN;
            }
          }
          
          if(tmpTag.startsWith("<en-media")){
            
            mediaStartIndex    = tmpTag.indexOf("<en-media");
            mediaEndIndex      = tmpTag.indexOf(">");

            if(mediaEndIndex != -1){
              mediaTag    = tmpTag.substring(mediaStartIndex, mediaEndIndex + 1);
              
              if(
                !mediaTag.endsWith("/>") &&
                mediaTag.length() > 2){
                
                result += mediaTag.substring(0, mediaTag.length() - 1) + "/>";
              }else{
                result += mediaTag;
              }
              
              line = line.replace(mediaTag, SSStrU.empty).replace("&amp;nbsp;", SSStrU.empty).replace("Â", SSStrU.empty).trim();
              continue;
            }
          }
          
          while(tmpTag.contains("href=\"")){
            
            hrefIndex    = tmpTag.indexOf("href=\"");
            hrefEndIndex = tmpTag.indexOf("\"", hrefIndex + 6);
            href         = tmpTag.substring(hrefIndex + 6, hrefEndIndex);
            
            if(tmpTag.contains("title=\"")){
              
              titleIndex    = tmpTag.indexOf("title=\"");
              titleEndIndex = tmpTag.indexOf("\"", titleIndex + 7);
              title         = tmpTag.substring(titleIndex + 7, titleEndIndex);
              title         = title.replace("&amp;nbsp;", SSStrU.empty).replace("Â", SSStrU.empty);
              
              tmpTag       = tmpTag.substring(0, titleIndex) + tmpTag.substring(titleEndIndex + 1, tmpTag.length() - 1);
              hrefIndex    = tmpTag.indexOf("href=\"");
              hrefEndIndex = tmpTag.indexOf("\"", hrefIndex + 6);
              href         = tmpTag.substring(hrefIndex + 6, hrefEndIndex);
              
              tmpTag = tmpTag.substring(0, hrefIndex) + tmpTag.substring(hrefEndIndex + 1, tmpTag.length() - 1);
              
              result += "<div>" + "<a href=\"" + href + "\">" + title + "</a>" + "</div>" + SSStrU.backslashRBackslashN;
              
            }else{
              result += "<div>" + "<a href=\"" + href + "\">" + href + "</a>" + "</div>" + SSStrU.backslashRBackslashN;
                
              tmpTag = tmpTag.substring(0, hrefIndex) + tmpTag.substring(hrefEndIndex + 1, tmpTag.length() - 1);
            }
          }
          
          line = line.replace(tag, SSStrU.empty).replace("&amp;nbsp;", SSStrU.empty).replace("Â", SSStrU.empty).trim();
        }
        
        line = line.replace("&amp;nbsp;", SSStrU.empty).replace("Â", SSStrU.empty).trim();
        
        if(!line.isEmpty()){
          result += "<div>" + line + "</div>" + SSStrU.backslashRBackslashN;
        }
      }
      
      result +=
        "</body>\n"
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
  
  private String downnloadNoteResourcesAndFillXHTMLWithLocalImageLinks(
    final String path) throws Exception{
    
    BufferedReader lineReader      = null;
    String         result          = SSStrU.empty;
    SSUri          fileURI         = null;
    String         fileID          = null;
    Resource       resource        = null;
    SSUri          thumbnailURI;
    String         line;
    String         tmpLine;
    String         hash;
    String         mimeType;
    int            startIndex;
    int            endIndex1;
    int            endIndex2;
    int            endIndex;
    int            hashIndex;
    int            hashEndIndex;
    
    try{
      
      lineReader = new BufferedReader(new FileReader(new File(path)));
      
      while((line = lineReader.readLine()) != null){
        
        line = line.trim();
        
        tmpLine = line;
        
        while(tmpLine.contains("<en-media")){
          
          startIndex = tmpLine.indexOf("<en-media");
          
          if(
            !tmpLine.contains("</en-media>") &&
            !tmpLine.contains("/>")){
            
            result += tmpLine;
            break; //xhtml invalid
          }
          
          if(!tmpLine.contains("hash=\"")){
            result += tmpLine;
            break;
          }
          
          endIndex1 = tmpLine.indexOf("</en-media>", startIndex);
          endIndex2 = tmpLine.indexOf("/>",          startIndex);
          
          if(endIndex1 != -1){
            endIndex = endIndex1;
          }else{
            endIndex = endIndex2;
          }

          mimeType = null;
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeU.textPlain + "\"")  &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeU.textPlain  + "\"")){
            mimeType = SSMimeTypeU.textPlain;
          }
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeU.textVcard + "\"")  &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeU.textVcard  + "\"")){
            mimeType = SSMimeTypeU.textVcard;
          }
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeU.audioAmr + "\"")  &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeU.audioAmr  + "\"")){
            mimeType = SSMimeTypeU.audioAmr;
          }
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeU.audioMpeg4 + "\"")  &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeU.audioMpeg4  + "\"")){
            mimeType = SSMimeTypeU.audioMpeg4;
          }
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeU.textHtml + "\"")  &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeU.textHtml  + "\"")){
            mimeType = SSMimeTypeU.textHtml;
          }
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeU.applicationBin + "\"")  &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeU.applicationBin  + "\"")){
            mimeType = SSMimeTypeU.applicationBin;
          }
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeU.imageJpeg + "\"")  &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeU.imageJpeg  + "\"")){
            mimeType = SSMimeTypeU.imageJpeg;
          }
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeU.imagePng + "\"") &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeU.imagePng + "\"")){
            mimeType = SSMimeTypeU.imagePng;
          }
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeU.imageGif  + "\"")  &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeU.imageGif  + "\"")){
            mimeType = SSMimeTypeU.imageGif;
          }
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeU.applicationPdf  + "\"") &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeU.applicationPdf  + "\"")){
            mimeType = SSMimeTypeU.applicationPdf;
          } 
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeU.applicationMsword2007  + "\"") &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeU.applicationMsword2007  + "\"")){
            mimeType = SSMimeTypeU.applicationMsword2007;
          } 
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeU.applicationMsword  + "\"") &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeU.applicationMsword  + "\"")){
            mimeType = SSMimeTypeU.applicationMsword;
          } 
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeU.applicationMspowerpoint2007  + "\"") &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeU.applicationMspowerpoint2007  + "\"")){
            mimeType = SSMimeTypeU.applicationMspowerpoint2007;
          } 
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeU.applicationMspowerpoint  + "\"") &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeU.applicationMspowerpoint  + "\"")){
            mimeType = SSMimeTypeU.applicationMspowerpoint;
          } 
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeU.applicationMsexcel2007 + "\"") &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeU.applicationMsexcel2007  + "\"")){
            mimeType = SSMimeTypeU.applicationMsexcel2007;
          } 
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeU.applicationMsexcel  + "\"") &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeU.applicationMsexcel  + "\"")){
            mimeType = SSMimeTypeU.applicationMsexcel;
          } 
          
          if(mimeType == null){
             
            SSLogU.warn("no / unknown mime type set in:"  + tmpLine);
            
            if(endIndex == endIndex1){
              result += tmpLine.substring(0, endIndex + 11);
              tmpLine = tmpLine.substring(endIndex + 11);
            }else{
              
              result += tmpLine.substring(0, endIndex + 2);
              tmpLine = tmpLine.substring(endIndex + 2);
            }
            
            continue;
          }
          
          hashIndex = tmpLine.indexOf("hash=\"");
          
          if(!(tmpLine.contains("hash=\"") && endIndex > hashIndex)){
            
            if(endIndex == endIndex1){
              result += tmpLine.substring(0, endIndex + 11);
              tmpLine = tmpLine.substring(endIndex + 11);
            }else{
              result += tmpLine.substring(0, endIndex + 2);
              tmpLine = tmpLine.substring(endIndex + 2);
            }
            
            continue;
          }
          
          if(
            SSStrU.equals(mimeType, SSMimeTypeU.imageJpeg)      ||
            SSStrU.equals(mimeType, SSMimeTypeU.imagePng)       ||
            SSStrU.equals(mimeType, SSMimeTypeU.imageGif)       ||
            SSStrU.equals(mimeType, SSMimeTypeU.applicationPdf)){

            hashEndIndex = tmpLine.indexOf("\"", hashIndex + 6);
            hash         = tmpLine.substring(hashIndex + 6, hashEndIndex);
            fileURI      = SSServCaller.vocURICreate(SSFileExtE.valueOf(SSMimeTypeU.fileExtForMimeType(mimeType)));
            fileID       = SSServCaller.fileIDFromURI(user, fileURI);
            
            resource =
              SSServCaller.evernoteResourceByHashGet(
                user,
                noteStore,
                note.getGuid(),
                hash);
            
            SSFileU.writeFileBytes(
              new FileOutputStream(localWorkPath + fileID),
              resource.getData().getBody(),
              resource.getData().getSize());
          }
          
          result += tmpLine.substring(0, startIndex);
          
          if(SSStrU.equals(mimeType, SSMimeTypeU.applicationPdf)){
            thumbnailURI = SSDataImportEvernoteThumbHelper.createThumbnail(user, localWorkPath, fileURI, 500, 500);
            fileID       = SSServCaller.fileIDFromURI(user, thumbnailURI);
            
            result +=
              "<div>Included PDF (preview):</div>" +
              "<img width=\"" +
              500 +
              "\" height=\"" +
              500 +
              "\" class=\"xmyImagex\" src=\"" +
              localWorkPath + fileID +
              "\"/>";
          }
          
          if(
            resource != null                                    &&
            !SSStrU.equals(mimeType, SSMimeTypeU.applicationPdf)){
          
            result +=
              "<img width=\"" +
              resource.getWidth() +
              "\" height=\"" +
              resource.getHeight() +
              "\" class=\"xmyImagex\" src=\"" +
              localWorkPath + fileID +
              "\"/>";
          }
          
          if(
            SSStrU.equals(mimeType, SSMimeTypeU.applicationMsword) ||
            SSStrU.equals(mimeType, SSMimeTypeU.applicationMsword2007)){
            
            result += "<div>Includes Microsoft Office Document (no preview available)</div>";
          }
          
          if(
            SSStrU.equals(mimeType, SSMimeTypeU.applicationMspowerpoint) ||
            SSStrU.equals(mimeType, SSMimeTypeU.applicationMspowerpoint2007)){

            result += "<div>Includes Microsoft Office Powerpoint Document (no preview available)</div>";
          }
          
          if(
            SSStrU.equals(mimeType, SSMimeTypeU.applicationMsexcel) ||
            SSStrU.equals(mimeType, SSMimeTypeU.applicationMsexcel2007)){

            result += "<div>Includes Microsoft Office Excel Document (no preview available)</div>";
          }
          
          if(tmpLine.contains("type=\"" + SSMimeTypeU.textPlain + "\"")){
            result += "<div>Includes Plain Text Document (no preview available)</div>";
          }
          
          if(tmpLine.contains("type=\"" + SSMimeTypeU.textVcard + "\"")){
            result += "<div>Includes VCard (no preview available)</div>";
          }
          
          if(tmpLine.contains("type=\"" + SSMimeTypeU.audioAmr + "\"")){
            result += "<div>Includes Adaptive Multi-Rate Audio (no preview available)</div>";
          }
          
          if(tmpLine.contains("type=\"" + SSMimeTypeU.audioMpeg4 + "\"")){
            result += "<div>Includes MPEG 4 Audio (no preview available)</div>";
          }
          
          if(tmpLine.contains("type=\"" + SSMimeTypeU.textHtml + "\"")){
            result += "<div>Includes HTML Page (no preview available)</div>";
          }
          
          if(tmpLine.contains("type=\"" + SSMimeTypeU.applicationBin + "\"")){
            result += "<div>Includes Application File (no preview available)</div>";
          }
          
          if(endIndex == endIndex1){
            tmpLine = tmpLine.substring(endIndex + 11, tmpLine.length());
          }else{
            tmpLine = tmpLine.substring(endIndex + 2, tmpLine.length());
          }
        }
        
        result += tmpLine;
        result += SSStrU.backslashRBackslashN;
      }
      
      return result.replace("&amp;nbsp;", SSStrU.empty).replace("Â", SSStrU.empty).trim();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      if(lineReader != null){
        lineReader.close();
      }
    }
  }
}