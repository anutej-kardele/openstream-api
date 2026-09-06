package com.anutej.openstream_api.dto.response;

import java.time.Instant;

public record PostResponse(
                Long id,
                String content,
                Instant createdAt,
                String authorUsername,
                String authorHandle) {
}
