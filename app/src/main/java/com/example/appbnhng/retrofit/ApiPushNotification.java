package com.example.appbnhng.retrofit;

import com.example.appbnhng.model.NotiResponse;
import com.example.appbnhng.model.NotiSendData;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiPushNotification {
    @Headers(
            {
                   "Content-Type: application/json",
                    "Authorization: key=AAAAkUv9dKw:APA91bGiu3vqek8aH8GRcdG1pJFR_aJDx6HjeV9Z7KcDu3aJTiV3LjiQWsl3ButV9eDEIhaKTcoqDXrDJnKndAys1drkwVo7KKjTYTrcArFtxcblduJkmNMDDTYOhMsuNnRpjkBGzvcu"
            }
    )
    @POST("fcm/send")
    Observable<NotiResponse> sendNotification(@Body NotiSendData data);
}
