package com.zyx.bookeshelf;

import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;

//import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.klinker.android.sliding.SlidingActivity;

import com.zyx.adpter.BookListAdapter;
import com.zyx.bean.Book;
import com.zyx.bean.Shelf;
import com.zyx.service.BookService;
import com.zyx.service.BookServiceImpl;
import com.zyx.service.ShelfService;
import com.zyx.service.ShelfServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class DetailActivity extends SlidingActivity {
    public static String Intent_Book_ToEdit = "BOOKTOEDIT";
    public static final int DETAIL_REQUEST_CODE = 201;
    public static final int DETAIL_RESULT_CODE = 1;
    private static final String TAG = "BookDetailActivity";
    private Book book;

    private TextView infoTitleTxt;
    private ImageView headerImg;
    private TextView addtimeTxt;
    private RelativeLayout authorRelativeLayout;
    private TextView authorTxt;
    private RelativeLayout translatorRelativeLayout;
    private TextView translatorTxt;
    private RelativeLayout publisherRelativeLayout;
    private TextView publisherTxt;
    private RelativeLayout pubtimeRelativeLayout;
    private TextView pubtimeTxt;
    private RelativeLayout isbnRelativeLayout;
    private TextView isbnTxt;
    private RelativeLayout readingStatusRelativeLayout;
    private TextView readingStatusTxt;
    private RelativeLayout bookshelfRelativeLayout;
    private TextView bookshelfTxt;
    private RelativeLayout notesRelativeLayout;
    private TextView notesTxt;
    private RelativeLayout labelsRelativeLayout;
    private TextView labelsTxt;
    private RelativeLayout websiteRelativeLayout;
    private TextView websiteTxt;
    private Button DelBook;

    @Override
    public void init(Bundle savedInstanceState) {

        Intent intent = getIntent();
        book = (Book) intent.getSerializableExtra(Intent_Book_ToEdit);
        setTitle(book.getName());
        setPrimaryColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
        );
        setContent(R.layout.detail_view);
        setHeaderContent(R.layout.detail_header);
        setFab(
                ContextCompat.getColor(this, R.color.colorAccent),
                R.drawable.ic_edit,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent i = new Intent(DetailActivity.this, EditActivity.class);
//                        i.putExtra(EditActivity.Book_tag, book);
//                        startActivity(i);
                        Intent iii=new Intent(DetailActivity.this,BookEditActivity.class);
                        iii.putExtra(BookEditActivity.Book_tag,book);
                        startActivityForResult(iii,BookEditActivity.EDIT_REQUEST_CODE);
                        //startActivity(iii);
//                        Toast.makeText(DetailActivity.this, "goto edit", Toast.LENGTH_SHORT).show();
//                        finish();
                    }
                }
        );

        setHeader();
        setBookInfo();
        setBookDetails();


    }

    private void setHeader() {
        DelBook=(Button)findViewById(R.id.BtnDel);
        DelBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent del=new Intent(DetailActivity.this,BookEditActivity.class);
                del.putExtra("deleteId",book);
                setResult(DetailActivity.DETAIL_RESULT_CODE,del);
                DetailActivity.this.finish();
            }
        });
        headerImg = (ImageView) findViewById(R.id.book_detail_header_image);
        //需要位图
//        if (book.getCover().length()!=0) {
//            String path = book.getCover();
//            Bitmap src = BitmapFactory.decodeFile(path);
//            headerImg.setImageBitmap(src);
//            headerImg.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
//        }
        addtimeTxt = (TextView) findViewById(R.id.book_detail_header_addtime_text);
        addtimeTxt.setText("你发现了个彩蛋！！！");
//
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(BookEditActivity.EDIT_RESULT_CODE,data);

        DetailActivity.this.finish();
       // Toast.makeText(DetailActivity.this,resultCode+"",Toast.LENGTH_LONG).show();
    }

    private void setBookInfo() {
        infoTitleTxt = (TextView) findViewById(R.id.book_detail_info_title_bar_text);
        authorRelativeLayout = (RelativeLayout) findViewById(R.id.book_detail_info_author_item);
        authorTxt = (TextView) findViewById(R.id.book_detail_info_author_text);
        translatorRelativeLayout = (RelativeLayout) findViewById(R.id.book_detail_info_translator_item);
        translatorTxt = (TextView) findViewById(R.id.book_detail_info_translator_text);
        publisherRelativeLayout = (RelativeLayout) findViewById(R.id.book_detail_info_publisher_item);
        publisherTxt = (TextView) findViewById(R.id.book_detail_info_publisher_text);
        pubtimeRelativeLayout = (RelativeLayout) findViewById(R.id.book_detail_info_pubtime_item);
        pubtimeTxt = (TextView) findViewById(R.id.book_detail_info_pubtime_text);
        isbnRelativeLayout = (RelativeLayout) findViewById(R.id.book_detail_info_isbn_item);
        isbnTxt = (TextView) findViewById(R.id.book_detail_info_isbn_text);


        final ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        String authors = book.getAuthor();
        if (authors!=null) {
        authorTxt.setText(authors);
        authorRelativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(
                        DetailActivity.this,
                        getResources().getString(R.string.book_info_author_toast),//作者已复制到剪贴板
                        Toast.LENGTH_SHORT)
                        .show();
                ClipData clipData = ClipData.newPlainText(
                        getString(R.string.app_name),
                        String.format(getString(R.string.book_info_author_clipboard_content),
                                authorTxt.getText().toString()));
                clipboardManager.setPrimaryClip(clipData);
                return true;
            }
        });
        } else {
            authorRelativeLayout.setVisibility(View.GONE);
        }
//
        String translators = book.getTranslator();
        if (translators !=null ) {
        translatorTxt.setText(translators);
        translatorRelativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(
                        DetailActivity.this,
                        getResources().getString(R.string.book_info_translator_toast),//xx已复制到剪贴板
                        Toast.LENGTH_SHORT)
                        .show();
                ClipData clipData = ClipData.newPlainText(
                        getString(R.string.app_name),
                        String.format(getString(R.string.book_info_translator_clipboard_content),
                                translatorTxt.getText().toString()));
                clipboardManager.setPrimaryClip(clipData);
                return true;
            }
        });
        } else {
            translatorRelativeLayout.setVisibility(View.GONE);
        }
//
        String publisher=book.getPublisher();
        if (publisher != null) {
        publisherTxt.setText(publisher);
        publisherRelativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(
                        DetailActivity.this,
                        getResources().getString(R.string.book_info_publisher_toast),
                        Toast.LENGTH_SHORT)
                        .show();
                ClipData clipData = ClipData.newPlainText(
                        getString(R.string.app_name),
                        String.format(getString(R.string.book_info_publisher_clipboard_content),//xx已复制到剪贴板
                                publisherTxt.getText().toString()));
                clipboardManager.setPrimaryClip(clipData);
                return true;
            }
        });
        } else {
            publisherRelativeLayout.setVisibility(View.GONE);
        }
//

        String year = book.getPublish_year();
        String month = book.getPublish_month();
        if (year == null && month == null) {
            pubtimeRelativeLayout.setVisibility(View.GONE);
        } else {
            String time=year+"-"+month;
        pubtimeTxt.setText(time);
        pubtimeRelativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(
                        DetailActivity.this,
                        getResources().getString(R.string.book_info_pubtime_toast),
                        Toast.LENGTH_SHORT)
                        .show();
                ClipData clipData = ClipData.newPlainText(
                        getString(R.string.app_name),
                        String.format(getString(R.string.book_info_pubtime_clipboard_content),//xx已复制到剪贴板
                                pubtimeTxt.getText().toString()));
                clipboardManager.setPrimaryClip(clipData);
                return true;
            }
        });
        }

        String isbn = book.getIsbn();
        if (isbn != null) {
        isbnTxt.setText(isbn);
        isbnRelativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(
                        DetailActivity.this,
                        getResources().getString(R.string.book_info_isbn_toast),
                        Toast.LENGTH_SHORT)
                        .show();
                ClipData clipData = ClipData.newPlainText(
                        getString(R.string.app_name),
                        String.format(getString(R.string.book_info_isbn_clipboard_content),//xx已复制到剪贴板
                                isbnTxt.getText().toString()));
                clipboardManager.setPrimaryClip(clipData);
                return true;
            }
        });
        } else {
            isbnRelativeLayout.setVisibility(View.GONE);
        }
//
        infoTitleTxt.setText("信息");
//
    }

    private void setBookDetails() {
        readingStatusRelativeLayout = (RelativeLayout) findViewById(R.id.book_detail_reading_status_item);
        readingStatusTxt = (TextView) findViewById(R.id.book_detail_reading_status_text);
        bookshelfRelativeLayout = (RelativeLayout) findViewById(R.id.book_detail_bookshelf_item);
        bookshelfTxt = (TextView) findViewById(R.id.book_detail_bookshelf_text);
        notesRelativeLayout = (RelativeLayout) findViewById(R.id.book_detail_notes_item);
        notesTxt = (TextView) findViewById(R.id.book_detail_notes_text);
        labelsRelativeLayout = (RelativeLayout) findViewById(R.id.book_detail_labels_item);
        labelsTxt = (TextView) findViewById(R.id.book_detail_labels_text);
        websiteRelativeLayout = (RelativeLayout) findViewById(R.id.book_detail_website_item);
        websiteTxt = (TextView) findViewById(R.id.book_detail_website_text);
//
        readingStatusRelativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(
                        DetailActivity.this,
                        getResources().getString(R.string.reading_status_image_view),
                        Toast.LENGTH_SHORT)
                        .show();
                return true;
            }
        });
        String readingStatus = book.getStatus();
        if(readingStatus == null) {
            readingStatusTxt.setText("未读");
        }else{
            readingStatusTxt.setText(readingStatus);
        }
//
        bookshelfRelativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(
                        DetailActivity.this,
                        getResources().getString(R.string.book_shelf_image_view),
                        Toast.LENGTH_SHORT)
                        .show();
                return true;
            }
        });
//
        int sheifID=book.getShelfId();
        bookshelfTxt.setText("默认书架");
        if(sheifID > 0) {
            bookshelfTxt.setText("书架No."+sheifID);
        }

        String note = book.getNote();
        if (note != null) {
        notesTxt.setText(note);
        notesRelativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(
                        DetailActivity.this,
                        getResources().getString(R.string.note_edit_text_hint),
                        Toast.LENGTH_SHORT)
                        .show();
                return true;
            }
        });
        } else {
            notesRelativeLayout.setVisibility(View.GONE);
        }
//
        List<Integer> tagID = book.getTags();
        if (tagID != null) {
            StringBuilder tagTitle = new StringBuilder();
            tagTitle.append("标签");
            for (Integer id : tagID) {
                tagTitle.append(id);
                tagTitle.append(",");
            }
            tagTitle.deleteCharAt(tagTitle.length() - 1);
            labelsTxt.setText(tagTitle);
            labelsRelativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(
                            DetailActivity.this,
                            getResources().getString(R.string.book_detail_labels_image_view),
                            Toast.LENGTH_SHORT)
                            .show();
                    return true;
                }
            });
        } else {
            labelsRelativeLayout.setVisibility(View.GONE);
        }
//
        final String website = book.getLink();
        if (website != null) {
        websiteTxt.setText(website);
        websiteRelativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(
                        DetailActivity.this,
                        getResources().getString(R.string.website_edit_text_hint),
                        Toast.LENGTH_SHORT)
                        .show();
                return true;
            }
        });
        websiteRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(website));
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivity(i);
                } else {
                    Toast.makeText(DetailActivity.this, R.string.book_detail_browser_not_found, Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        } else {
            websiteRelativeLayout.setVisibility(View.GONE);
        }
    }
}
