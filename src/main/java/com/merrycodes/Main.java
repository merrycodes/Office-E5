package com.merrycodes;

import com.microsoft.graph.auth.enums.NationalCloud;
import com.microsoft.graph.auth.publicClient.UsernamePasswordProvider;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.models.extensions.Message;
import com.microsoft.graph.models.extensions.User;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.graph.requests.extensions.IMessageCollectionPage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 * Github Action 版本 <br>
 * Office E5 调用 graph API <br>
 * <a href="https://docs.microsoft.com/zh-cn/graph/api/resources/message?view=graph-rest-1.0">关于邮件的操作</a>
 *
 * @author MerryCodes
 * @date 2020/12/19 16:49
 */
public class Main {

    /**
     * 参数一定要正确，否者会调用失败
     *
     * @param args 参数要求查看README.md
     */
    public static void main(String[] args) {
        UsernamePasswordProvider authProvider = new UsernamePasswordProvider(args[0], Collections.singletonList("https://graph.microsoft.com/.default"),
                args[1], args[2], NationalCloud.Global, args[3], args[4]);
        IGraphServiceClient graphClient = GraphServiceClient.builder().authenticationProvider(authProvider).buildClient();
        // 参数错误的时候会报错，而且也获取不了，jar包里面自己处理了(e.printStackTrace())
        // 获取用户
        User user = graphClient.me().buildRequest().get();
        // 查看用户邮件列表
        IMessageCollectionPage iMessageCollectionPage = graphClient.users(user.userPrincipalName).messages().buildRequest().select("sender,subject").get();
        List<Message> messageList = iMessageCollectionPage.getCurrentPage();
        System.out.println(String.format("运行时间：%s —— 共有%d封件邮件", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), messageList.size()));
    }

}
