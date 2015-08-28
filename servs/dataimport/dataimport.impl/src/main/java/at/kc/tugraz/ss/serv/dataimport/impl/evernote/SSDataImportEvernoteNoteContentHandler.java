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

import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.filerepo.api.SSFileRepoServerI;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSFileExtE;
import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSMimeTypeE;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFileAddPar;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFilesGetPar;
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
      
      xhtmlFilePath    = 
        localWorkPath + 
        SSVocConf.fileIDFromSSSURI(
            SSServCaller.vocURICreate(SSFileExtE.xhtml));
      
      fileUri          = SSServCaller.vocURICreate                  (SSFileExtE.pdf);
      pdfFilePath      = localWorkPath + 
        SSVocConf.fileIDFromSSSURI(
            fileUri);
      
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
      
      ((SSEntityServerI) SSServReg.getServ(SSEntityServerI.class)).entityUpdate(
        new SSEntityUpdatePar(
          user,
          fileUri,
          SSEntityE.file, //type,
          null, //label
          null, //description,
          null, //creationTime,
          null, //read,
          false, //setPublic
          true, //withUserRestriction
          false)); //shouldCommit)
          
      for(SSEntity file :
        ((SSFileRepoServerI) SSServReg.getServ(SSFileRepoServerI.class)).filesGet(
          new SSEntityFilesGetPar(
            user,
            noteUri, //entity
            true, //withUserRestriction
            false))){ //invokeEntityHandlers
        
        SSServCaller.entityRemove(file.id, false);
        
        try{
          SSFileU.delFile(localWorkPath + SSVocConf.fileIDFromSSSURI(file.id));
          
        }catch(Exception error){
          SSLogU.warn("evernote note file couldnt be removed");
        }
      }
      
      ((SSFileRepoServerI) SSServReg.getServ(SSFileRepoServerI.class)).fileAdd(
        new SSEntityFileAddPar(
          user, 
          fileUri, //file
          null, //label
          noteUri, //entity
          true, //withUserRestriction
          false));//shouldCommit
      
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
    SSMimeTypeE    mimeType;
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
            tmpLine.contains("type=\"" + SSMimeTypeE.applicationZip + "\"")  &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeE.applicationZip  + "\"")){
            mimeType = SSMimeTypeE.applicationZip;
          }
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeE.textPlain + "\"")  &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeE.textPlain  + "\"")){
            mimeType = SSMimeTypeE.textPlain;
          }
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeE.textVcard + "\"")  &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeE.textVcard  + "\"")){
            mimeType = SSMimeTypeE.textVcard;
          }
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeE.audioAmr + "\"")  &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeE.audioAmr  + "\"")){
            mimeType = SSMimeTypeE.audioAmr;
          }
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeE.audioWav + "\"")  &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeE.audioWav  + "\"")){
            mimeType = SSMimeTypeE.audioWav;
          }
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeE.audioMpeg4 + "\"")  &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeE.audioMpeg4  + "\"")){
            mimeType = SSMimeTypeE.audioMpeg4;
          }
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeE.audioMpeg + "\"")  &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeE.audioMpeg  + "\"")){
            mimeType = SSMimeTypeE.audioMpeg;
          }
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeE.textHtml + "\"")  &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeE.textHtml  + "\"")){
            mimeType = SSMimeTypeE.textHtml;
          }
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeE.applicationBin + "\"")  &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeE.applicationBin  + "\"")){
            mimeType = SSMimeTypeE.applicationBin;
          }
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeE.imageJpeg + "\"")  &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeE.imageJpeg  + "\"")){
            mimeType = SSMimeTypeE.imageJpeg;
          }
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeE.imagePng + "\"") &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeE.imagePng + "\"")){
            mimeType = SSMimeTypeE.imagePng;
          }
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeE.imageGif  + "\"")  &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeE.imageGif  + "\"")){
            mimeType = SSMimeTypeE.imageGif;
          }
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeE.imageTiff  + "\"")  &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeE.imageTiff  + "\"")){
            mimeType = SSMimeTypeE.imageTiff;
          }
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeE.applicationPdf  + "\"") &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeE.applicationPdf  + "\"")){
            mimeType = SSMimeTypeE.applicationPdf;
          } 
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeE.applicationMsword2007  + "\"") &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeE.applicationMsword2007  + "\"")){
            mimeType = SSMimeTypeE.applicationMsword2007;
          } 
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeE.applicationMsword  + "\"") &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeE.applicationMsword  + "\"")){
            mimeType = SSMimeTypeE.applicationMsword;
          } 
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeE.applicationMspowerpoint2007  + "\"") &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeE.applicationMspowerpoint2007  + "\"")){
            mimeType = SSMimeTypeE.applicationMspowerpoint2007;
          } 
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeE.applicationMspowerpoint  + "\"") &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeE.applicationMspowerpoint  + "\"")){
            mimeType = SSMimeTypeE.applicationMspowerpoint;
          } 
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeE.applicationMsexcel2007 + "\"") &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeE.applicationMsexcel2007  + "\"")){
            mimeType = SSMimeTypeE.applicationMsexcel2007;
          } 
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeE.applicationMsexcel  + "\"") &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeE.applicationMsexcel  + "\"")){
            mimeType = SSMimeTypeE.applicationMsexcel;
          } 
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeE.applicationMsexcelBinary  + "\"") &&
            endIndex > tmpLine.indexOf("type=\"" + SSMimeTypeE.applicationMsexcelBinary  + "\"")){
            mimeType = SSMimeTypeE.applicationMsexcelBinary;
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
            SSStrU.equals(mimeType, SSMimeTypeE.imageJpeg)      ||
            SSStrU.equals(mimeType, SSMimeTypeE.imagePng)       ||
            SSStrU.equals(mimeType, SSMimeTypeE.imageGif)       ||
            SSStrU.equals(mimeType, SSMimeTypeE.imageTiff)      ||
            SSStrU.equals(mimeType, SSMimeTypeE.applicationPdf)){

            hashEndIndex = tmpLine.indexOf            ("\"", hashIndex + 6);
            hash         = tmpLine.substring          (hashIndex + 6, hashEndIndex);
            fileURI      = SSServCaller.vocURICreate  (SSMimeTypeE.fileExtForMimeType(mimeType));
            fileID       = SSVocConf.fileIDFromSSSURI (fileURI);
            
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
          
          if(SSStrU.equals(mimeType, SSMimeTypeE.applicationPdf)){
            
            thumbnailURI = 
              SSDataImportEvernoteThumbHelper.createThumbnail(
                user, 
                localWorkPath, 
                fileURI, 
                500, 
                500);
            
            fileID = SSVocConf.fileIDFromSSSURI(thumbnailURI);
            
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
            !SSStrU.equals(mimeType, SSMimeTypeE.applicationPdf)){
          
            result +=
              "<img width=\"" +
              resource.getWidth() +
              "\" height=\"" +
              resource.getHeight() +
              "\" class=\"xmyImagex\" src=\"" +
              localWorkPath + fileID +
              "\"/>";
          }
          
          if(SSStrU.equals(mimeType, SSMimeTypeE.applicationZip)){
            result += "<div>Includes Compressed Archive (no preview available)</div>";
          }
          
          if(
            SSStrU.equals(mimeType, SSMimeTypeE.applicationMsword) ||
            SSStrU.equals(mimeType, SSMimeTypeE.applicationMsword2007)){
            
            result += "<div>Includes Microsoft Office Document (no preview available)</div>";
          }
          
          if(
            SSStrU.equals(mimeType, SSMimeTypeE.applicationMspowerpoint) ||
            SSStrU.equals(mimeType, SSMimeTypeE.applicationMspowerpoint2007)){

            result += "<div>Includes Microsoft Office Powerpoint Document (no preview available)</div>";
          }
          
          if(
            SSStrU.equals(mimeType, SSMimeTypeE.applicationMsexcel) ||
            SSStrU.equals(mimeType, SSMimeTypeE.applicationMsexcel2007)){

            result += "<div>Includes Microsoft Office Excel Document (no preview available)</div>";
          }
          
          if(tmpLine.contains("type=\"" + SSMimeTypeE.textPlain + "\"")){
            result += "<div>Includes Plain Text Document (no preview available)</div>";
          }
          
          if(tmpLine.contains("type=\"" + SSMimeTypeE.textVcard + "\"")){
            result += "<div>Includes VCard (no preview available)</div>";
          }
          
          if(tmpLine.contains("type=\"" + SSMimeTypeE.audioAmr + "\"")){
            result += "<div>Includes Adaptive Multi-Rate Audio (no preview available)</div>";
          }
          
          if(tmpLine.contains("type=\"" + SSMimeTypeE.audioWav + "\"")){
            result += "<div>Includes Wave Audio (no preview available)</div>";
          }
          
          if(
            tmpLine.contains("type=\"" + SSMimeTypeE.audioMpeg4 + "\"") ||
            tmpLine.contains("type=\"" + SSMimeTypeE.audioMpeg  + "\"")){
            result += "<div>Includes MPEG Audio (no preview available)</div>";
          }
          
          if(tmpLine.contains("type=\"" + SSMimeTypeE.textHtml + "\"")){
            result += "<div>Includes HTML Page (no preview available)</div>";
          }
          
          if(tmpLine.contains("type=\"" + SSMimeTypeE.applicationBin + "\"")){
            result += "<div>Includes Application File (no preview available)</div>";
          }
          
          if(tmpLine.contains("type=\"" + SSMimeTypeE.applicationMsexcelBinary + "\"")){
            result += "<div>Includes Binary Spreadsheet File (no preview available)</div>";
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