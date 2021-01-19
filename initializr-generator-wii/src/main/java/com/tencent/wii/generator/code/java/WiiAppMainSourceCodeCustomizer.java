package com.tencent.wii.generator.code.java;

import com.tencent.wii.generator.code.MainSourceCodeCustomizer;
import com.tencent.wii.generator.code.ProjectSourceCode;
import com.tencent.wii.generator.common.Constant;
import com.tencent.wii.generator.common.util.WiiModelUtil;
import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.Parameter;
import io.spring.initializr.generator.language.java.JavaCompilationUnit;
import io.spring.initializr.generator.language.java.JavaExpressionStatement;
import io.spring.initializr.generator.language.java.JavaMethodDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodInvocation;
import io.spring.initializr.generator.language.java.JavaSourceCode;
import io.spring.initializr.generator.language.java.JavaTypeDeclaration;
import io.spring.initializr.generator.project.ProjectDescription;

import java.lang.reflect.Modifier;

/**
 * <p> </p>
 *
 * @author terryychen@tencent.com
 * @date 2021/1/19
 */
public class WiiAppMainSourceCodeCustomizer
        implements MainSourceCodeCustomizer<JavaTypeDeclaration,
        JavaCompilationUnit, ProjectSourceCode<JavaTypeDeclaration, JavaCompilationUnit>> {

    private final ProjectDescription description;

    public WiiAppMainSourceCodeCustomizer(ProjectDescription description) {
        this.description = description;
    }

    @Override
    public void customize(ProjectSourceCode<JavaTypeDeclaration, JavaCompilationUnit> sourceCode) {
        JavaSourceCode javaSourceCode = new JavaSourceCode();
        sourceCode.addModeSourceCode(WiiModelUtil.appName(description), javaSourceCode);
        String pkg = description.getPackageName();
        javaSourceCode.createCompilationUnit(pkg, "package-info");
        javaSourceCode.createCompilationUnit(pkg + ".config", "package-info");

        JavaCompilationUnit compilationUnit = javaSourceCode.createCompilationUnit(pkg, Constant.APPLICATION_CLASS_NAME);
        JavaTypeDeclaration typeDeclaration = compilationUnit.createTypeDeclaration(Constant.APPLICATION_CLASS_NAME);
        typeDeclaration.modifiers(Modifier.PUBLIC);
        typeDeclaration.annotate(Annotation.name("org.springframework.boot.autoconfigure.SpringBootApplication",
                builder -> builder.attribute("scanBasePackages", String.class, description.getPackageName())));
        if (description.getRequestedDependencies().containsKey(Constant.MYBATIS_ID)) {
            typeDeclaration.annotate(Annotation.name("org.mybatis.spring.annotation.MapperScan",
                    builder -> builder.attribute("basePackages", String.class, description.getPackageName() + ".core.mapper")));
        }
        typeDeclaration.addMethodDeclaration(
                JavaMethodDeclaration.method("main").modifiers(Modifier.PUBLIC | Modifier.STATIC).returning("void")
                        .parameters(new Parameter("java.lang.String[]", "args"))
                        .body(new JavaExpressionStatement(
                                new JavaMethodInvocation("org.springframework.boot.SpringApplication", "run",
                                        typeDeclaration.getName() + ".class", "args"))));
    }
}
