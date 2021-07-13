package dream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Random;

public class taro {
    public static String zhanbu(String sendername, String sender, Connection con)
    {
        if(sender.startsWith("AnonymousMember"))
        {
            return null;
        }
        String id,day;
        ResultSet res;
        PreparedStatement sql;
        id=sender.substring(13,sender.length()-1);
        try {
            sql=con.prepareStatement("select mark from sign_in where id ="+id);
            res=sql.executeQuery();
            if(res.next())
            {
                if(res.getInt("mark")<50)
                {
                    sql=con.prepareStatement("select getin from word where form ='占卜_无好感'");
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
            }
            else
            {
                sql=con.prepareStatement("select getin from word where form ='占卜_无好感'");
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
        Date date = new Date();
        day=String.format("%tY"+"-"+"%tm"+"-"+"%td",date,date,date);
        try {
            sendername=name.find(sendername,sender,con);
            sql=con.prepareStatement("SELECT date FROM taro WHERE id = "+id);
            res= sql.executeQuery();
            if(res.next())
            {
                if(res.getString("date").equals(day))
                {
                    sql=con.prepareStatement("select getin from word where form = '占卜_重复'");
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
                    Random rand = new Random();
                    sql=con.prepareStatement("select word from taro_word where id = "+(rand.nextInt(44)+1));
                    res=sql.executeQuery();
                    if(res.next())
                    {
                        sql=con.prepareStatement("UPDATE taro SET date = '"+day+"' WHERE id = "+id);
                        sql.executeUpdate();
                        lasttime.last();
                        return sendername+",我为您抽到了"+res.getString("word");
                    }
                    else
                    {
                        return null;
                    }
                }
            }
            else
            {
                Random rand = new Random();
                sql=con.prepareStatement("select word from taro_word where id = "+(rand.nextInt(44)+1));
                res=sql.executeQuery();
                if(res.next())
                {
                    sql=con.prepareStatement("INSERT INTO taro (id,date) VALUES ("+id+",'"+day+"')");
                    sql.execute();
                    lasttime.last();
                    return sendername+",我为您抽到了"+res.getString("word");
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
