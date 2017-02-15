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
        bindAllTheViews();
        setTheTitleAndContent();
    }

    private void setTheTitleAndContent() {
        int positionFromCommentFragment=getIntent().getIntExtra("position",-1);
        int positionFromTalkFragment=getIntent().getIntExtra("position2",-1);
        if(positionFromCommentFragment!=-1){
            title.setText(MyApp.getInstance().getArticles().get(positionFromCommentFragment).getTitle());
            content.setText(Html.fromHtml(MyApp.getInstance().getArticles().get(positionFromCommentFragment).getIntroduction()));
        }
        if(positionFromTalkFragment!=-1){
            title.setText(MyApp.getInstance().getForums().get(positionFromTalkFragment).getTitle());
            content.setText(Html.fromHtml(MyApp.getInstance().getForums().get(positionFromTalkFragment).getIntroduction()));
        }
    }

    private void bindAllTheViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
