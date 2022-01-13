package xyz.ragunath.objectstreams

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class ObjectStream {
  companion object {
    fun <T> of(first: T, vararg remaining: T): List<T> {
      return listOf(first, *remaining)
    }

    fun <T : Any> of(clazz: KClass<T>): Builder<T> {
      return Builder(clazz)
    }
  }

  internal class Property<T>(val name: String, val values: List<T>)

  class Builder<T : Any>(private val clazz: KClass<T>) {
    private val properties = mutableListOf<Property<*>>()

    fun <P> property(name: String, first: P, vararg remaining: P): Builder<T> {
      properties.add(Property(name, listOf(first, *remaining)))
      return this
    }

    fun generate(): List<T> {
      val propertyCount = properties.size
      val constructor = clazz.constructors.first()

      return when (propertyCount) {
        1 -> {
          val (property1) = properties

          val start = property1.values.map(::listOf)

          return start
            .map { constructor.newInstance(it) }
        }

        2 -> {
          val (property1, property2) = properties

          val start = property1.values.map(::listOf)

          val a = start.productOf(property2.values)

          a
            .map { constructor.newInstance(it) }
        }

        3 -> {
          val (property1, property2, property3) = properties

          val start = property1.values.map(::listOf)

          val a = start.productOf(property2.values)
          val b = a.productOf(property3.values)

          b
            .map { constructor.newInstance(it) }
        }

        4 -> {
          val (property1, property2, property3, property4) = properties

          val start = property1.values.map(::listOf)

          val a = start.productOf(property2.values)
          val b = a.productOf(property3.values)
          val c = b.productOf(property4.values)

          c
            .map { constructor.newInstance(it) }
        }

        else -> {
          throw UnsupportedOperationException("Uh oh… we don't support streams with $propertyCount yet.")
        }
      }
    }

    private fun List<List<Any?>>.productOf(newList: List<Any?>): List<List<Any?>> {
      return this.flatMap { accumulatedValues -> newList.map { newValue -> accumulatedValues + newValue } }
    }

    private fun KFunction<T>.newInstance(
      arguments: List<Any?>
    ): T {
      return call(*arguments.toTypedArray())
    }
  }
}
