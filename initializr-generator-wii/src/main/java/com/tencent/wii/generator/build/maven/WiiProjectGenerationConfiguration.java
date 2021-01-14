package com.tencent.wii.generator.build.maven;

import com.tencent.wii.generator.build.BuildCustomizer;
import io.spring.initializr.generator.buildsystem.BuildItemResolver;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnPackaging;
import io.spring.initializr.generator.io.IndentingWriterFactory;
import io.spring.initializr.generator.packaging.war.WarPackaging;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.util.LambdaSafe;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p> </p>
 *
 * @author terryychen@tencent.com
 * @date 2021/1/14
 */
@ProjectGenerationConfiguration
@ConditionalOnBuildSystem(MavenBuildSystem.ID)
public class WiiProjectGenerationConfiguration {

    @Bean
    public MavenWrapperContributor mavenWrapperContributor() {
        return new MavenWrapperContributor();
    }

    @Bean
    public MavenBuild mavenBuild(ObjectProvider<BuildItemResolver> buildItemResolver,
                                                                                  ObjectProvider<BuildCustomizer<?>> buildCustomizers) {
        return createBuild(buildItemResolver.getIfAvailable(),
                buildCustomizers.orderedStream().collect(Collectors.toList()));
    }

    private MavenBuild createBuild(BuildItemResolver buildItemResolver, List<BuildCustomizer<?>> buildCustomizers) {
        MavenBuild build = (buildItemResolver != null) ? new MavenBuild(buildItemResolver) : new MavenBuild();
        LambdaSafe.callbacks(BuildCustomizer.class, buildCustomizers, build)
                .invoke((customizer) -> customizer.customize(build));
        return build;
    }

    @Bean
    public MavenBuildProjectContributor mavenBuildProjectContributor(MavenBuild build,
                                                                     IndentingWriterFactory indentingWriterFactory) {
        return new MavenBuildProjectContributor(build, indentingWriterFactory);
    }

    @Bean
    @ConditionalOnPackaging(WarPackaging.ID)
    public BuildCustomizer<MavenBuild> mavenWarPackagingConfigurer() {
        return (build) -> build.settings().packaging("war");
    }
}
