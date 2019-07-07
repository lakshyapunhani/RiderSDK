package in.roadcast.ridersdk.Helper;

import android.content.Context;
import android.net.ConnectivityManager;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import in.roadcast.ridersdk.BuildConfig;
import in.roadcast.ridersdk.Managers.SessionManager;
import in.roadcast.ridersdk.Networks.UserActivityToServer;
import in.roadcast.ridersdk.Requests.RequestInterface;
import in.roadcast.ridersdk.Utils.API;
import in.roadcast.ridersdk.Utils.CommonMethods;
import in.roadcast.ridersdk.Utils.CommonServiceMethods;
import in.roadcast.ridersdk.locationHelper.GetCurrentLocation;
import in.roadcast.ridersdk.locationHelper.listener.GPSLiveTrackingPositionProvider;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by roadcast on 1/9/17.
 */

public class HelperClient {

    private static Retrofit retrofit;
    private static Retrofit retrofitWithoutConverter;

    private static Retrofit miniServiceRetrofit;

    private static Retrofit retrofitGeocoding;

    private static OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS);

    private static OkHttpClient.Builder clientBuilderLocations = new OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS);

    private static OkHttpClient.Builder clientBuilderForGeocoding= new OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS);

    private static RequestInterface requestInterface;
    private static RequestInterface testrequestInterface;
    private static RequestInterface mapGeocodingInterface;
    private static RequestInterface requestInterfaceWithoutConverter;
    private static Gson gson;
    private static volatile CommonMethods commonMethods;
    private static SessionManager sessionManager;
    private static volatile CommonServiceMethods commonServiceMethods;
    private static GetCurrentLocation getCurrentLocation;

    private static GPSLiveTrackingPositionProvider liveTrackingPositionProvider;

    private static ConnectivityManager connectivityManager;
    private static UserActivityToServer userActivityToServer;

    public static Retrofit getRetrofitClient(String addedBaseUrl)
    {

        if (retrofit == null)
        {
            if (BuildConfig.DEBUG)
            {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                clientBuilder.addInterceptor(interceptor);
            }

            OkHttpClient httpClient = clientBuilder.build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(API.BASE_URL + addedBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
        }
        return retrofit;
    }



    public static Retrofit getRetrofitTestClient()
    {

        if (miniServiceRetrofit == null)
        {
            if (BuildConfig.DEBUG)
            {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                clientBuilderLocations.addInterceptor(interceptor);
            }

            OkHttpClient httpClient = clientBuilderLocations.build();
            miniServiceRetrofit = new Retrofit.Builder()
                    .baseUrl(API.TRACKING_BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
        }
        return miniServiceRetrofit;
    }



    public static Retrofit getRetrofitMapGeocoding()
    {

        if (retrofitGeocoding == null)
        {
            if (BuildConfig.DEBUG)
            {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                clientBuilderForGeocoding.addInterceptor(interceptor);
            }

            OkHttpClient httpClient = clientBuilderForGeocoding.build();
            retrofitGeocoding = new Retrofit.Builder()
                    .baseUrl(API.MAP_GEOCODING_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
        }
        return retrofitGeocoding;
    }



    public static SessionManager getSessionManager(Context context) {
        if (sessionManager == null) {
            sessionManager = new SessionManager(context);
        }

        return sessionManager;
    }


    public static RequestInterface getRequestInterface(String addedBaseUrl){

        if(retrofit == null)
        {
            getRetrofitClient(addedBaseUrl);
        }
        if (requestInterface == null)
        {
            requestInterface = retrofit.create(RequestInterface.class);
        }
        return requestInterface;
    }



    public static RequestInterface getTestRequestInterface()
    {
        if (miniServiceRetrofit == null)
        {
            getRetrofitTestClient();
        }
        if (testrequestInterface == null) {
            testrequestInterface = miniServiceRetrofit.create(RequestInterface.class);
        }
        //testrequestInterface = miniServiceRetrofit.create(RequestInterface.class);
        return testrequestInterface;
    }


    public static RequestInterface getMapGeocodingInterface()
    {
        if (retrofitGeocoding == null)
        {
            getRetrofitMapGeocoding();
        }
        if (mapGeocodingInterface == null) {
            mapGeocodingInterface = retrofitGeocoding.create(RequestInterface.class);
        }
        return mapGeocodingInterface;
    }


    public static Retrofit getRetrofitClientWihoutConverter() {
        if (retrofitWithoutConverter == null) {
            retrofitWithoutConverter = new Retrofit.Builder()
                    .baseUrl(API.BASE_URL)
                    .build();
        }

        return retrofitWithoutConverter;
    }

    public static RequestInterface getRequestInterfaceWithoutConverter(){

        if(retrofitWithoutConverter == null)
        {
            getRetrofitClientWihoutConverter();
        }
        if (requestInterfaceWithoutConverter == null) {
            requestInterfaceWithoutConverter = retrofitWithoutConverter.create(RequestInterface.class);
        }
        return requestInterfaceWithoutConverter;
    }

    public static Gson getGson()
    {
        if(gson == null)
        {
            gson = new Gson();
        }
        return gson;
    }

    public static GetCurrentLocation getCurrentLocation(Context context)
    {
        if (getCurrentLocation == null)
        {
            getCurrentLocation = new GetCurrentLocation(context);
        }
        return getCurrentLocation;
    }

    public static GPSLiveTrackingPositionProvider getLiveTrackingPositionProvider(Context context)
    {
        if (liveTrackingPositionProvider == null)
        {
            liveTrackingPositionProvider= new GPSLiveTrackingPositionProvider(context);
        }
        return liveTrackingPositionProvider;
    }

    public static UserActivityToServer getUserActivityToServer(Context context)
    {
        if (userActivityToServer == null)
        {
            userActivityToServer = new UserActivityToServer(context);
        }
        return userActivityToServer;
    }

    public static CommonMethods getCommonMethods(){
        if (commonMethods == null)
        {
            synchronized (CommonMethods.class)
            {
                if (commonMethods == null)
                {
                    commonMethods = new CommonMethods();
                }
            }
        }
        return commonMethods;
    }

    public static CommonServiceMethods getCommonServiceMethods()
    {
        if (commonServiceMethods == null)
        {
            synchronized (CommonServiceMethods.class)
            {
                if (commonServiceMethods == null)
                {
                    commonServiceMethods = new CommonServiceMethods();
                }
            }

        }
        return commonServiceMethods;
    }

    public static ConnectivityManager getConnectivityManager(Context context) {
        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        return connectivityManager;
    }


}
