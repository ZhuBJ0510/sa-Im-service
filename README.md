<h2 align="center" style="margin: 30px 0 30px;font-weight: bold;font-size:40px;">sa-Im（sa后台客服版） v1.0.0</h2>

基于sa-admin后台，开发的后台客服

# 项目配置

> ## 后台配置
> 打开需要用到的springboot项目（没有创建springboot项目）<br />
> 打开pom文件引用
> ```
>        <!--websocket-->
>        <dependency>
>            <groupId>org.springframework.boot</groupId>
>            <artifactId>spring-boot-starter-websocket</artifactId>
>            <version>2.0.5.RELEASE</version>
>        </dependency>
>        <!--Alijson插件-->
>        <dependency>
>            <groupId>com.alibaba</groupId>
>            <artifactId>fastjson</artifactId>
>            <version>1.2.7</version>
>        </dependency>
>        <!-- redis java客户端jar包 -->
>        <dependency>
>            <groupId>org.springframework.boot</groupId>
>            <artifactId>spring-boot-starter-data-redis</artifactId>
>        </dependency>
>        <dependency>
>           <groupId>org.apache.commons</groupId>
>            <artifactId>commons-pool2</artifactId>
>       </dependency>
>```
> 在配置文件里配置redis（已配置不需要配置）
>``` 
>  spring:
>      # redis配置
>      redis:
>        # Redis数据库索引（默认为0）
>        database: 0
>        # Redis服务器地址
>        host: 127.0.0.1
>        # Redis服务器连接端口
>        port: 6379
>        # Redis服务器连接密码（默认为空）
>        password: 123456
>        lettuce:
>          pool:
>            # 连接池最大连接数
>            max-active: 200
>            # 连接池最大阻塞等待时间（使用负值表示没有限制）
>            max-wait: -1ms
>            # 连接池中的最大空闲连接
>            max-idle: 10
>            # 连接池中的最小空闲连接
>            min-idle: 0
>        # 连接超时时间（毫秒）
>        timeout: 5000ms
>```
>  把java文件下的websocket放到项目的启动类同级文件下修改文件引用即可。<br />
>  启动

---

> ## sa-admin配置 (后台客服端) 
> 把文件分别放到相对应的文件夹加下<br />
> 在index.html 文件 引用
>```
><script src="static/im/sa_IM.js"></script>
>```
> 在 static/im/sa_IM.js 修改连接地址<br />
> ### 在sa.js的window.sa_admin = xxx下面添加
>```
> window.sa_IM = window.sa_IM || parent.sa_IM || top.sa_IM;
>```
> ### 在sa-code.js 调用 
>```
>sa_IM.login("xxx");(<xxx> 为后台id) 
>```
>来连接后台websocket
>在menu-list.js 里添加页面
>```
>{
>	id: '10', // id可能根据个人需求进行修改
>	name: '客服聊天',
>	icon: 'el-icon-document-copy',
>	info: '在后台与用户聊天',
>	url:'sa-html/IM/im.html'
>},
>```
> 运行index.html即可

---

> # uniapp (用户端)
> [连接](https://github.com/ZhuBJ0510/sa-Im-uniapp)

# 请求
> 连接： ws://ip:端口/websocket/{uid}/{type} <br />
> ## 发送消息
>> ### 客服
>> ```
>> {"type":"sendText","fId":"发给人id","text":"发送内容","genre":"text/文本，img/图片，mp3/音频"}
>>```
>
>>### 用户
>>```
>> {"type":"sendText","text":"发送内容1","genre":"text"}
>>```
>## 进入聊天室
>> ### 客服
>> ```
>> {type: "joinChat", fId: "用户id", pageNo: 1, pageSize: 10} pageNo从1开始
>>```
>
>>### 用户
>>```
>> {"type":"joinChat","pageNo":"当前页","pageSize":"页大小"}  
>>```
>## 客服加载聊天列表
>> ### 客服
>> ```
>> {type: "chatList"}
>>```
>## 用户加载未读消息
>> ### 用户
>> unread 为未读消息数量
>> ```
>> {type: "unread"}
>>```
## QQ群
QQ交流群：[782974737 点击加入](https://jq.qq.com/?_wv=1027&k=5DHN5Ib)
