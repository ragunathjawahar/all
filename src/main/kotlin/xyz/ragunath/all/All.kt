package xyz.ragunath.all

import kotlin.reflect.KClass

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

      return combine(
        firstProperty.values.map(::listOf),
        remainingProperties.map { it.values }
      ) { arguments -> constructor.call(*arguments.toTypedArray()) }
    }
  }
}

internal fun <T> combine(
  firstList: List<List<Any?>>,
  remainingLists: List<List<Any?>>,
  creator: (List<Any?>) -> T
): List<T> {
  var accumulator = firstList
  for (list in remainingLists) {
    accumulator = accumulator.product(list)
  }

  return accumulator.createInstances(creator)
}

private fun List<List<Any?>>.product(newList: List<Any?>): List<List<Any?>> {
  return this.flatMap { accumulatedValues ->
    newList.map { newValue -> accumulatedValues + newValue }
  }
}

private fun <T> List<List<Any?>>.createInstances(
  creator: (List<Any?>) -> T
): List<T> {
  return this.map(creator)
}
