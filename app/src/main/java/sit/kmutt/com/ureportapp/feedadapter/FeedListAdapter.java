package sit.kmutt.com.ureportapp.feedadapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import sit.kmutt.com.ureportapp.R;
import sit.kmutt.com.ureportapp.app.AppController;
import sit.kmutt.com.ureportapp.customview.ButtonUpDown;
import sit.kmutt.com.ureportapp.listviewfeed.FeedImageView;
import sit.kmutt.com.ureportapp.vo.FeedItem;

/**
 * Created by Thanabut on 8/9/2557.
 */
public class FeedListAdapter extends BaseAdapter implements View.OnClickListener{
    private Activity activity;
    private LayoutInflater inflater;
    private List<FeedItem> feedItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public FeedListAdapter(Activity activity, List<FeedItem> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.feed_item, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView timestamp = (TextView) convertView
                .findViewById(R.id.timestamp);
        TextView statusMsg = (TextView) convertView
                .findViewById(R.id.txtStatusMsg);
        TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
        NetworkImageView profilePic = (NetworkImageView) convertView
                .findViewById(R.id.profilePic);
        FeedImageView feedImageView = (FeedImageView) convertView
                .findViewById(R.id.feedImage1);

        ButtonUpDown btnUp = (ButtonUpDown) convertView.findViewById(R.id.btn_fi_up);
        ButtonUpDown btnDown = (ButtonUpDown) convertView.findViewById(R.id.btn_fi_down);
        btnUp.setBtnPair(btnDown);
        btnDown.setBtnPair(btnUp);
        btnUp.setBtnName(ButtonUpDown.UP);
        btnDown.setBtnName(ButtonUpDown.DOWN);
        Button btnComment = (Button) convertView.findViewById(R.id.btn_fi_comment);

        btnUp.setOnClickListener(this);

        btnDown.setOnClickListener(this);

        FeedItem item = feedItems.get(position);

        name.setText(item.getName());

        // Converting timestamp into x ago format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(item.getTimeStamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        timestamp.setText(timeAgo);

        // Chcek for empty status message
        if (!TextUtils.isEmpty(item.getStatus())) {
            statusMsg.setText(item.getStatus());
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            statusMsg.setVisibility(View.GONE);
        }

        // Checking for null feed url
        if (item.getUrl() != null) {
            url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
                    + item.getUrl() + "</a> "));

            // Making url clickable
            url.setMovementMethod(LinkMovementMethod.getInstance());
            url.setVisibility(View.VISIBLE);
        } else {
            // url is null, remove from the view
            url.setVisibility(View.GONE);
        }

        // user profile pic
        profilePic.setImageUrl(item.getProfilePic(), imageLoader);

        // Feed image
        if (item.getImge() != null) {
            feedImageView.setImageUrl(item.getImge(), imageLoader);
            feedImageView.setVisibility(View.VISIBLE);
            feedImageView
                    .setResponseObserver(new FeedImageView.ResponseObserver() {
                        @Override
                        public void onError() {
                        }

                        @Override
                        public void onSuccess() {
                        }
                    });
        } else {
            feedImageView.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public void onClick(View v) {
        ButtonUpDown pressedBtn;
        switch(v.getId()){
            case R.id.btn_fi_up:
                pressedBtn = ((ButtonUpDown)v);
                pressedBtn.setActivateUpDown(true);
                Log.i("Btn Down", "Click");
                break;
            case R.id.btn_fi_down:
                pressedBtn = ((ButtonUpDown)v);
                pressedBtn.setActivateUpDown(true);
                Log.i("Btn Up", "Click");
                break;
            case R.id.btn_fi_comment:

                break;
        }
    }
}
