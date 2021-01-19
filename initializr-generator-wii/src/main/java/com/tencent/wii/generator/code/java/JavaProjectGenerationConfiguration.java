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

package com.tencent.wii.generator.code.java;

import com.tencent.wii.generator.code.MainSourceCodeCustomizer;
import com.tencent.wii.generator.code.MainSourceCodeProjectContributor;
import com.tencent.wii.generator.code.ProjectSourceCode;
import com.tencent.wii.generator.code.TestSourceCodeCustomizer;
import com.tencent.wii.generator.code.TestSourceCodeProjectContributor;
import com.tencent.wii.generator.common.util.WiiModelUtil;
import io.spring.initializr.generator.condition.ConditionalOnLanguage;
import io.spring.initializr.generator.io.IndentingWriterFactory;
import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.java.JavaCompilationUnit;
import io.spring.initializr.generator.language.java.JavaLanguage;
import io.spring.initializr.generator.language.java.JavaSourceCode;
import io.spring.initializr.generator.language.java.JavaSourceCodeWriter;
import io.spring.initializr.generator.language.java.JavaTypeDeclaration;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Modifier;

/**
 * Configuration for contributions specific to the generation of a project that will use
 * Java as its language.
 *
 * @author Andy Wilkinson
 */
@ProjectGenerationConfiguration
@ConditionalOnLanguage(JavaLanguage.ID)
public class JavaProjectGenerationConfiguration {

    private final ProjectDescription description;

    private final IndentingWriterFactory indentingWriterFactory;

    public JavaProjectGenerationConfiguration(ProjectDescription description,
                                              IndentingWriterFactory indentingWriterFactory) {
        this.description = description;
        this.indentingWriterFactory = indentingWriterFactory;
    }

    @Bean
    public MainSourceCodeProjectContributor<JavaTypeDeclaration, JavaCompilationUnit, ?> mainJavaSourceCodeProjectContributor(
            ObjectProvider<MainSourceCodeCustomizer<?, ?, ?>> mainSourceCodeCustomizers) {
        return new MainSourceCodeProjectContributor<>(this.description,
                        ProjectSourceCode::new,
                        new JavaSourceCodeWriter(this.indentingWriterFactory),
                        mainSourceCodeCustomizers);
    }


    @Bean
    public MainSourceCodeCustomizer<JavaTypeDeclaration,
            JavaCompilationUnit,
            ProjectSourceCode<JavaTypeDeclaration, JavaCompilationUnit>> wiiWebSourceCodeCustomizer() {
        return sourceCode -> {
            JavaSourceCode javaSourceCode = new JavaSourceCode();
            sourceCode.addModeSourceCode(WiiModelUtil.webName(description), javaSourceCode);
            String rootPackage = description.getPackageName();
            String webPkg = rootPackage + ".web";
            javaSourceCode.createCompilationUnit(rootPackage + ".web", "package-info");

            JavaCompilationUnit compilationUnit = javaSourceCode.createCompilationUnit(webPkg + ".v1.controller",
                    "IndexController");
            JavaTypeDeclaration declaration = compilationUnit.createTypeDeclaration("IndexController");
            declaration.modifiers(Modifier.PUBLIC);
            declaration.annotate(Annotation.name("org.springframework.stereotype.Controller"));
            declaration.annotate(Annotation.name("org.springframework.web.bind.annotation.RequestMapping",
                    builder -> builder.attribute("value", String.class, "web")));
        };
    }

    @Bean
    public MainSourceCodeCustomizer<JavaTypeDeclaration,
            JavaCompilationUnit,
            ProjectSourceCode<JavaTypeDeclaration, JavaCompilationUnit>> wiiOssSourceCodeCustomizer() {
        return sourceCode -> {
            JavaSourceCode javaSourceCode = new JavaSourceCode();
            sourceCode.addModeSourceCode(WiiModelUtil.ossName(description), javaSourceCode);
            String rootPackage = description.getPackageName();
            String pkg = rootPackage + ".oss";
            javaSourceCode.createCompilationUnit(pkg, "package-info");

            JavaCompilationUnit compilationUnit = javaSourceCode.createCompilationUnit(pkg + ".v1.controller",
                    "AdminController");
            JavaTypeDeclaration declaration = compilationUnit.createTypeDeclaration("AdminController");
            declaration.modifiers(Modifier.PUBLIC);
            declaration.annotate(Annotation.name("org.springframework.web.bind.annotation.RestController"));
            declaration.annotate(Annotation.name("org.springframework.web.bind.annotation.RequestMapping",
                    builder -> builder.attribute("value", String.class, "oss/v1/admin")));
        };
    }

    @Bean
    public MainSourceCodeCustomizer<JavaTypeDeclaration,
            JavaCompilationUnit,
            ProjectSourceCode<JavaTypeDeclaration, JavaCompilationUnit>> wiiApiSourceCodeCustomizer() {
        return new WiiApiMainSourceCodeCustomizer(description);
    }

    @Bean
    public MainSourceCodeCustomizer<JavaTypeDeclaration,
            JavaCompilationUnit,
            ProjectSourceCode<JavaTypeDeclaration, JavaCompilationUnit>> wiiAppSourceCodeCustomizer() {
        return new WiiAppMainSourceCodeCustomizer(description);
    }

    @Bean
    public MainSourceCodeCustomizer<JavaTypeDeclaration,
            JavaCompilationUnit,
            ProjectSourceCode<JavaTypeDeclaration, JavaCompilationUnit>> wiiCoreSourceCodeCustomizer() {
        return new WiiCoreMainSourceCodeCustomizer(description);
    }


    @Bean
    public TestSourceCodeProjectContributor<JavaTypeDeclaration, JavaCompilationUnit, ?> testJavaSourceCodeProjectContributor(
            ObjectProvider<TestSourceCodeCustomizer<?, ?, ?>> testSourceCodeCustomizers) {
        return new TestSourceCodeProjectContributor<>(this.description,
                ProjectSourceCode::new,
                new JavaSourceCodeWriter(this.indentingWriterFactory),
                testSourceCodeCustomizers);
    }

}
