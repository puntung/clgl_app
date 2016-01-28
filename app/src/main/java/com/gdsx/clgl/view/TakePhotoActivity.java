package com.gdsx.clgl.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gdsx.clgl.R;
import com.gdsx.clgl.database.DataHelper;
import com.gdsx.clgl.entity.UploadRecord;
import com.gdsx.clgl.tricks.Constants;
import com.gdsx.clgl.tricks.PhotoAdapter;
import com.gdsx.clgl.utils.ReadImg2Binary2;
import com.google.gson.JsonObject;
import com.square.github.restrofit.ClglClient;
import com.square.github.restrofit.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.entity.PhotoDirectory;
import me.iwf.photopicker.utils.ImageCaptureManager;
import me.iwf.photopicker.utils.PhotoPickerIntent;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by mglory on 2015/12/21.
 */
public class TakePhotoActivity extends Activity implements View.OnClickListener{
    RecyclerView recyclerView;
    PhotoAdapter photoAdapter;
    private ImageCaptureManager captureManager;
    ArrayList<String> selectedPhotos = new ArrayList<>();
    private List<PhotoDirectory> directories;
    double[] wc = new double[2];
    private ImageView back_imgv;
    private TextView submit_tv;
    private ClglClient client;
    private DataHelper dh = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takephoto);
        initView();
    }

    private void initView(){
        client = ServiceGenerator.createService(ClglClient.class,Constants.BASE_URL);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        captureManager = new ImageCaptureManager(this);
        dh = new DataHelper(this);
        photoAdapter = new PhotoAdapter(this,selectedPhotos);
        directories = new ArrayList<>();
        back_imgv = (ImageView)findViewById(R.id.pic_select_back);
        submit_tv = (TextView)findViewById(R.id.pic_select_submit);
        back_imgv.setOnClickListener(this);
        submit_tv.setOnClickListener(this);

        switchIntent();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<String> photos = null;
        if (resultCode == RESULT_OK && requestCode == Constants.REQUEST_CODE_PIC) {
            if (data != null) {
                //相册选择
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }else {
                //拍照获取
                captureManager.galleryAddPic();
                String path = captureManager.getCurrentPhotoPath();
                wc = getCurrentLocation();
                UploadRecord ur = new UploadRecord();
                ur.setPath(path);
                ur.setWc(wc[0]+","+wc[1]);
                dh.insert(ur);
                photos.add(path);
            }
            selectedPhotos.clear();
            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            photoAdapter.notifyDataSetChanged();
        } else {
            finish();
        }
    }
    public void previewPhoto(Intent intent) {
        startActivityForResult(intent, Constants.REQUEST_CODE_PIC);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pic_select_back:
                finish();
                break;
            case R.id.pic_select_submit:
                boolean iscontain = false;
                List<UploadRecord> mr = new ArrayList<>();
                for (String s : selectedPhotos){
                    UploadRecord ur = dh.queryfromPath(s);
                    if (ur.getPath()==null){
                        ur.setPath(s);
                        ur.setWc("");
                        iscontain = true;
                    }
                    mr.add(ur);
                }
                if (iscontain){
                    TipDialog(mr);
                }else {
                    uploadPic(mr);
                }
                break;
        }
    }

    private void TipDialog(final List<UploadRecord> mr){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("注意");
        builder.setMessage("选择的文件中包含未获取位置信息的图片，是否继续上传");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                uploadPic(mr);
            }
        });
        builder.create().show();
    }

    private void switchIntent(){
        Intent originIntent = getIntent();
        int postion = originIntent.getIntExtra("position",-1);
        switch (postion){
            case 0 :
                try {
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, OrientationHelper.VERTICAL));
                    recyclerView.setAdapter(photoAdapter);
                    Intent intent = captureManager.dispatchTakePictureIntent();
                    startActivityForResult(intent, Constants.REQUEST_CODE_PIC);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case 1:
                if (selectedPhotos.size()==1){
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, OrientationHelper.VERTICAL));
                }else {
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
                }
                recyclerView.setAdapter(photoAdapter);
                PhotoPickerIntent intent = new PhotoPickerIntent(this);
                intent.setPhotoCount(9);
                intent.setShowCamera(false);
                startActivityForResult(intent, Constants.REQUEST_CODE_PIC);
                break;
            case -1:
                break;
        }
    }

    private void uploadPic(final List<UploadRecord> list){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("上传中...   (1/"+list.size()+")");
        pd.setCancelable(false);
        pd.show();
        AsynPostWithRxJava(pd,list,1);

    }

    private void AsynPostWithRxJava(final ProgressDialog pd,final List<UploadRecord> list, final int index){
        final JsonObject datajson = new JsonObject();
        datajson.addProperty("img", ReadImg2Binary2.imgToBase64(list.get(index-1).getPath()));
        datajson.addProperty("wc", list.get(index-1).getWc());
        Log.i("datajson-->",datajson.toString());
        //RxJava
        client.upload(datajson)
                .observeOn(AndroidSchedulers.mainThread()) //Subscriber 回调发生在主线程
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        Log.i("success", "SUCCESS");
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(getApplication(), "上传失败", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        int eff = dh.update(list.get(index-1).getPath());
                        Log.i("eff=====>", eff + "");

                        if (list.size() > index) {
                            int mi = index+1;
                            AsynPostWithRxJava(pd, list,mi);
                            pd.setMessage("上传中...   (" + mi + "/" + list.size() + ")");
                        } else {
                            //String id = jsonObject.get("id").toString();
                            list.clear();
                            Toast.makeText(getApplication(), "上传成功", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }

                    }
                });
    }

    private double[] getCurrentLocation(){
        final double[] mwc = new double[2];
        LocationManager lm =(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //GPS是否打开
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("GPS提示");
            builder.setMessage("只有开启GPS才能记录当前位置，请开启GPS定位模式");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("开启", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, Constants.REQUEST_CODE_LOC);
                }
            });
            builder.create().show();
            return  null;
        }


        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("正在获取位置");
        pd.show();
        String bestProvider = lm.getBestProvider(getCriteria(), true);
        lm.requestLocationUpdates(bestProvider, 100 * 1000, 500, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mwc[0] = location.getLongitude();
                mwc[1] = location.getLatitude();
                pd.dismiss();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
        return mwc;
    }

    /**
     * 返回查询条件
     * @return
     */
    private Criteria getCriteria(){
        Criteria criteria=new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        //设置是否需要方位信息
        criteria.setBearingRequired(false);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }
}
