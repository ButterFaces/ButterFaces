///<reference path="butterfaces-util-object.ts"/>

namespace ButterFaces {
    export class String {
        static format(format: string, params: Array<any>): string {
            return format.replace(/{(\d+)}/g, (match: string, num: number) => {
                return ButterFaces.Object.isNullOrUndefined(params[num]) ? match : params[num];
            });
        }
    }
}