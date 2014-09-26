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
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import sit.kmutt.com.ureportapp.app.AppController;
import sit.kmutt.com.ureportapp.vo.SessionManager;
import sit.kmutt.com.ureportapp.vo.UrlService;

/**
 * Created by Thanabut on 9/9/2557.
 */
public class RegisterActivity extends Activity implements View.OnClickListener{
    UrlService service;
    EditText firstName,lastName,email,username,password;
    Button btnSend;
    TextView tvJson;
    JSONObject newUserJSON;
    SessionManager sMan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_register);
        firstName = (EditText) findViewById(R.id.etFirstName);
        lastName = (EditText) findViewById(R.id.etLastName);
        email = (EditText) findViewById(R.id.etEmail);
        username = (EditText) findViewById(R.id.etUserName);
        password = (EditText) findViewById(R.id.etPassword);
        btnSend = (Button) findViewById(R.id.btn_send);
        tvJson = (TextView) findViewById(R.id.tv_json);

        btnSend.setOnClickListener(this);

        sMan = new SessionManager(getApplicationContext());
        service = new UrlService(sMan);
    }

    public void test(){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("firstName",firstName.getText().toString());
        map.put("lastName",lastName.getText().toString());
        map.put("email",email.getText().toString());
        map.put("userName",username.getText().toString());
        map.put("password",password.getText().toString());
        newUserJSON = new JSONObject(map);


        tvJson.setText(newUserJSON.toString());
        String url = service.getHeadUrl("register");

        JsonObjectRequest jsonReq =
                new JsonObjectRequest(Request.Method.POST,url,newUserJSON, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        tvJson = (TextView) findViewById(R.id.tv_json);
                        String result = "";
                        try {
                            result = jsonObject.getString("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        tvJson.setText("result : "+result);
                        Toast.makeText(getApplicationContext(),
                                "Registeration Completed",
                                Toast.LENGTH_LONG).show();
                        if(getApplicationContext() instanceof RegisterActivity){
                            ((RegisterActivity)getApplicationContext()).finish();
                        }
                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Log.e("register",volleyError.getMessage());
                        tvJson = (TextView) findViewById(R.id.tv_json);
                        tvJson.setText(volleyError.getMessage());

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

        AppController.getInstance().addToRequestQueue(jsonReq,"json");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send:
                test();
                break;
        }
    }
}
