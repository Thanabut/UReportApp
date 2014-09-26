package sit.kmutt.com.ureportapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sit.kmutt.com.ureportapp.R;

/**
 * Created by Thanabut on 23/9/2557.
 */
public class TestFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.form_post_problem,container,false);
        return rootView;
    }
}
