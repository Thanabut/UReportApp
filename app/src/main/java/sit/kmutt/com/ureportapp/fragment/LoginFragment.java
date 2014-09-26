package sit.kmutt.com.ureportapp.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import sit.kmutt.com.ureportapp.MapTest;
import sit.kmutt.com.ureportapp.R;

/**
 * Created by Thanabut on 23/9/2557.
 */
public class LoginFragment extends Fragment {
    Button btnTest;

    public LoginFragment(){}



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.form_login,container,false);
        btnTest = (Button) rootView.findViewById(R.id.btn_test_bg);



        
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTest.setPressed(!btnTest.isPressed());
            }
        });
        return rootView;
    }
}
