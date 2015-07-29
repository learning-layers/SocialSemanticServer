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

import java.util.*;

public class SSStrU{

  private SSStrU(){}

//  public static final String apiCircle                = "circles/circles";
//  
//  public static final String apiVideo                 = "videos/videos";
//  public static final String apiVideoAnnotation       = "videos/videos/annotation";
//  
//  public static final String apiUser                  = "users/users";
//  
//  public static final String apiLocation              = "location";
//  public static final String apiCollection            = "collection";
//  public static final String apiTag                   = "tag";
//  public static final String apiApp                   = "app";
//  public static final String apiAppStack              = "appstack";
//  public static final String apiAppStackTile          = "appstack/tile";
//  public static final String apiMessage               = "message";
//  public static final String apiLearnEpVersion        = "learnep/version";
//  public static final String apiLearnEpCircle         = "learnep/circle";
//  public static final String apiLearnEpEntity         = "learnep/entity";
//  public static final String apiLearnEp               = "learnep";
//  public static final String apiLearnEpTimelineState  = "learnep/timelinestate";
  
  //characters
  public  static final String equal                                           = "=";
  public  static final String zero                                            = "0";
  public  static final String five                                            = "5";
  public  static final String at                                              = "@";
  public  static final String colon                                           = ":";
  public  static final String comma                                           = ",";
  public  static final String semiColon                                       = ";";
  public  static final String pipe                                            = "|";
  public  static final String doubleQuote                                     = "\"";
  public  static final String backSlash                                       = "\\";
  public  static final String backSlashBackSlashN                             = "\\n";
  public  static final String dot                                             = ".";
  public  static final String percent                                         = "%";
  public  static final String bracketOpen                                     = "(";
  public  static final String bracketClose                                    = ")";
  public  static final String curlyBracketOpen                                = "{";
  public  static final String curlyBracketClose                               = "}";
  public  static final String lessThan                                        = "<";
  public  static final String greaterThan                                     = ">";
  public  static final String blank                                           = " ";
  public  static final String underline                                       = "_";
  public  static final String questionMark                                    = "?";
  public  static final String slash                                           = "/";
  public  static final String empty                                           = "";
  public  static final String ampersand                                       = "&";
  public  static final String backslashN                                      = "\n";
  public  static final String backslashR                                      = "\r";
  public  static final String backslashRBackslashN                            = "\r\n";
  public  static final String singleQuote                                     = "'";
  
  //characters encoded
  public  static final String ampersandEncoded                                = "&amp;";
  public  static final String commaEncoded                                    = "&#44;";
  
  //values misc
  public  static final String        valueThumb                               = "thumb";
  public  static final String        valueNA                                  = "NA";
  public  static final String        valueException                           = "exception";
  public  static final String        valueOp                                  = "op";
  public  static final String        valueTypes                               = "types";
  public  static final String        valueValues                              = "values";
  public  static final String        valueLastChunkSent                       = "lastChunkSent";
  public  static final String        valueObject                              = "object";
  public  static final String        valueMD5                                 = "MD5";
  public  static final String        valueDataColon                           = "data:";
  public  static final String        valueSemicolonBase64Comma                = ";base64,";
  public  static final String        valueCommaXComma                         = ",x,";
  public  static final String        valueColonColon                          = "::";
  public  static final String        valueGot                                 = "got";
  public  static final String        valueTaxonPath                           = "taxonPath";
  public  static final String        valueSource                              = "source";
  public  static final String        valueIntendedEndUserRole                 = "intendedEndUserRole";
  public  static final String        valueContext                             = "context";
  public  static final String        valueEn                                  = "en";
  public  static final String        valueTrue                                = "true";
  public  static final String        valueFalse                               = "false";
  public  static final String        valueNull                                = "null";
  public  static final String        valuePars                                = "pars";
  //  public  static final String        valueMailtoColon                         = "mailto:";
  public  static final String        valueOEORGANICEPRINTS                    = "OEORGANICEPRINTS";
  public  static final String        valueString                              = "string";
  public  static final String        valueLong                                = "long";
  public  static final String        valueMap                                 = "map";
  public  static final String        valueBoolean                             = "boolean";
  public  static final String        valueFloat                               = "float";
  public  static final String        valueInteger                             = "integer";
  public  static final String        valueDouble                              = "double";
  public  static final String        valueDotDot                              = "..";
  public  static final String        valueRoot                                = "root";
  public  static final String        valueSharedWithMeFiles                   = "SharedWithMeFiles";
  public  static final String        valueCollRootCircle                      = "collRootCircle";
  public  static final String        valueUser                                = "user";
  public  static final String        valueSystem                              = "system";
  
  public static String[] toArray(final List<String> toConverts) {
    
    if(toConverts == null){
      return new String[0];
    }
    
    return (String[]) toConverts.toArray(new String[toConverts.size()]);
  }
  
  public static String[] toArrayWithoutEmptyAndNull(final List<String> toConverts) {
    
    if(toConverts == null){
      return new String[0];
    }
    
    final List<String> result = new ArrayList<>();
    
    for(String toConvert : toConverts){
      
      if(!isEmpty(toConvert)){
        result.add(toConvert);
      }
    }
    
    return (String[]) result.toArray(new String[result.size()]);
  }
  
  public static String addTrailingSlash(final Object object){
    
    if(object == null){
      return null;
    }
      
    if(object.toString().isEmpty()){
      return slash;
    }
    
    if(object.toString().lastIndexOf(slash) == object.toString().length() - 1){
      return object.toString();
    }
    
    return object.toString() + slash;
  }
  
  public static String removeTrailingSlash(final Object object){
    
    if(object == null){
      return null;
    }
    
    if(
      object.toString().isEmpty() ||
      object.toString().lastIndexOf(slash) != object.toString().length() - 1){
      return object.toString();
    }
    
    return object.toString().substring(0, object.toString().length() - 1);
  }
  
  public static List<String> removeTrailingSlash(
    final List<? extends Object> objects){
      
    final List<String> result = new ArrayList<>();
    
    if(objects == null){
      return result;
    }
    
    for(Object object : objects){
      result.add(removeTrailingSlash(object));
    }
    
    return result;
  }
  
  public static String removeTrailingString(
    final Object object,
    final String trail) {
    
    if(object == null){
      return null;
    }
    
    if(object.toString().isEmpty()){
      return empty;
    }
    
    if(object.toString().lastIndexOf(trail) != object.toString().length() - trail.length()){
      return object.toString();
    }
    
    return object.toString().substring(0, object.toString().length() - trail.length());
  }
  
  public static Boolean contains(
    final Object object1,
    final Object object2){
    
    if(
      object1 == null ||
      object2 == null){
      return false;
    }
    
    return object1.toString().contains(object2.toString());
  }
  
  public static Boolean contains(
    final List<? extends Object>  objects,
    final Object                  objectToContain){
    
    if(objects == null){
      return false;
    }
    
    if(objectToContain == null){
      return objects.contains(null);
    }
    
    for(Object object : objects){
     
      if(equals(object, objectToContain)){
        return true;
      }
    }
    
    return false;
  }
  
  public static void remove(
    final List<? extends Object>  objects,
    final Object                  objectToRemove) throws Exception{
    
    if(objects == null){
      return;
    }
    
    if(objectToRemove == null){
      objects.remove(objectToRemove);
      return;
    }
    
    for(Object object : objects) {
      
      if(equals(object, objectToRemove)){
        
        objects.remove   (object);
        remove           (objects, objectToRemove);
        return;
      }
    }
  }
  
  public static List<String> toStr(
    final List<? extends Object> objects){
    
    final List<String> result = new ArrayList<>();
    
    if(objects == null){
      return result;
    }
    
    for(Object object : objects){
      
      if(object == null){
        result.add(null);
        continue;
      }
      
      result.add(object.toString());
    }
    
    return result;
  }
  
  public static List<String> toStrWithoutEmptyAndNull(
    final Object... objects) throws Exception{
    
    final List<String> result = new ArrayList<>();
    
    if(objects == null){
      return result;
    }
    
    for(Object object : objects){
      
      if(isEmpty(object)){
        continue;
      }
      
      result.add(object.toString());
    }
    
    return result;
  }
  
  public static String toCommaSeparatedStrNotNull(
    final List<? extends Object> objects) throws Exception{
    
    String result = new String();
    
    if(objects == null){
      return result;
    }
    
    for(Object object : objects){
      
      if(object == null){
        continue;
      }
      
      result += object.toString() + comma;
    }
    
    return removeTrailingString(result, comma);
  }
  
  public static List<String> toStrWithoutEmptyAndNull(
    final List<? extends Object> objects) throws Exception{
    
    final List<String> result = new ArrayList<>();
    
    if(objects == null){
      return result;
    }
    
    for(Object object : objects){
      
      if(isEmpty(object)){
        continue;
      }
      
      result.add(object.toString());
    }
    
    return result;
  }
  
  public static String toStr(final Object object){
    
    if(object == null){
      return null;
    }
    
    return object.toString();
  }
  
  public static String toStr(final int val){
    
    if(val == -1){
      return null;
    }
    
    return String.valueOf(val);
  }
  
  public static String removeDoubleQuotes(final String string){
    
    if(string == null){
      return null;
    }
    
    return replaceAll(string, doubleQuote, empty);
  }
  
  public static String surroundWithDoubleQuotes(final String string){
    
    if(string == null){
      return null;
    }
    
    if(
      string.length() > 1 &&
      string.substring(0, 1).equals("\"") &&
      string.substring(string.length() - 1, string.length()).equals("\"")){
      
      return string;
    }
    
    return "\"" + string + "\"";
  }
  
  public static String replaceAllBlanksSpecialCharactersDoubleDots(
    final String string, 
    final String with) {
    
    String result;
    
    if(
      isEmpty(string) ||
      with == null){
      return string;
    }
    
    result = string.replaceAll ("[\\!\\'\\\"\\-\\+\\^\\:\\,\\(\\)\\{\\}\\[\\]\\$]", SSStrU.empty);
    result = result.replaceAll ("\\.+",                                             SSStrU.dot);
    result = result.trim();
    result = result.replaceAll (" +",                                               SSStrU.blank);
    
    return result.replaceAll (SSStrU.blank, with);
  }
  
  public static String replaceAllLineFeedsWithTextualRepr(final String string){
    
    String tmp = replaceAll(string, SSStrU.backslashN, SSStrU.backSlashBackSlashN);
    
    return replaceAll(tmp, SSStrU.backslashR, SSStrU.empty);
  }
  
  public static String replaceAll(
    final String string, 
    final String find, 
    final String with){
    
    if(SSObjU.isNull(string, find, with)){
      return string;
    }
    
    return string.replace(find, with);
  }
  
  public static List<String> splitDistinctWithoutEmptyAndNull(
    final String toSplit, 
    final String splitter) throws Exception{
    
    return distinctWithoutEmptyAndNull(split(toSplit, splitter));
  }
  
  public static List<String> split(
    final String toSplit, 
    final String splitter) throws Exception{
    
    if(
      isEmpty(toSplit) ||
      isEmpty(splitter)){
      return new ArrayList<>();
    }
    
    return Arrays.asList(toSplit.split("\\Q" + splitter + "\\E"));
  }
  
  public static Boolean isEmpty(final Object object) {
    return object == null || object.toString().trim().isEmpty();
  }
  
  public static Boolean isEmpty(final Object... objects) {
    
    if(objects == null){
      return true;
    }
    
    for(Object object : objects){
      
      if(isEmpty(object)){
        return true;
      }
    }
    
    return false;
  }
  
  public static Boolean equals(
    final Object object1,
    final Object object2) {
    
    if(
      object1 == null ||
      object2 == null){
      return false;
    }
    
    return object1.toString().equals(object2.toString());
  }
  
  public static List<String> distinctWithoutNull(
    final List<? extends Object> objects) throws Exception{
    
    final List<String> result = new ArrayList<>();
    
    if(objects == null){
      return result;
    }
    
    for (Object object : objects) {
      
      if(
        object != null &&
        !result.contains(object.toString())){
        
        result.add(object.toString());
      }
    }
    
    return result;
  }
  
  public static void distinctWithoutNull2(
    final List<? extends Object> objects) throws Exception{
    
    if(objects == null){
      return;
    }
    
    final List<String> foundEntities = new ArrayList<>();
    
    for(Object object : objects){
      
      if(
        object == null ||
        foundEntities.contains(object.toString())){
        
        objects.remove(object);
        distinctWithoutNull2(objects);
        return;
      }

      foundEntities.add(object.toString());
    }
  }
  
  public static List<String> distinctWithoutEmptyAndNull(
    final List<? extends Object> objects){
    
    final List<String> result = new ArrayList<>();
    
    if(objects == null){
      return result;
    }
    
    for(Object object : objects){
      
      if(
        !isEmpty        (object) &&
        !result.contains(object.toString())){
        
        result.add(object.toString());
      }
    }
    
    return result;
  }
  
  public static List<String> distinctWithoutEmptyAndNull(
    final Object[] objects) {
    
    final List<String> result = new ArrayList<>();
    
    if(objects == null){
      return result;
    }
    
    for(Object object : objects){
      
      if(
        !isEmpty        (object) &&
        !result.contains(object.toString())){
        
        result.add(object.toString());
      }
    }
    
    return result;
  }
  
  public static void distinctWithoutEmptyAndNull2(
    final List<? extends Object> objects) {
    
    if(objects == null){
      return;
    }
    
    final List<String> foundEntities = new ArrayList<>();
    
    for(Object object : objects){
      
      if(
        isEmpty(object) ||
        foundEntities.contains(object.toString())){
        
        objects.remove(object);
        distinctWithoutEmptyAndNull2(objects);
        return;
      }

      foundEntities.add(object.toString());
    }
  }
  
  public static String removeNumbers(
    final String string){
    
    if(string == null){
      return null;
    }
    
    return string.replaceAll("[^0-9]", empty);
  }
  
  public static List<String> removeAll(
    final List<String> list, 
    final List<String> toRemove){
    
    if(list == null){
      return new ArrayList<>();
    }
    
    list.removeAll(toRemove);

    return list;
  }
  
  public static List<String> retainAll(
    final List<String> list, 
    final List<String> toRetain){
    
    if(list == null){
      return new ArrayList<>();
    }
    
    if(toRetain == null){
      return list;
    }
    
    list.retainAll(toRetain);
    
    return list;
  }
  
  public static String trim(
    final Object  object, 
    final Integer maxLength){
    
    if(object == null){
      return null;
    }
    
    String tmpString = object.toString().trim();
    
    if(tmpString.length() > maxLength){
      return tmpString.substring(0, maxLength);
    }
    
    return tmpString;
  }
  
  public static Integer[] toIntegerArray(final List<Integer> toConvert) {
		return (Integer[]) toConvert.toArray(new Integer[toConvert.size()]);
	}
}

///**
//   * @param s
//   * @return number of words existing in <code>s</code> separated with a space
//   */
//  public int countWords(final String s) {
//    String[] tmp = s.trim().split(" ");
//    int cnt = 0;
//    for (int i = 0; i < tmp.length; i++) {
//      if (tmp[i].trim().length() > 0) {
//        cnt++;
//      }
//    }
//    return cnt;
//  }

//  /**
//   * @param s
//   * @return "(" + s + ")"
//   */
//  public String addBrackets(final String s) {
//    return "(" + s + ")";
//  }

//  /**
//   * removes brackets, if existing, from the string
//   *
//   * @param s
//   * @return
//   */
//  public String removeBrackets(final String s) {
//    int i0 = s.indexOf("(");
//    int i1 = s.indexOf(")");
//    if (i0 < 0 && i1 < 0) {
//      return s;
//    }
//    if (i1 < 0) {
//      i1 = s.length();
//    }
//    return s.substring(i0 + 1, i1);
//  }

//  	/**
//	 * 0 returns distinct tag labels from SSTag[] <br>
//	 */
//	public List<String> getDistinctTagLabelsFromTagAssignments(
//			List<SSTag> tagAssignments){
//
//		List<String> result = new ArrayList<>();
//
//		for(SSTag tagAssignment : tagAssignments){
//
//			if(SSTagString.containsNot(result, tagAssignment.label)){
//				result.add(SSStrU.toStr(tagAssignment.label));
//			}
//		}
//
//		return result;
//	}

//	public List<SSColl> createWCollectionListFromWCollectionArray(
//			SSColl[] collectionArray) {
//
//		List<SSColl> result = new List<SSColl>();
//    result.addAll(Arrays.asList(collectionArray));
//
//		return result;
//	}

//	public List<SSTag> createNormalTagAssignmentListFromTagAssignmentArray(
//			SSTag[] tagAssignmentArray) {
//
//		List<SSTag> result = new List<SSTag>();
//    result.addAll(Arrays.asList(tagAssignmentArray));
//
//		return result;
//	}

//	/**
//	 * 0 converts SSTag[] to RMTagAssignment array <br>
//	 **/
//	public List<ModelTagAssignment> createTagAssignmentListFromTagAssignments(
//			SSTag[] tagAssignments) {
//
//		List<ModelTagAssignment> result = new List<ModelTagAssignment>();
//
//		for(SSTag tagAssignment : tagAssignments){
//
//			result.add(
//				new ModelTagAssignment(
//						new SSTag(tagAssignment.getTagURI(), null, null, null, tagAssignment.label, null),
//						tagAssignment.getResourceURI(),
//						tagAssignment.getAgentURI(),
//						new Date().getTime(),
//						tagAssignment.space));
//		}
//
//		return result;
//	}

//	/**
//	 * 0 converts SSUserEvent[]s to an SSUserEvent array <br>
//	 */
//	public SSUserEvent[] createEventListFromEventsList(
//			List<SSUserEvent[]> graphEvents) {
//
//		List<SSUserEvent> result = new List<SSUserEvent>();
//
//		for(SSUserEvent[] events : graphEvents){
//          result.addAll(Arrays.asList(events));
//		}
//
//		return SSUserEvent.toArray(result);
//	}

  //  public static List<String> split(
  //    String toSplit,
  //    char   splitter){
  //
  //    if(SSObjectUtils.isNull(toSplit)){
  //      return null;
  //    }
  //
  //    if(isEmpty(String.valueOf(splitter))){
  //
  //      List<String> noSplit = new ArrayList<>();
  //
  //      noSplit.add(toSplit);
  //
  //      return noSplit;
  //    }
  //
  //    return Arrays.asList(toSplit.split("\\" + splitter));
  //  }

//public static String getHashFromString(
//    final String string) throws NoSuchAlgorithmException{
//    
//    StringBuffer   stringBuffer     = new StringBuffer();
//    Formatter      stringFormatter  = new Formatter(stringBuffer);
//    MessageDigest  messageHash      = MessageDigest.getInstance(valueMD5);
//    
//    messageHash.update(string.getBytes());
//    
//    for (byte hashByte : messageHash.digest()) {
//      stringFormatter.format("%02x", hashByte);
//    }
//    
//    return stringBuffer.toString();
//  }

//  SSStrU.distinctWithoutEmptyAndNull(SSStrU.split(
//  new ArrayList<String>(new LinkedHashSet<String>(split(toSplit, splitter)));
//  Set<String> temp = new HashSet<String>(strings);
//  return Arrays.asList(temp.toArray(new String[temp.size()]));

  //  public static List<String> split(
  //    CharBuffer string,
  //    char       splitter){
  //
  //    if(SSObjectUtils.isNull(string)){
  //      return null;
  //    }
  //
  //    return split(string.toString(), splitter);
  //  }