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

          return property1.values
            .map { listOf(it) }
            .map { constructor.newInstance(it) }
        }

        2 -> {
          val (property1, property2) = properties

          property1.values
            .map { listOf(it) }
            .flatMap { (value1) -> property2.values.map { value2 -> listOf(value1, value2) } }
            .map { constructor.newInstance(it) }
        }

        3 -> {
          val (property1, property2, property3) = properties

          property1.values
            .map { listOf(it) }
            .flatMap { (value1) -> property2.values.map { value2 -> listOf(value1, value2) } }
            .flatMap { list2 -> property3.values.map { value3 -> list2 + value3 } }
            .map { constructor.newInstance(it) }
        }

        4 -> {
          val (property1, property2, property3, property4) = properties

          property1.values
            .map { listOf(it) }
            .flatMap { (value1) -> property2.values.map { value2 -> listOf(value1, value2) } }
            .flatMap { list2 -> property3.values.map { value3 -> list2 + value3 } }
            .flatMap { list3 -> property4.values.map { value4 -> list3 + value4 } }
            .map { constructor.newInstance(it) }
        }

        else -> {
          throw UnsupportedOperationException("Uh oh… we don't support streams with $propertyCount yet.")
        }
      }
    }

    private fun KFunction<T>.newInstance(
      arguments: List<Any?>
    ): T {
      return call(*arguments.toTypedArray())
    }
  }
}
