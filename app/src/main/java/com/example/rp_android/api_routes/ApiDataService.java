package com.example.rp_android.api_routes;

import com.example.rp_android.SearchModel;
import com.example.rp_android.UpCommingModel;
import com.example.rp_android.models.ORGPostModel;
import com.example.rp_android.models.ObjectModel;
import com.example.rp_android.models.OfferSendModel;
import com.example.rp_android.models.OffersPostModel;
import com.example.rp_android.models.OptionsPostModel;
import com.example.rp_android.models.OptionsSaveModel;
import com.example.rp_android.models.DetailModel;
import com.example.rp_android.models.StatisticsPostModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Api spojeni pro ziskavani dat
 */
public interface ApiDataService {

    @Headers({"Content-Type: application/json"})
    @POST("/api/upcomming")
    Call<ResponseBody> postUpcomming(
            @Header("Authorization") String authToken,
            @Body UpCommingModel model
    );

    @Headers({"Content-Type: application/json"})
    @POST("/api/employee_detail")
    Call<ResponseBody> postEmployeeDetail(
            @Header("Authorization") String authToken,
            @Body DetailModel model
    );

    @Headers({"Content-Type: application/json"})
    @POST("/api/objects")
    Call<ResponseBody> postObjects(
            @Header("Authorization") String authToken,
            @Body DetailModel model

    );

    @Headers({"Content-Type: application/json"})
    @POST("/api/organization")
    Call<ResponseBody> postOrganization(
            @Header("Authorization") String authToken,
            @Body ObjectModel model

    );

    @Headers({"Content-Type: application/json"})
    @POST("/api/options")
    Call<ResponseBody> postOptions(
            @Header("Authorization") String authToken,
            @Body OptionsPostModel model

    );
    @Headers({"Content-Type: application/json"})
    @POST("/api/optionsSave")
    Call<ResponseBody> postOptionsSave(
            @Header("Authorization") String authToken,
            @Body OptionsSaveModel model

    );

    @Headers({"Content-Type: application/json"})
    @POST("/api/workDay")
    Call<ResponseBody> postWorkDay(
            @Header("Authorization") String authToken,
            @Body OptionsPostModel model

    );

    @Headers({"Content-Type: application/json"})
    @POST("/api/sideObjects")
    Call<ResponseBody> postSideObjects(
            @Header("Authorization") String authToken,
            @Body DetailModel model

    );

    @Headers({"Content-Type: application/json"})
    @POST("/api/orgCalendar")
    Call<ResponseBody> postOrgCalendar(
            @Header("Authorization") String authToken,
            @Body ORGPostModel model

    );

    @Headers({"Content-Type: application/json"})
    @POST("/api/offers")
    Call<ResponseBody> postOffers(
            @Header("Authorization") String authToken,
            @Body OffersPostModel model

    );
    @Headers({"Content-Type: application/json"})
    @POST("/api/getOffer")
    Call<ResponseBody> sendOffer(
            @Header("Authorization") String authToken,
            @Body OfferSendModel model

    );
    @Headers({"Content-Type: application/json"})
    @POST("/api/deleteOffer")
    Call<ResponseBody> deleOffer(
            @Header("Authorization") String authToken,
            @Body OfferSendModel model

    );
    @Headers({"Content-Type: application/json"})
    @POST("/api/myShifts")
    Call<ResponseBody> myShifts(
            @Header("Authorization") String authToken,
            @Body ORGPostModel model

    );
    @Headers({"Content-Type: application/json"})
    @POST("/api/attendanceConditions")
    Call<ResponseBody> attendanceConditions(
            @Header("Authorization") String authToken,
            @Body ObjectModel model

    );
    @Headers({"Content-Type: application/json"})
    @POST("/api/confirmArrival")
    Call<ResponseBody> confirmArrival(
            @Header("Authorization") String authToken,
            @Body DetailModel model

    );
    @Headers({"Content-Type: application/json"})
    @POST("/api/confirmDeparture")
    Call<ResponseBody> confirmDeparture(
            @Header("Authorization") String authToken,
            @Body DetailModel model

    );
    @Headers({"Content-Type: application/json"})
    @POST("/api/pauseStart")
    Call<ResponseBody> pauseStart(
            @Header("Authorization") String authToken,
            @Body ObjectModel model

    );
    @Headers({"Content-Type: application/json"})
    @POST("/api/pauseEnd")
    Call<ResponseBody> pauseEnd(
            @Header("Authorization") String authToken,
            @Body ObjectModel model

    );

    @Headers({"Content-Type: application/json"})
    @POST("/api/boardLoader")
    Call<ResponseBody> boardLoader(
            @Header("Authorization") String authToken,
            @Body DetailModel model

    );

    @Headers({"Content-Type: application/json"})
    @POST("/api/myStatsTable")
    Call<ResponseBody> statisticCall(
            @Header("Authorization") String authToken,
            @Body StatisticsPostModel model

    );
    @Headers({"Content-Type: application/json"})
    @POST("/api/getAssignments")
    Call<ResponseBody> getAssignments(
            @Header("Authorization") String authToken,
            @Body ObjectModel omdel
    );
    @Headers({"Content-Type: application/json"})
    @POST("/api/search")
    Call<ResponseBody> searchEmployee(
            @Header("Authorization") String authToken,
            @Body SearchModel searcmodel
    );


}
