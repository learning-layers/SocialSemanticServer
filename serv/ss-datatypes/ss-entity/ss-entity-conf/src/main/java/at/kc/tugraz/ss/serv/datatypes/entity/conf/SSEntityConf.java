/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.kc.tugraz.ss.serv.serv.datatypes.entity.conf;

import at.kc.tugraz.ss.serv.serv.api.SSCoreServConfA;

public class SSEntityConf extends SSCoreServConfA{
  
  public static SSEntityConf copy(final SSEntityConf orig){
    
    final SSEntityConf copy = (SSEntityConf) SSCoreServConfA.copy(orig, new SSEntityConf());
    
    return copy;
  }
}
