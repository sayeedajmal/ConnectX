package com.Strong.personalchat.Utilities;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({"Content-Type:application/json", "YOUR_FCM_KEY"})

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}