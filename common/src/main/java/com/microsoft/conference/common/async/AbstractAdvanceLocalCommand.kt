package com.microsoft.conference.common.async

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.ReplayProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.future.asCompletableFuture
import org.jetbrains.annotations.NotNull

/**
 * 同步调用通过本地线程池封装成异步调用
 *
 * @param <R> 返回的本地对象
 * @param <T> RPC返回的结果
 */
abstract class AbstractAdvanceLocalCommand<T, R : Any> : AbstractAsyncCommand<R>() {

    override fun construct(): Flowable<R> {
        val subject: ReplayProcessor<R> = ReplayProcessor.createWithSize(1)
        try {
            CoroutineScope(Dispatchers.IO).async {
                callMethod()
            }.asCompletableFuture().whenComplete { value, exception ->
                if (exception === null) {
                    val result = convertResult(value)
                    subject.onNext(result)
                    subject.onComplete()
                } else {
                    subject.onError(exception)
                }
            }
        } catch (exception: Exception) {
            subject.onError(exception)
        }
        return subject;
    }

    protected abstract fun callMethod(): T

    @NotNull
    protected abstract fun convertResult(rpcResult: T): R
}