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

public class MenuItem {

	private final String name;

	private String text;

	private String icon;

	private MenuRole role;

	private String link;

	private boolean active;

	private MenuDivider divider;

	public MenuItem(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public MenuRole getRole() {
		return role;
	}

	public void setRole(MenuRole role) {
		this.role = role;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean hasIcon() {
		return !StringUtils.isEmpty(icon) && !"none".equalsIgnoreCase(icon);
	}

	public boolean isBeforeDivider() {
		return divider == MenuDivider.BEFORE;
	}

	public boolean isAfterDivider() {
		return divider == MenuDivider.AFTER;
	}

	public MenuDivider getDivider() {
		return divider;
	}

	public void setDivider(MenuDivider divider) {
		if (divider == null) {
			divider = MenuDivider.NONE;
		}
		this.divider = divider;
	}
}
