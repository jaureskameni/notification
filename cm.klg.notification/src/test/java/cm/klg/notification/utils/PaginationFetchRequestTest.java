package cm.klg.notification.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PaginationFetchRequestTest {

  @Test
  void shouldCreateWithBuilderValues() {
    var request = PaginationFetchRequest.builder().limit(10).pageIndex(0).build();

    assertThat(request)
        .extracting(PaginationFetchRequest::limit, PaginationFetchRequest::pageIndex)
        .containsExactly(10, 0);
  }

  @Test
  void shouldBeEqualWithSameValues() {
    var request1 = PaginationFetchRequest.builder().limit(10).pageIndex(0).build();
    var request2 = PaginationFetchRequest.builder().limit(10).pageIndex(0).build();

    assertThat(request1).isEqualTo(request2);
  }

  @Test
  void shouldNotBeEqualWithDifferentLimit() {
    var request1 = PaginationFetchRequest.builder().limit(10).pageIndex(0).build();
    var request2 = PaginationFetchRequest.builder().limit(20).pageIndex(0).build();

    assertThat(request1).isNotEqualTo(request2);
  }

  @Test
  void shouldNotBeEqualWithDifferentPageIndex() {
    var request1 = PaginationFetchRequest.builder().limit(10).pageIndex(0).build();
    var request2 = PaginationFetchRequest.builder().limit(10).pageIndex(1).build();

    assertThat(request1).isNotEqualTo(request2);
  }
}
