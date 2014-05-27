/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.kc.tugraz.ss.service.disc.impl.fct.op;

import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.ss.datatypes.datatypes.SSTextComment;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.datatypes.datatypes.enums.SSEntityE;
import at.kc.tugraz.ss.datatypes.datatypes.label.SSLabel;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSCircleE;
import at.kc.tugraz.ss.serv.datatypes.entity.datatypes.SSEntityCircle;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.disc.datatypes.pars.SSDiscUserEntryAddPar;
import at.kc.tugraz.ss.service.disc.impl.fct.sql.SSDiscSQLFct;
import java.util.ArrayList;

public class SSDiscUserEntryAddFct{
  
  public static void addDisc(
    final SSDiscSQLFct sqlFct,
    final SSUri        discUri,
    final SSUri        userUri, 
    final SSUri        targetUri,
    final SSEntityE    discType, 
    final SSLabel      discLabel) throws Exception{
    
    try{
      
      final SSUri tmpTargetUri;
      
      if(targetUri == null){
        tmpTargetUri = discUri;
      }else{
        tmpTargetUri = targetUri;
      }
      
      SSServCaller.entityAdd(
        userUri,
        tmpTargetUri,
        SSLabel.get(tmpTargetUri),
        SSEntityE.entity,
        false);
      
      SSServCaller.entityAdd(
        userUri,
        discUri,
        discLabel,
        discType,
        false);
      
      SSServCaller.entityCircleCreate(
        userUri, 
        discUri,
        new ArrayList<SSUri>(), 
        SSCircleE.priv, 
        discLabel, 
        userUri, 
        false);
      
      sqlFct.addDisc(
        userUri, 
        discUri, 
        tmpTargetUri);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static SSUri addDiscEntry(
    final SSDiscSQLFct  sqlFct, 
    final SSUri         userUri,
    final SSUri         discUri, 
    final SSTextComment content) throws Exception{
    
    try{
      final SSUri     discEntryUri  = SSServCaller.vocURICreate ();
      final SSEntityE discType      = SSServCaller.entityGet    (discUri).type;
      SSEntityE       discEntryType = null;
      
      switch(discType){
        case disc: discEntryType = SSEntityE.discEntry;   break;
        case qa:   discEntryType = SSEntityE.qaEntry;     break;
        case chat: discEntryType = SSEntityE.chatEntry;   break;
        default: throw new Exception("disc type not valid");
      }
      
      SSServCaller.entityAdd(
        userUri,
        discEntryUri,
        SSLabel.get(discEntryUri),
        discEntryType,
        false);
      
      for(SSEntityCircle entityUserCircle : SSServCaller.entityUserEntityCirclesGet(userUri, discUri)){
        
        SSServCaller.entityEntitiesToCircleAdd(
          userUri,
          entityUserCircle.circleUri,
          discEntryUri,
          false);
      }
      
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
  
  public static void checkWhetherUserCanAddDisc(
    final SSDiscUserEntryAddPar par) throws Exception{
    
    try{
      
      if(SSObjU.isNull(par.discLabel, par.discType)){
        throw new Exception("label or disc type null");
      }
      
      switch(par.discType){
        case disc:
        case qa:
        case chat: break;
        default: throw new Exception("disc type not valid");
      }
      
      if(
        SSEntityE.equals (par.discType, SSEntityE.qa) &&
        SSObjU.isNull    (par.content)){
        
        throw new Exception("question content null");
      }
      
      if(
        !SSObjU.isNull(par.targetUri) &&
        !SSServCaller.entityUserCanRead(par.user, par.targetUri)){
        throw new Exception("user cannot edit disc target");
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  public static void checkWhetherUserCanAddDiscEntry(
    final SSDiscUserEntryAddPar par) throws Exception{
    
    try{
     
      if(SSObjU.isNull(par.content)){
        throw new Exception("content missing");
      }
      
      if(!SSServCaller.entityUserCanEdit(par.user, par.discUri)){
        throw new Exception("user cannot edit discussion");
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
}
