package com.campmongoose.serversaturday;

import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NullMarked
public interface ExceptionUtils {

    static Collector<Throwable, ?, IOException> toIOException(String error) {
        return Collectors.collectingAndThen(Collectors.toList(), list -> {
            IOException ioException = new IOException(error);
            list.forEach(ioException::addSuppressed);
            return ioException;
        });
    }

    static void throwIOException(String error, Stream<? extends Throwable> stream) throws IOException {
        IOException exception = stream.collect(toIOException(error));
        if (exception.getSuppressed().length > 0) {
            throw exception;
        }
    }
}
