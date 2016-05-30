package com.rhcloud.httpispend_jntuhceh.ispend;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Muneer on 24-05-2016.
 */
public class SyncDeviceToServerServerRequests {

    private static final int TIMEOUT_MILLISEC = 3000;
    Context context;

    SyncDeviceToServerServerRequests(Context context) {
        this.context = context;
    }

    public void syncUserInBackground(User user, GetObjectCallback objectCallback)
    {
        //progressDialog.show();
        Toast.makeText(context, "Sync User started", Toast.LENGTH_SHORT).show();
        new SyncUserAsyncTask(user, objectCallback).execute();
    }

    public class SyncUserAsyncTask extends AsyncTask<Void, Void, String>
    {
        User user;
        GetObjectCallback objectCallback;

        public SyncUserAsyncTask(User user, GetObjectCallback objectCallback) {
            this.user = user;
            this.objectCallback = objectCallback;
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(context, "Sync User started", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(Void... params)
        {
            String register_url = "http://ispend-jntuhceh.rhcloud.com/final/d2s/user.php";
            try
            {
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(os, "UTF-8")));
                String data = URLEncoder.encode("Email", "UTF-8") + "=" + URLEncoder.encode(user.email, "UTF-8") + "&" +
                        URLEncoder.encode("Mobile", "UTF-8") + "=" + URLEncoder.encode(user.mobile, "UTF-8") + "&" +
                        URLEncoder.encode("Name", "UTF-8") + "=" + URLEncoder.encode(user.name, "UTF-8") + "&" +
                        URLEncoder.encode("Password", "UTF-8") + "=" + URLEncoder.encode(user.password, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine())!=null)
                {
                    response += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            objectCallback.done(result);
        }
    }


    public void syncCategoriesInBackGround(String email, String json_string, GetObjectCallback objectCallback) {
        new SyncCategoriesAsyncTask(email, json_string, objectCallback).execute();
    }

    public class SyncCategoriesAsyncTask extends AsyncTask<Void, Void, String> {
        String email, json_string;
        GetObjectCallback getObjectCallback;

        SyncCategoriesAsyncTask(String email, String json_string, GetObjectCallback getObjectCallback) {
            this.email = email;
            this.json_string = json_string;
            this.getObjectCallback = getObjectCallback;
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(context, "Sync Categories started", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
                HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
                HttpClient client = new DefaultHttpClient(httpParams);

                String url = "https://ispend-jntuhceh.rhcloud.com/final/d2s/categories.php";

                HttpPost request = new HttpPost(url);
                try {
                    request.setEntity(new ByteArrayEntity(json_string.getBytes("UTF8")));
                } catch (UnsupportedEncodingException e) {
                    //Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                    Log.d("exception: ", e.toString());
                }
                request.setHeader("json", json_string);
                HttpResponse response = null;
                try {
                    response = client.execute(request);
                } catch (IOException e) {
                   // Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                    Log.d("exception: ", e.toString());
                }

                return response.getStatusLine().toString();
            }
            catch (Exception e) {
                //Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                Log.d("exception: ", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            getObjectCallback.done(result);
        }
    }

    public void syncTransactionsInBackGround(String email, String json_string, GetObjectCallback objectCallback) {
        new SyncTransactionsAsyncTask(email, json_string, objectCallback).execute();
    }

    public class SyncTransactionsAsyncTask extends AsyncTask<Void, Void, String> {
        String email, json_string;
        GetObjectCallback getObjectCallback;

        SyncTransactionsAsyncTask(String email, String json_string, GetObjectCallback getObjectCallback) {
            this.email = email;
            this.json_string = json_string;
            this.getObjectCallback = getObjectCallback;
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(context, "Sync Transactions started", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
                HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
                HttpClient client = new DefaultHttpClient(httpParams);

                String url = "https://ispend-jntuhceh.rhcloud.com/final/d2s/transactions.php";

                HttpPost request = new HttpPost(url);
                try {
                    request.setEntity(new ByteArrayEntity(json_string.getBytes("UTF8")));
                } catch (UnsupportedEncodingException e) {
                    //Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                    Log.d("exception: ", e.toString());
                }
                request.setHeader("json", json_string);
                HttpResponse response = null;
                try {
                    response = client.execute(request);
                } catch (IOException e) {
                    // Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                    Log.d("exception: ", e.toString());
                }

                return response.getStatusLine().toString();
            }
            catch (Exception e) {
                //Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                Log.d("exception: ", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            getObjectCallback.done(result);
        }
    }
}

