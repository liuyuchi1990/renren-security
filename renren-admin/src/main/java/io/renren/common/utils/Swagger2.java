package io.renren.common.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 {

	/**
	 * 
	 * 启动Swagger及相关配置<br>
	 * <p>Title:createRestApi<br>
	 *   
	 * @return Docket    返回类型 
	 * @since JDK 1.8
	 */
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				// 扫描指定目录下的api
				.apis(RequestHandlerSelectors.basePackage("io.renren.modules")).paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Credit Review RESTful APIs")
				.description("Credit Review Online RESTful APIs Desgin").version("1.0").build();
	}
}