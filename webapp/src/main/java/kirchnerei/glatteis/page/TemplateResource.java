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

/**
 * Project: @PROJECT_NAME@
 * User: sarah
 * Date: 31.10.12
 * Publish: @PROJECT_PUBLISH@
 */
public enum TemplateResource {

	COMPLETE_PAGE("/kirchnerei/glatteis/page/template-complete-page.html"),
	PARTIAL_CONTENT("/kirchnerei/glatteis/page/template-partial-content.xml"),
	DIALOG_CONTENT("/kirchnerei/glatteis/page/template-dialog-content.xml"),
	REPLACE_CONTENT("/kirchnerei/glatteis/page/template-replace-content.xml"),
	REDIRECT_PAGE("")


	; // Implementations


	private final String url;

	TemplateResource(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}



	public static TemplateResource getTemplateFromRequest(RequestKind kind) {
		switch (kind) {
			case COMPLETE:
				return COMPLETE_PAGE;
			case PARTIAL:
				return PARTIAL_CONTENT;
		}
		return null;
	}
}
