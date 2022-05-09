package com.galactic_groups.service.security;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.EnumMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AccessCheckResult {
    private static final AccessCheckResult ACCESS_GRANTED = new AccessCheckResult(true, null);
    private static final Map<AccessDeniedCause, AccessCheckResult> accessDeniedCache = new EnumMap<>(AccessDeniedCause.class);

    private final boolean allowed;
    private final String description;

    public static AccessCheckResult accessGranted() {
        return ACCESS_GRANTED;
    }

    public static AccessCheckResult accessDenied(@NonNull AccessDeniedCause cause) {
        return accessDeniedCache.computeIfAbsent(cause, k -> new AccessCheckResult(false, cause.getDescription()));
    }
}
