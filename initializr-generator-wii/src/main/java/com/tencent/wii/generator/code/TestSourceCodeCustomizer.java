package com.tencent.wii.generator.code;

import io.spring.initializr.generator.language.CompilationUnit;
import io.spring.initializr.generator.language.TypeDeclaration;
import org.springframework.core.Ordered;

/**
 * Callback for customizing the application's main source code. Invoked with an
 * {@link Ordered order} of {@code 0} by default, considering overriding
 * {@link #getOrder()} to customize this behaviour.
 *
 * @param <T> language-specific type declaration
 * @param <C> language-specific compilation unit
 * @param <S> language-specific source code
 * @author Andy Wilkinson
 */
@FunctionalInterface
public interface TestSourceCodeCustomizer<T extends TypeDeclaration, C extends CompilationUnit<T>, S extends ProjectSourceCode<T, C>>
		extends Ordered {

	/**
     * project source 扩展
	 * @param sourceCode sourceCode
	 */
	void customize(S sourceCode);

	/**
     * 执行顺序
	 * @return
     */
	@Override
	default int getOrder() {
		return 0;
	}

}
