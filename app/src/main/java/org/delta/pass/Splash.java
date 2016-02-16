package org.delta.pass;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;


public class Splash extends ActionBarActivity {


    Boolean root;
    private boolean canExecuteSuCommand()
    {
        boolean i;
        try
        {
            Runtime.getRuntime().exec("su");

            return true;
        }
        catch (IOException localIOException)
        {
            return false;
        }
    }
    private boolean hasSuperuserApk()
    {
        return new File("/system/app/Superuser.apk").exists();
    }

    private boolean isTestKeyBuild()
    {
        String str = Build.TAGS;
        if ((str != null) && (str.contains("test-keys")))
            return true;
        else return false;

    }

    private class SUTask extends AsyncTask<Void, Boolean, Boolean> {
        private List<String> suResult = null;
        @Override
        protected Boolean doInBackground(Void... params) {
            // this method is executed in a background thread
            // no problem calling su here
            if (Shell.SU.available()) {

                suResult=Shell.SU.run(new String[]{"busybox chmod -c -R 777 "+Utilities.dbpath});
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
        TextView tv_root=(TextView)findViewById(R.id.tv_root);
        StringBuilder sb=new StringBuilder();
        if(suResult!=null)
            for(String line:suResult)sb.append(line).append("\n");
        if(root)
            tv_root.setText("Rooted\n"+sb.toString());
        else
            tv_root.setText("Not Rooted");

        File file = new File("/data/data/com.delta.pragyan16/databases/pragyan.db");
        if (file.exists()) {
            SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.delta.pragyan16/databases/pragyan.db", null, SQLiteDatabase.OPEN_READWRITE);
            Cursor resultSet = db.rawQuery("Select * from events", null);
            resultSet.moveToFirst();
            tv_root.setText(resultSet.toString());
            db.close();
        }
        else{
            Toast.makeText(Splash.this,"hhh",Toast.LENGTH_SHORT).show();
        }

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
