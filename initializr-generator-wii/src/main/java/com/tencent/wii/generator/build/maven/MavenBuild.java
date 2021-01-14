package com.tencent.wii.generator.build.maven;

import io.spring.initializr.generator.buildsystem.BuildItemResolver;

/**
 * <p>
 *     带 module 的 maven构建器
 * </p>
 *
 * @author terryychen@tencent.com
 * @date 2021/1/14
 */
public class MavenBuild extends io.spring.initializr.generator.buildsystem.maven.MavenBuild {

    private final MavenModelContainer mavenModelContainer = new MavenModelContainer();

    public MavenBuild(BuildItemResolver buildItemResolver) {
        super(buildItemResolver);
    }

    public MavenBuild() {
        super();
    }

    public void addModel(MavenBuild mavenBuild) {
        mavenModelContainer.addModel(mavenBuild);
    }

    public MavenModelContainer getMavenModelContainer() {
        return mavenModelContainer;
    }
}
