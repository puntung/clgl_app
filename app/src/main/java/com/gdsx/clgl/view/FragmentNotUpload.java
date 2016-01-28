package com.gdsx.clgl.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.gdsx.clgl.R;
import com.gdsx.clgl.database.DataHelper;
import com.gdsx.clgl.entity.UploadRecord;
import com.gdsx.clgl.tricks.HomeGridViewAdapter;
import com.gdsx.clgl.tricks.NotUploadPicAdapter;
import com.gdsx.clgl.tricks.UploadedPicAdapter;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mglory on 2015/12/18.
 */
public class FragmentNotUpload extends Fragment implements View.OnClickListener{
    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notupload, null);
        recyclerView = (RecyclerView)view.findViewById(R.id.not_upload_rcv);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lm);
        AsynGetPic();
        return view;
    }

    public void AsynGetPic(){

        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                DataHelper dh = new DataHelper(getActivity());
                List<UploadRecord> mlist = dh.getNotUploadedPic();
                subscriber.onNext(mlist);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {
                        Log.i("completet", "complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Object o) {
                        List<UploadRecord> urlist = (List<UploadRecord>)o;
                        Log.i("list",urlist.toString());
                        NotUploadPicAdapter adapter = new NotUploadPicAdapter(getActivity());
                        adapter.setDataList(urlist);
                        recyclerView.setAdapter(adapter);
                    }
                });
    }

    @Override
    public void onClick(View v) {

    }
}
