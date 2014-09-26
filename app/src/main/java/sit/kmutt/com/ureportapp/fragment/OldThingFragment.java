package sit.kmutt.com.ureportapp.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import sit.kmutt.com.ureportapp.FeedActivity;
import sit.kmutt.com.ureportapp.ImagePickActivity;
import sit.kmutt.com.ureportapp.LoginActivity;
import sit.kmutt.com.ureportapp.MapTest;
import sit.kmutt.com.ureportapp.PostProblemActivity;
import sit.kmutt.com.ureportapp.R;
import sit.kmutt.com.ureportapp.RegisterActivity;
import sit.kmutt.com.ureportapp.UserProfileActivity;
import sit.kmutt.com.ureportapp.sessioninterface.SessionListener;
import sit.kmutt.com.ureportapp.vo.SessionManager;
import sit.kmutt.com.ureportapp.vo.UrlService;
import sit.kmutt.com.ureportapp.vo.UserSession;

/**
 * Created by Thanabut on 23/9/2557.
 */
public class OldThingFragment extends Fragment implements View.OnClickListener{
    EditText etIP;
    Button btnMap,btnLogin,btnLogout,btnImg,btnVolley,btnRegister,btnPostProblem,btnProfile;
    TextView tvUserName, tvEmail;
    SessionManager ipMannager;
    UserSession userSession;
    SessionListener hostContext;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.oldthing_fragment,container,false);

        btnMap = (Button) rootView.findViewById(R.id.map_btn);
        btnLogin = (Button) rootView.findViewById(R.id.btn_login_form);
        btnLogout = (Button) rootView.findViewById(R.id.btn_log_out);
        btnImg = (Button) rootView.findViewById(R.id.pick_img_btn);
        btnVolley = (Button) rootView.findViewById(R.id.btn_volley_test);
        btnRegister = (Button) rootView.findViewById(R.id.btn_register);
        btnPostProblem = (Button) rootView.findViewById(R.id.btn_post_problem);
        btnProfile = (Button) rootView.findViewById(R.id.btn_profile);

        btnMap.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnImg.setOnClickListener(this);
        btnVolley.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnPostProblem.setOnClickListener(this);
        btnProfile.setOnClickListener(this);

        String s = UserSession.KEY_USERNAME;
        etIP = (EditText) rootView.findViewById(R.id.et_ip);
        tvUserName = (TextView) rootView.findViewById(R.id.tv_user_display);
        tvEmail = (TextView) rootView.findViewById(R.id.tv_email_display);
        refreshTextView();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof SessionListener){
            hostContext = (SessionListener) activity;
            userSession = hostContext.getUserSession();
            ipMannager = hostContext.getIpSession();
        }
        else{
            throw new ClassCastException(activity.toString()
                    + " must implement SessionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        hostContext = null;
    }

    public void refreshTextView(){
        HashMap<String, String> user = userSession.getUserDetails();
        tvUserName.setText("User name :: " + user.get(UserSession.KEY_USERNAME));
        tvEmail.setText("Email :: " + user.get(UserSession.KEY_EMAIL));

        String ip = ipMannager.getIp();
        etIP.setText(ip);
    }


    @Override
    public void onResume() {
        super.onResume();
        HashMap<String, String> user = userSession.getUserDetails();
        tvUserName.setText("User name :: "+user.get(UserSession.KEY_USERNAME));
        tvEmail.setText("Email :: "+user.get(UserSession.KEY_EMAIL));
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

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_set_ip:
                setIpCLick(v);
                break;
            case R.id.map_btn:
                mapClick(v);
                break;
            case R.id.pick_img_btn:
                openPickImg(v);
                break;
            case R.id.btn_volley_test:
                volleyClick(v);
                break;
            case R.id.btn_register:
                registerClick(v);
                break;
            case R.id.btn_login_form:
                loginClick(v);
                break;
            case R.id.btn_log_out:
                logoutClick(v);
                break;
            case R.id.btn_profile:
                profileClick(v);
                break;
            case R.id.btn_post_problem:
                postProblemClick(v);
                break;
        }
    }

    public void openPickImg(View view) {
        Intent intent = new Intent((Activity)hostContext, ImagePickActivity.class);
        startActivity(intent);
    }



    public void volleyClick(View view){
        Intent intent = new Intent(getActivity(), FeedActivity.class);
        startActivity(intent);
    }

    public void registerClick(View v){
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        startActivity(intent);
    }

    public void loginClick(View v){
        if(!userSession.isLogin()) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(getActivity().getApplicationContext(), "You are already log in", Toast.LENGTH_SHORT).show();
        }
    }

    public void setIpCLick(View v){
        String ip = etIP.getText().toString();
        ipMannager.createIpSession(ip);
        Toast.makeText(getActivity().getApplicationContext(),"Ip set "+ipMannager.getIp(),Toast.LENGTH_SHORT).show();
        UrlService urlTest = new UrlService(ipMannager);
        Toast.makeText(getActivity().getApplicationContext(),"Url ="+urlTest.getHeadUrl(),Toast.LENGTH_SHORT).show();
    }

    public void logoutClick(View v){
        userSession.clearUserSession();
        HashMap<String, String> user = userSession.getUserDetails();
        tvUserName.setText("User name :: "+user.get(UserSession.KEY_USERNAME));
        tvEmail.setText("Email :: "+user.get(UserSession.KEY_EMAIL));
    }

    public void postProblemClick(View view) {
        Intent intent = new Intent(getActivity(), PostProblemActivity.class);
        startActivity(intent);
    }

    public void profileClick(View view) {
        if(userSession.isLogin()) {
            Intent intent = new Intent(getActivity(), UserProfileActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(getActivity().getApplicationContext(),"You are not logged in Yet", Toast.LENGTH_SHORT).show();
        }
    }

    public void mapClick(View v){
        Intent intent = new Intent(getActivity().getApplicationContext(), MapTest.class);
        startActivity(intent);
    }
}
