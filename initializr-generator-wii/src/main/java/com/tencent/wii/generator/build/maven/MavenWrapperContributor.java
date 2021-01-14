package com.tencent.wii.generator.build.maven;

import io.spring.initializr.generator.project.contributor.MultipleResourcesProjectContributor;

/**
 * <p> </p>
 *
 * @author terryychen@tencent.com
 * @date 2021/1/14
 */
class MavenWrapperContributor extends MultipleResourcesProjectContributor {

    MavenWrapperContributor() {
        super("classpath:maven/wrapper", (filename) -> "mvnw".equals(filename) || "mvnw.cmd".equals(filename));
    }

}