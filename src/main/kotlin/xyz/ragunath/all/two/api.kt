package xyz.ragunath.all.two

import xyz.ragunath.all.All
import xyz.ragunath.all.combine

fun <A, B, OUT> All.Companion.of(creator: (A, B) -> OUT): First<A, B, OUT> {
  return First(creator)
}

class First<A, B, OUT>(private val creator: (A, B) -> OUT) {
  fun first(aValues: List<A>): Second<A, B, OUT> {
    return Second(creator, aValues)
  }
}

class Second<A, B, OUT>(
  private val creator: (A, B) -> OUT,
  private val aValues: List<A>
) {
  fun last(bValues: List<B>): List<OUT> {
    return combine(aValues, listOf(bValues)) { (a, b) ->
      creator.invoke(a as A, b as B)
    }
  }
}
