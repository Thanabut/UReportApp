package sit.kmutt.com.ureportapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONObject;

import java.util.HashMap;

import sit.kmutt.com.ureportapp.app.AppController;
import sit.kmutt.com.ureportapp.vo.SessionManager;
import sit.kmutt.com.ureportapp.vo.UrlService;
import sit.kmutt.com.ureportapp.vo.UserSession;

/**
 * Created by Thanabut on 11/9/2557.
 */
public class PostProblemActivity extends Activity implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    Spinner spnProblemType;
    EditText etTopic, etDescription, etIdDistrict;
    TextView tvUserName;
    Button btnPost;
    int problemType;

    UserSession uSession;
    HashMap<String, String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_post_problem);

        uSession = new UserSession(getApplicationContext());
        uSession.checkLogin(this);

        user = uSession.getUserDetails();

        spnProblemType = (Spinner) findViewById(R.id.spn_problem_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.problem_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnProblemType.setAdapter(adapter);
        spnProblemType.setOnItemSelectedListener(this);

        btnPost = (Button) findViewById(R.id.btn_post_problem);
        btnPost.setOnClickListener(this);

        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvUserName.setText("User name :: "+ user.get(UserSession.KEY_USERNAME));

        etDescription = (EditText) findViewById(R.id.et_description);
        etTopic = (EditText) findViewById(R.id.et_topic);
        etIdDistrict = (EditText) findViewById(R.id.et_id_district);

    }

    public void post(final PostProblemActivity postProblemActivity){
        Toast.makeText(getApplicationContext(),"Sending Information",Toast.LENGTH_SHORT).show();
        String discription = etDescription.getText().toString();
        String topic = etTopic.getText().toString();
        String idDistrict = etIdDistrict.getText().toString();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("topic",topic);
        map.put("description",discription);
        map.put("idDistrict",idDistrict);
        map.put("id_user",user.get(UserSession.KEY_ID_USER));
        map.put("problemType",problemType+"");

        SessionManager ipMan = new SessionManager(getApplicationContext());
        UrlService urlService = new UrlService(ipMan);
        String url = urlService.getHeadUrl("post");

        final JSONObject json = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,url,json,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Toast.makeText(getApplicationContext(),jsonObject.toString(),Toast.LENGTH_SHORT).show();
                postProblemActivity.finish();
            }
        },new Response.ErrorListener() {
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

        AppController.getInstance().addToRequestQueue(request,"json");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_post_problem:
                post(this);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        problemType = position+1;
        //Toast.makeText(parent.getContext(),problemType + " On Item Select : \n" + parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
