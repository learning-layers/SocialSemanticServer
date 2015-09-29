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
package at.tugraz.sss.serv;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public enum SSMimeTypeE {
  
  applicationZipCompressed                                   ("application/x-zip-compressed"),
  applicationZip                                             ("application/zip"),
  applicationBin                                             ("application/octet-stream"),
  applicationOpenOfficeDoc                                   ("application/vnd.oasis.opendocument.text"),
  applicationPdf                                             ("application/pdf"),
  applicationMsword                                          ("application/msword"),
  applicationMsaccess                                        ("application/msaccess"),
  applicationMsexcel                                         ("application/vnd.ms-excel"),
  applicationMspowerpoint                                    ("application/vnd.ms-powerpoint"),
  applicationMspowerpoint2007                                ("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
  applicationMsword2007                                      ("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
  applicationMsaccess2007                                    ("application/msaccess"),
  applicationMsexcel2007                                     ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
  applicationMsexcelBinary                                   ("application/vnd.ms-excel.sheet.binary.macroEnabled.12"),
  applicationAtom                                            ("application/atom+xml"),
  applicationLatex                                           ("application/x-latex"),
  applicationRdf                                             ("application/rdf+xml"),
  applicationOgg                                             ("application/ogg"),
  applicationJson                                            ("application/json"),
  multipartFormData                                          ("multipart/form-data"),
  textPlain                                                  ("text/plain"),
  textHtml                                                   ("text/html"),
  textCss                                                    ("text/css"),
  textVcard                                                  ("text/x-vcard"),
  imageJpeg                                                  ("image/jpeg"),
  imagePng                                                   ("image/png"),
  imageIcon                                                  ("image/x-icon"),
  imageGif                                                   ("image/gif"),
  imageSvg                                                   ("image/svg+xml"),
  imageBmp                                                   ("image/bmp"),
  imageTiff                                                  ("image/tiff"),
  videoAvi                                                   ("video/avi"),
  videoMp4                                                   ("video/mp4"),
  videoMpeg                                                  ("video/mpeg"),
  videoQuicktime                                             ("video/quicktime"),
  audioMp3                                                   ("audio/mp3"),
  audioMidi                                                  ("audio/midi"),
  audioMpeg4                                                 ("audio/x-m4a"),
  audioAmr                                                   ("audio/amr"),
  audioMpeg                                                  ("audio/mpeg"),
  audioWav                                                   ("audio/wav");
  
  public static Map<String, SSMimeTypeE>       mimeTypes           = new HashMap<>();
  public static Map<SSMimeTypeE, SSFileExtE>   fileExtPerMimeType  = new EnumMap<>(SSMimeTypeE.class);
  public static Map<SSFileExtE,  SSMimeTypeE>  mimeTypePerFileExt  = new EnumMap<>(SSFileExtE.class);
  private final String                         value;
  
  private SSMimeTypeE(final String newValue) {
    value = newValue;
  }
  
  @Override
  public String toString(){
    return value;
  }
  
  public static SSMimeTypeE get(final String value) throws Exception{
    
    if(!mimeTypes.containsKey(value)){
      throw new Exception("mime type not found for value");
    }
    
    return mimeTypes.get(value);
  }
  
  public static synchronized void init() throws Exception{
    
    mimeTypes.put(applicationZipCompressed.toString(),         applicationZipCompressed);
    mimeTypes.put(applicationZip.toString(),                   applicationZip);
    mimeTypes.put(applicationBin.toString(),                   applicationBin);
    mimeTypes.put(applicationOpenOfficeDoc.toString(),         applicationOpenOfficeDoc);
    mimeTypes.put(applicationPdf.toString(),                   applicationPdf);
    mimeTypes.put(applicationMsword.toString(),                applicationMsword);
    mimeTypes.put(applicationMsaccess.toString(),              applicationMsaccess);
    mimeTypes.put(applicationMsexcel.toString(),               applicationMsexcel);
    mimeTypes.put(applicationMspowerpoint.toString(),          applicationMspowerpoint);
    mimeTypes.put(applicationMspowerpoint2007.toString(),      applicationMspowerpoint2007);
    mimeTypes.put(applicationMsword2007.toString(),            applicationMsword2007);
    mimeTypes.put(applicationMsaccess2007.toString(),          applicationMsaccess2007);
    mimeTypes.put(applicationMsexcel2007.toString(),           applicationMsexcel2007);
    mimeTypes.put(applicationMsexcelBinary.toString(),         applicationMsexcelBinary);
    mimeTypes.put(applicationAtom.toString(),                  applicationAtom);
    mimeTypes.put(applicationLatex.toString(),                 applicationLatex);
    mimeTypes.put(applicationRdf.toString(),                   applicationRdf);
    mimeTypes.put(applicationOgg.toString(),                   applicationOgg);
    mimeTypes.put(applicationJson.toString(),                  applicationJson);
    mimeTypes.put(multipartFormData.toString(),                multipartFormData);
    mimeTypes.put(textPlain.toString(),                        textPlain);
    mimeTypes.put(textHtml.toString(),                         textHtml);
    mimeTypes.put(textCss.toString(),                          textCss);
    mimeTypes.put(textVcard.toString(),                        textVcard);
    mimeTypes.put(imageJpeg.toString(),                        imageJpeg);
    mimeTypes.put(imagePng.toString(),                         imagePng);
    mimeTypes.put(imageIcon.toString(),                        imageIcon);
    mimeTypes.put(imageGif.toString(),                         imageGif);
    mimeTypes.put(imageSvg.toString(),                         imageSvg);
    mimeTypes.put(imageBmp.toString(),                         imageBmp);
    mimeTypes.put(imageTiff.toString(),                        imageTiff);
    mimeTypes.put(videoAvi.toString(),                         videoAvi);
    mimeTypes.put(videoMp4.toString(),                         videoMp4);
    mimeTypes.put(videoMpeg.toString(),                        videoMpeg);
    mimeTypes.put(videoQuicktime.toString(),                   videoQuicktime);
    mimeTypes.put(audioMp3.toString(),                         audioMp3);
    mimeTypes.put(audioMidi.toString(),                        audioMidi);
    mimeTypes.put(audioMpeg4.toString(),                       audioMpeg4);
    mimeTypes.put(audioAmr.toString(),                         audioAmr);
    mimeTypes.put(audioMpeg.toString(),                        audioMpeg);
    mimeTypes.put(audioWav.toString(),                         audioWav);
    
    fileExtPerMimeType.put(applicationZipCompressed,     SSFileExtE.zip);
    fileExtPerMimeType.put(applicationZip,               SSFileExtE.zip);
    fileExtPerMimeType.put(applicationBin,               SSFileExtE.bin);
    fileExtPerMimeType.put(applicationOpenOfficeDoc,     SSFileExtE.odt);
    fileExtPerMimeType.put(applicationPdf,               SSFileExtE.pdf);
    fileExtPerMimeType.put(applicationMsword,            SSFileExtE.doc);
    fileExtPerMimeType.put(applicationMspowerpoint,      SSFileExtE.ppt);
    fileExtPerMimeType.put(applicationMsaccess,          SSFileExtE.mdb);
    fileExtPerMimeType.put(applicationMsexcel,           SSFileExtE.xls);
    fileExtPerMimeType.put(applicationMsword2007,        SSFileExtE.docx);
    fileExtPerMimeType.put(applicationMspowerpoint2007,  SSFileExtE.pptx);
    fileExtPerMimeType.put(applicationMsaccess2007,      SSFileExtE.accdb);
    fileExtPerMimeType.put(applicationMsexcel2007,       SSFileExtE.xlsx);
    fileExtPerMimeType.put(applicationMsexcelBinary,     SSFileExtE.xlsb);
    fileExtPerMimeType.put(textPlain,                    SSFileExtE.txt);
    fileExtPerMimeType.put(textHtml,                     SSFileExtE.html);
    fileExtPerMimeType.put(textVcard,                    SSFileExtE.vcf);
    fileExtPerMimeType.put(textCss,                      SSFileExtE.css);
    fileExtPerMimeType.put(imageJpeg,                    SSFileExtE.jpg);
    fileExtPerMimeType.put(imagePng,                     SSFileExtE.png);
    fileExtPerMimeType.put(videoAvi,                     SSFileExtE.avi);
    fileExtPerMimeType.put(videoMpeg,                    SSFileExtE.mpeg);
    fileExtPerMimeType.put(videoQuicktime,               SSFileExtE.mov);
    fileExtPerMimeType.put(audioMidi,                    SSFileExtE.midi);
    fileExtPerMimeType.put(audioMpeg4,                   SSFileExtE.m4a);
    fileExtPerMimeType.put(audioMp3,                     SSFileExtE.mp3);
    fileExtPerMimeType.put(audioAmr,                     SSFileExtE.amr);
    fileExtPerMimeType.put(audioMpeg,                    SSFileExtE.mp3);
    fileExtPerMimeType.put(audioWav,                     SSFileExtE.wav);
    fileExtPerMimeType.put(videoMp4,                     SSFileExtE.m4v);
    fileExtPerMimeType.put(imageIcon,                    SSFileExtE.ico);
    fileExtPerMimeType.put(imageGif,                     SSFileExtE.gif);
    fileExtPerMimeType.put(imageBmp,                     SSFileExtE.bmp);
    fileExtPerMimeType.put(imageTiff,                    SSFileExtE.tiff);
    fileExtPerMimeType.put(applicationAtom,              SSFileExtE.atom);
    fileExtPerMimeType.put(applicationLatex,             SSFileExtE.latex);
    fileExtPerMimeType.put(imageSvg,                     SSFileExtE.svg);
    fileExtPerMimeType.put(applicationRdf,               SSFileExtE.rdf);
    fileExtPerMimeType.put(applicationOgg,               SSFileExtE.ogg);
    
    mimeTypePerFileExt.put(SSFileExtE.zip,   applicationZipCompressed);
    mimeTypePerFileExt.put(SSFileExtE.zip,   applicationZip);
    mimeTypePerFileExt.put(SSFileExtE.bin,   applicationBin);
    mimeTypePerFileExt.put(SSFileExtE.odt,   applicationOpenOfficeDoc);
    mimeTypePerFileExt.put(SSFileExtE.pdf,   applicationPdf);
    mimeTypePerFileExt.put(SSFileExtE.doc,   applicationMsword);
    mimeTypePerFileExt.put(SSFileExtE.ppt,   applicationMspowerpoint);
    mimeTypePerFileExt.put(SSFileExtE.pptx,  applicationMspowerpoint2007);
    mimeTypePerFileExt.put(SSFileExtE.mdb,   applicationMsaccess);
    mimeTypePerFileExt.put(SSFileExtE.xls,   applicationMsexcel);
    mimeTypePerFileExt.put(SSFileExtE.docx,  applicationMsword2007);
    mimeTypePerFileExt.put(SSFileExtE.accdb, applicationMsaccess2007);
    mimeTypePerFileExt.put(SSFileExtE.xlsx,  applicationMsexcel2007);
    mimeTypePerFileExt.put(SSFileExtE.xlsb,  applicationMsexcelBinary);
    mimeTypePerFileExt.put(SSFileExtE.jpg,   imageJpeg);
    mimeTypePerFileExt.put(SSFileExtE.jpeg,  imageJpeg);
    mimeTypePerFileExt.put(SSFileExtE.png,   imagePng);
    mimeTypePerFileExt.put(SSFileExtE.mp3,   audioMp3);
    mimeTypePerFileExt.put(SSFileExtE.m4a,   audioMpeg4);
    mimeTypePerFileExt.put(SSFileExtE.midi,  audioMidi);
    mimeTypePerFileExt.put(SSFileExtE.amr,   audioAmr);
    mimeTypePerFileExt.put(SSFileExtE.mp3,   audioMpeg);
    mimeTypePerFileExt.put(SSFileExtE.wav,   audioWav);
    mimeTypePerFileExt.put(SSFileExtE.avi,   videoAvi);
    mimeTypePerFileExt.put(SSFileExtE.m4v,   videoMp4);
    mimeTypePerFileExt.put(SSFileExtE.mpeg,  videoMpeg);
    mimeTypePerFileExt.put(SSFileExtE.mov,   videoQuicktime);
    mimeTypePerFileExt.put(SSFileExtE.ico,   imageIcon);
    mimeTypePerFileExt.put(SSFileExtE.txt,   textPlain);
    mimeTypePerFileExt.put(SSFileExtE.html,  textHtml);
    mimeTypePerFileExt.put(SSFileExtE.css,   textCss);
    mimeTypePerFileExt.put(SSFileExtE.vcf,   textVcard);
    mimeTypePerFileExt.put(SSFileExtE.gif,   imageGif);
    mimeTypePerFileExt.put(SSFileExtE.bmp,   imageBmp);
    mimeTypePerFileExt.put(SSFileExtE.tif,   imageTiff);
    mimeTypePerFileExt.put(SSFileExtE.tiff,  imageTiff);
    mimeTypePerFileExt.put(SSFileExtE.atom,  applicationAtom);
    mimeTypePerFileExt.put(SSFileExtE.latex, applicationLatex);
    mimeTypePerFileExt.put(SSFileExtE.svg,   imageSvg);
    mimeTypePerFileExt.put(SSFileExtE.rdf,   applicationRdf);
    mimeTypePerFileExt.put(SSFileExtE.ogg,   applicationOgg);
  }
  
  public static SSFileExtE fileExtForMimeType(final SSMimeTypeE mimeType) throws Exception{
    
    if(fileExtPerMimeType.containsKey(mimeType)){
      return fileExtPerMimeType.get(mimeType);
    }
    
    throw new Exception("file ext not found for mime type: " + mimeType);
  }
  
  public static SSFileExtE fileExtForMimeType1(final String mimeType) throws Exception{
    
    SSMimeTypeE mime;
    
    try{
      mime = get(mimeType);
    }catch(Exception error){
      throw new Exception("mime type not found for mime: " + mimeType);
    }
    
    if(fileExtPerMimeType.containsKey(mime)){
      return fileExtPerMimeType.get(mime);
    }
    
    throw new Exception("file ext not found for mime type: " + mimeType);
  }
  
  public static SSMimeTypeE mimeTypeForFileExt(final SSFileExtE ext) throws Exception{
    
    if(mimeTypePerFileExt.containsKey(ext)){
      return mimeTypePerFileExt.get(ext);
    }
    
    throw new Exception("mime type not found for file ext");
  }
}
