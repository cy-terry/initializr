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
public class WiiCoreMainSourceCodeCustomizer
        implements MainSourceCodeCustomizer<JavaTypeDeclaration,
        JavaCompilationUnit, ProjectSourceCode<JavaTypeDeclaration, JavaCompilationUnit>> {

    private final ProjectDescription description;

    public WiiCoreMainSourceCodeCustomizer(ProjectDescription description) {
        this.description = description;
    }

    @Override
    public void customize(ProjectSourceCode<JavaTypeDeclaration, JavaCompilationUnit> sourceCode) {
        JavaSourceCode javaSourceCode = new JavaSourceCode();
        sourceCode.addModeSourceCode(WiiModelUtil.coreName(description), javaSourceCode);
        String pkg = description.getPackageName() + ".core";
        javaSourceCode.createCompilationUnit(pkg, "package-info");
        javaSourceCode.createCompilationUnit(pkg + ".config", "package-info");
        javaSourceCode.createCompilationUnit(pkg + ".annotation", "package-info");
        if (description.getRequestedDependencies().containsKey(Constant.VALIDATION_ID)) {
            javaSourceCode.createCompilationUnit(pkg + ".annotation.validator", "package-info");
        }

        javaSourceCode.createCompilationUnit(pkg + ".constant", "package-info");
        javaSourceCode.createCompilationUnit(pkg + ".constant.consist", "package-info");
        javaSourceCode.createCompilationUnit(pkg + ".constant.enums", "package-info");


        javaSourceCode.createCompilationUnit(pkg + ".exception", "package-info");
        javaSourceCode.createCompilationUnit(pkg + ".filter", "package-info");
        if (description.getRequestedDependencies().containsKey(Constant.MYBATIS_ID)) {
            javaSourceCode.createCompilationUnit(pkg + ".mapper", "package-info");
        }

        javaSourceCode.createCompilationUnit(pkg + ".model", "package-info");
        javaSourceCode.createCompilationUnit(pkg + ".model.dos", "package-info");
        javaSourceCode.createCompilationUnit(pkg + ".model.dto", "package-info");
        javaSourceCode.createCompilationUnit(pkg + ".model.entity", "package-info");
        javaSourceCode.createCompilationUnit(pkg + ".model.req", "package-info");
        javaSourceCode.createCompilationUnit(pkg + ".model.vo", "package-info");

        javaSourceCode.createCompilationUnit(pkg + ".service", "package-info");
        javaSourceCode.createCompilationUnit(pkg + ".service.impl", "package-info");

        javaSourceCode.createCompilationUnit(pkg + ".utils", "package-info");
    }
}
