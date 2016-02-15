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

import at.tugraz.sss.serv.datatype.par.SSCircleEntitiesAddPar;
import at.tugraz.sss.serv.entity.api.SSEntityServerI;
import at.tugraz.sss.conf.SSConf;
import at.tugraz.sss.serv.datatype.par.SSEntityUpdatePar;
import at.tugraz.sss.serv.entity.api.SSAddAffiliatedEntitiesToCircleI;
import at.tugraz.sss.serv.datatype.par.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.datatype.enums.*;
import at.tugraz.sss.serv.datatype.par.SSServPar; 
import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.entity.api.SSDescribeEntityI;
import at.tugraz.sss.serv.datatype.SSEntity;
import at.tugraz.sss.serv.datatype.SSEntityContext;
import at.tugraz.sss.serv.datatype.par.SSEntityDescriberPar;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.tugraz.sss.servs.common.impl.user.SSUserCommons;
import java.util.ArrayList;
import java.util.List;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import at.tugraz.sss.serv.util.SSFileExtE;
import at.tugraz.sss.serv.util.SSFileU;
import at.tugraz.sss.serv.datatype.SSImage;
import at.tugraz.sss.serv.datatype.enums.SSImageE;
import at.tugraz.sss.serv.util.SSLogU;
import at.tugraz.sss.serv.util.*;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI; 
import at.tugraz.sss.serv.entity.api.SSUsersResourcesGathererI;
import at.tugraz.sss.serv.datatype.par.SSEntityRemovePar;
import at.tugraz.sss.servs.image.api.SSImageClientI;
import at.tugraz.sss.servs.image.api.SSImageServerI;
import at.tugraz.sss.servs.image.datatype.par.SSImageProfilePictureSetPar;
import at.tugraz.sss.servs.image.datatype.par.SSImageAddPar;
import at.tugraz.sss.servs.image.datatype.par.SSImageGetPar;
import at.tugraz.sss.servs.image.datatype.par.SSImagesGetPar;
import at.tugraz.sss.servs.image.datatype.ret.SSImageAddRet;
import at.tugraz.sss.servs.image.datatype.ret.SSImageProfilePictureSetRet;
import at.tugraz.sss.servs.image.datatype.ret.SSImagesGetRet;
import at.tugraz.sss.servs.image.impl.sql.SSImageSQLFct;
import at.tugraz.sss.servs.file.api.*;
import at.tugraz.sss.servs.file.datatype.par.*;
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
  private final SSUserCommons      userCommons;
  
  public SSImageImpl(final SSConfA conf) throws SSErr {
    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
    
    this.sql            = new SSImageSQLFct   (dbSQL, SSConf.systemUserUri);
    this.entityServ     = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
    this.userCommons    = new SSUserCommons();
  }
  
  @Override
  public void getUsersResources(
    final SSServPar servPar,
    final Map<String, List<SSEntityContext>> usersEntities) throws SSErr{
    
    try{
      
      final SSImagesGetPar imagesGetPar =
        new SSImagesGetPar(
          servPar,
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
    }
  }
  
  @Override
  public SSEntity describeEntity(
    final SSServPar            servPar,
    final SSEntity             entity,
    final SSEntityDescriberPar par) throws SSErr{
    
    try{
      
      if(par.setThumb){
        
        for(SSEntity thumb :
          imagesGet(
            new SSImagesGetPar(
              servPar,
              par.user,
              entity.id,
              SSImageE.thumb,
              par.withUserRestriction))){
          
          entity.thumb = thumb;
          break;
        }
      }
      
      if(par.setProfilePicture){
        
        for(SSUri profilePicture : sql.getProfilePictures(servPar, entity.id)){
          
          entity.profilePicture =
            imageGet(
              new SSImageGetPar(
                servPar,
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
                servPar,
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
  public List<SSEntity> addAffiliatedEntitiesToCircle(
    final SSServPar                          servPar,
    final SSAddAffiliatedEntitiesToCirclePar par) throws SSErr{
    
    try{
      final SSEntityServerI circleServ         = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
      final List<SSEntity>  affiliatedEntities = new ArrayList<>();
      
      for(SSEntity entityAdded : par.entities){
        
        for(SSEntity image :
          imagesGet(
            new SSImagesGetPar(
              servPar,
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
          servPar,
          par.user,
          par.circle,
          SSUri.getDistinctNotNullFromEntities(affiliatedEntities), //entities
          false, //withUserRestriction
          false)); //shouldCommit
      
      SSEntity.addEntitiesDistinctWithoutNull(
        affiliatedEntities, 
        SSServReg.inst.addAffiliatedEntitiesToCircle(
          servPar,
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
  public SSImage imageGet(final SSImageGetPar par) throws SSErr{

    try{
      
      SSEntity image = sql.getEntityTest(par, par.user, par.image, par.withUserRestriction);
      
      if(image == null){
        return null;
      }
      
      final SSFileServerI fileServ = (SSFileServerI) SSServReg.getServ(SSFileServerI.class);
      
      image = sql.getImage(par, par.image);
      
      for(SSEntity file :
        fileServ.filesGet(
          new SSEntityFilesGetPar(
            par,
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
  public SSServRetI imagesGet(SSClientE clientType, SSServPar parA) throws SSErr{

    userCommons.checkKeyAndSetUser(parA);

    final SSImagesGetPar par = (SSImagesGetPar) parA.getFromClient(clientType, parA, SSImagesGetPar.class);
      
    return SSImagesGetRet.get(imagesGet(par));
  }

  @Override
  public List<SSEntity> imagesGet(final SSImagesGetPar par) throws SSErr{
    
    try{
      
      if(par.withUserRestriction){
        
        if(par.entity != null){
          
          final SSEntity enity = sql.getEntityTest(par, par.user, par.entity, par.withUserRestriction);
            
          if(enity == null){
            return new ArrayList<>();
          }
        }
      }
      
      final List<SSUri>    imageURIs = sql.getImages(par, par.entity, par.imageType);
      final List<SSEntity> images    = new ArrayList<>();
      final SSImageGetPar  imageGetPar =
        new SSImageGetPar(
          par,
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
  public SSImageAddRet imageAdd(final SSImageAddPar par) throws SSErr{

    try{
      
      if(par.imageType == null){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      if(
        par.createThumb &&
        par.file == null){
        
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      final SSFileServerI fileServ = (SSFileServerI) SSServReg.getServ(SSFileServerI.class);
      SSUri                   imageUri;

      if(par.uuid != null){
        imageUri = SSConf.vocURICreateFromId(par.uuid);
      }else{
        
        if(par.link != null){
          imageUri = par.link;
        }else{
          imageUri = SSConf.vocURICreate();
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
          SSLogU.warn("thumb creation failed", error);
        }
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      if(par.createThumb){
        
        if(
          par.removeThumbsFromEntity &&
          par.entity != null){
          
          for(SSEntity thumb :
            imagesGet(
              new SSImagesGetPar(
                par,
                par.user,
                par.entity,
                SSImageE.thumb,
                par.withUserRestriction))){
            
            entityServ.entityRemove(new SSEntityRemovePar(par, par.user, thumb.id));
            
            try{
              SSFileU.delFile(conf.getLocalWorkPath() + SSConf.fileIDFromSSSURI(thumb.file.id));
            }catch(Exception error){
              SSLogU.warn("couldnt remove thumbnail files from filesys", error);
            }
          }
        }
      }
      
      final SSUri image =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par,
            par.user,
            imageUri,  //entity
            SSEntityE.image,  //type
            par.label, //label,
            null, //description,
            null, //creationTime,
            null, //read,
            false, //setPublic,
            true, //createIfNotExists
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
      
      if(image == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return new SSImageAddRet(null, null);
      }
      
      sql.addImage(
        par, 
        imageUri, 
        par.imageType,
        par.link);
      
      if(par.entity != null){
        
        final SSUri entity =
          entityServ.entityUpdate(
            new SSEntityUpdatePar(
              par,
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
          sql.addImageToEntity(par, imageUri, par.entity);
        }
      }
      
      if(
        par.createThumb &&
        thumbFileURI != null){
        
        fileServ.fileAdd(
          new SSEntityFileAddPar(
            par,
            par.user,
            null, //fileBytes
            null, //fileLength
            null, //fileExt
            thumbFileURI, //file
            null, //type
            par.label, //label
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
            par,
            par.user,
            null, //fileBytes
            null, //fileLength
            null, //fileExt
            par.file, //file
            null, //type
            par.label, //label
            imageUri, //entity
            false, //createThumb
            null, //entityToAddThumbTo
            false, //removeExistingFilesForEntity
            par.withUserRestriction,
            par.shouldCommit));
      }
      
      dbSQL.commit(par, par.shouldCommit);

      return new SSImageAddRet(imageUri, thumbFileURI);
    }catch(SSErr error){
      
      switch(error.code){

        case sqlDeadLock:{
          
          try{
            dbSQL.rollBack(par, par.shouldCommit);
            SSServErrReg.regErrThrow(error);
            return null;
          }catch(Exception error2){
            SSServErrReg.regErrThrow(error2);
            return null;
          }
        }
        
        default:{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public SSServRetI imageProfilePictureSet(SSClientE clientType, SSServPar parA) throws SSErr {
    
    userCommons.checkKeyAndSetUser(parA);

    final SSImageProfilePictureSetPar par = (SSImageProfilePictureSetPar) parA.getFromClient(clientType, parA, SSImageProfilePictureSetPar.class);
    
    return new SSImageProfilePictureSetRet(imageProfilePictureSet(par));
  }
  
  @Override 
  public SSUri imageProfilePictureSet(final SSImageProfilePictureSetPar par) throws SSErr{
    
    try{
      
      if(SSObjU.isNull(par.file, par.entity)){
        throw SSErr.get(SSErrE.parameterMissing);
      }
      
      SSEntity file = 
        sql.getEntityTest(
          par, 
          par.user, 
          par.file,   
          par.withUserRestriction);
      
      final SSEntity entity = 
        sql.getEntityTest(
          par, 
          par.user, 
          par.entity, 
          par.withUserRestriction);
      
      if(SSObjU.isNull(file, entity)){
        return null;
      }
      
      dbSQL.startTrans(par, par.shouldCommit);
      
      final SSUri entityURI =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par,
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
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      SSUri profilePicture =
        imageAdd(
          new SSImageAddPar(
            par,
            par.user,
            null, //uuid
            null, //link
            SSImageE.image, //imageType
            par.entity, //entity
            par.file, //file
            entity.label, //label
            false, //createThumb
            false, //isImageToAddTheThumb
            false, //removeThumbsFromEntity
            par.withUserRestriction,
            false)).image;
      
      if(profilePicture == null){
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
          
      profilePicture =
        entityServ.entityUpdate(
          new SSEntityUpdatePar(
            par,
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
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      sql.removeProfilePictures (par, par.entity);
      sql.addProfilePicture     (par, par.entity, profilePicture);
      
      removeThumbsFromEntity(
        par,
        par.user,
        par.entity,
        par.withUserRestriction);
      
      if(!addThumbFromFileToEntity(
        par,
        par.user,
        par.entity,
        par.file,
        entity.label,
        par.withUserRestriction)){
        
        dbSQL.rollBack(par, par.shouldCommit);
        return null;
      }
      
      dbSQL.commit(par, par.shouldCommit);
      
      return par.entity;
        
    }catch(SSErr error){
      
      switch(error.code){

        case sqlDeadLock:{
          
          try{
            dbSQL.rollBack(par, par.shouldCommit);
            SSServErrReg.regErrThrow(error);
            return null;
          }catch(Exception error2){
            SSServErrReg.regErrThrow(error2);
            return null;
          }
        }
        
        default:{
          SSServErrReg.regErrThrow(error);
          return null;
        }
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }

  private void removeThumbsFromEntity(
    final SSServPar servPar,
    final SSUri   user,
    final SSUri   entity,
    final boolean withUserRestriction) throws SSErr{
    
    try{
      
      final List<SSEntity> existingThumbs =
        imagesGet(
          new SSImagesGetPar(
            servPar,
            user,
            entity, //entity
            SSImageE.thumb,
            withUserRestriction));
      
      sql.removeImagesFromEntity(servPar, SSUri.getDistinctNotNullFromEntities(existingThumbs));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  private boolean addThumbFromFileToEntity(
    final SSServPar servPar,
    final SSUri   user,
    final SSUri   entity,
    final SSUri   file,
    final SSLabel entityLabel,
    final boolean withUserRestriction) throws SSErr{
    
    try{
      //TODO refactor public setting: shall be done with hooks for entityPublicSet in respective entity type service implementations
      boolean worked = false;
      SSUri   thumbForEntity;
      SSUri   thumbURI;
      SSUri   thumbFileURI;
      
      for(SSEntity fileThumb :
        imagesGet(
          new SSImagesGetPar(
            servPar, 
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
              servPar, 
              user,
              null, //uuid
              null, //link
              SSImageE.thumb,
              entity, //entity
              fileThumb.file.id, //file
              entityLabel, //label
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
              servPar, 
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
              servPar, 
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
      return false;
    }
  }

  private SSUri createThumbnail(
    final SSUri      fileURI,
    final Integer    width) throws SSErr{
    
    try{
    
      final String      filePath          = conf.getLocalWorkPath() + SSConf.fileIDFromSSSURI(fileURI);
      final SSFileExtE  fileExt           = SSFileExtE.getFromStrToFormat(SSConf.fileIDFromSSSURI(fileURI));
      final SSUri       thumbFileURI      = SSConf.vocURICreate(SSFileExtE.png);
      final String      thumbnailPath     = conf.getLocalWorkPath() + SSConf.fileIDFromSSSURI(thumbFileURI);

      if(SSStrU.contains(SSFileExtE.imageFileExts, fileExt)){
        SSFileU.scalePNGAndWrite(ImageIO.read(new File(filePath)), thumbnailPath, width, width);
        return thumbFileURI;
      }

      switch(fileExt){
        
        case txt:{
          
          try{
            
            final String pdfFilePath = conf.getLocalWorkPath() + SSConf.fileIDFromSSSURI(SSConf.vocURICreate(SSFileExtE.pdf));
            
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
            SSLogU.warn("thumb creation from txt failed", error);
            throw error;
          }
        }
        
        case pdf:{
          
          SSFileU.writeScaledPNGFromPDF(filePath, thumbnailPath, width, width, false);
          return thumbFileURI;
        }
        
        case doc:{
          
          final String pdfFilePath  = conf.getLocalWorkPath() + SSConf.fileIDFromSSSURI(SSConf.vocURICreate(SSFileExtE.pdf));
          
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
//  public String imageBase64Get(final SSImageBase64GetPar par) throws SSErr{
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
//        SSConf.fileIDFromSSSURI(images.get(0).file.id);
//
//      return SSFileU.readImageToBase64Str(pngFilePath);
//      
//    }catch(Exception error){
//      SSServErrReg.regErrThrow(error);
//      return null;
//    }
//  }