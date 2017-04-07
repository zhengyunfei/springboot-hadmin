<h1>Sparrow框架之Hadmin</h1><br>
<h3>系统管理后台基本框架</h3><br>
        1.springboot
        2.springdata jpa
        3.shiro
        4.freemarker
    功能列表
    1.用户管理
        1.1添加用户
        1.2编辑用户
        1.3删除用户
        1.4查询
        1.5关联角色
    2.角色管理
      2.1新增角色
      2.2编辑角色
      2.3删除角色
      2.4查询角色
      2.5分配资源
    3.资源链接管理模块
       3.1新增资源链接
       3.2编辑资源链接
       3.3删除资源链接
       3.4查询资源链接
   <br>
系统中对springdata的查询条件Specification做了简单的封装，更加方便查询条件的灵活使用。<br>
4、前端技术：<br>
    1.使用Hadmin系统模版<br>
    2.数据表格使用bootstrap table插件<br>
    3.弹窗使用layer插件<br>
    4.日期选择使用laydate插件。
    5.表单验证使用jQuery validate插件等等<br>
5、系统部署：
    1）使用mysql数据库，先建立一个空数据库base，最好编码使用utf-8字符集，不然会乱码。
    2）把application.properties中的数据库连接信息修改成自己数据库的连接信息。
    3）修改spring.jpa.hibernate.ddl-auto为create，目的是让系统自动建表同时初始化相关集成数据。如果不需要自动初始化数据，可以删除resource目录下的import.sql文件。
6、系统启动后，访问：127.0.0.1/admin/会自动跳转到后台登录页面。
7、初始用户名和密码为：admin/111111。
