package com.along.androidchat.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Long
 * Date: 4/5/2019
 * Time: 11:20 PM
 */

/**
 * Created by Luca on 05/12/2014.
 *
 * This is a simple way to get result from a site using an async http post request
 * Visit https://github.com/texx00/SimpleHTTPPostRequest for more info
 *
 * Remember to add INTERNET and ACCESS_NETWORK_STATE permissions to manifest
 */

public class HTTPPostTask extends AsyncTask<String, Integer, String> {

    private String url;
    private Context context;
    private HTTPPostTaskListener l;
    private List<NameValuePair> nvpl=new ArrayList<NameValuePair>();

    public HTTPPostTask(Context context, String url, HTTPPostTaskListener listener){
        this.url=url;
        l=listener;
        this.context=context;
    }

    /**
     * used to check if a connection is available
     * @return true if an internet connection is available, false if not
     */
    public boolean checkConnection(){
        ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm!=null && cm.getActiveNetworkInfo()!=null){
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }else return false;
    }

    /**
     * Used to add params as post request
     * @param name of the param
     * @param value of the param
     */
    public void addPostParam(String name, String value){
        nvpl.add(new BasicNameValuePair(name, value));
    }

    @Override
    protected String doInBackground(String[] params){
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost httpPost=new HttpPost(url);
        StringBuilder stringBuilder=new StringBuilder();
        String buffStrChunk;
        try{
            httpPost.setEntity(new UrlEncodedFormEntity(nvpl));
            HttpResponse httpResponse=client.execute(httpPost);
            InputStream inputStream=httpResponse.getEntity().getContent();
            InputStreamReader isr=new InputStreamReader(inputStream);
            BufferedReader buffer=new BufferedReader(isr);
            while((buffStrChunk=buffer.readLine())!=null){
                stringBuilder.append(buffStrChunk);
            }
        }catch(Exception e){
            e.printStackTrace();
            if(l!=null)
                l.onError(context);
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onPostExecute(String result){
        if (l!=null)
            l.onDataReceived(result);
    }

    /**
     * listener class
     */
    public interface HTTPPostTaskListener{
        /**
         * what to do when the result is available
         *
         * @param result: string with the text downloaded from requested page (null if something has gone wrong)
         */
        public void onDataReceived(String result);

        /**
         * what to do if there is an error
         */
        public void onError(Context c);
    }
}