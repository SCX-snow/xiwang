package dream;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbconnect {

    public static Connection connect()
    {
        Connection con = null;
        String d_name = null,d_user = null,d_password = null,tmp;
        String[] dbin = {"数据库名:","数据库用户名:","数据库用户密码:"};
        File file = new File("dream/dream_db.txt");
        if(file.exists())
        {
            if(file.length()==0)
            {
                if (fileread(dbin, file)) return null;
            }
            else
            {
                try
                {
                    FileReader fr =new FileReader(file);
                    BufferedReader bufr =new BufferedReader(fr);
                    if((tmp= bufr.readLine())!=null)
                    {
                        d_name=tmp.substring(5);
                    }
                    if((tmp= bufr.readLine())!=null)
                    {
                        d_user=tmp.substring(7);
                    }
                    if((tmp= bufr.readLine())!=null)
                    {
                        d_password=tmp.substring(8);
                    }
                    fr.close();
                }
                catch (Exception e)
                {
                    System.out.println("数据库读取失败,请检查后重试");
                }
            }
        }
        else
        {
            try
            {
                file.createNewFile();
                System.out.println("文件创建成功");
            }
            catch(Exception e)
            {
                System.out.println("文件创建失败,请检查后重试");
                return null;
            }
            if (fileread(dbin, file)) return null;
        }
        try
        {
            try
            {
                Class.forName("com.mysql.cj.jdbc.Driver");
            }
            catch (ClassNotFoundException e)
            {
                System.out.println("连接数据库失败,请检查后重试");
            }
            try
            {
                con=DriverManager.getConnection("jdbc:mysql:"+"//127.0.0.1:3306/"+d_name,d_user,d_password);
            }
            catch (SQLException e)
            {
                System.out.println("连接数据库失败,请检查后重试");
            }
        }
        catch (Exception e)
        {
            System.out.println("连接数据库失败,请检查后重试");
            return null;
        }
        return con;
    }

    private static boolean fileread(String[] dbin, File file) {
        try
        {
            FileWriter fw =new FileWriter(file);
            BufferedWriter bufw =new BufferedWriter(fw);
            for (String s : dbin) {
                bufw.write(s);
                bufw.newLine();
            }
            bufw.close();
            fw.close();
        }
        catch (Exception e)
        {
            System.out.println("文件写入失败,请检查后重试");
            return true;
        }
        return false;
    }

    public static void disconnect(Connection con)
    {
        try {
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
