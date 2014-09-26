package sit.kmutt.com.ureportapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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

import java.util.HashMap;

import sit.kmutt.com.ureportapp.app.AppController;
import sit.kmutt.com.ureportapp.vo.SessionManager;
import sit.kmutt.com.ureportapp.vo.UrlService;
import sit.kmutt.com.ureportapp.vo.UserSession;

/**
 * Created by Thanabut on 11/9/2557.
 */
public class UserProfileActivity extends Activity {
    TextView tvUsername, tvFirstName, tvLastName, tvEmail;
    SessionManager sMan;
    UserSession uMan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_user);
        tvUsername = (TextView) findViewById(R.id.tv_username_profile);
        tvFirstName = (TextView) findViewById(R.id.tv_firstname_profile);
        tvLastName = (TextView) findViewById(R.id.tv_lastname_profile);
        tvEmail = (TextView) findViewById(R.id.tv_email_profile);

        sMan = new SessionManager(getApplicationContext());
        uMan = new UserSession(getApplicationContext());

        getData();
    }

    public void getData(){
        String url = new UrlService(sMan).getHeadUrl("get_user_profile");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id_user",uMan.getUserDetails().get(UserSession.KEY_ID_USER));
        JSONObject user = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,url,user,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                String userName = "";
                String firstName = "";
                String lastName = "";
                String email = "";

                try {
                    userName = jsonObject.getString("user_name");
                    firstName = jsonObject.getString("frist_name");
                    lastName = jsonObject.getString("last_name");
                    email = jsonObject.getString("email");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(AppController.getInstance(), "Got data " + jsonObject.toString()  , Toast.LENGTH_LONG).show();
                tvUsername.setText(userName);
                tvFirstName.setText(firstName);
                tvLastName.setText(lastName);
                tvEmail.setText(email);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
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
