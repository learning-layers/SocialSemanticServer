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
package at.kc.tugraz.ss.serv.auth.impl.fct.oidc;

import at.kc.tugraz.ss.serv.err.reg.SSServErrReg;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.openid.connect.sdk.UserInfoErrorResponse;
import com.nimbusds.openid.connect.sdk.UserInfoResponse;
import com.nimbusds.openid.connect.sdk.UserInfoSuccessResponse;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class SSAuthOIDC{
  
  public static String getOIDCUserLabel(final String authToken) throws Exception{
        
    // send request to OpenID Connect user info endpoint to retrieve complete user information
    // in exchange for access token.
    final HTTPRequest     hrq;
    final HTTPResponse    hrs;
    final UserInfoResponse userInfoResponse;
      
    try{
      URI userinfoEndpointUri = new URI((String)((JSONObject) fetchOidcProviderConfig().get("config")).get("userinfo_endpoint"));
      hrq                     = new HTTPRequest(HTTPRequest.Method.GET, userinfoEndpointUri.toURL());
      hrq.setAuthorization("Bearer "+ authToken);
      
      //TODO: process all error cases that can happen (in particular invalid tokens)
      hrs = hrq.send();
      
    } catch (IOException|URISyntaxException error) {
      SSServErrReg.regErrThrow(new Exception("Unexpected authentication error: " + error.getMessage()));
      return null;
    }
    
    // process response from OpenID Connect user info endpoint
    
    try {
      userInfoResponse = UserInfoResponse.parse(hrs);
    } catch (ParseException error) {
      SSServErrReg.regErrThrow(new Exception("Couldn't parse UserInfo response: " + error.getMessage()));
      return null;
    }
    
    // failed request for OpenID Connect user info will result in no agent being returned.
    if (userInfoResponse instanceof UserInfoErrorResponse) {
      SSServErrReg.regErrThrow(new Exception("Open ID Connect UserInfo request failed! Cause: " + ((UserInfoErrorResponse) userInfoResponse).getErrorObject().getDescription()));
      return null;
    }
    
    // In case of successful request, map OpenID Connect user info to intern
    UserInfo userInfo = ((UserInfoSuccessResponse)userInfoResponse).getUserInfo();
    
    try {
      
      JSONObject ujson = userInfo.toJSONObject();
      //response.println("User Info: " + userInfo.toJSONObject());
      
//      String sub  = (String) ujson.get("sub");
//      String mail = (String) ujson.get("mail");
//      String name = (String) ujson.get("name");
      
//      long oidcAgentId   = hash(sub);
//      username           = oidcAgentId + "";
//      password           = sub;
//      toUnlockPrivateKey = ujson.get("sub").toString();
//      email              = (String) ujson.get("email");
//      loginName          = (String) ujson.get("preferred_username");
      
      return (String) ujson.get("preferred_username");
      
    } catch (Exception error) {
      SSServErrReg.regErrThrow(error);
      return null;
    }
  }
  
  private static JSONObject fetchOidcProviderConfig() throws Exception{

		JSONObject result = new JSONObject();

		//send Open ID Provider Config request
		//(cf. http://openid.net/specs/openid-connect-discovery-1_0.html#ProviderConfig)
    URL         pConfigDocUri  = new URL("http://api.learning-layers.eu/o/oauth2".trim() + "/.well-known/openid-configuration");
		HTTPRequest pConfigRequest = new HTTPRequest(HTTPRequest.Method.GET, pConfigDocUri);

		// parse JSON result
		String     configStr = pConfigRequest.send().getContent();
		JSONObject config    = (JSONObject) JSONValue.parseWithException(configStr);

		// put JSON result in result table
		result.put("config", config);

		return result;
	}
}