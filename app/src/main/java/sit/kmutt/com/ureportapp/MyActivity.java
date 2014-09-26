package sit.kmutt.com.ureportapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import sit.kmutt.com.ureportapp.vo.SessionManager;
import sit.kmutt.com.ureportapp.vo.UrlService;
import sit.kmutt.com.ureportapp.vo.UserSession;


public class MyActivity extends Activity {
    EditText etIP;
    Button btnMap;
    SessionManager ipMannager;
    TextView tvUserName, tvEmail;
    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        btnMap = (Button) findViewById(R.id.map_btn);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivity.this, MapTest.class);
                startActivity(intent);
            }
        });
        etIP = (EditText) findViewById(R.id.et_ip);
        ipMannager = new SessionManager(getApplicationContext());
        tvUserName = (TextView) findViewById(R.id.tv_user_display);
        tvEmail = (TextView) findViewById(R.id.tv_email_display);
        userSession = new UserSession(getApplicationContext());
        HashMap<String, String> user = userSession.getUserDetails();
        tvUserName.setText("User name :: "+user.get(UserSession.KEY_USERNAME));
        tvEmail.setText("Email :: "+user.get(UserSession.KEY_EMAIL));


        String ip = ipMannager.getIp();
        etIP.setText(ip);

    }

    @Override
    protected void onResume() {
        super.onResume();
        HashMap<String, String> user = userSession.getUserDetails();
        tvUserName.setText("User name :: "+user.get(UserSession.KEY_USERNAME));
        tvEmail.setText("Email :: "+user.get(UserSession.KEY_EMAIL));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openPickImg(View view) {
        Intent intent = new Intent(MyActivity.this, ImagePickActivity.class);
        startActivity(intent);
    }



    public void volleyClick(View view){
        Intent intent = new Intent(MyActivity.this, FeedActivity.class);
        startActivity(intent);
    }

    public void registerClick(View v){
        Intent intent = new Intent(MyActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void loginClick(View v){
        if(!userSession.isLogin()) {
            Intent intent = new Intent(MyActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"You are already log in", Toast.LENGTH_SHORT).show();
        }
    }

    public void setIpCLick(View v){
        String ip = etIP.getText().toString();
        ipMannager.createIpSession(ip);
        Toast.makeText(getApplicationContext(),"Ip set "+ipMannager.getIp(),Toast.LENGTH_SHORT).show();
        UrlService urlTest = new UrlService(ipMannager);
        Toast.makeText(getApplicationContext(),"Url ="+urlTest.getHeadUrl(),Toast.LENGTH_SHORT).show();
    }

    public void logoutClick(View v){
        userSession.clearUserSession();
        HashMap<String, String> user = userSession.getUserDetails();
        tvUserName.setText("User name :: "+user.get(UserSession.KEY_USERNAME));
        tvEmail.setText("Email :: "+user.get(UserSession.KEY_EMAIL));
    }

    public void postProblemClick(View view) {
        Intent intent = new Intent(MyActivity.this, PostProblemActivity.class);
        startActivity(intent);
    }

    public void profileClick(View view) {
        if(userSession.isLogin()) {
            Intent intent = new Intent(MyActivity.this, UserProfileActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"You are not logged in Yet", Toast.LENGTH_SHORT).show();
        }
    }
}
