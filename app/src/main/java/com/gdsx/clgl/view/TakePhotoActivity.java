package com.gdsx.clgl.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Observable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gdsx.clgl.R;
import com.gdsx.clgl.tricks.Constants;
import com.gdsx.clgl.tricks.PhotoAdapter;
import com.gdsx.clgl.utils.ReadImg2Binary2;
import com.google.gson.JsonObject;
import com.square.github.restrofit.ClglClient;
import com.square.github.restrofit.ServiceGenerator;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by mglory on 2015/12/21.
 */
public class TakePhotoActivity extends Activity implements View.OnClickListener{
    RecyclerView recyclerView;
    PhotoAdapter photoAdapter;
    ArrayList<String> selectedPhotos = new ArrayList<>();
    double[] wc = new double[2];
    private ImageView back_imgv;
    private TextView submit_tv;
    private LinearLayout loc_ly;
    private ClglClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takephoto);
        initView();
    }

    private void initView(){
        client = ServiceGenerator.createService(ClglClient.class,Constants.BASE_URL);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        photoAdapter = new PhotoAdapter(this,selectedPhotos);
        back_imgv = (ImageView)findViewById(R.id.pic_select_back);
        submit_tv = (TextView)findViewById(R.id.pic_select_submit);
        loc_ly = (LinearLayout)findViewById(R.id.at_location);
        back_imgv.setOnClickListener(this);
        submit_tv.setOnClickListener(this);
        loc_ly.setOnClickListener(this);
        //select mode with one-pic  so the num of grid is 1
        // if select mode is  nine-pic ,change number  4
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, OrientationHelper.VERTICAL));
        recyclerView.setAdapter(photoAdapter);
        PhotoPickerIntent intent = new PhotoPickerIntent(this);
        //select mode 1 or 9
        intent.setPhotoCount(1);
        intent.setShowCamera(true);
        startActivityForResult(intent, Constants.REQUEST_CODE_PIC);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<String> photos = null;
        if (resultCode == RESULT_OK && requestCode == Constants.REQUEST_CODE_PIC) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();
            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            photoAdapter.notifyDataSetChanged();
            wc = getCurrentLocation();

        }else if(resultCode == RESULT_OK && requestCode == Constants.REQUEST_CODE_LOC){
            Log.i("LOC","trun back to mainactiviry");
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
                Log.i("Select-pic", selectedPhotos.toString());
                String imguri = selectedPhotos.get(0);
                JsonObject datajson = new JsonObject();
                datajson.addProperty("img", ReadImg2Binary2.imgToBase64(imguri));
                datajson.addProperty("wc",wc[0]+","+wc[1]);
                Log.i("data",datajson.toString());
                Uploadpic(datajson);
                break;
            case R.id.at_location:
                Toast.makeText(this,"("+wc[0]+","+wc[1]+")",Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private void Uploadpic(JsonObject json){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("正在上传中...");
        pd.show();
        //RxJava
       client.upload(json)
               .observeOn(AndroidSchedulers.mainThread()) //Subscriber 回调发生在主线程
               .subscribe(new Observer<JsonObject>() {
                   @Override
                   public void onCompleted() {
                       Log.i("success", "SUCCESS");
                       pd.dismiss();
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
                       Log.i("data", jsonObject.toString());
                       String id = jsonObject.get("id").toString();
                       Toast.makeText(getApplication(), "上传成功，ID为 " + id, Toast.LENGTH_SHORT).show();
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
