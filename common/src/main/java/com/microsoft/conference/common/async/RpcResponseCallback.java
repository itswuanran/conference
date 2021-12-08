package com.microsoft.conference.common.async;

public interface RpcResponseCallback {
    /**
     * 当对端业务层抛出异常，rpc层将回调该方法。
     *
     * @param t 对端业务层抛出的异常
     */
    public void onAppException(Throwable t);

    /**
     * 当对端业务层正常返回结果，RPC层将回调该方法。
     */
    public void onAppResponse(Object appResponse);

    /**
     * 当rpc层出现异常时，回调该方法。
     */
    public void onRpcException(Exception rpcEx);
}