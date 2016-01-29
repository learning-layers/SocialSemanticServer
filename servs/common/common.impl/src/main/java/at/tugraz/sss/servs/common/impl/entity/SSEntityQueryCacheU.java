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
package at.tugraz.sss.servs.common.impl.entity;

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.util.SSDateU;
import at.tugraz.sss.serv.reg.SSServErrReg;
import java.util.Iterator;
import java.util.Map;

public class SSEntityQueryCacheU {
  
  public static void entityQueryCacheClean(final Map<String, SSEntityResultPages> entityQueryCache) throws SSErr{
    
    try{
      
      final Long                             now            = SSDateU.dateAsLong();
      final Long                             fiveMinutesAgo = now - SSDateU.minuteInMilliSeconds * 5;
      Map.Entry<String, SSEntityResultPages> entry;
      
      for(Iterator<Map.Entry<String, SSEntityResultPages>> it = entityQueryCache.entrySet().iterator(); it.hasNext();){
        
        entry = it.next();
        
        if(fiveMinutesAgo > entry.getValue().creationTime){
          it.remove();
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
