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
package kirchnerei.glatteis.webapp;

import kirchnerei.glatteis.page.RequestKind;
import kirchnerei.glatteis.resources.TimeoutETagStrategy;
import kirchnerei.grundstein.LogUtils;
import kirchnerei.grundstein.click.util.RequestConverter;
import kirchnerei.grundstein.click.util.RequestConverterFactory;
import kirchnerei.grundstein.composite.CompositeException;
import kirchnerei.grundstein.persistence.EntityService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Setup class for CompositeBuilder instance
 */
public class WebappSetup {

	private static final Log log = LogFactory.getLog(WebappSetup.class);

	/**
	 * returns the value of either system env or system properties
	 * @throws CompositeException if there is no value, then an exception will be thrown
	 */
	public static String getParam(String name) {
		String value = System.getenv(name);
		if (value == null) {
			value = System.getProperty(name);
		}
		if (value == null) {
			throw new CompositeException(
				"parameter '%s' is not present in system env or system properties", name);
		}
		return value;
	}

	/**
	 * set the name of the persistence unit
	 *
	 * setUp the persistence parameter (driver, url, user and password)
	 */
	public static class entityService extends EntityService {
		{
			setName("glatteis-app");

			String driver = getParam("DB_DRIVER");
			String url = getParam("DB_URL");
			String user = getParam("DB_USER");
			String password = getParam("DB_PASSWORD");
			setUpPersistence(driver, url, user, password);
		}
	}

	/**
	 * add the converter for enum RequestKind
	 */
	public static class requestConverterFactory extends RequestConverterFactory {
		{
			addConverter(RequestKind.class, new RequestConverter() {
				@Override
				public Object convert(Object o) {
					return RequestKind.fromString(o != null ? o.toString() : "");
				}
			});
		}
	}

	/**
	 * Timeout ETag Strategy for javascript and css styles
	 *
	 * <p>
	 *     See also: web.xml
	 * </p>
	 *
	 * @see kirchnerei.grundstein.webapp.HttpTextResourceServlet
	 */
	public static class timeoutETagStrategy extends TimeoutETagStrategy {
		{
			setTimeout(1L);
			LogUtils.debug(log, "timeout etag strategy in 1 minute");
		}
	}
}
