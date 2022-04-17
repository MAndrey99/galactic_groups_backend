package com.galactic_groups.utils;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public interface RequestProvider {

    MockHttpServletRequestBuilder get() throws Exception;
}
