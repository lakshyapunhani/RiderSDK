package in.roadcast.ridersdk.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by roadcast on 6/2/18.
 */

public class RcResponseModel<RcGENERIC> {

    @SerializedName("success")
    @Expose
    private String success;

    @SerializedName("data")
    @Expose
    private RcGENERIC data;

    @SerializedName("total")
    @Expose
    private int total = 0;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("code")
    @Expose
    private String code;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public RcGENERIC getData() {
        return data;
    }

    public void setData(RcGENERIC data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
