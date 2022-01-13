package xyz.ragunath.objectstreams

import kotlin.reflect.KClass

class ObjectStream {
  companion object {
    fun <T> of(first: T, vararg remaining: T): List<T> {
      return listOf(first, *remaining)
    }

    fun <T : Any> of(clazz: KClass<T>): Builder<T> {
      return Builder(clazz)
    }
  }

  internal class Property(val name: String, val values: List<Int>)

  class Builder<T : Any>(private val clazz: KClass<T>) {
    private val properties = mutableListOf<Property>()

    fun property(name: String, first: Int, vararg remaining: Int): Builder<T> {
      properties.add(Property(name, listOf(first, *remaining.toTypedArray())))
      return this
    }

    fun generate(): List<T> {
      val first = properties.first()
      val constructor = clazz.constructors.first()
      return first.values.map { value -> constructor.call(value) }
    }
  }
}
