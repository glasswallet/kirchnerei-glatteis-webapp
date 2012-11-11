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


/**
 * Handle all methods of the website application
 *
 * Singleton of the Website class.
 *
 * @type {Object}
 */
var website = (function (window, $) {

	/**
	 * @constructor
	 */
	function Website() { }

	var __contextPath = "",
		__config = {
			isTraceEnable: false,
			isDebugEnable: true,
			isInfoEnable: true,
			isWarnEnable: true,

			notifyStartTime: 100,
			// duration of showing of notification
			notifyDuration: 3000
		};

	/**
	 * Initalized the application
	 *
	 * @param {String} contextPath the context path of the application
	 * @param {String} actionList a string with json array of actions
	 * @param {Object} config a configuration object
	 */
	Website.prototype.initApplication = function (contextPath, actionList, config) {
		if (config) {
			for (var i in config) {
				var value = config[i];
				__config[i] = value;
				Log.debug('set the config ', i, '=', value);
			}
		}
		__contextPath = contextPath;
		this.executeActionList(actionList);

		this.initHistory();
		this.initLinkAction();
		this.initDialog();
		// TODO find a place for ajax-request (this.initRequest();)
	};

	/**
	 * Initializes the own history management.
	 */
	Website.prototype.initHistory = function () {
		var h = window.History;
		if (!h.enabled) {
			Log.debug('history logging is not enable');
			return false;
		}
		var that = this;
		var internalStateChangeFunc = function () {
			var state = h.getState();
			Log.debug('state change event -> state=', state.data, ', title=',
				state.title, ', url=', state.url);
			that.loadContentFromUrl(state.url, state.title);
		};
		// an das Event "statechange" des "window" Objekts anbinden.
		h.Adapter.bind(window, 'statechange', internalStateChangeFunc);
		return true;
	};

	/**
	 * Update the a elements with the style class "link-action" and append the click method.
	 */
	Website.prototype.initLinkAction = function () {
		var that = this;
		var clickFunc = function () {
			var link = $(this),
				title = link.text(),
				role = link.attr('role'),
				url = link.attr('href');
			switch (role) {
				case 'as-dialog':
					that.handleDialogLinkAction(title, url);
					return false;
				case 'as-partial':
					that.handlePartialLinkAction(title, url);
					return false;
				default:
					Log.warn('unknown role "', role, '" in the link action "', url, '"');
					return false;
			}
		};
		if (arguments.length > 0) {
			var ctx = arguments[0];
			Log.debug('update link click on ', ctx);
			$('a.link-action', ctx).on('click', clickFunc);
		} else {
			Log.debug('update link click on whole page');
			$('a.link-action').on('click', clickFunc);
		}
	};

	Website.prototype.initDialog = function () {
		$('#dialog').on('hidden', function () {
			$(this).removeData('modal');
			Log.debug('remove dialog data');
		});
	};

	/**
	 * Initialized the ajay request
	 */
	Website.prototype.initRequest = function () {
		$('#ajax-request').ajaxStart(function () {
			$(this).fadeIn('fast');
			Log.debug('start ajax request');
		}).ajaxComplete(function () {
			Log.debug('complete ajax request');
			$(this).fadeOut('slow');
		});
	};

	Website.prototype.handleDialogLinkAction = function (title, url) {
		var that = this,
			action = this.createPartialUrl(url);
		var callFunc = function (asXmlString) {
			that.receiveData(asXmlString);
			that.showDialog();
		};
		$.get(action, {}, callFunc);
	};

	Website.prototype.handlePartialLinkAction = function (title, url) {
		this.addHistory(title, url);
		Log.debug('add to history "', url, '"');
	};


	/**
	 * Adds a url with the title to the browser history
	 *
	 * <p>
	 * Adding a url to the browser history will be executing to get the content.
	 * </p>
	 *
	 * @param {String} title
	 * @param {String} url
	 */
	Website.prototype.addHistory = function (title, url) {
		var h = window.History;
		Log.debug('add browser history -> ', url);
		h.pushState({}, title, url, true);
	};



	/**
	 * Request the url and load the content into the "#content" area.
	 *
	 * @param {String} url
	 * @param {String} title
	 */
	Website.prototype.loadContentFromUrl = function (url, title) {
		Log.debug('load content from ', title, ', url -> ', url);
		var that = this,
			action = this.createPartialUrl(url);
		var requestFunc = function (asXmlString) {
			that.receiveData(asXmlString);
		};
		$.get(action, {}, requestFunc);
	};

	/**
	 *
	 * @param {String} asXmlString
	 */
	Website.prototype.receiveData = function (asXmlString) {
		var json = this.toJson(asXmlString);
		if (!json) {
			Log.warn('can not convert to json', asXmlString.substr(0, 60));
			return;
		}
		var status = json.status ? json.status : 'unknown';
		Log.debug('receive data with status "', status, '"');
		switch (status.toLowerCase()) {
			case 'redirect':
				if (json.redirect) {
					var url = this.createCompleteUrl(json.redirect);
					document.location.replace(url);
				} else {
					document.location.reload();
				}
				break;
			case 'replace':
				// replace all night
				this.setTitle(json.title);
				this.setContent(json.content);
				this.executeActionList(json.actionlist);
				this.setNotify(json.notify);
				break;
			case 'dialog':
				this.setDialog(json.content);
				this.executeActionList(json.actionlist);
				break;
			case 'partial':
				this.setTitle(json.title);
				this.setNavigation(json.navigation);
				this.setContent(json.content);
				this.executeActionList(json.actionlist);
				this.setNotify(json.notify);
				break;
			default:
				Log.error('status "', status, '" can not processed');
				break;
		}
	};

	/**
	 * Sets the title of the current page
	 * @param {String} value
	 */
	Website.prototype.setTitle = function (value) {
		if (!value || value == null || value.length == 0) {
			return;
		}
		document.title = value;
	};

	/**
	 * Sets the navigation menu list.
	 * @param {String} value
	 */
	Website.prototype.setNavigation = function (value) {
		if (!value || value == null || value.length == 0) {
			return;
		}
		var panel = $('#navigation');
		panel.html(value);
		this.initLinkAction(panel);
	};

	/**
	 * Sets the value to the content area.
	 *
	 * @param {String} value
	 * @see Website.receiveData()
	 */
	Website.prototype.setContent = function (value) {
		if (!value || value == null || value.length == 0) {
			return;
		}
		var panel = $('#content');
		panel.html(value);
		this.initLinkAction(panel);
	};

	Website.prototype.setDialog = function (value) {
		if (!value || value == null || value.length == 0) {
			return;
		}
		var panel = $('#dialog');
		panel.html(value);
	};

	Website.prototype.setNotify = function (value) {
		if (!value || value == null || value.length < 10) {
			return;
		}
		$('#notify').html(value);
		var that = this;
		window.setTimeout(function () {
			that.showNotify();
		}, __config.notifyStartTime);
	};

	Website.prototype.showDialog = function () {
		 $('#dialog').modal({
			 'backdrop': true
		 });
	};

	Website.prototype.closeDialog = function () {
		$('#dialog').modal('hide');
	};

	Website.prototype.showNotify = function () {
		var notify = $('#notify');
		notify.show('fast', function() {
			window.setTimeout(function () {
				notify.hide('slow', function() {
					notify.html('');
				});
			}, __config.notifyDuration);
		});
	};

	/**
	 * Executes the action from the give list.
	 *
	 * @param {String} actionlist a string with json array of actions.
	 */
	Website.prototype.executeActionList = function (actionlist) {
		if (!actionlist || actionlist == null || actionlist.length == 0 || actionlist == '[]') {
			Log.debug('no action execute');
			return;
		}
		var items = JSON.parse(actionlist);
		for (var index in items) {
			var action = items[index];
			if (this.handleAction(action)) {
				Log.debug('execute the action "', action, '"');
			}
		}
	};

	/**
	 * Create a partial url from the given url.
	 *
	 * <pre>
	 *     var url = website.createPartialUrl('/contextPath/home');
	 *     // url => '/contextPath/p/home';
	 * </pre>
	 * @param {String} url
	 * @return {String}
	 */
	Website.prototype.createPartialUrl = function (url) {
		var cp = this.getContextPath();
		var pos = url.indexOf('?');
		var query = '';
		if (pos >= 0) {
			query = url.substr(pos);
			url = url.substring(0, pos);
		}
		pos = url.indexOf(cp);
		if (pos >= 0) {
			url = url.substr(pos + cp.length + 1);
		}
		return cp + '/p/' + url + query;
	};

	/**
	 * Prepare and create an complete url from the given action / url
	 *
	 * @param {String} url
	 * @return {String}
	 */
	Website.prototype.createCompleteUrl = function (url) {
		var cp = this.getContextPath();
		var pos = url.indexOf(cp);
		if (pos >= 0) {
			url = url.substr(pos + cp.length + 1);
		}
		return cp + '/' + url;
	};

	/**
	 * Convert the xml string into a json object.
	 *
	 * @param {String} asXmlString
	 * @return {JSON} the JSON Objekt.
	 */
	Website.prototype.toJson = function (asXmlString) {
		var json = false;
		try {
			json = $.xml2json(asXmlString);
		} catch (e) {
			Log.debug('error by parse the xml ', asXmlString.substring(0, 60));
		}
		return json;
	};

	/**
	 * Returns the content path of the application.
	 *
	 * @return {String}
	 */
	Website.prototype.getContextPath = function () {
		return __contextPath;
	};


	/**
	 * Initialized the home page where is logged in an user
	 *
	 * @param action
	 */
	Website.prototype.handleInitHomeLogin = function (action) {
		Log.debug('parameter: ', action);
		// TODO
	};

	/**
	 * Initialized the home page where is not logged in an user
	 *
	 * @param action
	 */
	Website.prototype.handleInitHomeLogout = function (action) {
		Log.debug('parameter: ', action);
		// TODO
	};

	/**
	 * Initialized the dialog that has a form.
	 *
	 * @param action
	 */
	Website.prototype.handleInitDialogForm = function (action) {
		Log.trace('initialized the form at the dialog: ', action);
		var that = this;
		var receiveFunc = function (asXmlString) {
			that.receiveData(asXmlString);
		};
		var submitFunc = function () {
			var form = $(this),
				action = form.attr('action'),
				url = that.createPartialUrl(action),
				postData = form.serialize();
			$.post(url, postData, receiveFunc)
			return false;
		};
		$('#dialog form').on('submit', submitFunc);
	};

	var __actionRegistry = {};

	/**
	 * Execute the registered callback function.
	 *
	 * @param action
	 * @return {Boolean}
	 */
	Website.prototype.handleAction = function (action) {
		if (__actionRegistry[action]) {
			var callFunc = __actionRegistry[action];
			var params = [];
			for (var i in arguments) {
				params.push(arguments[i]);
			}
			callFunc.apply(this, params);
			return true;
		}
		return false;
	};

	Website.prototype.registerAction = function (action, callFunc) {
		__actionRegistry[action] = callFunc;
	};

	/**
	 * Registers all action callback functions
	 */
	Website.prototype.registerAllActions = function () {
		Log.debug('register all actions');
		this.registerAction('homeLogin', this.handleInitHomeLogin);
		this.registerAction('homeLogout', this.handleInitHomeLogout);
		this.registerAction('dialogForm', this.handleInitDialogForm);
	};

	var Log = {

		trace : function () {
			if (__config.isTraceEnable) {
				var params = ["trace: "];
				for (var i in arguments) {
					params.push(arguments[i]);
				}
				console.debug.apply(console, params);
			}
		},

		debug : function () {
			if (__config.isDebugEnable) {
				var params = ["debug: "];
				for (var i in arguments) {
					params.push(arguments[i]);
				}
				console.debug.apply(console, params);
			}
		},

		info : function () {
			if (__config.isInfoEnable) {
				var params = ["info: "];
				for (var i in arguments) {
					params.push(arguments[i]);
				}
				console.info.apply(console, params);
			}
		},

		warn : function () {
			if (__config.isWarnEnable) {
				var params = ["warn: "];
				for (var i in arguments) {
					params.push(arguments[i]);
				}
				console.warn.apply(console, params);
			}
		}
	};

	var website = new Website();
	website.registerAllActions();
	return website;
} (window, jQuery));
