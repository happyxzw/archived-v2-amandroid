amandroid [![Gitter](https://badges.gitter.im/sireum/amandroid.svg)](https://gitter.im/sireum/amandroid?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge) 
========================

# This repo no longer maintained, please checkout our new repo: https://github.com/arguslab/Argus-SAF

This is official reporitory for the [Amandroid](http://amandroid.sireum.org/).

This reporitory only contains android related data structures and resolvers. For the core data structures and static analysis algorithms, you can check out our [Jawa](https://github.com/sireum/jawa) reporitory.

## Repository structure

```
amandroid/
+--sireum-amandroid               Android resource parsers, information collector, decompiler, environment method builder.
+--sireum-amandroid-alir          Component based analysis, Android specific reaching facts analysis, api models, etc.
+--sireum-amandroid-cli           Amandroid command line tool modes.
+--sireum-amandroid-concurrent    Amandroid actor system.
+--sireum-amandroid-dedex         Dex file decompiler, register type resolver.
+--sireum-amandroid-run           Flexiable test ground, you can write anything here to test your code.
+--sireum-amandroid-security      Security related checking tasks, source and sink managers.
+--sireum-amandroid-serialization Serialize amandroid data structures into json format.
+--sireum-amandroid-test          Test suite.
```

## How to contribute

To contribute to the Amandroid Core, Amandroid Alir, Amandroid Dedex, Amandroid Serialization, Amandroid Concurrent, please send us a [pull request](https://help.github.com/articles/using-pull-requests/#fork--pull) from your fork of this repository!

For more information on building and developing Amandroid, please also check out our [guidelines for contributing](CONTRIBUTING.md). People who provided excellent ideas or more than 200 LOC of codes are listed in [contributor](CONTRIBUTOR.md).
 
## What to contribute

If you don't know what to contribute, please check out our [challenges need to resolve](CHALLENGE.md).

## How to build

Please checkout our [amandroid-build](https://github.com/fgwei/amandroid-build) reporitory.
