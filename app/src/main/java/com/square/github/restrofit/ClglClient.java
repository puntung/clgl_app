package com.square.github.restrofit;



import com.google.gson.JsonObject;



import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;


/**
 * Created by mglory on 2015/12/23.
 */
public interface ClglClient {
    @POST("/car")
    Observable<JsonObject> upload(@Body JsonObject data);

}
