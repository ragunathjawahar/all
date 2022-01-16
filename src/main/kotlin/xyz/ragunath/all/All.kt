package xyz.ragunath.all

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class All {
  companion object {
    fun <T : Any> of(clazz: KClass<T>): Builder<T> {
      return Builder(clazz)
    }
  }

  internal class Property<X>(val name: String, val values: List<X>)

  class Builder<T : Any>(private val clazz: KClass<T>) {
    private val properties = mutableListOf<Property<*>>()

    fun <A> property(name: String, first: A, vararg remaining: A): Builder<T> {
      properties.add(Property(name, listOf(first, *remaining)))
      return this
    }

    fun generate(): List<T> {
      val constructor = clazz.constructors.first()

      val firstProperty = properties.first()
      val remainingProperties = properties.drop(1)

      var accumulator = firstProperty.values.map(::listOf)
      for (property in remainingProperties) {
        accumulator = accumulator.product(property.values)
      }

      return accumulator.newInstances(constructor)
    }

    private fun List<List<Any?>>.product(newList: List<Any?>): List<List<Any?>> {
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