package com.github.dwickern.macros

import org.scalatest.{FunSuite, Matchers}

class NameOfTest extends FunSuite with Matchers {
  import NameOf._

  test("def") {
    def localDef = ???
    nameOf(localDef) should equal ("localDef")
  }

  test("def()") {
    def localDef() = ???
    nameOf(localDef) should equal ("localDef")
    nameOf(localDef()) should equal ("localDef")
  }

  test("val") {
    val localVal = ""
    nameOf(localVal) should equal ("localVal")
  }

  test("function") {
    def func1(x: Int): String = ???
    nameOf(func1 _) should equal ("func1")

    def func2(x: Int, y: Int): String = ???
    nameOf(func2 _) should equal ("func2")

    def func3(x: Double, y: Int, z: String): Boolean = ???
    nameOf(func3 _) should equal ("func3")
  }

  test("curried function") {
    def curried(x: Int)(y: Int): String = ???
    nameOf(curried _) should equal ("curried")
  }

  test("generic function") {
    def generic[T](x: Int): String = ???
    nameOf(generic _) should equal ("generic")
  }

  test("instance member") {
    class SomeClass(val foobar: String)
    val myClass = new SomeClass("")
    nameOf(myClass.foobar) should equal ("foobar")
  }

  test("this member") {
    new {
      private[this] val foobar = ""
      nameOf(this.foobar) should equal ("foobar")
    }
  }

  test("class member") {
    class SomeClass(val foobar: String)
    nameOf[SomeClass](_.foobar) should equal ("foobar")
    nameOf { c: SomeClass => c.foobar } should equal ("foobar")
    nameOf((_: SomeClass).foobar) should equal ("foobar")
  }

  test("object member") {
    object SomeObject {
      lazy val member = ???
    }
    nameOf(SomeObject.member) should equal ("member")
  }

  test("class") {
    class RegularClass()
    nameOfType[RegularClass] should equal ("RegularClass")
  }

  test("case class") {
    case class CaseClass()
    nameOf(CaseClass) should equal ("CaseClass")
    nameOfType[CaseClass] should equal ("CaseClass")
  }

  test("object") {
    object SomeObject
    nameOf(SomeObject) should equal ("SomeObject")
    nameOfType[SomeObject.type] should equal ("SomeObject")
  }

  test("object/class") {
    object OuterObject {
      class InnerClass()
    }
    nameOfType[OuterObject.InnerClass] should equal ("InnerClass")
  }

  test("class/class") {
    class OuterClass {
      class InnerClass()
    }
    nameOfType[OuterClass#InnerClass] should equal ("InnerClass")
  }

  test("generic class") {
    class GenericClass[T, U]()
    nameOfType[GenericClass[_, _]] should equal ("GenericClass")
  }

  test("type alias") {
    type alias = String
    nameOfType[alias] should equal ("String")
  }

  test("error: this") {
    "nameof(this)" shouldNot compile
  }

  test("error: literals") {
    "nameof(\"test\")" shouldNot compile
    "nameof(123)" shouldNot compile
    "nameof(true)" shouldNot compile
    "nameof(null)" shouldNot compile
    "nameof()" shouldNot compile
  }
}
