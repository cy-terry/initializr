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

package com.tencent.wii.generator.code;

import io.spring.initializr.generator.language.CompilationUnit;
import io.spring.initializr.generator.language.SourceCode;
import io.spring.initializr.generator.language.SourceCodeWriter;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.util.LambdaSafe;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * {@link ProjectContributor} for the application's main source code.
 *
 * @param <T> language-specific type declaration
 * @param <C> language-specific compilation unit
 * @param <S> language-specific source code
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 */
public class MainSourceCodeProjectContributor<T extends TypeDeclaration,
        C extends CompilationUnit<T>,
        S extends ProjectSourceCode<T, C>>
        implements ProjectContributor {

    private final ProjectDescription description;

    private final Supplier<S> sourceFactory;

    private final SourceCodeWriter<SourceCode<T, C>> sourceWriter;

    private final ObjectProvider<MainSourceCodeCustomizer<?, ?, ?>> codeCustomizers;

    @SuppressWarnings("unchecked")
    public MainSourceCodeProjectContributor(ProjectDescription description,
                                            Supplier<S> sourceFactory,
                                            SourceCodeWriter<? extends SourceCode<T, C>> sourceWriter,
                                            ObjectProvider<MainSourceCodeCustomizer<?, ?, ?>> codeCustomizers) {
        this.description = description;
        this.sourceFactory = sourceFactory;
        this.sourceWriter = (SourceCodeWriter<SourceCode<T, C>>) sourceWriter;
        this.codeCustomizers = codeCustomizers;
    }

    @Override
    public void contribute(Path projectRoot) throws IOException {
        S projectSourceCode = this.sourceFactory.get();
        if (Objects.nonNull(projectSourceCode.projectSourceCode())) {
            this.sourceWriter.writeTo(
                    this.description
                            .getBuildSystem()
                            .getMainSource(projectRoot, this.description.getLanguage()),
                    projectSourceCode.projectSourceCode());
        }
        customize(projectSourceCode);
        for (Map.Entry<String, ProjectSourceCode.ModeSourceCode<T, C>> entry : projectSourceCode.modelCodeContainer().entrySet()) {
            SourceCode<T, C> sourceCode = entry.getValue().getSourceCode();
            this.sourceWriter.writeTo(
                    this.description
                            .getBuildSystem()
                            .getMainSource(projectRoot.resolve(entry.getValue().getModel()), this.description.getLanguage()),
                    sourceCode);
        }
    }

    @SuppressWarnings("unchecked")
    private void customize(S projectSourceCode) {
        List<MainSourceCodeCustomizer<?, ?, ?>> customizers = this.codeCustomizers.orderedStream()
                .collect(Collectors.toList());
        LambdaSafe.callbacks(MainSourceCodeCustomizer.class, customizers, projectSourceCode)
                .invoke((customizer) -> customizer.customize(projectSourceCode));
    }

}
