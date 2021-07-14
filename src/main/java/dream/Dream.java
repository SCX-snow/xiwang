package dream;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.PlainText;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

public final class Dream extends JavaPlugin {

    public static final Dream INSTANCE = new Dream();

    private Dream() {
        super(new JvmPluginDescriptionBuilder("dream.Dream", "1.0").build());
    }

    @Override
    public void onLoad(@NotNull PluginComponentStorage $this$onLoad)
    {
        Connection con;
        PreparedStatement sql;
        ResultSet res=null;
        con=dbconnect.connect();
        if(con==null)
        {
            getLogger().error("数据库加载失败");
            onDisable();
        }
        else
        {
            getLogger().info("数据库加载成功");
            dbconnect.disconnect(con);
        }
    }

    @Override
    public void onEnable()
    {
        Listener listener = GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, event -> {
            String content = event.getMessage().contentToString();
            String sendername =event.getSenderName();
            String sender =event.getSender().toString();
            String miraimessage=event.getMessage().serializeToMiraiCode();
            long qq;
            Connection con;
            PreparedStatement sql;
            ResultSet res=null;
            con=dbconnect.connect();
            qq=event.getBot().getId();
            try {
                sql=con.prepareStatement("select form from word where getin = '"+content+"'");
                res=sql.executeQuery();
                if(res.next())
                {
                    switch (res.getString("form")){
                        case "签到":
                            event.getSubject().sendMessage(new PlainText("").plus(new At(event.getSender().getId())).plus(new PlainText(signin.qiandao(sendername,sender,con))));
                            break;
                        case "好感度":
                            event.getSubject().sendMessage(new PlainText("").plus(new At(event.getSender().getId())).plus(new PlainText(signin.chaxun(sendername,sender,con))));
                            break;
                        case "占卜":
                            event.getSubject().sendMessage(new PlainText("").plus(new At(event.getSender().getId())).plus(new PlainText(taro.zhanbu(sendername,sender,con))));
                    }
                }
                else if(miraimessage.contains("[mirai:at:"+qq+"]"))
                {
                    if(content.contains("你叫我什么"))
                    {
                        event.getSubject().sendMessage("我对你的称呼是"+name.find(content,sender,con));
                    }
                    else if(content.contains("以后叫我"))
                    {
                        event.getSubject().sendMessage(new PlainText("").plus(new At(event.getSender().getId())).plus(new PlainText(name.change(sendername,sender,con))));
                    }
                    else if(content.contains("功能"))
                    {
                        event.getSubject().sendMessage(new PlainText("").plus(new At(event.getSender().getId())).plus(new PlainText(info.function(con))));
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            dbconnect.disconnect(con);
        });
    }

    @Override
    public void onDisable()
    { }

}