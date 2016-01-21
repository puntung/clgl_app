package com.gdsx.clgl.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gdsx.clgl.R;
import com.gdsx.clgl.tricks.HomeGridViewAdapter;

/**
 * Created by mglory on 2015/12/18.
 */
public class FragmentNotUpload extends Fragment implements AdapterView.OnItemClickListener{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notupload, null);
        initView();
        return view;
    }

    public void initView(){

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
            case 1:
                break;
        }
    }
}
