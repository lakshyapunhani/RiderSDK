package in.roadcast.ridersdk.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckUserResponsePojo
{
    @SerializedName("active")
    @Expose
    private boolean active;
    @SerializedName("children")
    @Expose
    private List<Object> children = null;
    @SerializedName("company")
    @Expose
    private CompanyDetailsPojo company;
    @SerializedName("device_token")
    @Expose
    private Object deviceToken;
    @SerializedName("field1")
    @Expose
    private Object field1;
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("parent")
    @Expose
    private Object parent;
    @SerializedName("permissions")
    @Expose
    private List<Object> permissions = null;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("roles")
    @Expose
    private List<UserRolesPojo> roles = null;
    @SerializedName("types")
    @Expose
    private List<Object> types = null;
    @SerializedName("unique_id")
    @Expose
    private Object uniqueId;
    @SerializedName("user_device")
    @Expose
    private CheckUserProfileResponsePojo userDevice;
    @SerializedName("web_token")
    @Expose
    private Object webToken;


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Object> getChildren() {
        return children;
    }

    public void setChildren(List<Object> children) {
        this.children = children;
    }

    public CompanyDetailsPojo getCompany() {
        return company;
    }

    public void setCompany(CompanyDetailsPojo company) {
        this.company = company;
    }

    public Object getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(Object deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Object getField1() {
        return field1;
    }

    public void setField1(Object field1) {
        this.field1 = field1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getParent() {
        return parent;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }

    public List<Object> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Object> permissions) {
        this.permissions = permissions;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<UserRolesPojo> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRolesPojo> roles) {
        this.roles = roles;
    }

    public List<Object> getTypes() {
        return types;
    }

    public void setTypes(List<Object> types) {
        this.types = types;
    }

    public Object getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Object uniqueId) {
        this.uniqueId = uniqueId;
    }

    public CheckUserProfileResponsePojo getUserDevice() {
        return userDevice;
    }

    public void setUserDevice(CheckUserProfileResponsePojo userDevice) {
        this.userDevice = userDevice;
    }

    public Object getWebToken() {
        return webToken;
    }

    public void setWebToken(Object webToken) {
        this.webToken = webToken;
    }

}
