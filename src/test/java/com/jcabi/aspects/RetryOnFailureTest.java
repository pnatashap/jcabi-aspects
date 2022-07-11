/*
 * Copyright (c) 2012-2017, jcabi.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the jcabi.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jcabi.aspects;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link RetryOnFailure} annotation and its implementation.
 * @since 0.0.0
 */
@SuppressWarnings("PMD.DoNotUseThreads")
public final class RetryOnFailureTest {

    /**
     * RetryOnFailure can force duplicate execution of the same method.
     * @throws Exception If something goes wrong
     */
    @Test
    public void executesMethodManyTimes() throws Exception {
        final AtomicInteger count = new AtomicInteger();
        new Runnable() {
            @Override
            @RetryOnFailure(verbose = false, unit = TimeUnit.SECONDS, delay = 1)
            public void run() {
                if (count.incrementAndGet() < 2) {
                    throw new IllegalArgumentException(
                        "this exception should be caught and swallowed"
                    );
                }
            }
        } .run();
        MatcherAssert.assertThat(count.get(), Matchers.greaterThan(0));
    }

    /**
     * RetryOnFailure can retry when Error types are thrown.
     * @throws Exception If something goes wrong
     */
    @Test
    public void retriesOnError() throws Exception {
        final AtomicInteger count = new AtomicInteger();
        new Runnable() {
            @Override
            @RetryOnFailure(verbose = false, unit = TimeUnit.SECONDS, delay = 1)
            public void run() {
                if (count.incrementAndGet() < 2) {
                    throw new AssertionError("Should be caught and ignored.");
                }
            }
        } .run();
        MatcherAssert.assertThat(count.get(), Matchers.greaterThan(0));
    }

}
