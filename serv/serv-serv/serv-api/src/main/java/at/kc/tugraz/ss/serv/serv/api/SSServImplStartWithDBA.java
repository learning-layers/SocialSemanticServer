/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.kc.tugraz.ss.serv.serv.api;

import at.kc.tugraz.ss.serv.db.api.SSDBSQLI;

public abstract class SSServImplStartWithDBA extends SSServImplStartA{
  
  public final SSDBSQLI   dbSQL;
  
  public SSServImplStartWithDBA(final SSConfA conf, final SSDBSQLI dbSQL){
    super(conf);
    
    this.dbSQL = dbSQL;
  }
}
