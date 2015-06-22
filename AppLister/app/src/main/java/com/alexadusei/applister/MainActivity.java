package com.alexadusei.applister;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexadusei.applister.model.App;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "List Info";
    private RecyclerView rv;
    private ArrayList<App> appBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = (RecyclerView) findViewById(R.id.app_list);
        rv.setLayoutManager(new LinearLayoutManager(this));

        initializeData();
        initializeAdapter();
    }

    // get list of all installed apps.
    private void initializeData(){
        appBundle = new ArrayList<App>();
        String appNameData;
        String appVersionData;
        double appSizeData = 0;
        String sizePrefixData;
        Drawable appIconData;

        double appBytes = 0, appKB, appMB, appGB;

        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packs.size(); i++){
            PackageInfo p = packs.get(i);

            if (p.versionName == null) {
                continue;
            }

            appNameData = p.applicationInfo.loadLabel(getPackageManager()).toString();
            appVersionData = p.versionName;
            appIconData = p.applicationInfo.loadIcon(getPackageManager());

            try {
                ApplicationInfo tmpInfo = getPackageManager().getApplicationInfo(p.packageName, -1);
                appBytes = new File(tmpInfo.sourceDir).length();

            } catch (PackageManager.NameNotFoundException e){
                Log.d("Error", "An error occurred");
            }

            appKB = appBytes / 1024;
            appMB = appKB / 1024;
            appGB = appMB / 1024;

            if (appKB < 1000) {
                appSizeData = appKB;
                sizePrefixData = " KB";
            }
            else if (appMB < 1000) {
                appSizeData = appMB;
                sizePrefixData = " MB";
            }
            else {
                appSizeData = appGB;
                sizePrefixData = " GB";
            }

            appBundle.add(new App(appNameData, appVersionData, appSizeData, sizePrefixData, appIconData));
        }
    }

    private void initializeAdapter(){
        AppListAdapter adapter = new AppListAdapter(appBundle);
        rv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
