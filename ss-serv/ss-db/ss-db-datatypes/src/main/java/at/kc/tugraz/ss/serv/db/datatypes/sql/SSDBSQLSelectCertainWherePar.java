/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.serv.db.datatypes.sql;

import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.util.List;
import java.util.Map;

public class SSDBSQLSelectCertainWherePar extends SSServPar{

  public List<String>        tableNames;
  public List<String>        columnNames;
  public Map<String, String> whereParNamesWithValues;
  public String              whereFixedRestriction;
  
  public SSDBSQLSelectCertainWherePar(SSServPar par) throws Exception{
   
    super(par);
    
    try{
      if(pars != null){
        this.tableNames              = (List<String>)        pars.get(SSVarU.tableNames);
        this.columnNames             = (List<String>)        pars.get(SSVarU.columnNames);
        this.whereParNamesWithValues = (Map<String, String>) pars.get(SSVarU.whereParNamesWithValues);
        this.whereFixedRestriction   = (String)              pars.get(SSVarU.whereFixedRestriction);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
