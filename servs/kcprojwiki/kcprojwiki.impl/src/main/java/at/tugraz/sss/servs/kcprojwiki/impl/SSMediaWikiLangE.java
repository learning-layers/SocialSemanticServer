/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2016, Graz University of Technology - KTI (Knowledge Technologies Institute).
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
package at.tugraz.sss.servs.kcprojwiki.impl;

public enum SSMediaWikiLangE {

  text ("text"),
  login("login"),
  sessionid("sessionid"),
  token("token"),
  lgname("lgname"),
  lgpassword("lgpassword"),
  lgdomain("lgdomain"),
  lgtoken("lgtoken"),
  indexpageids("indexpageids"),
  prop("prop"),
  revisions("revisions"),
  rvlimit("rvlimit"),
  rvprop("rvprop"),
  content("content"),
  titles("titles"),
  query("query"),
  pageids("pageids"),
  results("results"),
  ask("ask"),
  items("items"),
  pages("pages"),
  edittoken("edittoken"),
  timestamp("timestamp"),
  intoken("intoken"),
  edit("edit"),
  info("info"),
  q("q"),
  result("result"),
  Success("Success"),
  code("code"),
  title("title"),
  mUrlform("mUrlform"),
  Cookie("Cookie"), 
  formEquals("form="),
  targetEquals("target="),
  formatEqualsJson("format=json"),
  ChangesBMD("Changes%20BMD"),
  apiActionQuery("api.php?action=query"),
  apiActionLogin("api.php?action=login"),
  apiActionLogout("api.php?action=logout"),
  apiActionEdit("api.php?action=edit"),
  apiActionSfautoedit("api.php?action=sfautoedit"),
  apiActionAsk("api.php?action=ask"),
  TotalProjectResources("Total%20Project%20Resources"),
  ResourcesUsedMonthEnd("Resources%20Used%20Month%20End"),
  ExportDate("Export%20Date"),
  ProjectProgress("Project%20Progress"),
  ProjektVorgangsebene("Projekt-Vorgangsebene"),
  WorksInVorgang("Works In Vorgang"),
  VorgangNumber("Vorgang%20Number"),
  VorgangBlankNumber("Vorgang Number"),
  VorgangBlankName("Vorgang Name"),
  ProjectNumber("Project%20Number"),
  ProjectBlankNumber("Project Number"),
  CategoryProjektVorgangsebene("[[Category:Projekt-Vorgangsebene]]"),
  CategoryProjekt("[[Category:Projekt]]");
  
  private final String val;
  
  private SSMediaWikiLangE(final String value){
    this.val = value;
  }
  
  @Override
  public String toString(){
    return val;
  }
}