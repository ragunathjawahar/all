package io.redgreen.all.four

import io.redgreen.all.All
import io.redgreen.all.combine

fun <A, B, C, D, OUT> All.Companion.of(
  creator: (a: A, b: B, c: C, d: D) -> OUT
): First<A, B, C, D, OUT> {
  return First(creator)
}

class First<A, B, C, D, OUT>(private val creator: (a: A, b: B, c: C, d: D) -> OUT) {
  fun first(aValues: List<A>): Second<A, B, C, D, OUT> {
    return Second(creator, aValues)
  }
}

class Second<A, B, C, D, OUT>(
  private val creator: (a: A, b: B, c: C, d: D) -> OUT,
  private val aValues: List<A>
) {
  fun second(bValues: List<B>): Third<A, B, C, D, OUT> {
    return Third(creator, aValues, bValues)
  }
}

class Third<A, B, C, D, OUT>(
  private val creator: (a: A, b: B, c: C, d: D) -> OUT,
  private val aValues: List<A>,
  private val bValues: List<B>
) {
  fun third(cValues: List<C>): Fourth<A, B, C, D, OUT> {
    return Fourth(creator, aValues, bValues, cValues)
  }
}

class Fourth<A, B, C, D, OUT>(
  private val creator: (a: A, b: B, c: C, d: D) -> OUT,
  private val aValues: List<A>,
  private val bValues: List<B>,
  private val cValues: List<C>
) {
  fun last(dValues: List<Char>): List<OUT> {
    return combine(aValues, listOf(bValues, cValues, dValues)) { (a, b, c, d) ->
      creator(a as A, b as B, c as C, d as D)
    }
  }
}
