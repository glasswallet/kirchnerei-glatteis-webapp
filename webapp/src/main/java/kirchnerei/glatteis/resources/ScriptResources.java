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

import kirchnerei.grundstein.webapp.TextResources;

public class ScriptResources extends TextResources {

	public ScriptResources() {
		addResource("class:///kirchnerei/glatteis/resources/jquery.js");
		addResource("class:///kirchnerei/glatteis/resources/jquery.history.js");
		addResource("class:///kirchnerei/glatteis/resources/jquery.xml2json.js");
		addResource("class:///kirchnerei/glatteis/resources/bootstrap.js");
		addResource("class:///kirchnerei/glatteis/resources/website.js");
		setContentType("text/javascript");
		setEncoding("UTF-8");
	}
}
