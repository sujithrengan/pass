package org.delta.pass;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;



public class ChatList extends Activity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<String> contact;
    ArrayList<String> timestamp;
    ArrayList<String> jid;


    private class SUTask extends AsyncTask<Void, Boolean, Boolean> {
        private List<String> suResult = null;
        @Override
        protected Boolean doInBackground(Void... params) {
            // this method is executed in a background thread
            // no problem calling su here

            if (Shell.SU.available()) {

                suResult=Shell.SU.run(new String[]{"busybox chmod -c -R 777 "+Utilities.ppicpath});
                //suResult=Shell.SU.run(new String[]{"cat storage/sdcard1/s.txt" });
                return true;

            }
            else
                return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            Log.e("ll",suResult.get(0));

        }
    }

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



        new SUTask().execute();



        contact=new ArrayList<String>();
        timestamp=new ArrayList<String>();
        jid=new ArrayList<String>();
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


        getChatList();
        mAdapter = new ChatListAdapter(contact,timestamp,jid,ChatList.this);


        mRecyclerView.setItemAnimator(new FadeInAnimator());

        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        //        scaleAdapter.setFirstOnly(false);
        //        scaleAdapter.setInterpolator(new OvershootInterpolator());
        mRecyclerView.setAdapter(scaleAdapter);

        //mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshContent();
            }
        });


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
                jid.clear();
                do {
                    String c=cursor.getString(0);
                    if(c.endsWith("s.whatsapp.net"))
                    contact.add(c.substring(2,c.indexOf("@")));
                    else
                        contact.add(cursor.getString(1));
                    timestamp.add(cursor.getString(2));
                    jid.add(cursor.getString(0));

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
        mAdapter = new ChatListAdapter(contact,timestamp,jid,ChatList.this);
        //mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new FadeInAnimator());

        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        //        scaleAdapter.setFirstOnly(false);
        //        scaleAdapter.setInterpolator(new OvershootInterpolator());
        mRecyclerView.setAdapter(scaleAdapter);
        mSwipeRefreshLayout.setRefreshing(false);

        }



}
