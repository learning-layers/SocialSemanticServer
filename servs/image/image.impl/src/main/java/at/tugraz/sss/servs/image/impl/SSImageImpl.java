/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2015, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.tugraz.sss.servs.image.impl;

import at.kc.tugraz.ss.circle.api.SSCircleServerI;
import at.kc.tugraz.ss.circle.datatypes.par.SSCircleEntitiesAddPar;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.conf.conf.SSVocConf;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.service.filerepo.api.SSFileRepoServerI;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCircleI;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.serv.SSClientE;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServPar; import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntity;
import at.tugraz.sss.serv.SSEntityContext;
import at.tugraz.sss.serv.SSEntityDescriberPar;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.util.SSServCallerU;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.SSErrE;
import at.tugraz.sss.serv.SSFileExtE;
import at.tugraz.sss.serv.SSFileU;
import at.tugraz.sss.serv.SSImage;
import at.tugraz.sss.serv.SSImageE;
import at.tugraz.sss.serv.SSLogU;
import at.tugraz.sss.serv.SSObjU;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSServRetI; import at.tugraz.sss.serv.SSVarNames;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.SSUsersResourcesGathererI;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityRemovePar;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFileAddPar;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFilesGetPar;
import at.tugraz.sss.servs.image.api.SSImageClientI;
import at.tugraz.sss.servs.image.api.SSImageServerI;
import at.tugraz.sss.servs.image.conf.SSImageConf;
import at.tugraz.sss.servs.image.datatype.par.SSImageProfilePictureSetPar;
import at.tugraz.sss.servs.image.datatype.par.SSImageAddPar;
import at.tugraz.sss.servs.image.datatype.par.SSImageGetPar;
import at.tugraz.sss.servs.image.datatype.par.SSImagesGetPar;
import at.tugraz.sss.servs.image.datatype.ret.SSImageAddRet;
import at.tugraz.sss.servs.image.datatype.ret.SSImageProfilePictureSetRet;
import at.tugraz.sss.servs.image.datatype.ret.SSImagesGetRet;
import at.tugraz.sss.servs.image.impl.sql.SSImageSQLFct;
import java.io.File;
import java.util.Map;
import javax.imageio.ImageIO;

public class SSImageImpl 
extends SSServImplWithDBA 
implements 
  SSImageClientI, 
  SSImageServerI,
  SSDescribeEntityI,
  SSAddAffiliatedEntitiesToCircleI, 
  SSUsersResourcesGathererI{

  private final SSImageSQLFct         sql;
  private final SSEntityServerI       entityServ;
  
  public SSImageImpl(final SSConfA conf) throws SSErr {
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
    this.sql            = new SSImageSQLFct   (dbSQL, SSVocConf.systemUserUri);
    this.entityServ     = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
  }
  
  @Override
  public void getUsersResources(
    final Map<String, List<SSEntityContext>> usersEntities) throws Exception{
    
    try{
      
      final SSImagesGetPar imagesGetPar =
        new SSImagesGetPar(
          null, //user
          null, //entity,
          null, //imageType
          true); //withUserRestriction
      
      SSUri userID;
      
      for(String user : usersEntities.keySet()){
        
        userID = SSUri.get(user);
        
        imagesGetPar.user = userID;
        
        for(SSEntity image : imagesGet(imagesGetPar)){
          
          usersEntities.get(user).add(
            new SSEntityContext(
              image.id,
              SSEntityE.image,
              null,
              null));
        }
      }
      
    }catch(Exception error){
      SSLogU.err(error);
      SSServErrReg.reset();
    }
  }
  
  @Override
  public SSEntity describeEntity(
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws Exception{
    
    try{
      
      if(par.setThumb){
        
        for(SSEntity thumb :
          imagesGet(
            new SSImagesGetPar(
              par.user,
              entity.id,
              SSImageE.thumb,
              par.withUserRestriction))){
          
          entity.thumb = thumb;
          break;
        }
      }
      
      if(par.setProfilePicture){
        
        for(SSUri profilePicture : sql.getProfilePictures(entity.id)){
          
          entity.profilePicture =
            imageGet(
              new SSImageGetPar(
                par.user,
                profilePicture,
                par.withUserRestriction));
          
          break;
        }
      }
      
      switch(entity.type){
        
        case image:{
          
          if(SSStrU.equals(entity, par.recursiveEntity)){
            break;
          }
          
          return SSImage.get(
            imageGet(
              new SSImageGetPar(
                par.user,
                entity.id,
                par.withUserRestriction)),
            entity);
        }
      }
      
      return entity;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  @Override
  public List<SSEntity> addAffiliatedEntitiesToCircle(final SSAddAffiliatedEntitiesToCirclePar par) throws Exception{
    
    try{
      final SSCircleServerI circleServ         = (SSCircleServerI) SSServReg.getServ(SSCircleServerI.class);
      final List<SSEntity>  affiliatedEntities = new ArrayList<>();
      
      for(SSEntity entityAdded : par.entities){
        
        for(SSEntity image :
          imagesGet(
            new SSImagesGetPar(
              par.user, 
              entityAdded.id, 
              null, 
              par.withUserRestriction))){
          
          if(SSStrU.contains(par.recursiveEntities, image.id)){
            continue;
          }
          
          SSEntity.addEntitiesDistinctWithoutNull(
            affiliatedEntities,
            image);
        }
      }
      
      if(affiliatedEntities.isEmpty()){
        return affiliatedEntities;
      }
      
      circleServ.circleEntitiesAdd(
        new SSCircleEntitiesAddPar(
          par.user,
          par.circle,
          SSUri.getDistinctNotNullFromEntities(affiliatedEntities), //entities
          false, //withUserRestriction
          false)); //shouldCommit
      
      SSEntity.addEntitiesDistinctWithoutNull(
        affiliatedEntities, 
        SSServCallerU.handleAddAffiliatedEntitiesToCircle(
          par.user, 
          par.circle, 
          affiliatedEntities,  //entities
          par.recursiveEntities,
          par.withUserRestriction));
        
      return affiliatedEntities;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSImage imageGet(final SSImageGetPar par) throws Exception{

    try{
      
      SSEntity image = sql.getEntityTest(par.user, par.image, par.withUserRestriction);
      
      if(image == null){
        return null;
      }
      
      final SSFileRepoServerI fileServ = (SSFileRepoServerI) SSServReg.getServ(SSFileRepoServerI.class);
      
      image = sql.getImage(par.image);
      
      for(SSEntity file :
        fileServ.filesGet(
          new SSEntityFilesGetPar(
            par.user,
            par.image,
            par.withUserRestriction,
            false))){ //invokeEntityHandlers
        
        image.file = file;
        break;
      }
      
      return (SSImage) image;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI imagesGet(SSClientE clientType, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSImagesGetPar par = (SSImagesGetPar) parA.getFromJSON(SSImagesGetPar.class);
      
    return SSImagesGetRet.get(imagesGet(par));
  }

  @Override
  public List<SSEntity> imagesGet(final SSImagesGetPar par) throws Exception{
    
    try{
      
      if(par.withUserRestriction){
        
        if(par.entity != null){
          
          final SSEntity enity = sql.getEntityTest(par.user, par.entity, par.withUserRestriction);
            
          if(enity == null){
            return new ArrayList<>();
          }
        }
      }
      
      final List<SSUri>    imageURIs = sql.getImages(par.entity, par.imageType);
      final List<SSEntity> images    = new ArrayList<>();
      final SSImageGetPar  imageGetPar =
        new SSImageGetPar(
          par.user,
          null, //image
          par.withUserRestriction);
        
      for(SSUri imageURI : imageURIs){
        
        imageGetPar.image = imageURI;
        
        SSEntity.addEntitiesDistinctWithoutNull(
          images, 
          imageGet(imageGetPar));
      }
      
      return images;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSImageAddRet imageAdd(final SSImageAddPar par) throws Exception{

    try{
      
      if(par.imageType == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(
        par.createThumb &&
        par.file == null){
        
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final SSFileRepoServerI fileServ = (SSFileRepoServerI) SSServReg.getServ(SSFileRepoServerI.class);
      SSUri                   thumbURI = null;
      SSUri                   imageUri;

      if(par.uuid != null){
        imageUri = SSVocConf.vocURICreateFromId(par.uuid);
      }else{
        
        if(par.link != null){
          imageUri = par.link;
        }else{
          imageUri = SSVocConf.vocURICreate();
        }
      }
      
      SSUri thumbFileURI = null;
      
      if(par.createThumb){
        
        try{
          
          thumbFileURI =
            createThumbnail(
              par.file,
              500);
          
        }catch(Exception error){
          SSServErrReg.reset();
          SSLogU.warn("thumb creation failed");
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      if(par.createThumb){
        
        if(
          par.removeThumbsFromEntity &&
          par.entity != null){
          
          for(SSEntity thumb :
            imagesGet(
              new SSImagesGetPar(
                par.user,
                par.entity,
                SSImageE.thumb,
                par.withUserRestriction))){
            
            entityServ.entityRemove(new SSEntityRemovePar(par.user, thumb.id));
            
            try{
              SSFileU.delFile(SSImageConf.getLocalWorkPath() + SSVocConf.fileIDFromSSSURI(thumb.file.id));
            }catch(Exception error){
              SSLogU.warn("couldnt remove thumbnail files from filesys");
            }
          }
        }
      }
      
      final SSUri image =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            imageUri,  //entity
            SSEntityE.image,  //type
            null, //label,
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic,
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      
      if(image == null){
        dbSQL.rollBack(par.shouldCommit);
        return new SSImageAddRet(null, null);
      }
      
      sql.addImage(
        imageUri, 
        par.imageType,
        par.link);
      
      if(par.entity != null){
        
        final SSUri entity =
          entityServ.entityUpdate(
            new SSEntityUpdatePar(
              par.user,
              par.entity,  //entity
              null,  //type
              null, //label,
              null, //description,
              null, //creationTime,
              null, //read,
              false, //setPublic,
              false, //createIfNotExists
              par.withUserRestriction, //withUserRestriction
              false)); //shouldCommit)
        
        if(entity != null){
          sql.addImageToEntity(imageUri, par.entity);
        }
      }
      
      if(
        par.createThumb &&
        thumbFileURI != null){
        
        fileServ.fileAdd(
          new SSEntityFileAddPar(
            par.user,
            null, //fileBytes
            null, //fileLength
            null, //fileExt
            thumbFileURI, //file
            null, //type
            null, //label
            imageUri, //entity
            false, //createThumb
            null, //entityToAddThumbTo
            false, //removeExistingFilesForEntity
            par.withUserRestriction,
            par.shouldCommit));
      }
      
      if(
        par.file != null &&
        !par.isImageToAddTheThumb){
        
        fileServ.fileAdd(
          new SSEntityFileAddPar(
            par.user,
            null, //fileBytes
            null, //fileLength
            null, //fileExt
            par.file, //file
            null, //type
            null, //label
            imageUri, //entity
            false, //createThumb
            null, //entityToAddThumbTo
            false, //removeExistingFilesForEntity
            par.withUserRestriction,
            par.shouldCommit));
      }
      
      dbSQL.commit(par.shouldCommit);

      return new SSImageAddRet(imageUri, thumbURI);
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return imageAdd(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI imageProfilePictureSet(SSClientE clientType, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);

    final SSImageProfilePictureSetPar par = (SSImageProfilePictureSetPar) parA.getFromJSON(SSImageProfilePictureSetPar.class);
    
    return new SSImageProfilePictureSetRet(imageProfilePictureSet(par));
  }
  
  @Override 
  public SSUri imageProfilePictureSet(final SSImageProfilePictureSetPar par) throws Exception{
    
    try{
      
      if(SSObjU.isNull(par.file, par.entity)){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      SSEntity file = 
        sql.getEntityTest(
          par.user, 
          par.file,   
          par.withUserRestriction);
      
      final SSEntity entity = 
        sql.getEntityTest(
          par.user, 
          par.entity, 
          par.withUserRestriction);
      
      if(SSObjU.isNull(file, entity)){
        return null;
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
      final SSUri entityURI =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            par.file, //entity
            null,  //type
            null, //label,
            null, //description,
            null, //creationTime,
            null, //read,
            true, //setPublic,
            false, //createIfNotExists
            par.withUserRestriction,
            false));
      
      if(entityURI == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      SSUri profilePicture =
        imageAdd(
          new SSImageAddPar(
            par.user,
            null, //uuid
            null, //link
            SSImageE.image, //imageType
            par.entity, //entity
            par.file, //file
            false, //createThumb
            false, //isImageToAddTheThumb
            false, //removeThumbsFromEntity
            par.withUserRestriction,
            false)).image;
      
      if(profilePicture == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
          
      profilePicture =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par.user,
            profilePicture, //entity
            null,  //type
            null, //label,
            null, //description,
            null, //creationTime,
            null, //read,
            true, //setPublic,
            false, //createIfNotExists
            par.withUserRestriction,
            false));
          
      if(profilePicture == null){
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      sql.removeProfilePictures (par.entity);
      sql.addProfilePicture     (par.entity, profilePicture);
      
      removeThumbsFromEntity(
        par.user,
        par.entity,
        par.withUserRestriction);
      
      if(!addThumbFromFileToEntity(
        par.user,
        par.entity,
        par.file,
        par.withUserRestriction)){
        
        dbSQL.rollBack(par.shouldCommit);
        return null;
      }
      
      dbSQL.commit(par.shouldCommit);
      
      return par.entity;
        
    }catch(Exception error){
      
      if(SSServErrReg.containsErr(SSErrE.sqlDeadLock)){
        
        if(dbSQL.rollBack(par.shouldCommit)){
          
          SSServErrReg.reset();
          
          return imageProfilePictureSet(par);
        }else{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
      dbSQL.rollBack(par.shouldCommit);
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  private void removeThumbsFromEntity(
    final SSUri   user,
    final SSUri   entity,
    final Boolean withUserRestriction) throws Exception{
    
    try{
      
      final List<SSEntity> existingThumbs =
        imagesGet(
          new SSImagesGetPar(
            user,
            entity, //entity
            SSImageE.thumb,
            withUserRestriction));
      
      sql.removeImagesFromEntity(SSUri.getDistinctNotNullFromEntities(existingThumbs));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  private Boolean addThumbFromFileToEntity(
    final SSUri   user,
    final SSUri   entity,
    final SSUri   file,
    final Boolean withUserRestriction) throws Exception{
    
    try{
      //TODO refactor public setting: shall be done with hooks for entityPublicSet in respective entity type service implementations
      Boolean worked = false;
      SSUri   thumbForEntity;
      SSUri   thumbURI;
      SSUri   thumbFileURI;
      
      for(SSEntity fileThumb :
        imagesGet(
          new SSImagesGetPar(
            user,
            file,
            SSImageE.thumb,
            withUserRestriction))){
        
        if(fileThumb.file == null){
          continue;
        }
        
        thumbForEntity =
          imageAdd(
            new SSImageAddPar(
              user,
              null, //uuid
              null, //link
              SSImageE.thumb,
              entity, //entity
              fileThumb.file.id, //file
              false, //createThumb
              false, //isImageToAddTheThumb
              false, //removeThumbsFromEntity
              withUserRestriction,
              false)).image;
        
        if(thumbForEntity == null){
          continue;
        }
        
        thumbURI =
          entityServ.entityUpdate(
            new SSEntityUpdatePar(
              user,
              thumbForEntity, //entity
              null,  //type
              null, //label,
              null, //description,
              null, //creationTime,
              null, //read,
              true, //setPublic,
              false, //createIfNotExists
              withUserRestriction,
              false));
        
        if(thumbURI == null){
          continue;
        }
        
        thumbFileURI =
          entityServ.entityUpdate(
            new SSEntityUpdatePar(
              user,
              fileThumb.file.id, //entity
              null,  //type
              null, //label,
              null, //description,
              null, //creationTime,
              null, //read,
              true, //setPublic,
              false, //createIfNotExists
              withUserRestriction,
              false));
        
        if(thumbFileURI == null){
          continue;
        }
        
        worked = true;
        break;
      }
      
      return worked;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  private SSUri createThumbnail(
    final SSUri      fileURI,
    final Integer    width) throws Exception{
    
    try{
    
      final String      filePath          = SSImageConf.getLocalWorkPath() + SSVocConf.fileIDFromSSSURI(fileURI);
      final SSFileExtE  fileExt           = SSFileExtE.getFromStrToFormat(SSVocConf.fileIDFromSSSURI(fileURI));
      final SSUri       thumbFileURI      = SSVocConf.vocURICreate(SSFileExtE.png);
      final String      thumbnailPath     = SSImageConf.getLocalWorkPath() + SSVocConf.fileIDFromSSSURI(thumbFileURI);

      if(SSStrU.contains(SSFileExtE.imageFileExts, fileExt)){
        SSFileU.scalePNGAndWrite(ImageIO.read(new File(filePath)), thumbnailPath, width, width);
        return thumbFileURI;
      }

      switch(fileExt){
        
        case txt:{
          
          try{
            
            final String pdfFilePath = SSImageConf.getLocalWorkPath() + SSVocConf.fileIDFromSSSURI(SSVocConf.vocURICreate(SSFileExtE.pdf));
            
            SSFileU.writePDFFromText(
              pdfFilePath,
              filePath);
            
             SSFileU.writeScaledPNGFromPDF(
               pdfFilePath, 
               thumbnailPath, 
               width, 
               width, 
               false);
             
            return thumbFileURI;
            
          }catch(Exception error){
            SSLogU.warn("thumb creation from txt failed");
            throw error;
          }
        }
        
        case pdf:{
          
          SSFileU.writeScaledPNGFromPDF(filePath, thumbnailPath, width, width, false);
          return thumbFileURI;
        }
        
        case doc:{
          
          final String pdfFilePath  = SSImageConf.getLocalWorkPath() + SSVocConf.fileIDFromSSSURI(SSVocConf.vocURICreate(SSFileExtE.pdf));
          
          SSFileU.writePDFFromDoc       (filePath,    pdfFilePath);
          SSFileU.writeScaledPNGFromPDF (pdfFilePath, thumbnailPath, width, width, false);
          return thumbFileURI;
        }
      }
      
      SSLogU.info("thumb creation for file ext not supported: " + fileExt);
      return null;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
}
//@Override
//  public String imageBase64Get(final SSImageBase64GetPar par) throws Exception{
//
//    try{
//      final List<SSEntity> images =
//        imagesGet(
//          new SSImagesGetPar(
//            par.user,
//            par.entity,
//            par.imageType,
//            par.withUserRestriction));
//
//      if(
//        images.isEmpty() ||
//        images.get(0).file == null){
//        return null;
//      }
//
//      final String pngFilePath =
//        SSCoreConf.instGet().getSss().getLocalWorkPath() +
//        SSVocConf.fileIDFromSSSURI(images.get(0).file.id);
//
//      return SSFileU.readImageToBase64Str(pngFilePath);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }