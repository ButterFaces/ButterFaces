///<reference path="definitions/external/tsd.d.ts"/>
///<reference path="butterfaces-overlay.ts"/>

module ButterFaces {
    export class Ajax {
        static sendRequest(clientId:string, event:string, renderIds:string[], /*optional string */ params:string, disableRenderIds:boolean) {
            jsf.ajax.request(clientId, event, {
                "javax.faces.behavior.event": event,
                render: renderIds.join(", "),
                params: params,
                onevent: (function (data) {
                    //console.log(data);
                    if (disableRenderIds) {
                        ButterFaces.Ajax.disableElementsOnRequest(data, renderIds);
                    }
                })
            });
        };

        static disableElementsOnRequest(data:any, ids:string[]) {
            var status:string = data.status;

            // console.log(data);
            // console.log(ids);

            switch (status) {
                case "begin": // Before the ajax request is sent.
                    // console.log('ajax request begin');

                    for (var i = 0; i < ids.length; i++) {
                        var $elementToDisable = $(document.getElementById(ids[i]));

                        if ($elementToDisable.length !== 0) {
                            //console.log('disable ' + ids[i]);
                            new ButterFaces.Overlay(0, false, document.getElementById(ids[i])).show();
                            //console.log('disablee ' + ids[i]);
                        }
                    }

                    break;

                case "complete": // After the ajax response is arrived.
                    // console.log('ajax request complete');
                    break;

                case "success": // After update of HTML DOM based on ajax response..
                                // console.log('ajax request success');

                    for (i = 0; i < ids.length; i++) {
                        var $elementToEmable = $(document.getElementById(ids[i]));

                        if ($elementToEmable.length !== 0) {
                            //console.log('enable ' + ids[i]);
                            new ButterFaces.Overlay(0, false, document.getElementById(ids[i])).hide();
                            //console.log('enabled ' + ids[i]);
                        }
                    }

                    break;
            }
        };
    }
}