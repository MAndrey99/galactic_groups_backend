package com.galactic_groups.utils;

import com.galactic_groups.data.view.UserRole;
import lombok.NonNull;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Objects;
import java.util.Set;

public interface TestUtils {

    static <T> boolean isEqualsByClassAndFields(T a, T b, String... ignore) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        var cls = a.getClass();
        if (!cls.equals(b.getClass())) {
            return false;
        }
        var ignoreFieldNames = Set.of(ignore);
        for (var field : cls.getDeclaredFields()) {
            if (ignoreFieldNames.contains(field.getName())) {
                continue;
            }
            try {
                field.setAccessible(true);
                if (!Objects.equals(field.get(a), field.get(b))) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    static MockHttpServletRequestBuilder authorized(@NonNull RequestProvider requestProvider,
                                                    @NonNull UserRole role) throws Exception {
        return requestProvider.get().header("Authorization", "Basic " + switch (role) {
            case Admin -> "YWRtaW46YWRtaW4=";
            case Owner -> "b3duZXJAbGV0aS5ydTpwYXNzd29yZA==";
            case Employee -> "dXNlckBsZXRpLnJ1OnBhc3N3b3Jk";
        });
    }

    static MockHttpServletRequestBuilder unauthorized(@NonNull RequestProvider requestProvider) throws Exception {
        return requestProvider.get();
    }
}
