package xyz.ragunath.objectstreams

import arrow.core.Tuple4
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
          val (first) = properties
          return first.values.map { value -> constructor.call(value) }
        }
        2 -> {
          val (first, second) = properties
          val twoParameters = first.values.flatMap { firstValue ->
            second.values.map { secondValue -> firstValue to secondValue }.toList()
          }
          twoParameters.map { (first, second) -> constructor.call(first, second) }
        }
        3 -> {
          val (first, second, third) = properties
          val twoParameters = first.values.flatMap { firstValue ->
            second.values.map { secondValue -> firstValue to secondValue }.toList()
          }
          val threeParameters = twoParameters.flatMap { firstTwoValues ->
            val (firstValue, secondValue) = firstTwoValues
            third.values.map { thirdValue -> Triple(firstValue, secondValue, thirdValue) }
          }
          threeParameters.map { (first, second, three) -> constructor.call(first, second, three) }
        }
        4 -> {
          val (first, second, third, fourth) = properties
          val twoParameters = first.values.flatMap { firstValue ->
            second.values.map { secondValue -> firstValue to secondValue }.toList()
          }
          val threeParameters = twoParameters.flatMap { firstTwoValues ->
            val (firstValue, secondValue) = firstTwoValues
            third.values.map { thirdValue -> Triple(firstValue, secondValue, thirdValue) }
          }
          val fourParameters = threeParameters.flatMap { triple ->
            val (firstValue, secondValue, thirdValue) = triple
            fourth.values.map { fourthValue -> Tuple4(firstValue, secondValue, thirdValue, fourthValue) }
          }
          fourParameters.map { (first, second, third, fourth) -> constructor.call(first, second, third, fourth) }
        }
        else -> {
          throw UnsupportedOperationException("Uh oh… we don't support streams with $propertyCount yet.")
        }
      }
    }
  }
}
