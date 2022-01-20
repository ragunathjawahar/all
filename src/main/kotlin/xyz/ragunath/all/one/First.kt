package xyz.ragunath.all.one

class First<A, OUT>(private val creator: (A) -> OUT) {
  fun last(values: List<A>): List<OUT> {
    return values.map(creator::invoke)
  }
}
