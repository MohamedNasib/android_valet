package com.icapp.icapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.icapp.icapp.Helpers.RealM;
import com.icapp.icapp.Interfaces.ICallback;
import com.icapp.icapp.Models.User;
import com.icapp.icapp.R;

public class LoginActivity extends ParentActivity implements View.OnClickListener, ICallback {
    Button btn_login;
    EditText et_userName, et_passCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_userName = findViewById(R.id.et_userName);
        et_passCode = findViewById(R.id.et_passCode);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
            String companyCode = et_userName.getText().toString();
            String passCode = et_passCode.getText().toString();
            if (companyCode.isEmpty()) {
                showToast(getString(R.string.please_enter_company_code));
            } else if (passCode.isEmpty()) {
                showToast(getString(R.string.please_enter_passcode));
            } else if (!sharedPrefs.get(R.string.COMPANY_CODE_KEY, "").equalsIgnoreCase(et_userName.getText().toString())) {
                showToast(getString(R.string.company_code_not_found));
            } else {
                User user = RealM.getInstance().getUser(et_passCode.getText().toString());
                if (user == null) {
                    showToast(getString(R.string.passcode_not_found));
                } else {
                    sharedPrefs.set(R.string.USER_ID_KEY, String.valueOf(user.getId()));
                    sharedPrefs.set(R.string.FIRST_NAME_KEY, user.getFirstName());
                    sharedPrefs.set(R.string.LAST_NAME_KEY, user.getLastName());
                    sharedPrefs.set(R.string.USER_IMAGE_KEY, user.getFaceImage());
                    startActivity(new Intent(this, MainActivity.class));
                    finishAffinity();
                }
            }
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
}