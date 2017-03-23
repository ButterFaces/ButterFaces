namespace ButterFaces {

    export class TreeBox {

        public static removeTrivialTreeDropDown(treeBoxId: string) {
            this.removeTrivialTreeDropDownById(treeBoxId);
            this.removeUnAssociatedTrivialTreeDropDowns();
        }

        private static removeUnAssociatedTrivialTreeDropDowns() {
            const dropdowns = document.querySelectorAll(".tr-dropdown[data-tree-box-id]");

            Array.prototype.slice.call(dropdowns, 0).forEach((dropdown: any) => {
                const dropdownId = dropdown.getAttribute("data-tree-box-id");
                if (document.querySelectorAll(".butter-component-treebox[data-tree-box-id=" + dropdownId + "]").length === 0) {
                    dropdown.parentNode.removeChild(dropdown);
                }
            });
        }

        private static removeTrivialTreeDropDownById(treeBoxId: string) {
            // in jQuery: $(".tr-dropdown[data-tree-box-id=" + treeBoxId + "]").remove();
            // plain javascript:
            const dropdowns = document.querySelectorAll(".tr-dropdown[data-tree-box-id=" + treeBoxId + "]");
            Array.prototype.slice.call(dropdowns, 0).forEach((dropdown :any) => {
                dropdown.parentNode.removeChild(dropdown);
            });
        };
    }

}