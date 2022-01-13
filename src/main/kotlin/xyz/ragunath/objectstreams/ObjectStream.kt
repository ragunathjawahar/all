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
            .map { (value1) -> constructor.call(value1) }
        }

        2 -> {
          val (property1, property2) = properties

          property1.values
            .flatMap { value1 -> property2.values.map { value2 -> listOf(value1, value2) } }
            .map { (value1, value2) -> constructor.call(value1, value2) }
        }

        3 -> {
          val (property1, property2, property3) = properties

          property1.values
            .flatMap { value1 -> property2.values.map { value2 -> value1 to value2 } }
            .flatMap { (value1, value2) -> property3.values.map { value3 -> listOf(value1, value2, value3) } }
            .map { (value1, value2, value3) -> constructor.call(value1, value2, value3) }
        }

        4 -> {
          val (property1, property2, property3, property4) = properties

          property1.values
            .flatMap { value1 -> property2.values.map { value2 -> value1 to value2 } }
            .flatMap { (value1, value2) -> property3.values.map { value3 -> Triple(value1, value2, value3) } }
            .flatMap { (value1, value2, value3) -> property4.values.map { fourthValue -> listOf(value1, value2, value3, fourthValue) } }
            .map { (value1, value2, value3, value4) -> constructor.call(value1, value2, value3, value4) }
        }

        else -> {
          throw UnsupportedOperationException("Uh oh… we don't support streams with $propertyCount yet.")
        }
      }
    }
  }
}
