package com.zyx.service;

import android.content.Context;

import com.zyx.bean.Book;
import com.zyx.bean.Shelf;
import com.zyx.bean.Tag;
import com.zyx.bookeshelf.MainActivity;
import com.zyx.comparator.authorComparator;
import com.zyx.comparator.nameComparator;
import com.zyx.comparator.publisherComparator;
import com.zyx.comparator.timeComparator;
import com.zyx.dao.BookDao;
import com.zyx.dao.BookDaoImpl;
import sun.misc.BASE64Encoder;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class BookServiceImpl implements BookService {
    BookDao bookDao = new BookDaoImpl();

    private  Book resBook;
    private static BookServiceImpl intance = null;
    private BookServiceImpl(){}
    public static BookServiceImpl getInstance(){
        if(intance == null)
            intance = new BookServiceImpl();
        return intance;
    }


    @Override
    public List<Book> SelectByTag(Tag tag, List<Book> books) {
        int id = tag.getId();
        List<Book> res = new ArrayList<>();
        for (Book b:books){
            if(b.getTags()!=null&&b.getTags().contains(id)){
                res.add(b);
            }
        }
        return res;
    }

    @Override
    public void SortByAuthor(List<Book> books) {
       Collections.sort(books,new authorComparator());
    }

    @Override
    public void SortByName(List<Book> books) {
        Collections.sort(books,new nameComparator());
    }

    @Override
    public void SortByTime(List<Book> books) {
        Collections.sort(books,new timeComparator());
    }

    @Override
    public void SortByPublisher(List<Book> books) {
        Collections.sort(books,new publisherComparator());
    }

    @Override
    public boolean UpdateBook(Context context, Book book) {
        ArrayList<Book> books = (ArrayList<Book>) GetBooks(context);

        for(int i=0;i<books.size();i++){
            if(books.get(i).getId() == book.getId()) {
                books.set(i,book);
                SaveBooks(context,books);
                return  true;
            }
        }
        return false;
    }


    @Override
    public boolean DeleteBook(Context context, int id) {
        ArrayList<Book> books = (ArrayList<Book>) GetBooks(context);
        for(Book b : books){
            if(b.getId() == id){
                books.remove(b);
                SaveBooks(context,books);
                return true;
            }

        }
        return false;
    }

    @Override
    public List<Book> SelectByShelf(Shelf shelf, List<Book> books) {
        int id = shelf.getId();
        List<Book> res = new ArrayList<>();
        for (Book b:books){
            if(b.getShelfId()==id)
                res.add(b);
        }
        return res;
    }

    @Override
    public void SaveBooks(Context context, List<Book> books) {
        bookDao.saveBook(books,context);
    }

    @Override
    public List<Book> GetBooks(Context context) {
        return bookDao.readBook(context);
    }

    @Override
    public List<Book> SearchBook(List<Book> list, String s) {
        List<Book> res = new ArrayList<>();

        for(Book b : list) {
            if(b.getName().indexOf(s) != -1 || b.getAuthor().indexOf(s) != -1 || b.getPublisher().indexOf(s) != -1)
                res.add(b);
        }
        return res;
    }

    @Override
    public int InsertBook(Context context,Book book) {
       return bookDao.InsertBook(context,book);
    }

    private class downloadIsbn  implements Runnable{
        String isbn;
        downloadIsbn(String i){
            isbn = i;
        }

        @Override
        public void run() {
            try {
                URL url = new URL("http://119.29.3.47:9001/book/worm/isbn?isbn="+isbn);
//                URL url = new URL("http://isbn.szmesoft.com/isbn/query?isbn="+isbn);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //设置连接属性。不喜欢的话直接默认也阔以
                conn.setConnectTimeout(5000);//设置超时
                conn.setUseCaches(false);//数据不多不用缓存了

                //这里连接了
                conn.connect();
                //这里才真正获取到了数据
                InputStream inputStream = conn.getInputStream();
                InputStreamReader input = new InputStreamReader(inputStream);
                BufferedReader buffer = new BufferedReader(input);
                if (conn.getResponseCode() == 200) {//200意味着返回的是"OK"
                    String inputLine;
                    StringBuffer resultData = new StringBuffer();//StringBuffer字符串拼接很快
                    while ((inputLine = buffer.readLine()) != null) {
                        resultData.append(inputLine);
                    }
                    String bookJson = resultData.toString();
                    Log.v("res",bookJson);
            }
        } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

}
}
