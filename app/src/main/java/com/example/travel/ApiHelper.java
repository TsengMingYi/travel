package com.example.travel;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

public class ApiHelper {
    private static final String TAG = ApiHelper.class.getSimpleName();
    private static final String BASE_URL = "https://gis.taiwan.net.tw/XMLReleaseALL_public/scenic_spot_C_f.json";
    private static Handler mainThreadHandler;


    public static void requestApi(String partialUrl, Map<String, String> params, Callback callback){
        String xdate = getServerTime();
        String sAuth = getApiAuth(xdate);
        if(sAuth.length() != 0){
            if(params == null){
                params = new HashMap<>();
            }
            params.put("Authorization",sAuth);
            params.put("x-date",xdate);
            params.put("Accept-Encoding", "gzip");
            params.put("$format", "JSON");
        }else {
            Log.e(TAG, "sAuth is empty");
        }
        //requestBaseApi(BASE_URL + partialUrl, params, callback);
        //requestBaseApi(BASE_URL + partialUrl, params, callback);
        requestBaseApi("https://gis.taiwan.net.tw/XMLReleaseALL_public/scenic_spot_C_f.json" + partialUrl, params, callback);
    }

    private static void requestBaseApi(final String apiUrl,final Map<String, String> params, final Callback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection;

                try {
                    URL url = new URL(apiUrl);
                    if ("https".equalsIgnoreCase(url.getProtocol())) {
                        SslUtils.ignoreSsl();
                    }
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    if(params != null){
                        Set<String> keys = params.keySet();
                        for(String key : keys){
                            connection.setRequestProperty(key, params.get(key));
                        }
                    }
                    connection.setDoInput(true);
                    Log.e("tttt", "" + connection.getResponseCode());
                    BufferedReader in;

                    // 判斷來源是否為gzip
                    if ("gzip".equals(connection.getContentEncoding())) {
                        InputStreamReader reader = new InputStreamReader(
                                new GZIPInputStream(connection.getInputStream()));
                        in = new BufferedReader(reader);
                    } else {
                        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                        in = new BufferedReader(reader);
                    }
                    // 返回的數據已經過解壓

                    // 讀取回傳資料
                    String line;
                    final StringBuilder response = new StringBuilder();
                    while ((line = in.readLine()) != null) {
                        response.append(line)
                                .append('\n');
                    }

                    Log.e("ttt", response.toString());
                    if(callback != null){
                        if(mainThreadHandler == null){
                            mainThreadHandler = new Handler(Looper.getMainLooper());
                        }
                        mainThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.success(response.toString());
                            }
                        });
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    if(callback != null){
                        if(mainThreadHandler == null){
                            mainThreadHandler = new Handler(Looper.getMainLooper());
                        }
                        mainThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.fail(e);
                            }
                        });
                    }
                }
            }
        }).start();

    }

    private static String getApiAuth(String xdate){
        // 申請的APPID
        // （FFFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF 為 Guest
        // 帳號，以IP作為API呼叫限制，請替換為註冊的APPID & APPKey）

        final String APPID = "19a7a03057e24dc995ed452d7367a551";
        // 申請的APPKey
        final String APPKey = "UWZsasTr98xm8tZdJRUqSp8954w";

        // 取得當下的UTC時間，Java8有提供時間格式DateTimeFormatter.RFC_1123_DATE_TIME
        // 但是格式與C#有一點不同，所以只能自行定義
        String SignDate = "x-date: " + xdate;
        String Signature;
        try {
            // 取得加密簽章
            Signature = HMAC_SHA1.Signature(SignDate, APPKey);
        } catch (SignatureException e1) {
            e1.printStackTrace();
            return "";
        }
        String sAuth = "hmac username=\"" + APPID + "\", algorithm=\"hmac-sha1\", headers=\"x-date\", signature=\""
                + Signature + "\"";
        System.out.println(sAuth);
        return sAuth;
    }
    private static String getServerTime() {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",
                Locale.US);

        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        return dateFormat.format(calendar.getTime());

    }

    public interface Callback{
        void success(String rawString);
        void fail(Exception e);
    }
}
