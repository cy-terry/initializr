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

import io.spring.initializr.generator.io.IndentingWriterFactory;
import io.spring.initializr.generator.project.contributor.ProjectContributor;

import java.io.IOException;
import java.nio.file.Path;

/**
 * {@link ProjectContributor} to contribute the files for a {@link MavenBuild}.
 *
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 */
public class MavenModelBuildProjectContributor implements ProjectContributor {

	private final MavenBuild build;

	private final IndentingWriterFactory indentingWriterFactory;

	private final MavenBuildWriter buildWriter;

	public MavenModelBuildProjectContributor(MavenBuild build, IndentingWriterFactory indentingWriterFactory) {
		this.build = build;
		this.indentingWriterFactory = indentingWriterFactory;
		this.buildWriter = new MavenBuildWriter();
	}

	@Override
	public void contribute(Path projectRoot) throws IOException {
		buildWriter.writeAllTo(indentingWriterFactory, projectRoot, build);
	}
}
