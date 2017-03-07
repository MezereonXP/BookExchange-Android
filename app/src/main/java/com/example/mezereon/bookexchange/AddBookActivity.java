package com.example.mezereon.bookexchange;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mezereon.bookexchange.Component.DaggerAppComponent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
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

public class AddBookActivity extends AppCompatActivity {

    @BindView(R.id.buttonToUpBook)
    Button takeFile;
    @BindView(R.id.imageView7)
    ImageView bookPic;
    @BindView(R.id.button9)
    Button buttonToUpBook;
    @BindView(R.id.editText10)
    EditText bookName;
    @BindView(R.id.textViewChooseView)
    TextView textViewChooseView;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.spinner2)
    Spinner spinner2;
    @BindView(R.id.editText12)
    EditText introduction;

    @Inject
    Retrofit retrofit;

    public interface SendBookService {
        @Multipart
        @POST("upbook.php")
        Observable<Void> send(@Part List<MultipartBody.Part> partList);
    }

    private boolean HAVE_CHOOSED=false;
    private String picPath;
    private SharedPreferences sharePreference;
    private ProgressDialog progressDialog;
    String dateSet;
    Calendar calendar=Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheStatusBar();
        setContentView(R.layout.activity_add_book);
        bindAllTheView();
        injectByDagger();
        initProgressDialog();
        initSharePreference();
        setTheTakePhotoButton();
        setTheSendButton();
        setTheTimeChoose();
    }

    //Set the status bar of the system
    private void setTheStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setStatusBarColor(Color.parseColor(MyApp.COLOR_STATUSBAR));
        }
    }

    private void setTheTimeChoose() {
        textViewChooseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddBookActivity.this, DateSet, calendar
                        .get(Calendar.YEAR), calendar
                        .get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    DatePickerDialog.OnDateSetListener DateSet = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // 每次保存设置的日期
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String str = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            textViewChooseView.setText(str);
        }
    };

    private void initProgressDialog() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("发布书籍中....");
        progressDialog.setTitle("System");
    }

    private void setTheSendButton() {
        buttonToUpBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookName.getText().length()!=0&&introduction.getText().length()!=0
                        &&HAVE_CHOOSED){
                    progressDialog.show();
                    pressThePic();
                }else{
                    Toast.makeText(AddBookActivity.this,"请完整填写信息",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendToServer() {
        SendBookService sendBookService=retrofit.create(SendBookService.class);
        Subscription subcription = sendBookService.send(getList())
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
                        Toasty.success(AddBookActivity.this,"上传成功",Toast.LENGTH_SHORT,true).show();
                        finish();
                    }
                });
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

    private List<MultipartBody.Part> getList() {
        File file = new File(picPath);//filePath 图片地址
        String username = sharePreference.getString("USERNAME","NONE");
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)//表单类型
                .addFormDataPart("username", username)
                .addFormDataPart("bookname",bookName.getText().toString())
                .addFormDataPart("buydate",textViewChooseView.getText().toString())
                .addFormDataPart("date",MyApp.getInstance().returnTime())
                .addFormDataPart("introduction",introduction.getText().toString())
                .addFormDataPart("kind",spinner.getSelectedItem().toString())
                .addFormDataPart("wantkind",spinner2.getSelectedItem().toString());//ParamKey.TOKEN 自定义参数key常量类，即参数名
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
        List<MultipartBody.Part> parts = builder.build().parts();
        return parts;
    }

    private void initSharePreference() {
        sharePreference=getSharedPreferences("USERINFO",MODE_PRIVATE);
    }

    private void setTheTakePhotoButton() {
        takeFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openThePhotoActivity();
            }
        });
    }

    private void openThePhotoActivity() {
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

    private void injectByDagger() {
        DaggerAppComponent.builder().build().inject(this);
    }

    private void bindAllTheView() {
        ButterKnife.bind(this);
    }


}
