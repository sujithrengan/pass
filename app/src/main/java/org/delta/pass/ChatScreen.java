package org.delta.pass;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;


public class ChatScreen extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<String> messages;
    ArrayList<String> timestamp;
    ArrayList<String> contact;

    int isgroup=0;


    String jid;

    Handler handler =new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);

            mRecyclerView.setItemAnimator(new FadeInAnimator());

            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
            ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
            //        scaleAdapter.setFirstOnly(false);
            //        scaleAdapter.setInterpolator(new OvershootInterpolator());
            mRecyclerView.setAdapter(scaleAdapter);
            //mRecyclerView.setAdapter(mAdapter);
            mSwipeRefreshLayout.setRefreshing(false);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.setTitle(jid);
        setContentView(R.layout.activity_chat_screen);
        //getActionBar().setTitle(jid);


        Bundle b=getIntent().getExtras();
        jid="";
        jid=b.getString("jid");
        if(jid.contains("g.us"))
            isgroup=1;




        messages=new ArrayList<String>();
        timestamp=new ArrayList<String>();
        contact=new ArrayList<String>();
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




        Thread p=new Thread(new Runnable() {

            @Override
            public void run() {

                refreshContent();
            }
        });


        p.start();


        mAdapter = new ChatAdapter(messages,timestamp,contact,jid,ChatScreen.this);

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

                Thread p=new Thread(new Runnable() {

                    @Override
                    public void run() {

                        refreshContent();
                    }
                });


                p.start();

            }
        });


    }


    private void getChatList()
    {
        File file = new File(Utilities.dbpath+Utilities.dbname_messages);

        if (file.exists()) {

            SQLiteDatabase db = SQLiteDatabase.openDatabase(Utilities.dbpath+Utilities.dbname_messages, null, SQLiteDatabase.OPEN_READWRITE);

            //SELECT
            Cursor cursor = db.rawQuery("Select key_from_me,data,timestamp,remote_resource from messages where key_remote_jid=\""+jid+"\" order by timestamp desc", null);
            if (cursor.moveToFirst()) {

                messages.clear();
                timestamp.clear();
                contact.clear();
                do {
                    Integer k=cursor.getInt(0);
                    if(k==0)
                        messages.add(cursor.getString(1)+"0");
                    else
                        messages.add(cursor.getString(1)+"1");
                    timestamp.add(cursor.getString(2));

                    if(isgroup==1)
                        contact.add(cursor.getString(3));
                    else
                        contact.add(jid);

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
            Toast.makeText(ChatScreen.this, "NoRoot", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshContent(){

        getChatList();
        mAdapter = new ChatAdapter(messages,timestamp,contact,jid,ChatScreen.this);

        handler.sendEmptyMessage(0);


    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = Utilities.prefs.edit();
        editor.putString("lastread",String.valueOf(System.currentTimeMillis()));
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_screen, menu);
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
        if (id == R.id.action_reply) {

            final Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.setPackage("com.google.android.googlequicksearchbox");
            intent.putExtra(SearchManager.QUERY, "Send Whatsapp text heyyy to "+Utilities.contacts.get(jid).name);
            startActivity(intent);
            return true;
        }


        if (id == R.id.action_reply_w) {
            Uri uri = Uri.parse("smsto:" + jid.substring(0,jid.indexOf("@")));
            Intent i = new Intent(Intent.ACTION_SENDTO, uri);
            i.setPackage("com.whatsapp");
            startActivity(Intent.createChooser(i, ""));
        }

        return super.onOptionsItemSelected(item);
    }
}
