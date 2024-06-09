package com.icapp.icapp.Activities;

import static com.icapp.icapp.Helpers.Constants.FACE_KEY;
import static com.icapp.icapp.Helpers.Constants.FACE_KEY_CODE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.icapp.icapp.Interfaces.ICallback;
import com.icapp.icapp.Models.Output.Login.UserData;
import com.icapp.icapp.R;

import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class RegisterActivity extends ParentActivity implements View.OnClickListener , ICallback {
    Button btn_register;
    EditText et_userName, et_passCode;
    RelativeLayout rl_btn_face_id;
    MultipartBody.Part faceImageBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_userName = findViewById(R.id.et_userName);
        et_passCode = findViewById(R.id.et_passCode);
        btn_register = findViewById(R.id.btn_register);
        rl_btn_face_id = findViewById(R.id.rl_btn_face_id);
        rl_btn_face_id.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rl_btn_face_id) {
            Intent intent = new Intent(this, SetupFaceIDActivity.class);
            startActivityForResult(intent, FACE_KEY_CODE);
        } else if (view.getId() == R.id.btn_register) {
            String companyCode = et_userName.getText().toString();
            String passCode = et_passCode.getText().toString();
            if (companyCode.isEmpty()) {
                showToast(getString(R.string.please_enter_company_code));
            } else if (passCode.isEmpty()) {
                showToast(getString(R.string.please_enter_passcode));
            } else if (faceImageBody == null) {
                showToast(getString(R.string.please_face_identity));
            } else if (!sharedPrefs.get(R.string.COMPANY_CODE_KEY, "").equalsIgnoreCase(et_userName.getText().toString())) {
                showToast(getString(R.string.company_code_not_found));
            } else {
                retrofit.newUser(RequestBody.create(MediaType.parse("text/plain"), et_userName.getText().toString()),
                        RequestBody.create(MediaType.parse("text/plain"), et_passCode.getText().toString()),
                        faceImageBody, this);
            }
        }
    }

    @Override
    public void onSuccess(Response response, String msg) {
        if (response.body() instanceof UserData) {
            handleLoginResponse((UserData) response.body());
        }
    }

    @Override
    public void onSuccessFail(String msg) {
        super.onSuccessFail(msg);
    }

    @Override
    public void onFail(String msg) {
        super.onFail(msg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String filePath = data.getStringExtra(FACE_KEY);
            File scanImageFile = new File(Objects.requireNonNull(filePath));
            RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), scanImageFile);
            faceImageBody = MultipartBody.Part.createFormData("face_image", scanImageFile.getName(), imageRequestBody);
        }
    }
}