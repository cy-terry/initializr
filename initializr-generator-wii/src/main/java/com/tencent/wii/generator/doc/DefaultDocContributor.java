package com.tencent.wii.generator.doc;

import io.spring.initializr.generator.project.contributor.ProjectContributor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * <p>
 *     默认文档提供者
 * </p>
 *
 * @author terryychen@tencent.com
 * @date 2021/1/15
 */
public class DefaultDocContributor implements ProjectContributor {
    @Override
    public void contribute(Path projectRoot) throws IOException {
        Path path = projectRoot.resolve("doc");
        Files.createDirectories(path);
    }
}
