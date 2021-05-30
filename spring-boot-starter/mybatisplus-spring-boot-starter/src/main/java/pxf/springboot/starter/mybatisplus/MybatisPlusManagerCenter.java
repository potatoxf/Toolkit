package pxf.springboot.starter.mybatisplus;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis-Plus管理中心
 *
 * @author potatoxf
 * @date 2021/5/8
 */
@Configuration
@ConditionalOnMissingBean(MybatisPlusAutoConfiguration.class)
public class MybatisPlusManagerCenter {}
