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

package kirchnerei.glatteis.page;

import org.apache.commons.lang.StringUtils;

public enum RequestKind {

	UNKNOWN,
	COMPLETE,
	PARTIAL;



	@Override
	public String toString() {
		return name().toLowerCase();
	}

	public static RequestKind fromString(String name) {
		if (StringUtils.isEmpty(name)) {
			return UNKNOWN;
		}
		try {
			return valueOf(name.toUpperCase());
		} catch (Exception e) {
			return UNKNOWN;
		}
	}

	public static RequestKind prepare(RequestKind kind) {
		if (kind == null) {
			return UNKNOWN;
		}
		return kind;
	}
}