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

import at.kc.tugraz.ss.recomm.datatypes.ret.SSRecommUsersRet;
import at.tugraz.sss.servs.ocd.api.SSOCDClientI;
import at.tugraz.sss.servs.ocd.api.SSOCDServerI;
import at.tugraz.sss.serv.SSConfA;
import at.tugraz.sss.serv.SSDBNoSQL;
import at.tugraz.sss.serv.SSDBNoSQLI;
import at.tugraz.sss.serv.SSDBSQL;
import at.tugraz.sss.serv.SSDBSQLI;
import at.tugraz.sss.serv.SSServImplWithDBA;
import at.tugraz.sss.serv.SSServPar;
import at.tugraz.sss.serv.SSSocketCon;
import at.tugraz.sss.serv.caller.SSServCallerU;
import at.tugraz.sss.servs.ocd.conf.SSOCDConf;

public class SSOCDImpl extends SSServImplWithDBA implements SSOCDClientI, SSOCDServerI {

    private SSOCDConf ocdConf = null;

    //TODO remove SQL/NoSQL services; are not needed since OCD uses JPA/EntityManager for DB access.

    public SSOCDImpl(final SSConfA conf) throws Exception {
        super(conf, (SSDBSQLI) SSDBSQL.inst.serv(), (SSDBNoSQLI) SSDBNoSQL.inst.serv());
        ocdConf = (SSOCDConf) conf;
    }

    @Override
    public void ocdCreateGraph(SSSocketCon sSCon, SSServPar parA) throws Exception {

        //Fixme uncomment to turn on user authorization 
        //SSServCallerU.checkKey(parA);
        
        String response = ocdCreateGraph(1);
        sSCon.writeRetFullToClient(response, parA.op);
    }

    @Override
    public String ocdCreateGraph(Integer parA) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ocdGetGraphs(SSSocketCon sSCon, SSServPar parA) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ocdDeleteGraph(SSSocketCon sSCon, SSServPar parA) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ocdCreateCover(SSSocketCon sSCon, SSServPar parA) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ocdGetCovers(SSSocketCon sSCon, SSServPar parA) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ocdDeleteCover(SSSocketCon sSCon, SSServPar parA) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String ocdGetGraphs(SSServPar parA) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String ocdDeleteGraph(SSServPar parA) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String ocdCreateCover(SSServPar parA) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String ocdGetCovers(SSServPar parA) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String ocdDeleteCover(SSServPar parA) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
