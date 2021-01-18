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

import com.tencent.wii.generator.build.BillOfMaterials;
import com.tencent.wii.generator.build.BuildCustomizer;
import com.tencent.wii.generator.common.Constant;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.MavenRepository;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.version.VersionReference;
import io.spring.initializr.metadata.InitializrConfiguration.Env.Maven;
import io.spring.initializr.metadata.InitializrConfiguration.Env.Maven.ParentPom;
import io.spring.initializr.metadata.InitializrMetadata;

import java.util.Objects;

/**
 * The default {@link Maven} {@link BuildCustomizer}.
 *
 * @author Stephane Nicoll
 */
public class WiiParentMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

    private final ProjectDescription description;

    private final InitializrMetadata metadata;

    public WiiParentMavenBuildCustomizer(ProjectDescription description, InitializrMetadata metadata) {
        this.description = description;
        this.metadata = metadata;
    }

    @Override
    public void customize(MavenBuild build) {
        project(build);
        property(build);
        parent(build);
        repository(build);
        bom(build);
        plugin(build);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private void project(MavenBuild build) {
        build.settings().name(this.description.getName())
                .description(this.description.getDescription())
                .packaging("pom")
                .version(description.getVersion())
                .group(description.getGroupId())
                .artifact(description.getArtifactId());
    }

    private void property(MavenBuild build) {
        String springBootVersion = this.description.getPlatformVersion().toString();
        build.properties().property("java.version", this.description.getLanguage().jvmVersion())
                .property("project.build.sourceEncoding", "UTF-8")
                .property("project.reporting.outputEncoding", "UTF-8")
                .property("org.apache.maven.plugins", "maven-compiler-plugin")
                .property("spring-boot.version", springBootVersion);
    }

    private void parent(MavenBuild build) {
        String springBootVersion = this.description.getPlatformVersion().toString();
        Maven maven = this.metadata.getConfiguration().getEnv().getMaven();
        ParentPom parentPom = maven.resolveParentPom(springBootVersion);
        if (!maven.isSpringBootStarterParent(parentPom)) {
            build.settings().parent(parentPom.getGroupId(), parentPom.getArtifactId(), parentPom.getVersion());
        }
    }

    private void repository(MavenBuild build) {
        build.repositories().add(MavenRepository
                .withIdAndUrl("wii-repo", "https://wii.coding.net/public-artifacts/wii-spring/repo"));
    }

    private void bom(MavenBuild build) {
        BillOfMaterials.Builder builder = BillOfMaterials.withCoordinates("com.tencent.wii",
                "wii-dependencies",
                BillOfMaterials.DEFAULT_TYPE,
                BillOfMaterials.DEFAULT_SCOPE);
        builder.version(VersionReference.ofValue("${spring-boot.version}"));
        builder.order(0);
        build.boms().add("wii-dependencies", builder);

        BillOfMaterials.Builder junit = BillOfMaterials.withCoordinates("junit", "junit", Constant.EMPTY_STR, "test");
        junit.version(VersionReference.ofValue("4.13"));
        junit.order(1);
        build.boms().add("junit", junit);

        BillOfMaterials.Builder actuator = BillOfMaterials.withCoordinates(Constant.SPRINGBOOT_GROUP,
                "spring-boot-starter-actuator");
        actuator.version(VersionReference.ofValue("${spring-boot.version}"));
        actuator.order(2);
        build.boms().add("spring-boot-starter-actuator", actuator);

        BillOfMaterials.Builder validation = BillOfMaterials.withCoordinates(Constant.SPRINGBOOT_GROUP,
                "spring-boot-starter-validation");
        validation.version(VersionReference.ofValue("${spring-boot.version}"));
        validation.order(3);
        build.boms().add("spring-boot-starter-validation", validation);

        BillOfMaterials.Builder swagger2 = BillOfMaterials.withCoordinates("io.springfox", "springfox-swagger2");
        swagger2.version(VersionReference.ofValue("2.9.2"));
        swagger2.order(4);
        build.boms().add("springfox-swagger2", swagger2);

        BillOfMaterials.Builder swaggerUi = BillOfMaterials.withCoordinates("io.springfox", "springfox-swagger-ui");
        swaggerUi.version(VersionReference.ofValue("2.9.2"));
        swaggerUi.order(5);
        build.boms().add("springfox-swagger-ui", swaggerUi);

        // wii-core
        Dependency dependency = description.getRequestedDependencies().get("wii-core");
        if (Objects.nonNull(dependency)) {
            BillOfMaterials.Builder wiiCore = BillOfMaterials.withCoordinates(dependency.getGroupId(), dependency.getArtifactId());
            wiiCore.version(dependency.getVersion());
            wiiCore.order(1);
            build.boms().add("wii-core", wiiCore);
        }

        String projectCore = description.getArtifactId() + "-core";
        BillOfMaterials.Builder wiiProjectCore = BillOfMaterials.withCoordinates(description.getGroupId(), projectCore);
        wiiProjectCore.version(VersionReference.ofValue(description.getVersion()));
        wiiProjectCore.order(1);
        build.boms().add(projectCore, swaggerUi);

    }

    private void plugin(MavenBuild build) {
        build.plugins().add("org.apache.maven.plugins",
                "maven-compiler-plugin",
                builder -> builder.version("3.8.1")
                        .configuration(configurationBuilder -> {
                            configurationBuilder.add("source", "${java.version}");
                            configurationBuilder.add("target", "${java.version}");
                            configurationBuilder.add("encoding", "${project.build.sourceEncoding}");
                        }));
    }
}
