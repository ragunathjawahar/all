package io.redgreen.all

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

      val firstList = properties.first().values
      val remainingLists = properties.drop(1).map { it.values }

      return combine(firstList, remainingLists) { arguments ->
        constructor.call(*arguments.toTypedArray())
      }
    }
  }
}

internal fun <T> combine(
  firstList: List<Any?> = emptyList(),
  remainingLists: List<List<Any?>>,
  creator: (List<Any?>) -> T
): List<T> {
  var accumulator = firstList.map(::listOf)
  for (list in remainingLists) {
    accumulator = accumulator.product(list)
  }
  return accumulator.map(creator)
}

private fun List<List<Any?>>.product(newList: List<Any?>): List<List<Any?>> {
  return this.flatMap { accumulatedValues ->
    newList.map { newValue -> accumulatedValues + newValue }
  }
}
