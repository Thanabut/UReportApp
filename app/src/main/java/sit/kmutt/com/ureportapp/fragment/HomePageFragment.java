package sit.kmutt.com.ureportapp.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import sit.kmutt.com.ureportapp.PostProblemActivity;
import sit.kmutt.com.ureportapp.R;
import sit.kmutt.com.ureportapp.app.AppController;
import sit.kmutt.com.ureportapp.feedadapter.FeedListAdapter;
import sit.kmutt.com.ureportapp.sessioninterface.SessionListener;
import sit.kmutt.com.ureportapp.vo.FeedItem;
import sit.kmutt.com.ureportapp.vo.SessionManager;
import sit.kmutt.com.ureportapp.vo.UrlService;
import sit.kmutt.com.ureportapp.vo.UserSession;

/**
 * Created by Thanabut on 25/9/2557.
 */
public class HomePageFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = HomePageFragment.class.getSimpleName();
    private ListView listView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    //private String URL_FEED = "http://api.androidhive.info/feed/feed.json";
    private String URL_FEED = "http://192.168.150.1/ureportservice/service/get_all_post";

    SessionManager ipMannager;
    UserSession userSession;
    SessionListener hostContext;

    Button btnPostProblem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.feed_layout,container,false);


        UrlService urlService = new UrlService(ipMannager);
        URL_FEED = urlService.getHeadUrl("get_all_post");
        listView = (ListView) view.findViewById(R.id.list);

        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedListAdapter((Activity)hostContext, feedItems);
        listView.setAdapter(listAdapter);

        btnPostProblem =(Button) view.findViewById(R.id.btn_hp_post_problem);
        btnPostProblem.setOnClickListener(this);

        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(URL_FEED);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            makeRequest();
        }
        return view;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_hp_post_problem:
                Intent intent = new Intent((Activity)hostContext, PostProblemActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void makeRequest(){
        // making fresh volley request and getting json
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                URL_FEED, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d(TAG, "Response: " + response.toString());

                if (response != null) {
                    parseJsonFeed(response);
                    Toast.makeText((Activity)hostContext, "refreshed", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if(error.getMessage() != null) {
                    Log.e("", error.getMessage());
                }
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText((Activity)hostContext,
                            "time out",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO
                } else if (error instanceof ParseError) {
                    //TODO
                    Toast.makeText((Activity)hostContext,
                            "Parse",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(jsonReq);
    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FeedItem item = new FeedItem();
                item.setId(feedObj.getInt("id"));
                item.setName(feedObj.getString("name"));

                // Image might be null sometimes
                String image = feedObj.isNull("image") ? null : feedObj
                        .getString("image");
                item.setImge(image);
                item.setStatus(feedObj.getString("status"));
                item.setProfilePic(feedObj.getString("profilePic"));
                item.setTimeStamp(feedObj.getString("timeStamp"));

                // url might be null sometimes
                String feedUrl = feedObj.isNull("url") ? null : feedObj
                        .getString("url");
                item.setUrl(feedUrl);

                feedItems.add(item);
            }

            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.my, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        feedItems = new ArrayList<FeedItem>();
        listAdapter = new FeedListAdapter((Activity)hostContext, feedItems);
        listView.setAdapter(listAdapter);
        makeRequest();
        return super.onOptionsItemSelected(item);
    }
}
