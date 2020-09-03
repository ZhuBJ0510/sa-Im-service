<h2 align="center" style="margin: 30px 0 30px;font-weight: bold;font-size:40px;">sa-Im（sa后台客服版） v1.0.0</h2>

基于sa-admin后台，开发的后台客服

#项目配置
打开需要用到的springboot项目（没有创建springboot项目）
打开pom文件引用
      <!--websocket-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-websocket</artifactId>
            <version>2.0.5.RELEASE</version>
        </dependency>
        <!--Alijson插件-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.7</version>
        </dependency>
        <!-- redis java客户端jar包 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
        </dependency>
在配置文件里配置redis（已配置不需要配置）
    spring:
  # redis配置
  redis:
    # Redis数据库索引（默认为0）
    database: 0
    # Redis服务器地址
    host: 127.0.0.1
    # Redis服务器连接端口
    port: 6379
    # Redis服务器连接密码（默认为空）
    password: 123456
    lettuce:
      pool:
        # 连接池最大连接数
        max-active: 200
        # 连接池最大阻塞等待时间（使用负值表示没有限制）
        max-wait: -1ms
        # 连接池中的最大空闲连接
        max-idle: 10
        # 连接池中的最小空闲连接
        min-idle: 0
    # 连接超时时间（毫秒）
    timeout: 5000ms
  把java文件下的websocket放到项目的启动类同级文件下修改文件引用即可。
  启动
