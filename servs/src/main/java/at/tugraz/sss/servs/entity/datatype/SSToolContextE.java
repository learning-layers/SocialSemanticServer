/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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

/**
 * @author Dieter Theiler
 */

package at.tugraz.sss.servs.entity.datatype;

import at.tugraz.sss.servs.entity.datatype.SSErr;
import at.tugraz.sss.servs.common.impl.SSServErrReg;
import java.util.ArrayList;
import java.util.List;

public enum SSToolContextE{
  
  timelineArea,
  help,
  organizeArea,
  searchTab,
  episodeTab,
  bitTab,
  notificationTab,
  emailImport,
  evernoteImport,
  sss;
  
  public static SSToolContextE get(final String space) throws SSErr{
    
    try{
      
      if(space == null){
        return null;
      }
      
      return SSToolContextE.valueOf(space);
    }catch(IllegalArgumentException error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  public static List<SSToolContextE> get(final List<String> strings) throws SSErr{

    final List<SSToolContextE> result = new ArrayList<>();
    
    if(strings == null){
      return result;
    }
    
    for(String string : strings){
      result.add(get(string));
    }
    
    return result;
  }
}
