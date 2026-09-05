package cm.klg.notification.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class PageDataTest {

  @Test
  void shouldCreateWithElements() {
    var elements = List.of("a", "b", "c");
    var pageData = PageData.of(3L, elements);

    assertThat(pageData)
        .extracting(PageData::total, PageData::elements)
        .containsExactly(3L, elements);
  }

  @Test
  void shouldHandleEmptyList() {
    var pageData = PageData.of(0L, List.of());

    assertThat(pageData.elements()).isEmpty();
    assertThat(pageData.total()).isZero();
  }

  @Test
  void shouldStreamElements() {
    var elements = List.of(1, 2, 3, 4, 5);
    var pageData = PageData.of(5L, elements);

    assertThat(pageData.stream().filter(x -> x > 2).toList()).containsExactly(3, 4, 5);
  }

  @Test
  void shouldBeEqualWithSameValues() {
    var elements = List.of("a", "b");

    assertThat(PageData.of(2L, elements)).isEqualTo(PageData.of(2L, elements));
  }

  @Test
  void shouldNotBeEqualWithDifferentTotal() {
    var elements = List.of("a", "b");

    assertThat(PageData.of(2L, elements)).isNotEqualTo(PageData.of(10L, elements));
  }
}
