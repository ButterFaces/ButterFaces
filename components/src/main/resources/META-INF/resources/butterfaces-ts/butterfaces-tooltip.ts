///<reference path="definitions/external/tsd.d.ts"/>

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

            let offsetBottom = $(window).height() - $(source).offset().top - $(source).outerHeight();
            let offsetRight = $(window).width() - $(source).offset().left - $(source).outerWidth();

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
                console.log('ButterFaces.Tooltip.calculateTooltipPosition - offsetRight (' + offsetRight + ') is greater than popoverMaxWidthByBootstrap (' + this.popoverMaxWidthByBootstrap + ') -> position is right');
                return 'right';
            }
            console.log('ButterFaces.Tooltip.calculateTooltipPosition - offsetBottom (' + offsetBottom + ') is greater than popoverMaxHeightByButterFaces (' + this.popoverMaxHeightByButterFaces + ') -> position is bottom');
            return 'bottom';
        };
    }
}