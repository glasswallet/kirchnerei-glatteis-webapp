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
 * Project: @PROJECT_NAME@
 * User: sarah
 * Date: 02.11.12
 * Publish: @PROJECT_PUBLISH@
 */
public enum MenuRole {

	MENU_PARTIAL("as-partial"),

	MENU_DIALOG("as-dialog");

	private final String role;

	MenuRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return role;
	}



	public static MenuRole fromString(String role) {
		if (StringUtils.isEmpty(role)) {
			return MENU_PARTIAL;
		}
		if (role.equals("as-partial") || role.equals("partial")) {
			return MENU_PARTIAL;
		} else if (role.equals("as-dialog") || role.equals("dialog")) {
			return MENU_DIALOG;
		}
		try {
			return valueOf(role.toUpperCase());
		} catch (Exception e) {
			return MENU_PARTIAL;
		}
	}
}
