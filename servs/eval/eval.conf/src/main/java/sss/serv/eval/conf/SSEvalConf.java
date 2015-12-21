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
package sss.serv.eval.conf;

import at.tugraz.sss.serv.conf.SSCoreServConfA;
import at.tugraz.sss.serv.datatype.enums.SSToolE;
import java.util.ArrayList;
import java.util.List;

public class SSEvalConf extends SSCoreServConfA{
  
  public List<SSToolE> tools           = new ArrayList<>();
  
  public static SSEvalConf copy(final SSEvalConf orig){
    
    final SSEvalConf copy = (SSEvalConf) SSCoreServConfA.copy(orig, new SSEvalConf());
    
    if(orig.tools == null){
      copy.tools = new ArrayList<>();
    }else{
      copy.tools.addAll(orig.tools);
    }
    
    return copy;
  }
}