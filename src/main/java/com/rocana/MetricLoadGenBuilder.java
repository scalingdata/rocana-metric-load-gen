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

import com.rocana.configuration.annotations.ConfigurationProperty;
import com.rocana.transform.conf.ActionBuilder;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import java.util.regex.Pattern;

@ConfigurationProperty(name = "gen-metric-load")
public class MetricLoadGenBuilder implements ActionBuilder<MetricLoadGen> {

  private Pattern hostPattern;
  private Pattern locationPattern;
  private long numHosts = 1;
  private long numLocations = 1;
  private long numDays = 1;
  private String destination;

  @Override
  public MetricLoadGen build() {
    Preconditions.checkNotNull(hostPattern, "Parameter host-pattern is not configured!");
    Preconditions.checkNotNull(locationPattern, "Parameter location-pattern is not configured!");
    Preconditions.checkNotNull(destination, "Parameter destination is not configured!");

    // Return a new instance of MetricLoadGen.
    return new MetricLoadGen(hostPattern, locationPattern, numHosts, numLocations, numDays, destination);
  }

  public Pattern getHostPattern() {
    return hostPattern;
  }

  @ConfigurationProperty(name = "host-pattern")
  public void setHostPattern(String hostPattern) {
    this.hostPattern = Pattern.compile(hostPattern);
  }

  public Pattern getLocationPattern() {
    return locationPattern;
  }

  @ConfigurationProperty(name = "location-pattern")
  public void setLocationPattern(String locationPattern) {
    this.locationPattern = Pattern.compile(locationPattern);
  }

  public long getNumHosts() {
    return numHosts;
  }

  @ConfigurationProperty(name = "num-hosts")
  public void setNumHosts(long numHosts) {
    this.numHosts = numHosts;
  }

  public long getNumLocations() {
    return numLocations;
  }

  @ConfigurationProperty(name = "num-locations")
  public void setNumLocations(long numLocations) {
    this.numLocations = numLocations;
  }

  public long getNumDays() {
    return numDays;
  }

  @ConfigurationProperty(name = "num-days")
  public void setNumDays(long numDays) {
    this.numDays = numDays;
  }

  public String getDestination() {
    return destination;
  }

  @ConfigurationProperty(name = "destination")
  public void setDestination(String destination) {
    this.destination = destination;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
      .add("hostPattern", hostPattern)
      .add("locationPattern", locationPattern)
      .add("numHosts", numHosts)
      .add("numLocations", numLocations)
      .add("numDays", numDays)
      .add("destination", destination)
      .toString();
  }
}
