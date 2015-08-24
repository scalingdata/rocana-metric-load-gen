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

import com.rocana.transform.Action;
import com.rocana.transform.ActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * <p>
 * Sample Rocana {@link Action} implementation that places a configurable
 * uppercase message into the transformation's {@link ActionContext}.
 * </p>
 *
 * @see MyActionBuilder
 */
public class MyAction implements Action {

  private static final Logger logger = LoggerFactory.getLogger(MyAction.class);

  private String message;

  public MyAction(String message) {
    this.message = message;
  }

  @Override
  public Status execute(ActionContext actionContext) {
    /*
     * By convention, Actions log at TRACE to facilitate debugging of
     * transformations in offline settings.
     */
    logger.trace("Action: MyAction. message:{}", message);

    /*
     * Set the context variable `myaction.message` to a ByteBuffer containing
     * the configured message. The use of a ByteBuffer here is because our
     * example uses this variable for the event body which is always a
     * ByteBuffer. If we instead wanted to set an attribute, we could use a
     * string.
     *
     * Subsequent actions can access this variable for further processing.
     */
    actionContext.put("myaction.message", ByteBuffer.wrap(message.toUpperCase().getBytes()));

    // Return either SUCCESS or FAILURE as appropriate.
    return Status.SUCCESS;
  }

}
