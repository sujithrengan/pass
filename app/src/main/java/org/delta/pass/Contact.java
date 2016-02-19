package org.delta.pass;

/**
 * Created by HP on 19-02-2016.
 */
public class Contact {

    String jid;
    String name;
    String wa_name;
    String number;

    Contact(String jid,String name,String wa_name)
    {
        this.jid=jid;
        this.name=name;
        this.wa_name=wa_name;
        this.number=jid.substring(0,jid.indexOf("@"));

        if(this.name==null)
            this.name=this.wa_name;
    }

    public String getName(String number)
    {
        if(name!=null)
        return this.name;
        else
            return wa_name;
    }
}
