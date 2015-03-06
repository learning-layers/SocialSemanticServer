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
  
  public final static List<String> videoFileExts = new ArrayList<>();
  public final static List<String> audioFileExts = new ArrayList<>();
  public final static List<String> imageFileExts = new ArrayList<>();
  public final static List<String> zipFileExts   = new ArrayList<>();
  
  private static SSFileExtU inst = new SSFileExtU();

  public static boolean isAudioOrVideoFileExt(final String fileExt){
    
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
  
    videoFileExts.add(SSFileExtE.avi.toString());
    videoFileExts.add(SSFileExtE.m4v.toString());
    videoFileExts.add(SSFileExtE.mpeg.toString());
    videoFileExts.add(SSFileExtE.mpg.toString());
    videoFileExts.add(SSFileExtE.mov.toString());
    videoFileExts.add(SSFileExtE.flv.toString());
    videoFileExts.add(SSFileExtE.wmv.toString());

    audioFileExts.add(SSFileExtE.mp3.toString());
    audioFileExts.add(SSFileExtE.ogg.toString());
    audioFileExts.add(SSFileExtE.wav.toString());
    audioFileExts.add(SSFileExtE.midi.toString());
    audioFileExts.add(SSFileExtE.wma.toString());
    audioFileExts.add(SSFileExtE.m4a.toString());
    audioFileExts.add(SSFileExtE.amr.toString());
  
    imageFileExts.add(SSFileExtE.jpeg.toString());
    imageFileExts.add(SSFileExtE.jpg.toString());
    imageFileExts.add(SSFileExtE.png.toString());
    imageFileExts.add(SSFileExtE.ico.toString());
    imageFileExts.add(SSFileExtE.gif.toString());
    imageFileExts.add(SSFileExtE.svg.toString());
    imageFileExts.add(SSFileExtE.bmp.toString());
    
    zipFileExts.add(SSFileExtE.rar.toString());
    zipFileExts.add(SSFileExtE.zip.toString());
  }
}