flyway数据库版本控制

I、官网：https://flywaydb.org/

II、关于SQL脚本
SQL脚本默认放在./src/resources/db/migration,可以通过spring.flyway.locations修改
SQL脚本命名规范如下：
1、Prefix 前缀：V 代表版本迁移，U 代表撤销迁移，R 代表可重复迁移
2、Version 版本号：版本号通常 . 和整数组成
3、Separator 分隔符：固定由两个下划线 __ 组成
4、Description 描述：由下划线分隔的单词组成，用于描述本次迁移的目的
5、Suffix 后缀：如果是 SQL 文件那么固定由 .sql 组成，如果是基于 Java 类则默认不需要后缀

III、版本管理表
系统会自动创建flyway_schema_history用于记录所有版本演化和状态，其表结构如下(以 MySQL 为例)：
Field	             Type	        Null	    Key	    Default
version_rank	    int(11)	        NO	        MUL	    NULL
installed_rank	    int(11)	        NO	        MUL	    NULL
version	            varchar(50)	    NO	        PRI	    NULL
description	        varchar(200)	NO		            NULL
type	            varchar(20)	    NO		            NULL
script	            varchar(1000)	NO		            NULL
checksum	        int(11)         YES		            NULL
installed_by	    varchar(100)	NO		            NULL
installed_on	    timestamp	    NO		            CURRENT_TIMESTAMP
execution_time	    int(11)	        NO		            NULL
success	            tinyint(1)	    NO	        MUL	    NULL

其中：
checksum：Flyway 会给脚本计算一个 checksum 保存在数据库中，用于在之后运行过程中对比 sql 文件是否有变化，
          如果发生了变化，则会报错，也就防止了误修改脚本导致发生问题。

IV、flyway事务
默认情况下，Flyway总是将整个迁移的执行封装在单个事务中。也可以设置group属性等于true，将未执行的迁移封装到一个事务中。
对于SQL迁移，可以指定脚本配置属性executeInTransaction。

V、Schema creation

如果createSchemas设置为false，可能出现如下情况：
`flyway.createSchemas=false
flyway.schemas=my_schema`
1、运行迁移
2、my_schema 不是由Flyway创建的
3、由于my_schema是默认Schema，因此Flyway尝试在my_schema创建架构历史记录表
4、my_schema不存在，因此操作失败
因此，当切换createSchemas到时false，建议进行以下设置：
将默认Schema设置为 flyway_history_schema，通过设置defaultSchema或将其放在schemas配置选项中的第一位
设置initSql为创建flyway_history_schema（如果不存在）
将其他架构放在schemas属性中
`flyway.createSchemas=false
flyway.initSql=CREATE IF NOT EXISTS flyway_history_schema
flyway.schemas=flyway_history_schema,my_schema`

此时，将发生以下情况：
1、运行迁移
2、initSql被执行，所以flyway_history_schema被创建
3、由于flyway_history_schema是默认schema，因此Flyway尝试在flyway_history_schema创建架构历史记录表
4、my_schema 不是由Flyway创建的
5、迁移正常进行
6、迁移可自由控制my_schema的创建

VI、Migration States（迁移状态）

1、迁移已解决或已应用。Flyway的文件系统和类路径扫描程序已检测到已解决的迁移。最初，它们是未决的。一旦对数据库执行了它们，它们就会被应用。
2、迁移成功后，在Flyway的架构历史记录表中将其标记为成功（success）。
3、当迁移失败并且数据库支持DDL事务时，将回滚迁移，并且架构历史记录表中不会记录任何内容。
4、如果迁移失败并且数据库不支持DDL事务，则在模式历史记录表中将迁移标记为失败（failed），表明可能需要手动清理数据库。
5、已被撤消迁移撤消其影响的版本化迁移被标记为“撤消”（undone）。
6、自从上次应用以来，其校验和已更改的可重复迁移被标记为过时（outdated），直到再次执行它们为止。另请注意，更改占位符的值将导致可重复的迁移被视为过时（outdated）的迁移。
7、当Flyway发现所应用的版本迁移的版本高于已知的最高版本时（通常是在较新版本的软件已迁移该架构时发生），该迁移被标记为未来（future）。


