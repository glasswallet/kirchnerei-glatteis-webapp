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
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

public class JsonConvertServiceTest {

	private static JsonConvertService service = new JsonConvertService();

	private static final Date TEST_DATE;

	static {
		Calendar cal = Calendar.getInstance();
		cal.set(2012, Calendar.JULY, 4);
		TEST_DATE = cal.getTime();
	}

	@Test
	public void testBeanToString() {
		Bean bean = new Bean(10, "Test", TEST_DATE);
		beanToString(bean);
	}

	@Test
	public void testStringToBean() {
		Bean b1 = new Bean(10, "Test", TEST_DATE);
		String text = beanToString(b1);
		Bean b2 = service.fromString(text, Bean.class);
		assertNotNull(b2);
		assertNotSame(b1, b2);
	}

	@Test
	public void testBeanListToString() {
		beanToString(new Bean(1, "Bean 1", TEST_DATE), new Bean(2, "Bean 2", TEST_DATE));
	}

	@Test
	public void testStringToBeanList() {
		String text = beanToString(new Bean(1, "Bean 1", TEST_DATE), new Bean(2, "Bean 2", TEST_DATE));
		List<Bean> items = service.fromString(text, new TypeReference<List<Bean>>() {});
		assertNotNull(items);
		assertEquals(2, items.size());
	}


	private String beanToString(Bean bean) {
		String text = service.toString(bean);
		assertNotNull(text);
		return text;
	}

	private String beanToString(Bean... beans) {
		List<Bean> items = Arrays.asList(beans);
		String text = service.toString(items);
		assertNotNull(text);
		return text;
	}

	public static class Bean {

		private int id;

		private String name;

		private Date date;

		public Bean() {
		}

		public Bean(int id, String name, Date date) {
			this.id = id;
			this.name = name;
			this.date = date;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}
	}
}
