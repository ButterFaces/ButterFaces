/// <reference path="definitions/external/jquery/jquery.d.ts" />

module ButterFaces {

    export class TreeBox {
        public static removeTrivialTreeDropDown(treeBoxId:string) {
            $('.tr-dropdown[data-tree-box-id=' + treeBoxId + ']').remove();

            // TODO remove unbinded dropdowns
        }
    }

}