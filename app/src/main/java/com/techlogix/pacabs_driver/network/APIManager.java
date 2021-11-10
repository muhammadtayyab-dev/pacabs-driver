package com.techlogix.pacabs_driver.network;

import android.app.Dialog;

import com.techlogix.pacabs_driver.PacabDriver;
import com.techlogix.pacabs_driver.activities.BaseActivity;
import com.techlogix.pacabs_driver.activities.DashboardActivity;
import com.techlogix.pacabs_driver.enumirations.ErrorDescription;
import com.techlogix.pacabs_driver.enumirations.StatusIDEnums;
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
import com.techlogix.pacaps.dialogs.PacapDialog;
import com.techlogix.pacaps.utility.Utility;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class APIManager {
    public static APIManager instance = new APIManager();
    public static Retrofit retrofit;

    public GenericResponseModel getResponseModel() {
        return responseModel;
    }

    public GenericResponseModel responseModel;

    public static APIManager getInstance() {

        return instance;
    }

    private APIManager() {
        retrofit = ClientInstance.getRetrofitInstance();
    }


    public void getAllCities(CallbackGenric callback) {
        GetDataService service = retrofit.create(GetDataService.class);
        Call<GenericResponseModel<ArrayList<CititesResponseModel>>> result = service.getAllCities();
        sendResultGeneric(result, callback, Utility.Companion.getLOV_CITIES(), true);
    }

    public void getAllVehicleTypes(CallbackGenric callback) {
        GetDataService service = retrofit.create(GetDataService.class);
        Call<GenericResponseModel<ArrayList<VehiclesResponseModel>>> result = service.getAllVehicleTypes();
        sendResultGeneric(result, callback, Utility.Companion.getLOV_VEHICLES(), true);
    }

    public void getAllServiceTypes(CallbackGenric callback) {
        GetDataService service = retrofit.create(GetDataService.class);
        Call<GenericResponseModel<ArrayList<ServiceType>>> result = service.getAllServiceTypes();
        sendResultGeneric(result, callback, Utility.Companion.getLOV_SERVICE_TYPE(), true);
    }

    public void createDriver(CreateDriverRequestModel requestModel, CallbackGenric callback) {
        GetDataService service = retrofit.create(GetDataService.class);
        Call<GenericResponseModel<CreateDriverResponseModel>> result = service.createDriver(requestModel);
        sendResultGeneric(result, callback, 0, true);
    }

    public void loginDriver(LoginDriverRequestModel requestModel, CallbackGenric callback) {
        GetDataService service = retrofit.create(GetDataService.class);
        Call<GenericResponseModel<CreateDriverResponseModel>> result = service.loginDriver(requestModel);
        sendResultGeneric(result, callback, 0, true);
    }

    public void verifyOtp(VerifyOTPRequestModel requestModel, CallbackGenric callback) {
        GetDataService service = retrofit.create(GetDataService.class);
        Call<GenericResponseModel<CreateDriverResponseModel>> result   = service.verifyOTP(requestModel);
        sendResultGeneric(result, callback, 0, true);
    }

    public void updateAvailability(String vehicleId, int status, CallbackGenric callback) {
        GetDataService service = retrofit.create(GetDataService.class);
        Call<GenericResponseModel> result = service.updateAvailability(vehicleId, status);
        sendResultGeneric(result, callback, Utility.Companion.getUPDATE_AVAILABILITY(), false);
    }

    public void polling(PollingRequestModel requestModel, CallbackGenric callback) {
        GetDataService service = retrofit.create(GetDataService.class);
        Call<GenericResponseModel<PollingResponseModel>> result = service.polling(requestModel);
        sendResultGeneric(result, callback, Utility.Companion.getPOLLING(), false);
    }

    public void jobRequest(String vehicleId, CallbackGenric callback) {
        GetDataService service = retrofit.create(GetDataService.class);
        Call<GenericResponseModel<JobResponseModel>> result = service.jobRequest(vehicleId);
        sendResultGeneric(result, callback, Utility.Companion.getJOB_REQUEST(), false);
    }

    public void jobAcknowledegment(JobRequestModel requestModel, CallbackGenric callback) {
        GetDataService service = retrofit.create(GetDataService.class);
        Call<GenericResponseModel> result = service.jobAcknowledegment(requestModel);
        sendResultGeneric(result, callback, Utility.Companion.getJOB_ACKNWOLEDGE(), false);
    }

    public void jobAcceptance(JobRequestModel requestModel, CallbackGenric callback) {
        GetDataService service = retrofit.create(GetDataService.class);
        Call<GenericResponseModel<JobResponseModel>> result = service.jobAcceptance(requestModel);
        sendResultGeneric(result, callback, Utility.Companion.getACCEPT_JOB_REQUEST(), true);
    }

    public void jobRejectance(JobRequestModel requestModel, CallbackGenric callback) {
        GetDataService service = retrofit.create(GetDataService.class);
        Call<GenericResponseModel<JobRejectResponseModel>> result = service.jobRejectance(requestModel);
        sendResultGeneric(result, callback, Utility.Companion.getREJECT_JOB_REQUEST(), true);
    }

    public void jobEnd(JobRequestModel requestModel, CallbackGenric callback) {
        GetDataService service = retrofit.create(GetDataService.class);
        Call<GenericResponseModel<EndRideResponseModel>> result = service.jobEnd(requestModel);
        sendResultGeneric(result, callback, Utility.Companion.getEND_JOB_REQUEST(), true);
    }

    public void updateEvent(UpdateEventRequestModel requestModel, CallbackGenric callback) {
        GetDataService service = retrofit.create(GetDataService.class);
        Call<GenericResponseModel> result = service.updateEvent(requestModel);
        sendResultGeneric(result, callback, (int) StatusIDEnums.Completed_Ride.getStatusID().getValue(), true);
    }

    public void updateStatus(UpdateEventRequestModel requestModel, CallbackGenric callback) {
        GetDataService service = retrofit.create(GetDataService.class);
        Call<GenericResponseModel> result = service.updateStatus(requestModel);
        sendResultGeneric(result, callback, (int) StatusIDEnums.Started_Ride.getStatusID().getValue(), true);
    }

    public void getCurrentJob(String vehicleId, CallbackGenric callback) {
        GetDataService service = retrofit.create(GetDataService.class);
        Call<GenericResponseModel< JobResponseModel>> result = service.getCurrentJob(vehicleId);
        sendResultGeneric(result, callback, Utility.Companion.getCURRENT_JOB_REQUEST(), true);
    }

    public void updateToken(TokenRequestModel tokenRequestModel, CallbackGenric callback) {
        GetDataService service = retrofit.create(GetDataService.class);
        Call<GenericResponseModel> result = service.updateToken(tokenRequestModel);
        sendResultGeneric(result, callback, Utility.Companion.getUPDATE_TOKEN(), true);
    }


    public void getAllMyBookings(String vehicleId, CallbackGenric callback) {
        GetDataService service = retrofit.create(GetDataService.class);
        Call<GenericResponseModel<ArrayList<AllBookingResponseModel>>> result = service.getAllBookings(vehicleId);
        sendResultGeneric(result, callback, Utility.Companion.getLOV_SERVICE_TYPE(), true);
    }


    public void getWalletBalance(String vehicleId, CallbackGenric callback) {
        GetDataService service = retrofit.create(GetDataService.class);
        Call<GenericResponseModel<WalletResponseModel>> result = service.getWalletBalance(vehicleId);
        sendResultGeneric(result, callback,0,  true);
    }

    public void getAllEarnings(String vehicleId,String startDate, String endDate, CallbackGenric callback) {
        GetDataService service = retrofit.create(GetDataService.class);
        Call<GenericResponseModel<ArrayList<EarningResponseModel>>> result = service.getAllEarnings(vehicleId,startDate,endDate);
        sendResultGeneric(result, callback,0,  true);
    }

//    public void createUser(CreateUserRequestModel requestModel, CallbackGenric callback)A {
//        GetDataService service = retrofit.create(GetDataService.class);
//        Call<GenericResponseModel<CreateUserResponseModel>> result = service.createUser(requestModel);
//        sendResultGeneric(result, callback, 0);
//    }


//    public void verifyOtp(VerifyUserOtp requestModel, CallbackGenric callback) {
//        GetDataService service = retrofit.create(GetDataService.class);
//        Call<GenericResponseModel<CreateUserResponseModel>> result = service.verfiOtp(requestModel);
//        sendResultGeneric(result, callback, 0);
//    }


//    public void verifyWithNumberPassowwrd(VerifyUserWithMobileAndPasswoadRequest requestModel, CallbackGenric callback) {
//        GetDataService service = retrofit.create(GetDataService.class);
//        Call<GenericResponseModel<CreateUserResponseModel>> result = service.verifyWithNumberPassowwrd(requestModel);
//        sendResultGeneric(result, callback, 0);
//    }

    private <T> void sendResultGeneric(Call<T> call, final CallbackGenric result, int rc, Boolean showProgress) {
        PacapDialog dialog = null;

        if (showProgress) {
            if (Objects.requireNonNull(PacabDriver.Companion.getINSTANCE()).getBaseActivity() != null && !Objects.requireNonNull(PacabDriver.Companion.getINSTANCE().getBaseActivity()).isFinishing()) {
                dialog = new PacapDialog(PacabDriver.Companion.getINSTANCE().getBaseActivity());
            }
            if (dialog != null)
                dialog.show();
        }
        final Dialog finalDialog = dialog;

        call.clone().enqueue(new retrofit2.Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {

                if (finalDialog != null)
                    finalDialog.dismiss();
                if (response.code() == 200 || response.code() == 201) {

                    GenericResponseModel<T> genericResponseModel = (GenericResponseModel<T>) response.body();
                   /* if (!genericResponseModel.getStatus()  ) {

//                        ((BaseActivity) PacabDriver.Companion.getINSTANCE().getBaseActivity()).showErrorDialog("Error response", genericResponseModel.getMessage() + "\n" +
//                                ((genericResponseModel.getError() == null ||
//                                        genericResponseModel.getError().isEmpty()) ? "" : genericResponseModel.getError()), genericResponseModel);

                    } else*/
                    {
                        responseModel = genericResponseModel;
                        result.onResult(genericResponseModel, rc);
                    }
                } else {

                    ((BaseActivity) PacabDriver.Companion.getINSTANCE().getBaseActivity()).showErrorDialog("Error Api", response.message(), new GenericResponseModel(false, response.message(), null, response.message()));
//                    result.onResult(new GenericResponseModel(false, response.message(), null, response.message()), rc);
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {

                if(rc==Utility.Companion.getPOLLING())
                {
                    DashboardActivity dashboardActivity= new DashboardActivity();
                    dashboardActivity.getPolling();
                }
                else if(rc==Utility.Companion.getJOB_REQUEST())
                {
                    DashboardActivity dashboardActivity= new DashboardActivity();
                    dashboardActivity.getJobRequest();
                }
                else {

                    if (showProgress) {
                        if (finalDialog != null)
                            finalDialog.dismiss();
                    }
                    ErrorDescription errorDescription = ErrorDescription.GENERAL_ERROR;
                    if (t instanceof java.net.UnknownHostException || t instanceof java.net.ConnectException) {

                        errorDescription = ErrorDescription.SERVER_RESPONDING;
                    } else if (t instanceof JSONException) {

                        errorDescription = ErrorDescription.INVALID_RESPONCE;
                    }
                    //                    ((BaseActivity) PacabDriver.Companion.getINSTANCE().getBaseActivity()).showErrorDialog("Retrfofit Error", errorDescription.ed.desc, new GenericResponseModel(false, errorDescription.ed.desc, null, errorDescription.ed.desc));
                    t.printStackTrace();
                }
            }
        });
    }

    public interface Callback {
        void onResult(boolean z, String response);
    }

    public interface CallbackGenric<T> {
        void onResult(GenericResponseModel<T> response, int requestCode);

//        void onError(String error, int requestCode);
    }
}