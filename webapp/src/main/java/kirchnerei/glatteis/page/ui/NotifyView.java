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

import org.apache.click.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the notify view of an notify list.
 *
 * <p>
 *     Render the notify view is with the velocity macro <b>notifylist</b> possible. This macro is
 *     defined in the global file <b>macro.vm</b> in the root of the web context.
 * </p>
 *
 * @see NotifyList
 */
public class NotifyView {

	private final String title;

	private final String style;

	private final List<String> messages;

	/**
	 * Internal constructor
	 * @param title the title of the notify
	 * @param style the css style of the notify
	 * @param messages the list of messages
	 */
	private NotifyView(String title, String style, List<String> messages) {
		this.title = title;
		this.style = style;
		this.messages = messages;
	}

	public String getTitle() {
		return title;
	}

	public String getStyle() {
		return style;
	}

	public List<String> getMessages() {
		return messages;
	}

	/**
	 * Create a message view with the title, style and a list of messages
	 *
	 * @param page the page
	 * @param list the notify list
	 * @return the notifyView instance for render in the page html resource.
	 */
	public static NotifyView createMessageView(Page page, NotifyList list) {
		String titleKey = String.format("message.%s.title", list.getKind());
		String title = page.getMessage(titleKey);
		String style = list.getKind().getStyle();
		List<String> messages = new ArrayList<String>(list.size());
		for (NotifyList.Message msg : list) {
			if (msg.hasArgs()) {
				messages.add(page.getMessage(msg.getName(), msg.getArgs()));
			} else {
				messages.add(page.getMessage(msg.getName()));
			}
		}
		return new NotifyView(title, style, messages);
	}
}
