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

import at.tugraz.sss.servs.ocd.api.SSOCDClientI;
import at.tugraz.sss.servs.ocd.api.SSOCDServerI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSErr;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.servs.ocd.conf.SSOCDConf;
import at.tugraz.sss.servs.ocd.datatypes.pars.SSOCDCreateCoverPar;
import at.tugraz.sss.servs.ocd.datatypes.pars.SSOCDCreateGraphPar;
import at.tugraz.sss.servs.ocd.datatypes.pars.SSOCDDeleteCoverPar;
import at.tugraz.sss.servs.ocd.datatypes.pars.SSOCDDeleteGraphPar;
import at.tugraz.sss.servs.ocd.datatypes.pars.SSOCDGetCoversPar;
import at.tugraz.sss.servs.ocd.datatypes.pars.SSOCDGetGraphPar;
import at.tugraz.sss.servs.ocd.datatypes.pars.SSOCDGetGraphsPar;
import at.tugraz.sss.servs.ocd.impl.jerseyclient.SSOCDResource;

public class SSOCDImpl 
extends SSServImplWithDBA 
implements 
  SSOCDClientI, 
  SSOCDServerI {

  private SSOCDConf ocdConf = null;

  //TODO remove SQL/NoSQL services; are not needed since OCD uses JPA/EntityManager for DB access.

  public SSOCDImpl(final SSConfA conf) throws SSErr {
    super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
    ocdConf = (SSOCDConf) conf;
  }

  @Override
  public void ocdCreateGraph(SSSocketCon sSCon, SSServPar parA) throws Exception {
    //Fixme uncomment to turn on user authorization 
    //SSServCallerU.checkKey(parA);
    final SSOCDCreateGraphPar par    = (SSOCDCreateGraphPar) parA.getFromJSON(SSOCDCreateGraphPar.class);
    String response = ocdCreateGraph(par);
    //sSCon.writeRetFullToClient(response, parA.op);
  }

  @Override
  public String ocdCreateGraph(SSOCDCreateGraphPar parA) throws Exception {
    return SSOCDResource.requestCreateGraph(parA);
  }
  
  @Override
  public void ocdGetGraphs(SSSocketCon sSCon, SSServPar parA) throws Exception {
    final SSOCDGetGraphsPar par = (SSOCDGetGraphsPar) parA.getFromJSON(SSOCDGetGraphsPar.class);
    String response = ocdGetGraphs(par);
    //sSCon.writeRetFullToClient(response, parA.op);
  }
  
  @Override
  public String ocdGetGraphs(SSOCDGetGraphsPar parA) throws Exception {
    return SSOCDResource.requestGetGraphs(parA);
  }
  
  @Override
  public void ocdGetGraph(SSSocketCon sSCon, SSServPar parA) throws Exception {
    final SSOCDGetGraphPar par = (SSOCDGetGraphPar) parA.getFromJSON(SSOCDGetGraphPar.class);
    String response = ocdGetGraph(par);
    //sSCon.writeRetFullToClient(response, parA.op);
  }

  @Override
  public String ocdGetGraph(SSOCDGetGraphPar parA) throws Exception {
    return SSOCDResource.requestGetGraph(parA);
  }
  
  @Override
  public void ocdDeleteGraph(SSSocketCon sSCon, SSServPar parA) throws Exception {
    final SSOCDDeleteGraphPar par = (SSOCDDeleteGraphPar) parA.getFromJSON(SSOCDDeleteGraphPar.class);
    String response = ocdDeleteGraph(par);
    //sSCon.writeRetFullToClient(response, parA.op);
  }
  
  @Override
  public String ocdDeleteGraph(SSOCDDeleteGraphPar parA) throws Exception {
    return SSOCDResource.requestDeleteGraph(parA);
  }
  
  @Override
  public void ocdCreateCover(SSSocketCon sSCon, SSServPar parA) throws Exception {
    //Fixme uncomment to turn on user authorization 
    //SSServCallerU.checkKey(parA);
    final SSOCDCreateCoverPar par = (SSOCDCreateCoverPar) parA.getFromJSON(SSOCDCreateCoverPar.class);
    String response = ocdCreateCover(par);
    //sSCon.writeRetFullToClient(response, parA.op);
  }
  
  @Override
  public String ocdCreateCover(SSOCDCreateCoverPar parA) throws Exception {
    //return OCDRessource.requestCreateCover(parA);
    return "coverID";
  }
  
  @Override
  public void ocdGetCovers(SSSocketCon sSCon, SSServPar parA) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  @Override
  public String ocdGetCovers(SSOCDGetCoversPar parA) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  @Override
  public void ocdDeleteCover(SSSocketCon sSCon, SSServPar parA) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  @Override
  public String ocdDeleteCover(SSOCDDeleteCoverPar parA) throws Exception {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public String ocdGetAlgorithmNames() throws Exception {
    return SSOCDResource.requestGetAlgorithms();
  }
}