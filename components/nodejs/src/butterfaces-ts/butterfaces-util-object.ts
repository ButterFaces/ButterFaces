namespace ButterFaces {
    export class ObjectStatics {
        static isNullOrUndefined(value: any): boolean {
            return typeof value === "undefined" || value === null;
        }
    }
}