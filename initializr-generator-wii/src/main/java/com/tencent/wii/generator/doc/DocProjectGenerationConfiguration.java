package com.tencent.wii.generator.doc;

import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 *     文档项目生成器配置
 * </p>
 *
 * @author terryychen@tencent.com
 * @date 2021/1/15
 */
@ProjectGenerationConfiguration
public class DocProjectGenerationConfiguration {


    @Bean
    public ProjectContributor defaultDocContributor() {
        return new DefaultDocContributor();
    }
}
