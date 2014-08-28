/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.kc.tugraz.ss.activity.datatypes.ret;

import at.kc.tugraz.socialserver.utils.SSMethU;
import at.kc.tugraz.socialserver.utils.SSStrU;
import at.kc.tugraz.socialserver.utils.SSVarU;
import at.kc.tugraz.ss.activity.datatypes.SSActivity;
import at.kc.tugraz.ss.serv.datatypes.SSServRetI;
import at.kc.tugraz.ss.serv.jsonld.util.SSJSONLDU;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SSActivitiesUserGetRet extends SSServRetI{
 
  public List<SSActivity> activities = new ArrayList<>();

  public static SSActivitiesUserGetRet get(final List<SSActivity> activities, final SSMethU op){
    return new SSActivitiesUserGetRet(activities, op);
  }
  
  private SSActivitiesUserGetRet(final List<SSActivity> activities, final SSMethU op) {

    super(op);
    
    if(activities != null){
      this.activities.addAll(activities);
    }
  }

  @Override
  public Map<String, Object> jsonLDDesc(){
    
    final Map<String, Object> ld              = new HashMap<>();
    final Map<String, Object> activitiesObj   = new HashMap<>();
    
    activitiesObj.put(SSJSONLDU.id,        SSVarU.sss + SSStrU.colon + SSActivity.class.getName());
    activitiesObj.put(SSJSONLDU.container, SSJSONLDU.set);
    
    ld.put(SSVarU.activities, activitiesObj);
    
    return ld;
  }
  
  public List<SSActivity> getActivities() {
    return activities;
  }
}