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
package at.kc.tugraz.ss.service.tag.impl.fct.sql;

import at.tugraz.sss.serv.SSSQLVarNames;
import at.tugraz.sss.serv.SSDBSQLFct;
import at.tugraz.sss.serv.SSSpaceE;
import at.tugraz.sss.serv.SSUri;
import at.tugraz.sss.serv.SSServErrReg;
import at.tugraz.sss.serv.SSServImplWithDBA;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSTagSQLFct extends SSDBSQLFct{
  
  public SSTagSQLFct(final SSServImplWithDBA serv) throws Exception{
    super(serv.dbSQL);
  }

  public Boolean existsTagAss(
    final SSUri       userUri, 
    final SSUri       entityUri, 
    final SSUri       tagUri, 
    final SSSpaceE    space) throws Exception{

    ResultSet resultSet = null;
    
    try{
    
      final List<String>        columns  = new ArrayList<>();
      final Map<String, String> wheres  = new HashMap<>();
      
      column(columns, SSSQLVarNames.tagId);
      column(columns, SSSQLVarNames.userId);
      column(columns, SSSQLVarNames.entityId);
      column(columns, SSSQLVarNames.tagSpace);
      
      where(wheres, SSSQLVarNames.userId,       userUri);
      where(wheres, SSSQLVarNames.entityId,     entityUri);
      where(wheres, SSSQLVarNames.tagId,        tagUri);
      where(wheres, SSSQLVarNames.tagSpace,     space);
      
      resultSet = dbSQL.select(SSSQLVarNames.tagAssTable, columns, wheres, null, null, null);
      
      return resultSet.first();
      
    }catch(Exception error){
      SSServErrReg.regErrThrow(error);
      return null;
    }finally{
      dbSQL.closeStmt(resultSet);
    }
  }
}
