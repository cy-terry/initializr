package com.tencent.wii.generator.code.java;

import com.tencent.wii.generator.code.MainSourceCodeCustomizer;
import com.tencent.wii.generator.code.ProjectSourceCode;
import com.tencent.wii.generator.common.util.WiiModelUtil;
import io.spring.initializr.generator.language.Annotation;
import io.spring.initializr.generator.language.java.JavaCompilationUnit;
import io.spring.initializr.generator.language.java.JavaFieldDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodDeclaration;
import io.spring.initializr.generator.language.java.JavaMethodInvocation;
import io.spring.initializr.generator.language.java.JavaReturnStatement;
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
public class WiiApiMainSourceCodeCustomizer
        implements MainSourceCodeCustomizer<JavaTypeDeclaration,
        JavaCompilationUnit, ProjectSourceCode<JavaTypeDeclaration, JavaCompilationUnit>> {

    private final ProjectDescription description;

    public WiiApiMainSourceCodeCustomizer(ProjectDescription description) {
        this.description = description;
    }

    @Override
    public void customize(ProjectSourceCode<JavaTypeDeclaration, JavaCompilationUnit> sourceCode) {
        JavaSourceCode javaSourceCode = new JavaSourceCode();
        sourceCode.addModeSourceCode(WiiModelUtil.apiName(description), javaSourceCode);
        String rootPackage = description.getPackageName();
        String pkg = rootPackage + ".api";
        javaSourceCode.createCompilationUnit(pkg, "package-info");

        JavaCompilationUnit compilationUnit = javaSourceCode.createCompilationUnit(pkg + ".controller.v1",
                "DemoController");
        JavaTypeDeclaration declaration = compilationUnit.createTypeDeclaration("DemoController");
        declaration.modifiers(Modifier.PUBLIC);
        declaration.annotate(Annotation.name("org.springframework.web.bind.annotation.RestController"));
        declaration.annotate(Annotation.name("org.springframework.web.bind.annotation.RequestMapping",
                builder -> builder.attribute("value", String.class, "api/v1/demo")));
        declaration.addFieldDeclaration(JavaFieldDeclaration.field("LOGGER")
                .modifiers(Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL)
                .value("org.slf4j.LoggerFactory.getLogger(DemoController.class)")
                .returning("org.slf4j.Logger")
        );

        JavaCompilationUnit compilationUnit1 = javaSourceCode.createCompilationUnit(pkg + ".controller", "PreCheckController");
        JavaTypeDeclaration declaration1 = compilationUnit1.createTypeDeclaration("PreCheckController");
        declaration1.modifiers(Modifier.PUBLIC);
        declaration1.annotate(Annotation.name("org.springframework.web.bind.annotation.RestController"));
        declaration1.annotate(Annotation.name("org.springframework.web.bind.annotation.RequestMapping",
                builder -> builder.attribute("value", String.class, "/")));
        JavaMethodDeclaration methodDeclaration = JavaMethodDeclaration
                .method("checkPreload")
                .modifiers(Modifier.PUBLIC)
                .returning("String")
                .body(new JavaReturnStatement(
                        new JavaMethodInvocation("String", "valueOf", "\"OK\"")
                ));
        methodDeclaration.annotate(Annotation.name("org.springframework.web.bind.annotation.RequestMapping",
                builder -> builder.attribute("value", String.class, "checkpreload.htm")));
        declaration1.addMethodDeclaration(methodDeclaration);
    }
}
