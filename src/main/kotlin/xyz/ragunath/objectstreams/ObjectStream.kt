package xyz.ragunath.objectstreams

class ObjectStream {
  companion object {
    fun <T> of(first: T, vararg remaining: T): List<T> {
      return listOf(first, *remaining)
    }
  }
}
