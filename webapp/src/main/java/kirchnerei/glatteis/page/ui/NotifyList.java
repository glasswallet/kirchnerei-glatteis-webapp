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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NotifyList implements Iterable<NotifyList.Message>, Serializable {

	private static final long serialVersionUID = -8964725509375542176L;

	public static final String KEY = NotifyList.class.getName();

	private NotifyKind kind = NotifyKind.INFO;

	private final List<Message> messages = new ArrayList<Message>();

	public synchronized void addMessage(Message message) {
		this.messages.add(message);
	}

	public synchronized void addMessage(String name, Object... args) {
		addMessage(new Message(name, args));
	}

	public synchronized void addMessage(String name) {
		addMessage(new Message(name, null));
	}

	public NotifyKind getKind() {
		return kind;
	}

	public void setKind(NotifyKind kind) {
		kind = NotifyKind.prepare(kind);
		if (this.kind.compareTo(kind) < 0) {
			this.kind = kind;
		}
	}

	public boolean isEmpty() {
		return messages.isEmpty();
	}

	public int size() {
		return messages.size();
	}

	/**
	 * Clear and reset the list of messages and set the kind to INFO.
	 */
	public synchronized void clear() {
		messages.clear();
		setKind(NotifyKind.INFO);
	}

	@Override
	public Iterator<Message> iterator() {
		return messages.iterator();
	}

	public static class Message {

		private final String name;

		private final Object[] args;

		public Message(String name, Object[] args) {
			this.name = name;
			this.args = args;
		}

		public String getName() {
			return name;
		}

		public Object[] getArgs() {
			return args;
		}

		public boolean hasArgs() {
			return args != null && args.length > 0;
		}
	}
}
