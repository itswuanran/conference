package com.microsoft.conference.common.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class CompletableFutureHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompletableFutureHelper.class);

    public static void await(int timeoutMillis, CompletableFuture<?>... cfs) {
        try {
            CompletableFuture.allOf(cfs).get(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (TimeoutException timeoutException) {
            LOGGER.warn("timeout when waiting futures completed, timeoutMillis: {}", timeoutMillis, timeoutException);
        } catch (InterruptedException | ExecutionException exception) {
            LOGGER.error("exception when waiting futures completed, timeoutMillis: {}", timeoutMillis, exception);
        }
    }

    public static void await(List<? extends CompletableFuture<?>> futures) {
        await(10000, futures);
    }

    public static void await(int timeoutMillis, List<? extends CompletableFuture<?>> futures) {
        CompletableFuture<?>[] cfs = futures.toArray(new CompletableFuture[0]);
        await(timeoutMillis, cfs);
    }

    public static <T> CompletableFuture<List<T>> transferToList(final List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
    }

    public static <K, V> CompletableFuture<Map<K, V>> transferToMap(final List<CompletableFuture<Map<K, V>>> futures) {
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v -> futures
                .stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e2, HashMap::new)));
    }
}
