package com.gdsx.clgl.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdsx.clgl.R;
import com.gdsx.clgl.tricks.HomeGridViewAdapter;

import java.util.ArrayList;

/**
 * Created by mglory on 2015/12/18.
 */
public class FragmentAlreadyUploade extends Fragment implements View.OnClickListener{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alreadyupload, null);
        return view;
    }



    @Override
    public void onClick(View v) {

    }
}
