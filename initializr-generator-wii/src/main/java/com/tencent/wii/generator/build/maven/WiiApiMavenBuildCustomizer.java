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
import io.spring.initializr.generator.buildsystem.BuildItemResolver;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.metadata.InitializrConfiguration.Env.Maven;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * The default {@link Maven} {@link BuildCustomizer}.
 *
 * @author Stephane Nicoll
 */
public class WiiApiMavenBuildCustomizer extends WiiAppDependencyBuild<MavenBuild> implements BuildCustomizer<MavenBuild> {

    private final ProjectDescription description;

    private final InitializrMetadata metadata;

    private final BuildItemResolver buildItemResolver;

    public WiiApiMavenBuildCustomizer(ProjectDescription description, InitializrMetadata metadata, BuildItemResolver buildItemResolver) {
        this.description = description;
        this.metadata = metadata;
        this.buildItemResolver = buildItemResolver;
    }

    @Override
    public void customize(MavenBuild build) {
        MavenBuild apiModel = new MavenBuild(buildItemResolver);

        project(apiModel);
        parent(apiModel);
        dependency(apiModel);

        build.addModel(apiModel);
    }

    @Override
    public int getOrder() {
        return 1;
    }

    private void project(MavenBuild build) {
        build.settings().artifact(description.getArtifactId() + "-api")
            .version(description.getVersion());
    }

    private void parent(MavenBuild build) {
        build.settings().parent(description.getGroupId(), description.getArtifactId(), description.getVersion());
    }

    private void dependency(MavenBuild build) {
        super.build(build, description);

    }
}
