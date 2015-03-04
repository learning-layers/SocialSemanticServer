/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

import com.lowagie.text.Image;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.poi.util.IOUtils;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

public class SSPDFFromXHTMLImageEmbedder implements ReplacedElementFactory {
  
  private final ReplacedElementFactory superFactory;
  
  public SSPDFFromXHTMLImageEmbedder(ReplacedElementFactory superFactory) {
    this.superFactory = superFactory;
  }
  
  @Override
  public ReplacedElement createReplacedElement(
    LayoutContext     layoutContext,
    BlockBox          blockBox,
    UserAgentCallback uac,
    int               cssWidth,
    int               cssHeight){
    
    Element element = blockBox.getElement();
    
    if (element == null){
      return null;
    }
    
    String nodeName  = element.getNodeName();
    String className = element.getAttribute("class");
    String src       = element.getAttribute("src");
    
    if(
      !"img".equals      (nodeName)    ||
      !className.contains("xmyImagex") || 
      src == null){
      
      return superFactory.createReplacedElement(
        layoutContext, 
        blockBox, 
        uac, 
        cssWidth, 
        cssHeight);
    }
    
    InputStream in = null;
    
    try{
      
      try{
        in                    = new FileInputStream(src);
      }catch(Exception error){
        SSLogU.warn("to embed image no found");
        throw error;
      }
      
      final byte[]  bytes   = IOUtils.toByteArray(in);
      final Image   image   = Image.getInstance(bytes);
      final FSImage fsImage = new ITextFSImage(image);
      
      fsImage.scale(cssWidth, cssHeight);
        
      return new ITextImageElement(fsImage);
    }catch(Exception error){
      
      SSLogU.warn(error.getMessage());
      
      return superFactory.createReplacedElement(
        layoutContext, 
        blockBox, 
        uac, 
        cssWidth, 
        cssHeight);
    } finally {
      IOUtils.closeQuietly(in);
    }
  }
  
  @Override
  public void reset() {
    superFactory.reset();
  }
  
  @Override
  public void remove(Element e) {
    superFactory.remove(e);
  }
  
  @Override
  public void setFormSubmissionListener(FormSubmissionListener listener) {
    superFactory.setFormSubmissionListener(listener);
  }
}