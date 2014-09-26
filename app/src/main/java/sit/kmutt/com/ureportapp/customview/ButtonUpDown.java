package sit.kmutt.com.ureportapp.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import sit.kmutt.com.ureportapp.R;

/**
 * Created by Thanabut on 26/9/2557.
 */
public class ButtonUpDown extends Button implements View.OnTouchListener, View.OnDragListener{
    private ButtonUpDown btnPair;
    public final static int UP = 1;
    public final static int DOWN = 2;
    private boolean isTouch = false;
    private int curBackground ;
    private int btnName = 1;


    public ButtonUpDown(Context context) {
        super(context);
    }

    public ButtonUpDown(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public ButtonUpDown(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public void setActivateUpDown(boolean isActivate){
        if(isActivate && !this.isTouch){
            if(this.btnName == this.UP){
                this.setBackgroundResource(R.drawable.button_up_selector);
                btnPair.setBackgroundResource(R.drawable.button_selector);
                this.isTouch = true;
                btnPair.setTouch(false);
            }
            else{
                this.setBackgroundResource(R.drawable.button_down_selector);
                btnPair.setBackgroundResource(R.drawable.button_selector);
                this.isTouch = true;
                btnPair.setTouch(false);
            }

        }
        else{
            this.setBackgroundResource(R.drawable.button_selector);
            this.isTouch = false;


        }
    }

    public void setBg(int drawable, boolean isTemp){
        if(!isTemp){
            this.setCurBackground(drawable);
        }
        this.setBackgroundResource(drawable);
    }

    public int getCurBackground() {
        return curBackground;
    }

    public void setCurBackground(int curBackground) {
        this.curBackground = curBackground;
    }

    public ButtonUpDown getBtnPair() {
        return btnPair;
    }

    public void setBtnPair(ButtonUpDown btnPair) {
        this.btnPair = btnPair;
    }

    public int getUP() {
        return UP;
    }

    public int getDOWN() {
        return DOWN;
    }

    public boolean isTouch() {
        return isTouch;
    }

    public void setTouch(boolean isTouch) {
        this.isTouch = isTouch;
    }

    public int getBtnName() {
        return btnName;
    }

    public void setBtnName(int btnName) {
        this.btnName = btnName;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                v.setBackgroundResource(R.drawable.button_bg_hold);
                Log.i("On touch","down");
                // Start action ...
                break;
            case MotionEvent.ACTION_UP:
                Log.i("On touch","ACTION_UP");
                break;
            case MotionEvent.ACTION_OUTSIDE:
                // Stop action ...
                Log.i("On touch","ACTION_OUTSIDE");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.i("On touch","ACTION_POINTER_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.i("On touch","ACTION_POINTER_UP");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("On touch","ACTION_MOVE");
                break;
        }
        return false;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        this.setBg(this.getCurBackground(),false);
        Log.i("Drag" , "DDD");
        return false;
    }
}

