/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tencent.wii.generator.build.maven;

import com.tencent.wii.generator.build.BuildCustomizer;
import com.tencent.wii.generator.build.WiiAppDependencyBuild;
import com.tencent.wii.generator.common.Constant;
import io.spring.initializr.generator.buildsystem.BuildItemResolver;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.version.VersionReference;
import io.spring.initializr.metadata.InitializrConfiguration.Env.Maven;
import io.spring.initializr.metadata.InitializrMetadata;

import java.util.Objects;

/**
 * The default {@link Maven} {@link BuildCustomizer}.
 *
 * @author Stephane Nicoll
 */
public class WiiAppMavenBuildCustomizer extends WiiAppDependencyBuild<MavenBuild> implements BuildCustomizer<MavenBuild> {

    private final ProjectDescription description;

    private final InitializrMetadata metadata;

    private final BuildItemResolver buildItemResolver;

    public WiiAppMavenBuildCustomizer(ProjectDescription description, InitializrMetadata metadata, BuildItemResolver buildItemResolver) {
        this.description = description;
        this.metadata = metadata;
        this.buildItemResolver = buildItemResolver;
    }

    @Override
    public void customize(MavenBuild build) {
        MavenBuild appModel = new MavenBuild(buildItemResolver);

        project(appModel);
        parent(appModel);
        dependency(appModel);
        plugin(appModel);

        build.addModel(appModel);
    }

    @Override
    public int getOrder() {
        return 1;
    }

    private void project(MavenBuild build) {
        build.settings().artifact(description.getArtifactId() + "-app")
                .version(description.getVersion());
    }

    private void parent(MavenBuild build) {
        build.settings().parent(description.getGroupId(), description.getArtifactId(), description.getVersion());
    }

    private void dependency(MavenBuild build) {
        build(build, description);

        build.dependencies().add("spring-boot-starter-test", Dependency
                .withCoordinates(Constant.SPRINGBOOT_GROUP, "spring-boot-starter-test")
                .scope(DependencyScope.TEST_RUNTIME)
        );

        Dependency dependency = description.getRequestedDependencies().get("devtools");
        if (Objects.nonNull(dependency)) {
            build.dependencies().add("devtools", dependency);
        }
    }

    private void plugin(MavenBuild build) {
        build.plugins().add(Constant.SPRINGBOOT_GROUP,
                "spring-boot-maven-plugin",
                builder -> builder.version("${spring-boot.version}")
                        .configuration(configurationBuilder -> configurationBuilder
                        .add("mainClass", description.getPackageName() + "." + Constant.APPLICATION_CLASS_NAME))
                        .execution("repackage", executionBuilder -> executionBuilder.configuration(configurationBuilder -> configurationBuilder
                                .add("id", "repackage")
                                .add("goals", configurationBuilder1 -> configurationBuilder1
                                        .add("goal", "repackage"))))
        );
    }

    @Override
    protected void build(MavenBuild build, ProjectDescription description) {
        super.build(build, description);

        build.dependencies().add(description.getArtifactId() + "-api", Dependency
                .withCoordinates(description.getGroupId(), description.getArtifactId() + "-api")
                .version(VersionReference.ofValue(description.getVersion()))
                .build());

        build.dependencies().add(description.getArtifactId() + "-web", Dependency
                .withCoordinates(description.getGroupId(), description.getArtifactId() + "-web")
                .version(VersionReference.ofValue(description.getVersion()))
                .build());

        build.dependencies().add(description.getArtifactId() + "-oss", Dependency
                .withCoordinates(description.getGroupId(), description.getArtifactId() + "-oss")
                .version(VersionReference.ofValue(description.getVersion()))
                .build());
    }
}
