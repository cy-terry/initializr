package com.tencent.wii.generator.build.maven;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * maven model 构建容器
 * </p>
 *
 * @author terryychen@tencent.com
 * @date 2021/1/14
 */
public class MavenModelContainer {

    private final List<MavenBuild> models = new LinkedList<>();

    /**
     * 添加model
     *
     * @param model model
     */
    public void addModel(MavenBuild model) {
        models.add(model);
    }

    public List<MavenBuild> getModels() {
        return models;
    }
}
