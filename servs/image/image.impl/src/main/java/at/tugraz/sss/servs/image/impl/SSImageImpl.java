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
import at.kc.tugraz.ss.conf.conf.SSCoreConf;
import at.kc.tugraz.ss.serv.datatypes.entity.api.SSEntityServerI;
import at.kc.tugraz.ss.serv.voc.conf.SSVocConf;
import at.tugraz.sss.servs.entity.datatypes.par.SSEntityUpdatePar;
import at.kc.tugraz.ss.service.filerepo.api.SSFileRepoServerI;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCircleI;
import at.tugraz.sss.serv.SSAddAffiliatedEntitiesToCirclePar;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSEntityE;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDescribeEntityI;
import at.tugraz.sss.serv.SSEntity;
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
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServReg;
import at.tugraz.sss.serv.SSStrU;
import at.tugraz.sss.serv.caller.SSServCaller;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFileAddPar;
import at.tugraz.sss.servs.file.datatype.par.SSEntityFilesGetPar;
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
import java.io.File;
import javax.imageio.ImageIO;

public class SSImageImpl 
extends SSServImplWithDBA 
implements 
  SSImageClientI, 
  SSImageServerI,
  SSDescribeEntityI,
  SSAddAffiliatedEntitiesToCircleI{

  private final SSImageSQLFct         sqlFct;
  private final SSEntityServerI       entityServ;
  private final String                localWorkPath;
  
  public SSImageImpl(final SSConfA conf) throws Exception{
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    
     this.sqlFct         = new SSImageSQLFct   (this);
     this.entityServ     = (SSEntityServerI) SSServReg.getServ(SSEntityServerI.class);
     this.localWorkPath  = SSCoreConf.instGet().getSss().getLocalWorkPath();
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
        
        for(SSUri profilePicture : sqlFct.getProfilePictures(entity.id)){
          
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
      
      if(par.withUserRestriction){
        
        if(!SSServCallerU.canUserRead(par.user, par.image)){
          return null;
        }
      }
      
      final SSImage image = sqlFct.getImage(par.image);
      
      for(SSEntity file :
        ((SSFileRepoServerI) SSServReg.getServ(SSFileRepoServerI.class)).filesGet(
          new SSEntityFilesGetPar(
            par.user,
            par.image,
            par.withUserRestriction,
            false))){ //invokeEntityHandlers
        
        image.file = file;
        break;
      }
      
      return image;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public void imagesGet(SSSocketCon sSCon, SSServPar parA) throws Exception{

    SSServCallerU.checkKey(parA);

    final SSImagesGetPar par = (SSImagesGetPar) parA.getFromJSON(SSImagesGetPar.class);
      
    sSCon.writeRetFullToClient(SSImagesGetRet.get(imagesGet(par)));
  }

  @Override
  public List<SSEntity> imagesGet(final SSImagesGetPar par) throws Exception{
    
    try{
      
      if(par.withUserRestriction){
        
        if(par.entity != null){
          
          if(!SSServCallerU.canUserRead(par.user, par.entity)){
            return new ArrayList<>();
          }
        }
      }
      
      final List<SSUri>    imageURIs = sqlFct.getImages(par.entity, par.imageType);
      final List<SSEntity> images    = new ArrayList<>();
        
      for(SSUri imageURI : imageURIs){
        
        SSEntity.addEntitiesDistinctWithoutNull(
          images, 
          imageGet(
            new SSImageGetPar(
              par.user, 
              imageURI, 
              par.withUserRestriction)));
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
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(
        par.createThumb &&
        par.file == null){
        
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      final SSFileRepoServerI fileServ = (SSFileRepoServerI) SSServReg.getServ(SSFileRepoServerI.class);
      SSUri                   thumbURI = null;
      SSUri                   imageUri;

      if(par.uuid != null){
        imageUri = SSServCaller.vocURICreateFromId(par.uuid);
      }else{
        
        if(par.link != null){
          imageUri = par.link;
        }else{
          imageUri = SSServCaller.vocURICreate();
        }
      }
      
      dbSQL.startTrans(par.shouldCommit);
      
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
            
            SSServCaller.entityRemove(thumb.id, false);
            
            try{
              SSFileU.delFile(localWorkPath + SSVocConf.fileIDFromSSSURI(thumb.file.id));
            }catch(Exception error){
              SSLogU.warn("couldnt remove thumbnail files from filesys");
            }
          }
        }
      }
      
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
          par.withUserRestriction, //withUserRestriction
          false)); //shouldCommit)
      
      sqlFct.addImage(
        imageUri, 
        par.imageType,
        par.link);
      
      if(par.entity != null){
        
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
            par.withUserRestriction, //withUserRestriction
            false)); //shouldCommit)
        
        sqlFct.addImageToEntity(imageUri, par.entity);
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
  public void imageProfilePictureSet(SSSocketCon sSCon, SSServPar parA) throws Exception {
    
    SSServCallerU.checkKey(parA);

    final SSImageProfilePictureSetPar par = (SSImageProfilePictureSetPar) parA.getFromJSON(SSImageProfilePictureSetPar.class);
    
    sSCon.writeRetFullToClient(new SSImageProfilePictureSetRet(imageProfilePictureSet(par)));
  }
  
  @Override 
  public SSUri imageProfilePictureSet(final SSImageProfilePictureSetPar par) throws Exception{
    
    try{
      
      if(par.file == null){
        throw new SSErr(SSErrE.parameterMissing);
      }
      
      if(par.withUserRestriction){
        
        if(
          !SSServCallerU.canUserRead(par.user, par.entity) ||
          !SSServCallerU.canUserRead(par.user, par.file)){
          return null;
        }
      }

      dbSQL.startTrans(par.shouldCommit);
      
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
          par.withUserRestriction,
          false));
      
      final SSUri profilePicture =
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
          dbSQL.commit(par.shouldCommit);
          return null;
        }
          
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
            par.withUserRestriction,
            false));
          
        sqlFct.removeProfilePictures (par.entity);
        sqlFct.addProfilePicture     (par.entity, profilePicture);
        
        removeThumbsFromEntity(
          par.user, 
          par.entity, 
          par.withUserRestriction);
          
        addThumbFromFileToEntity(
          par.user, 
          par.entity, 
          par.file, 
          par.withUserRestriction);
        
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
      
      sqlFct.removeImagesFromEntity(SSUri.getDistinctNotNullFromEntities(existingThumbs));
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  private void addThumbFromFileToEntity(
    final SSUri   user,
    final SSUri   entity,
    final SSUri   file,
    final Boolean withUserRestriction) throws Exception{
    
    try{
      //TODO refactor public setting: shall be done with hooks for entityPublicSet in respective entity type service implementations
      SSUri thumbForEntity;
      
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
            withUserRestriction,
            false));
        
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
            withUserRestriction,
            false));
        
        break;
      }
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
    }
  }

  private SSUri createThumbnail(
    final SSUri      fileURI,
    final Integer    width) throws Exception{
    
    try{
    
      final String      filePath          = localWorkPath + SSVocConf.fileIDFromSSSURI(fileURI);
      final SSFileExtE  fileExt           = SSFileExtE.getFromStrToFormat(SSVocConf.fileIDFromSSSURI(fileURI));
      final SSUri       thumbFileURI      = SSServCaller.vocURICreate(SSFileExtE.png);
      final String      thumbnailPath     = localWorkPath + SSVocConf.fileIDFromSSSURI(thumbFileURI);

      if(SSStrU.contains(SSFileExtE.imageFileExts, fileExt)){
        SSFileU.scalePNGAndWrite(ImageIO.read(new File(filePath)), thumbnailPath, width, width);
        return thumbFileURI;
      }

      switch(fileExt){
        
        case txt:{
          
          try{
            
            final String pdfFilePath = localWorkPath + SSVocConf.fileIDFromSSSURI(SSServCaller.vocURICreate(SSFileExtE.pdf));
            
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
          
          final String pdfFilePath  = localWorkPath + SSVocConf.fileIDFromSSSURI(SSServCaller.vocURICreate(SSFileExtE.pdf));
          
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