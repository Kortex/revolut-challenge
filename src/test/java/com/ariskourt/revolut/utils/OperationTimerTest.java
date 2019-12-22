package com.ariskourt.revolut.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

class OperationTimerTest {

    private OperationTimer timer;

    @BeforeEach
    void setUp() {
        timer = new OperationTimer();
    }

    @Test
    @DisplayName("Starting the timer inits that start instant and clear the stop one")
    public void start_StartingTimer_StartInstantNotNullStopInstantNull() {
        timer.start();
        assertNotNull(timer.getStart());
        assertNull(timer.getStop());
    }

    @Test
    @DisplayName("Starting and stopping the timer, results in both instants not null")
    public void stop_StoppingTimer_StartAndStopInstantNotNull() {
        timer.start();
        timer.stop();
        assertNotNull(timer.getStart());
        assertNotNull(timer.getStop());
    }

    @Test
    @DisplayName("Starting and stopping timer and getting elapsed, results in correct value")
    public void Elapsed_StartingAndStoppingTimer_CorrectElapseTime() {
        timer.start();
        await().atMost(2, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                timer.stop();
                long millis = Duration.between(timer.getStart(), timer.getStop()).toMillis();
                assertEquals(millis, (long) timer.elapsed());
            });
    }

    @Test
    @DisplayName("Starting and stopping timer, results in correct message")
    public void ToString_StartingAndStoppingTimer_CorrectToStringMessage() {
        timer.start();
        await().atMost(2, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                timer.stop();
                assertNotNull(timer.getStart());
                assertNotNull(timer.getStop());
                assertThat(timer.toString(), containsString(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(timer.elapsed()))));
            });
    }

    @Test
    @DisplayName("Starting but not stopping the timer, results in correct message")
    public void ToString_WhenTimerHasNotBeenStopped_CorrectToStringMessage() {
        timer.start();
        assertThat(timer.toString(), containsString("Operation is still running"));
    }

}