package com.cgf.project;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProjectApplicationTests {

	//DI注入数据源
//	@Autowired
//	DataSource dataSource;

	//@Test
	//public void pyTest() throws Exception{
	//	PythonUtils pythonUtils = new PythonUtils();
	//	pythonUtils.cmdPython("Java","51job");
	//}

//	@Test
//	public void contextLoads() throws SQLException {
//		//看一下默认数据源
//		System.out.println(dataSource.getClass());
//		//获得连接
//		Connection connection =   dataSource.getConnection();
//		System.out.println(connection);
//
//		DruidDataSource druidDataSource = (DruidDataSource) dataSource;
//		System.out.println("druidDataSource 数据源最大连接数：" + druidDataSource.getMaxActive());
//		System.out.println("druidDataSource 数据源初始化连接数：" + druidDataSource.getInitialSize());
//
//		//关闭连接
//		connection.close();
//	}
//	@Test
//	public void test() throws Exception{
//		List<Users>  users = userMapper.findAllUser();
//		for (Users user:users){
//			System.out.println(user.toString());
//		}
//	}
}
