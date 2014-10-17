/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.kc.tugraz.ss.serv.serv.api;

import at.kc.tugraz.ss.datatypes.datatypes.entity.SSUri;
import java.util.List;
import java.util.Map;

public interface SSUsersResourcesGathererI{
 
   public void getUsersResources(
    final List<String>             allUsers,
    final Map<String, List<SSUri>> usersResources) throws Exception;
}
