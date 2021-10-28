package com.softwaremill.diffx.scalatest

import com.softwaremill.diffx.{ConsoleColorConfig, Diff}
import org.scalactic.{Prettifier, source}
import org.scalatest.Assertion
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.{MatchResult, Matcher}

trait DiffShouldMatcher {
  implicit def convertToAnyShouldMatcher[T: Diff](
      any: T
  )(implicit pos: source.Position, prettifier: Prettifier, c: ConsoleColorConfig): AnyShouldWrapper[T] =
    new AnyShouldWrapper[T](any, pos, prettifier, c, Diff[T])

  final class AnyShouldWrapper[T](
      val leftValue: T,
      val pos: source.Position,
      val prettifier: Prettifier,
      val c: ConsoleColorConfig,
      val d: Diff[T]
  ) extends Matchers {

    def shouldMatchTo(rightValue: T): Assertion = {
      Matchers.convertToAnyShouldWrapper[T](leftValue)(pos, prettifier).should(matchTo[T](rightValue)(d, c))
    }

    private def matchTo[A: Diff](right: A)(implicit c: ConsoleColorConfig): Matcher[A] = { left =>
      val result = Diff[A].apply(left, right)
      if (!result.isIdentical) {
        val diff =
          result.show().split('\n').mkString(Console.RESET, s"${Console.RESET}\n${Console.RESET}", Console.RESET)
        MatchResult(matches = false, s"Matching error:\n$diff", "")
      } else {
        MatchResult(matches = true, "", "")
      }
    }
  }
}

object DiffShouldMatcher extends DiffShouldMatcher
