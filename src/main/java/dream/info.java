package dream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class info {
    public static String function(Connection con)
    {
        ResultSet res;
        PreparedStatement sql;
        StringBuilder putout= new StringBuilder();
        putout.append("\n");
        try {
            sql=con.prepareStatement("select * from info");
            res=sql.executeQuery();
            while(res.next())
            {
                putout.append(res.getString("id")).append(".").append(res.getString("form")).append("   :").append(res.getString("info")).append("\n");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return putout.toString();
    }
}
