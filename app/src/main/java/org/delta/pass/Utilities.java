package org.delta.pass;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by HP on 16-02-2016.
 */
public class Utilities {
    public static String dbpath2="/data/data/com.whatsapp/databases/";
    public static String dbname2="wa.db";
    public static String dbpath="/data/data/com.whatsapp/databases/";
    public static String ppicpath="/storage/emulated/0/WhatsApp/";
    public static String dbname_contacts="wa.db";
    public static String dbname_messages="msgstore.db";
    public static HashMap<String,Contact> contacts=new HashMap<String,Contact>();
    public static HashMap<String,String[]> media=new HashMap<String,String[]>();

    public static SharedPreferences prefs;
    public static String lastread;

}
