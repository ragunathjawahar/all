package xyz.ragunath.objectstreams

import arrow.core.Tuple4
import arrow.core.Tuple7
import com.google.common.truth.Truth.assertThat
import org.approvaltests.Approvals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import xyz.ragunath.objectstreams.fixtures.Age
import xyz.ragunath.objectstreams.fixtures.Coffee
import xyz.ragunath.objectstreams.fixtures.GrindSize
import xyz.ragunath.objectstreams.fixtures.Roast

class ObjectStreamTest {
  @Nested
  inner class SingleProperty {
    @Test
    fun `value objects`() {
      // given
      val agesBuilder = ObjectStream
        .of(Age::class)
        .property("value", 1, 2, 3)

      // when
      val ages = agesBuilder.generate()

      // then
      assertThat(ages)
        .containsExactly(Age(1), Age(2), Age(3))
        .inOrder()
    }

    @Test
    fun `age with one value`() {
      // given
      val ageBuilder = ObjectStream
        .of(Age::class)
        .property("value", 1)

      // when & then
      assertThat(ageBuilder.generate())
        .containsExactly(Age(1))
    }
  }

  @Nested
  inner class TwoProperties {
    @Test
    fun pairs() {
      // given
      val pairsBuilder = ObjectStream
        .of(Pair::class)
        .property("first", "A", "B")
        .property("second", 1, 2, 3)

      // when
      val pairs = pairsBuilder.generate()

      // then
      assertThat(pairs)
        .containsExactly(
          "A" to 1,
          "A" to 2,
          "A" to 3,
          "B" to 1,
          "B" to 2,
          "B" to 3,
        )
        .inOrder()
    }
  }

  @Nested
  inner class ThreeProperties {
    @Test
    fun coffee() {
      // given
      val coffeeBuilder = ObjectStream
        .of(Coffee::class)
        .property("estate", "Attikan")
        .property("roast", Roast.Light, Roast.Medium, Roast.Dark)
        .property("grindSize", GrindSize.Fine, GrindSize.MediumFine, GrindSize.Medium, GrindSize.Coarse)

      // when
      val coffee = coffeeBuilder.generate()

      // then
      assertThat(coffee)
        .containsExactly(
          Coffee("Attikan", Roast.Light, GrindSize.Fine),
          Coffee("Attikan", Roast.Light, GrindSize.MediumFine),
          Coffee("Attikan", Roast.Light, GrindSize.Medium),
          Coffee("Attikan", Roast.Light, GrindSize.Coarse),
          Coffee("Attikan", Roast.Medium, GrindSize.Fine),
          Coffee("Attikan", Roast.Medium, GrindSize.MediumFine),
          Coffee("Attikan", Roast.Medium, GrindSize.Medium),
          Coffee("Attikan", Roast.Medium, GrindSize.Coarse),
          Coffee("Attikan", Roast.Dark, GrindSize.Fine),
          Coffee("Attikan", Roast.Dark, GrindSize.MediumFine),
          Coffee("Attikan", Roast.Dark, GrindSize.Medium),
          Coffee("Attikan", Roast.Dark, GrindSize.Coarse),
        )
        .inOrder()
    }
  }

  @Nested
  inner class FourProperties {
    @Test
    fun `tuple 4`() {
      // given
      val tuple4Builder = ObjectStream
        .of(Tuple4::class)
        .property("first", 1, 2, 3, 4)
        .property("second", 'A', 'B', 'C', 'D')
        .property("third", true, false)
        .property("fourth", 1.0, 2.0, 3.0)

      // when
      val tuple4s = tuple4Builder.generate()

      // then
      Approvals.verifyAll(tuple4s.toTypedArray()) { (first, second, third, fourth) ->
        "$first, $second, $third, $fourth"
      }
    }
  }

  @Nested
  inner class AnyNumberOfProperties {
    @Test
    fun `tuple 7`() {
      // given
      val tuple7Builder = ObjectStream
        .of(Tuple7::class)
        .property("first", 1)
        .property("second", 1, 2)
        .property("third", 1, 2, 3)
        .property("fourth", 1, 2, 3, 4)
        .property("fifth", 1, 2, 3, 4, 5)
        .property("sixth", 1, 2, 3, 4, 5, 6)
        .property("seventh", 1, 2, 3, 4, 5, 6, 7)

      // when
      val tuple7s = tuple7Builder.generate()

      // then
      Approvals.verifyAll(tuple7s.toTypedArray()) { (first, second, third, fourth, fifth, sixth, seventh) ->
        "$first-$second-$third-$fourth-$fifth-$sixth-$seventh"
      }
    }
  }
}
