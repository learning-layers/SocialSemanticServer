/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.kc.tugraz.ss.service.disc.impl.fct.op;

import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.disc.datatypes.enums.SSDiscE;
import at.kc.tugraz.ss.service.disc.impl.fct.sql.SSDiscSQLFct;

public class SSDiscUserEntryAddFct{
  
  public static SSUri addDisc(
    final SSDiscSQLFct sqlFct,
    final SSUri        userUri, 
    SSUri              targetUri,
    final SSDiscE      discType, 
    final SSLabel      discLabel) throws Exception{
    
    try{
      final SSUri discUri = sqlFct.createDiscUri();
      
      if(targetUri == null){
        targetUri = discUri;
      }
      
      SSServCaller.entityAdd(
        userUri,
        targetUri,
        SSLabel.get(targetUri),
        SSEntityE.entity,
        false);
      
      SSServCaller.entityAdd(
        userUri,
        discUri,
        discLabel,
        SSEntityE.disc,
        false);
      
      sqlFct.addDisc(
        discUri, 
        targetUri, 
        discType);
      
      return discUri;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  public static SSUri addDiscEntry(
    final SSDiscSQLFct  sqlFct, 
    final SSUri         userUri,
    final SSUri         discUri, 
    final SSTextComment content) throws Exception{
    
    try{
      final SSUri discEntryUri = sqlFct.createDiscEntryUri();
      
      SSServCaller.entityAdd(
        userUri,
        discEntryUri,
        SSLabel.get(discEntryUri),
        SSEntityE.discEntry,
        false);
      
      sqlFct.addDiscEntry(
        discEntryUri, 
        discUri, 
        content);
      
      return discEntryUri;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
