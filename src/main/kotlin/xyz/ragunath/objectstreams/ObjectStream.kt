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

      return if (propertyCount == 1) {
        val first = properties.first()
        return first.values.map { value -> constructor.call(value) }
      } else if (propertyCount == 2) {
        val (first, second) = properties
        val twoParameters = first.values.flatMap { firstValue ->
          second.values.map { secondValue -> firstValue to secondValue }.toList()
        }
        twoParameters.map { (first, second) -> constructor.call(first, second) }
      } else if (propertyCount == 3) {
        val (first, second, third) = properties
        val twoParameters = first.values.flatMap { firstValue ->
          second.values.map { secondValue -> firstValue to secondValue }.toList()
        }
        val threeParameters = twoParameters.flatMap { firstTwoValues ->
          val (firstValue, secondValue) = firstTwoValues
          third.values.map { thirdValue -> Triple(firstValue, secondValue, thirdValue) }
        }
        threeParameters.map { (first, second, three) -> constructor.call(first, second, three) }
      } else {
        throw UnsupportedOperationException("Uh oh… we don't support streams with $propertyCount yet.")
      }
    }
  }
}
