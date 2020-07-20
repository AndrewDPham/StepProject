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
        ArrayList<TimeRange> timeRanges = new ArrayList<TimeRange>();
        List<TimeRange> attendeeRelevantTimeRanges = new ArrayList<TimeRange>();

        // Add the TimeRanges of events into a List that are relevant to the request's attendees 
        for(Event event : events){
	        if(!Collections.disjoint(request.getAttendees(), event.getAttendees())){
	            attendeeRelevantTimeRanges.add(event.getWhen());
            }
        }
        
        // Sort the List so that TimeRanges are ordered by when they start
        Collections.sort(attendeeRelevantTimeRanges, TimeRange.ORDER_BY_START);

        /** 
         * Check edge cases: Check if duration of request is valid and check if there are 
         * any relevant time ranges that affect the request.
         */
        if(attendeeRelevantTimeRanges.size() == 0){
            if(request.getDuration() > TimeRange.WHOLE_DAY.duration()){
                return timeRanges;
            }
            timeRanges.add(TimeRange.WHOLE_DAY);
            return timeRanges;
        }

        // Add a TimeRange with a range from the beginning of the day until start of first event if duration is valid
        TimeRange current = attendeeRelevantTimeRanges.get(0);
        TimeRange toAdd = TimeRange.fromStartEnd(TimeRange.START_OF_DAY, current.start(), false);
        if(toAdd.duration() >= request.getDuration()){
                timeRanges.add(toAdd);
        }

        /**  
         * Check if the current TimeRange has overlap or contains another TimeRange.
         * The current TimeRange reflects the tail of a group of overlapped & contained events. Add TimeRanges
         * where there are gaps between the tail event and the next non-overlapped & non-contained event.
         */
        for(int i = 0; i < attendeeRelevantTimeRanges.size(); i++){
            if(current.contains(attendeeRelevantTimeRanges.get(i))){
                continue;
            }

            if(current.overlaps(attendeeRelevantTimeRanges.get(i))){
                current = attendeeRelevantTimeRanges.get(i);
                continue;                
            }    

            toAdd = TimeRange.fromStartEnd(current.end(), attendeeRelevantTimeRanges.get(i).start(), false);
            if(toAdd.duration() >= request.getDuration()){
                timeRanges.add(toAdd);
            }
            current = attendeeRelevantTimeRanges.get(i);
        }

        // Add a TimeRange with a range from the end of the latest event to the end of the day if duration is valid
        toAdd = TimeRange.fromStartEnd(current.end(), TimeRange.END_OF_DAY, true);
        if(toAdd.duration() >= request.getDuration()){
            timeRanges.add(toAdd);
        }

        return timeRanges;
    }
}