package in.roadcast.ridersdk.Managers;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;

import in.roadcast.ridersdk.Helper.HelperClient;
import in.roadcast.ridersdk.Pojo.RcResponseModel;
import in.roadcast.ridersdk.Requests.RequestCallBack;
import in.roadcast.ridersdk.Requests.RequestInterface;
import in.roadcast.ridersdk.Utils.API;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by roadcast on 6/2/18.
 */

public class RcRequestManager {

    private static volatile RcRequestManager sRcRequestManager;

    public static RcRequestManager getInstance()
    {
        if (sRcRequestManager == null)
        {
            synchronized (RcRequestManager.class)
            {
                if (sRcRequestManager == null)
                {
                    sRcRequestManager = new RcRequestManager();
                }
            }
        }
        return sRcRequestManager;
    }

    private RcRequestManager()
    {
        //Prevent form the reflection api.
        if (sRcRequestManager != null)
        {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public void updateIdleAndTripCompleteEvent(HashMap<String,Object> hashMap, String token, HashMap<String,String> tokenHash, RequestCallBack<String> callBack) {
        HelperClient.getRequestInterface("")
                .updateIdleAndTripEvent(token, hashMap)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,@NonNull Response<ResponseBody> response) {
                        if (response.code() == RequestInterface.POST_SUCCESS || response.code() == RequestInterface.RESPONSE_SUCCESS) {
                            callBack.onSuccess("");
                        } else if (response.code() == RequestInterface.UNAUTHORIZED) {
                            //requestToken(tokenHash, token, callBack);
                        } else {
                            callBack.onFailure(response.code());
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call,@NonNull Throwable t) {
                        callBack.onFailure(RequestInterface.INTERNET_FAILURE);
                    }
                });
    }

}
