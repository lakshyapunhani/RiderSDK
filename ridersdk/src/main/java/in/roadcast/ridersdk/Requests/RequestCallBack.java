package in.roadcast.ridersdk.Requests;

/**
 * Created by roadcast on 6/2/18.
 */

public interface RequestCallBack<RcGENERIC> {
    void onSuccess(RcGENERIC response);
    void onFailure(int failureCode);
    void onTokenRefresh(String token);
}
