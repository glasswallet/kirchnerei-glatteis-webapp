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

import kirchnerei.glatteis.json.JsonConvertService;
import kirchnerei.glatteis.page.ui.*;
import kirchnerei.grundstein.ClassUtils;
import kirchnerei.grundstein.LogUtils;
import kirchnerei.grundstein.composite.CompositeBuilder;
import kirchnerei.grundstein.composite.CompositeInit;
import org.apache.click.Context;
import org.apache.click.Page;
import org.apache.click.util.Bindable;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * the common page class for all "Apache Click" pages
 */
public class CommonPage extends Page implements CompositeInit {

	private static final Log log = LogFactory.getLog(CommonPage.class);

	@Bindable
	protected int rule;

	@Bindable
	protected RequestKind requestKind;

	private TemplateResource templateResource = null;

	/**
	 * Contains the list of menu items for the top navigation
	 */
	private final Map<String, Menu> menubar = new LinkedHashMap<String, Menu>();
	private final List<String> scriptAction = new ArrayList<String>();


	protected JsonConvertService jsonService;

	@Override
	public void init(CompositeBuilder builder) {
		jsonService = builder.getSingleton(JsonConvertService.class);
	}

	@Override
	public String getTemplate() {
		if (templateResource == null) {
			templateResource = TemplateResource.getTemplateFromRequest(requestKind);
		}
		return templateResource != null ? templateResource.getUrl() : super.getTemplate();
	}

	@Override
	public void onInit() {
		LogUtils.trace(log, "init page with request kind '%s' (blank)", requestKind);
		// prepare the value if null then returns UNKNOWN
		requestKind = RequestKind.prepare(requestKind);
		LogUtils.debug(log, "init page with request kind '%s' (after prepare)", requestKind);
		LogUtils.debug(log, "request with rule '%s'", rule);
	}

	@Override
	public void onRender() {
		if (templateResource == null) {
			templateResource = TemplateResource.getTemplateFromRequest(requestKind);
		}
		switch (templateResource) {
			case COMPLETE_PAGE:
				onCompleteRender();
				break;
			case PARTIAL_CONTENT:
				onPartialRender();
				break;
			case DIALOG_CONTENT:
				onDialogRender();
				break;
		}
		onNotifyRender();
	}


	/**
	 * Set the template url
	 */
	public void setTemplateResource(TemplateResource res) {
		this.templateResource = res;
	}

	public Menu addMenu(String menuName) {
		if (!menubar.containsKey(menuName)) {
			Menu data = new Menu(menuName);
			setMenuData(menuName, data);
			menubar.put(menuName, data);
		}
		return menubar.get(menuName);
	}

	public MenuItem addMenu(String menuName, String menuItem) {
		Menu menuView = addMenu(menuName);
		MenuItem view = new MenuItem(menuName);
		setMenuData(menuItem, view);
		menuView.getChildList().add(view);
		return view;
	}

	public void addScriptAction(String action) {
		LogUtils.debug(log, "add the script action '%s'", action);
		scriptAction.add(action);
	}

	/**
	 * Returns the notify list. if the list is not exist, the will be created.
	 *
	 * @return notify list
	 */
	public NotifyList getNotifyList() {
		Context ctx = getContext();
		Object o = ctx.getSessionAttribute(NotifyList.KEY);
		if (o == null) {
			o = new NotifyList();
			ctx.setSessionAttribute(NotifyList.KEY, o);
		}
		return ClassUtils.cast(o, NotifyList.class);
	}

	/**
	 * Sets the kind of notification
	 * @param kind the kind of notification
	 * @return the notify list
	 */
	public NotifyList setNotifyKind(NotifyKind kind) {
		NotifyList list = getNotifyList();
		list.setKind(kind);
		return list;
	}

	/**
	 * add a notify messages to the current request and set the kind of notification.
	 *
	 * <p>
	 *     the parameter 'message' is a symbolic key for the real message that inserted at rendering.
	 * </p>
	 *
	 * @param kind the kind of notification
	 * @param message the symbolic message name
	 * @param args the arguments
	 * @return the notify list
	 */
	public NotifyList addNotifyMessage(NotifyKind kind, String message, Object... args) {
		NotifyList list = getNotifyList();
		list.setKind(kind);
		list.addMessage(message, args);
		return list;
	}

	/**
	 * add a notify messages to the current request.
	 *
	 * <p>
	 *     the parameter 'message' is a symbolic key for the real message that inserted at rendering.
	 * </p>
	 *
	 * @param message
	 * @param args
	 * @return
	 */
	public NotifyList addNotifyMessage(String message, Object... args) {
		NotifyList list = getNotifyList();
		list.addMessage(message, args);
		return list;
	}


	private void setMenuData(String menuName, MenuItem data) {
		data.setText(getMenuMessage(menuName, "text", null));
		data.setLink(getMenuMessage(menuName, "link", "none"));
		data.setIcon(getMenuMessage(menuName, "icon", "none"));
		data.setRole(MenuRole.fromString(getMenuMessage(menuName, "role", "as-partial")));
		data.setDivider(MenuDivider.fromString(getMenuMessage(menuName, "divider", "none")));
	}

	private String getMenuMessage(String name, String property, String defaultValue) {
		String key = String.format("action.%s.%s", name, property);
		try {
			return getMessage(key);
		} catch (Exception e) {
			LogUtils.trace(log, "messages key '%s' is missing", key);
			return StringUtils.defaultIfEmpty(defaultValue, key);
		}
	}

	private void onPartialRender() {
		addModel("menulist", new ArrayList<Menu>(menubar.values()));
		addModel("actionlist", jsonService.toString(scriptAction));
		addModel("navigation", "/kirchnerei/glatteis/page/ui/navigation.html");
		NotifyList list = getNotifyList();
		if (!list.isEmpty()) {
			addModel("notify", NotifyView.createMessageView(this, list));
			list.clear();
		}
		setHeader("Content-Type", "text/xml");
		setHeader("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
	}

	private void onCompleteRender() {
		addModel("menulist", new ArrayList<Menu>(menubar.values()));
		addModel("actionlist", jsonService.toString(scriptAction));
		addModel("navigation", "/kirchnerei/glatteis/page/ui/navigation.html");
		NotifyList list = getNotifyList();
		if (!list.isEmpty()) {
			addModel("notify", NotifyView.createMessageView(this, list));
			list.clear();
		}
		setHeader("Content-Type", "text/html");
		setHeader("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
	}

	private void onDialogRender() {
		addModel("actionlist", jsonService.toString(scriptAction));
		setHeader("Content-Type", "text/xml");
		setHeader("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
	}

	private void onNotifyRender() {
		NotifyList list = getNotifyList();
		if (!list.isEmpty()) {
			addModel("notify", NotifyView.createMessageView(this, list));
			list.clear();
		}
	}
}
