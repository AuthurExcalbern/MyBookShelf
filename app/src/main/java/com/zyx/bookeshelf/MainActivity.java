package com.zyx.bookeshelf;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
//import android.support.design.internal.NavigationMenu;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AlertDialog;
//import android.util.Log;
//import android.view.MenuInflater;
import android.view.View;
//import android.support.design.widget.NavigationView;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
//import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
//import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
//import android.widget.TextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.zyx.adpter.BookListAdapter;
import com.zyx.bean.Book;
import com.zyx.bean.Shelf;
import com.zyx.bean.Tag;
//import com.zyx.scanner.Bulk_Addition;
import com.zyx.scanner.Bulk_Addition;
import com.zyx.service.BookService;
import com.zyx.service.BookServiceImpl;
import com.zyx.service.ShelfService;
import com.zyx.service.ShelfServiceImpl;
import com.zyx.service.TagService;
import com.zyx.service.TagServiceImpl;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private  ArrayList<Shelf> shelfs=new ArrayList<>();
    BookListAdapter bookListAdapter;
    ArrayList<Book> books = new ArrayList<>();
    ArrayList<Tag> tags = new ArrayList<>();
    ShelfService shelfService = ShelfServiceImpl.getInstance();
    BookService bookService = BookServiceImpl.getInstance();
    TagService tagService = TagServiceImpl.getInstance();
    int currentShelfId=-2;
    int currentTagId=-2;
    int currentShelfPosition=-1;
    static  ArrayAdapter<String>  shelfSpinnerAdapter;
    static  List<String> resshelfs = new ArrayList<>();//书架spinner绑定数据源
    //已选书架及标签id，未选择为-2;
    public ArrayList<Book> res,searchRes,tagRes,shelfRes;//存放各个操作后的结果
    public ArrayList<Book> preRes = new ArrayList<>();//保存搜索前结果
    String newTagName;//重命名时使用
    String newShelfName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        Tag g = new Tag();
//        g.setName("高数");
//        Tag a = new Tag();
//        a.setName("物理");
//        List<Tag> tags = new ArrayList<>();
//        tags.add(g);
//        tags.add(a);
//        tagService.SaveTags(MainActivity.this,tags);
        List<Book> tmpBook = bookService.GetBooks(MainActivity.this);
        List<Tag> tmpTag = tagService.GetTags(MainActivity.this);
        List<Shelf> tmpShelf= shelfService.GetShelves(MainActivity.this);
        if( tmpShelf!=null){
            for(Shelf s: tmpShelf){
                shelfs .add(s);
            }
        }
        if( tmpBook !=null){
            for(Book b:tmpBook){
                books .add(b);
            }
        }
        if(tmpTag!=null){
            for(Tag t: tmpTag){
                tags .add(t);
            }
        }
//        Shelf newShelf = new Shelf();
//        newShelf.setName("娱乐");
//        shelfs.add(newShelf);
//        shelfService.InsertShelf(MainActivity.this,newShelf);
        resshelfs.add("所有");
        for(Shelf s:shelfs){
            resshelfs.add(s.getName());
        }
        Spinner shelfSpinner = findViewById(R.id.shelfSpinner);
        shelfSpinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,resshelfs);
        shelfSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shelfSpinner.setAdapter(shelfSpinnerAdapter);
        shelfSpinner.setOnItemSelectedListener(new ShelfSpinnerClickListener());
        FloatingActionButton add = findViewById(R.id.add);
        add.setOnClickListener(new scannerAddListener());
        FloatingActionButton multiadd = findViewById(R.id.multiadd);
        multiadd.setOnClickListener(new scannerMultiAddListener());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            FloatingActionButton add = findViewById(R.id.add);
            FloatingActionButton multiadd = findViewById(R.id.multiadd);
            TextView addHint = findViewById(R.id.addHint);
            TextView multiaddHint = findViewById(R.id.multiaddHint);
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                if(multiadd.getVisibility()==View.GONE){
                    add.setVisibility(View.VISIBLE);
                    multiadd.setVisibility(View.VISIBLE);
                    addHint.setVisibility(View.VISIBLE);
                    multiaddHint.setVisibility(View.VISIBLE);
                }else {
                    addHint.setVisibility(View.GONE);
                    multiaddHint.setVisibility(View.GONE);
                    add.setVisibility(View.GONE);
                    multiadd.setVisibility(View.GONE);
                }
            }
        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        Book testBook = new Book();
//        testBook.setAuthor("Kennth C.Louden");
//        testBook.setName("UNIX环境高级编程");
//        testBook.setShelfId(2);
//        testBook.setPublisher("机械工业出版社");
//        testBook.setCover(R.drawable.ic_menu_camera+"");
//        testBook.setPublish_month("8");
//        testBook.setPublish_year("2010");
//        books.add(testBook);
//        bookService.InsertBook(MainActivity.this,testBook);


//        Book testBook = new Book();
//        testBook.setAuthor("Kennth C.Louden");
//        testBook.setName("疯狂java讲义");
//        testBook.setShelfId(1);
//        testBook.setId(5);
//        testBook.setPublisher("机械工业出版社");
//        testBook.setCover(R.drawable.ic_menu_camera+"");
//        testBook.setPublish_month("8");
//        testBook.setPublish_year("2010");
//        Book test = new Book();
//        test.setName("人工智能");
//        test.setId(6);
//        test.setCover(R.drawable.ic_menu_camera+"");
//        test.setAuthor("严蔚敏");
//        test.setShelfId(2);
//        test.setPublish_month("10");
//        test.setPublish_year("2020");
//
//        books.add(testBook);
//        books.add(test);


       // bookService.SaveBooks(MainActivity.this,books);
       // shelfService.SaveShelf(MainActivity.this,shelfs);
       // List<Book> resBooks =  bookService.GetBooks(MainActivity.this);

//        对书籍进行排序，注意保存副本以免在再次存入本地文件时修改了本地文件的顺序
        //bookService.SortByAuthor(resBooks);

//        查看书架中的书
//        resBooks = bookService.SelectByShelf(s,resBooks);


//        shelfService.SaveShelf(MainActivity.this,shelves);

//        插入测试 shelf只需要名字，id自增 ;book 以及 tag 同理
//        Shelf newShelf = new Shelf();
//        newShelf.setName("科技");
//        shelfService.InsertShelf(MainActivity.this,newShelf);



        bookListAdapter = new BookListAdapter(books,this);
        ListView bookList = findViewById(R.id.book_list);
        bookList.setAdapter(bookListAdapter);
        bookList.setOnItemClickListener(new bookItemClickListener());


        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        for (Tag tag:tags) {
           MenuItem menuItem =  navigationView.getMenu().add(R.id.nav_subTag, tag.getId(), 0, tag.getName());

        }


        SearchView searchView = findViewById(R.id.search_bar);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new searchQueryListener());
        searchView.setOnSearchClickListener(new onSearchClickListener());

        //点击弹出侧滑栏
        ImageView openNav = findViewById(R.id.open_nav);
        openNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            drawer.openDrawer(navigationView);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //排序依据选择
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        EditText editText = new EditText(this);
        //res = (ArrayList<Book>) books.clone();
        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.menu_author:
                bookService.SortByAuthor(res);
                break;
            case R.id.menu_publisher:
                bookService.SortByPublisher(res);
                break;
            case R.id.menu_title:
                bookService.SortByName(res);
                break;
            case R.id.menu_publish_time :
                bookService.SortByTime(res);
                break;
            case R.id.menu_deleteTag :
                if(currentTagId == -2){
                    Toast.makeText(MainActivity.this,"未选中任何标签",Toast.LENGTH_LONG).show();
                    break;
                }
                boolean isTagDelete = tagService.DeleteTag(MainActivity.this,currentTagId);
                if(isTagDelete){
                    Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_LONG).show();
                    NavigationView nav = findViewById(R.id.nav_view);
                    nav.getMenu().removeItem(currentTagId);
                }else {
                    Toast.makeText(MainActivity.this,"删除失败",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.menu_renameTag :
                if(currentTagId == -2){
                    Toast.makeText(MainActivity.this,"未选中任何标签",Toast.LENGTH_LONG).show();
                    break;
                }
                newTagName = editText.getText().toString();
                new AlertDialog.Builder(this).setTitle("重命名").setView(editText).setPositiveButton("确定",new onRenameTagListener(editText)).setNegativeButton("取消",null).show();
                break;
            case R.id.menu_renameShelf :
                if(currentShelfId==-2){
                    Toast.makeText(MainActivity.this,"未选中任何书架",Toast.LENGTH_LONG).show();
                    break;
                }else {
                    newShelfName=editText.getText().toString();
                    new AlertDialog.Builder(this).setTitle("重命名").setView(editText).setPositiveButton("确定",new onRenameShelfListener(editText)).setNegativeButton("取消",null).show();

                }
                break;

            case R.id.menu_deleteShelf :
                if(currentShelfId==-2){
                    Toast.makeText(MainActivity.this,"未选中任何书架",Toast.LENGTH_LONG).show();
                }else {
                boolean isShelfDelete = shelfService.DeleteShelf(this,currentShelfId);
                        if(isShelfDelete){
                        resshelfs.remove(currentShelfPosition);
                        shelfSpinnerAdapter.notifyDataSetChanged();
                        bookListAdapter.updateView(res,MainActivity.this);
                        bookListAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }

        return  true;
        //return super.onOptionsItemSelected(item);
    }



    //侧滑栏
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String itemTitle = (String) item.getTitle();
        Toast.makeText(MainActivity.this,id+"",Toast.LENGTH_LONG).show();
        switch (id){
            case R.id.nav_search:
                Toast.makeText(MainActivity.this,"搜索",Toast.LENGTH_LONG).show();
                SearchView searchView = findViewById(R.id.search_bar);
                searchView.requestFocus();
                searchView.onActionViewExpanded();
                break;
            case R.id.nav_addTag:
                //添加标签
                final EditText editText = new EditText(this);
                new AlertDialog.Builder(this).setTitle("添加标签").setView(editText).setPositiveButton("确定",new AddTagClickListen(editText)).setNegativeButton("取消",null).show();
                break;
            case R.id.nav_book:
                //展示所有
                currentShelfId = -2;
                currentTagId = -2;
                bookListAdapter.updateView(books,MainActivity.this);
                bookListAdapter.notifyDataSetChanged();
                break;
            case R.id.nav_setting:
                break;
            case R.id.nav_about:
                break;
             default:
                 //书架筛选书籍
                 Tag tag = new Tag();
                 tag.setId(id);
                 tag.setName(itemTitle);
                 currentTagId = id;
                 res = (ArrayList<Book>) bookService.SelectByTag(tag,books);
                 //tagRes = (ArrayList<Book>) res.clone();
                 bookListAdapter.updateView(res,MainActivity.this);
                 bookListAdapter.notifyDataSetChanged();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
      //  super.onActivityResult(requestCode, resultCode, data);
        Book book;
        if(data == null)
            return;
        if(resultCode == BookEditActivity.EDIT_RESULT_CODE) {
            book = (Book) data.getSerializableExtra("resBook");
            for (int i = 0; i < books.size(); i++) {
                if (books.get(i).getId() == book.getId()) {
                    books.set(i, book);
                    break;
                }
            }
        }
        if(resultCode == AddActivity.ADD_RESULT_CODE){
                book = (Book) data.getSerializableExtra("resBook");
                String tt = book.getName();
                int resId = bookService.InsertBook(MainActivity.this,book);
                book.setId(resId);
                books.add(book);
        }
        if(resultCode == DetailActivity.DETAIL_RESULT_CODE){
            Book b = (Book) data.getSerializableExtra("deleteId");
            int id = b.getId();
            if(bookService.DeleteBook(MainActivity.this,id)){
                Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(MainActivity.this,"删除失败",Toast.LENGTH_LONG).show();
            }
            books = (ArrayList<Book>) bookService.GetBooks(MainActivity.this);
        }
        if(resultCode == AddActivity.ADD_REQUEST){
            Toast.makeText(MainActivity.this,"手动添加",Toast.LENGTH_LONG).show();
        }
        if(resultCode == Bulk_Addition.BULK_RESULT_CODE){
            ArrayList<Book> res = (ArrayList<Book>) data.getSerializableExtra("SCAN_RESULT_LIST");
            for(Book b:res){
                int id = bookService.InsertBook(MainActivity.this,b);
                b.setId(id);
                books.add(b);
            }
        }
            bookListAdapter.updateView(books,MainActivity.this);
           // bookListAdapter.notifyDataSetChanged();
            //Toast.makeText(MainActivity.this,book.getName(),Toast.LENGTH_LONG).show();
    }

    //    protected void onActivityResult(int requestCode,int resultCode ,Intent intent){
//        super.onActivityResult(requestCode,resultCode,intent);
//        if(resultCode == BookEditActivity.EDIT_RESULT_CODE){
//            Toast.makeText(MainActivity.this,"编辑完成",Toast.LENGTH_LONG).show();
//        }
//    }



    public class ShelfSpinnerClickListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //第0个是所有书架
            if(position!=0){
                 res =(ArrayList<Book>) bookService.SelectByShelf(shelfs.get(position-1),books);
                 shelfRes = (ArrayList<Book>) res.clone();
                 currentShelfId = shelfs.get(position-1).getId();
                 currentShelfPosition=position;
                bookListAdapter.updateView(res,MainActivity.this);
                bookListAdapter.notifyDataSetChanged();
            }else {

                //初始化显示所有的书
               // if(res == null)
                res = books;
                currentShelfId = -2;
                shelfRes = (ArrayList<Book>) res.clone();
                bookListAdapter.updateView(res,MainActivity.this);
                bookListAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            bookListAdapter.updateView(shelfRes,MainActivity.this);
            bookListAdapter.notifyDataSetChanged();
        }
    }

    private class searchQueryListener implements SearchView.OnQueryTextListener {
        //搜索
        @Override
        public boolean onQueryTextSubmit(String query) {
            res= (ArrayList<Book>) bookService.SearchBook(books,query);
            //searchRes = (ArrayList<Book>) res.clone();
            bookListAdapter.updateView(res,MainActivity.this);
            bookListAdapter.notifyDataSetChanged();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if(newText.equals("")) {
                bookListAdapter.updateView(preRes, MainActivity.this);
                bookListAdapter.notifyDataSetChanged();
            }
            return false;
        }
    }

    private class AddTagClickListen implements DialogInterface.OnClickListener {
        EditText edit;
        AddTagClickListen(EditText editText){
            edit = editText;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String input = edit.getText().toString();
            if(input.equals(""))
                return;
            else {
                Toast.makeText(MainActivity.this,input,Toast.LENGTH_LONG).show();
                Tag newTag = new Tag();
                newTag.setName(input);
                int id = tagService.InsertTag(MainActivity.this,newTag);
                if(id == -1) {
                    Toast.makeText(MainActivity.this,"已存在该标签",Toast.LENGTH_LONG).show();
                    return;
                }
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.getMenu().add(R.id.nav_subTag, id, 0, newTag.getName());
            }
        }
    }

    private class onSearchClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
           preRes  = (ArrayList<Book>) res.clone();//保存搜索前结果
        }
    }


    private class onRenameTagListener implements DialogInterface.OnClickListener {
        EditText et;
        onRenameTagListener(EditText e){
            et = e;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            newTagName = et.getText().toString();
            boolean isTagRename = tagService.RenameTag(MainActivity.this,currentTagId,newTagName);
            if(isTagRename){
                Toast.makeText(MainActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                NavigationView navigationView = findViewById(R.id.nav_view);
                navigationView.getMenu().findItem(currentTagId).setTitle(newTagName);
            }else {
                Toast.makeText(MainActivity.this,"修改失败",Toast.LENGTH_LONG).show();
            }
        }
    }

    private class onRenameShelfListener implements DialogInterface.OnClickListener {
        EditText et;
        onRenameShelfListener(EditText e){
            et = e;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            newShelfName = et.getText().toString();
            boolean isShelfRename = shelfService.RenameShelf(MainActivity.this,currentShelfId,newShelfName);
            if(isShelfRename){
                Toast.makeText(MainActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                resshelfs.set(currentShelfPosition,newShelfName);
                shelfSpinnerAdapter.notifyDataSetChanged();
            }else {
                Toast.makeText(MainActivity.this,"修改失败",Toast.LENGTH_LONG).show();
            }
        }
    }

    private class scannerAddListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent i = new Intent();
            i.setClass(MainActivity.this,AddActivity.class);
            startActivityForResult(i,AddActivity.ADD_REQUEST_CODE);
           // bookService.DownloadBook("9787111077039");
        }
    }

    private class scannerMultiAddListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, Bulk_Addition.class);
            startActivityForResult(intent, Bulk_Addition.BULK_REQUEST_CODE);
        }
    }

    private class bookItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent i = new Intent(MainActivity.this,DetailActivity.class);
            i.putExtra(DetailActivity.Intent_Book_ToEdit,books.get(position));
           // startActivity(i);
            try {
                startActivityForResult(i,DetailActivity.DETAIL_REQUEST_CODE);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
