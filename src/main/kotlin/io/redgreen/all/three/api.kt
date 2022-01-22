package io.redgreen.all.three

import io.redgreen.all.All
import io.redgreen.all.combine

fun <A, B, C, OUT> All.Companion.of(creator: (a: A, b: B, c: C) -> OUT): First<A, B, C, OUT> {
  return First(creator)
}

class First<A, B, C, OUT>(private val creator: (a: A, b: B, c: C) -> OUT) {
  fun first(aValues: List<A>): Second<A, B, C, OUT> {
    return Second(creator, aValues)
  }
}

class Second<A, B, C, OUT>(
  private val creator: (a: A, b: B, c: C) -> OUT,
  private val aValues: List<A>
) {
  fun second(bValues: List<B>): Third<A, B, C, OUT> {
    return Third(creator, aValues, bValues)
  }
}

class Third<A, B, C, OUT>(
  private val creator: (a: A, b: B, c: C) -> OUT,
  private val aValues: List<A>,
  private val bValues: List<B>
) {
  fun last(cValues: List<C>): List<OUT> {
    return combine(aValues, listOf(bValues, cValues)) { (a, b, c) ->
      creator.invoke(a as A, b as B, c as C)
    }
  }
}
