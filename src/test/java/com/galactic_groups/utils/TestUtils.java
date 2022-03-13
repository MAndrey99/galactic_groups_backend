package com.galactic_groups.utils;

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
}
