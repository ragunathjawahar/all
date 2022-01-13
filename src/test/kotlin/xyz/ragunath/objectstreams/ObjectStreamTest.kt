package xyz.ragunath.objectstreams

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ObjectStreamTest {
  @Nested
  inner class SingleParameters {
    @Test
    fun integers() {
      // given & when
      val integers = ObjectStream.of(1, 2, 3)

      // then
      assertThat(integers)
        .containsExactly(1, 2, 3)
        .inOrder()
    }

    @Test
    fun doubles() {
      // given & when
      val doubles = ObjectStream.of(1.0, 2.0, 3.0)

      // then
      assertThat(doubles)
        .containsExactly(1.0, 2.0, 3.0)
        .inOrder()
    }
  }
}
