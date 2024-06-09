package com.icapp.icapp.Interfaces;

import retrofit2.Response;

public interface ICallback {
    public void onSuccess(Response response, String msg);
    public void onSuccessFail(String msg);
    public void onFail(String msg);
}
