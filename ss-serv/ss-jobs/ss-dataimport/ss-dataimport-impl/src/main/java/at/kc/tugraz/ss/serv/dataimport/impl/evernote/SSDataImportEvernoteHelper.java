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
package at.kc.tugraz.ss.serv.dataimport.impl.evernote;

import at.kc.tugraz.socialserver.utils.SSDateU;
import at.kc.tugraz.socialserver.utils.SSFileExtU;
import at.kc.tugraz.socialserver.utils.SSFileU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import at.kc.tugraz.ss.serv.dataimport.datatypes.pars.SSDataImportEvernotePar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteInfo;
import at.kc.tugraz.ss.serv.jobs.evernote.impl.helper.SSEvernoteHelper;
import at.kc.tugraz.ss.serv.localwork.serv.SSLocalWorkServ;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.filerepo.conf.SSFileRepoConf;
import com.evernote.edam.type.LinkedNotebook;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.SharedNotebook;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import org.jpedal.PdfDecoder;
import org.jpedal.fonts.FontMappings;
import org.xhtmlrenderer.pdf.ITextRenderer;

public class SSDataImportEvernoteHelper {
  
  private final  SSFileRepoConf          localWorkConf          = (SSFileRepoConf) SSLocalWorkServ.inst.servConf;
  private        SSEvernoteInfo          evernoteInfo           = null;
  private        SSLabelStr              userName               = null;
  private        SSUri                   userUri                = null;
  private        List<SharedNotebook>    sharedNotebooks        = null;
  private        List<String>            sharedNotebookGuids    = null;
  
  private final SSEvernoteHelper evernoteHelper = new SSEvernoteHelper();
  
  public void setBasicEvernoteInfo(final SSDataImportEvernotePar par) throws Exception{
    
    this.evernoteInfo    = SSServCaller.getEvernoteInfo (par.user, par.authToken);
    this.userName        = evernoteHelper.getUserName   (evernoteInfo);
    this.userUri         = SSServCaller.logUserIn       (userName, false);
  }
  
  public void handleSharedNotebooks() throws Exception{
    sharedNotebooks     = SSServCaller.getEvernoteSharedNotebooks (evernoteInfo.noteStore);
    sharedNotebookGuids = evernoteHelper.getSharedNotebookGuids  (sharedNotebooks);
  }
  
  public void handleNotebooks(SSDataImportEvernotePar par) throws Exception{
    
    SSUri       notebookUri;
    SSLabelStr  notebookLabel;
    Boolean     isSharedNotebook;
    
    for(Notebook notebook : SSServCaller.getEvernoteNotebooks(evernoteInfo.noteStore)){
      
      notebookUri      = evernoteHelper.uriHelper.getNormalOrSharedNotebookUri      (userName,    notebook, sharedNotebookGuids);
      isSharedNotebook = evernoteHelper.isSharedNootebook                           (notebookUri, userName, notebook);
      notebookLabel    = evernoteHelper.labelHelper.getNormalOrSharedNotebookLabel  (notebookUri, notebook);
      
      SSServCaller.entityAddAtCreationTime(
        userUri,
        notebookUri,
        notebookLabel,
        notebook.getServiceCreated(),
        SSEntityEnum.evernoteNotebook,
        false);
      
      evernoteHelper.ueHelper.addUEsForNormalNotebook (userUri,          notebookUri, notebook);
      evernoteHelper.ueHelper.addUEsForSharedNotebook (isSharedNotebook, userUri,     notebook, notebookUri, sharedNotebooks);
      
      handleEvernoteNotes(
        userUri,
        evernoteInfo,
        notebook,
        isSharedNotebook);
    }
  }
  
  public void handleLinkedNotebooks() throws Exception{

    int                     timeCounter = 1;
    SSUri                   notebookUri;
    SSLabelStr              notebookLabel;
    SSLabelStr              noteLabel;
    Long                    creationTimeForLinkedNotebook;
    SSUri                   noteUri;
    SSUri                   resourceUri;
    SSLabelStr              resourceLabel;
    
    for(LinkedNotebook linkedNotebook : SSServCaller.getEvernoteLinkedNotebooks(evernoteInfo.noteStore)){
      
      notebookUri   = evernoteHelper.uriHelper.getLinkedNotebookUri     (linkedNotebook);
      notebookLabel = evernoteHelper.labelHelper.getLinkedNotebookLabel (linkedNotebook, notebookUri);
      
      creationTimeForLinkedNotebook = new Date().getTime() - (SSDateU.dayInMilliSeconds * timeCounter);
      timeCounter++;
      
      SSServCaller.entityAddAtCreationTime(
        userUri,
        notebookUri,
        notebookLabel,
        creationTimeForLinkedNotebook,
        SSEntityEnum.evernoteNotebook,
        false);
      
      evernoteHelper.ueHelper.addUEsForLinkedNotebook(
        userUri,
        notebookUri,
        creationTimeForLinkedNotebook);
      
//      for(Note note : SSServCaller.getEvernoteLinkedNotes(evernoteInfo.noteStore, linkedNotebook)){
//        
//        noteUri   = evernoteHelper.uriHelper.getLinkedNoteUri     (linkedNotebook, note);
//        noteLabel = evernoteHelper.labelHelper.getLinkedNoteLabel (note, noteUri);
//        
//        SSServCaller.addEntityAtCreationTime(
//          userUri,
//          noteUri,
//          noteLabel,
//          creationTimeForLinkedNotebook,
//          SSEntityEnum.evernoteNote);
//        
//        SSServCaller.addUEAtCreationTime(
//          userUri,
//          noteUri,
//          SSUEEnum.evernoteNoteFollow,
//          SSStrU.empty,
//          creationTimeForLinkedNotebook,
//          shouldCommit);
//        
//        if(note.getResources() != null){
//          
//          for(Resource resource : note.getResources()){
//            
//            resourceUri   = SSUri.get     (linkedNotebook.getWebApiUrlPrefix() + "share/" + linkedNotebook.getShareKey() + "/res/" + resource.getGuid());
//            resourceLabel = SSLabelStr.get(SSStrU.toString(resourceUri));
//            
//            SSServCaller.addEntityAtCreationTime(
//              userUri,
//              resourceUri,
//              resourceLabel,
//              note.getUpdated(),
//              SSEntityEnum.evernoteResource);
//            
//            SSServCaller.addUEAtCreationTime(
//              userUri,
//              resourceUri,
//              SSUEEnum.evernoteResourceFollow,
//              SSStrU.empty,
//              creationTimeForLinkedNotebook,
//              shouldCommit);
//          }
//        }
//      }
    }
  }
  
  private void handleEvernoteNotes(
    SSUri                userUri,
    SSEvernoteInfo       evernoteInfo,
    Notebook             notebook,
    Boolean              isSharedNotebook) throws Exception{
    
    SSUri      noteUri;
    SSLabelStr noteLabel;
    
    for(Note note : SSServCaller.getEvernoteNotes(evernoteInfo.noteStore, notebook.getGuid())){
      
      noteUri   = evernoteHelper.uriHelper.getNormalOrSharedNoteUri   (evernoteInfo, note);
      noteLabel = evernoteHelper.labelHelper.getNoteLabel             (note,         noteUri);
      
      SSServCaller.entityAddAtCreationTime(
        userUri,
        noteUri,
        noteLabel,
        note.getCreated(),
        SSEntityEnum.evernoteNote,
        false);
      
      evernoteHelper.ueHelper.addUEsAndTagsForNormalNote (userUri,          note,    noteUri);
      evernoteHelper.ueHelper.addUEsForSharedNote        (isSharedNotebook, userUri, notebook, noteUri, sharedNotebooks);
      evernoteHelper.ueHelper.addUEsForNoteAttrs         (userUri,          note,    noteUri);
      
      handleEvernoteNoteContent (note);
      handleEvernoteResources   (userUri, notebook, evernoteInfo, note, isSharedNotebook, sharedNotebooks);
    }
  }
  
  private void handleEvernoteResources(
    SSUri                userUri,
    Notebook             notebook,
    SSEvernoteInfo       evernoteInfo,
    Note                 note,
    Boolean              isSharedNotebook,
    List<SharedNotebook> sharedNotebooks) throws Exception{
    
    SSUri      resourceUri;
    SSLabelStr resourceLabel;
    
    if(
      note                == null ||
      note.getResources() == null){
      return;
    }
    
    for(Resource resource : note.getResources()){
      
      resourceUri   = evernoteHelper.uriHelper.getResourceUri     (evernoteInfo, resource);
      resourceLabel = evernoteHelper.labelHelper.getResourceLabel (resource,     resourceUri);
      
      SSServCaller.entityAddAtCreationTime(
        userUri,
        resourceUri,
        resourceLabel,
        note.getUpdated(),
        SSEntityEnum.evernoteResource,
        false);
      
      evernoteHelper.ueHelper.addUEsForResource       (userUri, resourceUri, note);
      evernoteHelper.ueHelper.addUEsForSharedResource (userUri, resourceUri, notebook, sharedNotebooks, isSharedNotebook);
    }
  }
  
  private void handleEvernoteNoteContent(Note note) throws Exception{
    
    SSUri  fileUri;
    String fileId;
    
    try{
      fileUri = SSServCaller.fileCreateUri     (userUri, SSFileExtU.png);
      fileId  = SSServCaller.fileIDFromURI     (userUri, fileUri);
      
      String           xhtmlFilePath    = localWorkConf.getPath() + SSEntityEnum.evernoteNote + SSStrU.underline + note.getGuid() + SSStrU.dot + SSFileExtU.xhtml;
      String           pdfFilePath      = localWorkConf.getPath() + SSEntityEnum.evernoteNote + SSStrU.underline + note.getGuid() + SSStrU.dot + SSFileExtU.pdf;
      String           pngFilePath      = localWorkConf.getPath() + fileId;
      
      evernoteNoteWriteHTML         (note,        xhtmlFilePath);
      evernoteNoteWritePDF          (pdfFilePath, xhtmlFilePath);
      evernoteNoteWritePNG          (pdfFilePath, pngFilePath);  //&&evernoteNoteUploadPNGToWebDav (pngFilePath, pngFileID)
      
      System.out.println(fileId);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  private void evernoteNoteWritePNG(String pdfFilePath, String pngFilePath) throws Exception{
    
    PdfDecoder    pdfToImgDecoder = null;
    BufferedImage buffImage;
    File          pngFile;
    Image         pngImage;
    BufferedImage scaledThumb;
    Graphics2D    graphics2D;
    
    try {
      
      pdfToImgDecoder = new PdfDecoder(true);
      
      FontMappings.setFontReplacements();
      
      pdfToImgDecoder.openPdfFile(pdfFilePath); //file
      //decode_pdf.openPdfFile("C:/myPDF.pdf", "password"); //encrypted file
      //decode_pdf.openPdfArray(bytes); //bytes is byte[] array with PDF
      //decode_pdf.openPdfFileFromURL("http://www.mysite.com/myPDF.pdf",false);
      
      pdfToImgDecoder.setExtractionMode(0, 1f); //do not save images
      /**get page 1 as an image*/
      //page range if you want to extract all pages with a loop
      //int start = 1,  end = decode_pdf.getPageCount();
      buffImage = pdfToImgDecoder.getPageAsImage(1);
      pngFile   = new File(pngFilePath);
      
      ImageIO.write(buffImage, SSFileExtU.png, pngFile);
      
      pngImage  = ImageIO.read(pngFile);
      buffImage = (BufferedImage) pngImage;
      
      //scale the thumb
      scaledThumb = new BufferedImage(350, 350, BufferedImage.TYPE_INT_RGB);
      graphics2D  = scaledThumb.createGraphics();
      
      graphics2D.setComposite(AlphaComposite.Src);
      graphics2D.drawImage(buffImage, 0, 0, 350, 350, null);
      graphics2D.dispose();
      
      ImageIO.write(scaledThumb, SSFileExtU.png, pngFile);
      
    }catch (Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(pdfToImgDecoder != null){
        pdfToImgDecoder.closePdfFile();
      }
    }
  }
  
  private void evernoteNoteWriteHTML(Note note, String xhtmlFilePath) throws Exception{
    
    OutputStreamWriter out = null;
    
    try {
      
      out = new OutputStreamWriter(SSFileU.openOrCreateFileWithPathForWrite(xhtmlFilePath), "UTF-8");
      
      out.write(note.getContent());
      
    } catch (Exception error1) {
      SSServErrReg.regErrThrow(error1);
    }finally{
      
      if(out != null){
        
        try {
          out.close();
        } catch (IOException error2) {
          SSServErrReg.regErrThrow(error2);
        }
      }
    }
  }
  
  private void evernoteNoteWritePDF(String pdfFilePath, String xhtmlFilePath) throws Exception{
    
    FileOutputStream out      = null;
    ITextRenderer    renderer;
    String           uri;
    
    try{
      
      uri      = new File(xhtmlFilePath).toURI().toURL().toString();
      out      = new FileOutputStream(pdfFilePath);
      renderer = new ITextRenderer();
      
      renderer.setDocument(uri);
      renderer.layout();
      renderer.createPDF(out);
      
    }catch(Exception error1) {
      SSServErrReg.regErrThrow(error1);
    }finally{
      
      if(out != null){
        
        try {
          out.close();
        } catch (IOException error2){
          SSServErrReg.regErrThrow(error2);
        }
      }
    }
  }
}
