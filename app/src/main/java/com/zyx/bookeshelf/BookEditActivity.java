package com.zyx.bookeshelf;

//import android.app.AlertDialog;
//import android.content.DialogInterface;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.Spinner;
//import android.widget.SpinnerAdapter;
//import android.widget.TextView;
//import android.widget.Toast;

import com.zyx.bean.Book;
import com.zyx.bean.Shelf;
import com.zyx.bean.Tag;
import com.zyx.dao.ShelfDaoImpl;
import com.zyx.dao.TagDaoImpl;
import com.zyx.service.BookService;
import com.zyx.service.BookServiceImpl;
import com.zyx.service.ShelfService;
import com.zyx.service.ShelfServiceImpl;
import com.zyx.service.TagServiceImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BookEditActivity extends AppCompatActivity {
    public static String Book_tag="EditAct";
    public static final int EDIT_REQUEST_CODE = 202;
    public static final int EDIT_RESULT_CODE = 2;
    public static int reCode=0;//手动添加的请求码，用于在保存按钮判断是手动添加还是更改图书
    String[] readingstate=new String[]{"已读","阅读中","未读"};

    List<Shelf> bookshelfList =new ArrayList<Shelf>();
    ShelfServiceImpl bookShelfManager;

    List<Tag> labelList=new ArrayList<Tag>();
    TagServiceImpl labelManager;

    BookService bookService = BookServiceImpl.getInstance();

    Book book;

    Spinner bookshelf_spinner;
    Spinner readingstate_spinner;
    int present_bookshelfspinner_selection;
    int present_readingstatespinner_selection;

    ImageView book_pic_edit;
    EditText book_name;
    EditText author;
    EditText press;
    EditText press_time_year;
    EditText press_time_month;
    EditText isbn;
    EditText note_edit;
    EditText book_resource;

    TextView label;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

//        获得传过来的book对象
        Intent intent=getIntent();
        book=(Book)intent.getSerializableExtra(Book_tag);

        labelManager=TagServiceImpl.getInstance();
        if(labelManager.GetTags(BookEditActivity.this) !=null) {
            labelList = labelManager.GetTags(BookEditActivity.this);
        }

        //     bookshelfList.clear(); 先清除 然后再读入！

        Shelf firstShelf = new Shelf();
        firstShelf.setName("默认书架");
        bookshelfList.add(firstShelf);bookshelfList.add(firstShelf);
        //添加书架列表，要先判断是否是空
        bookShelfManager=ShelfServiceImpl.getInstance();
        if(bookShelfManager.GetShelves(BookEditActivity.this) != null) {
            for (Shelf temp : bookShelfManager.GetShelves(BookEditActivity.this)) {
                bookshelfList.add(temp);
            }
        }
        Shelf lastShelf = new Shelf();
        lastShelf.setName("添加书架");
        bookshelfList.add(lastShelf);



        //加载上方toolbar
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_edit) ;
        setSupportActionBar(toolbar);



        //初始化控件
        book_pic_edit=(ImageView)findViewById(R.id.bookpic_edit);
        book_name=(EditText) findViewById(R.id.book_name_edit);
        author=(EditText)findViewById(R.id.book_author_edit);
        press=(EditText) findViewById(R.id.book_press_edit);
        press_time_year=(EditText)findViewById(R.id.book_presstime_edit_year);
        press_time_month=(EditText)findViewById(R.id.book_presstime_edit_month);
        isbn=(EditText) findViewById(R.id.book_isbn_edit);
        readingstate_spinner=(Spinner)findViewById(R.id.spinner_readingstate);
        bookshelf_spinner=(Spinner) findViewById(R.id.spinner_bookshelf_edit);
        note_edit=(EditText)findViewById(R.id.book_note_edit);
        book_resource=(EditText) findViewById(R.id.book_source_edit);
        label=(TextView)findViewById(R.id.book_label_edit);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(BookEditActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage("图书信息未保存，请问是否继续？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });

        //获取该图书信息 赋值到各个图书的信息项
        setMessage_frombook();

        //阅读状态的spinner
        readingstate_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //标记当前的选择的阅读状态
                present_readingstatespinner_selection=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //标签点击
        label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //书籍标签 tag
                final int label_count=labelList.size();
                final String[] labelList_str=new String[label_count];
                for(int i=0;i<labelList.size();i++){
                    labelList_str[i]=labelList.get(i).getName();
                }


                //要根据这个book的 labelList 设置这个selected
                final boolean []selected=new boolean[label_count];
                if(book.getTags()!=null) {
                    for (int i = 0; i < label_count; i++) {
                        for (int j = 0; j < book.getTags().size(); j++) {
                            Integer temp = book.getTags().get(j);
                            String tagName = myGetTagNameById(temp, labelList);

                            if (tagName !="" && labelList_str[i].equals(tagName)) {//需要按tag的id设置select
                                selected[i] = true;
                                break;
                            }
                        }
                    }
                }
                else{
                    book.setTags(new ArrayList<Integer>());
                }


                AlertDialog.Builder dialog=new AlertDialog.Builder(BookEditActivity.this);
                dialog.setTitle("选择标签");
                dialog.setMultiChoiceItems(labelList_str, selected, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        //doing nothing
                    }
                });
                dialog.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String label_content="";
                        if(book.getTags()!=null) book.getTags().clear();
                        ArrayList<Integer> tags = (ArrayList<Integer>) book.getTags();
                        for(int i=0;i<label_count;i++){
                            //tag状态
                            if(selected[i]){
                                if(i==0){
                                    label_content=label_content+labelList_str[i];

                                }else{
                                    label_content=label_content+","+labelList_str[i];
                                }
                                if(myGetTagIdByName(labelList_str[i],labelList) !=-1){
                                    tags.add(myGetTagIdByName(labelList_str[i],labelList));
                                }
                            }
                        }
                        book.setTags(tags);
                        label.setText(label_content);
                        //这个book的 labelList要更新

                    }
                });


                dialog.show();
            }
        });


        //书架的spinner 监听鼠标点击
        bookshelf_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //如果在spinner里面点击的是“添加书架”这一项  则弹出对话框dialog
                if(position==bookshelf_spinner.getAdapter().getCount()-1){
                    final EditText bookshelf_name_edit=new EditText(BookEditActivity.this);
                    bookshelf_name_edit.setHint("请输入书架名称");
                    AlertDialog.Builder dialog=new AlertDialog.Builder(BookEditActivity.this);
                        dialog.setTitle("新建书架");                //dialog标题
                        dialog.setCancelable(false);                //按back键不可取消dialog
                        dialog.setView(bookshelf_name_edit);        //把editText放入dialog中
                        //确定按钮
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String bookshelfname=bookshelf_name_edit.getText().toString();
                                //在倒数第二位置添加新书架到书架的list中
//                                bookshelfList.add(bookshelfList.size()-1 , new BookShelf(bookshelfname));
                               Shelf temp=new Shelf();
                                temp.setName(bookshelfname);
                                bookshelfList.add(bookshelfList.size()-1 , temp);

                                bookShelfManager.InsertShelf(BookEditActivity.this,temp);
                                MainActivity.resshelfs.add(temp.getName());
                                MainActivity.shelfSpinnerAdapter.notifyDataSetChanged();
                                //刷新一下spinner的内容以及所选内容
                                refresh_bookshelf_spinner();

                            }
                        });
                    //取消按钮
                        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //取消添加书架，则默认回到当前的书架选择，以避免spinner显示"添加书架"这一项
                                //标记当前书架选择
                                    bookshelf_spinner.setSelection(present_bookshelfspinner_selection,true);
                            }
                        });
                    dialog.show();


                }else{
                    //标记当前书架选择
                    present_bookshelfspinner_selection=position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private Integer myGetTagIdByName(String s, List<Tag> labelList) {
        if(labelList != null)
        {
            for(Tag j:labelList){
                if(j.getName().equals(s)) return j.getId();
            }
        }
        return -1;
    }

    private String myGetTagNameById(Integer temp, List<Tag> labelList) {
        if(labelList != null)
        {
            for(Tag s:labelList){
                if(s.getId() == temp) return s.getName();
            }
        }
        return "";

    }


    public void refresh_bookshelf_spinner(){
        //刷新下拉框spinner内的内容
        int count=bookshelfList.size();
        String[] bookshelfList_names=new String[count-1];
        for(int i=1;i<count;i++){
            bookshelfList_names[i-1] = bookshelfList.get(i).getName();
        }
        ArrayAdapter<String> bookshelflist_adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,bookshelfList_names);
        bookshelflist_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookshelf_spinner.setAdapter(bookshelflist_adapter);
        //标记当前书架选择
        present_bookshelfspinner_selection=count-3;
        bookshelf_spinner.setSelection(present_bookshelfspinner_selection,true);



    }


    public void setMessage_frombook(){

//        book_pic_edit.setImageResource(book.getImageId());

        book_name.setText(book.getName());
        author.setText(book.getAuthor());
        press.setText(book.getPublisher());
        press_time_year.setText(book.getPublish_year());
        press_time_month.setText(book.getPublish_month());
        isbn.setText(book.getIsbn());
        note_edit.setText(book.getNote());
        book_resource.setText(book.getLink());
        String imgid = String.valueOf(book.getCover()).equals("")?R.drawable.book1_pic+"":String.valueOf(book.getCover());
        int imgResId = Integer.parseInt(imgid);
        book_pic_edit.setImageResource(imgResId);
       // book_pic_edit.setImageResource(Integer.valueOf(book.getCover()));
        String la = "";
        if(book.getTags()!=null){
            for(Integer t :book.getTags()){
                la += myGetTagNameById(t,labelList)+",";
            }
            label.setText(la);
        }




        //加载阅读状态下拉框
        ArrayAdapter<String> readingstate_adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,readingstate);
        readingstate_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        readingstate_spinner.setAdapter(readingstate_adapter);

        //设置阅读状态下拉框默认选中值
        SpinnerAdapter readingstate_spinnerAdapter=readingstate_spinner.getAdapter();
        int k_r=readingstate_spinnerAdapter.getCount();
        //readingstate_spinner.setSelection(2,true);
        if(book.getStatus()!=null) {
            for (int i = 0; i < k_r; i++) {
                if (book.getStatus().equals(readingstate_spinnerAdapter.getItem(i).toString())) {
                    //标记当前书架选择
                    present_readingstatespinner_selection = i;
                    readingstate_spinner.setSelection(present_readingstatespinner_selection, true);
                    break;
                }
            }
        }

        //加载书架下拉框

        int count=bookshelfList.size();
        String[] bookshelfList_names=new String[count-1];
        for(int i=1;i<count;i++){
            bookshelfList_names[i-1] = bookshelfList.get(i).getName();
        }
        ArrayAdapter<String> bookshelflist_adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,bookshelfList_names);
        bookshelflist_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookshelf_spinner.setAdapter(bookshelflist_adapter);

        //设置书架下拉框默认选中值
        SpinnerAdapter bookshelf_spinnerAdapter=bookshelf_spinner.getAdapter();
        int k_b=bookshelf_spinnerAdapter.getCount();
        for(int i=0;i<k_b;i++){
            if(bookshelfList.get(book.getShelfId()).getName().equals(bookshelf_spinnerAdapter.getItem(i).toString())){
                //标记当前书架选择
                present_bookshelfspinner_selection=i;
                bookshelf_spinner.setSelection(present_bookshelfspinner_selection,true);
                break;
            }
        }
    }

    public void setBook(){
        book.setName(book_name.getText().toString());
        book.setAuthor(author.getText().toString());
        book.setIsbn(isbn.getText().toString());
        book.setPublisher(press.getText().toString());
        book.setPublish_year(press_time_year.getText().toString());
        book.setPublish_month(press_time_month.getText().toString());
        book.setShelfId(getShelfIdByName(bookshelf_spinner.getAdapter().getItem(present_bookshelfspinner_selection).toString(),bookshelfList));
        book.setStatus(readingstate_spinner.getAdapter().getItem(present_readingstatespinner_selection).toString());
        book.setLink(book_resource.getText().toString());
        book.setNote(note_edit.getText().toString());
        //标签已经在程序中更新
    }

    private int getShelfIdByName(String s, List<Shelf> bookshelfList) {
        if(bookshelfList != null)
        {
            for(Shelf j:bookshelfList){
                if(j.getName().equals(s)) return j.getId();
            }
        }
        return -1;
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(BookEditActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage("图书信息未保存，请问是否继续？");
        dialog.setCancelable(false);
        dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BookEditActivity.this.finish();
            }
        });
        dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu,menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:

                setBook();//在退出保存时保存界面的编辑项

                if(reCode == AddActivity.ADD_REQUEST_CODE){
                    //用户手动添加图书。跳转回扫码界面再跳转主界面保存图书
                    Intent intent1=new Intent(BookEditActivity.this,AddActivity.class);
                    intent1.putExtra("resBook",book);
                    setResult(AddActivity.ADD_REQUEST,intent1);
                }
                else{
                    Intent intent1=new Intent(BookEditActivity.this,DetailActivity.class);
                    intent1.putExtra("resBook",book);
                    if(bookService.UpdateBook(BookEditActivity.this,book)){
                        Toast.makeText(BookEditActivity.this,"更新成功",Toast.LENGTH_LONG).show();
                        //startActivityForResult(intent1,BookEditActivity.EDIT_RESULT_CODE);
                        //startActivity(intent1);
                        setResult(BookEditActivity.EDIT_RESULT_CODE,intent1);
                    }
                }
                BookEditActivity.this.finish();


                break;
        }
        return true;
    }
}
