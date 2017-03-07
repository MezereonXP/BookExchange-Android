package com.example.mezereon.bookexchange;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mezereon.bookexchange.Component.DaggerAppComponent;
import com.example.mezereon.bookexchange.Module.Book;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class AddArticleActivity extends AppCompatActivity {

    private boolean HAVE_CHOOSED=false;
    private String picPath;
    private SharedPreferences sharePreference;
    private ProgressDialog progressDialog;

    @BindView(R.id.editText7)
    EditText title;
    @BindView(R.id.editText8)
    EditText bookName;
    @BindView(R.id.editText9)
    EditText content;
    @BindView(R.id.button8)
    Button takeFile;
    @BindView(R.id.imageView6)
    ImageView bookPic;
    @BindView(R.id.button9)
    Button send;

    @Inject
    Retrofit retrofit;

    public interface SendArticleService {
        @Multipart
        @POST("uparticle.php")
        Observable<Void> send(@Part List<MultipartBody.Part> partList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheStatusBar();
        setContentView(R.layout.activity_add_article);
        bindAllTheView();
        initSharePreference();
        initProgressDialog();
        injectByDagger();
        setTheTakePhoteButton();
        setTheSendButton();
    }

    private void initProgressDialog() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("发表书评中....");
        progressDialog.setTitle("System");
    }

    private void initSharePreference() {
        sharePreference=getSharedPreferences("USERINFO",MODE_PRIVATE);
    }

    private void injectByDagger() {
        DaggerAppComponent.builder().build().inject(this);
    }

    private void setTheSendButton() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookName.getText().length()!=0&&content.getText().length()!=0
                        &&title.getText().length()!=0&&HAVE_CHOOSED){
                    progressDialog.show();
                    pressThePic();
                }else{
                    Toast.makeText(AddArticleActivity.this,"请完整填写信息",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendToServer() {
        SendArticleService sendArticleService=retrofit.create(SendArticleService.class);
        Subscription subcription = sendArticleService.send(getList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() { }
                    @Override
                    public void onError(Throwable e) {
                        Log.d("error",e.toString());
                    }
                    @Override
                    public void onNext(Void aVoid) {
                        progressDialog.dismiss();
                        Toasty.success(AddArticleActivity.this,"上传成功",Toast.LENGTH_SHORT,true).show();
                        finish();
                    }
                });
    }

    private List<MultipartBody.Part> getList() {
        File file = new File(picPath);//filePath 图片地址
        String username = sharePreference.getString("USERNAME","NONE");
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)//表单类型
                .addFormDataPart("username", username)
                .addFormDataPart("bookname",bookName.getText().toString())
                .addFormDataPart("authorpic",sharePreference.getString("USERSRC","NONE"))
                .addFormDataPart("date",MyApp.getInstance().returnTime())
                .addFormDataPart("introduction",content.getText().toString())
                .addFormDataPart("title",title.getText().toString());//ParamKey.TOKEN 自定义参数key常量类，即参数名
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
        List<MultipartBody.Part> parts = builder.build().parts();
        Log.d("tag","sending!");
        return parts;
    }

    private void setTheTakePhoteButton() {
        takeFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openThePhoteActivity();
            }
        });
    }

    private void openThePhoteActivity() {
        RxGalleryFinal
                .with(this).image().radio().crop().imageLoader(ImageLoaderType.PICASSO)
                .subscribe(new RxBusResultSubscriber<ImageRadioResultEvent>() {
                    @Override
                    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                        //图片选择结果
                        bookPic.setImageURI(Uri.parse(imageRadioResultEvent.getResult().getOriginalPath()));
                        HAVE_CHOOSED=true;
                        picPath=imageRadioResultEvent.getResult().getOriginalPath();

                    }
                })
                .openGallery();
    }

    private void bindAllTheView() {
        ButterKnife.bind(this);
    }

    private void pressThePic() {
        File file=new File(picPath);
        Luban.get(this)
                .load(file)                     //传人要压缩的图片
                .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() { }
                    @Override
                    public void onSuccess(File file) {
                        picPath=file.getPath();
                        sendToServer();
                    }
                    @Override
                    public void onError(Throwable e) { }
                }).launch();
    }

    //Set the status bar of the system
    private void setTheStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setStatusBarColor(Color.parseColor(MyApp.COLOR_STATUSBAR));
        }
    }

}
