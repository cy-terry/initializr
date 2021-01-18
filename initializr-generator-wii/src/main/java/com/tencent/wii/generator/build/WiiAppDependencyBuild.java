package com.tencent.wii.generator.build;

import com.tencent.wii.generator.common.util.WiiModelUtil;
import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.version.VersionReference;

/**
 * <p> </p>
 *
 * @author terryychen@tencent.com
 * @date 2021/1/18
 */
public class WiiAppDependencyBuild<T extends Build> {

    protected void build(T t, ProjectDescription description) {
        String app = WiiModelUtil.coreName(description);
        t.dependencies().add(app, Dependency.withCoordinates(description.getGroupId(), app).
                version(VersionReference.ofValue(description.getVersion())));
    }
}
