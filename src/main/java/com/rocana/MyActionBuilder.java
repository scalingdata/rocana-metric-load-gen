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

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.rocana.configuration.annotations.ConfigurationCollection;
import com.rocana.configuration.annotations.ConfigurationProperty;
import com.rocana.transform.conf.ActionBuilder;

import java.util.List;

/**
 * <p>
 * An {@link ActionBuilder} responsible for receiving configuration information
 * and creating instances of an {@link com.rocana.transform.Action}
 * implementation.
 * </p>
 *
 * @see MyAction
 * @see ActionBuilder
 */
/*
 * The ConfigurationProperty annotation indicates the name of the action that
 * should appear in the configuration file. ConfigurationProperty annotations
 * on setter methods are used to configure properties of the action. The type
 * of the setter's argument indicates how the value is to be interpreted. The
 * following types exist:
 *
 * Java Type -> Configuration Type
 * =========    ==================
 * String -> "A string"
 * Integer -> 123
 * Long -> 123L
 * Float -> 0.123
 * Double -> 0.123
 * Map<String, T> -> { key: valueOfTypeT, ... }
 * List<T> -> [ valueOfTypeT, ... }
 *
 * For complex types - Maps and Lists - an additional ConfigurationCollection
 * annotation is required so the configuration library understands the value
 * types. This is because Java generics are not reified. That is, the type of
 * T is erased at runtime, so it's not possible for the configuration library
 * to inspect the expected generic type. Collection values may contain complex
 * types as well, however this example is not shown here.
 */
@ConfigurationProperty(name = "my-action")
public class MyActionBuilder implements ActionBuilder<MyAction> {

  private String message;
  private List<Integer> numbers;

  @Override
  public MyAction build() {
    Preconditions.checkNotNull(message, "Parameter message is not configured!");

    // Return a new instance of MyAction.
    return new MyAction(message);
  }

  public String getMessage() {
    return message;
  }

  @ConfigurationProperty(name = "message")
  public void setMessage(String message) {
    this.message = message;
  }

  public List<Integer> getNumbers() {
    return numbers;
  }

  /*
   * This property isn't used. It's just here to demonstrate how to bind lists.
   */
  @ConfigurationProperty(name = "numbers")
  @ConfigurationCollection(elementType = Integer.class)
  public void setNumbers(List<Integer> numbers) {
    this.numbers = numbers;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
      .add("message", message)
      .add("numbers", numbers)
      .toString();
  }

}
