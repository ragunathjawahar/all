package io.redgreen.all.one

import io.redgreen.all.All

fun <A, OUT> All.Companion.of(creator: (A) -> OUT): First<A, OUT> {
  return First(creator)
}

class First<A, OUT>(private val creator: (A) -> OUT) {
  fun last(aValues: List<A>): List<OUT> {
    return aValues.map(creator::invoke)
  }
}
