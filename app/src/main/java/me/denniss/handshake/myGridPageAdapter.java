package me.denniss.handshake;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tomer on 2/22/2015.
 */
public class myGridPageAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    private ArrayList<Fragment> mPages;

    public myGridPageAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        initPages();
    }

    private void initPages() {
        mPages = new ArrayList<>();

        ListenForShakePageFragment row1 = new ListenForShakePageFragment();

        LearnNewLessonFragment row2 = new LearnNewLessonFragment();

        mPages.add(row1);
        mPages.add(row2);

    }

    @Override
    public Fragment getFragment(int row, int col) {
        return mPages.get(col);
    }

    @Override
    public int getRowCount() {
       return 1;
    }

    @Override
    public int getColumnCount(int row) {
        return mPages.size();
    }
}