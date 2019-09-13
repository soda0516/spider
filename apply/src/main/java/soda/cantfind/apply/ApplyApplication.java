package soda.cantfind.apply;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = {"soda.cantfind.apply","soda.module"},exclude = SecurityAutoConfiguration.class)
@MapperScan(basePackages = {"soda.cantfind.apply.mapper","soda.module.user.mapper"})
@EnableSwagger2
// TODO: 2019/9/2 明天的任务是，添加一个下载excel的功能，数据库要增加字段，任务列表里要与关联任务list表里task id这个信息
public class ApplyApplication {
    private static Logger logger = LoggerFactory.getLogger(ApplyApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ApplyApplication.class, args);
    }
    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
