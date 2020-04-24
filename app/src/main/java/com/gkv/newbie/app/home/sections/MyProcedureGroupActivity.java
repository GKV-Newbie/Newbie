package com.gkv.newbie.app.home.sections;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.gkv.newbie.R;
import com.gkv.newbie.app.home.BaseNavigationActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MyProcedureGroupActivity extends BaseNavigationActivity {

    @BindView(R.id.webView)
    WebView webView;
    private final String INIT_URL="https://newbie.nihalkonda.com/userBin.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedure_group);
        ButterKnife.bind(this);
        //getSupportActionBar().hide();
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                System.out.println(url);
                super.onPageFinished(view, url);
                webView.loadUrl("javascript:(function(){console.log(document.body.innerHTML);})()");
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                System.out.println(newProgress);
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                System.out.println(consoleMessage.message());
                if(consoleMessage.message().startsWith("MROXJSONMROX")){
                    Intent i = new Intent(MyProcedureGroupActivity.this,ProcessInstructionsActivity.class);
                    i.putExtra(ProcessInstructionsActivity.EXTRA_PROCESS_ENCODED,consoleMessage.message().replace("MROXJSONMROX ",""));
                    startActivity(i);
                    webView.goBack();
                }
                return super.onConsoleMessage(consoleMessage);
            }
        });

        webView.loadUrl(INIT_URL+"?id="+ FirebaseAuth.getInstance().getCurrentUser().getEmail());
        Toast.makeText(this,INIT_URL,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if(webView.getUrl().equals(INIT_URL))
            super.onBackPressed();
        else
            webView.goBack();
    }
}