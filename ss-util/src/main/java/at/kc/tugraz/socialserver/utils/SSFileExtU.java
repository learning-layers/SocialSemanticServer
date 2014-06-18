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

import java.util.ArrayList;
import java.util.List;

public class SSFileExtU {
  
  public final static String                 yaml            = "yaml";
  public final static String                 pdf             = "pdf";
	public final static String                 doc             = "doc";
  public final static String                 ppt             = "ppt";
	public final static String                 mdb             = "mdb";
	public final static String                 xls             = "xls";
	public final static String                 docx            = "docx";
	public final static String                 accdb           = "accdb";
	public final static String                 xlsx            = "xlsx";
	public final static String                 atom            = "atom";
	public final static String                 latex           = "latex";
	public final static String                 rdf             = "rdf";
	public final static String                 txt             = "txt";
	public final static String                 html            = "html";
  public final static String                 xhtml           = "xhtml";
	public final static String                 css             = "css";
  public final static String                 xml             = "xml";
  
  //video
	public final static String                 avi             = "avi";
	public final static String                 mp4             = "mp4";
	public final static String                 mpeg            = "mpeg";
  public final static String                 mpg             = "mpg";
	public final static String                 mov             = "mov";
  public final static String                 flv             = "flv";
	public final static String                 wmv             = "wmv";
  
  //audio
  public final static String                 mp3             = "mp3";
  public final static String                 ogg             = "ogg";
  public final static String                 wav             = "wav";
	public final static String                 midi            = "midi";
  public final static String                 wma             = "wma";
  
  //image
  public final static String                 jpg             = "jpg";
	public final static String                 png             = "png";
	public final static String                 ico             = "ico";
	public final static String                 gif             = "gif";
	public final static String                 svg             = "svg";
	public final static String                 bmp             = "bmp";
  
  //zip
  public final static String                 rar             = "rar";
  public final static String                 zip             = "zip";
  
  public final static List<String> videoFileExts = new ArrayList<>();
  public final static List<String> audioFileExts = new ArrayList<>();
  public final static List<String> imageFileExts = new ArrayList<>();
  public final static List<String> zipFileExts   = new ArrayList<>();
  
  private static SSFileExtU inst = new SSFileExtU();

  public static boolean isNotAudioOrVideoFileExt(String fileExt) {
    return !isAudioOrVideoFileExt(fileExt);
  }
  
  public static boolean isAudioOrVideoFileExt(String fileExt){
    
    if(
        SSFileExtU.audioFileExts.contains(fileExt) ||
        SSFileExtU.videoFileExts.contains(fileExt)){
      return true;
    }
    
    return false;
  }

  public static String ext(String fileName) {
    
    if(
        SSStrU.isEmpty       (fileName) ||
        fileName.lastIndexOf (SSStrU.dot) == -1){
      return null;
    }
    
    return fileName.substring(fileName.lastIndexOf(SSStrU.dot) + 1);
  }
  
  private SSFileExtU(){
  
    videoFileExts.add(avi);
    videoFileExts.add(mpeg);
    videoFileExts.add(mpg);
    videoFileExts.add(mov);
    videoFileExts.add(flv);
    videoFileExts.add(wmv);

    audioFileExts.add(mp3);
    audioFileExts.add(ogg);
    audioFileExts.add(wav);
    audioFileExts.add(midi);
    audioFileExts.add(wma);
    
    imageFileExts.add(jpg);
    imageFileExts.add(png);
    imageFileExts.add(ico);
    imageFileExts.add(gif);
    imageFileExts.add(svg);
    imageFileExts.add(bmp);
    
    zipFileExts.add(rar);
    zipFileExts.add(zip);
  }
}