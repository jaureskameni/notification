package cm.klg.notification.utils;

import java.util.List;
import java.util.stream.Stream;

public record PageData<T>(long total, List<T> elements) {

  public static <T> PageData<T> of(long total, List<T> elements) {
    return new PageData<>(total, elements);
  }

  public Stream<T> stream() {
    return elements.stream();
  }
}
