package com.microsoft.conference.common.async;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.processors.ReplayProcessor;

/**
 * rpc callback 异步调用
 */
public abstract class AbstractAdvanceRpcCommand<T, R> extends AbstractAsyncCommand<R> {

    @Override
    protected Flowable<R> construct() {
        ReplayProcessor<R> subject = ReplayProcessor.createWithSize(1);
        RpcResponseCallback callback = new RpcResponseCallback() {
            @Override
            public void onAppException(Throwable t) {
                subject.onError(t);
            }

            @Override
            public void onAppResponse(Object appResponse) {
                try {
                    R r = convertResult((T) appResponse);
                    subject.onNext(r);
                    subject.onComplete();
                } catch (Exception bizException) {
                    subject.onError(bizException);
                }
            }

            @Override
            public void onRpcException(Exception e) {
                subject.onError(e);
            }
        };
        try {
            // TODO 调用前转换调用方式为callback
            callMethod();
        } catch (Exception e) {
            subject.onError(e);
        } finally {
            // TODO 调用完成清理 ThreadLocal信息
        }
        return subject;
    }

    protected abstract void callMethod();

    /**
     * 结果转换
     *
     * @param rpcResult
     * @return
     */
    protected abstract R convertResult(T rpcResult);
}
