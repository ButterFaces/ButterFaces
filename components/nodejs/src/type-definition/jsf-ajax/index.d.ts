///<reference path="../../../node_modules/@types/jee-jsf/index.d.ts"/>
// Copy of jee-jsf/index.d.ts because params does not work correctly in thorntail jsf 2.3 implementation
// Extend jsf.ajax.RequestOptions by attribute butterfaces.params

declare namespace jsf {
    namespace ajax {
        interface RequestOptions {

            /**
             * Additional to params, because params does not work...
             */
            'butterfaces.params'?: any;

        }
    }
}