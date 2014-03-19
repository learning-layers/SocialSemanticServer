/**
 * Copyright 2014 Graz University of Technology - KTI (Knowledge Technologies Institute)
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

import java.util.HashMap;
import java.util.Map;

public class SSMimeTypeU {

  public final static String                 applicationPdf                                             = "application/pdf";
	public final static String                 applicationMsword                                          = "application/msword";
	public final static String                 applicationMsaccess                                        = "application/msaccess";
	public final static String                 applicationMsexcel                                         = "application/vnd.ms-excel";
  public final static String                 applicationMspowerpoint                                    = "application/vnd.ms-powerpoint";
	public final static String                 applicationMsword2007                                      = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	public final static String                 applicationMsaccess2007                                    = "application/msaccess";
	public final static String                 applicationMsexcel2007                                     = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public final static String                 applicationAtom                                            = "application/atom+xml";
	public final static String                 applicationLatex                                           = "application/x-latex";
	public final static String                 applicationRdf                                             = "application/rdf+xml";
	public final static String                 applicationOgg                                             = "application/ogg";
  public final static String                 applicationJson                                            = "application/json";
	public final static String                 textPlain                                                  = "text/plain";
	public final static String                 textHtml                                                   = "text/html";
	public final static String                 textCss                                                    = "text/css";
	public final static String                 imageJpeg                                                  = "image/jpeg";
	public final static String                 imagePng                                                   = "image/png";
	public final static String                 imageIcon                                                  = "image/x-icon";
	public final static String                 imageGif                                                   = "image/gif";
	public final static String                 imageSvg                                                   = "image/svg+xml";
	public final static String                 imageBmp                                                   = "image/bmp";
	public final static String                 videoAvi                                                   = "video/avi";
	public final static String                 videoMp4                                                   = "video/mp4";
	public final static String                 videoMpeg                                                  = "video/mpeg";
	public final static String                 videoQuicktime                                             = "video/quicktime";
	public final static String                 audioMp3                                                   = "audio/mp3";
	public final static String                 audioMidi                                                  = "audio/midi";
  
  public       static Map<String, String>    mimeTypesFileExt                                           = null;
  
  private SSMimeTypeU(){}
  
  public static synchronized void init() throws Exception{
    
    mimeTypesFileExt = new HashMap<String, String>();
      
    mimeTypesFileExt.put(applicationPdf,           SSFileExtU.pdf);
    mimeTypesFileExt.put(applicationMsword,        SSFileExtU.doc);
    mimeTypesFileExt.put(applicationMspowerpoint,  SSFileExtU.ppt);
    mimeTypesFileExt.put(applicationMsaccess,      SSFileExtU.mdb);
    mimeTypesFileExt.put(applicationMsexcel,       SSFileExtU.xls);
    mimeTypesFileExt.put(applicationMsword2007,    SSFileExtU.docx);
    mimeTypesFileExt.put(applicationMsaccess2007,  SSFileExtU.accdb);
    mimeTypesFileExt.put(applicationMsexcel2007,   SSFileExtU.xlsx);
    mimeTypesFileExt.put(textPlain,                SSFileExtU.txt);
    mimeTypesFileExt.put(imageJpeg,                SSFileExtU.jpg);
    mimeTypesFileExt.put(imagePng,                 SSFileExtU.png);
    mimeTypesFileExt.put(audioMp3,                 SSFileExtU.mp3);
    mimeTypesFileExt.put(videoAvi,                 SSFileExtU.avi);
    mimeTypesFileExt.put(videoMp4,                 SSFileExtU.mp4);
    mimeTypesFileExt.put(videoMpeg,                SSFileExtU.mpeg);
    mimeTypesFileExt.put(videoQuicktime,           SSFileExtU.mov);
    mimeTypesFileExt.put(audioMidi,                SSFileExtU.midi);
    mimeTypesFileExt.put(imageIcon,                SSFileExtU.ico);
    mimeTypesFileExt.put(textHtml,                 SSFileExtU.html);
    mimeTypesFileExt.put(imageGif,                 SSFileExtU.gif);
    mimeTypesFileExt.put(textCss,                  SSFileExtU.css);
    mimeTypesFileExt.put(imageBmp,                 SSFileExtU.bmp);
    mimeTypesFileExt.put(applicationAtom,          SSFileExtU.atom);
    mimeTypesFileExt.put(applicationLatex,         SSFileExtU.latex);
    mimeTypesFileExt.put(imageSvg,                 SSFileExtU.svg);
    mimeTypesFileExt.put(applicationRdf,           SSFileExtU.rdf);
    mimeTypesFileExt.put(applicationOgg,           SSFileExtU.ogg);
  }
  
  public static String fileExtForMimeType(String mimeType) throws Exception{
		
    if(mimeTypesFileExt.containsKey(mimeType)){
      return mimeTypesFileExt.get(mimeType);
    }
    
		SSLogU.errThrow(new Exception("file ext not found for mime type"));
    return null;
	}
}
