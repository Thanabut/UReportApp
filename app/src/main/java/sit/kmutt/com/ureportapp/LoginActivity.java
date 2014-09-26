package sit.kmutt.com.ureportapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;

import sit.kmutt.com.ureportapp.app.AppController;
import sit.kmutt.com.ureportapp.vo.SessionManager;
import sit.kmutt.com.ureportapp.vo.UrlService;
import sit.kmutt.com.ureportapp.vo.UserSession;

/**
 * Created by Thanabut on 10/9/2557.
 */
public class LoginActivity extends Activity implements View.OnClickListener{
    JSONObject user;
    Button btnLogin;
    EditText etUserName, etPassword;
    SessionManager sMan;
    TextView tvStatus;
    UserSession userSession;

    private LoginActivity la = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_login);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        etUserName = (EditText) findViewById(R.id.etUserNameLogin);
        etUserName.setText("zoom");
        etPassword = (EditText) findViewById(R.id.etPasswordLogin);
        etPassword.setText("1234");
        tvStatus = (TextView) findViewById(R.id.tv_login_status);
        sMan = new SessionManager(getApplicationContext());
        userSession = new UserSession(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_login:
                login();
                break;
        }
    }

    public void login() {
        Toast.makeText(getApplicationContext(),"Request is sent, pls wait", Toast.LENGTH_SHORT).show();
        user = new JSONObject();
        try {
            user.put("username",etUserName.getText().toString());
            user.put("password",etPassword.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UrlService urlService = new UrlService(sMan);
        String url = urlService.getHeadUrl("login") ;
        tvStatus.setText(user.toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,url,user,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                tvStatus = (TextView) findViewById(R.id.tv_login_status);
                String userName = "sss";
                String id_user = "";
                String email = "";

                try {
                    userName = jsonObject.getString("username");
                    id_user = jsonObject.getString("id_user");
                    email = jsonObject.getString("email");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String test = userName + " and " + id_user+ " and " + email;
                tvStatus.setText(test+jsonObject.toString());
                userSession.createUserSession(id_user,userName,email);
                Toast.makeText(AppController.getInstance(),"You are logged in as "+userSession.getUserDetails().get(UserSession.KEY_USERNAME),Toast.LENGTH_SHORT).show();
                la.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                tvStatus = (TextView) findViewById(R.id.tv_login_status);
                tvStatus.setText("error");
                Log.e("Volly Error", volleyError.toString());
                if(volleyError.getMessage() != null) {
                    Log.e("", volleyError.getMessage());
                }
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(),
                            "time out",
                            Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof AuthFailureError) {
                    //TODO
                } else if (volleyError instanceof ServerError) {
                    //TODO
                } else if (volleyError instanceof NetworkError) {
                    //TODO
                } else if (volleyError instanceof ParseError) {
                    //TODO
                    Toast.makeText(getApplicationContext(),
                            "Parse",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        request.setRetryPolicy(
                new DefaultRetryPolicy(3000,3,2));

        AppController.getInstance().addToRequestQueue(request,"json");


    }
}
