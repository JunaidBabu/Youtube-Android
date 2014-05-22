package in.junaidbabu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by babu on 22/5/14.
 */
public class YouTube extends Activity {

    private WebView webView;
    @SuppressLint("SetJavaScriptEnabled")
    public void onCreate(Bundle saveState) {

        super.onCreate(saveState);
        setContentView(R.layout.activity_webview_main);

        webView = (WebView) findViewById(R.id.webview1);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

      // webView.loadUrl("https://www.youtube.com/embed/68AqHwgk2s8");
       webView.loadUrl("javascript:alert('Hello World!')");
  //String customHtml = "<html><body><h1>Hello, WebView</h1></body></html>";
  //webView.loadData(customHtml, "text/html", "UTF-8");



    }

}
