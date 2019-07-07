package in.roadcast.ridersdk.Requests;


import java.util.HashMap;
import java.util.List;

import in.roadcast.ridersdk.Pojo.CheckUserResponsePojo;
import in.roadcast.ridersdk.Pojo.ProfilePojo;
import in.roadcast.ridersdk.Pojo.RcResponseModel;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RequestInterface {

    String UNAUTHORIZED_USER = "unauthorized_user";
    String TOKEN_REQUEST_FAILURE = "token_request_failure";
    String STATUS_SUCCESS = "success";
    String MESSAGE_SUCCESS = "true";

    int INTERNAL_SERVER_ERROR=500;
    int UNAUTHORIZED=401;
    int INTERNET_FAILURE=600;
    int RESPONSE_SUCCESS=200;
    int NO_DATA_ON_SERVER=204;
    int POST_SUCCESS=201;
    int BAD_REQUEST=400;
    int SERVER_DOWN=503;
    int TOKEN_EXPIRED=5;
    int TOKEN_NOT_VALID=6;
    int TOKEN_MISSING=4;


    @POST("position")
    Call<ResponseBody> locationToService(@Header("Authorization") String token, @Header("content-type") String contentType, @Body RequestBody locations);

    @POST("event")      // "idle" for idle event & "tms" for trip completion
    Call<ResponseBody> updateIdleAndTripEvent (@Header("Authorization") String token, @Body HashMap<String, Object> data);

    @GET("user_login_otp")
    Call<ResponseBody> checkUser(@Query("username") String username);

    @GET("user_verify_otp")
    Call<ResponseBody> verifyOtp(@QueryMap HashMap<String,String> data);

    @GET("user")
    Call<RcResponseModel<List<CheckUserResponsePojo>>> getUserProfileDetails (@Header("Authorization") String accessToken, @QueryMap HashMap<String,Object> hashMap);

    @PATCH("user_device/{profileId}")
    Call<ResponseBody> updateProfileRequest (@Header("Authorization") String accessToken, @Body HashMap<String, Object> data, @Path("profileId") String profileId);

    @POST("user_device?__include&__only=id")
    Call<RcResponseModel<List<ProfilePojo>>> loginRequest(@Header("Authorization") String accessToken, @Body HashMap<String, Object> data);

}
