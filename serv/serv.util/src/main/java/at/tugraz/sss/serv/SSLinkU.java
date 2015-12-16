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
package at.tugraz.sss.serv;

public class SSLinkU{
  
  //prefixes
  public static final String prefixHttp            = "http://";
  
  public static final String httpsEvernote         = "https://www.evernote.com/";
  public static final String httpsEvernoteSandbox  = "https://sandbox.evernote.com/";
  public static final String httpWikipediaEn       = "https://en.wikipedia.org/wiki/";
  public static final String httpsWikipediaEn      = "http://en.wikipedia.org/wiki/";
  public static final String schemaOrgUrl          = "http://schema.org/url";
  public static final String xsd                   = "http://www.w3.org/2001/XMLSchema#";
  public static final String ccUahEsIeOntOeOaae    = "http://www.cc.uah.es/ie/ont/OE-OAAE#";
 
  //	public  static final String uriMature                                = "http://tug.mature-ip.eu/";
  //	public  static final String uriVirtualFileRepository                 = "http://tug.mature-ip.eu/virtualfilerepository/";
}


//public List<String> scaffRecommTags(SSServPar parI) throws Exception{
//URL                  recommUri;
//URLConnection        recommCon;
//BufferedReader       recommIn;
//String               userLabel; 
//String               inputLine;
// List<String>         result         = new ArrayList<>();
//String               resourceString = SSStrU.toStr(par.resource);
//    if(SSStrU.contains(SSStrU.toStr(par.resource), SSLinkU.wikipediaEn.toString())){
//      resourceString = SSStrU.removeStringFromBegin(resourceString, SSLinkU.wikipediaEn.toString());
//    }else{
//      resourceString = null;
//    }
//opPars = new HashMap<>();
//    opPars.put(SSVarU.user, par.user);
//opPars.put(SSVarU.resource, par.user);
//    
//     userLabel = (String) SSServReg.callServServer(new SSServPar(SSVarNames.labelGet, opPars));
//recommUri      = new URL(SSStrU.addTrailingSlash(((SSScaffConf)conf).getPath()) + userLabel + SSStrU.slash + resourceString);
//recommCon      = recommUri.openConnection();
//recommIn       = new BufferedReader(new InputStreamReader(recommCon.getInputStream()));
//
//while ((inputLine = recommIn.readLine()) != null){
//result = SSJSONU.jsonArrayList(inputLine);
//break;
//}
//
//recommIn.close();

//return result;
//}