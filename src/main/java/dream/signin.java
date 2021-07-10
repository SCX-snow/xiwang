package dream;

import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Random;

public class signin {
    public static String qiandao(String sendername, String sender, Connection con)
    {
        if(sender.startsWith("AnonymousMember"))
        {
            return null;
        }
        String id,day;
        int mark;
        ResultSet res;
        PreparedStatement sql;
        id=sender.substring(13,sender.length()-1);
        java.util.Date date = new Date();
        day=String.format("%tY"+"-"+"%tm"+"-"+"%td",date,date,date);

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
            sql=con.prepareStatement("SELECT date FROM sign_in WHERE id = "+id);
            res=sql.executeQuery();
            if(res.next())
            {
                if(res.getString("date").equals(day))
                {
                    sql=con.prepareStatement("select getin from word where form = '签到_重复'");
                    res=sql.executeQuery();
                    res.next();
                    return res.getString("getin");
                }
                else
                {
                    sql=con.prepareStatement("SELECT mark FROM sign_in WHERE id = "+id);
                    res=sql.executeQuery();
                    res.next();
                    Random rand = new Random();
                    mark =rand.nextInt(10) + 1;
                    sql=con.prepareStatement("UPDATE sign_in SET mark = mark+"+mark+" ,date = '"+day+"' WHERE id = "+id);
                    return qiandaochenggong(sendername, con, mark, sql);
                }
            }
            else
            {
                sql=con.prepareStatement("INSERT INTO sign_in (id,mark, date) VALUES ("+id+",0,'"+day+"')");
                sql.execute();
                Random rand = new Random();
                mark =rand.nextInt(10) + 6;
                sql=con.prepareStatement("UPDATE sign_in SET mark = "+mark+" WHERE id = "+id);
                sql.executeUpdate();
                sql=con.prepareStatement("UPDATE sign_in SET date = '"+day+"' WHERE id = "+id);
                return qiandaochenggong(sendername, con, mark, sql);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            con.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    public static String chaxun(String sendername, String sender, Connection con)
    {
        if(sender.startsWith("AnonymousMember"))
        {
            return null;
        }
        String id,temp;
        int mark;
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
            sql=con.prepareStatement("SELECT mark FROM sign_in WHERE id = "+id);
            res=sql.executeQuery();
            if(res.next())
            {
                mark=res.getInt("mark");
                sql=con.prepareStatement("select getin from word where form = '好感度_前'");
                res=sql.executeQuery();
                res.next();
                temp=sendername+res.getString("getin")+mark;
                sql=con.prepareStatement("select getin from word where form = '好感度_后'");
                res=sql.executeQuery();
                res.next();
                return temp+res.getString("getin");
            }
            else
            {
                sql=con.prepareStatement("select getin from  word where form = '好感度_无'");
                res=sql.executeQuery();
                res.next();
                return res.getString("getin");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Nullable
    private static String qiandaochenggong(String sendername, Connection con, int mark, PreparedStatement sql) throws SQLException {
        ResultSet res;
        String out;
        sql.executeUpdate();
        sql=con.prepareStatement("select getin from word where form = '签到_成功_前'");
        res=sql.executeQuery();
        if(res.next())
        {
            out=sendername+res.getString("getin")+mark;
        }
        else
        {
            return null;
        }
        sql=con.prepareStatement("select getin from word where form = '签到_成功_后'");
        res=sql.executeQuery();
        if(res.next())
        {
            return out+res.getString("getin");
        }
        else
        {
            return null;
        }
    }
}
