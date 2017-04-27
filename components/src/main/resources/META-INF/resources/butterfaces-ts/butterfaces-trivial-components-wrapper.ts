///<reference path="../butterfaces-external/trivial-components.d.ts"/>
///<reference path="../../../../../../nodejs/node_modules/@types/mustache/index.d.ts"/>

namespace ButterFaces {

    import TrivialTagComboBox = TrivialComponents.TrivialTagComboBox;
    import EditingMode = TrivialComponents.EditingMode;
    import SearchBarMode = TrivialComponents.SearchBarMode;
    import TrivialTree = TrivialComponents.TrivialTree;
    import trivialMatch = TrivialComponents.trivialMatch;

    interface ButterfacesTrivialEntry {
        displayValue: string
    }


    export function createTrivialTagComponent($input,
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
        });
    }


    interface ButterfacesTrivialTreeEntry {
        title: string,
        description: string,
        id: number
    }

    export function createTrivialTreeComponent($input,
                                               options: {
                                                   searchBarMode: SearchBarMode,
                                                   selectedEntryId: number,
                                                   performanceOptimizationSettings: {
                                                       toManyVisibleItemsRenderDelay: number,
                                                       toManyVisibleItemsThreshold: number
                                                   },
                                                   templates: string[],
                                                   spinnerTemplate: string,
                                                   noEntriesTemplate: string,
                                                   entries: ButterfacesTrivialTreeEntry[]
                                               }): TrivialTree<ButterfacesTrivialTreeEntry> {
        return new TrivialTree<ButterfacesTrivialTreeEntry>($input, {
            searchBarMode: options.searchBarMode,
            selectedEntryId: options.selectedEntryId,
            performanceOptimizationSettings: options.performanceOptimizationSettings,
            entryRenderingFunction: (entry, depth) => Mustache.render(options.templates[Math.min(options.templates.length - 1, depth)], entry),
            spinnerTemplate: options.spinnerTemplate,
            noEntriesTemplate: options.noEntriesTemplate,
            entries: options.entries,
            queryFunction: TrivialComponents.customTreeQueryFunctionFactory(options.entries, "children", "expanded",
                (entry: any, queryString: string, nodeDepth: number) => {
                    let titleMatches = entry.title && trivialMatch(entry.title, queryString, null /*TODO remove parameter*/).length > 0;
                    let descriptionMatches = entry.description && trivialMatch(entry.description, queryString, null /*TODO remove parameter*/).length > 0;
                    return titleMatches || descriptionMatches;
                })
        });
    }


}