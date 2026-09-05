package cm.klg.notification.utils;

import lombok.Builder;

@Builder
public record PaginationFetchRequest(int limit, int pageIndex) {}
