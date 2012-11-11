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
package kirchnerei.glatteis.resources;

import kirchnerei.grundstein.composite.CompositeBuilder;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

public class ScriptResourcesTest {

	private CompositeBuilder builder;

	@Before
	public void setUp() {
		builder = new CompositeBuilder();
		builder.setup(Setup.class);
	}


	@Test
	public void testWriteScriptFile() throws Exception {
		ScriptResources res = builder.getSingleton(ScriptResources.class);
		assertNotNull(res);
		ByteArrayOutputStream out = new ByteArrayOutputStream(512 * 1024);
		res.writeTo(out);
		IOUtils.closeQuietly(out);
		String content = new String(out.toByteArray(), Charset.forName(res.getEncoding()));
		System.out.println(content);
	}
}
