package in.roadcast.ridersdk.Requests;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import in.roadcast.ridersdk.Helper.HelperClient;
import in.roadcast.ridersdk.Managers.SessionManager;
import in.roadcast.ridersdk.Pojo.CheckUserResponsePojo;
import in.roadcast.ridersdk.Pojo.ProfilePojo;
import in.roadcast.ridersdk.Pojo.RcResponseModel;
import in.roadcast.ridersdk.RoadcastDelegate;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRequests
{
    public static void loginUser(String mobileNumber, RoadcastDelegate delegate, Context context)
    {
        HelperClient.getRequestInterface("")
                .checkUser(mobileNumber)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.code() == RequestInterface.RESPONSE_SUCCESS) {
                            HelperClient.getSessionManager(context).saveMobileNumber(mobileNumber);
                            delegate.success();
                        } else if (response.code() == RequestInterface.BAD_REQUEST) {
                            delegate.failure("user_not_exists");
                        } else {
                            delegate.failure("bad_request");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        delegate.failure("bad_request");
                    }
                });
    }

    public static void otpConfirm(String otp,Context context,RoadcastDelegate delegate)
    {
        HashMap<String,String> hashMap = new HashMap<>();
        SessionManager sessionManager = HelperClient.getSessionManager(context);
        hashMap.put("username",sessionManager.getMobileNumber());
        hashMap.put("otp",otp);

        HelperClient.getRequestInterface("")
                .verifyOtp(hashMap)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                        if (response.code() == RequestInterface.RESPONSE_SUCCESS) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                JSONObject metaObject = jsonObject.optJSONObject("meta");

                                String userId = metaObject.optString("id");
                                String token = metaObject.optString("token");

                                sessionManager.setLoginId(Long.parseLong(userId));
                                sessionManager.setAccessToken(token);
                                getUserProfileData(context,delegate,true);
                            } catch (Exception e) {
                                e.printStackTrace();
                                delegate.failure("json_exception");
                            }


                        } else {
                            delegate.failure("invalid_otp");

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        delegate.failure("invalid_otp");

                    }
                });
    }

    public static void getUserProfileData(Context context,RoadcastDelegate delegate,boolean fromOTP) {
        SessionManager sessionManager = HelperClient.getSessionManager(context);

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("__include","user_device");
        hashMap.put("__id__equal",sessionManager.getLoginId());

        HelperClient.getRequestInterface("")
                .getUserProfileDetails(sessionManager.getAccessToken(), hashMap)
                .enqueue(new Callback<RcResponseModel<List<CheckUserResponsePojo>>>() {
                    @Override
                    public void onResponse(@NonNull Call<RcResponseModel<List<CheckUserResponsePojo>>> call, @NonNull Response<RcResponseModel<List<CheckUserResponsePojo>>> response) {
                        if (response.code() == RequestInterface.RESPONSE_SUCCESS && response.body().getTotal() > 0) {
                            if (response.body().getData().get(0).getName() !=  null) {
                                sessionManager.saveUsername(response.body().getData().get(0).getName());
                                sessionManager.setCompanyId(response.body().getData().get(0).getCompany().getId());
                                sessionManager.setIsAuthentified(true);
                                if (response.body().getData().get(0).getUserDevice() != null && response.body().getData().get(0).getUserDevice().getOnDuty() != null && response.body().getData().get(0).getUserDevice().getOnDuty().matches("1"))
                                {
                                    sessionManager.put_duty_status("true");
                                }
                                else
                                {
                                    sessionManager.put_duty_status("false");
                                }
                                if (fromOTP)
                                {
                                    if (response.body().getData().get(0).getUserDevice() != null) {
                                        sessionManager.saveUserProfileId(String.valueOf(response.body().getData().get(0).getUserDevice().getId()));
                                        updateUserProfileRequest(context,delegate);
                                    } else {
                                        userProfileRequest(context,delegate);
                                    }
                                }
                                else
                                {
                                    delegate.success();
                                }
                            }
                            else
                            {
                                delegate.failure("request_failed");
                            }
                        } else {
                            delegate.failure("request_failed");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RcResponseModel<List<CheckUserResponsePojo>>> call, @NonNull Throwable t) {
                        delegate.failure("request_failed");
                    }
                });
    }

    private static void updateUserProfileRequest(Context context,RoadcastDelegate delegate)
    {
        SessionManager sessionManager = HelperClient.getSessionManager(context);
        String uniqueId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String currentAppVersion = "1";
        try {
            currentAppVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        HashMap<String, Object> map = new HashMap<>();

        map.put("manufacturer", Build.MANUFACTURER);
        map.put("model", Build.MODEL);
        map.put("androidVersion", Build.VERSION.RELEASE);
        map.put("sdk", Build.VERSION.SDK_INT);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("on_duty", "1");
        hashMap.put("online", "1");
        hashMap.put("in_zone", "0");
        hashMap.put("device_token", "");
        hashMap.put("device_details", map);
        hashMap.put("app_version", currentAppVersion);
        hashMap.put("total_amt_collect", "0");
        hashMap.put("total_amt_deposit", "0");
        hashMap.put("user_id", sessionManager.getLoginId());
        hashMap.put("unique_id", uniqueId);
        hashMap.put("status", "U");
        hashMap.put("device_type", "ANDROID");

        HelperClient.getRequestInterface("")
                .updateProfileRequest(sessionManager.getAccessToken(), hashMap, sessionManager.getUserProfileId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.code() == RequestInterface.RESPONSE_SUCCESS) {
                            delegate.success();
                        } else {
                            delegate.failure("request_failed");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        delegate.failure("request_failed");
                    }
                });
    }

    private static void userProfileRequest(Context context,RoadcastDelegate delegate)
    {
        SessionManager sessionManager = HelperClient.getSessionManager(context);
        String uniqueId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String currentAppVersion = "1";
        try {
            currentAppVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        HashMap<String, Object> map = new HashMap<>();

        map.put("manufacturer", Build.MANUFACTURER);
        map.put("model", Build.MODEL);
        map.put("androidVersion", Build.VERSION.RELEASE);
        map.put("sdk", Build.VERSION.SDK_INT);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("on_duty", "1");
        hashMap.put("online", "1");
        hashMap.put("in_zone", "0");
        hashMap.put("device_token", "");
        hashMap.put("device_details", map);
        hashMap.put("app_version", currentAppVersion);
        hashMap.put("total_amt_collect", "0");
        hashMap.put("total_amt_deposit", "0");
        hashMap.put("user_id", sessionManager.getLoginId());
        hashMap.put("unique_id", uniqueId);
        hashMap.put("status", "U");
        hashMap.put("device_type", "ANDROID");

        HelperClient.getRequestInterface("")
                .loginRequest(sessionManager.getAccessToken(), hashMap)
                .enqueue(new Callback<RcResponseModel<List<ProfilePojo>>>() {
                    @Override
                    public void onResponse(@NonNull Call<RcResponseModel<List<ProfilePojo>>> call, @NonNull Response<RcResponseModel<List<ProfilePojo>>> response) {
                        if (response.code() == RequestInterface.POST_SUCCESS) {
                            sessionManager.saveUserProfileId(String.valueOf(response.body().getData().get(0).getId()));
                            delegate.success();
                        } else {
                            delegate.failure("request_Failed");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RcResponseModel<List<ProfilePojo>>> call, @NonNull Throwable t) {
                        delegate.failure("request_Failed");
                    }
                });
    }


}
