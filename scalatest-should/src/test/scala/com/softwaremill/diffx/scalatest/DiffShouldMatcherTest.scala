package com.softwaremill.diffx.scalatest

import com.softwaremill.diffx.generic.auto._
import org.scalatest.OptionValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DiffShouldMatcherTest extends AnyFlatSpec with Matchers with DiffShouldMatcher with OptionValues {
  val right: Foo = Foo(
    Bar("asdf", 5, Map("a" -> 2)),
    List(123, 1234),
    Some(Bar("asdf", 5, Map("a" -> 2)))
  )
  val left: Foo = Foo(
    Bar("asdf", 66, Map("b" -> 3)),
    List(1234),
    Some(right)
  )

  ignore should "work" in {
    left should matchTo(right)
  }

  it should "work with option and some" in {
    Option("test") should matchTo(Some("test"))
  }
}
sealed trait Parent
case class Bar(s: String, i: Int, ss: Map[String, Int]) extends Parent
case class Foo(bar: Bar, b: List[Int], parent: Option[Parent]) extends Parent