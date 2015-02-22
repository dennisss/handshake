package me.denniss.handshake;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.view.WindowManager;

public class WearActivity extends Activity {

    public static Gesture g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_view_activity);
        g = new Gesture(this);
        final GridViewPager mGridPager = (GridViewPager) findViewById(R.id.pager);
        mGridPager.setAdapter(new myGridPageAdapter(this,getFragmentManager()));
        DotsPageIndicator dots = (DotsPageIndicator) findViewById(R.id.indicator);
        dots.setPager(mGridPager);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @Override
    public void onResume() {
        super.onResume();
        WearActivity.g.resume();

    }

    @Override
    public void onPause() {
        super.onPause();
        WearActivity.g.pause();

    }

}
