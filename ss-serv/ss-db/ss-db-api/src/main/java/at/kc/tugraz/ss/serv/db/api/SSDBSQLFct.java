/**
 * Copyright 2013 Graz University of Technology - KTI (Knowledge Technologies Institute)
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
package at.kc.tugraz.ss.serv.db.api;

import at.kc.tugraz.ss.datatypes.datatypes.SSEntityEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSLabelStr;
import at.kc.tugraz.ss.datatypes.datatypes.SSSpaceEnum;
import at.kc.tugraz.ss.datatypes.datatypes.SSUri;
import java.sql.ResultSet;

public class SSDBSQLFct extends SSDBFct{

  protected        final SSDBSQLI dbSQL;
  protected static final String   circleTable                         = "circle";
  protected static final String   circleUsersTable                    = "circleUsers";
  protected static final String   circleEntitiesTable                 = "circleEntities";
  protected static final String   collTable                           = "coll";
  protected static final String   collRootTable                       = "collroot";
  protected static final String   collEntryPosTable                   = "collentrypos";
  protected static final String   collHierarchyTable                  = "collhierarchy";
  protected static final String   collUserTable                       = "colluser";
  protected static final String   entityTable                         = "entity";

  public SSDBSQLFct(final SSDBSQLI dbSQL) throws Exception{
    super();
    
    this.dbSQL = dbSQL;
  }
  
  public SSUri bindingStrToUri(ResultSet resultSet, String binding) throws Exception{
    return SSUri.get(bindingStr(resultSet, binding));
  }
  
  public SSSpaceEnum bindingStrToSpace(ResultSet resultSet, String binding) throws Exception{
    return SSSpaceEnum.get(bindingStr(resultSet, binding));
  }
  
  public String bindingStr(ResultSet resultSet, String binding) throws Exception{
    return resultSet.getString(binding);
  }
  
  public SSLabelStr bindingStrToLabel(ResultSet resultSet, String binding) throws Exception{
    return SSLabelStr.get(bindingStr(resultSet, binding));
  }
  
  public SSEntityEnum bindingStrToEntityType(ResultSet resultSet, String binding) throws Exception{
    return SSEntityEnum.valueOf(bindingStr(resultSet, binding));
  }
  
  public Float bindingStrToFloat(ResultSet resultSet, String binding) throws Exception{
    return Float.parseFloat(bindingStr(resultSet, binding));
  }
  
  public Integer bindingStrToInteger(ResultSet resultSet, String binding) throws Exception{
    return Integer.parseInt(bindingStr(resultSet, binding));
  }
  
  public Long bindingStrToLong(ResultSet resultSet, String binding) throws Exception{
    return Long.parseLong(bindingStr(resultSet, binding));
  }
}
