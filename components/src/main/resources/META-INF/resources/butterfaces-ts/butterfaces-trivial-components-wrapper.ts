///<reference path="../butterfaces-external/trivial-components.d.ts"/>

namespace ButterFaces {

    import TrivialTagComboBox = TrivialComponents.TrivialTagComboBox;
    import EditingMode = TrivialComponents.EditingMode;

    type ButterfacesTrivialEntry = {
        displayValue: string,
    };

    export class TrivialComponentsWrapper {

        public static createTagsComponent($input,
                                          options: {
                                              autoComplete: boolean,
                                              distinct: boolean,
                                              editingMode: EditingMode,
                                              maxSelectedEntries: number,
                                              selectedEntries: ButterfacesTrivialEntry[],
                                              freeTextSeparators: string[]
                                          }): TrivialTagComboBox<ButterfacesTrivialEntry> {
            return new TrivialTagComboBox<ButterfacesTrivialEntry>($input, {

                autoComplete: options.autoComplete,
                allowFreeText: true,
                showTrigger: false,
                distinct: options.distinct,
                editingMode: options.editingMode,
                matchingOptions: {
                    matchingMode: 'contains',
                    ignoreCase: true,
                    maxLevenshteinDistance: 0
                },

                maxSelectedEntries: options.maxSelectedEntries,
                selectedEntries: options.selectedEntries,
                freeTextSeparators: options.freeTextSeparators as any, // TODO remove this cast when trivial components is fixed

                valueFunction: entries => {
                    return entries
                        .map(entry => entry.displayValue)
                        .join(",");
                },
                entryRenderingFunction: entry => entry.displayValue


                // TODO
                // if (StringUtils.isNotEmpty(entriesVar)) {
                //     jQueryPluginCall.append("\n    valueProperty: 'id',");
                //     jQueryPluginCall.append("\n    entries: " + entriesVar + ",");
                //     jQueryPluginCall.append("\n    template: '" + DEFAULT_SINGLE_LINE_OF_TEXT_TEMPLATE + "',");
                //     jQueryPluginCall.append("\n    inputTextProperty: 'butterObjectToString',");
                // }
                //

                //
                //
                // });
            });
        }

    }

}