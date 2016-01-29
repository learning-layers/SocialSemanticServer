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
package at.tugraz.sss.serv.util;

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.reg.*;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import java.io.*;
import java.nio.charset.Charset;
import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jpedal.PdfDecoder;
import org.jpedal.fonts.FontMappings;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hwpf.usermodel.Range;
import org.xhtmlrenderer.pdf.ITextRenderer;

public class SSFileU{
  
  public static final String dirNameLib                     = "lib/";
  public static final String dirNameConf                    = "conf/";
  public static final String dirNameData                    = "data/";
  public static final String dirNameDataCsv                 = "data/csv/";
  public static final String dirNameTmp                     = "tmp/";
  
  public static final String fileNameSSSConf                = "sss.conf.yaml";
  public static final String fileNameUsersCsv               = "users.csv";
  public static final String fileNameLog4JProperties        = "log4j.properties";
  public static final String fileNameSSSJar                 = "sss.jar";
  public static final String fileNameRunitSh                = "runit.sh";
  public static final String fileNameRunitBat               = "runit.bat";
  public static final String fileNameSSSEvalLog             = "sss.eval.log";
  public static final String fileNameSSSLog                 = "sss.log";
  
  private SSFileU(){}
  
  public static String dirWorking(){
    return correctDirPath(new File(SSStrU.empty).getAbsolutePath());
  }
  
//  public static String dirCurrent() {
//    return "./";
//  }
//  
//  public static String dirUserHome() {
//    return correctDirPath(System.getProperty("user.home"));
//  }
//  
//  public static String dirUser() {
//    return correctDirPath(System.getProperty("user.dir"));
//  }
  
//  public static String dirCatalinaHome(){
//    return correctDirPath(System.getProperty("catalina.home"));
//  }
//  
//  public static String dirCatalinaBase(){
//    return correctDirPath(System.getProperty("catalina.base"));
//  }
  
//  public static String dirWorkingTmp() {
//    return dirWorking() + dirNameTmp;
//  }
//  
//  public static String dirWorkingData(){
//    return dirWorking() + dirNameData;
//  }
//  
//  public static String dirWorkingDataCsv(){
//    return dirWorking() + dirNameDataCsv;
//  }
//  
//  public static String dirWorkingScriptR(){
//    return dirWorking() + "script/r/";
//  }
  
  /**
   * <ul>
   * <li>if folder is null, folder will be set to an empty String</li>
   * <li>replaces all "\" w/ "/"</li>
   * </ul>
   * @param folder
   * @return the input parameter if folder already ends with "/" or is empty, otherwise
   * "/" is appended
   */
  public static String correctDirPath(String folder) {
    
    if (SSStrU.isEmpty(folder)){
      folder = SSStrU.empty;
    }
    
    folder = SSStrU.replaceAll(folder, SSStrU.backSlash, SSStrU.slash);
    
    if(
      folder.endsWith      (SSStrU.slash) ||
      SSStrU.isEmpty(folder)){
      
      return folder;
    }else{
      return folder + SSStrU.slash;
    }
  }
  
  public static void delFile(final String filePath) throws SSErr {
    new File(filePath).delete();
  }
  
  public static void appendTextToFile(String filePath, String text) throws SSErr{
    
    FileWriter     fileWriter    = null;
    BufferedWriter bufferWritter = null;
    
    try {
      fileWriter    = new FileWriter     (filePath, true);
      bufferWritter = new BufferedWriter (fileWriter);
      
      bufferWritter.write(text);
      
    }catch (IOException error) {
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(bufferWritter != null){
        try {
          bufferWritter.close();
        } catch (IOException ex) {
          SSLogU.err(ex);
        }
      }
      
      if(fileWriter != null){
        try {
          fileWriter.close();
        } catch (IOException ex) {
          SSLogU.err(ex);
        }
      }
    }
  }
  
  public static FileInputStream openFileForRead(
    final String filePath) throws SSErr{
    
    try{
      return new FileInputStream(new File(filePath));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static boolean existsFile(final String filePath) throws SSErr{
    
    try{
      
      final File file = new File(filePath);
      
      return file.exists();
    }catch(Exception error){
      throw error;
    }
  }
  
  public static FileOutputStream openOrCreateFileWithPathForWrite(
    final String filePath) throws SSErr{
    
    try{
      
      final File file = new File(filePath);
      
      if(!file.exists()){
        file.getParentFile().mkdirs();
      }
      
      return new FileOutputStream(file);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static FileOutputStream openOrCreateFileWithPathForAppend(
    final String filePath) throws SSErr{
    
    try{
      
      final File file = new File(filePath);
      
      if(!file.exists()){
        file.getParentFile().mkdirs();
      }
      
      return new FileOutputStream(file, true);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static String readFileText(
    final File    file,
    final Charset charset) throws SSErr {
    
    FileInputStream   in = null;
    
    try{
      
      final byte[] bytes       = new byte[1];
      String       fileContent = SSStrU.empty;
      
      in = openFileForRead(file.getAbsolutePath());
      
      while(in.read(bytes) != -1){
        fileContent += new String(bytes, charset);
      }
      
      in.close();
      
      return fileContent;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      if(in != null){
        try {
          in.close();
        } catch (IOException ex) {
          SSLogU.err(ex);
        }
      }
    }
  }
  
  public static String readStreamText(
    final InputStream streamIn) throws SSErr{
    
    try{
      
      final InputStreamReader inputReader = new InputStreamReader (streamIn, SSEncodingU.utf8.toString());
      final char[]            buffer      = new char[1];
      String                  result      = new String();
      
      while(inputReader.read(buffer) != -1){
        result += buffer[0];
      }
      
      return result;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      
      if(streamIn != null){
        try {
          streamIn.close();
        } catch (IOException ex) {
          SSLogU.err(ex);
        }
      }
    }
  }
  
  public static void readFileBytes(
    final OutputStream    outStream,
    final FileInputStream fileIn,
    final Integer         transmissionSize) throws SSErr{
    
    try{
      final byte[] fileBytes   = new byte[transmissionSize];
      int          read;
      
      
      
      while ((read = fileIn.read(fileBytes)) != -1) {
        
        if(
          fileBytes.length == 0 ||
          read             <= 0){
          
          outStream.write     (new byte[0]);
          outStream.flush     ();
          break;
        }
        
        outStream.write     (fileBytes, 0, read);
        outStream.flush     ();
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(outStream != null){
        try {
          outStream.close();
        } catch (IOException ex) {
          SSLogU.err(ex);
        }
      }
      
      if(fileIn != null){
        try {
          fileIn.close();
        } catch (IOException ex) {
           SSLogU.err(ex);
        }
      }
    }
  }
  
  public static void writeFileText(
    final File   file,
    final String text) throws SSErr{
    
    OutputStreamWriter fileOut = null;
      
    try{
//    final byte[]        bytes      = text.getBytes();
      
        new OutputStreamWriter(
          openOrCreateFileWithPathForWrite(
            file.getAbsolutePath()),
          Charset.forName(SSEncodingU.utf8.toString()));
      
      fileOut.write(text);
//      fileOut.write (bytes, 0, bytes.length);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(fileOut != null){
        try {
          fileOut.close();
        } catch (IOException ex) {
          SSLogU.err(ex);
        }
      }
    }
  }
  
  public static void writeFileBytes(
    final FileOutputStream fileOut,
    final InputStream      streamIn,
    final Integer          transmissionSize) throws SSErr{
    
    try{
      
      final byte[] fileBytes   = new byte[transmissionSize];
      int          read;
      while ((read = streamIn.read(fileBytes)) != -1) {
        
        if(
          fileBytes.length == 0 ||
          read             <= 0){
          
          fileOut.write     (new byte[0]);
          fileOut.flush     ();
          break;
        }
        
        fileOut.write     (fileBytes, 0, read);
        fileOut.flush     ();
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(fileOut != null){
        try {
          fileOut.close();
        } catch (IOException ex) {
          SSLogU.err(ex);
        }
      }
      
      if(streamIn != null){
        try {
          streamIn.close();
        } catch (IOException ex) {
          SSLogU.err(ex);
        }
      }
    }
  }
  
  public static void writeFileBytes(
    final FileOutputStream fileOut,
    final byte[]           bytes,
    final Integer          length) throws SSErr{
    
    try{
      
      fileOut.write(bytes, 0, length);
      fileOut.flush     ();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(fileOut != null){
        try {
          fileOut.close();
        } catch (IOException ex) {
          SSLogU.err(ex);
        }
      }
    }
  }
  
  public static File[] filesForDirPath(final String dirPath){
    return new File(correctDirPath(dirPath)).listFiles();
  }
  
  //warning: has to be synchronized to have ITextRenderer working correctly //https://java.net/projects/xhtmlrenderer/lists/users/archive/2010-10/message/1
  public static synchronized void writePDFFromXHTML(
    final String  pdfFilePath,
    final String  xhtmlFilePath,
    final boolean useImageEmbedder) throws SSErr{
    
    FileOutputStream out = null;
    
    try{
      
      final ITextRenderer renderer = new ITextRenderer();
      final String        uri      = new File(xhtmlFilePath).toURI().toURL().toString();
      
      out = new FileOutputStream(pdfFilePath);
      
      if(useImageEmbedder){
        renderer.getSharedContext().setReplacedElementFactory(
          new SSPDFFromXHTMLImageEmbedder(
            renderer.getSharedContext().getReplacedElementFactory()));
      }
      
      renderer.setDocument(uri);
      renderer.layout(); //can happen http://stackoverflow.com/questions/13678641/while-converting-xhtml-with-css-to-pdf-got-an-exception-java-lang-indexoutofboun
      renderer.createPDF(out);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(out != null){
        try {
          out.close();
        } catch (IOException ex) {
          SSLogU.err(ex);
        }
      }
    }
  }
  
  public static void writePDFFromText(
    final String pdfFilePath,
    final String textFilePath)throws SSErr{
    
    OutputStream    out = null;
    BufferedReader  br = null;
    
    try{
      
      out = openOrCreateFileWithPathForWrite(pdfFilePath);
      
      final Document        document = new Document();
      final PdfWriter       writer   = PdfWriter.getInstance(document, out);
      String line;
      
      document.open();
      writer.setPageEmpty(true);
      document.newPage();
      writer.setPageEmpty(true);
      
      br = new BufferedReader(new FileReader(new File(textFilePath)));
      
      while((line = br.readLine()) != null){
        document.add(new Paragraph(line));
      }
      
      document.close();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(out != null){
        try {
          out.close();
        } catch (IOException ex) {
          SSLogU.err(ex);
        }
      }
      
      if(br != null){
        try {
          br.close();
        } catch (IOException ex) {
          SSLogU.err(ex);
        }
      }
    }
  }
  
  public static void writePDFFromDoc(
    final String docFilePath,
    final String pdfFilePath) throws SSErr{
    
    try{
      final Document        document = new Document();
      final POIFSFileSystem fs       = new POIFSFileSystem(openFileForRead(docFilePath));
      final HWPFDocument    word     = new HWPFDocument  (fs);
      final WordExtractor   we       = new WordExtractor (word);
      final OutputStream    out      = openOrCreateFileWithPathForWrite(pdfFilePath);
      final PdfWriter       writer   = PdfWriter.getInstance(document, out);
      final Range           range    = word.getRange();
      
      document.open();
      writer.setPageEmpty(true);
      document.newPage();
      writer.setPageEmpty(true);
      
      String[] paragraphs = we.getParagraphText();
      
      for (int i = 0; i < paragraphs.length; i++) {
        
        org.apache.poi.hwpf.usermodel.Paragraph pr = range.getParagraph(i);
        // CharacterRun run = pr.getCharacterRun(i);
        // run.setBold(true);
        // run.setCapitalized(true);
        // run.setItalic(true);
        paragraphs[i] = paragraphs[i].replaceAll("\\cM?\r?\n", "");
        System.out.println("Length:" + paragraphs[i].length());
        System.out.println("Paragraph" + i + ": " + paragraphs[i].toString());
        
        // add the paragraph to the document
        document.add(new Paragraph(paragraphs[i]));
      }
      
      document.close();
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void writeScaledPNGFromPDF(
    final String  pdfFilePath,
    final String  pngFilePath,
    final Integer width,
    final Integer height,
    final boolean scale) throws SSErr{
    
    PdfDecoder    pdfToImgDecoder = null;
    BufferedImage buffImage;
    File          pngFile;
    Image         pngImage;
    
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
      
      ImageIO.write(buffImage, SSFileExtE.png.toString(), new File(pngFilePath));
      
      pngImage  = ImageIO.read(new File(pngFilePath));
      buffImage = (BufferedImage) pngImage;
      
      //scale the thumb
      if(scale){
        scalePNGAndWrite(buffImage, pngFilePath, width, height);
      }
     
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(pdfToImgDecoder != null){
        pdfToImgDecoder.closePdfFile();
      }
    }
  }
  
  public static void scalePNGAndWrite(
    final BufferedImage buffImage,
    final String        pngFilePath,
    final Integer       width,
    final Integer       height) throws IOException{
    
    final BufferedImage scaledThumb = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    final Graphics2D    graphics2D  = scaledThumb.createGraphics();
    
    graphics2D.setComposite(AlphaComposite.Src);
    graphics2D.drawImage(buffImage, 0, 0, width, height, null);
    graphics2D.dispose();
    
    ImageIO.write(scaledThumb, SSFileExtE.png.toString(), new File(pngFilePath));
  }
  
  public static void writeStr(
    final String str,
    final String filePath) throws SSErr{
    
    OutputStreamWriter out = null;
    
    try {
      
      out = new OutputStreamWriter(openOrCreateFileWithPathForWrite(filePath), SSEncodingU.utf8.toString());
      
      out.write(str);
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }finally{
      
      if(out != null){
        try {
          out.close();
        } catch (IOException ex) {
          SSLogU.err(ex);
        }
      }
    }
  }
  
  public static String readImageToBase64Str(
    final String  filePath,
    final Integer transmissionSize) throws SSErr{
    
    try{
      final DataInputStream fileReader = new DataInputStream (new FileInputStream(new File(filePath)));
      final List<Byte>      bytes      = new ArrayList<>();
      byte[]                chunk      = new byte[transmissionSize];
      int                   fileChunkLength;
      
      while(true){
        
        fileChunkLength = fileReader.read(chunk);
        
        if(fileChunkLength == -1){
          break;
        }
        
        for(int counter = 0; counter < fileChunkLength; counter++){
          bytes.add(chunk[counter]);
        }
      }
      
      return "data:image/png;base64," + DatatypeConverter.printBase64Binary(ArrayUtils.toPrimitive(bytes.toArray(new Byte[bytes.size()])));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private static void formatAudioAndVideoFileName(final File file) throws SSErr{
    
    try{
      final  Path       pathToFile  = file.toPath();
      String            fileName    = pathToFile.getFileName().toString().toLowerCase();
      final  SSFileExtE fileExt     = SSFileExtE.ext(fileName);
      
      if(!SSFileExtE.isAudioOrVideoFileExt(fileExt)){
        return;
      }
      
      fileName = SSStrU.replaceAllBlanksSpecialCharactersDoubleDots(fileName, SSStrU.underline);
      
      Files.move(pathToFile, pathToFile.resolveSibling(fileName));
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}

//  private void writePDF(
//    final String pdfFilePath,
//    final String filePath) throws SSErr{
//
//    FileOutputStream out      = null;
//    ITextRenderer    renderer;
//    String           uri;
//
//    try{
//
//      uri      = new File(filePath).toURI().toURL().toString();
//      out      = new FileOutputStream(pdfFilePath);
//      renderer = new ITextRenderer();
//
//      renderer.setDocument(uri);
//      renderer.layout();
//      renderer.createPDF(out);
//
//    }catch(Exception error1) {
//      SSServErrReg.regErrThrow(error1);
//    }finally{
//
//      if(out != null){
//
//        try {
//          out.close();
//        } catch (IOException error2){
//          SSServErrReg.regErrThrow(error2);
//        }
//      }
//    }
//  }


//  private static void existsDirTemp() throws SSErr{
//
//    File file = new File(dirWorkingTmp());
//
//    if(
//      file.exists() &&
//      file.isDirectory()) {
//			return;
//		}
//
//		if(!file.mkdir()) {
//			SSLogU.logAndThrow(new Exception("temp folder doesn't exists neither could be created"));
//		}
//  }

//public static boolean isUrlReachable(String url) {
//    try {
//      InetAddress address = InetAddress.getByName(url);
//      return address.isReachable(1000);
//    } catch (Exception e) {
//      log.debug(e.getMessage());
//    }
//    return false;
//  }

//	public static String copyDocumentFromWebdav(String filename) throws SSErr {
//		try {
//			InputStream is = WebdavHelper.getInstance().getDocument(filename);
//			String location = SolrG.getTempFolderAbsolute();
//			SSUtils.copyFileFromInputStreamToLocal(is, location, filename);
//			return location;
//		} catch (Exception e) {
//			throw e;
//		}
//	}

///**
//	 * public static for testing only
//	 * @param url
//	 * @return
//	 */
//	public static String checkAndRepairSolrUrl(final String url) throws SolRException {
//		if (url == null || url.trim().isEmpty()) {
//			String msg = "no url for solr defined.";
//			log.error(msg);
//			throw new SolRException(msg);
//		}
//		String ret = url.startsWith(strU.PREFIX_HTTP) ? url : strU.PREFIX_HTTP + url;
//		return ret;
//	}

//  public static String getAsCorrectFolder(
//    String folder) {
//
//		if (SSObjectUtils.isNull(folder)) {
//			folder = strU.strEmpty;
//		}
//
//		folder = folder.replace("\\", "/");
//
//		if(
//				folder.endsWith("/") ||
//				SSStrU.isEmpty(folder)){
//
//			return folder;
//		}
//
//		return folder + "/";
//	}
//
//private static void addTextToFileNamesAtBeginInDir(File[] files, String textToAddAtBegin) throws IOException{
//
//    Path   pathToFile;
//    String fileName;
//
//    for(File file : files){
//
//      if(file.isDirectory()){
//        return;
//      }else{
//
//        pathToFile  = file.toPath();
//        fileName    = pathToFile.getFileName().toString();
//        fileName    = textToAddAtBegin + fileName;
//
//        try{
//          Files.move(pathToFile, pathToFile.resolveSibling(fileName));
//        }catch(FileAlreadyExistsException error){
//          System.out.println("file " + pathToFile.resolveSibling(fileName) + " already exists!");
//        }
//      }
//    }
//  }

//private static void formatAudioAndVideoFileNamesInDir(final File[] files) throws SSErr {
//
//    for(File file : files){
//
//      if(file.isDirectory()){
//        formatAudioAndVideoFileNamesInDir(file.listFiles());
//      }else{
//        formatAudioAndVideoFileName(file);
//      }
//    }
//  }