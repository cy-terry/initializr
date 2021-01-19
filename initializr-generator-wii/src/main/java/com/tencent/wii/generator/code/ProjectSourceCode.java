package com.tencent.wii.generator.code;

import io.spring.initializr.generator.language.CompilationUnit;
import io.spring.initializr.generator.language.SourceCode;
import io.spring.initializr.generator.language.TypeDeclaration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * 工程初始源码信息
 * </p>
 *
 * @author terryychen@tencent.com
 * @date 2021/1/19
 */
public class ProjectSourceCode<T extends TypeDeclaration, C extends CompilationUnit<T>> {

    private final Map<String, ModeSourceCode<T, C>> modelCodeContainer = new LinkedHashMap<>();

    private SourceCode<T, C> sourceCode;

    public void setSourceCode(SourceCode<T, C> sourceCode) {
        this.sourceCode = sourceCode;
    }

    public SourceCode<T, C> projectSourceCode() {
        return this.sourceCode;
    }

    public SourceCode<T, C> modelSourceCode(String model) {
        return modelCodeContainer.getOrDefault(model, new ModeSourceCode<>()).sourceCode;
    }

    public ProjectSourceCode<T, C> addModeSourceCode(String model, SourceCode<T, C> sourceCode) {
        modelCodeContainer.put(model, new ModeSourceCode<>(model, sourceCode));
        return this;
    }

    public Map<String, ModeSourceCode<T, C>> modelCodeContainer() {
        return modelCodeContainer;
    }

    public static class ModeSourceCode<T extends TypeDeclaration, C extends CompilationUnit<T>> {

        public ModeSourceCode(String model, SourceCode<T, C> sourceCode) {
            this.model = model;
            this.sourceCode = sourceCode;
        }

        public ModeSourceCode() {
        }

        private String model;

        private SourceCode<T, C> sourceCode;

        public String getModel() {
            return model;
        }

        public SourceCode<T, C> getSourceCode() {
            return sourceCode;
        }
    }
}
