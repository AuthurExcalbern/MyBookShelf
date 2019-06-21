package com.zyx.bookeshelf;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.zxing.Result;
import com.zyx.bean.Book;
import com.zyx.service.BookDownloader;
import com.zyx.service.BookService;
import com.zyx.service.BookServiceImpl;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.zyx.bookeshelf.BookEditActivity.Book_tag;

public class AddActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mZXingScannerView;
    private static final String TAG = "AddActivity";
    public static final int ADD_REQUEST_CODE = 203;
    public static final int ADD_REQUEST = 204;
    public static final int ADD_RESULT_CODE = 3;
    private static final int CAMERA_PERMISSION = 1;
    BookService bookService = BookServiceImpl.getInstance();
    Book resBook = new Book();


    private static final String FLASH_STATE = "FLASH_STATE";

    private boolean mFlash;
    private Toolbar mToolbar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestAllPower();
        if (savedInstanceState != null) {
            mFlash = savedInstanceState.getBoolean(FLASH_STATE, false);
        } else {
            mFlash = false;
        }
//        try{
            setContentView(R.layout.add_book);
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        mToolbar = (Toolbar) findViewById(R.id.singleScanToolbar);
        mToolbar.setTitle(R.string.scan_toolbar_title);
        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.singleScanFrame);
        mZXingScannerView = new ZXingScannerView(this); // 将ZXingScannerView作为布局
        contentFrame.addView(mZXingScannerView);
//        setContentView(mZXingScannerView);
        mZXingScannerView.setResultHandler(this);
        mZXingScannerView.setAutoFocus(true);
        mZXingScannerView.setFlash(mFlash);
    }

    public void requestAllPower() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, 1);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mZXingScannerView.setResultHandler(this); // 设置处理结果回调
//        mZXingScannerView.startCamera(); // 打开摄像头
        mZXingScannerView.setResultHandler(this);
        mZXingScannerView.setAutoFocus(true);
        mZXingScannerView.setFlash(mFlash);
        mZXingScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mZXingScannerView.stopCamera(); // 活动失去焦点的时候关闭摄像头
    }

    @Override
    public void handleResult(Result result) { // 实现回调接口，将数据回传并结束活动
//        Intent data = new Intent();
//        data.putExtra("text", result.getText());
//        setResult(RESULT_OK, data);
//        Toast.makeText(BookDetailActivity.this,"good!",Toast.LENGTH_SHORT).show();
        addBook(result.getText());
    }

    private void addBook(final String isbn) {
        mZXingScannerView.stopCamera();
        //添加图书
        //resBook =bookService.DownloadBook(isbn);
        final BookDownloader downloader = new BookDownloader(isbn);
       android.os.Handler handler = new android.os.Handler() {
            public void handleMessage(Message msg) {
                resBook = downloader.getRes();
                Log.v("resBook",resBook.getName());
                Intent intent = new Intent(AddActivity.this,MainActivity.class);
                intent.putExtra("resBook",resBook);
               // Log.v("RESBOOK",resBook.getName());
                setResult(AddActivity.ADD_RESULT_CODE,intent);
                finish();
            }
        };
        downloader.download(handler);
        resBook.setIsbn(isbn);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem;

        if (mFlash) {
            menuItem = menu.add(Menu.NONE, R.id.menu_simple_add_flash, 0, R.string.menu_single_add_flash_on);
            menuItem.setIcon(R.drawable.ic_flash_on);
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_simple_add_flash, 0, R.string.menu_single_add_flash_off);
            menuItem.setIcon(R.drawable.ic_flash_off);
        }

        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_IF_ROOM);

//        menuItem = menu.add(Menu.NONE, R.id.menu_simple_add_manually, 0, R.string.menu_single_add_manually);
//        MenuItemCompat.setShowAsAction(menuItem, MenuItemCompat.SHOW_AS_ACTION_NEVER);

        menuItem = menu.add(Menu.NONE, R.id.menu_simple_add_totally_manual, 1, R.string.menu_single_add_totally_manually);
        MenuItemCompat.setShowAsAction(menuItem, MenuItemCompat.SHOW_AS_ACTION_NEVER);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_simple_add_flash:
                mFlash = !mFlash;
                if (mFlash) {
                    item.setTitle(R.string.menu_single_add_flash_on);
                    item.setIcon(R.drawable.ic_flash_on);
                } else {
                    item.setTitle(R.string.menu_single_add_flash_off);
                    item.setIcon(R.drawable.ic_flash_off);
                }
                mZXingScannerView.setFlash(mFlash);
                break;
            case R.id.menu_simple_add_totally_manual:
//                Book mBook = new Book();
//                Intent i = new Intent(AddActivity.this, EditActivity.class);
//                i.putExtra("123", mBook);
//                startActivity(i);
                Intent i = new Intent(AddActivity.this,BookEditActivity.class);
                Book b = new Book();
                i.putExtra(Book_tag,b);
                BookEditActivity.reCode =  AddActivity.ADD_REQUEST_CODE;
                startActivityForResult(i,ADD_REQUEST_CODE);
                Toast.makeText(AddActivity.this,"added",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
      //  super.onActivityResult(requestCode, resultCode, data);
        //编辑界面手动添加图书
        if(resultCode == ADD_REQUEST){
            Book book = (Book) data.getSerializableExtra("resBook");
            Intent intent1=new Intent(AddActivity.this,MainActivity.class);
            intent1.putExtra("resBook",book);
            setResult(AddActivity.ADD_REQUEST,intent1);
        }

    }
}