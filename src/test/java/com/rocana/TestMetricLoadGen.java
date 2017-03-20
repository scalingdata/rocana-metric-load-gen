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

import com.rocana.configuration.ConfigurationParser;
import com.rocana.event.Event;
import com.rocana.event.EventTypeIds;
import com.rocana.transform.ActionEngine;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class TestMetricLoadGen {

  private static final Logger logger = LoggerFactory.getLogger(TestMetricLoadGen.class);

  /*
   * This test mimics how the transformation engine is invoked within Rocana.
   */
  @Test
  public void testMetricLoadGen() throws IOException {
    ConfigurationParser parser = new ConfigurationParser();

    ActionEngine<Event> engine = null;

    try (InputStream inputStream = Resources.getResource("test-gen-metric-load.conf").openStream()) {
      engine = parser.parse(new InputStreamReader(inputStream), ActionEngine.class);
    }
    Event input = Event.newBuilder()
      .setTs(System.currentTimeMillis())
      .setEventTypeId(EventTypeIds.HOST_METRIC_RECORD)
      .setLocation("dc1/rack2")
      .setHost("example-dn01.int.example.com")
      .setService("")
      .setAttributes(ImmutableMap.of("m.rocana.host.cpu.1.perc_idle", "8.756|g"))
      .build();

    Iterable<Event> events = engine.transform(input);

    Assert.assertNotNull("Event iterable is null after transformation", events);

    List<Event> eventList = Lists.newArrayList(events);
    Assert.assertEquals(5, eventList.size());

    Assert.assertEquals("example-dn0001.int.example.com", eventList.get(0).getHost());
    Assert.assertEquals("dc1/rack0001", eventList.get(0).getLocation());
    Assert.assertEquals("8.756|g", eventList.get(0).getAttributes().get("m.rocana.host.cpu.1.perc_idle"));

    Assert.assertEquals("example-dn0001.int.example.com", eventList.get(1).getHost());
    Assert.assertEquals("dc1/rack0002", eventList.get(1).getLocation());
    Assert.assertEquals("8.756|g", eventList.get(1).getAttributes().get("m.rocana.host.cpu.1.perc_idle"));

    Assert.assertEquals("example-dn0002.int.example.com", eventList.get(2).getHost());
    Assert.assertEquals("dc1/rack0001", eventList.get(2).getLocation());
    Assert.assertEquals("8.756|g", eventList.get(2).getAttributes().get("m.rocana.host.cpu.1.perc_idle"));

    Assert.assertEquals("example-dn0002.int.example.com", eventList.get(3).getHost());
    Assert.assertEquals("dc1/rack0002", eventList.get(3).getLocation());
    Assert.assertEquals("8.756|g", eventList.get(3).getAttributes().get("m.rocana.host.cpu.1.perc_idle"));

    Assert.assertEquals("example-dn01.int.example.com", eventList.get(4).getHost());
    Assert.assertEquals("dc1/rack2", eventList.get(4).getLocation());
    Assert.assertEquals("8.756|g", eventList.get(4).getAttributes().get("m.rocana.host.cpu.1.perc_idle"));

    logger.debug("Output events:{}", Joiner.on(",").join(events));
  }

}