package com.exam;


import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ExecutorTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void name() {
        assertThat("1", is("1"));
    }
}