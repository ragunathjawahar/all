package io.redgreen.all.five

import io.redgreen.all.All
import io.redgreen.all.combine

fun <A, B, C, D, E, OUT> All.Companion.of(
  creator: (a: A, b: B, c: C, d: D, e: E) -> OUT
): First<A, B, C, D, E, OUT> {
  return First(creator)
}

class First<A, B, C, D, E, OUT>(private val creator: (a: A, b: B, c: C, d: D, e: E) -> OUT) {
  fun first(aValues: List<Int>): Second<A, B, C, D, E, OUT> {
    return Second(creator, aValues)
  }
}

class Second<A, B, C, D, E, OUT> constructor(
  private val creator: (a: A, b: B, c: C, d: D, e: E) -> OUT,
  private val aValues: List<Int>
) {
  fun second(bValues: List<Int>): Third<A, B, C, D, E, OUT> {
    return Third(creator, aValues, bValues)
  }
}

class Third<A, B, C, D, E, OUT>(
  private val creator: (a: A, b: B, c: C, d: D, e: E) -> OUT,
  private val aValues: List<Int>,
  private val bValues: List<Int>
) {
  fun third(cValues: List<Int>): Fourth<A, B, C, D, E, OUT> {
    return Fourth(creator, aValues, bValues, cValues)
  }
}

class Fourth<A, B, C, D, E, OUT>(
  private val creator: (a: A, b: B, c: C, d: D, e: E) -> OUT,
  private val aValues: List<Int>,
  private val bValues: List<Int>,
  private val cValues: List<Int>
) {
  fun fourth(dValues: List<Int>): Fifth<A, B, C, D, E, OUT> {
    return Fifth(creator, aValues, bValues, cValues, dValues)
  }
}

class Fifth<A, B, C, D, E, OUT>(
  private val creator: (a: A, b: B, c: C, d: D, e: E) -> OUT,
  private val aValues: List<Int>,
  private val bValues: List<Int>,
  private val cValues: List<Int>,
  private val dValues: List<Int>
) {
  fun last(eValues: List<E>): List<OUT> {
    return combine(aValues, listOf(bValues, cValues, dValues, eValues)) { (a, b, c, d, e) ->
      creator.invoke(a as A, b as B, c as C, d as D, e as E)
    }
  }
}
