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
package at.tugraz.sss.serv.caller;

import at.kc.tugraz.socialserver.service.broadcast.datatypes.SSBroadcast;
import at.kc.tugraz.socialserver.service.broadcast.datatypes.enums.SSBroadcastEnum;
import at.kc.tugraz.ss.like.datatypes.SSLikes;
import at.kc.tugraz.ss.serv.jobs.evernote.datatypes.par.SSEvernoteInfo;
import at.kc.tugraz.ss.serv.ss.auth.datatypes.ret.SSAuthCheckCredRet;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.kc.tugraz.ss.service.coll.datatypes.SSColl;
import at.kc.tugraz.ss.service.search.datatypes.SSSearchLabel;
import at.kc.tugraz.ss.service.search.datatypes.SSSearchOpE;
import at.kc.tugraz.ss.service.search.datatypes.ret.SSSearchRet;
import at.kc.tugraz.ss.service.tag.datatypes.SSTagFrequ;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUE;
import at.kc.tugraz.ss.service.userevent.datatypes.SSUEE;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSFileExtE;
import at.tugraz.sss.serv.SSIDU;
import at.tugraz.sss.serv.SSLabel;
import at.tugraz.sss.serv.SSServOpE;
import at.tugraz.sss.serv.SSMimeTypeE;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSTextComment;
import at.tugraz.sss.serv.SSToolContextE;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSVarNames;
import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.SharedNotebook;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sss.serv.eval.datatypes.SSEvalLogE;

public class SSServCaller {

  /* eval */
  
  public static void evalLog(
    final SSUri          user,
    final SSToolContextE toolContext,
    final SSUri          forUser,
    final SSEvalLogE     type,
    final SSUri          entity,
    final String         content,
    final List<SSUri>    entities,
    final List<SSUri>    users) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,            user);
    opPars.put(SSVarNames.toolContext,     toolContext);
    opPars.put(SSVarNames.forUser,         forUser);
    opPars.put(SSVarNames.type,            type);
    opPars.put(SSVarNames.entity,          entity);
    opPars.put(SSVarNames.content,         content);
    opPars.put(SSVarNames.entities,        entities);
    opPars.put(SSVarNames.users,           users);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evalLog, opPars));
  }
  
  /* like */
  
  public static SSLikes likesUserGet(
    final SSUri user,
    final SSUri forUser,
    final SSUri entity) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,       user);
    opPars.put(SSVarNames.entity,     entity);
    opPars.put(SSVarNames.forUser,    forUser);
    
    return (SSLikes) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.likesUserGet, opPars));
  }
  
  public static SSUri likeUserSet(
    final SSUri   user,
    final SSUri   entity,
    final Integer value) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,      user);
    opPars.put(SSVarNames.entity,    entity);
    opPars.put(SSVarNames.value,     value);
    
    return (SSUri) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.likeUserSet, opPars));
  }
    
  /* friends */
  
  public static List<? extends SSEntity> friendsUserGet(
    final SSUri user) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,                    user);
    
    return (List<? extends SSEntity>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.friendsUserGet, opPars));
  }
  
   /* appStackLayout */
    
  public static SSUri appStackLayoutCreate(
    final SSUri          user,
    final String         uuid,
    final SSUri          app,
    final SSLabel        label,
    final SSTextComment  description) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,                    user);
    opPars.put(SSVarNames.uuid,                    uuid);
    opPars.put(SSVarNames.app,                     app);
    opPars.put(SSVarNames.label,                   label);
    opPars.put(SSVarNames.description,             description);

    return (SSUri) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.appStackLayoutCreate, opPars));
  }
  
  public static List<? extends SSEntity> appStackLayoutsGet(
    final SSUri user) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,      user);
    
    return (List<? extends SSEntity>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.appStackLayoutsGet, opPars));
  }
  
  /* app */
  
  public static SSUri appAdd(
    final SSUri         user,
    final SSLabel       label,
    final SSTextComment descriptionShort,
    final SSTextComment descriptionFunctional,
    final SSTextComment descriptionTechnical,
    final SSTextComment descriptionInstall,
    final SSUri         downloadIOS,
    final SSUri         downloadAndroid,
    final SSUri         fork,
    final List<SSUri>   downloads,
    final List<SSUri>   images,
    final List<SSUri>   videos) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,                    user);
    opPars.put(SSVarNames.label,                   label);
    opPars.put(SSVarNames.descriptionShort,        descriptionShort);
    opPars.put(SSVarNames.descriptionFunctional,   descriptionFunctional);
    opPars.put(SSVarNames.descriptionTechnical,    descriptionTechnical);
    opPars.put(SSVarNames.descriptionInstall,      descriptionInstall);
    opPars.put(SSVarNames.downloadIOS,             downloadIOS);
    opPars.put(SSVarNames.downloadAndroid,         downloadAndroid);
    opPars.put(SSVarNames.fork,                    fork);
    opPars.put(SSVarNames.downloads,               downloads);
    opPars.put(SSVarNames.images,                  images);
    opPars.put(SSVarNames.videos,                  videos);
    
    return (SSUri) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.appAdd, opPars));
  }
  
  public static List<? extends SSEntity> appsGet(
    final SSUri user) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,      user);
    
    return (List<? extends SSEntity>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.appsGet, opPars));
  }
   
   /* comment */
  
  public static List<SSUri> commentEntitiesCommentedGet(
    final SSUri user,
    final SSUri forUser) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,      user);
    opPars.put(SSVarNames.forUser,   forUser);
    
    return (List<SSUri>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.commentEntitiesCommentedGet, opPars));
  }
  
  public static List<SSTextComment> commentsGet(
    final SSUri user,
    final SSUri forUser,
    final SSUri entity) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,      user);
    opPars.put(SSVarNames.forUser,   forUser);
    opPars.put(SSVarNames.entity,    entity);
    
    return (List<SSTextComment>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.commentsGet, opPars));
  }
  
  public static List<SSTextComment> commentsUserGet(
    final SSUri user,
    final SSUri forUser,
    final SSUri entity) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,      user);
    opPars.put(SSVarNames.forUser,   forUser);
    opPars.put(SSVarNames.entity,    entity);
    
    return (List<SSTextComment>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.commentsUserGet, opPars));
  }
  
  /* flag */
  
  public static Integer uECountGet(
    final SSUri    user,
    final SSUri    forUser,
    final SSUri    entity,
    final SSUEE    type,
    final Long     startTime,
    final Long     endTime) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,      user);
    opPars.put(SSVarNames.forUser,   forUser);
    opPars.put(SSVarNames.entity,    entity);
    opPars.put(SSVarNames.type,      type);
    opPars.put(SSVarNames.startTime, startTime);
    opPars.put(SSVarNames.endTime,   endTime);
    
    return (Integer) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.uECountGet, opPars));
  }
  
  public static List<SSUE> uEsGet(
    final SSUri        user,
    final SSUri        forUser,
    final SSUri        entity,
    final List<SSUEE>  types,
    final Long         startTime,
    final Long         endTime) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,      user);
    opPars.put(SSVarNames.forUser,   forUser);
    opPars.put(SSVarNames.entity,    entity);
    opPars.put(SSVarNames.types,      types);
    opPars.put(SSVarNames.startTime, startTime);
    opPars.put(SSVarNames.endTime,   endTime);
    
    return (List<SSUE>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.uEsGet, opPars));
  }
  
  /* evernote */ 
  
  public static Resource evernoteResourceByHashGet(
    final SSUri           user,
    final NoteStoreClient noteStore,
    final String          noteGUID, 
    final String          resourceHash) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,           user);
    opPars.put(SSVarNames.noteStore,      noteStore);
    opPars.put(SSVarNames.noteGUID,       noteGUID);
    opPars.put(SSVarNames.resourceHash,   resourceHash);
    
    return (Resource) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteResourceByHashGet, opPars));
  }
    
  public static Boolean evernoteResourceAdd(
    final SSUri    user,
    final SSUri    note, 
    final SSUri    resource,
    final Boolean  shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,           user);
    opPars.put(SSVarNames.note,           note);
    opPars.put(SSVarNames.resource,       resource);
    opPars.put(SSVarNames.shouldCommit,   shouldCommit);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteResourceAdd, opPars));
  }
  
  public static Boolean evernoteUSNSet(
    final SSUri    user,
    final String   authToken, 
    final Integer  usn,
    final Boolean  shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,           user);
    opPars.put(SSVarNames.authToken,      authToken);
    opPars.put(SSVarNames.usn,            usn);
    opPars.put(SSVarNames.shouldCommit,   shouldCommit);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteUSNSet, opPars));
  }
    
  public static Boolean evernoteNoteAdd(
    final SSUri    user,
    final SSUri    notebook, 
    final SSUri    note,
    final Boolean  shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,           user);
    opPars.put(SSVarNames.notebook,       notebook);
    opPars.put(SSVarNames.note,           note);
    opPars.put(SSVarNames.shouldCommit,   shouldCommit);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteNoteAdd, opPars));
  }
    
  public static String evernoteUsersAuthTokenGet(
    final SSUri    user) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,           user);
    
    return (String) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteUsersAuthTokenGet, opPars));
  }
  
  public static Boolean evernoteUserAdd(
    final SSUri    user, 
    final String   authToken,
    final Boolean  shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,           user);
    opPars.put(SSVarNames.authToken,      authToken);
    opPars.put(SSVarNames.shouldCommit,   shouldCommit);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteUserAdd, opPars));
  }
  
  public static Resource evernoteResourceGet(
    final NoteStoreClient noteStore, 
    final String          resourceGUID,
    final Boolean         includeContent) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.noteStore,        noteStore);
    opPars.put(SSVarNames.resourceGUID,     resourceGUID);
    opPars.put(SSVarNames.includeContent,   includeContent);
    
    return (Resource) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteResourceGet, opPars));
  }
  
  public static List<SharedNotebook> evernoteNotebooksSharedGet(
    final NoteStoreClient noteStore) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();

    opPars.put(SSVarNames.noteStore,     noteStore);
      
    return (List<SharedNotebook>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteNotebooksSharedGet, opPars));
  }
  
   public static Notebook evernoteNotebookGet(
    final NoteStoreClient noteStore,
    final String          notebookGUID) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.noteStore,     noteStore);
    opPars.put(SSVarNames.notebookGUID,  notebookGUID);
    
    return (Notebook) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteNotebookGet, opPars));
  }
  
  public static Note evernoteNoteGet(
    final NoteStoreClient noteStore,
    final String          noteGUID,
    final Boolean         includeContent) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.noteStore,       noteStore);
    opPars.put(SSVarNames.noteGUID,        noteGUID);
    opPars.put(SSVarNames.includeContent,  includeContent);
    
    return (Note) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteNoteGet, opPars));
  }
  
  public static SSEvernoteInfo evernoteNoteStoreGet(
    final SSUri  user, 
    final String authToken) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,      user);
    opPars.put(SSVarNames.authToken, authToken);
    
    return (SSEvernoteInfo) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteNoteStoreGet, opPars));
  }
  
   public static List<String> evernoteNoteTagNamesGet(
    final NoteStoreClient noteStore, 
    final String          noteGUID) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.noteStore,      noteStore);
    opPars.put(SSVarNames.noteGUID,       noteGUID);
    
    return (List<String>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.evernoteNoteTagNamesGet, opPars));
  }
   
  public static Boolean broadcastAdd(
    final SSUri           user,
    final SSUri           entity,
    final SSBroadcastEnum type,
    final Object          content) throws Exception{
    
    final  Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,         user);
    opPars.put(SSVarNames.entity,       entity);
    opPars.put(SSVarNames.type,         type);
    opPars.put(SSVarNames.content,      content);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.broadcastAdd, opPars));
  }
  
  public static void broadcastUpdate() throws Exception{
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.broadcastUpdate, new HashMap<>()));
  } 
  
  public static List<SSBroadcast> broadcastsGet(
    final SSUri           user) throws Exception{
    
    final  Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,         user);
    
    return (List<SSBroadcast>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.broadcastsGet, opPars));
  }

  private static SSUri vocURIPrefixGet() throws Exception{
    return (SSUri) SSUri.get(SSVocConf.sssUri);
  }
  
  public static SSUri vocURICreate() throws Exception{
    return SSUri.get(vocURIPrefixGet() + SSIDU.uniqueID());
  }
  
  public static SSUri vocURICreateFromId(final String id) throws Exception{
    return SSUri.get(vocURIPrefixGet() + id);
  }
  
  public static SSUri vocURICreate(final SSFileExtE fileExt) throws Exception{
    return SSUri.get(vocURIPrefixGet() + SSIDU.uniqueID() + SSStrU.dot + fileExt.toString());
  }
  
  /* colls */
    
  public static SSColl collUserWithEntries(
    final SSUri user, 
    final SSUri coll) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user, user);
    opPars.put(SSVarNames.coll, coll);
    
    return (SSColl) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.collUserWithEntries, opPars));
  }
  
  public static SSColl collUserRootGet(final SSUri user) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user, user);
    
    return (SSColl) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.collUserRootGet, opPars));
  }
  
  public static List<SSColl> collsUserWithEntries(final SSUri user) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user, user);
    
    return (List<SSColl>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.collsUserWithEntries, opPars));
  }
  
  public static Boolean collUserEntryDelete(
    final SSUri        user, 
    final SSUri        entry, 
    final SSUri        parentColl, 
    final Boolean      shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.shouldCommit, shouldCommit);
    opPars.put(SSVarNames.user,         user);
    opPars.put(SSVarNames.coll,         parentColl);
    opPars.put(SSVarNames.entry,        entry);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.collUserEntryDelete, opPars));
  }
  
  public static SSUri collUserEntryAdd(
    final SSUri        user,
    final SSUri        coll,
    final SSUri        entry,
    final SSLabel      label,
    final Boolean      addNewColl,
    final Boolean      shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,              user);
    opPars.put(SSVarNames.shouldCommit,      shouldCommit);
    opPars.put(SSVarNames.coll,              coll);
    opPars.put(SSVarNames.entry,             entry);
    opPars.put(SSVarNames.label,             label);
    opPars.put(SSVarNames.addNewColl,        addNewColl);
    
    return (SSUri) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.collUserEntryAdd, opPars));
  }
  
  public static Boolean collUserEntriesAdd(
    final SSUri             user,
    final SSUri             coll,
    final List<SSUri>       entries,
    final List<SSLabel>     labels,
    final Boolean           saveUE,
    final Boolean           shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.shouldCommit, shouldCommit);
    opPars.put(SSVarNames.saveUE,       saveUE);
    opPars.put(SSVarNames.user,         user);
    opPars.put(SSVarNames.coll,         coll);
    opPars.put(SSVarNames.entries,      entries);
    opPars.put(SSVarNames.labels,       labels);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.collUserEntriesAdd, opPars));
  }
  
  public static void collUserRootAdd(
    final SSUri   user, 
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.shouldCommit, shouldCommit);
    opPars.put(SSVarNames.user,         user);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.collUserRootAdd, opPars));
  }
  
  public static List<SSColl> collUserHierarchyGet(
    final SSUri user, 
    final SSUri coll) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,      user);
    opPars.put(SSVarNames.coll,      coll);
    
    return (List<SSColl>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.collUserHierarchyGet, opPars));
  }
  
  public static List<SSTagFrequ> collUserCumulatedTagsGet(
    final SSUri user, 
    final SSUri coll) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,         user);
    opPars.put(SSVarNames.coll,      coll);
    
    return (List<SSTagFrequ>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.collUserCumulatedTagsGet, opPars));
  }
  
  public static List<SSColl> collsUserEntityIsInGet(
    final SSUri user,
    final SSUri entity) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,         user);
    opPars.put(SSVarNames.entity,    entity);
    
    return (List<SSColl>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.collsUserEntityIsInGet, opPars));
  }
  
  public static Boolean collUserEntriesDelete(
    final SSUri       user,
    final SSUri       coll,
    final List<SSUri> entries,
    final Boolean     shouldCommit) throws Exception{
      
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.shouldCommit, shouldCommit);
    opPars.put(SSVarNames.user,         user);
    opPars.put(SSVarNames.coll,         coll);
    opPars.put(SSVarNames.entries,  entries);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.collUserEntriesDelete, opPars));
  }
  
  public static Boolean collsUserCouldSubscribeGet(
    final SSUri       user) throws Exception{
      
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,         user);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.collsUserCouldSubscribeGet, opPars));
  }

  /* search */
  
  public static void searchResultPagesCacheClean() throws Exception{
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.searchResultPagesCacheClean, new HashMap<>()));
  }
  
  public static SSSearchRet search(
    final SSUri                user, 
    final Boolean              includeTextualContent,
    final List<String>         wordsToSearchFor,
    final Boolean              includeTags,
    final List<String>         tagsToSearchFor,
    final Boolean              includeAuthors,
    final List<SSUri>          authorsToSearchFor,
    final Boolean              includeLabel,
    final List<String>         labelsToSearchFor,
    final Boolean              includeDescription,
    final List<String>         descriptionsToSearchFor,
    final List<SSEntityE>      typesToSearchOnlyFor,
    final Boolean              includeOnlySubEntities,
    final List<SSUri>          entitiesToSearchWithin,
    final Boolean              extendToParents, 
    final Boolean              includeRecommendedResults,
    final Boolean              provideEntries,
    final String               pagesID,
    final Integer              pageNumber,
    final Integer              minRating,
    final Integer              maxRating,
    final SSSearchOpE          localSearchOp,
    final SSSearchOpE          globalSearchOp) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,                      user);
    opPars.put(SSVarNames.includeTextualContent,     includeTextualContent);
    opPars.put(SSVarNames.wordsToSearchFor,          wordsToSearchFor);
    opPars.put(SSVarNames.includeTags,               includeTags);
    opPars.put(SSVarNames.tagsToSearchFor,           tagsToSearchFor);
    opPars.put(SSVarNames.includeAuthors,            includeAuthors);
    opPars.put(SSVarNames.authorsToSearchFor,        authorsToSearchFor);
    opPars.put(SSVarNames.includeLabel,              includeLabel);
    opPars.put(SSVarNames.labelsToSearchFor,         SSSearchLabel.get(labelsToSearchFor));
    opPars.put(SSVarNames.includeDescription,        includeDescription);
    opPars.put(SSVarNames.descriptionsToSearchFor,   SSSearchLabel.get(descriptionsToSearchFor));
    opPars.put(SSVarNames.typesToSearchOnlyFor,      typesToSearchOnlyFor);
    opPars.put(SSVarNames.includeOnlySubEntities,    includeOnlySubEntities);
    opPars.put(SSVarNames.entitiesToSearchWithin,    entitiesToSearchWithin);
    opPars.put(SSVarNames.extendToParents,           extendToParents);
    opPars.put(SSVarNames.includeRecommendedResults, includeRecommendedResults);
    opPars.put(SSVarNames.provideEntries,            provideEntries);
    opPars.put(SSVarNames.pagesID,                   pagesID);
    opPars.put(SSVarNames.pageNumber,                pageNumber);
    opPars.put(SSVarNames.minRating,                 minRating);
    opPars.put(SSVarNames.maxRating,                 maxRating);
    opPars.put(SSVarNames.localSearchOp,             localSearchOp);
    opPars.put(SSVarNames.globalSearchOp,            globalSearchOp);
    
    return (SSSearchRet) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.search, opPars));
  }
  
  /* solr */

  public static List<String> solrSearch(
    final String       keyword,
    final Integer      maxResults) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.keyword,    keyword);
    opPars.put(SSVarNames.maxResults, maxResults);
    
    return (List<String>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.solrSearch, opPars));
  }
  
  public static void solrAddDoc(
    final SSUri        user,
    final String       fileID,
    final SSMimeTypeE  mimeType,
    final Boolean      shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.shouldCommit,    shouldCommit);
    opPars.put(SSVarNames.user,            user);
    opPars.put(SSVarNames.id,              fileID);
    opPars.put(SSVarNames.mimeType,        mimeType);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.solrAddDoc, opPars));
  }
  
  /* entity */
  
  public static Boolean entityReadGet(
    final SSUri user,
    final SSUri entity) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,   user);
    opPars.put(SSVarNames.entity, entity);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.entityReadGet, opPars));
  }
  
  public static List<SSEntity> entityEntitiesAttachedGet(
    final SSUri user,
    final SSUri entity) throws Exception{
    
    final Map<String, Object>  opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,   user);
    opPars.put(SSVarNames.entity, entity);
    
    return (List<SSEntity>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.entityEntitiesAttachedGet, opPars));
  }
    
  public static List<SSUri> entityFilesGet(
    final SSUri user, 
    final SSUri entity) throws Exception{
   
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,         user);
    opPars.put(SSVarNames.entity,       entity);
    
    return (List<SSUri>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.entityFilesGet, opPars));
  }
  
  public static List<SSUri> entityDownloadURIsGet(
    final SSUri user, 
    final SSUri entity) throws Exception{
   
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,         user);
    opPars.put(SSVarNames.entity,       entity);
    
    return (List<SSUri>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.entityDownloadURIsGet, opPars));
  }
  
  public static List<? extends SSEntity> entityScreenShotsGet(
    final SSUri user, 
    final SSUri entity) throws Exception{
   
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,         user);
    opPars.put(SSVarNames.entity,       entity);
    
    return (List<? extends SSEntity>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.entityScreenShotsGet, opPars));
  }
  
  public static void entityFileAdd(
    final SSUri   user,
    final SSUri   entity,
    final SSUri   file,
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,         user);
    opPars.put(SSVarNames.entity,       entity);
    opPars.put(SSVarNames.file,         file);
    opPars.put(SSVarNames.shouldCommit, shouldCommit);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.entityFileAdd, opPars));
  }
  
  public static List<SSEntity> entitiesForLabelsAndDescriptionsGet(
    final List<String> requireds,
    final List<String> absents,
    final List<String> eithers) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.requireds, requireds);
    opPars.put(SSVarNames.absents,   absents);
    opPars.put(SSVarNames.eithers,   eithers);
    
    return (List<SSEntity>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.entitiesForLabelsAndDescriptionsGet, opPars));
  }
  
  public static List<SSEntity> entitiesForLabelsGet(
    final List<String> requireds,
    final List<String> absents,
    final List<String> eithers) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.requireds, requireds);
    opPars.put(SSVarNames.absents,   absents);
    opPars.put(SSVarNames.eithers,   eithers);
    
    return (List<SSEntity>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.entitiesForLabelsGet, opPars));
  }
  
  public static List<SSEntity> entitiesForDescriptionsGet(
    final List<String> requireds,
    final List<String> absents,
    final List<String> eithers) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.requireds, requireds);
    opPars.put(SSVarNames.absents,   absents);
    opPars.put(SSVarNames.eithers,   eithers);
    
    return (List<SSEntity>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.entitiesForDescriptionsGet, opPars));
  }
  
  public static List<SSUri> entityUserParentEntitiesGet(
    final SSUri      user, 
    final SSUri      entity) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,      user);
    opPars.put(SSVarNames.entity,    entity);
    
    return (List<SSUri>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.entityUserParentEntitiesGet, opPars));
  }
  
  public static List<SSUri> entityUserSubEntitiesGet(
    final SSUri      user, 
    final SSUri      entity) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,      user);
    opPars.put(SSVarNames.entity,    entity);
    
    return (List<SSUri>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.entityUserSubEntitiesGet, opPars));
  }
  
  public static void entityRemove(
    final SSUri   entity, 
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.entity,       entity);
    opPars.put(SSVarNames.shouldCommit, shouldCommit);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.entityRemove, opPars));
  }
  
  /* modeling user event */

  public static List<SSUri> modelUEEntitiesForMiGet(
    final SSUri    user, 
    final String   mi) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user, user);
    opPars.put(SSVarNames.mi,   mi);
    
    return (List<SSUri>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.modelUEEntitiesForMiGet, opPars));
  }
  
  public static void modelUEUpdate() throws Exception{
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.modelUEUpdate, new HashMap<>()));
  }
  
  public static List<String> modelUEMIsForEntityGet(
    final SSUri user,
    final SSUri entity) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,        user);
    opPars.put(SSVarNames.entity,   entity);
    
    return (List<String>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.modelUEMIsForEntityGet, opPars));
  }
  
  /* data export */
  
  public static void dataExportUserRelations(
    final SSUri                     user) throws Exception {
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,                  user);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.dataExportUserRelations, opPars));
  }
  
  public static void dataExportUserEntityTagCategoryTimestamps(
    final SSUri                     user,
    final Boolean                   exportTags,
    final Boolean                   usePrivateTagsToo,
    final Boolean                   exportCategories,
    final String                    fileName) throws Exception {
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.fileName,              fileName);
    opPars.put(SSVarNames.exportTags,            exportTags);
    opPars.put(SSVarNames.usePrivateTagsToo,     usePrivateTagsToo);
    opPars.put(SSVarNames.exportCategories,      exportCategories);
    opPars.put(SSVarNames.user,                  user);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.dataExportUserEntityTagCategoryTimestamps, opPars));
  }
  
  public static void dataExportAddTagsCategoriesTimestampsForUserEntity(
    final SSUri        user,
    final SSUri        forUser,
    final SSUri        entity,
    final List<String> tags,
    final List<String> categories,
    final String       fileName) throws Exception {
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,          user);
    opPars.put(SSVarNames.forUser,       forUser);
    opPars.put(SSVarNames.entity,        entity);
    opPars.put(SSVarNames.tags,          tags);
    opPars.put(SSVarNames.categories,    categories);
    opPars.put(SSVarNames.fileName,      fileName);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.dataExportAddTagsCategoriesTimestampsForUserEntity, opPars));
  }
  
  /* recomm */
  
  public static Map<String, Double> recommTags(
    final SSUri         user,
    final String        realm,
    final SSUri         forUser,
    final SSUri         entity,
    final List<String>  categories,
    final Integer       maxTags,
    final Boolean       includeOwn) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,           user);
    opPars.put(SSVarNames.realm,          realm);
    opPars.put(SSVarNames.forUser,        forUser);
    opPars.put(SSVarNames.entity,         entity);
    opPars.put(SSVarNames.categories,     categories);
    opPars.put(SSVarNames.maxTags,        maxTags);
    opPars.put(SSVarNames.includeOwn,     includeOwn);
    
    return (Map<String, Double>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.recommTags, opPars));
  }
  
//  public static void recommUpdate(
//    final SSUri               user,
//    final String              realm,
//    final SSUri               forUser,
//    final SSUri               entity,
//    final List<String>        tags,
//    final List<String>        categories) throws Exception {
//    
//    final Map<String, Object> opPars = new HashMap<>();
//    
//    opPars.put(SSVarU.user,           user);
//    opPars.put(SSVarU.realm,          realm);
//    opPars.put(SSVarU.forUser,        forUser);
//    opPars.put(SSVarU.entity,         entity);
//    opPars.put(SSVarU.tags,           tags);
//    opPars.put(SSVarU.categories,     categories);
//    
//    SSServA.inst.callServViaServer(new SSServPar(SSServOpE.recommUpdate, opPars));
//  }
  
  public static Map<SSEntity, Double> recommResources(
    final SSUri           user,
    final String          realm,
    final SSUri           forUser,
    final SSUri           entity,
    final List<String>    categories,
    final Integer         maxResources,
    final List<SSEntityE> typesToRecommOnly, 
    final Boolean         setCircleTypes,
    final Boolean         includeOwn) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,                   user);
    opPars.put(SSVarNames.realm,                  realm);
    opPars.put(SSVarNames.forUser,                forUser);
    opPars.put(SSVarNames.entity,                 entity);
    opPars.put(SSVarNames.categories,             categories);
    opPars.put(SSVarNames.maxResources,           maxResources);
    opPars.put(SSVarNames.typesToRecommOnly,      typesToRecommOnly);
    opPars.put(SSVarNames.setCircleTypes,         setCircleTypes);
    opPars.put(SSVarNames.includeOwn,             includeOwn);
    
    return (Map<SSEntity, Double>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.recommResources, opPars));
  }
  
  /* data import */
  
  public static void dataImportAchso(
    final SSUri   user, 
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.shouldCommit,     shouldCommit);
    opPars.put(SSVarNames.user,             user);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.dataImportAchso, opPars));
  }
  
  public static void dataImportUserResourceTagFromWikipedia(
    final SSUri   user, 
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.shouldCommit,     shouldCommit);
    opPars.put(SSVarNames.user,             user);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.dataImportUserResourceTagFromWikipedia, opPars));
  }
  
  public static Map<String, String> dataImportSSSUsersFromCSVFile( 
    final String fileName) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.fileName, fileName);
    
    return (Map<String, String>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.dataImportSSSUsersFromCSVFile, opPars));
  }
  
  public static void dataImportEvernote(
    final SSUri   user, 
    final String  authToken, 
    final String  authEmail,
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.shouldCommit, shouldCommit);
    opPars.put(SSVarNames.user,         user);
    opPars.put(SSVarNames.authToken,    authToken);
    opPars.put(SSVarNames.authEmail,    authEmail);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.dataImportEvernote, opPars));
  }
  
  public static void dataImportMediaWikiUser(
    final SSUri   user,
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,        user);
    opPars.put(SSVarNames.shouldCommit, shouldCommit);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.dataImportMediaWikiUser, opPars));
  }
  
  /* auth */
  
  public static SSUri authRegisterUser(
    final SSUri   user,
    final SSLabel label,
    final String  email, 
    final String  password,
    final Boolean isSystemUser,
    final Boolean updatePassword,
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,              user);
    opPars.put(SSVarNames.label,             label);
    opPars.put(SSVarNames.email,             email);
    opPars.put(SSVarNames.password,          password);
    opPars.put(SSVarNames.isSystemUser,      isSystemUser);
    opPars.put(SSVarNames.updatePassword,    updatePassword);
    opPars.put(SSVarNames.shouldCommit,      shouldCommit);
    
    return (SSUri) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.authRegisterUser, opPars));
  }
   
  public static void authUsersFromCSVFileAdd(
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.shouldCommit,      shouldCommit);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.authUsersFromCSVFileAdd, opPars));
  }
    
  public static SSUri checkKey(final SSServPar par) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.key, par.key);
    
    return (SSUri) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.authCheckKey, opPars));
  }
  
  public static SSAuthCheckCredRet authCheckCred(
    final SSUri  user,
    final String key) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user, user);
    opPars.put(SSVarNames.key, key);
    
    return (SSAuthCheckCredRet) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.authCheckCred, opPars));
  }
  
  /* i5Cloud */
  
  public static List<String> i5CloudAchsoSemanticAnnotationsSetGet(
    final List<String> ids) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.ids,   ids);
    
    return (List<String>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.i5CloudAchsoSemanticAnnotationsSetGet, opPars));
  }
    
  public static String i5CloudAchsoVideoInformationGet() throws Exception{
    return (String) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.i5CloudAchsoVideoInformationGet, new HashMap<>()));
  }
    
  public static Map<String, String> i5CloudAuth() throws Exception{
    return (Map<String, String>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.i5CloudAuth, new HashMap<>()));
  }
  
  public static Boolean i5CloudFileUpload(
    final String fileName,
    final String containerSpace,
    final String authToken) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.fileName,   fileName);
    opPars.put(SSVarNames.space,      containerSpace);
    opPars.put(SSVarNames.authToken,  authToken);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.i5CloudFileUpload, opPars));
  }
  
  public static Boolean i5CloudFileDownload(
    final String fileName,
    final String containerSpace,
    final String authToken) throws Exception{
    
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.fileName,   fileName);
    opPars.put(SSVarNames.space,      containerSpace);
    opPars.put(SSVarNames.authToken,  authToken);
    
    return (Boolean) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.i5CloudFileDownload, opPars));
  } 

  /* video */
  
  public static List<? extends SSEntity> videosUserGet(
    final SSUri user, 
    final SSUri forUser,
    final SSUri forEntity) throws Exception{
   
    final Map<String, Object> opPars = new HashMap<>();
    
    opPars.put(SSVarNames.user,         user);
    opPars.put(SSVarNames.forUser,      forUser);
    opPars.put(SSVarNames.forEntity,    forEntity);
    
    return (List<? extends SSEntity>) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.videosUserGet, opPars));
  }
  
  public static SSEntity videoUserGet(
    final SSUri   user,
    final SSUri video) throws Exception{
    
    final Map<String, Object>  opPars           = new HashMap<>();
    
    opPars.put(SSVarNames.user,             user);
    opPars.put(SSVarNames.video,            video);
    
    return (SSEntity) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.videoUserGet, opPars));
  }
  
  public static SSUri videoUserAdd(
    final SSUri   user,
    final SSUri   link,
    final SSUri   forEntity,
    final String  uuid,
    final Boolean shouldCommit) throws Exception{
    
    final Map<String, Object>  opPars           = new HashMap<>();
    
    opPars.put(SSVarNames.shouldCommit,     shouldCommit);
    opPars.put(SSVarNames.user,             user);
    opPars.put(SSVarNames.link,             link);
    opPars.put(SSVarNames.uuid,             uuid);
    opPars.put(SSVarNames.forEntity,        forEntity);
    
    return (SSUri) SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.videoUserAdd, opPars));
  }

  public static void recommLoadUserRealms(
    final SSUri user) throws Exception{
    
    final Map<String, Object>  opPars           = new HashMap<>();
    
    opPars.put(SSVarNames.user,             user);
    
    SSServReg.inst.callServViaServer(new SSServPar(SSServOpE.recommLoadUserRealms, opPars));
  }
}