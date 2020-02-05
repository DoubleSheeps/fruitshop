# fruitshop
网上商城项目
记录：
2020.2.4
generatorConfiguration配置文件中添加属性：
<jdbcConnection
          ......
          <property name="nullCatalogMeansCurrent" value="true" />
</jdbcConnection>
防止自动生成Mapper文件收到数据库同名数据表的干扰


2020.2.5
DOMapper.xml文件中insert操作添加属性：
useGeneratedKeys="true" keyProperty="id"
可设置主键为数据表中自增的id，用于返回数据表自增id的值
