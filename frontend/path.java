
2023-10-05 12:27:29.846  WARN 49848 --- [  restartedMain] ConfigServletWebServerApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'liquibase' defined in class path resource [org/springframework/boot/autoconfigure/liquibase/LiquibaseAutoConfiguration$LiquibaseConfiguration.class]: Invocation of init method failed; nested exception is liquibase.exception.DatabaseException: org.postgresql.util.PSQLException: FATAL: password authentication failed for user "phitag"
2023-10-05 12:27:29.848  INFO 49848 --- [  restartedMain] o.apache.catalina.core.StandardService   : Stopping service [Tomcat]
2023-10-05 12:27:29.855  INFO 49848 --- [  restartedMain] ConditionEvaluationReportLoggingListener : 

Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
2023-10-05 12:27:29.862 ERROR 49848 --- [  restartedMain] o.s.boot.SpringApplication               : Application run failed

org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'liquibase' defined in class path resource [org/springframework/boot/autoconfigure/liquibase/LiquibaseAutoConfiguration$LiquibaseConfiguration.class]: Invocation of init method failed; nested exception is liquibase.exception.DatabaseException: org.postgresql.util.PSQLException: FATAL: password authentication failed for user "phitag"
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1786) ~[spring-beans-5.3.6.jar:5.3.6]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:602) ~[spring-beans-5.3.6.jar:5.3.6]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:524) ~[spring-beans-5.3.6.jar:5.3.6]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:335) ~[spring-beans-5.3.6.jar:5.3.6]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-5.3.6.jar:5.3.6]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:333) ~[spring-beans-5.3.6.jar:5.3.6]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:208) ~[spring-beans-5.3.6.jar:5.3.6]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:322) ~[spring-beans-5.3.6.jar:5.3.6]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:208) ~[spring-beans-5.3.6.jar:5.3.6]
	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1154) ~[spring-context-5.3.6.jar:5.3.6]
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:908) ~[spring-context-5.3.6.jar:5.3.6]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:583) ~[spring-context-5.3.6.jar:5.3.6]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:144) ~[spring-boot-2.4.5.jar:2.4.5]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:782) ~[spring-boot-2.4.5.jar:2.4.5]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:774) ~[spring-boot-2.4.5.jar:2.4.5]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:439) ~[spring-boot-2.4.5.jar:2.4.5]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:339) ~[spring-boot-2.4.5.jar:2.4.5]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1340) ~[spring-boot-2.4.5.jar:2.4.5]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1329) ~[spring-boot-2.4.5.jar:2.4.5]
	at de.garrafao.phitag.PhitagApplication.main(PhitagApplication.java:18) ~[classes/:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77) ~[na:na]
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:568) ~[na:na]
	at org.springframework.boot.devtools.restart.RestartLauncher.run(RestartLauncher.java:49) ~[spring-boot-devtools-2.4.5.jar:2.4.5]
Caused by: liquibase.exception.DatabaseException: org.postgresql.util.PSQLException: FATAL: password authentication failed for user "phitag"
	at liquibase.integration.spring.SpringLiquibase.afterPropertiesSet(SpringLiquibase.java:318) ~[liquibase-core-3.10.3.jar:na]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1845) ~[spring-beans-5.3.6.jar:5.3.6]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1782) ~[spring-beans-5.3.6.jar:5.3.6]
	... 24 common frames omitted
Caused by: org.postgresql.util.PSQLException: FATAL: password authentication failed for user "phitag"
	at org.postgresql.core.v3.ConnectionFactoryImpl.doAuthentication(ConnectionFactoryImpl.java:613) ~[postgresql-42.2.19.jar:42.2.19]
	at org.postgresql.core.v3.ConnectionFactoryImpl.tryConnect(ConnectionFactoryImpl.java:161) ~[postgresql-42.2.19.jar:42.2.19]
	at org.postgresql.core.v3.ConnectionFactoryImpl.openConnectionImpl(ConnectionFactoryImpl.java:213) ~[postgresql-42.2.19.jar:42.2.19]
	at org.postgresql.core.ConnectionFactory.openConnection(ConnectionFactory.java:51) ~[postgresql-42.2.19.jar:42.2.19]
	at org.postgresql.jdbc.PgConnection.<init>(PgConnection.java:223) ~[postgresql-42.2.19.jar:42.2.19]
	at org.postgresql.Driver.makeConnection(Driver.java:465) ~[postgresql-42.2.19.jar:42.2.19]
	at org.postgresql.Driver.connect(Driver.java:264) ~[postgresql-42.2.19.jar:42.2.19]
	at com.zaxxer.hikari.util.DriverDataSource.getConnection(DriverDataSource.java:138) ~[HikariCP-3.4.5.jar:na]
	at com.zaxxer.hikari.pool.PoolBase.newConnection(PoolBase.java:358) ~[HikariCP-3.4.5.jar:na]
	at com.zaxxer.hikari.pool.PoolBase.newPoolEntry(PoolBase.java:206) ~[HikariCP-3.4.5.jar:na]
	at com.zaxxer.hikari.pool.HikariPool.createPoolEntry(HikariPool.java:477) ~[HikariCP-3.4.5.jar:na]
	at com.zaxxer.hikari.pool.HikariPool.checkFailFast(HikariPool.java:560) ~[HikariCP-3.4.5.jar:na]
	at com.zaxxer.hikari.pool.HikariPool.<init>(HikariPool.java:115) ~[HikariCP-3.4.5.jar:na]
	at com.zaxxer.hikari.HikariDataSource.getConnection(HikariDataSource.java:112) ~[HikariCP-3.4.5.jar:na]
	at liquibase.integration.spring.SpringLiquibase.afterPropertiesSet(SpringLiquibase.java:313) ~[liquibase-core-3.10.3.jar:na]
	... 26 common frames omitted


Process finished with exit code 0
