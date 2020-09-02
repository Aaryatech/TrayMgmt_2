package com.ats.traymanagement.interfaces;

import com.ats.traymanagement.model.AllFrBalanceTrayReport;
import com.ats.traymanagement.model.Driver;
import com.ats.traymanagement.model.ErrorMessage;
import com.ats.traymanagement.model.FrList;
import com.ats.traymanagement.model.FrTrayCount;
import com.ats.traymanagement.model.FrTrayReportData;
import com.ats.traymanagement.model.FranchiseByRoute;
import com.ats.traymanagement.model.InTrayDetail;
import com.ats.traymanagement.model.Info;
import com.ats.traymanagement.model.LoginModel;
import com.ats.traymanagement.model.RouteListData;
import com.ats.traymanagement.model.TrayDetails;
import com.ats.traymanagement.model.TrayInData;
import com.ats.traymanagement.model.TrayMgmtDetailData;
import com.ats.traymanagement.model.TrayMgmtHeaderData;
import com.ats.traymanagement.model.TrayMgmtHeaderDisplayList;
import com.ats.traymanagement.model.Vehicle;
import com.ats.traymanagement.model.VehicleInTrayStatus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by MAXADMIN on 17/2/2018.
 */

public interface InterfaceApi {

    @GET("showRouteList")
    Call<RouteListData> getAllRouteList();

    @GET("getAllVehicalList")
    Call<ArrayList<Vehicle>> getAllVehicleList();

    @GET("getAllDriverList")
    Call<ArrayList<Driver>> getAllDriverList();

    @POST("traymgt/getFranchiseInRoute")
    Call<ArrayList<FranchiseByRoute>> getAllFranchiseByRoute(@Query("routeId") int routeId, @Query("tranId") int tranId);

    @POST("traymgt/saveTrayMgtHeader")
    Call<TrayMgmtHeaderData> saveTrayMgmtHeader(@Body TrayMgmtHeaderData trayMgmtHeaderData);

    @POST("traymgt/getOutTraysForFr")
    Call<ArrayList<FrTrayCount>> getFrTrayCount(@Query("frId") int frId, @Query("billDate") String billDate);

    @POST("traymgt/saveTrayMgtDetail")
    Call<Info> saveTrayMgmtDetail(@Body TrayMgmtDetailData trayMgmtDetailData);

    @POST("traymgt/getTrayMgtDetailByTranId")
    Call<ArrayList<TrayMgmtDetailData>> getTrayMgmtDetailByHeaderId(@Query("tranId") int tranId);

    @POST("traymgt/getTrayMgtDetailsByTranId")
    Call<ArrayList<VehicleInTrayStatus>> getTrayMgmtDetailByHeaderIdForIn(@Query("tranId") int tranId);

    @POST("traymgt/getTrayMgtDetailsByTranIdAndDate")
    Call<ArrayList<VehicleInTrayStatus>> getTrayMgmtDetailByHeaderIdAndDateForIn(@Query("tranId") int tranId, @Query("date") String date);

    @POST("traymgt/getLoadedVehicles")
    Call<ArrayList<TrayMgmtHeaderDisplayList>> getTrayMgmtHeadersByDateAndStatus(@Query("date") String date, @Query("vehStatus") int vehStatus);

    @POST("traymgt/updateOutVehicleData")
    Call<Info> updateVehicleOutData(@Query("tranId") int tranId, @Query("vehOutkm") float vehOutkm);

    @POST("traymgt/updateInVehicleData")
    Call<Info> updateVehicleInData(@Query("tranId") int tranId, @Query("vehInkm") float vehInkm, @Query("extraTrayIn") String extraTrayIn);

    @POST("traymgt/updateExtraOutTrays")
    Call<Info> updateVehicleOutTray(@Query("tranId") int tranId, @Query("extraOutTrays") String extraOutTrays);

    @POST("traymgt/updateDieselOfVehicle")
    Call<Info> updateDiesel(@Query("tranId") int tranId, @Query("diesel") float diesel);

    //@POST("traymgt/getAllVehicles")
    //Call<ArrayList<TrayMgmtHeaderDisplayList>> getAllVehicleList(@Query("date") String date);

    @POST("traymgt/getAllVehiclesBetDateForApp")
    Call<ArrayList<TrayMgmtHeaderDisplayList>> getAllVehicleList(@Query("fromDate") String fromDate,@Query("toDate") String toDate);

    @POST("traymgt/getTrayMgtHeader")
    Call<TrayMgmtHeaderDisplayList> getHeaderById(@Query("tranId") int tranId);

    @POST("traymgt/getLoadedVehiclesByStatus")
    Call<ArrayList<TrayMgmtHeaderDisplayList>> getTrayMgmtHeadersByStatus(@Query("vehStatus") int vehStatus);

    @POST("traymgt/deleteTrayMgtHeader")
    Call<ErrorMessage> deleteVehicleHeaderAndDetail(@Query("tranId") int tranId);

    @GET("traymgt/getServerDate")
    Call<Info> getServerDate();

    @POST("traymgt/getTotalTray")
    Call<ArrayList<TrayInData>> getTrayInData(@Query("tranId") int tranId);

    @POST("traymgt/getAllFrBalanceTray")
    Call<ArrayList<AllFrBalanceTrayReport>> getAllFrBalTray();

    @POST("traymgt/getFrTrayReportForLastEightDays")
    Call<ArrayList<FrTrayReportData>> getFrTrayReportData(@Query("frId") int frId, @Query("todaysDate") String todaysDate);

    @GET("getAllFrIdName")
    Call<FrList> getAllFranchisee();

    @POST("traymgt/getTrayMgmtSetailTrayByFrId")
    Call<ArrayList<InTrayDetail>> getFrReturnTrayInData(@Query("fromDate") String fromDate, @Query("toDate") String toDate, @Query("frId") int frId);

    @POST("traymgt/updateTrayDetailByTrayDetId")
    Call<TrayDetails> updateReturnTray(@Body InTrayDetail inTrayDetail);

    @POST("traymgt/getVehInLastRecForApp")
    Call<TrayMgmtHeaderData> getVehInLastRec(@Query("vehId") int vehId);



}
