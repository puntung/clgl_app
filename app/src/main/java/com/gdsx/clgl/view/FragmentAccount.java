package com.gdsx.clgl.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gdsx.clgl.R;

/**
 * Created by mglory on 2015/12/18.
 */
public class FragmentAccount extends Fragment implements View.OnClickListener{

    private LinearLayout mcredit_check;
    private LinearLayout muploaded_check;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, null);
        mcredit_check = (LinearLayout)view.findViewById(R.id.account_credit);
        muploaded_check = (LinearLayout)view.findViewById(R.id.account_uploaded);
        mcredit_check.setOnClickListener(this);
        muploaded_check.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.account_credit:
                break;
            case R.id.account_uploaded:
                startActivity(new Intent(getActivity(),UploadedRecordActivity.class));
                break;
        }
    }
}
