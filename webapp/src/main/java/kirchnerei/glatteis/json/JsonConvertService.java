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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConvertService {

	private final ObjectMapper mapper = new ObjectMapper();

	public String toString(Object value) {
		try {
			return mapper.writeValueAsString(value);
		} catch (Exception e) {
			throw new JsonConvertException(e, "convert into string is failed");
		}
	}

	public <T> T fromString(String text, Class<T> type) {
		try {
			return mapper.readValue(text, type);
		} catch (Exception e) {
			throw new JsonConvertException(e,
				"covert into the type '%s' is failed'", type == null ? "null" : type.getSimpleName());
		}
	}

	public <T> T fromString(String text, TypeReference<T> ref) {
		try {
			return mapper.readValue(text, ref);
		} catch (Exception e) {
			throw new JsonConvertException(e,
				"covert into the type '%s' is failed'", ref == null ? "null" : ref.getType());
		}
	}
}
