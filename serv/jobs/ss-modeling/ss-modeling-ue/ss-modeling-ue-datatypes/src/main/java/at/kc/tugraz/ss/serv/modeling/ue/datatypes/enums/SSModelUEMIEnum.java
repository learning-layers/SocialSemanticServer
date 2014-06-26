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
 package at.kc.tugraz.ss.serv.modeling.ue.datatypes.enums;

public enum SSModelUEMIEnum {

	not_changed_yet,
	no_collection_collaboration,
	in_collection_collaboration,
	a_lot_collection_collaboration,
	no_discussion_collaboration,
	in_a_discussion_collaboration,
	a_lot_discussion_collaboration,
	changed_by_lot_of_persons,
	not_changed_by_any_person,
	changed_by_at_least_one_person,
	no_tag,
	became_standard,
	tagged_a_lot,
	used_widely,
	not_used_widely,
	no_rating,
	often_rated,
	in_a_collection,
	in_many_collections,
	appears_in_many_search_results,
	viewed_a_lot,
	often_rated_high,
	reached_high_awareness,
	viewed_by_different_persons,
	shared_a_lot_within_community,
	referred_a_lot,
	standard_not_reached,
	got_no_awareness,
	viewed_by_at_least_one_person,
	tagged,
	rated,
	appears_in_search_result,
	referred,
	shared_within_community,
	was_prepared_for_audience,
	got_recommended,
	often_recommended,
	assessed,
	assessed_many_times,
	not_referred_yet,
	wasnt_prepared_for_audience_yet,
	was_prepared_for_audience_many_times,
	has_discussion,
	no_discussion,
	changed_by_adding_or_deleting_steps,
	not_changed_by_adding_or_deleting_steps,
	changed_by_many_adding_or_deleting_steps,
	not_viewed_yet,
	not_shared_with_community,
	not_organized_in_a_collection,
	organized_in_a_collection,
	often_organized_in_collections,
	selected_from_others,
	not_selected_from_others,
	often_selected_from_others,
	in_no_collection,
	not_rated_high_yet,
	rated_high,
	not_recommended_yet,
	appears_in_no_search_results,
	not_viewed_by_any_person,
	not_assessed_so_far,
	not_very_active,
	very_active,
	viewed,
	didnt_edit_a_resource_yet,
	edited_a_resource,
	edited_many_resources,
	no_contribution_to_a_discussion_yet,
	contributed_to_a_discussion,
	contributed_to_many_discussions,
	no_true_participation_in_social_network,
	particpated_in_social_network,
	particpates_frequently_in_social_network,
	has_many_entries,
	no_entries,
	has_at_least_one_entry,
	associated_with_many_persons,
	just_created,
	changed_a_lot,
	changed_at_least_one_time;
  
  public static Boolean contains(String mi){
    
    try{
      SSModelUEMIEnum.valueOf(mi);
      return true;
    }catch(Exception error){
      return false;
    }
  }
}
