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

import java.util.ArrayList;
import java.util.List;

public enum SSFileExtE{
  
  bin,
  yaml,
  odt,
  pdf ,
  doc ,
  ppt ,
  pptx,
  mdb ,
  xls ,
  docx,
  accdb,
  xlsx,
  xlsb,
  atom,
  latex,
  rdf,
  txt,
  html,
  xhtml,
  css,
  xml,
  vcf,
  
  //video
  avi,
  m4v,
  mpeg,
  mpg,
  mov,
  flv,
  wmv,
  
  //audio
  mp3,
  ogg,
  wav,
  midi,
  wma,
  m4a,
  amr,
  
  //image
  jpeg,
  jpg,
  png,
  ico,
  gif,
  svg,
  bmp,
  tif,
  tiff,
  
  //zip
  rar,
  zip;
  
  public final static List<SSFileExtE> videoFileExts = new ArrayList<>();
  public final static List<SSFileExtE> audioFileExts = new ArrayList<>();
  public final static List<SSFileExtE> imageFileExts = new ArrayList<>();
  public final static List<SSFileExtE> zipFileExts   = new ArrayList<>();
  
  public static void init(){
    
    videoFileExts.add(SSFileExtE.avi);
    videoFileExts.add(SSFileExtE.m4v);
    videoFileExts.add(SSFileExtE.mpeg);
    videoFileExts.add(SSFileExtE.mpg);
    videoFileExts.add(SSFileExtE.mov);
    videoFileExts.add(SSFileExtE.flv);
    videoFileExts.add(SSFileExtE.wmv);
    
    audioFileExts.add(SSFileExtE.mp3);
    audioFileExts.add(SSFileExtE.ogg);
    audioFileExts.add(SSFileExtE.wav);
    audioFileExts.add(SSFileExtE.midi);
    audioFileExts.add(SSFileExtE.wma);
    audioFileExts.add(SSFileExtE.m4a);
    audioFileExts.add(SSFileExtE.amr);
    
    imageFileExts.add(SSFileExtE.jpeg);
    imageFileExts.add(SSFileExtE.jpg);
    imageFileExts.add(SSFileExtE.png);
    imageFileExts.add(SSFileExtE.ico);
    imageFileExts.add(SSFileExtE.gif);
    imageFileExts.add(SSFileExtE.svg);
    imageFileExts.add(SSFileExtE.bmp);
    imageFileExts.add(SSFileExtE.tif);
    imageFileExts.add(SSFileExtE.tiff);
    
    zipFileExts.add(SSFileExtE.rar);
    zipFileExts.add(SSFileExtE.zip);
  }
  
  public static boolean isAudioOrVideoFileExt(final SSFileExtE fileExt){
    
    if(
      SSStrU.contains(audioFileExts, fileExt) ||
      SSStrU.contains(videoFileExts, fileExt)){
      return true;
    }
    
    return false;
  }
  
  public static boolean isImageFileExt(final SSFileExtE fileExt){
      return SSStrU.contains(imageFileExts, fileExt);
  }
  
  public static SSFileExtE ext(final String fileName) throws Exception {
    
    try{
      if(
        SSStrU.isEmpty       (fileName) ||
        fileName.lastIndexOf (SSStrU.dot) == -1){
        throw new Exception("fileName is null or doesnt contain dot with succeeding file extension");
      }
      
      return get(fileName.substring(fileName.lastIndexOf(SSStrU.dot) + 1));
      
    }catch(Exception error){
      throw new Exception("file ext not found for fileName: " + fileName);
    }
  }

  public static SSFileExtE get(final String value) throws Exception {
    
    try{
      
      return valueOf(value);
    }catch(Exception error){
      throw new Exception("file ext not available for: " + value);
    }
  }

  public static SSFileExtE getFromStrToFormat(final String string) throws Exception {
    
    try{
      
      if(string == null){
        throw new Exception();
      }
      
      final String  myStr    = SSStrU.removeTrailingSlash(string);
      final Integer dotIndex = myStr.lastIndexOf(SSStrU.dot);
      
      if(
        dotIndex < 0 ||
        dotIndex + 1 >= myStr.length()){
        throw new Exception();
      }
      
      return SSFileExtE.get(myStr.substring(dotIndex + 1, myStr.length()));
      
    }catch(Exception error){
      throw new Exception("couldnt get file ext for : " + string);
    }
  }
}