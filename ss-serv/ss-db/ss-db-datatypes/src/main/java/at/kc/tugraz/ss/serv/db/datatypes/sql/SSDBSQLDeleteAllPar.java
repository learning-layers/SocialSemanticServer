/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.kc.tugraz.ss.serv.db.datatypes.sql;

import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.serv.datatypes.SSServPar;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;

public class SSDBSQLDeleteAllPar extends SSServPar{
  
  public String tableName = null;
  
  public SSDBSQLDeleteAllPar(SSServPar par) throws Exception{
    
    super(par);
    
    try{
      
      if(pars != null){
        this.tableName = (String) pars.get(SSVarU.tableName);
      }
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
