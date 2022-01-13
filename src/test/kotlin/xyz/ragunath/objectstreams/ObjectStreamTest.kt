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
      val intStream = ObjectStream.of(1, 2, 3)

      // then
      assertThat(intStream)
        .containsExactly(1, 2, 3)
        .inOrder()
    }
  }
}
