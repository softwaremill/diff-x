![diffx](https://github.com/softwaremill/diffx/raw/master/banner.png)

[![Build Status](https://img.shields.io/github/actions/workflow/status/softwaremill/diffx/main.yaml?branch=master)](https://github.com/softwaremill/diffx/actions)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.softwaremill.diffx/diffx-core_2.13/badge.svg)](https://search.maven.org/search?q=g:com.softwaremill.diffx)
[![diffx-core Scala version support](https://index.scala-lang.org/softwaremill/diffx/diffx-core/latest-by-scala-version.svg)](https://index.scala-lang.org/softwaremill/diffx/diffx-core)


[![Gitter](https://badges.gitter.im/softwaremill/diffx.svg)](https://gitter.im/softwaremill/diffx?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-brightgreen.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)
[![Documentation Status](https://readthedocs.org/projects/diffx-scala/badge/?version=latest)](https://diffx-scala.readthedocs.io/en/latest/?badge=latest)

## Documentation

diffx documentation is available at [diffx-scala.readthedocs.io](https://diffx-scala.readthedocs.io).

## Modifying documentation
The documentation is typechecked using `mdoc`. The sources for the documentation exist in `docs-sources`. Don't modify the generated documentation in `generated-docs`, as these files will get overwritten!

When generating documentation, it's best to set the version to the current one, so that the generated doc files don't include modifications with the current snapshot version.

That is, in sbt run: `set version := "0.5.0"`, before running `mdoc` in `docs`.

## Releasing a new version

```sh
$ nix develop
$ sbt release
```

## Copyright

Copyright (C) 2019 SoftwareMill [https://softwaremill.com](https://softwaremill.com).
