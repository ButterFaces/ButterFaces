///<reference path="jquery.d.ts"/>

module ButterFaces {
    export class Tooltip {

        popoverMaxWidthByBootstrap:number;
        popoverMaxHeightByButterFaces:number;

        constructor(popoverMaxWidthByBootstrap = 276, popoverMaxHeightByButterFaces = 110) {
            this.popoverMaxWidthByBootstrap = popoverMaxWidthByBootstrap;
            this.popoverMaxHeightByButterFaces = popoverMaxHeightByButterFaces;

            console.log('ButterFaces.Tooltip.constructor - popoverMaxWidthByBootstrap is ' + this.popoverMaxHeightByButterFaces + ", popoverMaxHeightByButterFaces is " + this.popoverMaxWidthByBootstrap);
        }

        public calculateTooltipPosition(popover:any, source:any) {

            var offsetBottom = $(window).height() - $(source).offset().top - $(source).outerHeight();
            var offsetRight = $(window).width() - $(source).offset().left - $(source).outerWidth();

            console.log('ButterFaces.Tooltip.calculateTooltipPosition - offsetBottom is ' + offsetBottom);
            console.log('ButterFaces.Tooltip.calculateTooltipPosition - offsetRight is ' + offsetRight);

            if (offsetBottom < this.popoverMaxHeightByButterFaces) {
                if (offsetRight < this.popoverMaxWidthByBootstrap) {
                    console.log('ButterFaces.Tooltip.calculateTooltipPosition - offset right (' + offsetRight + ') is smaller than popoverMaxWidthByBootstrap (' + this.popoverMaxWidthByBootstrap + ') -> position is top or left');
                    if ($(source).offset().left < this.popoverMaxWidthByBootstrap) {
                        console.log('ButterFaces.Tooltip.calculateTooltipPosition - left offset is smaller than popoverMaxWidthByBootstrap (' + this.popoverMaxWidthByBootstrap + ') -> position is top');
                        return 'top';
                    }
                    console.log('ButterFaces.Tooltip.calculateTooltipPosition - left offset is NOT smaller than popoverMaxWidthByBootstrap (' + this.popoverMaxWidthByBootstrap + ') -> position is left');
                    return 'left';
                }
                return 'right';
            }

            return 'bottom';
        };
    }
}