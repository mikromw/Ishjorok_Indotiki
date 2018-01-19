package com.pmberjaya.indotiki.app.account.login;

/**
 * Created by edwin on 09/08/2016.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pmberjaya.indotiki.R;


/**
 * Created by edwin on 08/08/2016.
 */
public class YahooWebViewActivity extends AppCompatActivity {
    private WebView web;
    private static final String REQUEST_TOKEN_ENDPOINT_URL ="https://api.login.yahoo.com/oauth/v2/get_request_token";
    private static final String AUTHORIZE_WEBSITE_URL   ="https://api.login.yahoo.com/oauth2/request_auth";
    private static final String ACCESS_TOKEN_ENDPOINT_URL ="https://api.login.yahoo.com/oauth/v2/get_token";

    static final String YAHOO_CALLBACK_URL = "www.indotiki.com/ymail";
    static final String YAHOO_CONSUMER_KEY = "dj0yJmk9cG9HYlBkV1ZoUjRxJmQ9WVdrOVoyOHdVSFZFTnpnbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD0yNg--";
    static final String YAHOO_CONSUMER_SECRET = "de60ef68735c22ec1409c60b23f4a1fcdb58fcde";
    private String url =AUTHORIZE_WEBSITE_URL+"?client_id="+YAHOO_CONSUMER_KEY+"&redirect_uri=https://www.indotiki.com&response_type=token&approval_prompt=auto&language=en-us";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_login_yahoo_webview);
        web = findViewById(R.id.webv);
        web.getSettings().setJavaScriptEnabled(true);

        web.loadUrl(url);
        web.setWebViewClient(new WebViewClient() {

            boolean authComplete = false;
            Intent resultIntent = new Intent();

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                super.onPageStarted(view, url, favicon);

            }
            String authCode;
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("test","test");
//
//                if (url.contains("?access_token=") && authComplete != true) {
//                    Uri uri = Uri.parse(url);
//                    authCode = uri.getQueryParameter("access_token");
//                    Log.i("", "CODE : " + authCode);
//                    authComplete = true;
//                    resultIntent.putExtra("code", authCode);
//                    WebViewActivity.this.setResult(Activity.RESULT_OK, resultIntent);
//                    setResult(Activity.RESULT_CANCELED, resultIntent);
//                    Toast.makeText(getApplicationContext(),"Authorization Code is: " +authCode, Toast.LENGTH_SHORT).show();
//                }else if(url.contains("error=access_denied")){
//                    Log.i("", "ACCESS_DENIED_HERE");
//                    resultIntent.putExtra("code", authCode);
//                    authComplete = true;
//                    setResult(Activity.RESULT_CANCELED, resultIntent);
//                    Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();
//
//                }

            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("when you click on any interlink on webview that time you got url :-" + url);
                Log.d("URL AKSES TOKENNYA","when you click on any interlink on webview that time you got url :-" + url);
                if (url.contains("#access_token=")) {
                    Uri uri = Uri.parse(url);
                    authCode = uri.getFragment();
                    String access_token = authCode.substring(13, authCode.indexOf("&"));
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("access_token", access_token);
                    setResult(YahooWebViewActivity.this.RESULT_OK, returnIntent);
                    finish();
                } else if(url.contains("error=access_denied")){
                    finish();
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CookieSyncManager.createInstance(YahooWebViewActivity.this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }
}
