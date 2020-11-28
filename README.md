[TOC]

# 从零开始在OpenShift上开发和部署Spring Boot应用



## 前言

本文描述了从零开始开发测试Spring Boot应用，并在OpenShift测试环境上部署Spring Boot应用。

其中，在开发环境上使用H2内存数据库，在OpenShift测试环境上使用MySQL数据库。



## 工具和环境

示例代码：

* <https://github.com/cookcodeblog/spring-db-demo>

  

版本：

* Spring Boot 2.4.0
* OpenJDK 8
* JUnit 4.12



本地环境：

* IntellijIDEA Ultimate 2019.3

* Mac OS

* H2 Database

  

测试环境：

* OpenShift 4.6
* MySQL 8



## 本地开发和测试Spring Boot应用项目

### 创建Spring Boot应用项目



在Intellij上，打开New project / Spring Initialzr。

输入信息：

```txt
Group: cn.xdevops.spring

Artifactid: spring-db-demo

Java Version: 8

Name: spring-db-demo

Description: Spring Boot application demo for DB access

Package: cn.xdevops.spring

Dependencies:
Developer Tools / Lombok
Web / Spring Web
SQL / Spring Data JPA
SQL / H2 Database
SQL / MySQL Driver
```



### 本地开发环境配置



在`src/main/resources`目录下配置应用配置文件，用yaml方式更简洁一点。



删除`application.properties`，新建`application.yaml`：

```yaml
spring:
  profiles:
    active: local
  jpa:
    hibernate:
      ddl-auto: none
```



说明：

* 禁用`ddl-auto`功能，不允许程序自动修改表结构或执行SQL。
* 默认激活的profile为`local`。



新建`application-local.yaml`，本地开发连接H2内存数据库：

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:localdb;Mode=MySQL;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password:
    driver-class-name: org.h2.Driver
  h2:
    console:
      enabled: true # http://localhost:8080/h2-console
      settings:
        web-allow-others: false # disable remote access
        trace: false # disable tracing output
  jpa:
    show-sql: true
```



说明：

* 启用H2数据库的Web控制台，但是不允许远程连接（只能本机访问）
* `show-sql: true`  开启在后台日志中打印SQL，方便调试。



本地测试：

```bash
mvn clean install
mvn spring-boot:run
```



查看启动日志：

```txt
The following profiles are active: local
```



在浏览器中访问<http://localhost:8080>



### 访问本地H2数据库



在浏览器中访问H2数据库的控制台：<http://localhost:8080/h2-console>

输入`application-local.yaml` 中的H2数据库URL:

```txt
jdbc:h2:mem:localdb;Mode=MySQL;DB_CLOSE_ON_EXIT=FALSE
```



连接H2数据库。



### 创建表和初始化数据



数据库初始化脚本放在`sql/init_data.sql`:

```sql
-- Create table
drop table if exists fruit;
create table fruit (
  id BIGINT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

-- Init data
insert into fruit(name) values ('Cherry');
insert into fruit(name) values ('Apple');
insert into fruit(name) values ('Banana');
```



通过H2 Web控制台执行数据库初始化脚本，因为是用H2数据库的内存模式，因此每次应用重启后需要重新执行初始化脚本。也可以将H2数据库设置为文件模式，类似：

```
jdbc:h2:file:~/h2data/localdb;Mode=MySQL;DB_CLOSE_ON_EXIT=FALSE
```



### 编写代码

参见[spring-db-demo](<https://github.com/cookcodeblog/spring-db-demo>) 编写Entity、Repository和Controller。



### 测试应用

用Postman或Intellj的Restfultoolkit插件测试。



**Get all fruits**

GET

http://localhost:8080/api/fruits



**Add a fruit**

POST

http://localhost:8080/api/fruits

```json
{
  "name": "Grape"
}
```



**Get a fruit by id**

GET

http://localhost:8080/api/fruits/1



**Update a fruit**

http://localhost:8080/api/fruits/4

PUT

```json
{
  "name": "Green Grape"
}
```



**Delete a fruit**

DELETE

http://localhost:8080/api/fruits/4



## 准备在测试环境上部署Spring Boot应用

### 测试环境配置



在`src / main /resources` 下新建`application-test.yaml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://${DB_HOST}:${DB_PORT:3306}/${DB_NAME}?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    show-sql: true

```



在应用运行时需要从外部注入以下环境变量:

* `DB_HOST` 数据库地址
* `DB_PORT`数据库端口，默认为3306
* `DB_NAME`数据库名称
* `DB_USERNAME` 数据库用户名
* `DB_PASSWORD` 数据库密码



## 创建GitHub仓库并推送到GitHub

在GitHub上创建同名的`spring-db-demo`项目。

设置本地项目Git repo：

```bash
git init .
git status
git add .
git commit -m "Initial commit"
```



将项目推送到GitHub：

```bash
git remote add origin https://github.com/cookcodeblog/spring-db-demo.git
git branch -M main
git push -u origin main
```



## 在OpenShfit测试环境上部署应用



### 在OpenShift上创建项目

在OpenShift上，打开Admin / Home / Projects, Create Project。

```bash
Name: william-spring-demo
Display name: Spring Demo
```



或者用命令行方式：

```bash
oc new-project william-spring-demo --display-name="Spring Demo"
```





### 在OpenShift项目下安装MySQL数据库

在刚创建的`William-spring-demo`项目下安装MySQL数据库。

打开Develper / Add / Database。

这里只是为了演示，选择"MySQL (Ephemeral)"，描述为“MySQL database service, without persistent storage. ”，使用临时卷，在MySQL pod停止后数据丢失。

如果是真正的测试环境或生产环境，要选择“MySQL”， 描述为“MySQL database service, with persistent storage. ”。



配置数据库属性：

* Database Service name: `mysql`

* MySQL Connection Username: `william`

* MySQL Connection Password: `test123`

* MySQL root user Password: `admin123`

* MySQL Database Name: `testdb`

  

选择数据库版本为：`8.0-el7`。



在Typology中查看MySQL的部署情况和MySQL pod的日志，通常需要一段时间MySQL才能部署完成和正常运行。



### 查看MySQL数据库的属性



打开Developer / Secret。

选择Name，输入mysql查找

有两个以`mysql`开头的secret。

* 名为`mysql`的secret只有数据库名、用户名、密码和root用户密码。
* 名为`mysql-xxx-parameters-xxx`的secret有数据库的全部secret。



点击Reveal Values可以看到secret的内容。



### 创建数据库表和初始化数据

实际开发中可以通过自动化数据库变更工具（比如Liquidbase或Flyway）来自动化变更数据库。

这里只是为了演示，所以直接在MySQL数据库上执行SQL脚本来创建数据库表和初始化数据。

打开MySQL Pod的Teminal：

```bash
# 登录数据库
mysql -uwilliam -ptest123 testdb

# 复制SQL的内容到Terminal中运行

# 查看数据
select * from fruit;
```



如果要执行整个SQL文件，可以在`oc login`后运行脚本：

```bash
bash ./scripts/exec_sql_in_mysql.sh william test123 testdb ./sql/init_data.sql
```



脚本exec_sql_in_mysql.sh会复制本地SQL文件到MySQL pod中，并连接MySQL来执行SQL文件。



### 在OpenShift项目下创建应用

在刚创建的`William-spring-demo`项目下创建应用。

打开Develper / Add / From Catalog。

选择 Languages / Java。

输入jdk查找。

选择"Red Hat OpenJDK"，模板描述为“Build and run Java applications using Maven and OpenJDK 11.” （注意不是"OpenJDK"模板，这个的描述是“An example Java application using OpenJDK. ”）。

点“Create Application”继续。

选择Builder Image Version为 `openjdk-8-el7`，表示“Build and run Java applications using Maven and OpenJDK 8.”。

输入Git Repo URL为：`https://github.com/cookcodeblog/spring-db-demo.git`

输入Application Name为：`spring-db-demo-app`

输入Name为：`spring-db-demo`。

选择Resources为`DeploymentConfig`。

默认勾选“Create a route to the application"，对集群外部提供访问。

点击Create，创建应用。



创建应用时会同时创建：

- A `BuildConfig` to build source from a Git repository.
- An `ImageStream` to track built images.
- A `DeploymentConfig` to rollout new revisions when the image changes.
- A `Service` to expose your workload inside the cluster.
- An optional `Route` to expose your workload outside the cluster.



### 查看构建

打开BuildConfig，查看Build构建的日志。



### 配置应用连接上MySQL数据库

配置DeploymentConfig的环境变量。

点击Add More，增加`SPRING_PROFILES_ACTIVE`的环境变量，使用`test` profile的测试环境配置。Spring Boot应用在启动时会使用`application-test.yaml`配置。

```properties
SPRING_PROFILES_ACTIVE=test
```



点击Add from Config Map or Secret，增加MySQL数据库的配置属性的环境变量。

选择Secrets为`mysql-xxx-parameters-xxx`。

```properties
DB_HOST=DATABASE_SERVICE_NAME
DB_NAME=MYSQL_DATABASE
DB_USERNAME=MYSQL_USER
DB_PASSWORD=MYSQL_PASSWORD
```



说明：

* DB_XXX 环境变量定义在application-test.yaml中。



保存修改。

修改DeploymentConfig配置后，会自动触发重新部署应用。

查看应用Pod日志看到：

```txt
The following profiles are active: test
```

说明应用已经使用`test`profile。



访问应用：

在Typology中，点击DeploymentConfig的OpenURL来在浏览器中访问应用（打开的是应用的Router URL）。



### 测试应用

参照上面的步骤，用Postman来测试应用，只是将`localhost:8080`替换为Router的URL。





## 小结



本文描述了从零开始开发测试Spring Boot应用的过程，并借助OpenShift Java S2I builder方便地将应用部署到OpenShift测试环境中的过程。

对Spring Boot应用健康检查、CI/CD等内容将在后续的[博客](https://cookcode.blog.csdn.net/)文章中再介绍。



## 参考文档

**Intellj：**

* [使用IDEA Community创建Spring Boot项目](https://blog.csdn.net/nklinsirui/article/details/104461633)
* [好用的Intellij IDEA插件](https://blog.csdn.net/nklinsirui/article/details/104943834)



**H2 Database:**

* [在Spring Boot使用H2内存数据库](https://cloud.tencent.com/developer/article/1657709)
* [Spring Boot With H2 Database](https://www.baeldung.com/spring-boot-h2-database)



**Spring:**

* [Springboot（二十）启动时数据库初始化spring.datasource/spring.jpa](https://blog.csdn.net/u012326462/article/details/82080812)



**OpenShfit:**

* [Spring Boot Database Access on RHOAR](https://learn.openshift.com/middleware/courses/middleware-spring-boot/spring-db-access)


























