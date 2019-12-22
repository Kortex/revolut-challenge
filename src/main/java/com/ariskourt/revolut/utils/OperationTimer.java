package com.ariskourt.revolut.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

/***
 * Handy little utility for timing various operations
 */
@NoArgsConstructor
@Getter(AccessLevel.PACKAGE)
public class OperationTimer {

    private Instant start;
    private Instant stop;

    public void start() {
        start = Instant.now();
        stop = null;
    }

    public void stop() {
        stop = Instant.now();
    }

    Long elapsed() {
        return Optional.ofNullable(stop)
	    .map(stopInstant -> Duration.between(start, stopInstant).toMillis())
	    .orElse(null);
    }

    @Override
    public String toString() {
	return "Start time: " + start + " - End time: " + stop + ". "
	    + Optional.ofNullable(elapsed())
	    .map(elapsed -> "Operation took: " + elapsed + "ms")
	    .orElse("Timer not yet stopped. Operation is still running");
    }

}
