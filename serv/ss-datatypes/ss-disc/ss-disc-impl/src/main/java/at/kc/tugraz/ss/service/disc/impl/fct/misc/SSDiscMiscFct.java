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
package at.kc.tugraz.ss.service.disc.impl.fct.misc;

import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSEntityA;
import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import at.kc.tugraz.ss.serv.serv.caller.SSServCaller;
import at.kc.tugraz.ss.service.disc.datatypes.SSDisc;
import at.kc.tugraz.ss.service.disc.datatypes.SSDiscEntry;
import at.kc.tugraz.ss.service.disc.impl.fct.sql.SSDiscSQLFct;
import java.util.ArrayList;
import java.util.List;

public class SSDiscMiscFct{

  public static void shareDiscWithUser(
    final SSDiscSQLFct    sqlFct,
    final SSUri           user,
    final SSUri           forUser,
    final SSUri           disc,
    final SSUri           circle) throws Exception{
    
    try{
      
      if(SSObjU.isNull(sqlFct, user, forUser, disc, circle)){
        throw new Exception("pars null");
      }
      
      sqlFct.addDisc(disc, forUser);
      
      addDiscWithContentToCircle(
        sqlFct,
        user,
        disc,
        circle);
      
      SSServCaller.entityUsersToCircleAdd(
        user,
        circle,
        sqlFct.getDiscUserURIs(disc),
        false);
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static void addDiscWithContentToCircle(
    final SSDiscSQLFct    sqlFct,
    final SSUri           user,
    final SSUri           disc,
    final SSUri           circle) throws Exception{

    try{
      SSServCaller.entityEntitiesToCircleAdd(
        user,
        circle,
        getDiscContentURIs(sqlFct, disc),
        false);

    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }
  
  public static List<SSUri> getDiscContentURIs(
    final SSDiscSQLFct sqlFct,
    final SSUri        discUri) throws Exception{

    try{

      final List<SSUri>  discContentUris = new ArrayList<>();
      final SSDisc       disc            = sqlFct.getDiscWithEntries(discUri);

      discContentUris.add(discUri);
      discContentUris.add(disc.entity);
      
      for(SSEntityA entry : disc.entries){
        discContentUris.add(((SSDiscEntry)entry).id);
      }
      
      return discContentUris;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
