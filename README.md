# Trickle

A Truffle Clojure Interpreter.

Currently just playing around to learn more about Truffle. It might turn into
something more useful in the future, but no promises!

## Build

Prerequisites:

- [babashka](https://github.com/babashka/babashka#installation)
- [GraalVM](https://www.graalvm.org/)
- Set `GRAALVM_HOME` to the GraalVM home dir, e.g. `/Users/borkdude/Downloads/graalvm-ce-java11-21.1.0/Contents/Home`.
- Set `JAVA_CMD` to `$GRAALVM_HOME/bin/java` to use GraalVM for Java and Clojure compilation.

Then run `bb tasks` to see what you can do in this project:

``` text
The following tasks are available:

compile-java Compiles Java sources
compile-main Compiles trickle.main
run-main     Runs main
native-image Builds native image
clean        Removes classes and target dir
```

## License

Copyright © 2021 Michiel Borkent

Distributed under the EPL License. See LICENSE.
