package com.microsoft.conference.common.async;

import com.alibaba.c2m.smartlinker.command.AbstractAdvanceLocalCommand; /**
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
