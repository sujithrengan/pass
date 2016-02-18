package org.delta.pass;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class ChatList extends Activity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<String> contact;
    ArrayList<String> timestamp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
/*

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SwipeRefreshChatList fragment = new SwipeRefreshChatList();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
*/

        contact=new ArrayList<String>();
        timestamp=new ArrayList<String>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorScheme(R.color.color_scheme_1_1, R.color.color_scheme_1_2,
                R.color.color_scheme_1_3, R.color.color_scheme_1_4);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new ChatListAdapter(contact,timestamp);
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshContent();
            }
        });

        final Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.setPackage("com.google.android.googlequicksearchbox");
        intent.putExtra(SearchManager.QUERY, "Send Whatsapp text to Mom");
        //startActivity(intent);
    }




    private void getChatList()
    {
        File file = new File(Utilities.dbpath+Utilities.dbname_messages);

        if (file.exists()) {

            SQLiteDatabase db = SQLiteDatabase.openDatabase(Utilities.dbpath+Utilities.dbname_messages, null, SQLiteDatabase.OPEN_READWRITE);

            //SELECT
            Cursor cursor = db.rawQuery("Select key_remote_jid,subject,sort_timestamp from chat_list order by sort_timestamp desc", null);
            if (cursor.moveToFirst()) {

                contact.clear();
                timestamp.clear();
                do {
                    String c=cursor.getString(0);
                    if(c.endsWith("s.whatsapp.net"))
                    contact.add(c.substring(2,c.indexOf("@")));
                    else
                        contact.add(cursor.getString(1));
                    timestamp.add(cursor.getString(2));

                } while (cursor.moveToNext());
            }
            /*//UPDATE
            ContentValues values = new ContentValues();
            String where = "jid" + "= '" + jid + "'";
            values.put("status", "BlackoutEpisodes");
            int count =db.update("wa_contacts", values, where, null);
            */




            db.close();

        }

        else{
            Toast.makeText(ChatList.this,"NoRoot",Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshContent(){

        getChatList();
        mAdapter = new ChatListAdapter(contact,timestamp);
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setRefreshing(false);

        }



}
