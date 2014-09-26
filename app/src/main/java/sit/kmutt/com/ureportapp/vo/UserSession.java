package sit.kmutt.com.ureportapp.vo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import sit.kmutt.com.ureportapp.LoginActivity;
import sit.kmutt.com.ureportapp.MyActivity;

/**
 * Created by Thanabut on 10/9/2557.
 */
public class UserSession {
    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "current_user";
    public static final String KEY_ID_USER = "id_user";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_LOGIN = "login";

    public UserSession(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createUserSession(String id_user,String userName,String email){
        editor.putString(KEY_USERNAME, userName);
        editor.putString(KEY_ID_USER,id_user);
        editor.putString(KEY_EMAIL,email);
        editor.putBoolean(KEY_LOGIN, true);

        editor.commit();
    }

    public void clearUserSession(){
        editor.clear();
        editor.commit();
    }

    public void checkLogin(){
        // Check login status
        if(!this.isLogin()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            // Staring Login Activity
            _context.startActivity(i);
        }
    }

    public void checkLogin(Context c){
        // Check login status
        if(!this.isLogin()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            // Staring Login Activity
            _context.startActivity(i);
            if(c instanceof Activity && !(c instanceof MyActivity)) {
                ((Activity) c).finish();
            }
        }
    }

    public boolean isLogin(){
        return pref.getBoolean(KEY_LOGIN, false);
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_ID_USER, pref.getString(KEY_ID_USER, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));

        // return user
        return user;
    }
}
