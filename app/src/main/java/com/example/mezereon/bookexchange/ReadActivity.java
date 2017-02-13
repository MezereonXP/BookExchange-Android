package com.example.mezereon.bookexchange;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReadActivity extends AppCompatActivity {

    @Bind(R.id.textView14)
    TextView title;
    @Bind(R.id.textView15)
    TextView content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        ButterKnife.bind(this);
        int position=getIntent().getIntExtra("position",-1);
        int position2=getIntent().getIntExtra("position2",-1);
        if(position!=-1){
            title.setText(MyApp.getInstance().getArticles().get(position).getTitle());
            content.setText(Html.fromHtml(MyApp.getInstance().getArticles().get(position).getIntroduction()));
        }
        if(position2!=-1){
            title.setText(MyApp.getInstance().getForums().get(position).getTitle());
            content.setText(Html.fromHtml(MyApp.getInstance().getForums().get(position).getIntroduction()));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
