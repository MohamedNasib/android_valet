package com.icapp.icapp.Retrofit;

import com.icapp.icapp.Models.Input.Login.LoginInputParams;
import com.icapp.icapp.Models.Output.AddSignature.AddSignatureResponse;
import com.icapp.icapp.Models.Output.Arrival.ArrivalResponse;
import com.icapp.icapp.Models.Output.Arrivals.ArrivalsResponse;
import com.icapp.icapp.Models.Output.CheckCompany.CheckCompanyResponse;
import com.icapp.icapp.Models.Output.Login.UserData;
import com.icapp.icapp.Models.Output.Settings.SettingsResponse;
import com.icapp.icapp.Models.Output.UpdateCase.UpdateCaseResponse;
import com.icapp.icapp.Models.Output.UpdateStatus.UpdateStatusResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {
    @POST("login")
    Call<UserData> login(@Body LoginInputParams params);

//    @Multipart
//    @POST("arrival")
//    Call<ArrivalResponse> arrival(@PartMap Map<String, RequestBody> fields,
//                                  @Part MultipartBody.Part front_image,
//                                  @Part MultipartBody.Part left_image,
//                                  @Part MultipartBody.Part back_image,
//                                  @Part MultipartBody.Part right_image,
//                                  @Part List<MultipartBody.Part> issues_photos,
//                                  @Part List<MultipartBody.Part> additional_photos);

    @Multipart
    @POST("arrival")
    Call<ArrivalResponse> arrival(@PartMap Map<String, RequestBody> fields,
                                  @Part MultipartBody.Part front_image,
                                  @Part MultipartBody.Part left_image,
                                  @Part MultipartBody.Part back_image,
                                  @Part MultipartBody.Part right_image,
                                  @Part MultipartBody.Part another_front_image,
                                  @Part MultipartBody.Part another_left_image,
                                  @Part MultipartBody.Part another_back_image,
                                  @Part MultipartBody.Part another_right_image,
                                  @Part MultipartBody.Part signature_image,
                                  @Part List<MultipartBody.Part> issues_photos,
                                  @Part List<MultipartBody.Part> additional_photos);

    @Multipart
    @POST("users")
    Call<UserData> newUser(@Part("company_code") RequestBody company_code, @Part("pass_code") RequestBody pass_code, @Part MultipartBody.Part face_image);

    @GET("companies/{companyCode}/settings")
    Call<SettingsResponse> getSettings(@Path("companyCode") String companyCode);

    @GET("companies/{companyCode}")
    Call<CheckCompanyResponse> checkCompany(@Path("companyCode") String companyCode);

    @GET("arrival")
    Call<ArrivalsResponse> getAllArrivals(@Query("user_id") String user_id,
                                          @Query("date") Long date);

    @Multipart
    @POST("arrival/{ID}")
    Call<AddSignatureResponse> addSignature(@Path("ID") String ID,
                                            @Part MultipartBody.Part signature_image,
                                            @Part("_method") RequestBody _method);
    @Multipart
    @POST("tickets/{ID}/update-status")
    Call<UpdateStatusResponse> updateStatus(@Path("ID") String ID,
                                            @Part("status") RequestBody status,
                                            @Part("_method") RequestBody _method);

    @Multipart
    @POST("update-case")
    Call<UpdateCaseResponse> updateCase(@PartMap Map<String, RequestBody> fields,
                                        @Part List<MultipartBody.Part> issues_photos);
}