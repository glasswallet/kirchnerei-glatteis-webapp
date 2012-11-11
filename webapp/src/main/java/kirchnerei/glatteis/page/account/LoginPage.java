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
package kirchnerei.glatteis.page.account;

import kirchnerei.glatteis.page.CommonPage;
import kirchnerei.glatteis.page.ScriptActionResource;
import kirchnerei.glatteis.page.TemplateResource;

public class LoginPage extends CommonPage {


	@Override
	public void onGet() {
		setTemplateResource(TemplateResource.DIALOG_CONTENT);
		addScriptAction(ScriptActionResource.DIALOG_FORM);
	}

	@Override
	public void onPost() {
		boolean success = true;
		if (success) {
			setTemplateResource(TemplateResource.REDIRECT_PAGE);

		}
		// some errors
		onGet();
	}
}
