package com.gnzlt.ucotren.util;

import com.gnzlt.ucotren.BuildConfig;
import com.gnzlt.ucotren.model.bus.BusResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public final class RestClient {

    public static final String BUS_CALL_DEFAULT_OPERATION = "busStopTimes";
    public static final String BUS_STOP_RABANALES = "831";
    public static final String BUS_STOP_CORDOBA = "800";

    private static BusEndpoints sEndpoints;

    public static BusEndpoints getApiService() {
        return sEndpoints;
    }

    public static void init() {
        sEndpoints = createService();
    }

    private static BusEndpoints createService() {
        return getRetrofit().create(BusEndpoints.class);
    }

    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BUS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public interface BusEndpoints {

        @GET("aucorsa_v1.0.php")
        Call<BusResponse> getBusEstimation(
                @Query("token") String token,
                @Query("operation") String operation,
                @Query("busStop") String busStop
        );
    }
}
