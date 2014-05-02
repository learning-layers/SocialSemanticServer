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
package at.kc.tugraz.socialserver.utils;

import java.nio.*;
import java.security.*;
import java.util.*;

public class SSStrU{

  private SSStrU(){}
  
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
  
  //characters encoded
  public  static final String ampersandEncoded                                = "&amp;";
  public  static final String commaEncoded                                    = "&#44;";
  
  //values misc
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
  public  static final String        valueAnd                                 = "and";
  public  static final String        valueOr                                  = "or";
  public  static final String        valueCommaXComma                         = ",x,";
  public  static final String        valueColonColon                          = "::";
  public  static final String        valueGot                                 = "got";
  public  static final String        valueFinished                            = "finished";
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
  
  public static int lastIndexOf(final String string, final String what){
    
    if(SSObjU.isNull(string, what)){
      return -1;
    }
    
    return string.lastIndexOf(what);
  }
  
  public static String addTrailingSlash(final String string){
    
    if(
      string == null ||
      string.lastIndexOf(slash) == string.length() - 1){
      return string;
    }
    
    return string + slash;
  }
  
  public static String removeTrailingSlash(final String string){
    
    if(
      isEmpty(string)  ||
      string.lastIndexOf(slash) != string.length() - 1){
      return string;
    }
    
    return string.substring(0, string.length() - 1);
  }
  
  public static String removeTrailingString(final String string, final String trail) {
    
    if(
      isEmpty(string)  ||
      string.lastIndexOf(trail) != string.length() - trail.length()){
      return string;
    }
    
    return string.substring(0, string.length() - trail.length());
  }
  
  public static String addAtBegin(final String string, final String add){
    
    if(
      SSObjU.isNull(string)        ||
      isEmpty(add)                 ||
      string.startsWith(add)){
      return string;
    }
    
    return add + string;
  }
  
  public static String addAtEnd(final String string, final String add){
    
    if(
      SSObjU.isNull(string) ||
      isEmpty(add)                 ||
      string.substring(string.length() - 1, string.length()).equals(add)){
      return string;
    }
    
    return string + add;
  }
  
  public static String removeStringFromBegin(final String string, final String begin){
    
    if(
      isEmpty (string) ||
      isEmpty (begin)  ||
      !string.startsWith(begin)){
      return string;
    }
    
    return string.substring(begin.length(), string.length());
  }
  
  public static boolean hasNotLength(final List<String> list, final int desiredLength) throws Exception{
    
    return !hasLength(list, desiredLength);
  }
  
  public static boolean hasLength(
    final List<String> list,
    final int          desiredLength) throws Exception{
    
    if(
      SSObjU.isNull(list) ||
      list.size() != desiredLength){
      return false;
    }
    
    return true;
  }
  
  public static boolean hasNotLengthAtLeast(
    final List<String> list,
    final int          desiredLength) throws Exception{
    
    return !hasLengthAtLeast(list, desiredLength);
  }
  
  public static boolean hasLengthAtLeast(
    final List<String> list,
    final int          desiredLength) throws Exception{
    
    if(
      SSObjU.isNull(list) ||
      list.size() < desiredLength){
      return false;
    }
    
    return true;
  }
  
  public static String getHashFromString(
    final String string) throws NoSuchAlgorithmException{
    
    StringBuffer   stringBuffer     = new StringBuffer();
    Formatter      stringFormatter  = new Formatter(stringBuffer);
    MessageDigest  messageHash      = MessageDigest.getInstance(valueMD5);
    
    messageHash.update(string.getBytes());
    
    for (byte hashByte : messageHash.digest()) {
      stringFormatter.format("%02x", hashByte);
    }
    
    return stringBuffer.toString();
  }
  
  public static boolean containsNot(
    final List<String> list,
    final String       value){
    
    return !contains(list, value);
  }
  
  public static boolean containsNot(
    final List<Integer> list,
    final Integer       integer){
    
    return !contains(list, integer);
  }
  
  public static boolean containsNot(
    final String       string1,
    final String       string2){
    
    return !contains(string1, string2);
  }
  
  public static boolean contains(
    final List<String> list,
    final String       value){
    
    if(
      SSObjU.isNull(list) ||
      SSObjU.isNull(value)){
      return false;
    }
    
    return list.contains(value);
  }
  
  public static boolean contains(
    final String       string1,
    final String       string2){
    
    if(
      SSObjU.isNull(string1) ||
      SSObjU.isNull(string2)){
      return false;
    }
    
    return string1.contains(string2);
  }
  
  public static boolean contains(
    final List<Integer> list,
    final Integer       integer){
    
    if(
      SSObjU.isNull(list) ||
      SSObjU.isNull(integer)){
      return false;
    }
    
    return list.contains(integer);
  }
  
  public static String toString(final Object object){
    
    if(object == null){
      return null;
    }
    
    return object.toString();
  }
  
  public static String toString(final int val){
    
    if(val == -1){
      return null;
    }
    
    return String.valueOf(val);
  }
  
  public static String removeDoubleQuotes(final String string){
    
    if(SSObjU.isNull(string)){
      return null;
    }
    
    return replace(string, doubleQuote, empty);
  }
  
  public static String surroundWithDoubleQuotes(final String string){
    
    if(SSObjU.isNull(string)){
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
  
  public static String replaceBlanksSpecialCharactersDoubleDots(final String string, String with) {
    
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
  
  public static String replaceLineFeedsWithTextualRepr(final String string){
    
    String tmp = replace(string, SSStrU.backslashN, SSStrU.backSlashBackSlashN);
    
    return replace(tmp, SSStrU.backslashR, SSStrU.empty);
  }
  
  public static String replace(final String string, final String find, final String with){
    
    if(SSObjU.isNull(string, find, with)){
      return string;
    }
    
    return string.replace(find, with);
  }
  
  public static List<String> distinct(
    final List<String> strings) {
    
    final List<String> result = new ArrayList<String>();
    
    if(strings == null){
      return result;
    }
    
    for(String string : strings){
      
      if(
        !isEmpty     (string) &&
        !contains    (result, string)){
        
        result.add(string);
      }
    }
    
    return result;
  }
  
  public static String strFromList(List<String> list) {
    
    String result = SSStrU.empty;
    
    for(String string : list){
      result += string + SSStrU.comma;
    }
    
    return removeTrailingString(result, SSStrU.comma);
  }

  public static List<String> splitDistinct(
    final String toSplit, 
    final String splitter) throws Exception{
    
    return new ArrayList<String>(new LinkedHashSet<String>(split(toSplit, splitter)));
  }
  
  public static List<String> split(
    final String toSplit, 
    final String splitter) throws Exception{
    
    if(isEmpty(splitter)){
      SSLogU.errThrow(new Exception("cannot split distinct on null or empty splitter"));
      return null;
    }
    
    if(isEmpty(toSplit)){
      return new ArrayList<String>();
    }
    
    return Arrays.asList(toSplit.split("\\Q" + splitter + "\\E"));
  }
  
  public static boolean isNotEmpty(final String string){
    return !isEmpty(string);
  }
  
  /**
   * checks if a string is empty (null, "", " ")
   *
   * @param s
   * @return
   */
  public static boolean isEmpty(final String s) {
    
    if(
      s == null ||
      s.trim().isEmpty()) {
      return true;
    }
    
    return false;
  }
  
  public static boolean isEmpty(final String... s) {
    
    for (String a : s) {
      if (isEmpty(a)) {
        return true;
      }
    }
    
    return false;
  }
  
  public static String removeSlashFromEnd(final String string) {
    if (isEmpty(string)) {
      return string;
    }
    
    if (string.endsWith("/")) {
      return string.substring(0, string.length() - 1);
    }
    
    return string;
  }
  
  public static boolean equals(
    final String string1,
    final String string2) {
    
    if(
      SSObjU.isNull(string1) ||
      SSObjU.isNull(string2)){
      return false;
    }
    
    if (string1.equals(string2)) {
      return true;
    }
    
    return false;
  }
  
  public static boolean notEquals(
    final String string1,
    final String string2){
    
    return !equals(string1, string2);
  }
  
  public static int length(String string) {
    
    if(string == null) {
      return -1;
    }
    
    return string.length();
  }
  
  public static String subString(final String string, final  int from, final  int to) {
    
    if(
      isEmpty(string)              ||
      SSNumberU.isLess(from, 0)   ||
      SSNumberU.isLess(string.length(), to)){
      return null;
    }
    
    return string.substring(from, to);
  }
  
  public static String[] toStringArray(final Collection<String> toConvert) {
    return (String[]) toConvert.toArray(new String[toConvert.size()]);
  }
  
  public static boolean startsWith(final String string, final String prefix){
    
    if(
      isEmpty(string) ||
      isEmpty(prefix)){
      return false;
    }
    
    return string.startsWith(prefix);
  }
  
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
  
  public static List<String> split(final CharBuffer string, final String splitter) throws Exception{
    
    return split(string.toString(), splitter);
  }
  
  public static String filterNumbers(
    final String string){
    
    if(SSObjU.isNull(string)){
      return null;
    }
    
    return string.replaceAll("[^0-9]", empty);
  }
  
  public static List<String> getDistinct(final List<String> strings) {
    
    if(SSObjU.isNull(strings)){
      return strings;
    }
    
    Set<String> temp = new HashSet<String>(strings);
    
    return Arrays.asList(temp.toArray(new String[temp.size()]));
  }
  
  public static List<String> removeAll(
    final List<String> list, 
    final List<String> toRemove){
    
    if(
      list     == null ||
      toRemove == null){
      return new ArrayList<String>();
    }
    
    list.removeAll(toRemove);

    return list;
  }
  
  public static List<String> retainAll(
    final List<String> list, 
    final List<String> toRetain){
    
    if(
      list     == null ||
      toRetain == null){
      return new ArrayList<String>();
    }
    
    list.retainAll(toRetain);
    return list;
    
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
//		List<String> result = new ArrayList<String>();
//
//		for(SSTag tagAssignment : tagAssignments){
//
//			if(SSTagString.containsNot(result, tagAssignment.label)){
//				result.add(SSStrU.toString(tagAssignment.label));
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
  //      List<String> noSplit = new ArrayList<String>();
  //
  //      noSplit.add(toSplit);
  //
  //      return noSplit;
  //    }
  //
  //    return Arrays.asList(toSplit.split("\\" + splitter));
  //  }