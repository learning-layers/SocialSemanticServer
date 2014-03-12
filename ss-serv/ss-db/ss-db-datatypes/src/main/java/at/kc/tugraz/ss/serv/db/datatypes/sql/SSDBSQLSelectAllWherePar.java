/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.kc.tugraz.ss.serv.db.datatypes.sql;

import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import java.util.Map;

public class SSDBSQLSelectAllWherePar  extends SSServPar{
  
  public String              tableName;
  public Map<String, String> whereParNamesWithValues;
  
  public SSDBSQLSelectAllWherePar(SSServPar par) throws Exception{
   
    super(par);
    
    try{
      if(pars != null){
        this.tableName               = (String) pars.get(SSVarU.tableName);
        this.whereParNamesWithValues = (Map<String, String>) pars.get(SSVarU.whereParNamesWithValues);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}