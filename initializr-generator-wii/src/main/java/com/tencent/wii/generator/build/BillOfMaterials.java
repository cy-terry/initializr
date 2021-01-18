/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tencent.wii.generator.build;

/**
 * A Bill of Materials (BOM) definition to be declared in a project's build configuration.
 *
 * @author Stephane Nicoll
 */
public class BillOfMaterials extends io.spring.initializr.generator.buildsystem.BillOfMaterials {

	public final static String DEFAULT_TYPE = "pom";

	public final static String DEFAULT_SCOPE = "import";

	private final String type;

	private final String scope;

	protected BillOfMaterials(Builder builder) {
		super(builder);
		this.type = builder.type;
		this.scope = builder.scope;
	}

	/**
	 * Initialize a new BOM {@link Builder} with the specified coordinates.
	 * @param groupId the group ID of the bom
	 * @param artifactId the artifact ID of the bom
	 * @return a new builder
	 */
	public static Builder withCoordinates(String groupId, String artifactId) {
		return new Builder(groupId, artifactId);
	}

	/**
	 * Initialize a new BOM {@link Builder} with the specified coordinates.
	 * @param type 类型
	 * @param scope 范围
	 * @param groupId the group ID of the bom
	 * @param artifactId the artifact ID of the bom
	 * @return a new builder
	 */
	public static Builder withCoordinates(String groupId, String artifactId, String type, String scope) {
		return new Builder(groupId, artifactId, type, scope);
	}

	public String getType() {
		return type;
	}

	public String getScope() {
		return scope;
	}

	@Override
	public int getOrder() {
		return super.getOrder();
	}

	/**
	 * Builder for a Bill of Materials.
	 */
	public static class Builder extends io.spring.initializr.generator.buildsystem.BillOfMaterials.Builder {

		private String type;

		private String scope;

		protected Builder(String groupId, String artifactId) {
			super(groupId, artifactId);
		}

		protected Builder(String groupId, String artifactId, String type, String scope) {
			super(groupId, artifactId);
			this.type = type;
			this.scope = scope;
		}

		/**
		 * set type
		 * @param type
		 * @return
		 */
		public Builder type(String type) {
			this.type = type;
			return this;
		}

		public Builder scope(String scope) {
			this.scope = scope;
			return this;
		}

		/**
		 * Build a {@link BillOfMaterials} with the current state of this builder.
		 * @return a {@link BillOfMaterials}
		 */
		@Override
		public BillOfMaterials build() {
			return new BillOfMaterials(this);
		}

	}

}
