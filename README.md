> I was working on sub-database and sub-table for learning purpose, and found sharding-jdbc, so I started an incremental 
> sharding application of springboot+sharding-jdbc+mybatis. 
> 
## 1. Background
I won't go into the background of the project, but let's take an example: the service is available in Bangladesh and USA, you have option 
to make payment in BDT or USD. The current sharding strategy is as follows: region Bangladesh and USA has separate database, and each stores 
payments for BDT and USD currency and divides them according to the monthly schedule. As shown below:
* db_bd
	* payment_bdt_2022_01
	* payment_usd_2022_01
	* ........
	* payment_bdt_2022_12
	* payment_usd_2022_12
* db_us
    * payment_bdt_2022_01
	* payment_usd_2022_01
	* ........
	* payment_bdt_2022_12
	* payment_usd_2022_12

  The sub-library sub-table is like this. Build a library based on this.
	
	**Don't discuss whether it is appropriate to sub-database and sub-table. Here, this kind of fragmentation is just a chestnut to illustrate the matter of sub-database and sub-table.**
	
	**Sharding-jdbc is SQL that does not support database building. If you have incremental databases and data tables like me, you need to build databases and data tables for a period of time at one time.**

## 2. Building a library
Considering that there are indeed many tables, I only build tables for two months. See the demo file for the statement.

## 3. Springboot integrated sharding-jdbc
The maven configuration pom is as follows:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>com.spartajet</groupId>
	<artifactId>springboot-sharding-jdbc-demo</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>jar</packaging>

	<name>springboot-sharding-jdbc-demo</name>
	<description>Springboot integrate Sharding-jdbc Demo</description>

	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>1.4.1.RELEASE</version>
		<relativePath></relativePath>
	</parent>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<java.version>11</java.version>
		<project.build.jdk>${java.version}</project.build.jdk>
		<spring.boot.version>1.4.1.RELEASE</spring.boot.version>
		<com.alibaba.druid.version>1.0.13</com.alibaba.druid.version>
		<mysql-connector-java.version>8.0.11</mysql-connector-java.version>
		<sharding-jdbc.version>1.4.1</sharding-jdbc.version>
		<com.google.code.gson.version>2.8.0</com.google.code.gson.version>
		<joda-trade.version>2.9.7</joda-trade.version>
		<commons-dbcp.version>1.4</commons-dbcp.version>
		<commons-io.version>2.5</commons-io.version>
		<mybatis-spring-boot-starter.version>2.2.0</mybatis-spring-boot-starter.version>

		<disruptor.version>3.3.6</disruptor.version>
		<log4j.version>2.7</log4j.version>
	</properties>

	<dependencies>

		<dependency>
			<groupId>com.dangdang</groupId>
			<artifactId>sharding-jdbc-core</artifactId>
			<version>${sharding-jdbc.version}</version>
		</dependency>

		<dependency>
			<groupId>com.dangdang</groupId>
			<artifactId>sharding-jdbc-config-spring</artifactId>
			<version>${sharding-jdbc.version}</version>
		</dependency>

		<dependency>
			<groupId>com.dangdang</groupId>
			<artifactId>sharding-jdbc-self-id-generator</artifactId>
			<version>${sharding-jdbc.version}</version>
		</dependency>

		<dependency>
			<groupId>com.dangdang</groupId>
			<artifactId>sharding-jdbc-transaction</artifactId>
			<version>${sharding-jdbc.version}</version>
		</dependency>

		<dependency>
			<groupId>commons-io</groupId>
			<artifactId>commons-io</artifactId>
			<version>${commons-io.version}</version>
		</dependency>

		<dependency>
			<groupId>commons-dbcp</groupId>
			<artifactId>commons-dbcp</artifactId>
			<version>${commons-dbcp.version}</version>
		</dependency>

		<dependency>
			<groupId>com.google.code.gson</groupId>
			<artifactId>gson</artifactId>
			<version>${com.google.code.gson.version}</version>
		</dependency>


		<dependency>
			<groupId>org.mybatis.spring.boot</groupId>
			<artifactId>mybatis-spring-boot-starter</artifactId>
			<version>${mybatis-spring-boot-starter.version}</version>
		</dependency>

		<dependency>
			<groupId>org.mybatis</groupId>
			<artifactId>mybatis-typehandlers-jsr310</artifactId>
			<version>1.0.2</version>
		</dependency>

		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>${mysql-connector-java.version}</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
			<version>${spring.boot.version}</version>
			<exclusions>
				<exclusion>
					<artifactId>org.springframework.boot</artifactId>
					<groupId>spring-boot-start-logging</groupId>
				</exclusion>
			</exclusions>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<version>${spring.boot.version}</version>
			<scope>test</scope>
		</dependency>

		<!--Configure Log4j2 logging begin -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-log4j2</artifactId>
			<exclusions>
				<exclusion>
					<groupId>org.apache.logging.log4j</groupId>
					<artifactId>log4j-web</artifactId>
				</exclusion>
			</exclusions>
		</dependency>

		<dependency>
			<groupId>org.apache.logging.log4j</groupId>
			<artifactId>log4j-1.2-api</artifactId>
			<version>${log4j.version}</version>
		</dependency>

		<dependency>
			<groupId>org.apache.logging.log4j</groupId>
			<artifactId>log4j-web</artifactId>
			<version>${log4j.version}</version>
		</dependency>
		<dependency>
			<groupId>com.lmax</groupId>
			<artifactId>disruptor</artifactId>
			<version>${disruptor.version}</version>
		</dependency>

		<dependency>
			<groupId>javax.xml.bind</groupId>
			<artifactId>jaxb-api</artifactId>
			<version>2.3.0</version>
		</dependency>
		<!--Configure Log4j2 logging  end -->

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
			<version>${spring.boot.version}</version>
			<exclusions>
				<exclusion>
					<artifactId>org.springframework.boot</artifactId>
					<groupId>spring-boot-start-logging</groupId>
				</exclusion>
				<exclusion>
					<artifactId>logback-classic</artifactId>
					<groupId>ch.qos.logback</groupId>
				</exclusion>
				<exclusion>
					<artifactId>log4j-over-slf4j</artifactId>
					<groupId>org.slf4j</groupId>
				</exclusion>
			</exclusions>
		</dependency>

		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<version>1.18.24</version>
			<scope>provided</scope>
		</dependency>

	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<version>${spring.boot.version}</version>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.1</version>
				<configuration>
					<source>${project.build.jdk}</source>
					<target>${project.build.jdk}</target>
					<encoding>${project.build.sourceEncoding}</encoding>
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-jar-plugin</artifactId>
				<version>2.4</version>
			</plugin>
		</plugins>
	</build>
</project>
```
In fact, this is similar to the official website of sharding-jdbc.

## 4. Configure data source
I want to make the database configurable, so instead of configuring the database directly in the `application.properties` file, I write it in the `database.json` file.

```json
[
  {
    "name": "db_bd",
    "url": "jdbc:mysql://localhost:3306/db_bd",
    "username": "root",
    "password": "root",
    "driveClassName":"com.mysql.jdbc.Driver"
  },
  {
    "name": "db_us",
    "url": "jdbc:mysql://localhost:3306/db_us",
    "username": "root",
    "password": "root",
    "driveClassName":"com.mysql.jdbc.Driver"
  }
]
```
Then read the database file in springboot, and the loading method is as follows:
```java
    @Value("classpath:database.json")
    private Resource databaseFile;

    @Bean
    public List<Database> databases() throws IOException {
        String databasesString = IOUtils.toString(databaseFile.getInputStream(), Charset.forName("UTF-8"));
        List<Database> databases = new Gson().fromJson(databasesString, new TypeToken<List<Database>>() {
        }.getType());
        return databases;
    }
```
After loading the database information, you can configure the logical database through the factory method:
```java
    @Bean
    public HashMap<String, DataSource> dataSourceMap(List<Database> databases) {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        for (Database database : databases) {
            DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
            dataSourceBuilder.url(database.getUrl());
            dataSourceBuilder.driverClassName(database.getDriveClassName());
            dataSourceBuilder.username(database.getUsername());
            dataSourceBuilder.password(database.getPassword());
            DataSource dataSource = dataSourceBuilder.build();
            dataSourceMap.put(database.getName(), dataSource);
        }
        return dataSourceMap;
    }
```
In this way, each logical database is loaded.

## 5. Configure sharding strategy

### 5.1 Database sharding strategy
In this example, the sub-database of the database is divided according to region - Bangladesh (bd) and USA (us), and in sharding-jdbc, 
it is a single-key sharding. You can implement the interface `Single Key Database Sharding Algorithm` according to the official documentation

```java
@service
public class DatabaseShardingAlgorithm implements SingleKeyDatabaseShardingAlgorithm<String> {
	/**
	 * Calculate shard result name set based on shard value and SQL's = operator.<br/>
	 *  <p>do Equal Sharding uses = as a conditional sharding key in WHERE. Use sharding Value.get Value() in the algorithm to get the value after equal =</p>
	 *
	 * @param availableTargetNames A collection of all available target names, typically data source or table names
	 * @param shardingValue        Sharding value
	 *
	 * @return The target name pointed to after sharding, usually the name of the data source or table
	 */
	@Override
	public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {
		String databaseName = "";
		for (String targetName : availableTargetNames) {
			if (targetName.endsWith(shardingValue.getValue())) {
				databaseName = targetName;
				break;
			}
		}
		return databaseName;
	}
}
```
There are two other methods in this interface, `do In Sharding` and `do Between Sharding`, because I don't use the IN and BETWEEN methods 
for the time being, so I didn't write them, and returned null directly.

### 5.2 Data table fragmentation strategy
The sharding strategy of the data table is jointly determined according to the currency and time. In sharding-jdbc, it is multi-key sharding. 
According to the official documentation, it is OK to implement the `Multiple Keys Table Sharding Algorithm` interface

```java
@service
public class TableShardingAlgorithm implements MultipleKeysTableShardingAlgorithm {
	/**
	 * Calculate shard result name set based on shard value.<br/>
	 *  <h3>Multi-shard key partition table algorithm</h3>
	 *  <p>
	 *      1. Get the value collection for each shard key;<br/>
	 *      2. Get the Cartesian product of a shard key-value set；<br/>
	 *      3. Assemble the table name first through the Cartesian product to match the target table；<br/>
	 *      4. Returns the set of matched target tables；<br/>
	 *  </p>
	 *  <h3>Precautions for the multi-shard key table-sharding algorithm</h3>
	 *  <p>
	 *      1. It needs to be the same as the single shard key sharding strategy, and the three cases of = , IN , between...and should be handled separately；<br/>
	 *      2. Mainly some shard keys cannot use between...and conditions；<br/>
	 *      3. In the multi-sharding key sharding algorithm, if one of the sharding keys is not included in the shardingValues, the target table cannot be hit 
	 *         (multiple sharding keys either all exist or none of them exist, and if some of them exist, it will arrive at The target table cannot be hit); 
	 *         therefore, it is necessary to deal with the existence of the shard key part in the SQL condition<br/>
	 *  </p>
	 *
	 * @param availableTargetNames A collection of all available target names, typically data source or table names
	 * @param shardingValues       Shard value collection
	 *
	 * @return The set of target names pointed to after sharding, usually the data source or table name
	 */
	@Override
	public Collection<String> doSharding(Collection<String> availableTargetNames, Collection<ShardingValue<?>> shardingValues) {
		Collection<String> result = new LinkedHashSet<>();
		Set<Object> nameValueSet = getShardingValue(shardingValues, "currency");
		Set<Object> timeValueSet = getShardingValue(shardingValues, "time");
		Set<List<Object>> valueResult = Sets.cartesianProduct(nameValueSet, timeValueSet);
		for (List<Object> value : valueResult) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM");
			java.sql.Date date = (java.sql.Date) value.get(1);
			String timeString = formatter.format(date);
			String suffix = (String) value.get(0) + "_" + timeString;

			for (String tableName : availableTargetNames) {
				if (tableName.endsWith(suffix)) {
					result.add(tableName);
				}
			}
		}
		if (result.isEmpty())
			log.info("[Table strategy] ------error, the shard key value in the current SQL misses the target database table-----");

		return result;
	}
}
```
The use of these methods can be found in the official documentation
### 5.3 Injection sharding strategy
The above only defines the sharding algorithm, but has not yet formed a strategy, and has not told sharding which field to assign to the sharding algorithm:
```
@Configuration
public class ShardingStrategyConfig {
    @Bean
    public DatabaseShardingStrategy databaseShardingStrategy(DatabaseShardingAlgorithm databaseShardingAlgorithm) {
        DatabaseShardingStrategy databaseShardingStrategy = new DatabaseShardingStrategy("region", databaseShardingAlgorithm);
        return databaseShardingStrategy;
    }

    @Bean
    public TableShardingStrategy tableShardingStrategy(TableShardingAlgorithm tableShardingAlgorithm) {
        Collection<String> columns = new LinkedList<>();
        columns.add("currency");
        columns.add("time");
        TableShardingStrategy tableShardingStrategy = new TableShardingStrategy(columns, tableShardingAlgorithm);
        return tableShardingStrategy;
    }
}
```
In this way, a completed sharding strategy can be formed

## 6. Configure the Data Source of Sharding-jdbc
The principle of sharding-jdbc is actually very simple. It is to create a Data Source for the upper-layer application to use. 
This Data Source contains all the logical libraries and logical tables. When adding, deleting, modifying and checking the application, 
he will modify the SQL by himself, and then select the appropriate database to continue. operate. So this Data Source creation is very important
```java
    @Bean
    @Primary
    public DataSource shardingDataSource(HashMap<String, DataSource> dataSourceMap, DatabaseShardingStrategy databaseShardingStrategy, TableShardingStrategy tableShardingStrategy) {
        DataSourceRule dataSourceRule = new DataSourceRule(dataSourceMap);
        TableRule tableRule = TableRule.builder("payment").actualTables(Arrays.asList("db_bd.tick_bdt_2017_01", "db_bd.tick_bdt_2017_02", "db_bd.tick_usd_2017_01", "db_bd.tick_usd_2017_02", "db_us.tick_bdt_2017_01", "db_us.tick_bdt_2017_02", "db_us.tick_usd_2017_01", "db_us.tick_bdt_2017_02")).dataSourceRule(dataSourceRule).build();
        ShardingRule shardingRule = ShardingRule.builder().dataSourceRule(dataSourceRule).tableRules(Arrays.asList(tableRule)).databaseShardingStrategy(databaseShardingStrategy).tableShardingStrategy(tableShardingStrategy).build();
        DataSource shardingDataSource = ShardingDataSourceFactory.createDataSource(shardingRule);
        return shardingDataSource;
    }
```
Here I want to focus on why the @Primary annotation is used. Without this annotation, an error will be reported. The general meaning 
of the error is that there are too many Data Sources, and mybatis does not know which one to use. Add this mybatis to know the Data Source of sharding. 
The reference here is the multi-data source configuration of jpa
## 7. Configure mybatis
### 7.1 Bean

```java
public class Payment {
	private long id;
	private String currency;
	private String region;
	private double amount;
	private int product;
	private Date time;
}
```
### 7.2 Mapper
Very simple, just implement an insert method
```java
@Mapper
public interface PaymentMapper {
	@Insert("insert into payment (id,currency,region,amount,product,time) values (#{id},#{currency},#{region},#{amount},#{product},#{time})")
	void insertPayment(Payment payment);
}
```
### 7.3 Session Factory configuration
Also set up Session Factory:
```
@Configuration
@MapperScan(basePackages = "com.spartajet.shardingboot.mapper", sqlSessionFactoryRef = "sessionFactory")
public class SessionFactoryConfig {
    @Bean
    public SqlSessionFactory sessionFactory(DataSource shardingDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(shardingDataSource);
        return sessionFactory.getObject();
    }

    @Bean
    public CommonSelfIdGenerator commonSelfIdGenerator() {
        CommonSelfIdGenerator.setClock(AbstractClock.systemClock());
        CommonSelfIdGenerator commonSelfIdGenerator = new CommonSelfIdGenerator();
        return commonSelfIdGenerator;
    }
}
```
A `Common Self Id Generator` is added here, the id generator that comes with sharding, and the code is similar to `facebook`'s `snowflake`. 
I don't want to set the primary key of the database to be self-incrementing, otherwise the two-way synchronization of the data will die miserably.
### 
## 8. Test

```
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class SpringbootShardingJdbcDemoApplicationTests {
    @Autowired
    private PaymentMapper paymentMapper;
    @Autowired
    private CommonSelfIdGenerator commonSelfIdGenerator;

    @Test
    public void insertTest() {
        Payment payment = new Payment(commonSelfIdGenerator.generateId().longValue(), "usd", "bd", 300, 100, getDate("2022-09-11"));
        this.paymentMapper.insertPayment(payment);
    }
}
```
Successfully implemented incremental database and table division.......
