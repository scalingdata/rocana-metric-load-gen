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

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import com.rocana.configuration.ConfigurationParser;
import com.rocana.event.Event;
import com.rocana.transform.ActionEngine;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class TestMyAction {

  private static final Logger logger = LoggerFactory.getLogger(TestMyAction.class);

  /*
   * This test mimics how the transformation engine is invoked within Rocana.
   */
  @Test
  public void testMyAction() throws IOException {
    ConfigurationParser parser = new ConfigurationParser();

    ActionEngine<Event> engine = null;

    try (InputStream inputStream = Resources.getResource("test-my-action.conf").openStream()) {
      engine = parser.parse(new InputStreamReader(inputStream), ActionEngine.class);
    }

    Iterable<Event> events = engine.transform(
      Event.newBuilder()
        .setTs(System.currentTimeMillis())
        .setEventTypeId(1234)
        .setLocation("datacenter1/rack2")
        .setHost("myhost.rocana.com")
        .setService("some-service")
        .setAttributes(Maps.<String, String>newHashMap())
        .build()
    );

    Assert.assertNotNull("Event iterable is null after transformation", events);

    List<Event> eventList = Lists.newArrayList(events);
    Assert.assertEquals(1, eventList.size());

    Assert.assertEquals("HELLO WORLD", new String(eventList.get(0).getBody().array(), Charsets.UTF_8));

    logger.debug("Output events:{}", Joiner.on(",").join(events));
  }

}