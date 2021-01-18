package com.tencent.wii.generator.common.util;

import io.spring.initializr.generator.project.ProjectDescription;

/**
 * <p>
 *  wii project tools
 * </p>
 *
 * @author terry
 * @version 2021/1/18
 * @see
 * @since
 */
public class WiiModelUtil {

    /**
     * core model name
     * @param description
     * @return
     */
    public static String coreName(ProjectDescription description) {
        return description.getArtifactId() + "-core";
    }

    /**
     * app model name
     * @param description
     * @return
     */
    public static String appName(ProjectDescription description) {
        return description.getArtifactId() + "-app";
    }

    /**
     * oss model name
     * @param description
     * @return
     */
    public static String ossName(ProjectDescription description) {
        return description.getArtifactId() + "-oss";
    }

    /**
     * api mode name
     * @param description
     * @return
     */
    public static String apiName(ProjectDescription description) {
        return description.getArtifactId() + "-api";
    }

    /**
     * web model name
     * @param description
     * @return
     */
    public static String webName(ProjectDescription description) {
        return description.getArtifactId() + "-web";
    }
}
