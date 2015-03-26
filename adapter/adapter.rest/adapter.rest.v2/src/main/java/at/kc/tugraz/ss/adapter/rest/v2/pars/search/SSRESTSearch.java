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
package at.kc.tugraz.ss.adapter.rest.v2.pars.search;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.ss.adapter.rest.v2.SSRestMainV2;
import at.kc.tugraz.ss.service.search.datatypes.pars.SSSearchPar;
import at.kc.tugraz.ss.service.search.datatypes.ret.SSSearchRet;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/search")
@Api( value = "/search") // , basePath = "/search"
public class SSRESTSearch{

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("")
  @ApiOperation(
    value = "search for entities",
    response = SSSearchRet.class)
  public Response search(
    @Context HttpHeaders       headers,
    final SSSearchRESTAPIV2Par input){
    
    final SSSearchPar par;
    
    try{
      
      par =
        new SSSearchPar(
          SSMethU.search,
          null,
          null,
          input.includeTextualContent,
          input.wordsToSearchFor,
          input.includeTags,
          input.tagsToSearchFor,
          input.includeLabel,
          input.labelsToSearchFor,
          input.includeDescription,
          input.descriptionsToSearchFor,
          input.typesToSearchOnlyFor,
          input.includeOnlySubEntities,
          input.entitiesToSearchWithin,
          input.extendToParents,
          input.includeRecommendedResults,
          input.provideEntries,
          input.pagesID,
          input.pageNumber,
          input.minRating,
          input.maxRating,
          input.localSearchOp,
          input.globalSearchOp);
      
    }catch(Exception error){
      return Response.status(422).build();
    }
    
    return SSRestMainV2.handlePOSTRequest(headers, par);
  }
}
