package com.icapp.icapp.Activities;

import static com.icapp.icapp.Helpers.Constants.ADDITIONAL_PHOTO_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.BACK_SIDE_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.DAMAGE_KEY;
import static com.icapp.icapp.Helpers.Constants.DAMAGE_PHOTO_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.FRONT_SIDE_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.LEFT_SIDE_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.RIGHT_SIDE_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.SIDE_KEY;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.icapp.icapp.Adapters.ReportDamageAdapter;
import com.icapp.icapp.Interfaces.IRecyclerInterface;
import com.icapp.icapp.Models.CarPart;
import com.icapp.icapp.R;

import java.util.ArrayList;

public class ReportDamageActivity extends ParentActivity implements IRecyclerInterface {
    RecyclerView rv_damage;
    View btnView;
    AppCompatButton btn_submit;
    ReportDamageAdapter reportDamageAdapter;
    int sideType;
    ArrayList<CarPart> lst = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sideType = getIntent().getIntExtra(SIDE_KEY, 0);

        switch (sideType) {
            case FRONT_SIDE_REQUEST_CODE:
                setContentView(R.layout.activity_report_damage_front);
                break;
            case LEFT_SIDE_REQUEST_CODE:
                setContentView(R.layout.activity_report_damage_left);
                break;
            case BACK_SIDE_REQUEST_CODE:
                setContentView(R.layout.activity_report_damage_back);
                break;
            case RIGHT_SIDE_REQUEST_CODE:
                setContentView(R.layout.activity_report_damage_right);
                break;
            case ADDITIONAL_PHOTO_REQUEST_CODE:
                break;
        }

        rv_damage = findViewById(R.id.rv_carDamage);
        btn_submit = findViewById(R.id.btn_submit);

        reportDamageAdapter = new ReportDamageAdapter(this, lst, this);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv_damage.setLayoutManager(lm);
        rv_damage.setAdapter(reportDamageAdapter);

        btn_submit.setOnClickListener(view -> {

            switch (sideType) {
                case FRONT_SIDE_REQUEST_CODE:
                    sharedPrefs.set(R.string.FRONT_IMAGE_DAMAGES_KEY, lst);
                    break;
                case LEFT_SIDE_REQUEST_CODE:
                    sharedPrefs.set(R.string.LEFT_IMAGE_DAMAGES_KEY, lst);
                    break;
                case BACK_SIDE_REQUEST_CODE:
                    sharedPrefs.set(R.string.BACK_IMAGE_DAMAGES_KEY, lst);
                    break;
                case RIGHT_SIDE_REQUEST_CODE:
                    sharedPrefs.set(R.string.RIGHT_IMAGE_DAMAGES_KEY, lst);
                    break;
            }
            finish();
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void onCLick(View v) {
        btnView = v;
        startActivityForResult(new Intent(this, PickSideActivity.class)
                .putExtra(SIDE_KEY, DAMAGE_PHOTO_REQUEST_CODE), DAMAGE_PHOTO_REQUEST_CODE);
    }

    @SuppressLint({"NotifyDataSetChanged", "UseCompatLoadingForDrawables"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == DAMAGE_PHOTO_REQUEST_CODE && resultCode == DAMAGE_PHOTO_REQUEST_CODE) {
                CarPart carPart = data.getParcelableExtra(DAMAGE_KEY);
                if (carPart != null) {
                    ImageButton imageButton = (ImageButton) btnView;
                    imageButton.setEnabled(false);
                    imageButton.setColorFilter(getColor(R.color._2F8B28), PorterDuff.Mode.SRC_IN);
                    carPart.setName(btnView.getTag().toString());
                    lst.add(carPart);
                    reportDamageAdapter.notifyDataSetChanged();
                    validate();
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDeletePressed(int position) {
        ImageButton imageButton = (ImageButton) findViewWithTagRecursively((ViewGroup) getWindow().getDecorView(), lst.get(position).getName());
        imageButton.setEnabled(true);
        imageButton.setColorFilter(getColor(R.color._E04F33), PorterDuff.Mode.SRC_IN);
        lst.remove(position);
        reportDamageAdapter.notifyItemRemoved(position);
        validate();
    }

    private void validate() {
        if (lst.isEmpty()) {
            ((CardView) btn_submit.getParent()).setVisibility(View.INVISIBLE);
        } else {
            ((CardView) btn_submit.getParent()).setVisibility(View.VISIBLE);
        }
    }
}