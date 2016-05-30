package com.rhcloud.httpispend_jntuhceh.ispend;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
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
 * Created by Muneer on 25-05-2016.
 */
public class SyncServerToDeviceServerRequests {

    Context context;
    UserLocalStore userLocalStore;

    public SyncServerToDeviceServerRequests(Context context) {
        this.context = context;
        userLocalStore = new UserLocalStore(context);
    }


    void fetchCategoriesInBackground(String email, GetObjectCallback objectCallback) {
        new FetchCategoriesAsyncTask(email, objectCallback).execute();
    }

    class FetchCategoriesAsyncTask extends AsyncTask<Void, Void, String> {

        String email;
        GetObjectCallback objectCallback;

        public FetchCategoriesAsyncTask(String email,GetObjectCallback objectCallback) {
            this.email = email;
            this.objectCallback = objectCallback;
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(context, "Sync Categories started", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String JSON_STRING = "";
            try
            {
                String email = userLocalStore.getLoggedInUser().email;

                URL url = new URL("https://ispend-jntuhceh.rhcloud.com/final/s2d/categories.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(os, "UTF-8")));
                String data = URLEncoder.encode("Email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while((JSON_STRING = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(JSON_STRING + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            objectCallback.done(s);
        }
    }

    void fetchTransactionsInBackground(String email, GetObjectCallback objectCallback) {
        new FetchTransactionsAsyncTask(email, objectCallback).execute();
    }

    class FetchTransactionsAsyncTask extends AsyncTask<Void, Void, String> {

        String email;
        GetObjectCallback objectCallback;

        public FetchTransactionsAsyncTask(String email,GetObjectCallback objectCallback) {
            this.email = email;
            this.objectCallback = objectCallback;
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(context, "Sync Transactions started", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String JSON_STRING = "";
            try
            {
                String email = userLocalStore.getLoggedInUser().email;

                URL url = new URL("https://ispend-jntuhceh.rhcloud.com/final/s2d/transactions.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(os, "UTF-8")));
                String data = URLEncoder.encode("Email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while((JSON_STRING = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(JSON_STRING + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            objectCallback.done(s);
        }
    }

    public void fetchUserInBackground(User user, GetUserCallback userCallBack)
    {
        new fetchUserAsync(user, userCallBack).execute();
    }

    public class fetchUserAsync extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallback userCallBack;

        public fetchUserAsync(User user, GetUserCallback userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected User doInBackground(Void... params)
        {
            User returnedUser = null;
            String login_url = "http://ispend-jntuhceh.rhcloud.com/final/s2d/user.php";
            try
            {
                URL url = new URL(login_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("Email", user.email).appendQueryParameter("Password", user.password);
                String query = builder.build().getEncodedQuery();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
                InputStream in = new BufferedInputStream(conn.getInputStream());
                String response = IOUtils.toString(in, "UTF-8");
                if(response.length() <= 4) {
                    return null;
                }
                JSONObject jResponse = new JSONObject(response);

                if (jResponse.length() == 0)
                {
                    returnedUser = null;
                }
                else
                {
                    String mobile = jResponse.getString("Mobile");
                    String name = jResponse.getString("Name");

                    returnedUser = new User(user.email, mobile, name, user.password);
                }

                return returnedUser;

            } catch (IOException | JSONException e) {
                Log.d("Exception: ", e.toString());
            }
            finally {
                return returnedUser;
            }
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            userCallBack.done(returnedUser);
        }
    }
}
