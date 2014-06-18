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
 package at.kc.tugraz.socialserver.utils;

import java.io.*;
import java.nio.charset.Charset;

public class SSFileU{

  private SSFileU(){}
  
  public static String dirUserHome() {
		return correctDirPath(System.getProperty("user.home"));
	}
  
  public static String dirUser() {
		return correctDirPath(System.getProperty("user.dir"));
	}
    
  public static String dirWorking(){
    return correctDirPath(new File(SSStrU.empty).getAbsolutePath());
  }
  
  public static String dirCatalinaHome(){
    return correctDirPath(System.getProperty("catalina.home"));
  }
  
  public static String dirCatalinaBase(){
    return correctDirPath(System.getProperty("catalina.base"));
  }
  
  public static String dirWorkingTmp() {
    return dirWorking() + "tmp/";
  }
    
  public static String dirWorkingData(){
    return dirWorking() + "data/";
  }
  
  public static String dirWorkingDataCsv(){
    return dirWorking() + "data/csv/";
  }
  
  public static String dirWorkingScriptR(){
    return dirWorking() + "script/r/";
  }
  
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
  
	public static void delFile(String filePath) throws Exception {
		
    try {
			File file = new File(filePath);
			file.delete();
		}catch (Exception error){
      SSLogU.errThrow(error);
		}
	}
  
  public static void appendTextToFile(String filePath, String text) throws Exception{
		
		FileWriter     fileWriter    = null;
		BufferedWriter bufferWritter = null;
		
		try {
			fileWriter    = new FileWriter     (filePath, true);
			bufferWritter = new BufferedWriter (fileWriter); 
			
			bufferWritter.write(text);
        
		}catch (IOException error) {
			SSLogU.errThrow(error);
		}finally{
			
			if(bufferWritter != null){
				bufferWritter.close();
			}
			
			if(fileWriter!= null){
				fileWriter.close();
			}
		}
	}	
	
	public static FileInputStream openFileForRead(
    final String filePath) throws Exception{

    return new FileInputStream(new File(filePath));
	}
	
	public static FileOutputStream openOrCreateFileWithPathForWrite(
    final String filePath) throws Exception{
		
		try{
      
      final File file = new File(filePath);
      
      if(!file.exists()){
        file.getParentFile().mkdirs();
      }

      return new FileOutputStream(file);
    }catch(Exception error){
      SSLogU.errThrow(error);
      return null;
    }
	}
  
  public static String readFileText(
    final File    file, 
    final Charset charset) throws Exception {
   
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
      SSLogU.errThrow(error);
      return null;
   }finally{
      
      if(in != null){
        in.close();
      }
    }
  }
  
  public static String readStreamText(
    final InputStream streamIn) throws Exception{
    
    if(SSObjU.isNull(streamIn)){
      SSLogU.errThrow(new Exception("pars not ok"));
      return null;
    }
    
    try{
      final InputStreamReader inputReader = new InputStreamReader (streamIn, SSEncodingU.utf8);
      final char[]            buffer      = new char[1];
      String                  result      = new String();

      while(inputReader.read(buffer) != -1){
        result += buffer[0];
      }

      return result;
    }catch(Exception error){
      SSLogU.errThrow(error);
      return null;
    }finally{
      
      if(streamIn != null){
        streamIn.close();
      }
    }
  }
   
  public static void readFileBytes(
    final OutputStream    outStream, 
    final FileInputStream fileIn) throws Exception{
    
    if(SSObjU.isNull(outStream, fileIn)){
      SSLogU.errThrow(new Exception("pars not ok"));
      return;
    }
    
    final byte[] fileBytes   = new byte[SSSocketU.socketTranmissionSize];
    int          read;
    
    try{
      
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
      SSLogU.errThrow(error);
    }finally{
      
      if(outStream != null){
        outStream.close();
      }
      
      if(fileIn != null){
        fileIn.close();
      }
    }
  }
  
  public static void writeFileText(
    final File   file,
    final String text) throws Exception{
    
    if(SSObjU.isNull(file, text)){
      SSLogU.errThrow(new Exception("pars not ok"));
      return;
    }
        
    final byte[]     bytes      = text.getBytes();
    FileOutputStream fileOut    = null;
    
    try{
      
      fileOut = openOrCreateFileWithPathForWrite(file.getAbsolutePath());
      
      fileOut.write (bytes, 0, bytes.length);
      
    }catch(Exception error){
      SSLogU.errThrow(error);
    }finally{
      
      if(fileOut != null){
        fileOut.close();
      }
    }
  }
  
  public static void writeFileBytes(
    final FileOutputStream fileOut, 
    final InputStream      streamIn) throws Exception{
    
    if(SSObjU.isNull(fileOut, streamIn)){
      SSLogU.errThrow(new Exception("pars not ok"));
      return;
    }
    
    final byte[] fileBytes   = new byte[SSSocketU.socketTranmissionSize];
    int          read;
    
    try{
    
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
      SSLogU.errThrow(error);
    }finally{
      
      if(fileOut != null){
        fileOut.close();
      }
      
      if(streamIn != null){
        streamIn.close();
      }
    }
  }
  
  public static File[] filesForDirPath(final String dirPath){
    return new File(correctDirPath(dirPath)).listFiles();
  }
}

//  private static void existsDirTemp() throws Exception{
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

//	public static String copyDocumentFromWebdav(String filename) throws Exception {
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