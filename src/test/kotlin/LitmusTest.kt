import com.google.common.truth.Truth.assertThat
import org.approvaltests.Approvals
import org.junit.jupiter.api.Test

class LitmusTest {
  @Test
  fun `junit and truth are working`() {
    assertThat(true)
      .isTrue()
  }

  @Test
  fun `approvals is working`() {
    Approvals.verify("Hello, world!")
  }
}
