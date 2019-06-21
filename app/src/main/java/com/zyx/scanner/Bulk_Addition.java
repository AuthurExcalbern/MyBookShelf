package com.zyx.scanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.support.design.widget.TabLayout;
//import android.support.v4.view.ViewPager;
//import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.zyx.bookeshelf.MainActivity;
import com.zyx.scanner.Bulk_Scan_Fragment;
import com.zyx.bookeshelf.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import static com.zyx.scanner.Bulk_List_Fragment.list;


public class Bulk_Addition extends AppCompatActivity {
    private TabLayout tablayout;
    private ViewPager viewPager;
    private Button close;
    private Button right;
    public static final int BULK_REQUEST_CODE = 205;
    public static final int BULK_RESULT_CODE = 5;
    private String[] titles = {"                     扫描                     ", "                      已添加                     "};
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bulk_addition_layout);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},1);
        }
        list = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        tablayout = (TabLayout)findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        close = (Button)findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bulk_Addition.this.finish();
            }
        });

        right = (Button)findViewById(R.id.right);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list == null || list.size() == 0){
                    finish();
                }else {
                    Intent intent = new Intent();
                    intent.setClass(Bulk_Addition.this, MainActivity.class);
                    intent.putExtra("SCAN_RESULT_LIST", list);
                    setResult(BULK_RESULT_CODE, intent);
                    finish();
                }
            }
        });

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));

        viewPager.setAdapter(myPagerAdapter);

        tablayout.setupWithViewPager(viewPager);

        LinearLayout linearLayout = (LinearLayout) tablayout.getChildAt(0);

        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
        @Override
        public Fragment getItem(int position) {
            if(position==0)
            {
                Bulk_Scan_Fragment bulkScanFragment = new Bulk_Scan_Fragment();
                return bulkScanFragment;
            }
            else
            {
                Bulk_List_Fragment te = new Bulk_List_Fragment();
                return te;
            }
        }
        @Override
        public int getCount() {
            return titles.length;
        }
    }
}
