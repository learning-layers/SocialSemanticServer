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
package at.tugraz.sss.serv.impl.api;

import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI;
import at.tugraz.sss.serv.datatype.par.SSServPar;
import at.tugraz.sss.serv.reg.*;

public abstract class SSServImplStartA extends SSServImplA{

  protected SSDBSQLI   dbSQL;
  protected SSDBNoSQLI dbNoSQL;
  
  @Override
  public void destroy() throws SSErr{
    
    SSServReg.destroy();
    
//    List<SSServImplA> usedServs = new ArrayList<>();
//    
//    try{
//      servImplsUsedByThread.get().remove(this);
//      
//      usedServs.addAll(servImplsUsedByThread.get());
//      
//      for(SSServImplA servImpl : usedServs){
//        servImpl.finalizeImpl();
//      }
//            
//    }catch(Exception error){
//      SSServErrReg.regErr(error);
//    }finally{
//      SSServErrReg.logAndReset(true);
//    }
//      
//    servImplsUsedByThread.remove();
  }
  
  public SSServImplStartA(
    final SSConfA      conf){
    
    super(conf);
  }
  
  @Override
  public SSServRetI invokeClientServOp(
    final Class        servImplClientInteraceClass, 
    final SSServPar    par) throws SSErr{
    
    throw new UnsupportedOperationException(SSStrU.empty);
  }
}


// protected static final ThreadLocal<List<SSServImplA>> servImplsUsedByThread = new ThreadLocal<List<SSServImplA>>(){
//    
//    @Override protected List<SSServImplA> initialValue(){
//      
//      try{
//        return new ArrayList<>();
//      }catch (Exception error){
//        SSLogU.err(error);
//        return null;
//      }
//    }
//  };

//public static void regServImplUsedByThread(final SSServImplA servImpl){
//    
//    List<SSServImplA> servImplUsedList = servImplsUsedByThread.get();
//    
//    if(servImplUsedList.contains(servImpl)){
//      return;
//    }
//    
//    servImplUsedList.add(servImpl);
//  }