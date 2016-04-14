/**
 * Code contributed to the Learning Layers project
 * http://www.learning-layers.eu
 * Development is partly funded by the FP7 Programme of the European Commission under
 * Grant Agreement FP7-ICT-318209.
 * Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
  
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.util;

import at.tugraz.sss.servs.entity.datatype.SSErrE;
import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.entity.datatype.SSUri;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import java.util.ArrayList;
import java.util.List;

public enum SSFileExtE{
  
  csv,
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
  
  protected static final List<SSFileExtE> videoFileExts = new ArrayList<>();
  protected static final List<SSFileExtE> audioFileExts = new ArrayList<>();
  protected static final List<SSFileExtE> imageFileExts = new ArrayList<>();
  protected static final List<SSFileExtE> zipFileExts   = new ArrayList<>();
  
  public static List<SSFileExtE> getVideoFileExts(){
    return videoFileExts;
  }
  
  public static List<SSFileExtE> getAudioFileExts(){
    return audioFileExts;
  }
  
  public static List<SSFileExtE> getImageFileExts(){
    return imageFileExts;
  }
  
  public static List<SSFileExtE> getZipFileExts(){
    return zipFileExts;
  }
  
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
  
  public static SSFileExtE ext(final SSUri file) throws SSErr {
    
    try{
      
      final String fileStr = SSStrU.removeTrailingSlash(file);
      
      if(
        SSStrU.isEmpty       (fileStr) ||
        fileStr.lastIndexOf  (SSStrU.dot) == -1){
        throw new Exception("file id is null or doesnt contain dot with succeeding file extension");
      }
      
      return get(fileStr.substring(fileStr.lastIndexOf(SSStrU.dot) + 1));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(SSErrE.fileExtInvalid, error);
      return null;
    }
  }

  public static SSFileExtE get(final String value) throws SSErr {
    
    try{
      
      return valueOf(value);
    }catch(Exception error){
      SSServErrReg.regErrThrow(SSErrE.fileExtInvalid, error);
      return null;
    }
  }

  public static SSFileExtE getFromStrToFormat(final String string) throws SSErr {
    
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
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}