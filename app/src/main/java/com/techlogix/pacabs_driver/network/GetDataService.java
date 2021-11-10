package com.techlogix.pacabs_driver.network;

import com.techlogix.pacabs_driver.models.GenericResponseModel;
import com.techlogix.pacabs_driver.models.citiesModels.CititesResponseModel;
import com.techlogix.pacabs_driver.models.createDriverModels.CreateDriverRequestModel;
import com.techlogix.pacabs_driver.models.createDriverModels.CreateDriverResponseModel;
import com.techlogix.pacabs_driver.models.createDriverModels.LoginDriverRequestModel;
import com.techlogix.pacabs_driver.models.createDriverModels.VerifyOTPRequestModel;
import com.techlogix.pacabs_driver.models.earningModels.EarningResponseModel;
import com.techlogix.pacabs_driver.models.eventsModel.UpdateEventRequestModel;
import com.techlogix.pacabs_driver.models.jobModels.EndRideResponseModel;
import com.techlogix.pacabs_driver.models.jobModels.JobRejectResponseModel;
import com.techlogix.pacabs_driver.models.jobModels.JobRequestModel;
import com.techlogix.pacabs_driver.models.jobModels.JobResponseModel;
import com.techlogix.pacabs_driver.models.myAllBookingModels.AllBookingResponseModel;
import com.techlogix.pacabs_driver.models.polling.PollingRequestModel;
import com.techlogix.pacabs_driver.models.polling.PollingResponseModel;
import com.techlogix.pacabs_driver.models.serviceTypeModels.ServiceType;
import com.techlogix.pacabs_driver.models.tokenModel.TokenRequestModel;
import com.techlogix.pacabs_driver.models.vehicleModels.VehiclesResponseModel;
import com.techlogix.pacabs_driver.models.walletsModel.WalletResponseModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GetDataService {

    @GET("city")
    Call<GenericResponseModel<ArrayList<CititesResponseModel>>> getAllCities();

    @GET("vehiclecategory")
    Call<GenericResponseModel<ArrayList<VehiclesResponseModel>>> getAllVehicleTypes();

    @GET("triptype")
    Call<GenericResponseModel<ArrayList<ServiceType>>> getAllServiceTypes();

    @POST("drivermaster")
    Call<GenericResponseModel<CreateDriverResponseModel>> createDriver(@Body CreateDriverRequestModel requestModel);

    @POST("drivermaster/verifydriver")
    Call<GenericResponseModel<CreateDriverResponseModel>> loginDriver(@Body LoginDriverRequestModel requestModel);

    @POST("drivermaster/verifyOtp")
    Call<GenericResponseModel<CreateDriverResponseModel>> verifyOTP(@Body VerifyOTPRequestModel requestModel);

    @PUT("drivermaster/available/{vehicleid}/{avail}")
    Call<GenericResponseModel> updateAvailability(@Path("vehicleid") String vehicleid, @Path("avail") int avail);

    @POST("poll")
    Call<GenericResponseModel<PollingResponseModel>> polling(@Body PollingRequestModel requestModel);

    @GET("job/{vehicleid}")
    Call<GenericResponseModel<JobResponseModel>> jobRequest(@Path(value = "vehicleid") String vehicleid);

    @POST("job/received")
    Call<GenericResponseModel> jobAcknowledegment(@Body JobRequestModel requestModel);

    @POST("job/answeraccept")
    Call<GenericResponseModel<JobResponseModel>> jobAcceptance(@Body JobRequestModel requestModel);

    @POST("job/answerreject")
    Call<GenericResponseModel<JobRejectResponseModel>> jobRejectance(@Body JobRequestModel requestModel);

    @POST("job/end")
    Call<GenericResponseModel<EndRideResponseModel>> jobEnd(@Body JobRequestModel requestModel);

    @POST("job/event")
    Call<GenericResponseModel> updateEvent(@Body UpdateEventRequestModel requestModel);

    @POST("job/updateStatus")
    Call<GenericResponseModel> updateStatus(@Body UpdateEventRequestModel requestModel);

    @GET("job/current/{vehicleid}")
    Call<GenericResponseModel<JobResponseModel>> getCurrentJob(@Path(value = "vehicleid") String vehicleid);

    @POST("drivermaster/updatetoken")
    Call<GenericResponseModel> updateToken(@Body TokenRequestModel tokenRequestModel);

    @GET("booking/driver/{vehicleid}")
    Call<GenericResponseModel<ArrayList<AllBookingResponseModel>>> getAllBookings(@Path(value = "vehicleid") String vehicleid);

    @GET("drivermaster/walletbalance/{vehicleid}")
    Call <GenericResponseModel<WalletResponseModel>> getWalletBalance(@Path(value = "vehicleid") String vehicleid);

    @GET("booking/earnings/{vehicleid}/{startDate}/{endDate}")
    Call <GenericResponseModel<ArrayList<EarningResponseModel>>> getAllEarnings(@Path(value = "vehicleid") String vehicleid,
                                                                                @Path(value = "startDate") String startDate,
                                                                                @Path(value = "endDate") String endDate);





/*    @PUT("posts/{id}")
    Call<Post> putPost(@Path("id") int id, @Body Post post);
*/
//
//    @POST("user")
//    Call<GenericResponseModel<CreateUserResponseModel>> createUser(@Body CreateUserRequestModel requestModel);
//
//    @POST("user/verifyOtp")
//    Call<GenericResponseModel<CreateUserResponseModel>> verfiOtp(@Body VerifyUserOtp verifyUserOtp);
//
//    @POST("user/verifyuser")
//    Call<GenericResponseModel<CreateUserResponseModel>> verifyWithNumberPassowwrd(@Body VerifyUserWithMobileAndPasswoadRequest request);

}