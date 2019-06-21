package com.zyx.scanner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
//import android.support.annotation.NonNull;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.MenuItemCompat;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;
import com.zyx.bean.Book;
import com.zyx.bookeshelf.AddActivity;
import com.zyx.bookeshelf.MainActivity;
import com.zyx.bookeshelf.R;
import com.zyx.scanner.Bulk_List_Fragment;
import com.zyx.service.BookDownloader;
import com.zyx.service.BookService;
import com.zyx.service.BookServiceImpl;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.zyx.scanner.Bulk_List_Fragment.list;

public class Bulk_Scan_Fragment extends Fragment implements ZXingScannerView.ResultHandler {
    private static final String TAG = "BatchScanFragment";
    private static final String FLASH_STATE = "FLASH_STATE";

    private ZXingScannerView mScannerView;
    public static boolean mFlash = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_batch_scan, container, false);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            mFlash = savedInstanceState.getBoolean(FLASH_STATE, false);
        } else {
            mFlash = false;
        }
        ViewGroup viewGroup = (ViewGroup) view.findViewById(R.id.batch_add_frame_scan);
        mScannerView = new ZXingScannerView(getActivity());
        mScannerView.setAutoFocus(true);
        mScannerView.setFlash(mFlash);
        mScannerView.setResultHandler(this);
        viewGroup.addView(mScannerView);
        return view;
    }

    private void addBook(final String isbn) {
        Toast.makeText(getContext(),isbn,Toast.LENGTH_LONG).show();
        BookService b = BookServiceImpl.getInstance();
        final BookDownloader bookDownloader = new BookDownloader(isbn);
        android.os.Handler handler = new android.os.Handler() {
            public void handleMessage(Message msg) {
                Book resBook= new Book();
                resBook.setIsbn(isbn);
                resBook = bookDownloader.getRes();
                Log.v("resBook",resBook.getName());
                if(list == null)
                    list = new ArrayList<>();
                list.add(resBook);
            }
        };
        bookDownloader.download(handler);

    }

    @Override
    public void handleResult(Result rawResult) {
        Log.i(TAG, "ScanResult Contents = " + rawResult.getText() + ", Format = " + rawResult.getBarcodeFormat().toString());
        addBook(rawResult.getText());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(Bulk_Scan_Fragment.this);
            }
        }, 2000);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.setAutoFocus(true);
        mScannerView.setFlash(mFlash);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
}

