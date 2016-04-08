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
package at.tugraz.sss.servs.coll.datatype;


import at.tugraz.sss.serv.datatype.ret.SSServRetI; import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.datatype.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SSCollUserEntriesDeleteRet extends SSServRetI{

  public List<SSUri> entries = new ArrayList<>();

  public List<String> getEntries(){
    return SSStrU.removeTrailingSlash(entries);
  }
  
  public static SSCollUserEntriesDeleteRet get(List<SSUri> entries){
    return new SSCollUserEntriesDeleteRet(entries);
  }
  
  private SSCollUserEntriesDeleteRet(List<SSUri> entries) {

    super(SSVarNames.collEntriesDelete);
    
    SSUri.addDistinctWithoutNull(this.entries, entries);
  }
}
