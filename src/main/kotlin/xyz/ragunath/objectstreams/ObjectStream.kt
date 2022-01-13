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

          val finalList = start

          return finalList.newInstances(constructor)
        }

        2 -> {
          val (property1, property2) = properties

          val start = property1.values.map(::listOf)

          val a = start.productOf(property2.values)

          val finalList = a

          finalList.newInstances(constructor)
        }

        3 -> {
          val (property1, property2, property3) = properties

          val start = property1.values.map(::listOf)

          val a = start.productOf(property2.values)
          val b = a.productOf(property3.values)

          val finalList = b

          finalList.newInstances(constructor)
        }

        4 -> {
          val (property1, property2, property3, property4) = properties

          val start = property1.values.map(::listOf)

          val a = start.productOf(property2.values)
          val b = a.productOf(property3.values)
          val c = b.productOf(property4.values)

          val finalList = c

          finalList.newInstances(constructor)
        }

        else -> {
          throw UnsupportedOperationException("Uh oh… we don't support streams with $propertyCount yet.")
        }
      }
    }

    private fun List<List<Any?>>.productOf(newList: List<Any?>): List<List<Any?>> {
      return this.flatMap { accumulatedValues -> newList.map { newValue -> accumulatedValues + newValue } }
    }

    private fun List<List<Any?>>.newInstances(
      constructor: KFunction<T>
    ): List<T> {
      return map { constructor.newInstance(it) }
    }

    private fun KFunction<T>.newInstance(
      arguments: List<Any?>
    ): T {
      return call(*arguments.toTypedArray())
    }
  }
}
