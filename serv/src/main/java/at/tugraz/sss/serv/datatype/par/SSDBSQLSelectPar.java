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
package at.tugraz.sss.serv.datatype.par;

import at.tugraz.sss.serv.datatype.*;
import at.tugraz.sss.serv.datatype.enums.SSErrE;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class SSDBSQLSelectPar {
  
  public SSServPar                                              servPar                   = null;
  public List<String>                                           tables                    = new ArrayList<>();
  public List<String>                                           columns                   = new ArrayList<>();
  public List<MultivaluedMap<String, String>>                   orWheres                  = new ArrayList<>();
  public List<MultivaluedMap<String, String>>                   andWheres                 = new ArrayList<>();
  public MultivaluedMap<String, MultivaluedMap<String, String>> numbericWheres            = new MultivaluedHashMap<>();
  public List<String>                                           matches                   = new ArrayList<>();
  public List<String>                                           requireds                 = new ArrayList<>();
  public List<String>                                           absents                   = new ArrayList<>();
  public List<String>                                           eithers                   = new ArrayList<>();
  public List<String>                                           tableCons                 = new ArrayList<>();
  public String                                                 globalSearchOp            = "AND";
  public String                                                 orderByColumn             = null;
  public String                                                 sortType                  = null;
  public Integer                                                limit                     = null;
  
  public SSDBSQLSelectPar(
    final SSServPar                                              servPar,
    final List<String>                                           tables,
    final List<String>                                           columns,
    final List<MultivaluedMap<String, String>>                   orWheres,
    final List<MultivaluedMap<String, String>>                   andWheres,
    final MultivaluedMap<String, MultivaluedMap<String, String>> numbericWheres,
    final List<String>                                           tableCons) throws SSErr{
    
    this.servPar = servPar;
    
    if(tables.isEmpty()){
      throw SSErr.get(SSErrE.parameterMissing);
    }
    
    this.tables.addAll(tables);
    
    if(columns != null){
      this.columns.addAll(columns);
    }
    
    if(orWheres != null){
      this.orWheres.addAll(orWheres);
    }
    
    if(andWheres != null){
      this.andWheres.addAll(andWheres);
    }
    
    if(numbericWheres != null){
      this.numbericWheres.putAll(numbericWheres);
    }
    
    this.tableCons.addAll(tableCons);
  }
}