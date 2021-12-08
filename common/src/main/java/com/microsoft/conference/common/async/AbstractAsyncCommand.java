package com.microsoft.conference.common.async;

import com.alibaba.csp.sentinel.AsyncEntry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import io.reactivex.rxjava3.core.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public abstract class AbstractAsyncCommand<R> {

    /**
     * 外部调用兜底超时时间
     */
    protected static final int DEFAULT_EXECUTION_TIMEOUT = 3000;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private String msg;

    public void put(Object... msg) {
        StringBuilder format = new StringBuilder("%s");
        for (int i = 1; i < msg.length; i++) {
            format.append("|%s");
        }
        this.msg = String.format(format.toString(), msg);
    }

    /**
     * command暴露给外部的异步执行函数
     *
     * @return 返回类型为java8的completableFuture
     */
    public CompletableFuture<R> queue() {
        CompletableFuture<R> future = new CompletableFuture<>();
        try {
            AsyncEntry entry = SphU.asyncEntry(getResourceKey());
            construct().subscribe(x -> {
                try {
                    future.complete(x);
                } finally {
                    entry.exit();
                }
            }, exception -> {
                try {
                    future.completeExceptionally(exception);
                    Tracer.trace(exception);
                } finally {
                    entry.exit();
                }
            });
        } catch (BlockException blockException) {
            LOGGER.error("Execute command block {}, args: {}", getResourceKey(), msg, blockException);
            fallback(future);
        }
        return future;
    }

    /**
     * command暴露给外部的同步执行函数
     *
     * @param timeout 执行等待的超时时间
     * @return 返回执行结果
     */
    public R execute(long timeout) {
        try {
            return queue().get(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            String errMsg = String.format("Execute command timeout: %s, %s, args: %s", getResourceKey(), timeout, msg);
            throw new CommandTimeoutException(errMsg, e);
        }
    }

    /**
     * command暴露给外部的同步执行函数
     *
     * @return 返回类型为结果类型，阻塞执行，默认超时3s
     */
    public R execute() {
        return execute(DEFAULT_EXECUTION_TIMEOUT);
    }

    /**
     * 需要子类提供差异化实现的方法
     */
    protected abstract String getResourceKey();

    protected abstract Flowable<R> construct();

    protected abstract R getFallbackResult();

    private void fallback(CompletableFuture<R> future) {
        try {
            future.complete(getFallbackResult());
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
    }
}