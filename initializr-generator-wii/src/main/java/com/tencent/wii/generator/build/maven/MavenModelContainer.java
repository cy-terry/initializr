package com.tencent.wii.generator.build.maven;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * maven model 构建容器
 * </p>
 *
 * @author terryychen@tencent.com
 * @date 2021/1/14
 */
public class MavenModelContainer {

    private final Map<String, MavenBuild> models = new LinkedHashMap<>();

    /**
     * 添加model
     *
     * @param model model
     */
    public void addModel(MavenBuild model) {
        models.put(model.getSettings().getArtifact(), model);
    }

    public Map<String, MavenBuild> getModels() {
        return models;
    }
}
