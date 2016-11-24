namespace ButterFaces {
    export class String {
        static format(format: string): string {
            const args = Array.prototype.slice.call(arguments, 1);
            return format.replace(/{(\d+)}/g, function (match, num) {
                return typeof args[num] !== "undefined" ? args[num] : match;
            });
        };
    }
}