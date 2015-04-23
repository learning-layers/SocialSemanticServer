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

import java.util.HashMap;
import java.util.Map;

public abstract class SSServOpTestCaseA extends SSServImplStartA{
  
  protected final SSServOpE   op;
  protected String            jsonRequ;
  protected SSServPar         clientServPar;
  
  protected abstract void test()           throws Exception;
  protected abstract void testFromClient() throws Exception;
  protected abstract void setUp()          throws Exception;
  
  public SSServOpTestCaseA(final SSConfA conf, final SSDBSQLI dbSQL, final SSServOpE op){
    super(conf, dbSQL);
    
    this.op = op;
  }

  protected void tearDown() throws Exception{
  }
  
  protected void createJSONClientRetStr(final SSServRetI clientServResult) throws Exception{
    
    final Map<String, Object> ret = new HashMap<>();
    
    try{
      ret.put(SSVarU.op,                            SSStrU.toStr(op));
      ret.put(SSVarU.error,                         false);
      ret.put(SSStrU.toStr(op),                     clientServResult);
      ret.put(SSJSONLDU.context,                    SSJSONLDU.jsonLDContext(clientServResult.jsonLDDesc()));

      SSJSONU.jsonStr(ret);
    }catch(Exception error){
      SSLogU.err(error);
      throw error;
    }
  }
  
  @Override
  public void run(){
    
    try{
      
      setUp();
      test();
      testFromClient();
      tearDown();
      
    }catch(Exception error1){
      SSLogU.err(error1);
    }finally{
      try{
        finalizeImpl();
      }catch(Exception error2){
        SSLogU.err(error2);
      }
    }
  }
  
  @Override
  protected void finalizeImpl() throws Exception{
    finalizeThread(true);
  }
}