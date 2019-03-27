package com.mileworks.gen.common.config;

import com.baomidou.mybatisplus.mapper.ISqlInjector;
import com.baomidou.mybatisplus.mapper.LogicSqlInjector;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.mileworks.gen.common.interceptor.SqlStatementInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.mileworks.gen.*.dao"})
public class MybatisPlusConfig {

	/**
	 * mybatis-plus分页插件<br>
	 * 文档：http://mp.baomidou.com<br>
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		paginationInterceptor.setLocalPage(true);// 开启 PageHelper 的支持
		return paginationInterceptor;
	}

	/**
	 * 配置 sql打印拦截器
	 * application.yml中 MK.showsql: true 时生效
	 *
	 * @return SqlStatementInterceptor
	 */
	@Bean
	@ConditionalOnProperty(name = "MK.showsql", havingValue = "true")
	SqlStatementInterceptor sqlStatementInterceptor() {
		return new SqlStatementInterceptor();
	}

	/**
   * 注入主键生成器
   */
//  @Bean
//  public IKeyGenerator keyGenerator(){
//      return new H2KeyGenerator();
//  }
//	
	/**
	 * 注入sql注入器
	 */
	@Bean
	public ISqlInjector sqlInjector() {
		return new LogicSqlInjector();
	}

}
