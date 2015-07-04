package com.mac.themac.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mac.themac.R;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * The TwitterOAuthActivity provides a simple web view for users authenticating with Twitter. To do this authentication,
 * we do the following steps:
 * <p/>
 * 1. Using twitter4j, get the request token, request token secret, and oauth verifier
 * 2. Open a web view for the user to give the application access
 * 3. Using twitter4j, get the authentication token, secret, and user id with a accepted request token
 */
public class TwitterOAuthActivity extends Activity {

    private static final String TAG = TwitterOAuthActivity.class.getSimpleName();

    private WebView mTwitterView;

    private Twitter mTwitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setup twitter client
        mTwitter = new TwitterFactory(new ConfigurationBuilder()
                .setOAuthConsumerKey(getResources().getString(R.string.twitter_consumer_key))
                .setOAuthConsumerSecret(getResources().getString(R.string.twitter_consumer_secret))
                .build()).getInstance();

        // setup twitter webview
        mTwitterView = new WebView(this);
        mTwitterView.getSettings().setJavaScriptEnabled(true);

        // initialize view
        setContentView(mTwitterView);

        // start the web view
        loginToTwitter();
    }

    private void loginToTwitter() {
        // fetch the oauth request token then prompt the user to authorize the application
        new AsyncTask<Void, Void, RequestToken>() {
            @Override
            protected RequestToken doInBackground(Void... params) {
                RequestToken token = null;
                try {
                    token = mTwitter.getOAuthRequestToken("oauth://mac.twitter");
                } catch (TwitterException te) {
                    Log.e(TAG, te.toString());
                }
                return token;
            }

            @Override
            protected void onPostExecute(final RequestToken token) {

                if(token != null) {
                    mTwitterView.setWebViewClient(new WebViewClient() {
                        private boolean isRedirected;

                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon) {

                            if (!isRedirected) {
                                //Do something you want when starts loading
                            }
                            isRedirected = false;
                        }
                        @Override
                        public void onPageFinished(final WebView view, final String url) {
                            if (url.startsWith("oauth://mac.twitter") && !isRedirected) {
                                getTwitterOAuthTokenAndLogin(token, Uri.parse(url).getQueryParameter("oauth_verifier"));
                            }
                        }

                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url){
                            view.loadUrl(url);
                            isRedirected = true;
                            return true;
                        }
                    });
                    mTwitterView.loadUrl(token.getAuthorizationURL());
                }else{
                    Intent resultIntent = new Intent();
                    setResult(LoginActivity.RC_TWITTER_LOGIN, resultIntent);
                    finish();
                }
            }
        }.execute();
    }

    private void getTwitterOAuthTokenAndLogin(final RequestToken requestToken, final String oauthVerifier) {
        // once a user authorizes the application, get the auth token and return to the LoginActivity
        new AsyncTask<Void, Void, AccessToken>() {
            @Override
            protected AccessToken doInBackground(Void... params) {
                AccessToken accessToken = null;
                try {
                    accessToken = mTwitter.getOAuthAccessToken(requestToken, oauthVerifier);
                } catch (TwitterException te) {
                    Log.e(TAG, te.toString());
                }
                return accessToken;
            }

            @Override
            protected void onPostExecute(AccessToken token) {
                Intent resultIntent = new Intent();
                if(token != null) {
                    resultIntent.putExtra("oauth_token", token.getToken());
                    resultIntent.putExtra("oauth_token_secret", token.getTokenSecret());
                    resultIntent.putExtra("user_id", token.getUserId() + "");
                }
                setResult(LoginActivity.RC_TWITTER_LOGIN, resultIntent);
                finish();
            }
        }.execute();
    }
}
