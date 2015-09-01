/* ===================================================
 * bootstrap-markdown.js v2.9.0
 * http://github.com/toopay/bootstrap-markdown
 * ===================================================
 * Copyright 2013-2015 Taufan Aditya
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
 * ========================================================== */

!function ($) {

  "use strict"; // jshint ;_;

  /* MARKDOWN CLASS DEFINITION
   * ========================== */

  var Markdown = function (element, options) {
    // @TODO : remove this BC on next major release
    // @see : https://github.com/toopay/bootstrap-markdown/issues/109
    var opts = ['autofocus', 'savable', 'hideable', 'width', 
      'height', 'resize', 'iconlibrary', 'language', 
      'footer', 'fullscreen', 'hiddenButtons', 'disabledButtons'];
    $.each(opts,function(_, opt){
      if (typeof $(element).data(opt) !== 'undefined') {
        options = typeof options == 'object' ? options : {}
        options[opt] = $(element).data(opt)
      }
    });
    // End BC

    // Class Properties
    this.$ns           = 'bootstrap-markdown';
    this.$element      = $(element);
    this.$editable     = {el:null, type:null,attrKeys:[], attrValues:[], content:null};
    this.$options      = $.extend(true, {}, $.fn.markdown.defaults, options, this.$element.data('options'));
    this.$oldContent   = null;
    this.$isPreview    = false;
    this.$isFullscreen = false;
    this.$editor       = null;
    this.$textarea     = null;
    this.$handler      = [];
    this.$callback     = [];
    this.$nextTab      = [];

    this.showEditor();
  };

  Markdown.prototype = {

    constructor: Markdown

  , __alterButtons: function(name,alter) {
      var handler = this.$handler, isAll = (name == 'all'),that = this;

      $.each(handler,function(k,v) {
        var halt = true;
        if (isAll) {
          halt = false;
        } else {
          halt = v.indexOf(name) < 0;
        }

        if (halt === false) {
          alter(that.$editor.find('button[data-handler="'+v+'"]'));
        }
      });
    }

  , __buildButtons: function(buttonsArray, container) {
      var i,
          ns = this.$ns,
          handler = this.$handler,
          callback = this.$callback;

      for (i=0;i<buttonsArray.length;i++) {
        // Build each group container
        var y, btnGroups = buttonsArray[i];
        for (y=0;y<btnGroups.length;y++) {
          // Build each button group
          var z,
              buttons = btnGroups[y].data,
              btnGroupContainer = $('<div/>', {
                                    'class': 'btn-group'
                                  });

          for (z=0;z<buttons.length;z++) {
            var button = buttons[z],
                buttonContainer, buttonIconContainer,
                buttonHandler = ns+'-'+button.name,
                buttonIcon = this.__getIcon(button.icon),
                btnText = button.btnText ? button.btnText : '',
                btnClass = button.btnClass ? button.btnClass : 'btn',
                tabIndex = button.tabIndex ? button.tabIndex : '-1',
                hotkey = typeof button.hotkey !== 'undefined' ? button.hotkey : '',
                hotkeyCaption = typeof jQuery.hotkeys !== 'undefined' && hotkey !== '' ? ' ('+hotkey+')' : '';

            // Construct the button object
            buttonContainer = $('<button></button>');
            buttonContainer.text(' ' + this.__localize(btnText)).addClass('btn-default btn-sm').addClass(btnClass);
            if(btnClass.match(/btn\-(primary|success|info|warning|danger|link)/)){
                buttonContainer.removeClass('btn-default');
            }
            buttonContainer.attr({
                'type': 'button',
                'title': this.__localize(button.title) + hotkeyCaption,
                'tabindex': tabIndex,
                'data-provider': ns,
                'data-handler': buttonHandler,
                'data-hotkey': hotkey
            });
            if (button.toggle === true){
              buttonContainer.attr('data-toggle', 'button');
            }
            buttonIconContainer = $('<span/>');
            buttonIconContainer.addClass(buttonIcon);
            buttonIconContainer.prependTo(buttonContainer);

            // Attach the button object
            btnGroupContainer.append(buttonContainer);

            // Register handler and callback
            handler.push(buttonHandler);
            callback.push(button.callback);
          }

          // Attach the button group into container dom
          container.append(btnGroupContainer);
        }
      }

      return container;
    }
  , __setListener: function() {
      // Set size and resizable Properties
      var hasRows = typeof this.$textarea.attr('rows') !== 'undefined',
          maxRows = this.$textarea.val().split("\n").length > 5 ? this.$textarea.val().split("\n").length : '5',
          rowsVal = hasRows ? this.$textarea.attr('rows') : maxRows;

      this.$textarea.attr('rows',rowsVal);
      if (this.$options.resize) {
        this.$textarea.css('resize',this.$options.resize);
      }

      this.$textarea
        .on('focus',    $.proxy(this.focus, this))
        .on('keypress', $.proxy(this.keypress, this))
        .on('keyup',    $.proxy(this.keyup, this))
        .on('change',   $.proxy(this.change, this))
        .on('select',   $.proxy(this.select, this));

      if (this.eventSupported('keydown')) {
        this.$textarea.on('keydown', $.proxy(this.keydown, this));
      }

      // Re-attach markdown data
      this.$textarea.data('markdown',this);
    }

  , __handle: function(e) {
      var target = $(e.currentTarget),
          handler = this.$handler,
          callback = this.$callback,
          handlerName = target.attr('data-handler'),
          callbackIndex = handler.indexOf(handlerName),
          callbackHandler = callback[callbackIndex];

      // Trigger the focusin
      $(e.currentTarget).focus();

      callbackHandler(this);

      // Trigger onChange for each button handle
      this.change(this);

      // Unless it was the save handler,
      // focusin the textarea
      if (handlerName.indexOf('cmdSave') < 0) {
        this.$textarea.focus();
      }

      e.preventDefault();
    }

  , __localize: function(string) {
      var messages = $.fn.markdown.messages,
          language = this.$options.language;
      if (
        typeof messages !== 'undefined' &&
        typeof messages[language] !== 'undefined' &&
        typeof messages[language][string] !== 'undefined'
      ) {
        return messages[language][string];
      }
      return string;
    }

  , __getIcon: function(src) {
    return typeof src == 'object' ? src[this.$options.iconlibrary] : src;
  }

  , setFullscreen: function(mode) {
    var $editor = this.$editor,
        $textarea = this.$textarea;

    if (mode === true) {
      $editor.addClass('md-fullscreen-mode');
      $('body').addClass('md-nooverflow');
      this.$options.onFullscreen(this);
    } else {
      $editor.removeClass('md-fullscreen-mode');
      $('body').removeClass('md-nooverflow');

      if (this.$isPreview == true) this.hidePreview().showPreview()
    }

    this.$isFullscreen = mode;
    $textarea.focus();
  }

  , showEditor: function() {
      var instance = this,
          textarea,
          ns = this.$ns,
          container = this.$element,
          originalHeigth = container.css('height'),
          originalWidth = container.css('width'),
          editable = this.$editable,
          handler = this.$handler,
          callback = this.$callback,
          options = this.$options,
          editor = $( '<div/>', {
                      'class': 'md-editor',
                      click: function() {
                        instance.focus();
                      }
                    });

      // Prepare the editor
      if (this.$editor === null) {
        // Create the panel
        var editorHeader = $('<div/>', {
                            'class': 'md-header btn-toolbar'
                            });

        // Merge the main & additional button groups together
        var allBtnGroups = [];
        if (options.buttons.length > 0) allBtnGroups = allBtnGroups.concat(options.buttons[0]);
        if (options.additionalButtons.length > 0) {
          // iterate the additional button groups
          $.each(options.additionalButtons[0], function(idx, buttonGroup){
            
            // see if the group name of the addional group matches an existing group
            var matchingGroups = $.grep(allBtnGroups, function(allButtonGroup, allIdx){
              return allButtonGroup.name === buttonGroup.name;
            });

            // if it matches add the addional buttons to that group, if not just add it to the all buttons group
            if(matchingGroups.length > 0) {
              matchingGroups[0].data = matchingGroups[0].data.concat(buttonGroup.data);
            } else {              
              allBtnGroups.push(options.additionalButtons[0][idx]);
            }

          });
        } 

        // Reduce and/or reorder the button groups
        if (options.reorderButtonGroups.length > 0) {
          allBtnGroups = allBtnGroups
              .filter(function(btnGroup) {
                return options.reorderButtonGroups.indexOf(btnGroup.name) > -1;
              })
              .sort(function(a, b) {
                if (options.reorderButtonGroups.indexOf(a.name) < options.reorderButtonGroups.indexOf(b.name)) return -1;
                if (options.reorderButtonGroups.indexOf(a.name) > options.reorderButtonGroups.indexOf(b.name)) return 1;
                return 0;
              });
        }

        // Build the buttons
        if (allBtnGroups.length > 0) {
          editorHeader = this.__buildButtons([allBtnGroups], editorHeader);
        }

        if (options.fullscreen.enable) {
          editorHeader.append('<div class="md-controls"><a class="md-control md-control-fullscreen" href="#"><span class="'+this.__getIcon(options.fullscreen.icons.fullscreenOn)+'"></span></a></div>').on('click', '.md-control-fullscreen', function(e) {
              e.preventDefault();
              instance.setFullscreen(true);
          });
        }

        editor.append(editorHeader);

        // Wrap the textarea
        if (container.is('textarea')) {
          container.before(editor);
          textarea = container;
          textarea.addClass('md-input');
          editor.append(textarea);
        } else {
          var rawContent = (typeof toMarkdown == 'function') ? toMarkdown(container.html()) : container.html(),
              currentContent = $.trim(rawContent);

          // This is some arbitrary content that could be edited
          textarea = $('<textarea/>', {
                       'class': 'md-input',
                       'val' : currentContent
                      });

          editor.append(textarea);

          // Save the editable
          editable.el = container;
          editable.type = container.prop('tagName').toLowerCase();
          editable.content = container.html();

          $(container[0].attributes).each(function(){
            editable.attrKeys.push(this.nodeName);
            editable.attrValues.push(this.nodeValue);
          });

          // Set editor to blocked the original container
          container.replaceWith(editor);
        }

        var editorFooter = $('<div/>', {
                           'class': 'md-footer'
                         }),
            createFooter = false,
            footer = '';
        // Create the footer if savable
        if (options.savable) {
          createFooter = true;
          var saveHandler = 'cmdSave';

          // Register handler and callback
          handler.push(saveHandler);
          callback.push(options.onSave);

          editorFooter.append('<button class="btn btn-success" data-provider="'
                              + ns
                              + '" data-handler="'
                              + saveHandler
                              + '"><i class="icon icon-white icon-ok"></i> '
                              + this.__localize('Save')
                              + '</button>');


        }

        footer = typeof options.footer === 'function' ? options.footer(this) : options.footer;

        if ($.trim(footer) !== '') {
          createFooter = true;
          editorFooter.append(footer);
        }

        if (createFooter) editor.append(editorFooter);

        // Set width
        if (options.width && options.width !== 'inherit') {
          if (jQuery.isNumeric(options.width)) {
            editor.css('display', 'table');
            textarea.css('width', options.width + 'px');
          } else {
            editor.addClass(options.width);
          }
        }

        // Set height
        if (options.height && options.height !== 'inherit') {
          if (jQuery.isNumeric(options.height)) {
            var height = options.height;
            if (editorHeader) height = Math.max(0, height - editorHeader.outerHeight());
            if (editorFooter) height = Math.max(0, height - editorFooter.outerHeight());
            textarea.css('height', height + 'px');
          } else {
            editor.addClass(options.height);
          }
        }

        // Reference
        this.$editor     = editor;
        this.$textarea   = textarea;
        this.$editable   = editable;
        this.$oldContent = this.getContent();

        this.__setListener();

        // Set editor attributes, data short-hand API and listener
        this.$editor.attr('id',(new Date()).getTime());
        this.$editor.on('click', '[data-provider="bootstrap-markdown"]', $.proxy(this.__handle, this));

        if (this.$element.is(':disabled') || this.$element.is('[readonly]')) {
          this.$editor.addClass('md-editor-disabled');
          this.disableButtons('all');
        }

        if (this.eventSupported('keydown') && typeof jQuery.hotkeys === 'object') {
          editorHeader.find('[data-provider="bootstrap-markdown"]').each(function() {
            var $button = $(this),
                hotkey = $button.attr('data-hotkey');
            if (hotkey.toLowerCase() !== '') {
              textarea.bind('keydown', hotkey, function() {
                $button.trigger('click');
                return false;
              });
            }
          });
        }

        if (options.initialstate === 'preview') {
          this.showPreview();
        } else if (options.initialstate === 'fullscreen' && options.fullscreen.enable) {
          this.setFullscreen(true);
        }

      } else {
        this.$editor.show();
      }

      if (options.autofocus) {
        this.$textarea.focus();
        this.$editor.addClass('active');
      }

      if (options.fullscreen.enable && options.fullscreen !== false) {
        this.$editor.append('<div class="md-fullscreen-controls">'
                        + '<a href="#" class="exit-fullscreen" title="Exit fullscreen"><span class="' + this.__getIcon(options.fullscreen.icons.fullscreenOff) + '">'
                        + '</span></a>'
                        + '</div>');
        this.$editor.on('click', '.exit-fullscreen', function(e) {
          e.preventDefault();
          instance.setFullscreen(false);
        });
      }

      // hide hidden buttons from options
      this.hideButtons(options.hiddenButtons);

      // disable disabled buttons from options
      this.disableButtons(options.disabledButtons);

      // Trigger the onShow hook
      options.onShow(this);

      return this;
    }

  , parseContent: function(val) {
      var content;

      // parse with supported markdown parser
      var val = val || this.$textarea.val();

      if (this.$options.parser) {
        content = this.$options.parser(val);
      } else if (typeof markdown == 'object') {
        content = markdown.toHTML(val);
      } else if (typeof marked == 'function') {
        content = marked(val);
      } else {
        content = val;
      }

      return content;
    }

  , showPreview: function() {
      var options = this.$options,
          container = this.$textarea,
          afterContainer = container.next(),
          replacementContainer = $('<div/>',{'class':'md-preview','data-provider':'markdown-preview'}),
          content,
          callbackContent;

      if (this.$isPreview == true) {
        // Avoid sequenced element creation on missused scenario
        // @see https://github.com/toopay/bootstrap-markdown/issues/170
        return this;
      }
      
      // Give flag that tell the editor enter preview mode
      this.$isPreview = true;
      // Disable all buttons
      this.disableButtons('all').enableButtons('cmdPreview');

      // Try to get the content from callback
      callbackContent = options.onPreview(this);
      // Set the content based from the callback content if string otherwise parse value from textarea
      content = typeof callbackContent == 'string' ? callbackContent : this.parseContent();

      // Build preview element
      replacementContainer.html(content);

      if (afterContainer && afterContainer.attr('class') == 'md-footer') {
        // If there is footer element, insert the preview container before it
        replacementContainer.insertBefore(afterContainer);
      } else {
        // Otherwise, just append it after textarea
        container.parent().append(replacementContainer);
      }

      // Set the preview element dimensions
      replacementContainer.css({
        width: container.outerWidth() + 'px',
        height: container.outerHeight() + 'px'
      });

      if (this.$options.resize) {
        replacementContainer.css('resize',this.$options.resize);
      }

      // Hide the last-active textarea
      container.hide();

      // Attach the editor instances
      replacementContainer.data('markdown',this);

      if (this.$element.is(':disabled') || this.$element.is('[readonly]')) {
        this.$editor.addClass('md-editor-disabled');
        this.disableButtons('all');
      }

      return this;
    }

  , hidePreview: function() {
      // Give flag that tell the editor quit preview mode
      this.$isPreview = false;

      // Obtain the preview container
      var container = this.$editor.find('div[data-provider="markdown-preview"]');

      // Remove the preview container
      container.remove();

      // Enable all buttons
      this.enableButtons('all');
      // Disable configured disabled buttons
      this.disableButtons(this.$options.disabledButtons);

      // Back to the editor
      this.$textarea.show();
      this.__setListener();

      return this;
    }

  , isDirty: function() {
      return this.$oldContent != this.getContent();
    }

  , getContent: function() {
      return this.$textarea.val();
    }

  , setContent: function(content) {
      this.$textarea.val(content);

      return this;
    }

  , findSelection: function(chunk) {
    var content = this.getContent(), startChunkPosition;

    if (startChunkPosition = content.indexOf(chunk), startChunkPosition >= 0 && chunk.length > 0) {
      var oldSelection = this.getSelection(), selection;

      this.setSelection(startChunkPosition,startChunkPosition+chunk.length);
      selection = this.getSelection();

      this.setSelection(oldSelection.start,oldSelection.end);

      return selection;
    } else {
      return null;
    }
  }

  , getSelection: function() {

      var e = this.$textarea[0];

      return (

          ('selectionStart' in e && function() {
              var l = e.selectionEnd - e.selectionStart;
              return { start: e.selectionStart, end: e.selectionEnd, length: l, text: e.value.substr(e.selectionStart, l) };
          }) ||

          /* browser not supported */
          function() {
            return null;
          }

      )();

    }

  , setSelection: function(start,end) {

      var e = this.$textarea[0];

      return (

          ('selectionStart' in e && function() {
              e.selectionStart = start;
              e.selectionEnd = end;
              return;
          }) ||

          /* browser not supported */
          function() {
            return null;
          }

      )();

    }

  , replaceSelection: function(text) {

      var e = this.$textarea[0];

      return (

          ('selectionStart' in e && function() {
              e.value = e.value.substr(0, e.selectionStart) + text + e.value.substr(e.selectionEnd, e.value.length);
              // Set cursor to the last replacement end
              e.selectionStart = e.value.length;
              return this;
          }) ||

          /* browser not supported */
          function() {
              e.value += text;
              return jQuery(e);
          }

      )();
    }

  , getNextTab: function() {
      // Shift the nextTab
      if (this.$nextTab.length === 0) {
        return null;
      } else {
        var nextTab, tab = this.$nextTab.shift();

        if (typeof tab == 'function') {
          nextTab = tab();
        } else if (typeof tab == 'object' && tab.length > 0) {
          nextTab = tab;
        }

        return nextTab;
      }
    }

  , setNextTab: function(start,end) {
      // Push new selection into nextTab collections
      if (typeof start == 'string') {
        var that = this;
        this.$nextTab.push(function(){
          return that.findSelection(start);
        });
      } else if (typeof start == 'number' && typeof end == 'number') {
        var oldSelection = this.getSelection();

        this.setSelection(start,end);
        this.$nextTab.push(this.getSelection());

        this.setSelection(oldSelection.start,oldSelection.end);
      }

      return;
    }

  , __parseButtonNameParam: function (names) {
      return typeof names == 'string' ?
                      names.split(' ') :
                      names;

    }

  , enableButtons: function(name) {
      var buttons = this.__parseButtonNameParam(name),
        that = this;

      $.each(buttons, function(i, v) {
        that.__alterButtons(buttons[i], function (el) {
          el.removeAttr('disabled');
        });
      });

      return this;
    }

  , disableButtons: function(name) {
      var buttons = this.__parseButtonNameParam(name),
        that = this;

      $.each(buttons, function(i, v) {
        that.__alterButtons(buttons[i], function (el) {
          el.attr('disabled','disabled');
        });
      });

      return this;
    }

  , hideButtons: function(name) {
      var buttons = this.__parseButtonNameParam(name),
        that = this;

      $.each(buttons, function(i, v) {
        that.__alterButtons(buttons[i], function (el) {
          el.addClass('hidden');
        });
      });

      return this;
    }

  , showButtons: function(name) {
      var buttons = this.__parseButtonNameParam(name),
        that = this;

      $.each(buttons, function(i, v) {
        that.__alterButtons(buttons[i], function (el) {
          el.removeClass('hidden');
        });
      });

      return this;
    }

  , eventSupported: function(eventName) {
      var isSupported = eventName in this.$element;
      if (!isSupported) {
        this.$element.setAttribute(eventName, 'return;');
        isSupported = typeof this.$element[eventName] === 'function';
      }
      return isSupported;
    }

  , keyup: function (e) {
      var blocked = false;
      switch(e.keyCode) {
        case 40: // down arrow
        case 38: // up arrow
        case 16: // shift
        case 17: // ctrl
        case 18: // alt
          break;

        case 9: // tab
          var nextTab;
          if (nextTab = this.getNextTab(),nextTab !== null) {
            // Get the nextTab if exists
            var that = this;
            setTimeout(function(){
              that.setSelection(nextTab.start,nextTab.end);
            },500);

            blocked = true;
          } else {
            // The next tab memory contains nothing...
            // check the cursor position to determine tab action
            var cursor = this.getSelection();

            if (cursor.start == cursor.end && cursor.end == this.getContent().length) {
              // The cursor already reach the end of the content
              blocked = false;
            } else {
              // Put the cursor to the end
              this.setSelection(this.getContent().length,this.getContent().length);

              blocked = true;
            }
          }

          break;

        case 13: // enter
          blocked = false;
          break;
        case 27: // escape
          if (this.$isFullscreen) this.setFullscreen(false);
          blocked = false;
          break;

        default:
          blocked = false;
      }

      if (blocked) {
        e.stopPropagation();
        e.preventDefault();
      }

      this.$options.onChange(this);
    }

  , change: function(e) {
      this.$options.onChange(this);
      return this;
    }
  , select: function (e) {
      this.$options.onSelect(this);
      return this;
    }
  , focus: function (e) {
      var options = this.$options,
          isHideable = options.hideable,
          editor = this.$editor;

      editor.addClass('active');

      // Blur other markdown(s)
      $(document).find('.md-editor').each(function(){
        if ($(this).attr('id') !== editor.attr('id')) {
          var attachedMarkdown;

          if (attachedMarkdown = $(this).find('textarea').data('markdown'),
              attachedMarkdown === null) {
              attachedMarkdown = $(this).find('div[data-provider="markdown-preview"]').data('markdown');
          }

          if (attachedMarkdown) {
            attachedMarkdown.blur();
          }
        }
      });

      // Trigger the onFocus hook
      options.onFocus(this);

      return this;
    }

  , blur: function (e) {
      var options = this.$options,
          isHideable = options.hideable,
          editor = this.$editor,
          editable = this.$editable;

      if (editor.hasClass('active') || this.$element.parent().length === 0) {
        editor.removeClass('active');

        if (isHideable) {
          // Check for editable elements
          if (editable.el !== null) {
            // Build the original element
            var oldElement = $('<'+editable.type+'/>'),
                content = this.getContent(),
                currentContent = this.parseContent(content);

            $(editable.attrKeys).each(function(k,v) {
              oldElement.attr(editable.attrKeys[k],editable.attrValues[k]);
            });

            // Get the editor content
            oldElement.html(currentContent);

            editor.replaceWith(oldElement);
          } else {
            editor.hide();
          }
        }

        // Trigger the onBlur hook
        options.onBlur(this);
      }

      return this;
    }

  };

 /* MARKDOWN PLUGIN DEFINITION
  * ========================== */

  var old = $.fn.markdown;

  $.fn.markdown = function (option) {
    return this.each(function () {
      var $this = $(this)
        , data = $this.data('markdown')
        , options = typeof option == 'object' && option;
      if (!data) $this.data('markdown', (data = new Markdown(this, options)))
    })
  };

  $.fn.markdown.messages = {};

  $.fn.markdown.defaults = {
    /* Editor Properties */
    autofocus: false,
    hideable: false,
    savable: false,
    width: 'inherit',
    height: 'inherit',
    resize: 'none',
    iconlibrary: 'glyph',
    language: 'en',
    initialstate: 'editor',
    parser: null,

    /* Buttons Properties */
    buttons: [
      [{
        name: 'groupFont',
        data: [{
          name: 'cmdBold',
          hotkey: 'Ctrl+B',
          title: 'Bold',
          icon: { glyph: 'glyphicon glyphicon-bold', fa: 'fa fa-bold', 'fa-3': 'icon-bold' },
          callback: function(e){
            // Give/remove ** surround the selection
            var chunk, cursor, selected = e.getSelection(), content = e.getContent();

            if (selected.length === 0) {
              // Give extra word
              chunk = e.__localize('strong text');
            } else {
              chunk = selected.text;
            }

            // transform selection and set the cursor into chunked text
            if (content.substr(selected.start-2,2) === '**'
                && content.substr(selected.end,2) === '**' ) {
              e.setSelection(selected.start-2,selected.end+2);
              e.replaceSelection(chunk);
              cursor = selected.start-2;
            } else {
              e.replaceSelection('**'+chunk+'**');
              cursor = selected.start+2;
            }

            // Set the cursor
            e.setSelection(cursor,cursor+chunk.length);
          }
        },{
          name: 'cmdItalic',
          title: 'Italic',
          hotkey: 'Ctrl+I',
          icon: { glyph: 'glyphicon glyphicon-italic', fa: 'fa fa-italic', 'fa-3': 'icon-italic' },
          callback: function(e){
            // Give/remove * surround the selection
            var chunk, cursor, selected = e.getSelection(), content = e.getContent();

            if (selected.length === 0) {
              // Give extra word
              chunk = e.__localize('emphasized text');
            } else {
              chunk = selected.text;
            }

            // transform selection and set the cursor into chunked text
            if (content.substr(selected.start-1,1) === '_'
                && content.substr(selected.end,1) === '_' ) {
              e.setSelection(selected.start-1,selected.end+1);
              e.replaceSelection(chunk);
              cursor = selected.start-1;
            } else {
              e.replaceSelection('_'+chunk+'_');
              cursor = selected.start+1;
            }

            // Set the cursor
            e.setSelection(cursor,cursor+chunk.length);
          }
        },{
          name: 'cmdHeading',
          title: 'Heading',
          hotkey: 'Ctrl+H',
          icon: { glyph: 'glyphicon glyphicon-header', fa: 'fa fa-header', 'fa-3': 'icon-font' },
          callback: function(e){
            // Append/remove ### surround the selection
            var chunk, cursor, selected = e.getSelection(), content = e.getContent(), pointer, prevChar;

            if (selected.length === 0) {
              // Give extra word
              chunk = e.__localize('heading text');
            } else {
              chunk = selected.text + '\n';
            }

            // transform selection and set the cursor into chunked text
            if ((pointer = 4, content.substr(selected.start-pointer,pointer) === '### ')
                || (pointer = 3, content.substr(selected.start-pointer,pointer) === '###')) {
              e.setSelection(selected.start-pointer,selected.end);
              e.replaceSelection(chunk);
              cursor = selected.start-pointer;
            } else if (selected.start > 0 && (prevChar = content.substr(selected.start-1,1), !!prevChar && prevChar != '\n')) {
              e.replaceSelection('\n\n### '+chunk);
              cursor = selected.start+6;
            } else {
              // Empty string before element
              e.replaceSelection('### '+chunk);
              cursor = selected.start+4;
            }

            // Set the cursor
            e.setSelection(cursor,cursor+chunk.length);
          }
        }]
      },{
        name: 'groupLink',
        data: [{
          name: 'cmdUrl',
          title: 'URL/Link',
          hotkey: 'Ctrl+L',
          icon: { glyph: 'glyphicon glyphicon-link', fa: 'fa fa-link', 'fa-3': 'icon-link' },
          callback: function(e){
            // Give [] surround the selection and prepend the link
            var chunk, cursor, selected = e.getSelection(), content = e.getContent(), link;

            if (selected.length === 0) {
              // Give extra word
              chunk = e.__localize('enter link description here');
            } else {
              chunk = selected.text;
            }

            link = prompt(e.__localize('Insert Hyperlink'),'http://');

            if (link !== null && link !== '' && link !== 'http://' && link.substr(0,4) === 'http') {
              var sanitizedLink = $('<div>'+link+'</div>').text();

              // transform selection and set the cursor into chunked text
              e.replaceSelection('['+chunk+']('+sanitizedLink+')');
              cursor = selected.start+1;

              // Set the cursor
              e.setSelection(cursor,cursor+chunk.length);
            }
          }
        },{
          name: 'cmdImage',
          title: 'Image',
          hotkey: 'Ctrl+G',
          icon: { glyph: 'glyphicon glyphicon-picture', fa: 'fa fa-picture-o', 'fa-3': 'icon-picture' },
          callback: function(e){
            // Give ![] surround the selection and prepend the image link
            var chunk, cursor, selected = e.getSelection(), content = e.getContent(), link;

            if (selected.length === 0) {
              // Give extra word
              chunk = e.__localize('enter image description here');
            } else {
              chunk = selected.text;
            }

            link = prompt(e.__localize('Insert Image Hyperlink'),'http://');

            if (link !== null && link !== '' && link !== 'http://' && link.substr(0,4) === 'http') {
              var sanitizedLink = $('<div>'+link+'</div>').text();

              // transform selection and set the cursor into chunked text
              e.replaceSelection('!['+chunk+']('+sanitizedLink+' "'+e.__localize('enter image title here')+'")');
              cursor = selected.start+2;

              // Set the next tab
              e.setNextTab(e.__localize('enter image title here'));

              // Set the cursor
              e.setSelection(cursor,cursor+chunk.length);
            }
          }
        }]
      },{
        name: 'groupMisc',
        data: [{
          name: 'cmdList',
          hotkey: 'Ctrl+U',
          title: 'Unordered List',
          icon: { glyph: 'glyphicon glyphicon-list', fa: 'fa fa-list', 'fa-3': 'icon-list-ul' },
          callback: function(e){
            // Prepend/Give - surround the selection
            var chunk, cursor, selected = e.getSelection(), content = e.getContent();

            // transform selection and set the cursor into chunked text
            if (selected.length === 0) {
              // Give extra word
              chunk = e.__localize('list text here');

              e.replaceSelection('- '+chunk);
              // Set the cursor
              cursor = selected.start+2;
            } else {
              if (selected.text.indexOf('\n') < 0) {
                chunk = selected.text;

                e.replaceSelection('- '+chunk);

                // Set the cursor
                cursor = selected.start+2;
              } else {
                var list = [];

                list = selected.text.split('\n');
                chunk = list[0];

                $.each(list,function(k,v) {
                  list[k] = '- '+v;
                });

                e.replaceSelection('\n\n'+list.join('\n'));

                // Set the cursor
                cursor = selected.start+4;
              }
            }

            // Set the cursor
            e.setSelection(cursor,cursor+chunk.length);
          }
        },
        {
          name: 'cmdListO',
          hotkey: 'Ctrl+O',
          title: 'Ordered List',
          icon: { glyph: 'glyphicon glyphicon-th-list', fa: 'fa fa-list-ol', 'fa-3': 'icon-list-ol' },
          callback: function(e) {

            // Prepend/Give - surround the selection
            var chunk, cursor, selected = e.getSelection(), content = e.getContent();

            // transform selection and set the cursor into chunked text
            if (selected.length === 0) {
              // Give extra word
              chunk = e.__localize('list text here');
              e.replaceSelection('1. '+chunk);
              // Set the cursor
              cursor = selected.start+3;
            } else {
              if (selected.text.indexOf('\n') < 0) {
                chunk = selected.text;

                e.replaceSelection('1. '+chunk);

                // Set the cursor
                cursor = selected.start+3;
              } else {
                var list = [];

                list = selected.text.split('\n');
                chunk = list[0];

                $.each(list,function(k,v) {
                  list[k] = '1. '+v;
                });

                e.replaceSelection('\n\n'+list.join('\n'));

                // Set the cursor
                cursor = selected.start+5;
              }
            }

            // Set the cursor
            e.setSelection(cursor,cursor+chunk.length);
          }
        },
        {
          name: 'cmdCode',
          hotkey: 'Ctrl+K',
          title: 'Code',
          icon: { glyph: 'glyphicon glyphicon-asterisk', fa: 'fa fa-code', 'fa-3': 'icon-code' },
          callback: function(e) {
            // Give/remove ** surround the selection
            var chunk, cursor, selected = e.getSelection(), content = e.getContent();

            if (selected.length === 0) {
              // Give extra word
              chunk = e.__localize('code text here');
            } else {
              chunk = selected.text;
            }

            // transform selection and set the cursor into chunked text
            if (content.substr(selected.start-4,4) === '```\n'
                && content.substr(selected.end,4) === '\n```') {
              e.setSelection(selected.start-4, selected.end+4);
              e.replaceSelection(chunk);
              cursor = selected.start-4;
            } else if (content.substr(selected.start-1,1) === '`'
                && content.substr(selected.end,1) === '`') {
              e.setSelection(selected.start-1,selected.end+1);
              e.replaceSelection(chunk);
              cursor = selected.start-1;
            } else if (content.indexOf('\n') > -1) {
              e.replaceSelection('```\n'+chunk+'\n```');
              cursor = selected.start+4;
            } else {
              e.replaceSelection('`'+chunk+'`');
              cursor = selected.start+1;
            }

            // Set the cursor
            e.setSelection(cursor,cursor+chunk.length);
          }
        },
        {
          name: 'cmdQuote',
          hotkey: 'Ctrl+Q',
          title: 'Quote',
          icon: { glyph: 'glyphicon glyphicon-comment', fa: 'fa fa-quote-left', 'fa-3': 'icon-quote-left' },
          callback: function(e) {
            // Prepend/Give - surround the selection
            var chunk, cursor, selected = e.getSelection(), content = e.getContent();

            // transform selection and set the cursor into chunked text
            if (selected.length === 0) {
              // Give extra word
              chunk = e.__localize('quote here');

              e.replaceSelection('> '+chunk);

              // Set the cursor
              cursor = selected.start+2;
            } else {
              if (selected.text.indexOf('\n') < 0) {
                chunk = selected.text;

                e.replaceSelection('> '+chunk);

                // Set the cursor
                cursor = selected.start+2;
              } else {
                var list = [];

                list = selected.text.split('\n');
                chunk = list[0];

                $.each(list,function(k,v) {
                  list[k] = '> '+v;
                });

                e.replaceSelection('\n\n'+list.join('\n'));

                // Set the cursor
                cursor = selected.start+4;
              }
            }

            // Set the cursor
            e.setSelection(cursor,cursor+chunk.length);
          }
        }]
      },{
        name: 'groupUtil',
        data: [{
          name: 'cmdPreview',
          toggle: true,
          hotkey: 'Ctrl+P',
          title: 'Preview',
          btnText: 'Preview',
          btnClass: 'btn btn-primary btn-sm',
          icon: { glyph: 'glyphicon glyphicon-search', fa: 'fa fa-search', 'fa-3': 'icon-search' },
          callback: function(e){
            // Check the preview mode and toggle based on this flag
            var isPreview = e.$isPreview,content;

            if (isPreview === false) {
              // Give flag that tell the editor enter preview mode
              e.showPreview();
            } else {
              e.hidePreview();
            }
          }
        }]
      }]
    ],
    additionalButtons:[], // Place to hook more buttons by code
    reorderButtonGroups:[],
    hiddenButtons:[], // Default hidden buttons
    disabledButtons:[], // Default disabled buttons
    footer: '',
    fullscreen: {
      enable: true,
      icons: {
        fullscreenOn: {
          fa: 'fa fa-expand',
          glyph: 'glyphicon glyphicon-fullscreen',
          'fa-3': 'icon-resize-full'
        },
        fullscreenOff: {
          fa: 'fa fa-compress',
          glyph: 'glyphicon glyphicon-fullscreen',
          'fa-3': 'icon-resize-small'
        }
      }
    },

    /* Events hook */
    onShow: function (e) {},
    onPreview: function (e) {},
    onSave: function (e) {},
    onBlur: function (e) {},
    onFocus: function (e) {},
    onChange: function(e) {},
    onFullscreen: function(e) {},
    onSelect: function (e) {}
  };

  $.fn.markdown.Constructor = Markdown;


 /* MARKDOWN NO CONFLICT
  * ==================== */

  $.fn.markdown.noConflict = function () {
    $.fn.markdown = old;
    return this;
  };

  /* MARKDOWN GLOBAL FUNCTION & DATA-API
  * ==================================== */
  var initMarkdown = function(el) {
    var $this = el;

    if ($this.data('markdown')) {
      $this.data('markdown').showEditor();
      return;
    }

    $this.markdown()
  };

  var blurNonFocused = function(e) {
    var $activeElement = $(document.activeElement);

    // Blur event
    $(document).find('.md-editor').each(function(){
      var $this            = $(this),
          focused          = $activeElement.closest('.md-editor')[0] === this,
          attachedMarkdown = $this.find('textarea').data('markdown') ||
                             $this.find('div[data-provider="markdown-preview"]').data('markdown');

      if (attachedMarkdown && !focused) {
        attachedMarkdown.blur();
      }
    })
  };

  $(document)
    .on('click.markdown.data-api', '[data-provide="markdown-editable"]', function (e) {
      initMarkdown($(this));
      e.preventDefault();
    })
    .on('click focusin', function (e) {
      blurNonFocused(e);
    })
    .ready(function(){
      $('textarea[data-provide="markdown"]').each(function(){
        initMarkdown($(this));
      })
    });

}(window.jQuery);

/*
 * Arabic translation for bootstrap-markdown
 * George Ajam <george.ejaam@gmail.com>
 */
(function ($) {
    $.fn.markdown.messages.nl = {
        'Bold': "غامق",
        'Italic': "مائل",
        'Heading': "عنوان",
        'URL/Link': "URL/رابط",
        'Image': "صورة",
        'List': "قائمة",
        'Preview': "استعراض",
        'strong text': "نص غامق",
        'emphasized text': "نص هام",
        'heading text': "العنوان",
        'enter link description here': "ادخل وصف الرابط هنا",
        'Insert Hyperlink': "ادخل الرابط هنا",
        'enter image description here': "ادخل وصف الصورة هنا",
        'Insert Image Hyperlink': "ادخل رابط الصورة هنا",
        'enter image title here': "ادخل عنوان الصورة هنا",
        'list text here': "اكتب النص هنا"
    };
}(jQuery));

/**
 * Czech translation for bootstrap-markdown
 * Vít Kabele <vit@kabele.me>
 */
(function ($) {
    $.fn.markdown.messages.cs = {
        'Bold': "Tučně",
        'Italic': "Kurzíva",
        'Heading': "Nadpis",
        'URL/Link': "URL/Odkaz",
        'Image': "Obrázek",
        'Unordered List': "Seznam",
        'Ordered List': "Seřazený seznam",
        'Code': "Úsek kódu",
        'Quote': "Citace",
        'Preview': "Náhled",
        'strong text': "tučný text",
        'emphasized text': "zdůrazněný text",
        'heading text': "text nadpisu",
        'enter link description here': "sem vlož popis odkazu",
        'Insert Hyperlink': "Vložit Hyperlink",
        'enter image description here': "sem vlož popis obrázku",
        'Insert Image Hyperlink': "Vlož adresu obrázku",
        'enter image title here': "sem vlož popis obrázku",
        'list text here': "položka seznamu"
    };
}(jQuery));

/**
 * Danish translation for bootstrap-markdown
 * Dan Storm <storm@catalystcode.net>
 */
(function ($) {
    $.fn.markdown.messages.nb = {
        'Bold': 'Fed',
        'Italic': 'Kursiv',
        'Heading': 'Overskrift',
        'URL/Link': 'URL/Link',
        'Image': 'Billede',
        'List': 'Liste',
        'Preview': 'Forhåndsvisning',
        'strong text': 'stærk tekst',
        'emphasized text': 'fremhævet tekst',
        'heading text': 'overskrift tekst',
        'enter link description here': 'Skriv link beskrivelse her',
        'Insert Hyperlink': 'Indsæt link',
        'enter image description here': 'Indsæt billede beskrivelse her',
        'Insert Image Hyperlink': 'Indsæt billede link',
        'enter image title here': 'Indsæt billede titel',
        'list text here': 'Indsæt liste tekst her',
        'quote here': 'Indsæt citat her',
        'code text here': 'Indsæt kode her'
    };
}(jQuery));

/**
 * German translation for bootstrap-markdown
 * Tobias Nitsche <tobias-nitsche@gmx.net>
 */
(function ($) {
    $.fn.markdown.messages.de = {
        'Bold': "Fett",
        'Italic': "Kursiv",
        'Heading': "Überschrift",
        'URL/Link': "Link hinzufügen",
        'Image': "Bild hinzufügen",
        'Unordered List': "Unnummerierte Liste",
        'Ordered List': "Nummerierte Liste",
        'Code': "Quelltext",
        'Quote': "Zitat",
        'Preview': "Vorschau",
        'strong text': "Sehr betonter Text",
        'emphasized text': "Betonter Text",
        'heading text': "Überschrift Text",
        'enter link description here': "Linkbeschreibung",
        'Insert Hyperlink': "URL",
        'enter image description here': "Bildbeschreibung",
        'Insert Image Hyperlink': "Bild-URL",
        'enter image title here': "Titel des Bildes",
        'list text here': "Aufzählungs-Text"
    };
}(jQuery));

/**
 * Spanish translation for bootstrap-markdown
 * by Leandro Poblet <leandrodrhouse@gmail.com>
 */
;(function($){
    $.fn.markdown.messages['es'] = {
        'Bold': "Negrita",
        'Italic': "Itálica",
        'Heading': "Título",
        'URL/Link': "Inserte un link",
        'Image': "Inserte una imagen",
        'List': "Lista de items",
        'Preview': "Previsualizar",
        'strong text': "texto importante",
        'emphasized text': "texto con énfasis",
        'heading text': "texto titular",
        'enter link description here': "descripción del link",
        'Insert Hyperlink': "Inserte un hipervínculo",
        'enter image description here': "descripción de la imagen",
        'Insert Image Hyperlink': "Inserte una imagen con un hipervínculo",
        'enter image title here': "Inserte una imagen con título",
        'list text here': "lista con texto"
    };
}(jQuery));

/**
 * French translation for bootstrap-markdown
 * Benoît Bourgeois <bierdok@gmail.com>
 */
(function ($) {
    $.fn.markdown.messages.fr = {
        'Bold': "Gras",
        'Italic': "Italique",
        'Heading': "Titre",
        'URL/Link': "Insérer un lien HTTP",
        'Image': "Insérer une image",
        'List': "Liste à puces",
        'Preview': "Prévisualiser",
        'strong text': "texte important",
        'emphasized text': "texte souligné",
        'heading text': "texte d'entête",
        'enter link description here': "entrez la description du lien ici",
        'Insert Hyperlink': "Insérez le lien hypertexte",
        'enter image description here': "entrez la description de l'image ici",
        'Insert Image Hyperlink': "Insérez le lien hypertexte de l'image",
        'enter image title here': "entrez le titre de l'image ici",
        'list text here': "texte à puce ici"
    };
}(jQuery));

/**
 * Japanese translation for bootstrap-markdown
 * Kenta Murakami <kntmrkm@gmail.com>
 */
(function ($) {
    $.fn.markdown.messages['ja'] = {
        'Bold': "太字",
        'Italic': "斜体",
        'Heading': "見出し",
        'URL/Link': "リンク",
        'Image': "画像",
        'Unordered List': "リスト",
        'Ordered List': "数字リスト",
        'Code': "コード",
        'Quote': "引用",
        'Preview': "プレビュー",
        'strong text': "太字",
        'emphasized text': "強調",
        'heading text': "見出し",
        'enter link description here': "リンク説明",
        'Insert Hyperlink': "リンク挿入",
        'enter image description here': "画像説明",
        'Insert Image Hyperlink': "画像挿入",
        'enter image title here': "画像タイトル",
        'list text here': "リスト挿入",
        'code text here': "コード",
        'quote here': "引用挿入"
    };
}(jQuery));

/**
 + * Korean translation for bootstrap-markdown
 + * WoongBi Kim <ssinss@gmail.com>
 + */
;(function($){
    $.fn.markdown.messages['kr'] = {
        'Bold': "진하게",
        'Italic': "이탤릭체",
        'Heading': "머리글",
        'URL/Link': "링크주소",
        'Image': "이미지",
        'List': "리스트",
        'Preview': "미리보기",
        'strong text': "강한 강조 텍스트",
        'emphasized text': "강조 텍스트",
        'heading text': "머리글 텍스트",
        'enter link description here': "여기에 링크의 설명을 적으세요",
        'Insert Hyperlink': "하이퍼링크 삽입",
        'enter image description here': "여기세 이미지 설명을 적으세요",
        'Insert Image Hyperlink': "이미지 링크 삽입",
        'enter image title here': "여기에 이미지 제목을 적으세요",
        'list text here': "리스트 텍스트"
    };
}(jQuery));

/**
 * Norwegian bokmål translation for bootstrap-markdown
 * Tobias Bohwalli <hi@futhr.io>
 */
(function ($) {
    $.fn.markdown.messages.nb = {
        'Bold': 'Fet',
        'Italic': 'Kursiv',
        'Heading': 'Overskrift',
        'URL/Link': 'URL/Lenke',
        'Image': 'Bilde',
        'List': 'Liste',
        'Preview': 'Forhåndsvisning',
        'strong text': 'sterk tekst',
        'emphasized text': 'streket tekst',
        'heading text': 'overskriften tekst',
        'enter link description here': 'Skriv linken beskrivelse her',
        'Insert Hyperlink': 'Sett inn lenke',
        'enter image description here': 'Angi bildebeskrivelse her',
        'Insert Image Hyperlink': 'Sett inn lenke for bilde',
        'enter image title here': 'Angi bildetittel her',
        'list text here': 'liste tekst her'
    };
}(jQuery));

/**
 * Dutch translation for bootstrap-markdown
 * Jeroen Thora <jeroenthora@gmail.com>
 */
(function ($) {
    $.fn.markdown.messages.nl = {
        'Bold': "Vet",
        'Italic': "Cursief",
        'Heading': "Titel",
        'URL/Link': "URL/Link",
        'Image': "Afbeelding",
        'List': "Lijst",
        'Preview': "Voorbeeld",
        'strong text': "vet gedrukte tekst",
        'emphasized text': "schuin gedrukte tekst",
        'heading text': "Titel",
        'enter link description here': "Voer een link beschrijving in",
        'Insert Hyperlink': "Voer een http link in",
        'enter image description here': "Voer een afbeelding beschrijving in",
        'Insert Image Hyperlink': "Voer een afbeelding link in",
        'enter image title here': "Voer de afbeelding titel in",
        'list text here': "lijst item"
    };
}(jQuery));

/**
 * Polish translation for bootstrap-markdown
 * Marek Kaput
 */
(function ($) {
    $.fn.markdown.messages.pl = {
        'Bold': "Pogrubienie",
        'Italic': "Kursywa",
        'Heading': "Nagłówek",
        'URL/Link': "Wstaw link",
        'Image': "Wstaw obrazek",
        'Unordered List': "Lista punktowana",
        'Ordered List': "Lista numerowana",
        'Code': "Kod źródłowy",
        'Quote': "Cytat",
        'Preview': "Podgląd",
        'strong text': "pogrubiony tekst",
        'emphasized text': "pochylony tekst",
        'heading text': "nagłówek",
        'enter link description here': "opis linka",
        'Insert Hyperlink': "Wstaw link",
        'enter image description here': "opis obrazka",
        'Insert Image Hyperlink': "Wstaw obrazek",
        'enter image title here': "tytuł obrazka",
        'list text here': "lista"
    };
}(jQuery));

/**
 * Russian translation for bootstrap-markdown
 * by Oleg Vivtash <o@vivtash.net>
 */
;(function($){
    $.fn.markdown.messages['ru'] = {
        'Bold': "Жирный",
        'strong text': "выделенный текст",
        'Italic': "Курсив",
        'emphasized text': "наклонный текст",
        'Heading': "Заголовок",
        'heading text': "текст заголовка",
        'URL/Link': "Вставьте ссылку",
        'Insert Hyperlink': "Введите гиперссылку",
        'enter link description here': "введите описание ссылки",
        'Image': "Изображение",
        'enter image description here': "Введите описание изображения",
        'Insert Image Hyperlink': "Вставьте ссылку на изображение",
        'enter image title here': "Введите название изображения",
        'List': "Список",
        'Unordered List': "Неупорядоченный список",
        'Ordered List': "Упорядоченный список",
        'list text here': "текст списка",
        'Code': "Код",
        'code text here': "программный код",
        'Quote': "Цитата",
        'quote here': "цитируемый текст",
        'Preview': "Предварительный просмотр"
    };
}(jQuery));

/**
 * Slovenian translation for bootstrap-markdown
 * Davor Padovan <davor.padovan@gmail.com>
 */
(function ($) {
    $.fn.markdown.messages.sl = {
        'Bold': "Odebeljeno",
        'Italic': "Poševno",
        'Heading': "Naslov",
        'URL/Link': "Povezava",
        'Image': "Slika",
        'Unordered List': "Neurejen seznam",
        'Ordered List': "Urejen seznam",
        'Code': "Koda",
        'Quote': "Citat",
        'Preview': "Predogled",
        'strong text': "odebeljeno besedilo",
        'emphasized text': "poševno besedilo",
        'heading text': "naslov",
        'enter link description here': "opis povezave",
        'Insert Hyperlink': "Vstavi povezavo",
        'enter image description here': "opis slike",
        'Insert Image Hyperlink': "Vstavi povezavo do slike",
        'enter image title here': "naslov slike",
        'list text here': "seznam"
    };
}(jQuery));

/**
 * Swedish translation for bootstrap-markdown
 * Tobias Bohwalli <hi@futhr.io>
 */
(function ($) {
    $.fn.markdown.messages.sv = {
        'Bold': 'Fet',
        'Italic': 'Kursiv',
        'Heading': 'Rubrik',
        'URL/Link': 'URL/Länk',
        'Image': 'Bild',
        'List': 'Lista',
        'Preview': 'Förhandsgranska',
        'strong text': 'fet text',
        'emphasized text': 'överstruken text',
        'heading text': 'Rubrik',
        'enter link description here': 'Ange länk beskrivning här',
        'Insert Hyperlink': 'Sätt in länk',
        'enter image description here': 'Ange bild beskrivning här',
        'Insert Image Hyperlink': 'Sätt in länk för bild',
        'enter image title here': 'Ange bild rubrik här',
        'list text here': 'list text'
    };
}(jQuery));

/**
 * Turkish translation for bootstrap-markdown
 * Serkan Algur <info@wpadami.com>
 */
(function ($) {
    $.fn.markdown.messages.tr = {
        'Bold': "Kalın",
        'Italic': "İtalik",
        'Heading': "Başlık",
        'URL/Link': "Link ekle",
        'Image': "Resim ekle",
        'List': "Liste Oluşturun",
        'Preview': "Önizleme",
        'strong text': "kalın yazı",
        'emphasized text': "italik yazı",
        'heading text': "Başlık Yazısı",
        'enter link description here': "Link açıklamasını buraya girin",
        'Insert Hyperlink': "İnternet adresi girin",
        'enter image description here': "resim açıklamasını buraya ekleyin",
        'Insert Image Hyperlink': "Resim linkini ekleyin",
        'enter image title here': "resim başlığını buraya ekleyin",
        'list text here': "liste yazısı",
        'Save' : "Kaydet",
        'Ordered List' : "Numaralı Liste",
        'Unordered List' : "Madde imli liste",
        'Quote' : "Alıntı",
        'quote here' : "alıntıyı buraya ekleyin",
        'Code' : "Kod",
        'code text here' : "kodu buraya ekleyin"
    };
}(jQuery));

/**
 * Ukrainian translation for bootstrap-markdown
 * by Oleg Vivtash <o@vivtash.net>
 */
;(function($){
    $.fn.markdown.messages['ua'] = {
        'Bold': "Жирний",
        'Italic': "Курсів",
        'Heading': "Заголовок",
        'URL/Link': "Вставте посилання",
        'Image': "Зображення",
        'List': "Список",
        'Preview': "Попередній перегляд",
        'strong text': "виділений текст",
        'emphasized text': "нахилений текст",
        'heading text': "текст заголовку",
        'enter link description here': "введіть опис посилання",
        'Insert Hyperlink': "Введіть гіперпосилання",
        'enter image description here': "Введіть опис зображення",
        'Insert Image Hyperlink': "Вставте посилання на зображення",
        'enter image title here': "Введіть назву зображення",
        'list text here': "текст списку"
    };
}(jQuery));

/**
 * Chinese translation for bootstrap-markdown
 * benhaile <denghaier@163.com>
 */
(function ($) {
    $.fn.markdown.messages.zh = {
        'Bold': "粗体",
        'Italic': "斜体",
        'Heading': "标题",
        'URL/Link': "链接",
        'Image': "图片",
        'List': "列表",
        'Unordered List': "无序列表",
        'Ordered List': "有序列表",
        'Code': "代码",
        'Quote': "引用",
        'Preview': "预览",
        'strong text': "粗体",
        'emphasized text': "强调",
        'heading text': "标题",
        'enter link description here': "输入链接说明",
        'Insert Hyperlink': "URL地址",
        'enter image description here': "输入图片说明",
        'Insert Image Hyperlink': "图片URL地址",
        'enter image title here': "在这里输入图片标题",
        'list text here': "这里是列表文本",
        'code text here': "这里输入代码",
        'quote here': "这里输入引用文本"


    };
}(jQuery));
