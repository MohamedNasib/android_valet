package com.icapp.icapp.Retrofit;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Window;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.icapp.icapp.Interfaces.ICallback;
import com.icapp.icapp.Models.Output.AddSignature.AddSignatureResponse;
import com.icapp.icapp.Models.Output.Arrival.ArrivalResponse;
import com.icapp.icapp.Models.Output.Arrivals.ArrivalsResponse;
import com.icapp.icapp.Models.Output.CheckCompany.CheckCompanyResponse;
import com.icapp.icapp.Models.Output.Login.UserData;
import com.icapp.icapp.Models.Output.Settings.SettingsResponse;
import com.icapp.icapp.Models.Output.UpdateCase.UpdateCaseResponse;
import com.icapp.icapp.Models.Output.UpdateStatus.UpdateStatusResponse;
import com.icapp.icapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {
    API API;
    Context context;
    Dialog progressDialog;
    boolean showDialog;

    public Retrofit(Context appContext, boolean showDialog) {
        this.context = appContext;
        this.showDialog = showDialog;
        final OkHttpClient okHttpClient = new OkHttpClient
                .Builder().readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(new RequestInterceptor(context))
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("https://stg-ic-api.wetechr.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        API = retrofit.create(API.class);

        progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog_layout);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    public void newUser(RequestBody companyCode, RequestBody passCode, MultipartBody.Part face_image, ICallback callback) {
        if (showDialog) {
            progressDialog.show();
        }
        Call<UserData> call = API.newUser(companyCode, passCode, face_image);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UserData> call, @NonNull Response<UserData> response) {
                if (showDialog) {
                    progressDialog.dismiss();
                }
                UserData userResponse = response.body();
                if (userResponse != null && userResponse.getData() != null) {
                    callback.onSuccess(response, "");
                } else {
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {
                            String errorBodyText = errorBody.string();
                            JSONObject errorObj = new JSONObject(errorBodyText);
                            String errorMessage = errorObj.getString("message");
                            // Handle the error message as needed
                            callback.onSuccessFail(errorMessage);
                        }
                    } catch (IOException | JSONException e) {
                        Log.d("TAG", Objects.requireNonNull(e.getMessage()));
                        // Handle exceptions
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserData> call, @NonNull Throwable t) {
                if (showDialog) {
                    progressDialog.dismiss();
                }
                callback.onFail(t.getMessage());
            }
        });
    }

//    public void arrival(Map<String, RequestBody> fields,
//            MultipartBody.Part front_image,
//            MultipartBody.Part left_image,
//            MultipartBody.Part back_image,
//            MultipartBody.Part right_image,
//            List<MultipartBody.Part> issues_photos,
//            List<MultipartBody.Part> additional_photos,
//                        ICallback callback) {
//        if (showDialog) {
////            progressDialog.show();
//        }
//        Call<ArrivalResponse> call = API.arrival(fields,
//                front_image,
//                left_image,
//                back_image,
//                right_image,
//                issues_photos,
//                additional_photos);
//        call.enqueue(new Callback<>() {
//            @Override
//            public void onResponse(@NonNull Call<ArrivalResponse> call, @NonNull Response<ArrivalResponse> response) {
//                if (showDialog) {
//                    progressDialog.dismiss();
//                }
//                if (response.code() == 201) {
//                    callback.onSuccess(response, "");
//                } else if (response.code() == 409 || response.code() == 422) {
//                    callback.onSuccess(response, "duplicate");
//                } else {
//                    try {
//                        ResponseBody errorBody = response.errorBody();
//                        if (errorBody != null) {
//                            String errorBodyText = errorBody.string();
//                            JSONObject errorObj = new JSONObject(errorBodyText);
//                            String errorMessage = errorObj.getString("message");
//                            // Handle the error message as needed
//                            callback.onSuccessFail(errorMessage);
//                        }
//                    } catch (IOException | JSONException e) {
//                        Log.d("TAG", Objects.requireNonNull(e.getMessage()));
//                        // Handle exceptions
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ArrivalResponse> call, @NonNull Throwable t) {
//                if (showDialog) {
//                    progressDialog.dismiss();
//                }
//                callback.onFail(t.getMessage());
//            }
//        });
//    }

    public void arrival(Map<String, RequestBody> fields,
                        MultipartBody.Part front_image,
                        MultipartBody.Part left_image,
                        MultipartBody.Part back_image,
                        MultipartBody.Part right_image,
                        MultipartBody.Part another_front_image,
                        MultipartBody.Part another_left_image,
                        MultipartBody.Part another_back_image,
                        MultipartBody.Part another_right_image,
                        MultipartBody.Part signature_image,
                        List<MultipartBody.Part> issues_photos,
                        List<MultipartBody.Part> additional_photos,
                        ICallback callback) {
        if (showDialog) {
//            progressDialog.show();
        }
        Call<ArrivalResponse> call = API.arrival(fields,
                front_image,
                left_image,
                back_image,
                right_image,
                another_front_image,
                another_left_image,
                another_back_image,
                another_right_image,
                signature_image,
                issues_photos,
                additional_photos);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrivalResponse> call, @NonNull Response<ArrivalResponse> response) {
                if (showDialog) {
                    progressDialog.dismiss();
                }
                if (response.code() == 201) {
                    callback.onSuccess(response, "");
                } else if (response.code() == 409 || response.code() == 422) {
                    callback.onSuccess(response, "duplicate");
                } else {
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {
                            String errorBodyText = errorBody.string();
                            JSONObject errorObj = new JSONObject(errorBodyText);
                            String errorMessage = errorObj.getString("message");
                            // Handle the error message as needed
                            callback.onSuccessFail(errorMessage);
                        }
                    } catch (IOException | JSONException e) {
                        Log.d("TAG", Objects.requireNonNull(e.getMessage()));
                        // Handle exceptions
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrivalResponse> call, @NonNull Throwable t) {
                if (showDialog) {
                    progressDialog.dismiss();
                }
                callback.onFail(t.getMessage());
            }
        });
    }

    public void getSettings(String companyCode, ICallback callback) {
        if (showDialog) {
//            progressDialog.show();
        }
        Call<SettingsResponse> call = API.getSettings(companyCode);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<SettingsResponse> call, @NonNull Response<SettingsResponse> response) {
                if (showDialog) {
//                    progressDialog.dismiss();
                }
                SettingsResponse userResponse = response.body();
                if (userResponse != null && userResponse.getData() != null) {
                    callback.onSuccess(response, "");
                } else {
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {
                            String errorBodyText = errorBody.string();
                            JSONObject errorObj = new JSONObject(errorBodyText);
                            String errorMessage = errorObj.getString("message");
                            // Handle the error message as needed
                            callback.onSuccessFail(errorMessage);
                        }
                    } catch (IOException | JSONException e) {
                        Log.d("TAG", Objects.requireNonNull(e.getMessage()));
                        // Handle exceptions
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SettingsResponse> call, @NonNull Throwable t) {
                if (showDialog) {
//                    progressDialog.dismiss();
                }
                callback.onFail(t.getMessage());
            }
        });
    }

    public void checkCompany(String companyCode, ICallback callback) {
        if (showDialog) {
            progressDialog.show();
        }
        Call<CheckCompanyResponse> call = API.checkCompany(companyCode);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CheckCompanyResponse> call, @NonNull Response<CheckCompanyResponse> response) {
                if (showDialog) {
                    progressDialog.dismiss();
                }
                CheckCompanyResponse userResponse = response.body();
                if (userResponse != null && userResponse.getData() != null) {
                    callback.onSuccess(response, "");
                } else {
                    callback.onSuccessFail(context.getString(R.string.company_code_not_found));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CheckCompanyResponse> call, @NonNull Throwable t) {
                if (showDialog) {
                    progressDialog.dismiss();
                }
                callback.onFail(t.getMessage());
            }
        });
    }

    public void getAllArrivals(String user_id, Long date, ICallback callback) {
        if (showDialog) {
//            progressDialog.show();
        }
        Call<ArrivalsResponse> call = API.getAllArrivals(user_id, date);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrivalsResponse> call, @NonNull Response<ArrivalsResponse> response) {
                if (showDialog) {
//                    progressDialog.dismiss();
                }
                ArrivalsResponse userResponse = response.body();
                if (userResponse != null && userResponse.getData() != null) {
                    callback.onSuccess(response, "");
                } else {
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {
                            String errorBodyText = errorBody.string();
                            JSONObject errorObj = new JSONObject(errorBodyText);
                            String errorMessage = errorObj.getString("message");
                            // Handle the error message as needed
                            callback.onSuccessFail(errorMessage);
                        }
                    } catch (IOException | JSONException e) {
                        Log.d("TAG", Objects.requireNonNull(e.getMessage()));
                        // Handle exceptions
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrivalsResponse> call, @NonNull Throwable t) {
                if (showDialog) {
//                    progressDialog.dismiss();
                }
                callback.onFail(t.getMessage());
            }
        });
    }

    public void addSignature(String reportID,
                        MultipartBody.Part signature_image,
                        RequestBody _method,
                        ICallback callback) {
        if (showDialog) {
//            progressDialog.show();
        }
        Call<AddSignatureResponse> call = API.addSignature(
                reportID,
                signature_image,
                _method);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AddSignatureResponse> call, @NonNull Response<AddSignatureResponse> response) {
                if (showDialog) {
//                    progressDialog.dismiss();
                }
                if (response.code() == 200) {
                    callback.onSuccess(response, "");
                } else {
                    callback.onSuccessFail(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddSignatureResponse> call, @NonNull Throwable t) {
                if (showDialog) {
//                    progressDialog.dismiss();
                }
                callback.onFail(t.getMessage());
            }
        });
    }

    public void updateStatus(String id,
                             RequestBody status,
                             RequestBody _method,
                             ICallback callback) {
        if (showDialog) {
//            progressDialog.show();
        }
        Call<UpdateStatusResponse> call = API.updateStatus(id, status, _method);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UpdateStatusResponse> call, @NonNull Response<UpdateStatusResponse> response) {
                if (showDialog) {
//                    progressDialog.dismiss();
                }
                UpdateStatusResponse userResponse = response.body();
                if (userResponse != null && userResponse.getData() != null) {
                    callback.onSuccess(response, "");
                } else {
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {
                            String errorBodyText = errorBody.string();
                            JSONObject errorObj = new JSONObject(errorBodyText);
                            String errorMessage = errorObj.getString("message");
                            // Handle the error message as needed
                            callback.onSuccessFail(errorMessage);
                        }
                    } catch (IOException | JSONException e) {
                        Log.d("TAG", Objects.requireNonNull(e.getMessage()));
                        // Handle exceptions
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpdateStatusResponse> call, @NonNull Throwable t) {
                if (showDialog) {
//                    progressDialog.dismiss();
                }
                callback.onFail(t.getMessage());
            }
        });
    }

    public void updateCase(Map<String, RequestBody> fields,
                        List<MultipartBody.Part> issues_photos,
                        ICallback callback) {
        if (showDialog) {
//            progressDialog.show();
        }
        Call<UpdateCaseResponse> call = API.updateCase(fields, issues_photos);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UpdateCaseResponse> call, @NonNull Response<UpdateCaseResponse> response) {
                if (showDialog) {
                    progressDialog.dismiss();
                }
                if (response.code() == 201) {
                    callback.onSuccess(response, "");
                } else if (response.code() == 409 || response.code() == 422) {
                    callback.onSuccess(response, "duplicate");
                } else {
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {
                            String errorBodyText = errorBody.string();
                            JSONObject errorObj = new JSONObject(errorBodyText);
                            String errorMessage = errorObj.getString("message");
                            // Handle the error message as needed
                            callback.onSuccessFail(errorMessage);
                        }
                    } catch (IOException | JSONException e) {
                        Log.d("TAG", Objects.requireNonNull(e.getMessage()));
                        // Handle exceptions
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpdateCaseResponse> call, @NonNull Throwable t) {
                if (showDialog) {
                    progressDialog.dismiss();
                }
                callback.onFail(t.getMessage());
            }
        });
    }
}