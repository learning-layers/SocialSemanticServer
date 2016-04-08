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
package at.tugraz.sss.servs.ocd.impl;

import at.tugraz.sss.serv.datatype.enums.SSClientE;
import at.tugraz.sss.servs.ocd.api.SSOCDClientI;
import at.tugraz.sss.servs.ocd.api.SSOCDServerI;
import at.tugraz.sss.serv.conf.api.SSConfA;
import at.tugraz.sss.serv.db.api.SSDBNoSQLI;
import at.tugraz.sss.serv.db.api.SSDBSQLI;
import at.tugraz.sss.serv.datatype.SSErr;
import at.tugraz.sss.serv.reg.SSServErrReg;
import at.tugraz.sss.serv.impl.api.SSServImplWithDBA;
import at.tugraz.sss.serv.datatype.par.SSServPar;
import at.tugraz.sss.serv.reg.*;
import at.tugraz.sss.serv.datatype.ret.SSServRetI; 
import at.tugraz.sss.servs.ocd.conf.SSOCDConf;
import at.tugraz.sss.servs.ocd.datatype.SSOCDCreateCoverPar;
import at.tugraz.sss.servs.ocd.datatype.SSOCDCreateGraphPar;
import at.tugraz.sss.servs.ocd.datatype.SSOCDDeleteCoverPar;
import at.tugraz.sss.servs.ocd.datatype.SSOCDDeleteGraphPar;
import at.tugraz.sss.servs.ocd.datatype.SSOCDGetCoversPar;
import at.tugraz.sss.servs.ocd.datatype.SSOCDGetGraphPar;
import at.tugraz.sss.servs.ocd.datatype.SSOCDGetGraphsPar;

public class SSOCDImpl
extends SSServImplWithDBA
implements
  SSOCDClientI,
  SSOCDServerI {
  
  private SSOCDConf ocdConf = null;
  
  //TODO remove SQL/NoSQL services; are not needed since OCD uses JPA/EntityManager for DB access.
  
  public SSOCDImpl(final SSConfA conf) throws SSErr {
    super(conf, (SSDBSQLI) SSServReg.getServ(SSDBSQLI.class), (SSDBNoSQLI) SSServReg.getServ(SSDBNoSQLI.class));
    ocdConf = (SSOCDConf) conf;
  }
  
  @Override
  public SSServRetI ocdCreateGraph(SSClientE clientType, SSServPar parA) throws SSErr {
    try{
      
//TODO uncomment to turn on user authorization
      //SSServCallerU.checkKey(parA);
      final SSOCDCreateGraphPar par    = (SSOCDCreateGraphPar) parA.getFromClient(clientType, parA, SSOCDCreateGraphPar.class);
      
      String response = ocdCreateGraph(par);
      
      return null;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public String ocdCreateGraph(SSOCDCreateGraphPar parA) throws SSErr {
    
    return null;
//    return SSOCDResource.requestCreateGraph(parA);
  }
  
  @Override
  public SSServRetI ocdGetGraphs(SSClientE clientType, SSServPar parA) throws SSErr {
    
    try{
      final SSOCDGetGraphsPar par = (SSOCDGetGraphsPar) parA.getFromClient(clientType, parA, SSOCDGetGraphsPar.class);
      
      String response = ocdGetGraphs(par);
      
      return null;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public String ocdGetGraphs(SSOCDGetGraphsPar parA) throws SSErr {
    return null;
//    return SSOCDResource.requestGetGraphs(parA);
  }
  
  @Override
  public SSServRetI ocdGetGraph(SSClientE clientType, SSServPar parA) throws SSErr {
    
    try{
      final SSOCDGetGraphPar par = (SSOCDGetGraphPar) parA.getFromClient(clientType, parA, SSOCDGetGraphPar.class);
      
      String response = ocdGetGraph(par);
      
      return null;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public String ocdGetGraph(SSOCDGetGraphPar parA) throws SSErr {
    return null;
//    return SSOCDResource.requestGetGraph(parA);
  }
  
  @Override
  public SSServRetI ocdDeleteGraph(SSClientE clientType, SSServPar parA) throws SSErr {
    
    try{
      
      final SSOCDDeleteGraphPar par = (SSOCDDeleteGraphPar) parA.getFromClient(clientType, parA, SSOCDDeleteGraphPar.class);
      
      String response = ocdDeleteGraph(par);
      
      return null;
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public String ocdDeleteGraph(SSOCDDeleteGraphPar parA) throws SSErr {
    
    return null;
    //return SSOCDResource.requestDeleteGraph(parA);
  }
  
  @Override
  public SSServRetI ocdCreateCover(SSClientE clientType, SSServPar parA) throws SSErr {
    
    try{
      //TODO uncomment to turn on user authorization
      //SSServCallerU.checkKey(parA);
      final SSOCDCreateCoverPar par = (SSOCDCreateCoverPar) parA.getFromClient(clientType, parA, SSOCDCreateCoverPar.class);
      
      String response = ocdCreateCover(par);
      
      return null;
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  @Override
  public String ocdCreateCover(SSOCDCreateCoverPar parA) throws SSErr {
    //return OCDRessource.requestCreateCover(parA);
    return "coverID";
  }
  
  @Override
  public SSServRetI ocdGetCovers(SSClientE clientType, SSServPar parA) throws SSErr {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  @Override
  public String ocdGetCovers(SSOCDGetCoversPar parA) throws SSErr {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  @Override
  public SSServRetI ocdDeleteCover(SSClientE clientType, SSServPar parA) throws SSErr {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  @Override
  public String ocdDeleteCover(SSOCDDeleteCoverPar parA) throws SSErr {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  @Override
  public String ocdGetAlgorithmNames() throws SSErr {
    return null;
//    return SSOCDResource.requestGetAlgorithms();
  }
}