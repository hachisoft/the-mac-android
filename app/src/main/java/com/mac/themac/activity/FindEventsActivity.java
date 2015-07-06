package com.mac.themac.activity;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;

import com.mac.themac.R;
import com.mac.themac.TheMACApplication;
import com.mac.themac.model.User;
import com.mac.themac.utility.FirebaseHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindEventsActivity extends Activity {

    /* A reference to the Firebase */
    private FirebaseHelper _FBHelper;

    @Bind(R.id.btnFind)
    ImageButton _btnFind;

    @OnClick({R.id.btnMore, R.id.txtMore})
    public void launchMore(){
        TheMACApplication.startActivity(this, MoreActivity.class);
    }

    @OnClick({R.id.btnMyAccount, R.id.txtMyAccount})
    public void launchMyAccount(){
        TheMACApplication.startActivity(this, MyAccountActivity.class);
    }

    @OnClick({R.id.btnBill, R.id.txtBill})
    public void launchBill(){
        TheMACApplication.startActivity(this, BillActivity.class);
    }

    @OnClick({R.id.btnFind, R.id.txtFind})
    public void launchFind(){
        _btnFind.setPressed(true);
    }

    @OnClick({R.id.btnTennisCourts, R.id.txtTennisCourts})
    public void launchTennisCourts(){
        TheMACApplication.startActivity(this, TennisCourtsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        ButterKnife.bind(this);

        _FBHelper = TheMACApplication.theApp.getFirebaseHelper();

        User loggedInUser = _FBHelper.getLoggedInUser();
        _btnFind.setPressed(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
