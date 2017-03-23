namespace ButterFaces {
    export class String {
        static format(format: string, params: Array<any>): string {
            return format.replace(/{(\d+)}/g, function (match, num) {
                return typeof params[num] !== "undefined" ? params[num] : match;
            });
        };
    }
}