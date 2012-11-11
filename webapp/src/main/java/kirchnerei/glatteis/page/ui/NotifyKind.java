/*
 * Copyright 2012 Kirchnerei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kirchnerei.glatteis.page.ui;

import org.apache.commons.lang.StringUtils;

/**
 * @see NotifyList
 */
public enum NotifyKind {

	INFO("alert alert-info"),
	SUCCESS("alert alert-success"),
	WARNING("alert"),
	ERROR("alert alert-error");


	private final String style;

	NotifyKind(String style) {
		this.style = style;
	}

	public String getStyle() {
		return style;
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}



	public static NotifyKind fromString(String kind) {
		if (StringUtils.isEmpty(kind)) {
			return INFO;
		}
		try {
			return valueOf(kind.toUpperCase());
		} catch (Exception e) {
			return INFO;
		}
	}

	public static NotifyKind prepare(NotifyKind kind) {
		if (kind == null) {
			return INFO;
		}
		return kind;
	}
}
