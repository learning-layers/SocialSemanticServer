package at.kc.tugraz.socialserver.utils;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;
import java.io.FileInputStream;
import java.io.IOException;
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

public class ProfileImageReplacedElementFactory implements ReplacedElementFactory {
  
  private final ReplacedElementFactory superFactory;
  
  public ProfileImageReplacedElementFactory(ReplacedElementFactory superFactory) {
    this.superFactory = superFactory;
  }
  
  @Override
  public ReplacedElement createReplacedElement(
    LayoutContext layoutContext,
    BlockBox blockBox,
    UserAgentCallback userAgentCallback,
    int cssWidth,
    int cssHeight){
    
    Element element = blockBox.getElement();
    if (element == null) {
      return null;
    }
    
    String nodeName  = element.getNodeName();
    String className = element.getAttribute("class");
    String href      = element.getAttribute("href");
    String width      = element.getAttribute("width");
    String height      = element.getAttribute("height");
    
    if ("div".equals(nodeName) && className.contains("xmyImagex") && href != null && width != null && height != null) {
      
      InputStream input = null;
      
//      href= "F:\\workspace_git\\SocialSemanticServer\\ss\\tmp\\2781641170122541.png";
      try {
        input = new FileInputStream(href);
        
        byte[] bytes = IOUtils.toByteArray(input);
        Image image = Image.getInstance(bytes);
        
        FSImage fsImage = new ITextFSImage(image);
        
        if(fsImage != null) {
          
          fsImage.scale(Integer.valueOf(width), Integer.valueOf(height));
          
          return new ITextImageElement(fsImage);
        }
      }catch(Exception error){
        System.err.println(error);
      } finally {
        IOUtils.closeQuietly(input);
      }
    }
    
    return superFactory.createReplacedElement(layoutContext, blockBox, userAgentCallback, cssWidth, cssHeight);
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