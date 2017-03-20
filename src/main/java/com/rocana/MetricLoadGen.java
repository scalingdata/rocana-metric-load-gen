/*
 * Copyright (c) 2015 Rocana.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rocana;

import com.rocana.event.Event;
import com.rocana.event.EventTypeIds;
import com.rocana.transform.Action;
import com.rocana.transform.ActionContext;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Action the amplifies input metric events by replacing part of the input hostname and location, but retaining the
 * metric's name and value.
 * </p>
 *
 * @see MetricLoadGenBuilder
 */
public class MetricLoadGen implements Action {

  private static final Logger logger = LoggerFactory.getLogger(MetricLoadGen.class);

  private Pattern hostPattern;
  private Pattern locationPattern;
  private long numHosts;
  private long numLocations;
  private String destination;

  public MetricLoadGen(Pattern hostPattern, Pattern locationPattern, long numHosts, long numLocations,
    String destination) {
    this.hostPattern = hostPattern;
    this.locationPattern = locationPattern;
    this.numHosts = numHosts;
    this.numLocations = numLocations;
    this.destination = destination;

    logger.trace("Action: MetricLoadGen. hostPattern:{} locationPattern:{} numHosts:{} numLocations:{} destination:{}",
      hostPattern, locationPattern, numHosts, numLocations, destination);
  }

  @Override
  public Status execute(ActionContext actionContext) {
    Event event = actionContext.get("rocana.input");
    List<Event> outputEvents = actionContext.get(destination);
    if (outputEvents == null) {
      outputEvents = Lists.newArrayList();
      actionContext.put(destination, outputEvents);
    }

    if (event.getEventTypeId() == EventTypeIds.HOST_METRIC_RECORD) {
      Matcher hostMatcher = hostPattern.matcher(event.getHost());
      Matcher locationMatcher = locationPattern.matcher(event.getLocation());
      if (hostMatcher.matches() && locationMatcher.matches()) {
        for (long hostNum = 1; hostNum <= numHosts; hostNum++) {
          String newHost = String.format("%s%04d%s", hostMatcher.group(1), hostNum, hostMatcher.group(3));
          for (long locationNum = 1; locationNum <= numLocations; locationNum++) {
            String newLocation = String.format("%s%04d%s", locationMatcher.group(1), locationNum,
              locationMatcher.group(3));
            Event newEvent = Event.newBuilder(event).setHost(newHost).setLocation(newLocation).build();
            outputEvents.add(newEvent);
          }
        }
      }
    }
    outputEvents.add(event);

    // Return either SUCCESS or FAILURE as appropriate.
    return Status.SUCCESS;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
      .add("hostPattern", hostPattern)
      .add("locationPattern", locationPattern)
      .add("numHosts", numHosts)
      .add("numLocations", numLocations)
      .add("destination", destination)
      .toString();
  }
}
