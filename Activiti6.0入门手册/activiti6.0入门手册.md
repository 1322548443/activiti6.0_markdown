---
typora-root-url: assets
typora-copy-images-to: assets
---

## 一、Activiti介绍

   Activiti是由Alfresco软件在2010年5月17日发布的业务流程管理(BPM)框架,它是覆盖了业务流程管理，工作流，服务协作等领域的一个开源，灵活的，易扩展的可执行流程语言框架。

### 1. activiti的七大接口

流程引擎的API和服务

引擎API是与Activiti打交道的最常用方式。 我们从`ProcessEngine`开始， 创建它有多种方法 ，从ProcessEngine中，你可以获得很多囊括工作流/BPM方法的服务。 ProcessEngine和服务类都是线程安全的。 你可以在整个服务器中仅保持它们的一个引用就可以了。

![img](api.services.png)

**RepositoryService**：提供一系列管理流程部署和流程定义的API。
**RuntimeService**：在流程运行时对流程实例进行管理与控制。
**TaskService**：对流程任务进行管理，例如任务提醒、任务完成和创建任务等。
**IdentityService**：提供对流程角色数据进行管理的API，这些角色数据包括用户组、用户及它们之间的关系。
**ManagementService**：提供对流程引擎进行管理和维护的服务。
**HistoryService**：对流程的历史数据进行操作，包括查询、删除这些历史数据。
**FormService**：表单服务。

### 2. activiti的数据库表

Activiti的表都以**ACT_**开头。 第二部分是表示表的用途的两个字母标识。 用途也和服务的API对应。Activiti6.0在初始化的时候默认为我们创建了28张表，相比于 Activiti5.x 版本多了一些对数据库操作记录的日志表，这里对一些关键的表做了说明。

#### 2.1 资源库流程规则表 

ACT_RE_*: 'RE'表示`repository`。 这个前缀的表包含了流程定义和流程静态资源 （图片，规则，等等）。
1) act_re_deployment 部署信息表 
2) act_re_model 流程设计模型部署表 
3) act_re_procdef 流程定义数据表 

#### 2.2 运行时数据库表 

 ACT_RU_\*: 'RU'表示`runtime`。 这些运行时的表，包含流程实例，任务，变量，异步任务，等运行中的数据。
1) act_ru_execution 运行时流程执行实例表 
2) act_ru_identitylink 运行时流程人员表，主要存储任务节点与参与者的相关信息 
3) act_ru_task 运行时任务节点表 
4) act_ru_variable 运行时流程变量数据表 
注： Activiti只在流程实例执行过程中保存这些数据， 在流程结束时就会删除这些记录。 这样运行时表可以一直很小速度很快。

####  2.3 历史数据库表 

 ACT_HI_\*: 'HI'表示`history`。 这些表包含历史数据，比如历史流程实例， 变量，任务等等。
1) act_hi_actinst 历史节点表 
2) act_hi_attachment 历史附件表 
3) act_ih_comment 历史意见表 
4) act_hi_identitylink 历史流程人员表 
5) act_hi_detail 历史详情表，提供历史变量的查询 
6) act_hi_procinst 历史流程实例表 
7) act_hi_taskinst 历史任务实例表 
8) act_hi_varinst 历史变量表

#### 2.4 组织机构表 

 ACT_ID_\*: 'ID'表示`identity`。 这些表包含身份信息，比如用户，组等等。
1) act_id_group 用户组信息表 
2) act_id_info 用户扩展信息表 
3) act_id_membership 用户与用户组对应信息表 
4) act_id_user 用户信息表 
这四张表很常见，基本的组织机构管理，关于用户认证方面建议还是自己开发一套，组件自带的功能太简单，使用中有很多需求难以满足 

#### 2.5 通用数据表 

1) act_ge_bytearray 二进制数据表 
2) act_ge_property 属性数据表存储整个流程引擎级别的数据,初始化表结构时，会默认插入三条记录





### 3. activiti.cfg.xml

Activiti核心配置文件，配置流程引擎创建工具的基本参数和数据库连接池参数。 
定义数据库配置参数： 
 jdbcUrl: 数据库的JDBC URL。 
 jdbcDriver: 对应不同数据库类型的驱动。 
 jdbcUsername: 连接数据库的用户名。 
 jdbcPassword: 连接数据库的密码。 
基于JDBC参数配置的数据库连接 会使用默认的MyBatis连接池。 下面的参数可以用来配置连接池（来自MyBatis参数）： 
 jdbcMaxActiveConnections: 连接池中处于被使用状态的连接的最大值。默认为10。 
 jdbcMaxIdleConnections: 连接池中处于空闲状态的连接的最大值。 
 jdbcMaxCheckoutTime: 连接被取出使用的最长时间，超过时间会被强制回收。 默认为20000（20秒）。 
 jdbcMaxWaitTime: 这是一个底层配置，让连接池可以在长时间无法获得连接时， 打印一条日志，并重新尝试获取一个连接。（避免因为错误配置导致沉默的操作失败）。 默认为20000（20秒）。



```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!-- 配置 ProcessEngineConfiguration  -->
    <bean id="processEngineConfiguration" class="org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
      
        <!-- 配置数据库连接 -->
        <property name="jdbcDriver" value="com.mysql.jdbc.Driver"></property>
        <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/activiti6?characterEncoding=utf8"></property>
        <property name="jdbcUsername" value="root"></property>
        <property name="jdbcPassword" value="1234"></property>
        <!-- 配置创建表策略 :没有表时，自动创建,默认为false-->
        <property name="databaseSchemaUpdate" value="true"></property>
    </bean>
</beans>
```



##  二、Activiti安装以及运行

### 1. 下载和安装 activiti

必要环境：JDK 1.7+，Tomcat 8，MySQL 5.6，关于这些环境的安装这里就不再赘述了

下载activiti6.0.zip，解压文件，并将文件夹 wars下的3个war包复制到tomcat的webapps下

链接：https://pan.baidu.com/s/1hQuVPk5M-IE0BfRVviYk7g    提取码：iosq 

注意：Activiti默认使用H2内存数据库，为了数据的持久性，我们可以修改数据库的类型，要修改的文件位置：

Tomcat 8.5\webapps\activiti-app\WEB-INF\classes\META-INF\activiti-app\activiti-app.properties

![img](1552526662.jpg)

<center>图<strong> 2-1</strong></center>

这里修改为mysql数据库，箭头所示为自定义数据库名称

  启动tomcat访问  http://localhost:8080/activiti-app/，默认登陆名admin ，密码为  test



Kickstart App：主要用于流程模型管理，表单管理以及App管理，一个App可以包含多个流程模型

Task App：用于管理整个Activiti-app的任务，也可以在里面启动流程

Identity management：用于管理用户，用户组等信息

------

### 2. 运行activiti官方示例

 这里通过一个简单的请假流程来介绍一下Activiti：

#### 2.1 创建用户 

- 单击Identity management，切换到Users
- 点击Create User开始创建用户，新建用户名为employee1和manager1的对象

![1552534805892](/1552534805892.png)

<center>图<strong> 2-2</strong></center>

**注意：虽然邮箱不是必填项，但最好填上，否则可能会在登录的时候出现问题。**



#### 2.2 创建流程定义

- 点击左上角Activiti图标回到主菜单
- 点击Kickstart App，选择create Process创建流程，name和key都填上demo
- 拖动左侧图标创建流程图

![img](1552529242.jpg)

<center>图<strong> 2-3</strong></center>

​	在图2-3中我们定义了一个开始事件，两个用户任务，一个结束事件，我们需要将任务分配给某个具体的用户，点击第一个用户任务，并修改`Assignments`属性，将第一个任务分配给`employee1`，保存。再用同样的方法将第二个任务分配给`manager1`。这时一个简单的流程模型已经定义完毕，点击保存并退出编辑，我们就可以开始发布流程。

![img](1552532929.jpg)

<center>图<strong> 2-4</strong></center>

![1552874276847](/1552874276847.png)

<center>图<strong> 2-5</strong></center>

保存后再次点击demo流程，此处可以下载该流程文件。文件名为 xxxx.bpmn20.xml。



#### 2.3 发布流程

​	在activiti中一个App可以包含多个流程，所以在流程发布前需要新建一个App并为其设置模型，点击上方导航栏的Apps，单击Create App新建一个App，如图2-5所示。

![img](1552533996.jpg)

<center>图<strong> 2-6</strong></center>

​	新建App后在当前页面单击 `Edit included models` 添加刚刚创建的请假流程，单击保存按钮，勾选public退出编辑。这个时候在主页面上就可以看到我们新建的HR App了。

至此发布流程结束。

![img](1552534347.jpg)

<center>图<strong> 2-7</strong></center>



#### 2.4 启动与完成流程

​	点击右上角的Administrator，单击Sign out，用创建的employee账号登陆（用User id登陆），在主界面我们可以看到我们新建的HR App，单击进去，切换到Process，点击左上角的 `+ Start a process` 按钮，可以看到流程列表里有一个demo流程。点击右侧的start process 启动流程。这时我们的Tasks任务列表中就有一项代办任务，点击右上角的Claim就完成了对任务的签收：

![1552537601637](/1552537601637.png)

<center>图<strong> 2-8</strong></center>

​	完成后点击Complete提交任务。这个时候 employee的任务列表中就没有这项任务了，而我们切换manager账号登陆后就会发现manager1的任务列表中多了一项Manager audit待签收任务，这里可以在Comments处添加评论。点击Complete后流程走向结束节点，至此走完了整个流程。Processes列表中也自动将此次流程移除。

![1552538413166](/1552538413166.png)

<center>图<strong> 2-9</strong></center>

### 3.查看流程记录

​	除了 activiti -app 这个 war 包外，还有一个 activiti-admin war 包，在之前部署时也放到 Tomcat的应用目录下。 activiti-admin 用于查看流程引擎的主要数据，包括流程引擎的部署信息、流程定义、任务等。在浏览器中打开以下链接： http://localhost:8080/activiti-admin   默认登陆名 admin 密码是admin

![1552539857018](/1552539857018.png)

​	默认端口号是9999，修改端口号到8080后就可以访问activiti-app的操作记录了。点击instance选择demo流程就可看到我们刚刚操作的流程实例信息了。

![1552539948637](/1552539948637.png)



### 4.在IDEA上运行Activiti

​	总结一下上面的步骤：首先我们修改了activiti.cfg.xml文件让数据保存在MySQL中，再通过在线编辑工具创建一个流程定义文件（即bpmn/bpmn20.xml 文件），然后将该流程部署（deploy），最后执行该流程到结束。

简化一下流程就是：

创建引擎-----导入流程图-----部署流程-----执行流程-----流程结束

使用gradle管理jar包，项目结构如下

![1552874614998](/1552874614998.png)



**build.gradle**

```groovy
plugins {
    id 'java'
}

group 'com.activiti'
version '1.0-SNAPSHOT'

sourceCompatibility = 1.8

repositories {
    //设置本地maven数据库地址，现在maven中找jar包
    mavenLocal()
    //阿里云服务器
    maven { url "http://maven.aliyun.com/nexus/content/groups/public" }
    mavenCentral()
}

dependencies {
    testCompile group: 'junit', name: 'junit', version: '4.12'
    compile group: 'org.activiti', name: 'activiti-engine', version: '6.0.0'
    compile group: 'mysql', name: 'mysql-connector-java', version: '5.1.47'
}
```

**activiti.cfg.xml**

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
">
    <!-- 配置 ProcessEngineConfiguration  -->
    <bean id="processEngineConfiguration" class="org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
        <property name="processEngineName" value="myProcessEngine"></property>

        <!-- 配置数据库连接 -->
        <property name="jdbcDriver" value="com.mysql.jdbc.Driver"></property>
        <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/activiti6?characterEncoding=utf8"></property>
        <property name="jdbcUsername" value="root"></property>
        <property name="jdbcPassword" value="1234"></property>
        <!-- 配置创建表策略 :没有表时，自动创建,默认为false-->
        <property name="databaseSchemaUpdate" value="true"></property>
    </bean>
</beans>
```

**Demo.java**

```java
package com.activiti;

import org.activiti.engine.*;
import org.activiti.engine.task.Task;

public class Demo {
    public static void main(String[] args) {
        //创建流程引擎
        ProcessEngine  engine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResourceDefault()
                .buildProcessEngine();
        // 获取流程储存服务组件
        RepositoryService repositoryService = engine.getRepositoryService();
        //获取流程运行服务组件
        RuntimeService runtimeService = engine.getRuntimeService();
        //获取流程任务组件
        TaskService taskService = engine.getTaskService();
        //部署流程文件
        repositoryService.createDeployment()
                .addClasspathResource("bpmn/demo.bpmn20.xml")
                .deploy();
        //启动流程
        runtimeService.startProcessInstanceByKey("demo");
        //查询任务
        Task task = taskService.createTaskQuery().singleResult();
        System.out.println("第一个任务名称："+task.getName());
        //完成任务
        taskService.complete(task.getId());
        System.out.println("第一个任务完成");
        task = taskService.createTaskQuery().singleResult();
        System.out.println("第二个任务名称："+task.getName());
        taskService.complete(task.getId());
        System.out.println("第二个任务完成");
        System.exit(0);
    }
}
```

#### 4.1 用eclipse编辑bpmn文件

IDEA上也有bpmn的插件，plugins里搜索 actiBPM插件，不过这个插件效果很差，eclipse插件安装教程网上百度有很多，这里提供了一个插件的离线安装包的下载地址。

activiti-eclipse-plugin 离线安装包及安装教程

链接：https://pan.baidu.com/s/1qhhtMXRGiUpXQSzzLO5nGA 
提取码：gntf 



## 三、Activiti流程引擎的配置

### 1. 流程引擎配置类

​	ProcessEngineConfiguration对象代表一个Activiti流程引擎的全部配置，该类提供了一系列的创建ProcessEngineConfiguration实例的静态方法，这些方法用来读取和解析相应的配置文件，并返回ProcessEngineConfiguration实例。

​	本节主要介绍了四种获取流程引擎配置（ProcessEngineConfiguration）对象的方法，以及databaseSchemaUpdate的四种配置属性。



#### 1.1 读取默认的配置文件

```java
ProcessEngineConfiguration config =  ProcessEngineConfiguration			                            .createProcessEngineConfigurationFromResourceDefault(); 
```

默认的配置文件 activiti.cfg.xml 在**resources**目录下

| 抛出异常                      | 原因                                                         |
| ----------------------------- | ------------------------------------------------------------ |
| FileNotFoundException         | ClassPath目录下找不到**activiti.cfg.xml**文件                |
| NoSuchBeanDefinitionException | 配置文件中没有找到名称为**processEngineConfiguration**的bean |



#### 1.2 读取自定义的配置文件

```java
ProcessEngineConfiguration config =  ProcessEngineConfiguration
			.createProcessEngineConfigurationFromResource("自定义的配置文件名.xml");
```

此方法创建bean的过程同第一个方法一样，配置文件也一样，同时它还提供了一个重载的方法，允许读取自定义名称的bean。

```java
ProcessEngineConfiguration config =  ProcessEngineConfiguration	.createProcessEngineConfigurationFromResource("自定义的配置文件名.xml","自定义bean的名称");
```



#### 1.3 读取输入流配置

这种方法允许配置文件以流式传输的形式导入

```java
File file = new File("resource/input-stream.xml");
//得到文件输入流
InputStream files = new FileInputStream(file);
 //创建引擎配置
ProcessEngineConfiguration config = ProcessEngineConfiguration
	.createProcessEngineConfigurationFromInputStream(files);   
```



#### 1.4 全部基于默认值创建

​	使用该方法创建流程引擎配置不需要任何配置文件，而且它默认使用H2内存数据库，流程引擎关闭后默认删除表。多用于测试，可以调用 **config.getDatabaseSchemaUpdate()**方法查看流程引擎操作数据库的类型。

```java
ProcessEngineConfiguration config = ProcessEngineConfiguration
		.createStandaloneInMemProcessEngineConfiguration();
	//值为 create-drop
	System.out.println(config.getDatabaseSchemaUpdate());
	//值为 jdbc:h2:mem:activiti
	System.out.println(cofig.getJdbcUrl());
```



#### 1.5 扩展阅读

| databaseSchemaUpdate | 属性(可在流程引擎的bean中进行属性配置)                       |
| -------------------- | ------------------------------------------------------------ |
| flase                | 默认值。activiti在启动时，会对比数据库表中保存的版本，如果没有表或者版本不匹配，将抛出异常。 |
| true                 | activiti会对数据库中所有表进行更新操作。如果表不存在，则自动创建。 |
| create_drop          | 在activiti启动时创建表，在关闭时删除表（必须手动关闭引擎，才能删除表）。 |
| drop-create          | 在activiti启动时删除原来的旧表，然后在创建新表（不需要手动关闭引擎）。 |



### 2. 获取流程引擎对象

​	上一节讲了activit的配置，根据这些配置我们可以创建相应的流程引擎，Activiti提供了多种方法获取流程引擎对象。可以根据ProcessEngineConfiguration 的 buildProcessEngine方法，也可以使用ProcessEngines的init方法来创建ProcessEngine实例。

#### 2.1  ProcessEngineConfiguration的buildProcessEngine方法

```java
ProcessEngineCongfiguration config = ProcessEngineConfiguration
.createProcessEngineConfigurationFromResource("activiti-cfg.xml");	
ProcessEngine engine = cofing.buildProcessEngine();
```



#### 2.2 ProcessEngines 

​	ProcessEngines是一个创建流程引擎与关闭流程引擎的工具类，所有创建的（包括其他方法创建的）ProcessEngine对象都会被注册到ProcessEngines维护的一个Map对象中，key就是ProcessEngine实例的名称，value就是对应的实例对象。

##### 2.2.1  ProcessEngines的init方法与getDefaultProcessEngine方法

​	调用init方法，会自动加载ClassPath下的全部Activiti配置文件，并将创建的ProcessEngine引擎全部注册到ProcessEngines的Map中,调用getProcessEngines()方法可以获取到这个map对象。

​	此外ProcessEngines还提供了一个方法getDefaultProcessEngine，用于返回一个默认为default的引擎，同时调用该方法会先判断ProcessEngines是否进行了初始化，如果没有，会调用init方法进行一次初始化。

```java
	//调用初始化方法
    ProcessEngines.init();
	//获取所有的引擎
	Map<String,ProcessEngine> engines = ProcessEngines.getProcessEngines();

```

##### 2.2.2 registerProcessEngine 和 unregister方法（了解）

​	registeProcessEngine 方法向 ProcessEngines 中注册一个 ProcessEngine 实例，unregister方法则注销 ProcessEngines 中一个 ProcessEngine 实例。注册与注销 ProcessEngine 实例，均会根
ProcessEngine 实例的名称进行操作。



##### 2.2.3 destroy方法

​	ProcessEngines的destroy方法是对其维护的实例的销毁方法，调用此方法会调用所有ProcessEngine的close()方法。

```java
    //获取默认引擎
	ProcessEngine defaultEngine = ProcessEngines.getDefaultProcessEngine();	
	System.out.println("引擎数量："+ProcessEngines.getProcessEngines().size());
	//调用销毁方法
	ProcessEngines.destroy();
	System.out.println("调用销毁方法后的引擎数量："+
                      ProcessEngines.getProcessEngines().size())
    
     输出结果：
     引擎数量：1
     调用销毁方法后的引擎数量：0
```

​	需要注意的是调用destroy方法的前提是ProcessEngines的初始化状态为true，否则调用destroy方法不会有销毁效果。



#### 2.3 ProcessEngine对象

##### 2.3.1 获取服务组件

​	ProcessEngine在创建后，会初始化一系列的服务组件，这些组件提供了大部分操作流程引擎数据的业务方法，类似与service层，可以使用getxxxxService获得这些组件实例。

```java
ProcessEngine engine = ProcessEngineConfiguration
			.createProcessEngineConfigurationFromResource("activiti.cfg.xml")
    		.buildProcessEngine();

RepositoryService repositoryService = engine.getRepositoryService();
RuntimeService runtimeService = engine.getRuntimeService();
TaskService taskService = engine.getTaskService();
IdentityService identityService = engine.getIdentityService();
ManagementService managementService = engine.getManagementService() ; 
HistoryService historyService = engine . getHistoryService() ; 
FormService formService = engine.getFormService() ; 
```

##### 2.3.2 设置流程引擎名称

​	ProcessEngine类本身没有提供设置名称的方法，我们是通过ProcessEngineconfiguration来完成。

```java
ProcessEngineConfiguration config = ProcessEngineConfiguration
			.createProcessEngineConfigurationFromResource("name.xml");
//设置流程引擎名称
config.setProcessEngineName("test");
ProcessEngine engine = config.buildProcessEngine();
//根据名称查询流程引擎
ProcessEngine engineTest = ProcessEngines.getProcessEngine("test");
System.out.prntln("创建的引擎实例："+engine) ; 
System.out.prntln("查询的引擎实例："＋ engineTest) ; 


输出结果：
创建的引擎实例：org.activiti.engine.impl.ProcessEngineImpl@16fe72b
查询的引擎实例：org.activiti.engine.impl.ProcessEngineImpl@16fe72b
```

​	我们可以看出，两个引擎为同一对象，所以buildProcessEngine方法实际上完成了ProcessEngins的register操作；





## 四、用户组与用户

### 1. 用户组的管理

#### 1.1 Group对象

​	Group是一个接口，每一个Group实例表示一条用户组数据，它有一个子接口GroupEntity和一个实现类GroupEntityImpl，实现类提供了Group对应的表==act_id_group==中**NAME_**和**TYPE_**字段的get/set方法

![1552976554](1552976554.png)

| 字段名 | 属性名   | 属性                               |
| ------ | -------- | ---------------------------------- |
| ID_    | id       | Group主键，创建Group的时候指定     |
| REV_   | revision | 用户组数据的版本，每修改一次都会+1 |
| NAME_  | name     | 用户组名称                         |
| TYPE_  | type     | 用户组的类别                       |

#### 1.2 创建用户组

```java
/*创建用户组
*省略了获取引擎和服务的步骤 
*/
public void createGroup(){
        //获取groupId,防止重复用UUID
        String groupId = UUID.randomUUID().toString();
        //新建Group
        Group group = identityService.newGroup(groupId);
        //设置属性
        group.setName("用户组");
        group.setType("Users");
        //保存Group到数据库
        identityService.saveGroup(group);
        //查询Group
   		Group result =identityService.createGroupQuery()
       			.groupId(groupId).singleResult();
        System.out.println("Name:"+result.getName()
                           +"Id:"+result.getId()
                           +"Type"+result.getType());
    }
```

[^注意]: 调用newGroup()方法的时候groupId不能为空，否则会抛出 **groupid is null**的异常；主键重复的时候会抛出**Duplicate entry ‘1’ for key ‘PRIMARY’**的异常

​	如果不用UUID也可指定一个String类型的groupId，然后在设置属性的时候调用.setId(null),activiti会自动生成主键。 

#### 1.3 修改用户组

```java
/*
*修改用户组，不能修改ID，否则保存时会报错
*/
public void updateGroup(){
        //先查出用户组
        Group group = identityService.createGroupQuery()
            .groupName("用户组").singleResult();
        //设置属性
        group.setName("用户组");
        group.setType("Users");
        //如果修改Id，程序会报错如下
        //GroupEntityImpl@22295ec4 was updated by another transaction concurrently
        //group.setId(null);
        //保存修改
        identityService.saveGroup(group);
    }
```

#### 1.4 删除用户组

​	identityService方法提供了一个deleteGroup方法来删除用户组信息。

​	ACtiviti各模块之间与用户组相关联的数据表，仅仅提供一个字段来记录用户组的id，没有做外键关联，例如act_ru_identitylink表。

​	但像用户与用户组这样多对多的关系，Activiti还是设了一个中间表（act_id_membership），并做了外键关联。因此删除用户组的deleteGroup方法在执行的时候，会先将中间表里的关联数据删除，然后再删除用户组数据。

```java
/*
*如果act_id_membership表中有关联的数据也会一并删除
*/
identityService.deleteGroup(String groupId)
```



### 2. Activiti数据的查询

> ​	Activiti提供了一套数据查询API供开发者使用，可以使用各个服务组件的createXXXQuery 方法来获取这些查询对象。本节将结合用户组数据来讲解Activiti的数据查询设计，这些设计可应用于整个Activiti的数据查询体系。

#### 2.1 查询对象

​	Activiti的各个服务组件均提供了createXXXQuery方法，返回一个Query实例；Query是所有查询对象的父接口，该接口提供了若干个基础方法

| 方法名       | 属性                                               |
| ------------ | -------------------------------------------------- |
| asc          | 查询结果升序                                       |
| count        | 计算查询结果的数据量                               |
| desc         | 查询结果降序                                       |
| list         | 封装查询结果，返回相应类型的集合                   |
| listpage     | 分页返回查询结果                                   |
| singleResult | 查询单条符合条件的数据，若查到多条数据，则抛出异常 |

##### 2.1.1 list,listPage,count方法

```java
//list：返回所有的group对象并封装进List<>中
List<Group> datas = identityService.createGroupQuery().list();
//listPage：从索引为1的数据起，查询10条数据
List<Group> datas2 = identityService.createGroupQuery().listPage(1,10)
//count：统计group数量
 int count = identityService.createGroupQuery().count();
```

##### 2.1.2 asc和desc排序

​	Query提供了asc和desc方法，可以设置查询结果的排序方式，但是调用前必须告诉Query以何种条件进行排序即_orderByXXX方法_（如 orderByGroupId、orderByGroupName），否则会抛出**ActivitiException**，信息为：**You should call any of  the orderBy methods first before specifying a direction**；

```java
//注意排序后还是要调用list()或者listPage()方法才能返回具体的结果集，否则还是Query实例本身
List<Group> datas=identityService.createGroupQuery().orderByGroupId().asc().list();
```

##### 2.1.3 多字段排序

​	想要对多字段进行排序的时候要注意，如果调用了orderByXXX方法却没有调用一次asc()或desc()方法，则该排序方法会被下一次方法设置的查询条件覆盖。

```java
        //先按Name升序排列，再按Id降序排列
 List<Group> list = identityService.createGroupQuery()
            .orderByGroupName().asc().orderByGroupId().desc().list();

   for (Group group : list) {
     System.out.println("Id:"+group.getId()+"Name:"+group.getName());  
   }
```



##### 2.1.4 singleResult方法

​	根据查询条件到数据库中查询唯一的数据记录，若没有符合条件的数据，会返回null。若查到多条记录，则抛出异常 ：**Query return 2 results instead of max 1**。一般与Id查询条件配合使用。

```java
Group result = identityService.createGroupQuery().groupId(groupId).singleResult();
```



##### 2.1.5 使用原生SQL语句查询

​	各个服务组件中，都提供了createNativeXXXQuery的方法，可以调用sql方法传递SQL语句，调用parameter方法设置查询参数。

```java
List<Group> groups = identityService.createNativeGroupQuery()
    .sql("select * from ACT_ID_GROUP where Name_ = #{name} and Id_=#{id}")
    //parameter(String name,Object value) 多参数的时候可以接多个parameter方法
    .parameter("name","Users")
    .patameter("id","3")
    .list();
```



#### 2.2 用户组数据查询

​	GroupQuery对象除了拥有Query接口的公用方法，还有自己的查询方法

| 方法名                                | 属性                                             |
| ------------------------------------- | ------------------------------------------------ |
| groupId(String groupId)               | 根据ID查询数据                                   |
| groupMember(String groupMemberUserId) | 根据用户ID查询用户所在的组                       |
| groupName(String groupName)           | 根据用户组名称查询用户组                         |
| groupNameLike(String groupName)       | 根据用户组名称模糊查询用户组                     |
| groupType(String groupType)           | 根据用户组类型查询用户组                         |
| orderByGroupId()                      | 设置根据Id排序                                   |
| orderByGroupName()                    | 设置根据Name排序                                 |
| orderByGroupType()                    | 设置根据类型排序                                 |
| potentialStarter(String procDefId)    | 根据流程定义的Id查询有权限启动该流程定义的用户组 |

[^注意！！]: 表中的方法的返回值都是`GroupQuery`对象，如果想要具体的数据都要调用**list()**或者**singleResult()**等方法



### 3. 用户的管理

#### 3.1 User对象

​	与Group对象一样，User对象同样是一个接口，它有一个子接口UserEntity，实现类为UserEntityImpl，对应==act_id_user==表，包含以下映射属性。

| 字段名      | 属性名             | 属性                                                     |
| ----------- | ------------------ | -------------------------------------------------------- |
| ID_         | id                 | 用户ID                                                   |
| FIRST_      | firstName          | 用户的姓                                                 |
| LAST_       | lastName           | 用户的名                                                 |
| EMAIL_      | email              | 用户邮箱                                                 |
| PWD_        | password           | 用户密码                                                 |
| PICTURE_ID_ | pictureByteArrayId | 用户图片信息对应的数据记录ID，保存在act_ge_bytearray表中 |
| revision    | REV_               | 该用户数据的版本，activiti自行管理                       |



#### 3.2 添加用户

添加用户的操作同添加用户组的操作差不多

```java
 @Test
    public void createUser(){
        //新建用户对象
        User user = identityService.newUser("1");
		//编辑用户信息
        user.setId(null);
        user.setFirstName("X");
        user.setLastName("dd");
        user.setEmail("xdd@gmail.com");
        user.setPassword("1234");
    	//保存用户信息
        identityService.saveUser(user);
    }
```



#### 3.3 修改用户

```java
  /*
   *同样不能修改ID
   */
    public void updateUser(){
        //根据ID查找用户
        User user = identityService.createUserQuery()
            .userId("1112").singleResult();
       //修改用户信息
        user.setPassword("12345");
        //保存修改
        identityService.saveUser(user);
    }	
```



####  3.4 删除用户

​	删除用户会先删除本模块中与用户相关联的数据，如用户的图片信息，中间表中的信息等

```java
identityService.deleteUser(String UserID);
```



#### 3.5 验证用户密码

​	checkPassword的第一个参数是用户的ID，第二个参数是用户的密码

```java
identityService.checkPassword(String userId, String password)
```



#### 3.6 用户数据查询

​	UserQuery对象也提供了自己的查询方法

| 方法名              | 属性                                         |
| ------------------- | -------------------------------------------- |
| userEmail           | 根据email查询用户                            |
| userFirstName       | 根据用户姓查询                               |
| userLastName        | 根据用户名查询                               |
| userId              | 根据用户ID查询                               |
| memberOfGroup       | 根据用户ID组查询该组下全部的用户             |
| orderByUserEmail    | 根据Email进行排序                            |
| orderByUserId       | 根据ID进行排序                               |
| orderByUserLastName | 根据用户名排序                               |
| potentialStarter    | 根据流程定义的id查询有权限启动流程定义的用户 |

表中省略了一些模糊查询的方法

#### 3.7 设置认证用户

​	。。。有点懵

​	identityService提供了setAuthenticateUserId方法将用户的ID设置到当前的线程中



### 4. 用户信息管理

​	在avtiviti中用户信息表是将用户的相关信息抽象出来单独建的一张表act_id_info

#### 4.1 添加和删除用户信息

```java
//添加用户信息
identityService.setUserInfo(String userId,String key,String value);
//删除用户信息
 identityService。deleteUserInfo(String userId,String key);
```



#### 4.2 查询用户信息

```java
identityService.getUserInfo(String id,String key);
```



#### 4.3 设置用户图片

```java
//读取图片
FileInputStream fis = new FileInputStream(new File("resouces"));
//设置图片转化为byte数组
 BufferedImage img = ImageIO.read(fis);
ByteArrayOutputstream output = new ByteArrayOutputStream();
ImageIO.write(img,"png","output");
//获取byte数组，并转化为picture实例
byte[] picArray = output.toByteArray();
Picture picture = new Prcture("picArray","angus image");
//为用户设置图片
 identityService.setUserPicture(id,picture);
    
```



### 5. 用户与用户组关系

​	用户与用户组的关系是多对多的关系，Activiti创建了act_id_membership表用来维护这些关系，第一个字段为用户ID，第二个字段为用户组ID。

#### 5.1 绑定关系和解除关系

​	绑定关系意味着像act_id_membership表中写入一条数据。

```java
/*省略用户创建和用户组创建过程
*@User user 用户对象
*@Group group 用户组对象
*/
	//绑定关系
identityService.createMembership(user.getId(),group.getId());
    //解除关系
identityService.deleteMembership(user.getId(),group.getId());
```

[^注意]:解除关系的时候就算数据表中没有对应的数据，也不会抛出异常



#### 5.2 查询用户组下的用户

​	activiti提供了方法查询用户组下的用户和用户所在的用户组

```java
/*省略用户创建和用户组创建过程
*@User user 用户对象
*@Group group 用户组对象
*/
//查询用户组下的用户
List<User> users = identityService.createUserQuery().memberOfGroup(group.getId()).list();

//查询用户所在的用户组
List<Group> group = identityService.createUserQuery().groupMember(user.getId()).list();
```





## 五、流程存储服务

​	RepositoryService负责对流程文件的部署以及流程的定义进行管理，不管是JBPM还是Activiti等工作流引擎都会产生流程文件。在Activiti中这些数据被保存在==act_ge_bytearray==表中。

### 1. 流程文件的部署

#### 1.1Deployment对象

​	Deployment对象也是一个接口，一个Deployment实例代表一条==act_re_deployment==表的数据。子接口为DeploymentEntity，实现类为DeploymentEntityImpl，以下是映射表

| 字段名          | 属性名         | 属性                                         |
| --------------- | -------------- | -------------------------------------------- |
| ID_             | id             | 主键                                         |
| NAME_           | name           | 部署名称                                     |
| CATEGORY_       | category       | 部署类别                                     |
| KEY_            | key            | 为部署设置键属性                             |
| TENANT_ID_      | tenantId       | 同一个软件可能被多个租户使用，预留字段租户ID |
| DEPLOY_TIME     | deploymentTime | 部署时间                                     |
| ENGINE_VERSION_ | engineVersion  | 引擎版本                                     |



#### 1.2 获取DeploymentBuilder对象

​	对流程文件进行部署需要使用DeploymentBuilder对象，获取该对象可以调用RespositoryService的createDeployment方法，具体代码如下：

```java
//得到流程存储服务实例
RepositoryService repositoryService = engine.getRepositoryService();
//创建DeploymentBuilder对象
DeploymentBuilder builder = repositoryService.createDeployment();
```

#### 1.3 添加流程文件五种方法

​	DeploymentBuilder 中包含了多种 addXXX方法，可以为流程部署添加资源。

| 方法                                                  | 属性                             |
| ----------------------------------------------------- | -------------------------------- |
| addClasspathResource(String resource)                 | 添加classpath下的资源文件        |
| addInputStream(String resourceName,InputStream)       | 添加输入流资源                   |
| addString(String resourceName,String text)            | 添加字符串资源                   |
| addZipInputStream(ZipInputStream inputStream)         | 添加zip压缩包资源                |
| addBpmnModel(String resourceName,BpmnModel bpmnModel) | 解析BPMN模型对象，并作为资源保存 |
| addBytes(String resourceName,byte[] bytes)            | 添加字节资源                     |

以下通过代码示范一些添加资源文件的方法

#####  1.3.1 添加输入流资源

​	在DeploymentEntityImpl类中，用一个Map来维护资源，表示一次部署中会有多个资源。调用 addInputStream 方法，实际上就是网DeploymentEntityImpl的Map里面添加元素，Map的key是资源名称，value的解析InputStream后获得的byte数组。

```java
/* 
 * 省去了获取流程引擎和流程存储服务实例的步骤
 * RepositoryService repositoryService
 */
	//第一个资源输入流
	InputStream is1 = new FileInputStream(new File("resource1"));
	//第二个资源输入流
	InputStream is2 = new FileInputStream(new File("resource2"));
	//创建DeploymentBuilder实例
	DeploymentBuilder builder = repositoryService.createDeployment();
	builder.addInputStream("inputA",is1);
	builder.addInputStream("inputB",is2);
	//执行部署方法
	builder.deploy();
```



##### 1.3.2 添加 classpath 资源

​	与addInputStream方法类似，addClasspathResource方法也是向部署实体的Map里面添加元素。但不同的是addClasspathResource方法会将指定的 classpath 下的资源文件转换为 InputStream ，再调用 addInputStream 方法。

```java
/* 
 * 省去了获取流程引擎和流程存储服务实例的步骤
 * RepositoryService repositoryService
 */
	//创建DeploymentBuilder实例
	DeploymentBuilder builder = repositoryService.createDeployment();
	//一次添加一个文件
	builder.addClasspathResource("resource1");
	builder.addClasspathResource("resource2");
	//执行部署方法
	builder.deploy();
```



##### 1.3.3 添加压缩包资源

​	在实际应用中，可能要将多个资源部署到流程引擎中，可以将这些关联的资源压缩在一个zip包中，可以调用addZipInputStream方法直接部署该压缩包，该方法会遍历压缩包中的全部文件，再将其转换为byte数组，写入资源表中

```java
/* 
 * 省去了获取流程引擎和流程存储服务实例的步骤
 * RepositoryService repositoryService
 */
	//创建DeploymentBuilder实例
	DeploymentBuilder builder = repositoryService.createDeployment();
	//获取zip资源流
	FileInputStream fis = new FileInputStream(new File("resource.zip"));
	//读取zip文件，创建 ZipInputStream 对象
	ZipInputStream zi = new ZipInputStream(fis);
	//添加Zip压缩包资源
	builder.addZipInputStream(zi);
	//执行部署
	builder.deploy();
```



##### 1.3.4 添加BPMN模型资源

​	DeploymentBuilder提供了一个 addBpmnModel 方法，可传入BPMN规范的模型来进行部署。

```java
/* 
 * 省去了获取流程引擎和流程存储服务实例的步骤
 * RepositoryService repositoryService
 */
	DeploymentBuilder builder = repositoryService.createDeployment(); 
	builder.addBpmnModel("MyCodeProcess",createProcessModel()) 
		.name("MyCodeDeploy").deploy();
    
private BpmnModel createProcessModel() { 
	//创建 BPMN 模型对象
	BpmnModel model= new BpmnModel(); 
	org.activiti.bpmn.model.Process process= new org.activiti.bpmn.model.Process(); 
	model.addProcess(process) ; 
	process.setid("myProcess"); 
	process.setName("My Process"); 
	//开始事件
	StartEvent startEvent = new StartEvent(); 
	startEvent.setid("startEvent"); 
	process.addFlowElement(startEvent); 
	//用户任务
	UserTask userTask =new UserTask(); 
	userTask.setName("User Task"); 
	userTask.setid("userTask"); 
	process.addFlowElement(userTask); 
	//结束事件
	EndEvent endEvent =new EndEvent(); 
	endEvent.setid("endEvent"); 
	process addFlowElement(endEvent);
	//添加流程顺序
	process.addFlowElement(new SequenceFlow("startEvent","userTask")); 
	process.addFlowElement(new SequenceFlow("userTask","endEvent")); 
	return model; 
}
```



##### 1.3.5 addString 和addBytes方法

​	addBytes方法比较简单，直接将资源名称与byte数组添加到DeploymentEntityImpl的Map中，不再赘述了。而addString方法也就是将String类型的数据转化为byte类型，再调用addBytes方法。

#### 1.4 修改部署信息

​	使用DeploymentBuilder的name、key、category、tenanId方法可以设置属性。

```java
builder.name("name").tenantId("tenantId").key("key").category("category");
```

#### 1.5 流程部署

​	添加资源文件后可调用deploy方法完成流程的部署，部署后会在act_re_procdef表中写入部署流程的数据

```java
builder.addClasspathResource("resource").deploy();
```



#### 1.6 过滤重复部署和取消部署验证

两次部署之间资源没有任何变化，为了防止数据库的重复写入，在部署的时候调用enableDuplicateFiltering方法，它会根据部署对象名称去数据库中查找最后一条部署记录，如果发现最后一条记录与当前部署的记录一致就不会重新部署。这里的一致指的是资源文件和资源名一样。

```java
builder.addClassPathResource(String resource).enableDuplicateFiltering();
```

取消部署验证

```java
builder.disableSchemaValidation()  //取消xml验证
builder.disableBpmnValidation()   //取消bpmn验证
```



### 2. 流程定义管理

​	流程定义管理是指由RepositoryService提供的一系列对流程定义的控制，包括中止流程定义、激活流程定义和设置流程权限等。

#### 2.1 ProcessDefinition对象

​	ProcessDefinition对象是一个接口，一个ProcessDefinition实例代表一条流程定义数据，它的实现类为ProcessDefinitionEntityImpl，对应的表为==act_re_procdef==。

#### 2.2 获取ProcessDefinition对象

​	获取ProcessDefinition对象要求已经有流程部署了，即act_re_procdef 表中必须有数据。然后可以通过`repositoryService.createProcessDefinitionQuery()`去调用对应的方法获取流程定义对象，下表列出了几种方法：

| 方法                                                | 说明                     |
| --------------------------------------------------- | ------------------------ |
| processDefinitionCategory(String category)          | 根据类别查询流程定义     |
| processDefinitionCategoryNotEquals(String category) | 列出不包含字段的类别查询 |
| processDefinitionCategoryLike(String categoryLike)  | 根据类别 模糊 查询       |
| processDefinitionName(String name)                  | 根据name查询             |
| processDefinitionId(String id)                      | 根据ID查询单个流程实例   |
| processDefinitionIds(Set<String> ids)               | 多ID 查询返回集合        |
| processDefinitionKey(String key)                    | 根据关键词查询           |
| deploymentId(String id)                             | 根据流程部署id查询       |
| deploymentIds(Set<String> ids)                      | 根据多个流程部署id查询   |

​	如果想要具体的对象或者对象集 还需要调用singleResult() 或者 list()方法。



#### 2.3 挂起和激活流程定义

​	流程的挂起与激活在实际生产中常常会用到，下面是具体的代码示例：

```java
	//这里演示根据流程实例ID挂起、激活流程实例
	String id  =  processDefinition.getId();   
   // 根据一个流程实例的id挂起流程实例
   runtimeService.suspendProcessInstanceById(id);
    // 根据一个流程实例的id激活流程实例
   runtimeService.activateProcessInstanceById(id);

	//其他的根据key，tendeId（租户ID） 等参数就不赘述了，根据实际情况灵活使用
```

**_注意：流程挂起后不能重复挂起，只能激活后再挂起，激活同样也是如此_**

#### 2.4 流程缓存设置 (待施工…)

​	为了提高响应性能，Activiti读取流程后将一些常用项做了一个缓存存在一个Map中，key是流程定义的Id

获取缓存….

还没测出来…

后续添加吧….

 流程缓存在配置文件的配置项，value表示缓存最大数量

```xml
<property name="processDefinitionCacheLimit" value="5" />
```



### 3. 流程定义权限

#### 3.1 设置权限

```java
repositoryService.addCandidateStarterUser("流程定义ID","用户ID");
repositoryService.addCandidateStarterGroup("流程定义ID","组ID");
```

#### 3.2 IdentityLink对象

​	一个IdentutyLink对象表示一种 身份数据与流程绑定的关系。

#### 3.3 查询权限数据

```java
//根据用户查询有权限的流程定义
List<ProcessDefinition> list = repositoryService
        .createProcessDefinitionQuery().startableByUser("用户User对象").list();
```

### 4. 数据的查询与删除



