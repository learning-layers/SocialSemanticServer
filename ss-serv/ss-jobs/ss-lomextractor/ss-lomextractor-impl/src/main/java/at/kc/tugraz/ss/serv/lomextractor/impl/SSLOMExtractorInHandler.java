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
package at.kc.tugraz.ss.serv.lomextractor.impl;

import at.kc.tugraz.socialserver.utils.SSFileU;
import at.kc.tugraz.socialserver.utils.SSLogU;
import at.kc.tugraz.socialserver.utils.SSObjU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSXMLU;
import at.kc.tugraz.socialserver.utils.SSEncodingU;
import at.kc.tugraz.socialserver.utils.SSFileExtU;
import at.kc.tugraz.socialserver.utils.SSMimeTypeU;
import at.kc.tugraz.ss.serv.lomextractor.datatypes.SSLOMConceptRelation;
import at.kc.tugraz.ss.serv.lomextractor.datatypes.SSLOMCoverage;
import at.kc.tugraz.ss.serv.lomextractor.datatypes.SSLOMDesc;
import at.kc.tugraz.ss.serv.lomextractor.datatypes.SSLOMKeyword;
import at.kc.tugraz.ss.serv.lomextractor.datatypes.SSLOMResource;
import at.kc.tugraz.ss.serv.lomextractor.datatypes.SSLOMTitle;
import at.kc.tugraz.ss.serv.lomextractor.datatypes.SSLOMUser;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SSLOMExtractorInHandler {

  private SSLOMExtractorStats        stat                         = null;
  private Document                   lomXML                       = null;
  private NodeList                   general;
  private Node                       generalNode;
  private Element                    generalElement;
  private NodeList                   metaMetadata;
  private Node                       metaMetadataNode;
  private Element                    metaMetadataElement;
  private NodeList                   technical;
  private Node                       technicalNode;
  private Element                    technicalElement;
  private NodeList                   educational;
  private Node                       educationalNode;
  private Element                    educationalElement; 
  private NodeList                   classification;
  private Node                       classificationNode;
  private Element                    classificationElement;
  
  public void extractLOM(
    Map<String, SSLOMResource> resources, 
    File                       file,
    SSLOMExtractorStats        stat) throws Exception{
    
    this.stat = stat;
    
    DocumentBuilderFactory     lomXMLFactory         = DocumentBuilderFactory.newInstance();
    DocumentBuilder            lomXMLBuilder         = lomXMLFactory.newDocumentBuilder();
    List<String>               formats               = new ArrayList<String>();
    List<String>               learningResourceTypes = new ArrayList<String>();
    List<String>               intendedEndUserRoles  = new ArrayList<String>();
    List<String>               contexts              = new ArrayList<String>();
    List<SSLOMConceptRelation> conceptRelations      = new ArrayList<SSLOMConceptRelation>();
    List<SSLOMUser>            users                 = new ArrayList<SSLOMUser>();
    String                     id                    = null;
    String                     titleEn;
    List<SSLOMTitle>           titles;
    String                     lang;
    List<SSLOMDesc>            descs;
    List<SSLOMKeyword>         keywords;
    List<SSLOMCoverage>        coverages;
    SSLOMResource              resource;
    String                     fileText;
    
    try{
      id     = SSStrU.removeTrailingString(file.getName(), SSStrU.dot + SSFileExtU.xml);
      lomXML = lomXMLBuilder.parse(file);
      lomXML.getDocumentElement().normalize();
      
    }catch(Exception error){
      
      try{
        fileText = SSFileU.readFileText(file,     Charset.forName(SSEncodingU.utf8));
        fileText = SSStrU.replace      (fileText, SSStrU.ampersand, SSStrU.ampersandEncoded);
        
        SSFileU.writeFileText(file, fileText);
        
        lomXML   = lomXMLBuilder.parse(file);
        lomXML.getDocumentElement().normalize();
        
      }catch(Exception error1){
        stat.invalidXMLFileCount++;
        return;
      }
    }
    
    if(!setGeneral(file)){
      return;
    }
    
    titles = getTitles(file);
    
    if(titles.size() <= 0){
      return;
    }
    
    titleEn = titles.get(0).titleInLangWithDefault(titles, SSStrU.valueEn).label; //setUriIfEmpty   (uri, titles);
    
    if(SSStrU.isEmpty(titleEn)){
      return;
    }
    
    lang                   = getLang         (file);
    descs                  = getDescriptions (file);
    keywords               = getKeywords     (file);
    coverages              = getCoverages    (file);
    
    if(setMetaMetadata(file)){
      users                = getUsers(file);
    }
    
    if(setTechnical(file)){
      formats              = getFormats(file);
    }
    
    if(setEducational(file)){
      learningResourceTypes  = getLearningResourceTypes(file);
      intendedEndUserRoles   = getIntendedEndUserRoles(file);
      contexts               = getContexts(file);
    }
    
    if(setClassification(file)){
      conceptRelations       = getConceptRelations(file);
    }
    
    if(!resources.containsKey(id)){
      
      resources.put(id, new SSLOMResource(
        id,
        getUri(file),
        lang,
        formats,
        titles,
        keywords,
        descs,
        learningResourceTypes,
        intendedEndUserRoles,
        contexts,
        conceptRelations,
        users,
        coverages));
      
    }else{
      stat.duplicateResourceCount++;
      
      resource = resources.get(id);
      
      resource.formats.addAll                  (formats);
      resource.titles.addAll                   (titles);
      resource.keywords.addAll                 (keywords);
      resource.descs.addAll                    (descs);
      resource.learningResourceTypes.addAll    (learningResourceTypes);
      resource.intendedEndUserRoles.addAll     (intendedEndUserRoles);
      resource.contexts.addAll                 (contexts);
      resource.conceptRelatios.addAll          (conceptRelations);
      resource.users.addAll                    (users);
      resource.coverages.addAll                (coverages);
    }
  }
  
//  private String setUriIfEmpty(String uri, List<SSLOMTitle> titles){
//   
//    if(SSStrU.isEmpty(uri)){
//      return titles.get(0).titleInLangWithDefault(titles, SSStrU.valueEn).label;
//    }
//    
//    return uri;
//  }
    
  private List<SSLOMKeyword> getKeywords(File file){
    
    List<SSLOMKeyword>     result          = new ArrayList<SSLOMKeyword>();
    NodeList               keywords;
    Node                   keywordNode;
    Element                keywordElement;
    NodeList               strings;
    Element                stringElement;
    Node                   stringNode;
    String                 keyword;
    String                 language;
    
    keywords = generalElement.getElementsByTagName(SSXMLU.keyword);
    
    for(int keywordCounter = 0; keywordCounter < keywords.getLength(); keywordCounter++) {
      
      keywordNode = keywords.item(keywordCounter);
      
      if(SSXMLU.isNotNode(keywordNode)){
        SSLogU.info("wrong keyword structure 1: " + file.getAbsolutePath());
        continue;
      }
      
      keywordElement = (Element)keywordNode;
      strings        = keywordElement.getElementsByTagName(SSXMLU.string);
      
      for (int stringCounter = 0; stringCounter < strings.getLength(); stringCounter++) {
        
        stringNode = strings.item(stringCounter);
        
        if(SSXMLU.isNotNode(stringNode)){
          SSLogU.info("wrong keyword structure 2: " + file.getAbsolutePath());
          continue;
        }
        
        stringElement = (Element)stringNode;
        keyword       = stringNode.getTextContent().trim();
        language      = stringElement.getAttribute(SSXMLU.language);
        
        result.add(new SSLOMKeyword(keyword, language));
      }
    }
    
    if(result.size() <= 0){
      stat.noKeywordCount++;
    }
    
    return result;
  }
  
  private List<SSLOMDesc> getDescriptions(File file) {
    
    List<SSLOMDesc>        result          = new ArrayList<SSLOMDesc>();
    NodeList               descs;
    Node                   descNode;
    Element                descElement;
    NodeList               strings;
    Node                   stringNode;
    Element                stringElement;
    String                 desc;
    String                 language;
    
    descs = generalElement.getElementsByTagName(SSXMLU.description);
    
    for(int descCounter = 0; descCounter < descs.getLength(); descCounter++) {
      
      descNode = descs.item(descCounter);
      
      if(SSXMLU.isNotNode(descNode)){
        SSLogU.info("wrong desc structure 1: " + file.getAbsolutePath());
        continue;
      }
      
      descElement = (Element)descNode;
      strings     = descElement.getElementsByTagName(SSXMLU.string);
      
      for (int stringCounter = 0; stringCounter < strings.getLength(); stringCounter++) {
        
        stringNode = strings.item(stringCounter);
        
        if(SSXMLU.isNotNode(stringNode)){
          SSLogU.info("wrong desc structure 2: " + file.getAbsolutePath());
          continue;
        }

        stringElement = (Element)stringNode;
        desc          = stringNode.getTextContent().trim();
        language      = stringElement.getAttribute(SSXMLU.language);
        
        result.add(new SSLOMDesc(desc, language));
      }
    }
    
    if(result.size() <= 0){
      stat.noDescCount++;
    }
     
    return result;
  }
  
  private String getLang(File file) {
    String                 result          = SSStrU.empty;
    NodeList               langs;
    Node                   langNode;
    
    langs = generalElement.getElementsByTagName(SSXMLU.language);
    
    for(int langsCounter = 0; langsCounter < langs.getLength(); langsCounter++) {
      
      langNode = langs.item(langsCounter);
      
      if(SSXMLU.isNotNode(langNode)){
        SSLogU.info("wrong lang structure 1: " + file.getAbsolutePath());
        continue;
      }
      
      return langNode.getTextContent().trim();
    }
    
    if(SSStrU.isEmpty(result)){
      stat.noLangCount++;
    }
    
    return result;
  }
  
  private List<SSLOMTitle> getTitles(File file){
    
    List<SSLOMTitle>       result          = new ArrayList<SSLOMTitle>();
    NodeList               titles;
    Node                   titleNode;
    Element                titleElement;
    NodeList               strings;
    Element                stringElement;
    Node                   stringNode;
    String                 title;
    String                 language;
    
    titles = generalElement.getElementsByTagName(SSXMLU.title);
    
    for(int titleCounter = 0; titleCounter < titles.getLength(); titleCounter++) {
      
      titleNode = titles.item(titleCounter);
      
      if(SSXMLU.isNotNode(titleNode)){
        SSLogU.info("wrong title structure 1: " + file.getAbsolutePath());
        continue;
      }
      
      titleElement   = (Element)titleNode;
      strings        = titleElement.getElementsByTagName(SSXMLU.string);
      
      for (int stringCounter = 0; stringCounter < strings.getLength(); stringCounter++) {
        
        stringNode = strings.item(stringCounter);
        
        if(SSXMLU.isNotNode(stringNode)){
          SSLogU.info("wrong title structure 2: " + file.getAbsolutePath());
          continue;
        }
        
        stringElement = (Element)stringNode;
        title         = stringNode.getTextContent().trim();
        language      = stringElement.getAttribute(SSXMLU.language);
        
        result.add(new SSLOMTitle(title, language));
      }
    }
    
    if(result.size() <= 0){
      stat.noTitleCount++;
    }
    
    return result;
  }
  
  private List<SSLOMCoverage> getCoverages(File file) {
    List<SSLOMCoverage>    result          = new ArrayList<SSLOMCoverage>();
    NodeList               coverages;
    Node                   coverageNode;
    Element                coverageElement;
    NodeList               strings;
    Node                   stringNode;
    Element                stringElement;
    String                 coverage;
    String                 language;
    
    coverages      = generalElement.getElementsByTagName(SSXMLU.coverage);
    
    for(int coverageCounter = 0; coverageCounter < coverages.getLength(); coverageCounter++) {
      
      coverageNode = coverages.item(coverageCounter);
      
      if(SSXMLU.isNotNode(coverageNode)){
        SSLogU.info("wrong coverage structure 1: " + file.getAbsolutePath());
        continue;
      }
      
      coverageElement  = (Element)coverageNode;
      strings          = coverageElement.getElementsByTagName(SSXMLU.string);
      
      for (int stringCounter = 0; stringCounter < strings.getLength(); stringCounter++) {
        
        stringNode = strings.item(stringCounter);
        
        if(SSXMLU.isNotNode(stringNode)){
          SSLogU.info("wrong coverage structure 2: " + file.getAbsolutePath());
          continue;
        }
        
        stringElement = (Element)stringNode;
        coverage      = stringNode.getTextContent().trim();
        language      = stringElement.getAttribute(SSXMLU.language);
        
        result.add(new SSLOMCoverage(coverage, language));
      }
    }
    
    if(result.size() <= 0){
      stat.noCoverageCount++;
    }
    
    return result;
  }
  
  private String getUri(File file){
    String                 result          = SSStrU.empty;
    NodeList               identifiers;
    Node                   identifierNode;
    Element                identifierElement;
    NodeList               catalogs;
    NodeList               uris;
    
    identifiers    = generalElement.getElementsByTagName(SSXMLU.identifier);
    
    for(int identifierCounter = 0; identifierCounter < identifiers.getLength(); identifierCounter++) {
      
      identifierNode = identifiers.item(identifierCounter);
      
      if(SSXMLU.isNotNode(identifierNode)){
        SSLogU.info("wrong uri structure 1: " + file.getAbsolutePath());
        continue;
      }
      
      identifierElement = (Element)identifierNode;
      catalogs          = identifierElement.getElementsByTagName(SSXMLU.catalog);
      
      if(!catalogs.item(0).getTextContent().trim().equals(SSXMLU.URI)){
        continue;
      }
      
      uris = identifierElement.getElementsByTagName(SSXMLU.entry);
      
      result = uris.item(0).getTextContent().trim();
      break;
    }
    
    if(SSStrU.isEmpty(result)){
      stat.noUriCount++;
    }
    
    return result;
  }
  
  private List<SSLOMUser> getUsers(File file){
    
    List<SSLOMUser>        result           = new ArrayList<SSLOMUser>();
    NodeList               contributes;
    Node                   contributeNode;
    Element                contributeElement;
    NodeList               roles;
    Node                   roleNode;
    Element                roleElement;
    NodeList               values;
    Node                   valueNode;
    String                 role;
    NodeList               entities;
    Node                   entityNode;
    VCard                  vcard;
    String                 fullName;

    contributes         = metaMetadataElement.getElementsByTagName(SSXMLU.contribute);
    
    for(int contributeCounter = 0; contributeCounter < contributes.getLength(); contributeCounter++){
      
      contributeNode = contributes.item(contributeCounter);
      
      if(SSXMLU.isNotNode(contributeNode)){
        SSLogU.info("wrong user structure 1: " + file.getAbsolutePath());
        continue;
      }
      
      contributeElement = (Element)contributeNode;
      roles             = contributeElement.getElementsByTagName(SSXMLU.role);
      roleNode          = roles.item(0);
      
      if(SSXMLU.isNotNode(roleNode)){
        SSLogU.info("wrong user structure 2: " + file.getAbsolutePath());
        continue;
      }
      
      roleElement = (Element)roleNode;
      values      = roleElement.getElementsByTagName(SSXMLU.value);
      valueNode   = values.item(0);
      role        = valueNode.getTextContent().trim();
      
      entities     = contributeElement.getElementsByTagName(SSXMLU.entity);
      entityNode   = entities.item(0);
      
      if(SSXMLU.isNotNode(entityNode)){
        SSLogU.info("wrong user structure 3: " + file.getAbsolutePath());
        continue;
      }
      
      vcard        = Ezvcard.parse(entityNode.getTextContent()).first();
      fullName     = vcard.getFormattedName().getValue().trim();
      
      result.add(new SSLOMUser(fullName, role));
    }
    
    if(result.size() <= 0){
      stat.noUserCount++;
    }
    
    return result;
  }
  
  private List<String> getFormats(File file){
    
    List<String>           result           = new ArrayList<String>();
    NodeList               formats;
    Node                   formatNode;
    
    formats = technicalElement.getElementsByTagName(SSXMLU.format);
    
    for(int formatCounter = 0; formatCounter < formats.getLength(); formatCounter++){
      
      formatNode = formats.item(formatCounter);
      
      if(SSXMLU.isNotNode(formatNode)){
        
        SSLogU.info("wrong format: " + file.getAbsolutePath());
        continue;
      }
      
      result.add(formatNode.getTextContent().trim());
    }

    if(result.size() <= 0){
      stat.noFormatCount++;
    }
    
    return result;
  }
  
  private List<String> getLearningResourceTypes(File file){
    
    List<String>           result             = new ArrayList<String>();
    NodeList               learningResourceTypes;
    Node                   learningResourceTypeNode;
    Element                learningResourceTypeElement;
    NodeList               values;
    Node                   valueNode;
    
    learningResourceTypes     = educationalElement.getElementsByTagName(SSXMLU.learningResourceType);
    
    for(int learningResourceTypeCounter = 0; learningResourceTypeCounter < learningResourceTypes.getLength(); learningResourceTypeCounter++){
      
      learningResourceTypeNode  = learningResourceTypes.item(0);
      
      if(SSXMLU.isNotNode(learningResourceTypeNode)){
        SSLogU.info("wrong learning resource type structure 1: " + file.getAbsolutePath());
        continue;
      }
      
      learningResourceTypeElement = (Element)learningResourceTypeNode;
      values                      = learningResourceTypeElement.getElementsByTagName(SSXMLU.value);
      valueNode                   = values.item(0);
      
      if(SSXMLU.isNotNode(valueNode)){
        SSLogU.info("wrong learning resource type structure 2: " + file.getAbsolutePath());
        continue;
      }
      
      result.add(valueNode.getTextContent().trim());
    }
    
    if(result.size() <= 0){
      stat.noLearningResourceTypeCount++;
    }
    
    return result;
  }
  
  private List<String> getIntendedEndUserRoles(File file){
    
    List<String>           result             = new ArrayList<String>();
    NodeList               intendedEndUserRoles;
    Node                   intendedEndUserRoleNode;
    Element                intendedEndUserRoleElement;
    NodeList               values;
    Node                   valueNode;
    
    intendedEndUserRoles  = educationalElement.getElementsByTagName(SSStrU.valueIntendedEndUserRole);
    
    for(int intendedEndUserRoleCounter = 0; intendedEndUserRoleCounter < intendedEndUserRoles.getLength(); intendedEndUserRoleCounter++){
      
      intendedEndUserRoleNode = intendedEndUserRoles.item(intendedEndUserRoleCounter);
      
      if(SSXMLU.isNotNode(intendedEndUserRoleNode)){
        SSLogU.info("wrong intended end user role resource type structure 1: " + file.getAbsolutePath());
        continue;
      }
      
      intendedEndUserRoleElement = (Element)intendedEndUserRoleNode;
      values                      = intendedEndUserRoleElement.getElementsByTagName(SSXMLU.value);
      valueNode                   = values.item(0);
      
      if(SSXMLU.isNotNode(valueNode)){
        SSLogU.info("wrong intended end user role resource type structure 2: " + file.getAbsolutePath());
        continue;
      }
      
      result.add(valueNode.getTextContent().trim());
    }
    
    if(result.size() <= 0){
      stat.noIntendedEndUserRoleCount++;
    }
    
    return result;
  }
  
  private List<String> getContexts(File file){
    
    List<String>           result             = new ArrayList<String>();
    NodeList               contexts;
    Node                   contextNode;
    Element                contextElement;
    NodeList               values;
    Node                   valueNode;
    
    contexts            = educationalElement.getElementsByTagName(SSStrU.valueContext);
    
    for(int contextCounter = 0; contextCounter < contexts.getLength(); contextCounter++){
      
      contextNode = contexts.item(contextCounter);
      
      if(SSXMLU.isNotNode(contextNode)){
        SSLogU.info("wrong context structure 1: " + file.getAbsolutePath());
        continue;
      }
      
      contextElement = (Element)contextNode;
      values         = contextElement.getElementsByTagName(SSXMLU.value);
      valueNode      = values.item(0);
      
      if(SSXMLU.isNotNode(valueNode)){
        continue;
      }
      
      result.add(valueNode.getTextContent().trim());
    }
    
    if(result.size() <= 0){
      stat.noContextCount++;
    }
    
    return result;
  }
  
  private List<SSLOMConceptRelation> getConceptRelations(File file) throws Exception{
    
    List<SSLOMConceptRelation> result                = new ArrayList<SSLOMConceptRelation>();
    NodeList                   taxonPaths;
    Node                       taxonNode;
    Element                    taxonElement;
    NodeList                   sources;
    Node                       sourceNode;
    Element                    sourceElement;
    String                     lang;
    NodeList                   ids;
    Node                       idNode;
    NodeList                   strings;
    Node                       stringNode;
    Element                    stringElement;
    List<String>               relationAndConcept;
    
    taxonPaths             = classificationElement.getElementsByTagName(SSStrU.valueTaxonPath);
    
    for(int taxonPathCounter = 0; taxonPathCounter < taxonPaths.getLength(); taxonPathCounter++){
      
      taxonNode = taxonPaths.item(taxonPathCounter);
      
      if(SSXMLU.isNotNode(taxonNode)){
        continue;
      }
      
      taxonElement     = (Element)taxonNode;
      sources          = taxonElement.getElementsByTagName(SSStrU.valueSource);
      sourceNode       = sources.item(0);
      
      if(SSXMLU.isNotNode(sourceNode)){
        continue;
      }
      
      sourceElement    = (Element)sourceNode;
      strings          = sourceElement.getElementsByTagName(SSXMLU.string);
      stringNode       = strings.item(0);
      
      if(SSXMLU.isNotNode(stringNode)){
        continue;
      }
      
      stringElement    = (Element)stringNode;
      lang             = stringElement.getAttribute(SSXMLU.language);
      
      ids              = taxonElement.getElementsByTagName(SSXMLU.id);
      idNode           = ids.item(0);
     
      if(SSXMLU.isNotNode(idNode)){
        continue;
      }
      
      relationAndConcept = SSStrU.split(idNode.getTextContent().trim(), SSStrU.valueColonColon);
      
      if(relationAndConcept.size() == 2){
        result.add(new SSLOMConceptRelation(relationAndConcept.get(1).trim(), relationAndConcept.get(0).trim(), lang));
      }else{
        result.add(new SSLOMConceptRelation(relationAndConcept.get(0).trim(), SSStrU.empty, lang));
      }
    }
    
    if(result.size() <= 0){
      stat.noConceptRelationCount++;
    }
        
    return result;
  }
  
  private boolean setGeneral(File file){
    
    general = lomXML.getElementsByTagName(SSXMLU.general);
    
    if(
      SSObjU.isNull(general) ||
      general.getLength() <= 0){
      
      stat.noGeneralCount++;
      return false;
    }
    
    generalNode = general.item(0);
    
    if(SSXMLU.isNotNode(generalNode)){
      stat.noGeneralCount++;
      return false;
    }
    
    generalElement = (Element)generalNode;
    
    return true;
  }
  
  private boolean setMetaMetadata(File file){
   
    metaMetadata     = lomXML.getElementsByTagName(SSXMLU.metaMetadata);
    
    if(
      SSObjU.isNull(metaMetadata) ||
      metaMetadata.getLength() <= 0){
      
      stat.noMetaMetadataCount++;
      return false;
    }
    
    metaMetadataNode = metaMetadata.item(0);
    
    if(SSXMLU.isNotNode(metaMetadataNode)){
      stat.noMetaMetadataCount++;
      return false;
    }
    
    metaMetadataElement = (Element)metaMetadataNode;
    
    return true;
  }
  
  private boolean setEducational(File file){
    
    educational        = lomXML.getElementsByTagName(SSXMLU.educational);
    
    if(
      SSObjU.isNull(educational) ||
      educational.getLength() <= 0){
      
      stat.noEducationalCount++;
      return false;
    }
    
    educationalNode    = educational.item(0);
    
    if(SSXMLU.isNotNode(educationalNode)){
      stat.noEducationalCount++;
      return false;
    }
    
    educationalElement        = (Element)educationalNode;
    
    return true;
  }
  
  private boolean setClassification(File file){
    
    classification        = lomXML.getElementsByTagName(SSXMLU.classification);
    
    if(
      SSObjU.isNull(classification) ||
      classification.getLength() <= 0){
      
      stat.noClassificationCount++;
      return false;
    }
    
    classificationNode    = classification.item(0);
    
    if(SSXMLU.isNotNode(classificationNode)){
      stat.noClassificationCount++;
      return false;
    }
    
    classificationElement  = (Element)classificationNode;
    
    return true;
  }
  
  private boolean setTechnical(File file){
    
    technical        = lomXML.getElementsByTagName(SSXMLU.technical);

    if(
      SSObjU.isNull(technical) ||
      technical.getLength() <= 0){
      
      stat.noTechnicalCount++;
      return false;
    }
    
    technicalNode    = technical.item(0);
    
    if(SSXMLU.isNotNode(technicalNode)){
      stat.noTechnicalCount++;
      return false;
    }
    
    technicalElement = (Element)technicalNode;
    
    return true;
  }
}
