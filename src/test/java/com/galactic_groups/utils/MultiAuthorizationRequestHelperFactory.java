package com.galactic_groups.utils;

import com.galactic_groups.data.view.UserRole;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

import static com.galactic_groups.utils.TestUtils.authorized;
import static com.galactic_groups.utils.TestUtils.unauthorized;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
@RequiredArgsConstructor
public class MultiAuthorizationRequestHelperFactory {

    private final MockMvc mockMvc;

    /**
     * TODO
     * @param requestProvider
     * @param okPostProcessor
     * @param forbiddenPostProcessor
     * @param availableFor
     * @return
     */
    public MultiAuthorizationRequestHelper buildMultiAuthorizationRequestHelper(
            @NonNull RequestProvider requestProvider,
            @NonNull RequestResultPostProcessor okPostProcessor,
            RequestResultPostProcessor forbiddenPostProcessor,
            UserRole... availableFor) {
        var rolesSet = Arrays.stream(availableFor)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(UserRole.class)));
        return new MultiAuthorizationRequestHelper(requestProvider, okPostProcessor, forbiddenPostProcessor, rolesSet);
    }

    @RequiredArgsConstructor
    public class MultiAuthorizationRequestHelper {

        @NonNull
        private final RequestProvider requestProvider;

        @NonNull
        private final RequestResultPostProcessor okPostProcessor;

        private final RequestResultPostProcessor forbiddenPostProcessor;

        @NonNull
        private final EnumSet<UserRole> availableFor;

        public void performAs(UserRole role) {
            if (availableFor.contains(role)) {
                performAs(role, okPostProcessor);
            } else {
                performAs(role, resultActions -> {
                    resultActions.andExpect(status().isForbidden());
                    if (forbiddenPostProcessor != null)
                        forbiddenPostProcessor.process(resultActions);
                });
            }
        }

        @SneakyThrows
        private void performAs(UserRole role, RequestResultPostProcessor afterPerformAction) {
            var requestBuilder = role != null ? authorized(requestProvider, role)
                    : unauthorized(requestProvider);
            var result = mockMvc.perform(requestBuilder);
            if (afterPerformAction != null)
                afterPerformAction.process(result);
        }
    }
}
