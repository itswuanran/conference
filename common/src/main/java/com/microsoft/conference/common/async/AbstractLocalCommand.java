package com.microsoft.conference.common.async;

/**
 * 默认实现结果本地化方法
 *
 * @param <R>
 */
public abstract class AbstractLocalCommand<R> extends AbstractAdvanceLocalCommand<R, R> {
    @Override
    protected R convertResult(R rpcResult) {
        return rpcResult;
    }
}
