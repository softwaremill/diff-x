package com.softwaremill.diffx.utest

import com.softwaremill.diffx.{ShowConfig, Diff}
import utest.AssertionError

trait DiffxAssertions {

  def assertEqual[T: Diff](t1: T, t2: T)(implicit c: ShowConfig): Unit = {
    val result = Diff.compare(t1, t2)
    if (!result.isIdentical) {
      throw AssertionError(result.show(), Seq.empty, null)
    }
  }
}

object DiffxAssertions extends DiffxAssertions
