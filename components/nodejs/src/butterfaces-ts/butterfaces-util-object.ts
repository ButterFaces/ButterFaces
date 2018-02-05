namespace ButterFaces {
    export class Object {
        static isNullOrUndefined(value: any): boolean {
            return typeof value === "undefined" || value === null;
        }
    }
}