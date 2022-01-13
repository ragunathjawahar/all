package xyz.ragunath.objectstreams

class ObjectStream {
  companion object {
    fun of(first: Int, vararg remaining: Int): List<Int> {
      return listOf(first, *remaining.toTypedArray())
    }
  }
}
