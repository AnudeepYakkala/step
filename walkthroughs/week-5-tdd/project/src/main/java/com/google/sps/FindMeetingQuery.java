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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class FindMeetingQuery {
  /*
   * Returns the Collection of TimeRanges containing all the possible time frames to schedule
   * the request ensuring that the attandees don't have any conflicts with other events.
   */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    if (!request.getOptionalAttendees().isEmpty()) {
      List<TimeRange> requestAttendeeRangesWithOptional = filterToRequestAttendeeTimeRangesOptional(
          events, request.getAttendees(), request.getOptionalAttendees());
      Collections.sort(requestAttendeeRangesWithOptional, TimeRange.ORDER_BY_START);
      requestAttendeeRangesWithOptional = combineOverlaps(requestAttendeeRangesWithOptional);
      List<TimeRange> result =
          findMeetingRangesWithNoConflict(requestAttendeeRangesWithOptional, request.getDuration());
      if (!result.isEmpty()) {
        return result;
      } else if (request.getAttendees().isEmpty()) {
        // If there are no mandotory attendees and no non-conflicting times for optional
        // attendees, return an empty list.
        return Collections.emptyList();
      }
    }
    // If there are no times where all the optional and mandotory attendees can attend,
    // check for times where all the mandotory attendees can attend.
    List<TimeRange> requestAttendeeRanges =
        filterToRequestAttendeeTimeRanges(events, request.getAttendees());
    Collections.sort(requestAttendeeRanges, TimeRange.ORDER_BY_START);
    requestAttendeeRanges = combineOverlaps(requestAttendeeRanges);
    return findMeetingRangesWithNoConflict(requestAttendeeRanges, request.getDuration());
  }

  /*
   * Returns a List of all the events with at least one attendee from the request.
   */
  private List<TimeRange> filterToRequestAttendeeTimeRanges(
      Collection<Event> events, Collection<String> requestAttendees) {
    return events.stream()
        .filter(event -> !Collections.disjoint(event.getAttendees(), requestAttendees))
        .map(event -> event.getWhen())
        .collect(Collectors.toList());
  }

  /*
   * Returns a List of all the events with at least one attendee or one optional
   * attendee from the request.
   */
  private List<TimeRange> filterToRequestAttendeeTimeRangesOptional(Collection<Event> events,
      Collection<String> requestAttendees, Collection<String> optionalRequestAttendees) {
    return events.stream()
        .filter(event
            -> (!Collections.disjoint(event.getAttendees(), requestAttendees)
                || !Collections.disjoint(event.getAttendees(), optionalRequestAttendees)))
        .map(event -> event.getWhen())
        .collect(Collectors.toList());
  }

  /*
   * Returns a List with all the overlapping TimeRanges combined.
   */
  private List<TimeRange> combineOverlaps(List<TimeRange> ranges) {
    List<TimeRange> result = new ArrayList<>();
    for (int i = 0; i < ranges.size(); i++) {
      TimeRange currentRange = ranges.get(i);
      while (i + 1 < ranges.size() && currentRange.overlaps(ranges.get(i + 1))) {
        // Combine the two overlapping TimeRanges into a single TimeRange
        currentRange = TimeRange.fromStartEnd(currentRange.start(),
            Math.max(currentRange.end(), ranges.get(i + 1).end()),
            /* inclusive=*/false);
        i++;
      }
      result.add(currentRange);
    }
    return result;
  }

  /*
   * Returns a List containing all the TimeRanges with no conflict that are long enough for
   * the request.
   */
  private List<TimeRange> findMeetingRangesWithNoConflict(
      List<TimeRange> ranges, long requestDuration) {
    List<TimeRange> result = new ArrayList<>();
    int currentStart = TimeRange.START_OF_DAY;
    // Find all the ranges with no conflicts that are long enough for the request meeting.
    for (TimeRange meetingRange : ranges) {
      if (meetingRange.start() - currentStart >= requestDuration) {
        result.add(
            TimeRange.fromStartEnd(currentStart, meetingRange.start(), /* inclusive=*/false));
      }
      currentStart = meetingRange.end();
    }
    // Check if there is time left for the request between the last meeting and the end of the day.
    if (TimeRange.END_OF_DAY - currentStart >= requestDuration) {
      result.add(TimeRange.fromStartEnd(currentStart, TimeRange.END_OF_DAY, /* inclusive=*/true));
    }
    return result;
  }
}
