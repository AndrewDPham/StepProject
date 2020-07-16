
// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class FindMeetingQuery {
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        // throw new UnsupportedOperationException("TODO: Implement this method.");

        Collection<TimeRange> timeRanges = new ArrayList<TimeRange>();

        List<TimeRange> attendeeRelevantTimeRanges = new ArrayList<TimeRange>();
        Collection<String> requestAttendees = request.getAttendees(); 
        Collection<String> eventAttendees;

        for(Event event : events){
	        eventAttendees = event.getAttendees(); 
	        if(!Collections.disjoint(requestAttendees, eventAttendees)){
	            attendeeRelevantTimeRanges.add(event.getWhen());
            }
        }
        
        Collections.sort(attendeeRelevantTimeRanges, TimeRange.ORDER_BY_START);

        if(attendeeRelevantTimeRanges.size() == 0){
            if(request.getDuration() > TimeRange.WHOLE_DAY.duration()){
                return timeRanges;
            }
            timeRanges.add(TimeRange.WHOLE_DAY);
            return timeRanges;
        }

        //Add from the beginning of the day until start of first meeting if duration is valid
        TimeRange current = attendeeRelevantTimeRanges.get(0);
        TimeRange toAdd = TimeRange.fromStartEnd(TimeRange.START_OF_DAY, current.start(), false);
        if(toAdd.duration() >= request.getDuration()){
                timeRanges.add(toAdd);
        }
        
        
        

        return timeRanges;
    }
}

        

	        // if( i == 0 ){
		    //     timeRanges.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY, attendeeRelevantTimeRanges.get(0).start(), false));
	        // }
 
	        // if( i == attendeeRelevantTimeRanges.size() - 1){
		    //     timeRanges.add(TimeRange.fromStartEnd(attendeeRelevantTimeRanges.get(attendeeRelevantTimeRanges.size() - 1).end(), TimeRange.END_OF_DAY, true));
            // }




//   Event Schema:
//   private final String title;
//   private final TimeRange;
//   private final Set<String> attendees = new HashSet<>();
 
//   MeetingRequest Schema:
//   private final Collection<String> attendees = new HashSet<>();
//   private final Collection<String> optional_attendees = new HashSet<>();
//   private final long duration;
 
//   TimeRange Schema:
//   private final int start;
//   private final int duration;
 
// Check for conflict;



/*
    Iterate through events
    

    Add time range
*/