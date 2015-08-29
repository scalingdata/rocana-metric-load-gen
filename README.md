# Rocana Transform Action Plugin Example

This project demonstrates how to build a plugin for the Rocana transformation
engine. Plugins can perform arbitrary processing on event data within the
system.

Before you start, we assume the following:
* An intermediate knowledge of Java 7 or later.
* An intermediate understanding of Apache Maven.
* A basic understanding of JUnit.

A transformation plugin consists of two primary classes: an `Action` and an
`ActionBuilder`.

An implementation of the `Action` interface contains the logic for how to process
individual events. Actions are free to contain any kind of logic, although they
should avoid expensive calls to external services or APIs as they are executed
for _every event_ that passes through the system. See the class `MyAction` in this
project for a simple `Action` that uppercases a message and sets a context
variable other `Action`s may reference later in the transformation.

An `ActionBuilder` implementation is responsible for holding configuration
information and creating new instances of an appropriate `Action`. In almost all
cases, there is a one to one relationship between `Action`s and `ActionBuilder`s.
`ActionBuilder`s are annotated with Rocana Configuration annotations that specify
configuration property names and types. See `MyActionBuilder` in this project for
a simple example of an `ActionBuilder`.

Upon startup, the Rocana transformation engine uses the Java `ServiceLoader`
facility to find and instantiate `ActionBuilder`s. This is how `ActionBuilder`s are
found at runtime. If you're not familiar with the `ServiceLoader` facility, you
can get away with just specifying the fully qualified class name of the
`ActionBuilder` implementation in the file named `com.rocana.transform.conf.ActionBuilder`
in the `src/main/resources/META-INF/services/` directory in this project.

An example configuration that uses `MyAction` is provided in
`src/test/resources/test-my-action.conf`.

Finally, a test of the `MyAction` plugin `TestMyAction` is included. This test
mimics how Rocana components invoke the transformation engine making it suitable
to test custom `Action`s. You may customize the types of events used as inputs
to the transformation engine to test specific use cases.

## Building the Project

This project is managed by Maven.

To build the project, run its tests, and assemble its output, run:

`mvn package`

To clean any temporary build files, run:

`mvn clean`

To build the project and assemble its output, but skip tests, run:

`mvn package -DskipTests`

## License

This project is licensed under the Apache License 2.0. See the included LICENSE
file for full details.
