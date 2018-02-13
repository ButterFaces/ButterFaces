///<reference path="../../node_modules/@types/jquery/index.d.ts"/>

/**
 * jQuery-Plugin "Expanded TextAreas" for expandable text areas. It is used for the JSF-Component "b:textarea".
 * Works with at least jQuery 1.7.
 *
 * How to use:
 * jQuery("#someTextAreaSelector").butterExpandable();
 */
(function ($: JQueryStatic) {
    // you have to extend jQuery with the fn["pluginName"] notation because in Typescript you can't extend
    // the existing typing interface with fn.pluginName!
    $.fn["butterExpandable"] = function () {
        return this.each((index, element) => {
            let rootElement: JQuery = $(element);
            if (rootElement.find("textarea").length > 0) {
                new ButterFaces.TextareaExpandable(rootElement);
            } else {
                new ButterFaces.DivExpandable(rootElement);
            }
        });
    };
})(jQuery);

namespace ButterFaces {

    const EXPAND_HEIGHT: number = 250; //in px
    const EXPAND_WIDTH: number = 500; //in px
    const ANIMATION_DURATION: number = 200; //in ms
    const REPOSITION_INTERVAL: number = 500; //in ms
    const EASING: string = "swing";
    const KEYCODE_ESCAPE: number = 27;

    abstract class AbstractExpandable {

        rootElement: JQuery;

        ghostElement: JQuery;
        originalElement: JQuery;
        initialHeight: number;
        initialWidth: number;
        initialOffset: JQuery.Coordinates;
        positionTriggerInterval: number;

        constructor(rootElement: JQuery) {
            this.rootElement = rootElement;
        }

        abstract createGhostElement(): JQuery;

        abstract isExpansionEventIgnored(event: any): boolean;

        abstract onGhostElementCreated(): void;

        abstract onGhostElementCollapsed(isCancelled: boolean): void;

        abstract transferValueToGhostElement(): void;

        expandElement(event: any): void {
            if (this.isExpansionEventIgnored(event)) {
                return;
            }

            this.initialHeight = this.originalElement.outerHeight();
            this.initialWidth = this.originalElement.outerWidth();
            this.initialOffset = this.originalElement.offset();

            //create a ghost element that be animated on gets the focus
            this.ghostElement = this.createGhostElement();
            this.transferValueToGhostElement();
            this.ghostElement.css("width", this.initialWidth)
                .css("height", this.initialHeight)
                .css("position", "absolute")
                .css("top", this.initialOffset.top)
                .css("left", this.initialOffset.left)
                .css("z-index", 2000)
                .css("box-shadow", "5px 5px 5px 0 #999")
                .addClass("butter-component-expandable-ghost")
                .appendTo($("body"))
                .animate({
                    height: EXPAND_HEIGHT,
                    width: this.initialWidth > EXPAND_WIDTH ? this.initialWidth : EXPAND_WIDTH
                }, ANIMATION_DURATION, EASING, () => {
                    $(document)
                        .on("click.expandable", event => {
                            this.handleMouseClick(event);
                        })
                        .on("keydown.expandable", event => {
                            this.handleEscapeKey(event);
                        });

                    $(window).on("resize.expandable", () => {
                        this.repositionGhostElement();
                    });

                    //keep track of the orginal element"s position
                    this.positionTriggerInterval = window.setInterval(() => this.repositionGhostElement, REPOSITION_INTERVAL);
                });

            //make original invisible
            this.originalElement
                .css("visibility", "hidden")
                .siblings()
                .css("visibility", "hidden");

            this.onGhostElementCreated();
        }

        /**
         * Collapses the ghost element and sets the value if not isCancelled
         * @param isCancelled
         */
        collapseElement(cancelled: any): void {
            // "cancelled" can be an event object
            let isCancelled = typeof cancelled === "boolean" && cancelled;

            $(document)
                .off("click.expandable")
                .off("keydown.expandable");

            //make original visible again
            this.originalElement
                .css("visibility", "visible")
                .siblings()
                .css("visibility", "visible");

            let self = this;
            this.ghostElement.animate({
                height: self.initialHeight,
                width: self.initialWidth
            }, ANIMATION_DURATION, EASING, function () {
                //on animation complete

                self.onGhostElementCollapsed(isCancelled);

                //delete the ghost element
                self.ghostElement.remove();
                self.ghostElement = null;

                //delete position trigger timeout and resize listener
                window.clearInterval(self.positionTriggerInterval);
                $(window).off("resize.expandable");
            });
        }

        private handleMouseClick(event: any): void {
            // collapse ghost element if user clicks beside it
            if (!$(event.target).is(".butter-component-expandable-ghost")) {
                this.collapseElement(false);
            }
        }

        private handleEscapeKey(event: any): void {
            if (event.which === KEYCODE_ESCAPE) {
                this.collapseElement(true);
            }
        }

        private repositionGhostElement(): void {
            //keep track of window resizing and reposition the ghost element
            if (this.ghostElement !== undefined && this.ghostElement != null) {
                this.initialOffset = this.originalElement.offset();
                this.ghostElement
                    .css("top", this.initialOffset.top)
                    .css("left", this.initialOffset.left);
            }
        }
    }

    export class DivExpandable extends AbstractExpandable {

        constructor(rootElement: JQuery) {
            super(rootElement);
            this.originalElement = this.rootElement.find(".butter-component-value-readonly");
            this.rearrangeOriginalElementStructure();
        }

        private rearrangeOriginalElementStructure(): void {
            let _label = this.rootElement.find(".butter-component-label");

            this.originalElement
                .addClass("butter-component-expandable-original")
                .click(event => {
                    this.expandElement(event);
                })
                .detach();

            let _container = $("<div>")
                .addClass("butter-component-expandable-readonly-container")
                .insertAfter(_label);

            let _icon = $("<span>").addClass("input-group-text glyphicon glyphicon-resize-full");

            this.originalElement.appendTo(_container);
            $("<div>")
                .addClass("butter-component-expandable-readonly-icon")
                .append(_icon)
                .appendTo(_container);
        }

        createGhostElement(): JQuery {
            return $("<div>");
        }

        isExpansionEventIgnored(event: any): boolean {
            return false;
        }

        onGhostElementCreated(): void {
            // do nothing
        }

        onGhostElementCollapsed(isCancelled: boolean): void {
            // do nothing
        }

        transferValueToGhostElement(): void {
            $("<div>")
                .html(this.originalElement.html())
                .addClass("butter-component-expandable-ghost-readonlyContent")
                .appendTo(this.ghostElement);
        }

    }

    export class TextareaExpandable extends AbstractExpandable {

        private blockFocusEventOnOriginal: boolean;
        private blockBlurEventOnOriginal: boolean;

        constructor(rootElement: JQuery) {
            super(rootElement);

            this.blockFocusEventOnOriginal = false;
            this.blockBlurEventOnOriginal = false;

            this.originalElement = this.rootElement.find("textarea");
            this.originalElement.addClass("butter-component-expandable-original");

            this.originalElement.focus(event => {
                this.expandElement(event);
            });
            this.originalElement.blur(event => {
                this.handleBlurEvent(event);
            });

            this.addInputGroupAddon();
        }

        private addInputGroupAddon(): void {
            this.originalElement
                .addClass("form-control")
                .parent()
                .addClass("input-group");
            $("<span class=\"input-group-append\"><span class=\"input-group-text glyphicon glyphicon-resize-full\"></span></span>")
                .insertAfter(this.originalElement);
        }

        private handleBlurEvent(event: any): void {
            if (this.blockBlurEventOnOriginal) {
                // prevent blur event bubbling, so it will not be triggered in jsf
                event.preventDefault();
            }
        }

        createGhostElement(): JQuery {
            return $("<textarea>");
        }

        isExpansionEventIgnored(event: any): boolean {
            this.blockBlurEventOnOriginal = true;
            if (this.blockFocusEventOnOriginal) {
                event.preventDefault();
                return true;
            } else {
                return false;
            }
        }

        onGhostElementCreated(): void {
            this.ghostElement
                .blur(event => {
                    this.collapseElement(event);
                })
                .focus();
            this.moveCaretToEnd(this.ghostElement);
        }

        onGhostElementCollapsed(isCancelled: boolean): void {
            if (!isCancelled) {
                //transfer value back from ghost to original
                this.originalElement.val(this.ghostElement.val());

                // trigger blur and keyup event on original textarea and don"t block
                // it for jsf
                this.blockBlurEventOnOriginal = false;
                this.blockFocusEventOnOriginal = true;
                // defer the events a little bit, look at
                // (http://stackoverflow.com/questions/8380759/why-isnt-this-textarea-focusing-with-focus#8380785)
                window.setTimeout(() => {
                    this.originalElement.trigger("keyup");
                    this.originalElement.trigger("change");
                    this.originalElement.trigger("blur");
                    this.blockFocusEventOnOriginal = false;
                }, 50);
            } else {
                this.blockBlurEventOnOriginal = true;
                this.blockFocusEventOnOriginal = false;
            }
        }

        transferValueToGhostElement(): void {
            this.ghostElement.val(this.originalElement.val());
        }

        private moveCaretToEnd(element: any): void {
            if (typeof element.selectionStart === "number") {
                element.selectionStart = element.selectionEnd = element.value.length;
            } else if (typeof element.createTextRange !== "undefined") {
                let range = element.createTextRange();
                range.collapse(false);
                range.select();
            } else {
                let strLength = (<string>this.ghostElement.val()).length * 2;
                (<HTMLInputElement>this.ghostElement.get(0)).setSelectionRange(strLength, strLength);
            }

        }

    }
}