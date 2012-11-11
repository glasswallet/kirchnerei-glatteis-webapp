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
package kirchnerei.glatteis.json;

import kirchnerei.grundstein.ExceptionValues;

public class JsonConvertException extends RuntimeException {

	private ExceptionValues values;

	public JsonConvertException(String message, Object... args) {
		super(message);
		this.values = new ExceptionValues(args);
	}

	public JsonConvertException(Throwable cause, String message, Object... args) {
		super(message, cause);
		this.values = new ExceptionValues(args);
	}

	@Override
	public String getMessage() {
		return values.getMessage(super.getMessage());
	}
}
