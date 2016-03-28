package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.util.Pair;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.mattpflance.builditbigger.backend.theJokester.TheJokester;
import com.mattpflance.textdisplayactivity.TextDisplayActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, List<String>> {

    private static TheJokester myApiService = null;
    private String mJokeType;
    private Context context;

    @Override
    protected List<String> doInBackground(Pair<Context, String>... params) {
        if(myApiService == null) {  // Only do this once
            TheJokester.Builder builder = new TheJokester.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://synthetic-pipe-126322.appspot.com/_ah/api/");

            myApiService = builder.build();
        }

        context = params[0].first;
        mJokeType = params[0].second;

        try {
            return myApiService.getJoke(mJokeType).execute().getJoke();
        } catch (IOException e) {
            ArrayList<String> err = new ArrayList<>();
            err.add(e.getMessage());
            return err;
        }
    }

    @Override
    protected void onPostExecute(List<String> result) {
        Intent intent = new Intent(context, TextDisplayActivity.class);
        intent.putExtra("JOKE TYPE", mJokeType);
        intent.putStringArrayListExtra(mJokeType, (ArrayList<String>)result);
        context.startActivity(intent);
    }
}