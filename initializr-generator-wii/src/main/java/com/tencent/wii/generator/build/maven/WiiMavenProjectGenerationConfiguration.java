package com.tencent.wii.generator.build.maven;

import com.tencent.wii.generator.build.BuildCustomizer;
import io.spring.initializr.generator.buildsystem.BuildItemResolver;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.io.IndentingWriterFactory;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.metadata.InitializrMetadata;
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
public class WiiMavenProjectGenerationConfiguration {

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
    public MavenModelBuildProjectContributor mavenBuildProjectContributor(MavenBuild build,
                                                                          IndentingWriterFactory indentingWriterFactory) {
        return new MavenModelBuildProjectContributor(build, indentingWriterFactory);
    }

    @Bean
    public BuildCustomizer<MavenBuild> wiiParentMavenBuildConfigurer(ProjectDescription description,
                                                                  InitializrMetadata metadata) {
        return new WiiParentMavenBuildCustomizer(description, metadata);
    }

    @Bean
    public BuildCustomizer<MavenBuild> wiiWebMavenBuildConfigurer(ProjectDescription description,
                                                                  InitializrMetadata metadata,
                                                                  ObjectProvider<BuildItemResolver> buildItemResolver) {
        return new WiiWebMavenBuildCustomizer(description, metadata, buildItemResolver.getIfAvailable());
    }

    @Bean
    public BuildCustomizer<MavenBuild> wiiOssMavenBuildConfigurer(ProjectDescription description,
                                                                  InitializrMetadata metadata,
                                                                  ObjectProvider<BuildItemResolver> buildItemResolver) {
        return new WiiOssMavenBuildCustomizer(description, metadata, buildItemResolver.getIfAvailable());
    }

    @Bean
    public BuildCustomizer<MavenBuild> wiiApiMavenBuildConfigurer(ProjectDescription description,
                                                                  InitializrMetadata metadata,
                                                                  ObjectProvider<BuildItemResolver> buildItemResolver) {
        return new WiiApiMavenBuildCustomizer(description, metadata, buildItemResolver.getIfAvailable());
    }

    @Bean
    public BuildCustomizer<MavenBuild> wiiAppMavenBuildConfigurer(ProjectDescription description,
                                                                   InitializrMetadata metadata,
                                                                   ObjectProvider<BuildItemResolver> buildItemResolver) {
        return new WiiAppMavenBuildCustomizer(description, metadata, buildItemResolver.getIfAvailable());
    }

    @Bean
    public BuildCustomizer<MavenBuild> wiiCoreMavenBuildConfigurer(ProjectDescription description,
                                                                  InitializrMetadata metadata,
                                                                  ObjectProvider<BuildItemResolver> buildItemResolver) {
        return new WiiCoreMavenBuildCustomizer(description, metadata, buildItemResolver.getIfAvailable());
    }
}
