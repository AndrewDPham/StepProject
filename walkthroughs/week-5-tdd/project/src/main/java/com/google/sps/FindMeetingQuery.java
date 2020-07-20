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
        List<TimeRange> optionalAttendeeRelevantTimeRanges = new ArrayList<TimeRange>();

        // Add the TimeRanges of events into a List that are relevant to the request's attendees 
        for (Event event: events) {
            if (!Collections.disjoint(request.getAttendees(), event.getAttendees())) {
                attendeeRelevantTimeRanges.add(event.getWhen());
            } else if (!Collections.disjoint(request.getOptionalAttendees(), event.getAttendees())) {
                optionalAttendeeRelevantTimeRanges.add(event.getWhen());
            }
        }

        /** 
         * Check edge cases: Check if duration of request is valid and check if there are 
         * any relevant time ranges that affect the request.
         */
        if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
            return timeRanges;
        }
        if (attendeeRelevantTimeRanges.size() == 0 && optionalAttendeeRelevantTimeRanges.size() == 0) {
            timeRanges.add(TimeRange.WHOLE_DAY);
            return timeRanges;
        }

        // Sort the List so that TimeRanges are ordered by when they start
        Collections.sort(attendeeRelevantTimeRanges, TimeRange.ORDER_BY_START);
        Collections.sort(optionalAttendeeRelevantTimeRanges, TimeRange.ORDER_BY_START);

        TimeRange current;
        TimeRange toAdd;

        // Case by case basis on whether to add mandatory or optional attendees
        if (attendeeRelevantTimeRanges.size() == 0) {
            current = optionalAttendeeRelevantTimeRanges.get(0);

            // Add a TimeRange with a range from the beginning of the day until start of first event if duration is valid
            toAdd = TimeRange.fromStartEnd(TimeRange.START_OF_DAY, current.start(), false);
            if (toAdd.duration() >= request.getDuration()) {
                timeRanges.add(toAdd);
            }

            for (TimeRange timeRange: optionalAttendeeRelevantTimeRanges) {
                if (current.contains(timeRange)) {
                    continue;
                }

                if (current.overlaps(timeRange)) {
                    current = timeRange;
                    continue;
                }

                toAdd = TimeRange.fromStartEnd(current.end(), timeRange.start(), false);
                if (toAdd.duration() >= request.getDuration()) {
                    timeRanges.add(toAdd);
                }
                current = timeRange;
            }

            // Add a TimeRange with a range from the end of the latest event to the end of the day if duration is valid
            toAdd = TimeRange.fromStartEnd(current.end(), TimeRange.END_OF_DAY, true);
            if (toAdd.duration() >= request.getDuration()) {
                timeRanges.add(toAdd);
            }

        } else {
            current = attendeeRelevantTimeRanges.get(0);

            toAdd = TimeRange.fromStartEnd(TimeRange.START_OF_DAY, current.start(), false);
            if (toAdd.duration() >= request.getDuration()) {
                timeRanges.add(toAdd);
            }
            
            for (TimeRange timeRange: attendeeRelevantTimeRanges) {
                if (current.contains(timeRange)) {
                    continue;
                }

                if (current.overlaps(timeRange)) {
                    current = timeRange;
                    continue;
                }

                toAdd = TimeRange.fromStartEnd(current.end(), timeRange.start(), false);
                if (toAdd.duration() >= request.getDuration()) {
                    timeRanges.add(toAdd);
                }
                current = timeRange;
            }

            toAdd = TimeRange.fromStartEnd(current.end(), TimeRange.END_OF_DAY, true);
            if (toAdd.duration() >= request.getDuration()) {
                timeRanges.add(toAdd);
            }

            List<TimeRange> timeRangesWithNoOptionalInterference = new ArrayList<TimeRange>();

            // Goes through current timeRanges and removes meetings where optionalAttendees can't attend
            for (int i = timeRanges.size() - 1; i >= 0; i--) {
                for (TimeRange timeRange: optionalAttendeeRelevantTimeRanges) {
                    System.out.println(timeRanges.get(i).start() + " " + timeRanges.get(i).end() + " | " + timeRange.start() + " " + timeRange.end());
                    if (timeRange.overlaps(timeRanges.get(i)) || timeRange.contains(timeRanges.get(i))) {
                        timeRangesWithNoOptionalInterference.add(timeRanges.get(i));
                        System.out.println(timeRangesWithNoOptionalInterference + "HERE");
                        timeRanges.remove(timeRanges.get(i));
                        continue;
                    }
                }
            }
            
            // If all optionalAttendees are busy, return normal schedule for mandatory attendees
            if (timeRanges.size() == 0) {
                Collections.sort(timeRangesWithNoOptionalInterference, TimeRange.ORDER_BY_START);
                return timeRangesWithNoOptionalInterference;
            }

        }
        return timeRanges;
    }
}