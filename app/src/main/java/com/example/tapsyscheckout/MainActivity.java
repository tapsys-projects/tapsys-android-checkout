package com.example.tapsyscheckout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button but = (Button)findViewById(R.id.button);
        but.setOnClickListener(v -> {
            volleyPost();
        });
    }

    public void volleyPost(){
        String postUrl = "https://testgateway.tapsys.net/plugin/wordpress/order/v1/init";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject postData = new JSONObject();
        try {
            postData.put("firstName", "Turab");
            postData.put("lastName", "Qureshi");
            postData.put("amount", "100");
            postData.put("environment", "sandbox");
            postData.put("clientId", "64b68307-917b-4269-ae5e-944ff1f0fac1");
            postData.put("clientWordPressKey", "e37d8b4d-6495-397a-a57a-ff3a70746e9d");
            postData.put("billingEmail", "turabaq@gmail.com");
            postData.put("merchantId", "010210742100019");
            postData.put("currency", "586");
            postData.put("billingAddress", "karachi");
            postData.put("billingPhone", "03467941479");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject _data = response.getJSONObject("data");
                    try {
                        WebView webView = findViewById(R.id.webview);
                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.setWebViewClient(new WebViewClient() {
                            public void onPageFinished(WebView view, String url) {
                                try {
                                    URL _url = new URL(url);
                                    String baseUrl = _url.getHost();
                                    System.out.println(baseUrl);
                                    if(!baseUrl.equals("testgateway.tapsys.net") && !baseUrl.equals("gateway.tapsys.net")){
                                        findViewById(R.id.button).animate().alpha(1.0f).setDuration(3000).start();
                                        findViewById(R.id.button).setClickable(true);
                                    }
                                } catch (MalformedURLException e) { e.printStackTrace(); }
                            }
                        });
                        String _token = _data.getString("token");
                        findViewById(R.id.button).animate().alpha(0f).setDuration(3000).start();
                        findViewById(R.id.button).setClickable(false);
                        webView.loadUrl("https://testgateway.tapsys.net/plugin/components/?env=sandbox&beacon="+ _token +"&source=mobile_app&order_id=33&nonce=mobile_app_order_id_33&redirect_url=https://turabaliqureshi.com/success.html&cancel_url=https://turabaliqureshi.com/failure.html");
                    } catch (JSONException e) { e.printStackTrace(); }
                } catch (JSONException e) { e.printStackTrace(); }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}