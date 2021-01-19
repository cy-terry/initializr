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

package com.tencent.wii.generator.configuration;

import com.tencent.wii.generator.common.Constant;
import com.tencent.wii.generator.common.util.WiiModelUtil;
import io.spring.initializr.generator.language.SourceStructure;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.SingleResourceProjectContributor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A {@link SingleResourceProjectContributor} that contributes a
 * {@code application.properties} file to a project.
 *
 * @author Stephane Nicoll
 */
public class WiiApplicationPropertiesContributor extends SingleResourceProjectContributor {

	private final ProjectDescription description;

	public WiiApplicationPropertiesContributor(ProjectDescription description) {
		this("classpath:configuration/application.yml", description);
	}

	public WiiApplicationPropertiesContributor(String resourcePattern, ProjectDescription description) {
		super("src/main/resources/application.yml", resourcePattern);
		this.description = description;
	}

	@Override
	public void contribute(Path projectRoot) throws IOException {
		Path appPath = projectRoot.resolve(WiiModelUtil.appName(description));
		super.contribute(appPath);
		SourceStructure sourceStructure = description.getBuildSystem().getMainSource(appPath, description.getLanguage());

		if (!Files.exists(sourceStructure.getResourcesDirectory())) {
			Files.createDirectories(sourceStructure.getResourcesDirectory());
		}

		if (description.getRequestedDependencies().containsKey(Constant.MYBATIS_ID)) {
			Path mapper = sourceStructure.getResourcesDirectory().resolve("mapper");
			if (!Files.exists(mapper)) {
				Files.createDirectories(mapper);
			}
		}
	}
}
