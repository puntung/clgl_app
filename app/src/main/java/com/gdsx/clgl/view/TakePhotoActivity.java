package com.gdsx.clgl.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Observable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
    private ImageView back_imgv;
    private TextView submit_tv;
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
        back_imgv.setOnClickListener(this);
        submit_tv.setOnClickListener(this);
        //select mode with one-pic  so the num of grid is 1
        // if select mode is  nine-pic ,change number  4
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, OrientationHelper.VERTICAL));
        recyclerView.setAdapter(photoAdapter);
        PhotoPickerIntent intent = new PhotoPickerIntent(this);
        //select mode 1 or 9
        intent.setPhotoCount(1);
        intent.setShowCamera(true);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<String> photos = null;
        if (resultCode == RESULT_OK && requestCode == Constants.REQUEST_CODE) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();
            if (photos != null) {
                selectedPhotos.addAll(photos);
            }
            photoAdapter.notifyDataSetChanged();

        }else {
           finish();
        }
    }
    public void previewPhoto(Intent intent) {
        startActivityForResult(intent, Constants.REQUEST_CODE);
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
                //Log.i("IMGAE-BASE64",ReadImg2Binary2.imgToBase64(imguri));
                JsonObject datajson = new JsonObject();
                datajson.addProperty("img", ReadImg2Binary2.imgToBase64(imguri));
                datajson.addProperty("wc","");
                Log.i("data",datajson.toString());
                Uploadpic(datajson);
                break;
        }
    }

    private void Uploadpic(JsonObject json){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("正在上传中...");
        pd.show();
        //RxJava
       client.upload(json)
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Observer<JsonObject>() {
                   @Override
                   public void onCompleted() {
                       Log.i("success","SUCCESS");
                       pd.dismiss();
                       finish();
                   }

                   @Override
                   public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(getApplication(),"上传失败",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                   }

                   @Override
                   public void onNext(JsonObject jsonObject) {
                        Log.i("data",jsonObject.toString());
                        String id = jsonObject.get("id").toString();
                        Toast.makeText(getApplication(),"上传成功，ID为 "+id,Toast.LENGTH_SHORT).show();
                   }
               });

    }
}
