package com.sora.projectn.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sora on 2016/3/29.
 */
public class GetHttpResponse {




    public static String getHttpResponse(String urlStr){
        // TODO Auto-generated method stub
        HttpURLConnection connection=null;
        try {
            URL url=new URL(urlStr);
            connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            InputStream in=connection.getInputStream();
            BufferedReader reader=new BufferedReader(new InputStreamReader(in));
            StringBuilder response=new StringBuilder();
            String line;
            while((line=reader.readLine())!=null){
                response.append(line);
            }
            Log.d("data", String.valueOf(response));
            return String.valueOf(response);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            if(connection!=null){
                connection.disconnect();
            }
        }
        return null;
    }


}
