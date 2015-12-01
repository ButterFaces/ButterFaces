interface jsf {
    ajax: {
        addOnEvent(callback:any): void;
        addOnError(callback:any): void;
    }
}

interface ajax {
    status: string;
    description: string;
}

declare var jsf:jsf;