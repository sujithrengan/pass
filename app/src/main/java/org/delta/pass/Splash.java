package org.delta.pass;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;


public class Splash extends ActionBarActivity {

    String utext="null";

    Boolean root;


        private class SUTask extends AsyncTask<Void, Boolean, Boolean> {
        private List<String> suResult = null;
        @Override
        protected Boolean doInBackground(Void... params) {
            // this method is executed in a background thread
            // no problem calling su here
            if (Shell.SU.available()) {

                suResult=Shell.SU.run(new String[]{"busybox chmod -c -R 777 "+Utilities.dbpath2});
                //suResult=Shell.SU.run(new String[]{"cat storage/sdcard1/s.txt" });
                return true;

            }
            else
                return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            root=aBoolean;
            setResult(suResult);

        }
    }

    private void setResult(List<String> suResult)
    {
       final TextView tv_root=(TextView)findViewById(R.id.tv_root);
        StringBuilder sb=new StringBuilder();
        sb.append(">");
        if(suResult!=null)
            for(String line:suResult)sb.append(line).append("\n>");
        if(root)
            tv_root.setText("Rooted\n"+sb.toString());
        else
            tv_root.setText("Not Rooted");

        File file = new File(Utilities.dbpath2+Utilities.dbname2);

        if (file.exists()) {
            String jid="919884465829@s.whatsapp.net";
            SQLiteDatabase db = SQLiteDatabase.openDatabase(Utilities.dbpath2+Utilities.dbname2, null, SQLiteDatabase.OPEN_READWRITE);

            //SELECT
            Cursor resultSet = db.rawQuery("Select * from wa_contacts where jid=\""+jid+"\"", null);
            if(resultSet!=null)
            {
                resultSet.moveToFirst();
                tv_root.setText(tv_root.getText() + "\n" + resultSet.getString(5) + "-" + resultSet.getString(0) + "--" + resultSet.getString(7));


                ContentValues values = new ContentValues();
                String where = "jid" + "= '" + jid + "'";
                values.put("display_name", "Gunner");
                values.put("sort_name", "Gunner");
                int count = db.update("wa_contacts", values, where, null);
                Toast.makeText(Splash.this,"xxx"+String.valueOf(count),Toast.LENGTH_SHORT).show();

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
            Toast.makeText(Splash.this,"hhh",Toast.LENGTH_SHORT).show();
        }



    }

    private void ShowDialog()
    {
        AlertDialog.Builder ab=new AlertDialog.Builder(this);
        ab.setMessage("Content Modified!");
        ab.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(this,"Confirmed",Toast.LENGTH_SHORT).show();
            }
        });


        AlertDialog ad=ab.create();
        ad.show();
        Thread p=new Thread(new Runnable() {
            private List<String> suResult = null;
            @Override
            public void run() {
                Log.e("loggg", utext);
                suResult=Shell.SU.run(new String[]{"echo \""+ utext+"\" > "+"storage/sdcard1/s.txt" });

            }

        });
        p.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new SUTask().execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
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


        return super.onOptionsItemSelected(item);
    }
}
