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

import java.util.HashMap;
import java.util.Map;

public class SSMimeTypeU {

  public final static String                 applicationZip                                             = "application/zip";
  public final static String                 applicationBin                                             = "application/octet-stream";
  public final static String                 applicationOpenOfficeDoc                                   = "application/vnd.oasis.opendocument.text";
  public final static String                 applicationPdf                                             = "application/pdf";
	public final static String                 applicationMsword                                          = "application/msword";
	public final static String                 applicationMsaccess                                        = "application/msaccess";
	public final static String                 applicationMsexcel                                         = "application/vnd.ms-excel";
  public final static String                 applicationMspowerpoint                                    = "application/vnd.ms-powerpoint";
  public final static String                 applicationMspowerpoint2007                                = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
	public final static String                 applicationMsword2007                                      = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	public final static String                 applicationMsaccess2007                                    = "application/msaccess";
	public final static String                 applicationMsexcel2007                                     = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public final static String                 applicationAtom                                            = "application/atom+xml";
	public final static String                 applicationLatex                                           = "application/x-latex";
	public final static String                 applicationRdf                                             = "application/rdf+xml";
	public final static String                 applicationOgg                                             = "application/ogg";
  public final static String                 applicationJson                                            = "application/json";
  public final static String                 multipartFormData                                          = "multipart/form-data";
	public final static String                 textPlain                                                  = "text/plain";
	public final static String                 textHtml                                                   = "text/html";
	public final static String                 textCss                                                    = "text/css";
  public final static String                 textVcard                                                  = "text/x-vcard";
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
  public final static String                 audioMpeg4                                                 = "audio/x-m4a";
  public final static String                 audioAmr                                                   = "audio/amr";
  public final static String                 audioMpeg                                                  = "audio/mpeg";
  
  public       static Map<String, String>    mimeTypesFileExt                                           = null;
  public       static Map<String, String>    fileExtForMimTypes                                         = null;
  
  private SSMimeTypeU(){}
  
  public static synchronized void init() throws Exception{
    
    mimeTypesFileExt   = new HashMap<>();
    fileExtForMimTypes = new HashMap<>();
      
    mimeTypesFileExt.put(applicationZip,               SSFileExtE.zip.toString());
    mimeTypesFileExt.put(applicationBin,               SSFileExtE.bin.toString());
    mimeTypesFileExt.put(applicationOpenOfficeDoc,     SSFileExtE.odt.toString());
    mimeTypesFileExt.put(applicationPdf,               SSFileExtE.pdf.toString());
    mimeTypesFileExt.put(applicationMsword,            SSFileExtE.doc.toString());
    mimeTypesFileExt.put(applicationMspowerpoint,      SSFileExtE.ppt.toString());
    mimeTypesFileExt.put(applicationMsaccess,          SSFileExtE.mdb.toString());
    mimeTypesFileExt.put(applicationMsexcel,           SSFileExtE.xls.toString());
    mimeTypesFileExt.put(applicationMsword2007,        SSFileExtE.docx.toString());
    mimeTypesFileExt.put(applicationMspowerpoint2007,  SSFileExtE.pptx.toString());
    mimeTypesFileExt.put(applicationMsaccess2007,      SSFileExtE.accdb.toString());
    mimeTypesFileExt.put(applicationMsexcel2007,       SSFileExtE.xlsx.toString());
    mimeTypesFileExt.put(textPlain,                    SSFileExtE.txt.toString());
    mimeTypesFileExt.put(textHtml,                     SSFileExtE.html.toString());
    mimeTypesFileExt.put(textVcard,                    SSFileExtE.vcf.toString());
    mimeTypesFileExt.put(imageJpeg,                    SSFileExtE.jpg.toString());
    mimeTypesFileExt.put(imagePng,                     SSFileExtE.png.toString());
    mimeTypesFileExt.put(videoAvi,                     SSFileExtE.avi.toString());
    mimeTypesFileExt.put(videoMpeg,                    SSFileExtE.mpeg.toString());
    mimeTypesFileExt.put(videoQuicktime,               SSFileExtE.mov.toString());
    mimeTypesFileExt.put(audioMidi,                    SSFileExtE.midi.toString());
    mimeTypesFileExt.put(audioMpeg4,                   SSFileExtE.m4a.toString());
    mimeTypesFileExt.put(audioMp3,                     SSFileExtE.mp3.toString()); 
    mimeTypesFileExt.put(audioAmr,                     SSFileExtE.amr.toString());
    mimeTypesFileExt.put(audioMpeg,                    SSFileExtE.mp3.toString());
    mimeTypesFileExt.put(videoMp4,                     SSFileExtE.m4v.toString());
    mimeTypesFileExt.put(imageIcon,                    SSFileExtE.ico.toString());
    mimeTypesFileExt.put(imageGif,                     SSFileExtE.gif.toString());
    mimeTypesFileExt.put(imageBmp,                     SSFileExtE.bmp.toString());
    mimeTypesFileExt.put(applicationAtom,              SSFileExtE.atom.toString());
    mimeTypesFileExt.put(applicationLatex,             SSFileExtE.latex.toString());
    mimeTypesFileExt.put(imageSvg,                     SSFileExtE.svg.toString());
    mimeTypesFileExt.put(applicationRdf,               SSFileExtE.rdf.toString());
    mimeTypesFileExt.put(applicationOgg,               SSFileExtE.ogg.toString());
    
    fileExtForMimTypes.put(SSFileExtE.zip.toString(),   applicationZip);
    fileExtForMimTypes.put(SSFileExtE.bin.toString(),   applicationBin);
    fileExtForMimTypes.put(SSFileExtE.odt.toString(),   applicationOpenOfficeDoc);
    fileExtForMimTypes.put(SSFileExtE.pdf.toString(),   applicationPdf);
    fileExtForMimTypes.put(SSFileExtE.doc.toString(),   applicationMsword);
    fileExtForMimTypes.put(SSFileExtE.ppt.toString(),   applicationMspowerpoint);
    fileExtForMimTypes.put(SSFileExtE.pptx.toString(),  applicationMspowerpoint2007);
    fileExtForMimTypes.put(SSFileExtE.mdb.toString(),   applicationMsaccess);
    fileExtForMimTypes.put(SSFileExtE.xls.toString(),   applicationMsexcel);
    fileExtForMimTypes.put(SSFileExtE.docx.toString(),  applicationMsword2007);
    fileExtForMimTypes.put(SSFileExtE.accdb.toString(), applicationMsaccess2007);
    fileExtForMimTypes.put(SSFileExtE.xlsx.toString(),  applicationMsexcel2007);
    fileExtForMimTypes.put(SSFileExtE.jpg.toString(),   imageJpeg);
    fileExtForMimTypes.put(SSFileExtE.jpeg.toString(),  imageJpeg);
    fileExtForMimTypes.put(SSFileExtE.png.toString(),   imagePng);
    fileExtForMimTypes.put(SSFileExtE.mp3.toString(),   audioMp3);
    fileExtForMimTypes.put(SSFileExtE.m4a.toString(),   audioMpeg4);
    fileExtForMimTypes.put(SSFileExtE.midi.toString(),  audioMidi);
    fileExtForMimTypes.put(SSFileExtE.amr.toString(),   audioAmr);
    fileExtForMimTypes.put(SSFileExtE.mp3.toString(),   audioMpeg);
    fileExtForMimTypes.put(SSFileExtE.avi.toString(),   videoAvi);
    fileExtForMimTypes.put(SSFileExtE.m4v.toString(),   videoMp4);
    fileExtForMimTypes.put(SSFileExtE.mpeg.toString(),  videoMpeg);
    fileExtForMimTypes.put(SSFileExtE.mov.toString(),   videoQuicktime);
    fileExtForMimTypes.put(SSFileExtE.ico.toString(),   imageIcon);
    fileExtForMimTypes.put(SSFileExtE.txt.toString(),   textPlain);
    fileExtForMimTypes.put(SSFileExtE.html.toString(),  textHtml);
    fileExtForMimTypes.put(SSFileExtE.css.toString(),   textCss);
    fileExtForMimTypes.put(SSFileExtE.vcf.toString(),   textVcard);
    fileExtForMimTypes.put(SSFileExtE.gif.toString(),   imageGif);
    fileExtForMimTypes.put(SSFileExtE.bmp.toString(),   imageBmp);
    fileExtForMimTypes.put(SSFileExtE.atom.toString(),  applicationAtom);
    fileExtForMimTypes.put(SSFileExtE.latex.toString(), applicationLatex);
    fileExtForMimTypes.put(SSFileExtE.svg.toString(),   imageSvg);
    fileExtForMimTypes.put(SSFileExtE.rdf.toString(),   applicationRdf);
    fileExtForMimTypes.put(SSFileExtE.ogg.toString(),   applicationOgg);
  }
  
  public static String fileExtForMimeType(String mimeType) throws Exception{
		
    if(mimeTypesFileExt.containsKey(mimeType)){
      return mimeTypesFileExt.get(mimeType);
    }
    
		SSLogU.errThrow(new Exception("file ext not found for mime type: " + mimeType));
    return null;
	}
  
  public static String mimeTypeForFileExt(String ext) throws Exception{
		
    if(fileExtForMimTypes.containsKey(ext)){
      return fileExtForMimTypes.get(ext);
    }
    
		SSLogU.errThrow(new Exception("mime type not found for file ext"));
    return null;
	}
}
