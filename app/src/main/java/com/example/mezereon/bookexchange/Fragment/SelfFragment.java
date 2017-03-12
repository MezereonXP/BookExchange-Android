package com.example.mezereon.bookexchange.Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mezereon.bookexchange.Adapter.MyRecycleViewAdapter;
import com.example.mezereon.bookexchange.Adapter.NormalRecycleViewAdapter;
import com.example.mezereon.bookexchange.Component.DaggerAppComponent;
import com.example.mezereon.bookexchange.Module.User;
import com.example.mezereon.bookexchange.MyApp;
import com.example.mezereon.bookexchange.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.lzy.widget.PullZoomView;
import com.lzy.widget.manager.ExpandLinearLayoutManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.IOverScrollStateListener;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelfFragment extends Fragment {


    @Inject
    Retrofit retrofit;

    @BindView(R.id.setting)
    RecyclerView setting;
    @BindView(R.id.imageView3)
    ImageView bg;
    @BindView(R.id.userpic)
    CircleImageView pic;
    @BindView(R.id.textView11)
    TextView name;
    @BindView(R.id.textView12)
    TextView sign;
    @BindView(R.id.pzv)
    PullZoomView pullZoomView;

    private boolean hasLazyLoad = false;
    private String picPath;
    private View viewOnSelfFragment;
    public void setHasLazyLoad(boolean hasLazyLoad) {
        this.hasLazyLoad = hasLazyLoad;
    }

    /**
     * 懒加载,防止ViewPager重复创建
     */
    protected void onLazyLoad() {

    }

    public interface SendPicService {
        @Multipart
        @POST("upload.php")
        Observable<List<User>> send(@Part List<MultipartBody.Part> partList);
    }

    public interface SendSignService {
        @GET("changeSign.php")
        Observable<Void> send(@Query("username") String username,
                              @Query("sign") String sign);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && !hasLazyLoad) {
            onLazyLoad();
            hasLazyLoad = true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hasLazyLoad = false;
    }
    public SelfFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewOnSelfFragment= inflater.inflate(R.layout.fragment_self, container, false);
        bindAllTheViews();
        injectByDagger();
        setTheRecycleView();
        setViewOverScroll();
        setTheUserInfo();
        setThePicChange();
        setTheSign();
        return viewOnSelfFragment;
    }

    private void setTheSign() {
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTheDialogToChangeSign();
            }
        });
    }

    private void openTheDialogToChangeSign() {
        final EditText editText = new EditText(getActivity());
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(getActivity());
        inputDialog.setTitle("请输入签名").setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sign= editText.getText().toString();
                        sendSign(sign.equals("")?"这个人很懒，没留下任何东西":sign);
                    }
                }).show();
    }

    private void sendSign(final String sign) {
        SharedPreferences sharePreference=getActivity()
                .getSharedPreferences("USERINFO",getActivity().MODE_PRIVATE);
        SendSignService sendSignService=retrofit.create(SendSignService.class);
        Subscription subscription=sendSignService.send(sharePreference.getString("USERNAME","NONE"),sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {
                        Toasty.error(getActivity(),"修改失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        Toasty.success(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
                        SelfFragment.this.sign.setText(sign);
                        getActivity().getSharedPreferences("USERINFO",getActivity().MODE_PRIVATE)
                                .edit().putString("USERSIGNATRUE",sign).commit();
                    }
                });
    }

    private void injectByDagger() {
        DaggerAppComponent.builder().build().inject(this);
    }

    private void setThePicChange() {
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openThePhoteChoose();
            }
        });
    }

    private void openThePhoteChoose() {
        RxGalleryFinal
                .with(getActivity()).image().radio().crop().imageLoader(ImageLoaderType.PICASSO)
                .subscribe(new RxBusResultSubscriber<ImageRadioResultEvent>() {
                    @Override
                    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                        picPath=imageRadioResultEvent.getResult().getOriginalPath();
                        pressThePic();
                    }
                })
                .openGallery();
    }

    private void pressThePic() {
        File file=new File(picPath);
        Luban.get(getActivity())
                .load(file)                     //传人要压缩的图片
                .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() { }
                    @Override
                    public void onSuccess(File file) {
                        picPath=file.getPath();
                        sendThePic();
                    }
                    @Override
                    public void onError(Throwable e) { }
                }).launch();
    }


    private void sendThePic() {
        SendPicService sendPicService=retrofit.create(SendPicService.class);
        Subscription subscription=sendPicService.send(getList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<User>>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {
                        Toasty.error(getActivity(),"上传失败", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNext(List<User> users) {
                        Log.d("TAG",users.get(0).getSrc());
                        Toasty.success(getActivity(),"上传成功",Toast.LENGTH_SHORT).show();
                        pic.setImageURI(Uri.fromFile(new File(picPath)));
                        getActivity().getSharedPreferences("USERINFO",getActivity().MODE_PRIVATE)
                                .edit().putString("USERSRC",users.get(0).getSrc()).commit();
                    }
                });
    }

    private List<MultipartBody.Part> getList() {
        File file = new File(picPath);//filePath 图片地址
        String username = getActivity().getSharedPreferences("USERINFO"
                ,getActivity().MODE_PRIVATE).getString("USERNAME","NONE");
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)//表单类型
                .addFormDataPart("username", username);//ParamKey.TOKEN 自定义参数key常量类，即参数名
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
        List<MultipartBody.Part> parts = builder.build().parts();
        return parts;
    }

    private void bindAllTheViews() {
        ButterKnife.bind(this,viewOnSelfFragment);
    }

    private void setTheRecycleView() {
        setting.setLayoutManager(new ExpandLinearLayoutManager(viewOnSelfFragment.getContext()));//这里用线性显示 类似于listview
        setting.setAdapter(new MyRecycleViewAdapter(viewOnSelfFragment.getContext()));
        //OverScrollDecoratorHelper.setUpOverScroll(setting,OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
    }

    private void setViewOverScroll() {
        pullZoomView.setIsParallax(true);
        pullZoomView.setIsZoomEnable(true);
        pullZoomView.setSensitive(1.5f);
        pullZoomView.setZoomTime(500);
    }

    private void setTheUserInfo() {
        SharedPreferences sp=getActivity().getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        Picasso.with(viewOnSelfFragment.getContext()).load(sp.getString("USERSRC","NONE")).into(pic);
        name.setText(sp.getString("USERNAME","none"));
        sign.setText(sp.getString("USERSIGNATRUE","NONE"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
