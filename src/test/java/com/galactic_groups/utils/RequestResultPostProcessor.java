package com.galactic_groups.utils;

import org.springframework.test.web.servlet.ResultActions;

public interface RequestResultPostProcessor {

    void process(ResultActions resultActions) throws Exception;
}
