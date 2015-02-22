package me.denniss.handshake;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomer on 2/22/2015.
 */
public class SelectAppActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_app_layout);


        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List pkgAppsList = getPackageManager().queryIntentActivities( mainIntent, 0);

        List<String> apps = new ArrayList<>();
        for(ResolveInfo r : (List<ResolveInfo>)pkgAppsList)
        {
            apps.add(r.resolvePackageName);
        }

        WearableListView list = (WearableListView)findViewById(R.id.sample_list_view);
        SelectAppListAdapter adapter = new SelectAppListAdapter(this,apps);
        list.setAdapter(adapter);

    }
}
