package dream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class name {
    public static String find(String sendername, String sender, Connection con)
    {
        if(sender.startsWith("AnonymousMember"))
        {
            return null;
        }
        String id;
        ResultSet res;
        PreparedStatement sql;
        id=sender.substring(13,sender.length()-1);
        try {
            sql=con.prepareStatement("SELECT sendername FROM sign_in WHERE id = "+id);
            res= sql.executeQuery();
            if(res.next())
            {
                if(res.getString("sendername")!=null)
                {
                    sendername = res.getString("sendername");
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        lasttime.last();
        return sendername;
    }
    public static String change(String contant, String sender, Connection con)
    {
        if(sender.startsWith("AnonymousMember"))
        {
            return null;
        }
        String id,name;
        ResultSet res;
        PreparedStatement sql;
        id=sender.substring(13,sender.length()-1);
        name=contant.substring(contant.lastIndexOf("以后叫我")+1);
        try {
            sql=con.prepareStatement("select mark from sign_in where id ="+id);
            res=sql.executeQuery();
            if(res.next())
            {
                if(res.getInt("mark")<100)
                {
                    sql=con.prepareStatement("select getin from word where form ='改名_无好感'");
                    res=sql.executeQuery();
                    if(res.next())
                    {
                        lasttime.last();
                        return res.getString("getin");
                    }
                    else
                    {
                        return null;
                    }
                }
                else
                {
                    sql=con.prepareStatement("UPDATE sign_in SET sendername = '"+name+"' WHERE id = "+id);
                    sql.executeUpdate();
                    name=find(null, sender, con);
                    lasttime.last();
                    return "以后就叫你"+name+"了";
                }
            }
            else
            {
                sql=con.prepareStatement("select getin from word where form ='改名_无好感'");
                res=sql.executeQuery();
                if(res.next())
                {
                    lasttime.last();
                    return res.getString("getin");
                }
                else
                {
                    return null;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
