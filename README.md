# activiti7

# 1.Activiti01
## 1.1 SpringBoot 整合Activiti(version:7.1.0.M6)

### (1) pom.xml配置相关依赖

```
<properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <maven.compiler.source>${java.version}</maven.compiler.source>
        <maven.compiler.target>${java.version}</maven.compiler.target>
    </properties>
    <!--activiti7.1.0.M6-->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.activiti.dependencies</groupId>
                <artifactId>activiti-dependencies</artifactId>
                <version>7.1.0.M6</version>
                <scope>import</scope>
                <type>pom</type>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.activiti</groupId>
            <artifactId>activiti-engine</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.18</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.17</version>
        </dependency>
    </dependencies>
```

### (2) 配置activiti数据源信息(ps:使用的是Mysql数据库)

```
spring.application.name=activiti01
server.port=8082

#activiti 数据源
spring.datasource.activiti.driver=com.mysql.jdbc.Driver
spring.datasource.activiti.jdbc-url=jdbc:mysql://localhost:3306/activiti7?useUnicode=true&characterEncoding=utf8
spring.datasource.activiti.username=root
spring.datasource.activiti.password=root
spring.datasource.activiti.driverClassName=com.mysql.jdbc.Driver
```

### (3) activiti配置类编写

```
package com.pjcft.config;/*
 *@author: PJC
 *@time: 2020/2/13
 *@description: null
 */

import com.alibaba.druid.pool.DruidDataSource;
import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.history.HistoryLevel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class ActivitiDataSourceConfig {

    /**
     * activiti数据源
     * @return
     */
    @Bean()
    @ConfigurationProperties(prefix = "spring.datasource.activiti")
    public DataSource activitiDataSource(){
        return DataSourceBuilder.create().build();
    }

    /**
     * 工作流引擎相关配置
     * @param dataSource
     * @return
     */
    @Bean
    public ProcessEngineConfiguration processEngineConfiguration(@Qualifier("activitiDataSource") DataSource dataSource){
        StandaloneProcessEngineConfiguration standaloneProcessEngineConfiguration = new StandaloneProcessEngineConfiguration();
        standaloneProcessEngineConfiguration.setDataSource(dataSource);
        //自动更新表结构，自动建表
        standaloneProcessEngineConfiguration.setDatabaseSchemaUpdate("true");
        //保存历史数据级别设置为full最高级别
        standaloneProcessEngineConfiguration.setHistoryLevel(HistoryLevel.FULL);
        //检查历史表是否存在
        standaloneProcessEngineConfiguration.setDbHistoryUsed(true);
        return standaloneProcessEngineConfiguration;
    }

    /**
     * 工作流流程引擎
     * @param processEngineConfiguration
     * @return
     */

    @Bean
    public ProcessEngine processEngine(ProcessEngineConfiguration processEngineConfiguration){
        return processEngineConfiguration.buildProcessEngine();
    }

    /**
     * RuntimeService
     * @param processEngine
     * @return
     */

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine){
        return processEngine.getRuntimeService();
    }

    /**
     * TaskService
     * @param processEngine
     * @return
     */

    @Bean
    public TaskService taskService(ProcessEngine processEngine){
        return processEngine.getTaskService();
    }

    /**
     * RepositoryService
     * @param processEngine
     * @return
     */
    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine){
        return processEngine.getRepositoryService();
    }

    /**
     * HistoryService
     * @param processEngine
     * @return
     */
    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    /**
     * ManagementService
     * @param processEngine
     * @return
     */
    @Bean
    public ManagementService managementService(ProcessEngine processEngine){
        return processEngine.getManagementService();
    }

    /**
     * DynamicBpmnService
     * @param processEngine
     * @return
     */
    @Bean
    public DynamicBpmnService dynamicBpmnService(ProcessEngine processEngine){
        return processEngine.getDynamicBpmnService();
    }
}

```
### (4) 项目启动及效果


## 1.2 SpringBoot 基于Activiti进行多数据源配置

### (1) 配置数据源配置信息
        在pom文件中添加如下数据源信息
```
#默认数据源配置
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/demo_book?useUnicode=true&characterEncoding=utf8
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
```

### (2) 多数据源配置类编写
```
package com.pjcft.config;/*
 *@author: PJC
 *@time: 2020/2/15
 *@description: null
 */

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@MapperScan(basePackages = {"com.pjcft.mapper.mysql"},sqlSessionTemplateRef = "mysqlSqlSessionTemplate")
public class MysqlDataSourceConfig {

    @Bean("mysqlDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource mysqlDataSource(){
        return new DruidDataSource();
    }

    @Bean
    @Primary
    public SqlSessionFactory mysqlSqlSessionFactory(@Qualifier("mysqlDataSource") DataSource dataSource){
        SqlSessionFactoryBean defaultSessionFactoryBean = new SqlSessionFactoryBean();
        defaultSessionFactoryBean.setDataSource(dataSource);
        //添加xml目录
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

        try {
            defaultSessionFactoryBean.setMapperLocations(resourcePatternResolver.getResources("classpath*:mapper/mysql/*.xml"));
            return defaultSessionFactoryBean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Bean
    @Primary
    public SqlSessionTemplate mysqlSqlSessionTemplate(@Qualifier("mysqlSqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory);
        return template;
    }
}

```

### (3) 项目启动

## 1.3 SpringBoot 整合Mybatis

### (1) pom.xml文件添加相关依赖
```
<dependency>
  <groupId>org.mybatis.spring.boot</groupId>
  <artifactId>mybatis-spring-boot-starter</artifactId>
  <version>1.3.1</version>
</dependency>
```
### (2) model层
```
package com.pjcft.model;/*
 *@author: PJC
 *@time: 2020/2/15
 *@description: null
 */

public class Book {
    private String bookId;

    private String bookName;

    private String bookAuthorId;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthorId() {
        return bookAuthorId;
    }

    public void setBookAuthorId(String bookAuthorId) {
        this.bookAuthorId = bookAuthorId;
    }
}
```
### (3) Dao层
```
package com.pjcft.mapper.mysql;/*
 *@author: PJC
 *@time: 2020/2/15
 *@description: null
 */

import com.pjcft.model.Book;

import java.util.List;

public interface BookMapper {

    public List<Book> selectAllBook();
}
```
### (4) Mapper层
```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.pjcft.mapper.mysql.BookMapper" >
    <resultMap id="BaseResultMap" type="com.pjcft.model.Book" >
        <id column="book_id" property="bookId" jdbcType="VARCHAR" />
        <result column="book_name" property="bookName" jdbcType="VARCHAR" />
        <result column="book_author_id" property="bookAuthorId" jdbcType="VARCHAR" />
    </resultMap>

    <select id="selectAllBook" resultMap="BaseResultMap">
      SELECT * FROM Book
    </select>

</mapper>
```

### (5) Service层
```
package com.pjcft.service;/*
 *@author: PJC
 *@time: 2020/2/15
 *@description: null
 */

import com.pjcft.model.Book;

import java.util.List;

public interface BookService{

    public List<Book> selectAllBook();
}
```
### (6) ServiceImpl层
```
package com.pjcft.service.impl;/*
 *@author: PJC
 *@time: 2020/2/15
 *@description: null
 */

import com.pjcft.mapper.mysql.BookMapper;
import com.pjcft.model.Book;
import com.pjcft.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService{

    @Autowired
    private BookMapper bookMapper;


    @Override
    public List<Book> selectAllBook() {
        return bookMapper.selectAllBook();
    }
}
```

### (7) Controller层
```
package com.pjcft.controller;/*
 *@author: PJC
 *@time: 2020/2/15
 *@description: null
 */


import com.pjcft.model.Book;
import com.pjcft.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public List<Book> getBooks(){
        List<Book> books = bookService.selectAllBook();
        System.out.println(books.toString());
        return books;
    }
}
```

### (8) 启动类包扫描
在Application.java的启动类加入包扫描路径
```
@SpringBootApplication(scanBasePackages = "com.pjcft")
```
### (9) 项目启动及效果
