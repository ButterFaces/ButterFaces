///<reference path="../../../../../../nodejs/node_modules/trivial-components/dist/js/bundle/trivial-components.d.ts"/>
///<reference path="../../../../../../nodejs/node_modules/@types/mustache/index.d.ts"/>

namespace ButterFaces {

    interface ButterfacesTrivialEntry {
        displayValue: string
    }


    export function createTrivialTagComponent($input,
                                              options: {
                                                  autoComplete: boolean,
                                                  distinct: boolean,
                                                  editingMode: TrivialComponents.EditingMode,
                                                  maxSelectedEntries: number,
                                                  selectedEntries: ButterfacesTrivialEntry[],
                                                  freeTextSeparators: string[],
                                                  entries: ButterfacesTrivialEntry[]
                                              }): TrivialComponents.TrivialTagComboBox<ButterfacesTrivialEntry> {
        return new TrivialComponents.TrivialTagComboBox<ButterfacesTrivialEntry>($input, {

            autoComplete: options.autoComplete,
            allowFreeText: true,
            showTrigger: true, // TODO parameter
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
            entries: options.entries,

            valueFunction: entries => {
                return entries
                    .map(entry => (entry as any).id != null ? (entry as any).id : (entry as any).title)
                    .join(",");
            },
            freeTextEntryFactory: freeText => {
                return {title: freeText}
            },
            entryRenderingFunction: entry => `<div>${(entry as any).title}</div>`, // TODO template parameter

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
                                                   searchBarMode: TrivialComponents.SearchBarMode,
                                                   selectedEntryId: number,
                                                   performanceOptimizationSettings: {
                                                       toManyVisibleItemsRenderDelay: number,
                                                       toManyVisibleItemsThreshold: number
                                                   },
                                                   templates: string[],
                                                   spinnerTemplate: string,
                                                   noEntriesTemplate: string,
                                                   entries: ButterfacesTrivialTreeEntry[]
                                               }): TrivialComponents.TrivialTree<ButterfacesTrivialTreeEntry> {
        return new TrivialComponents.TrivialTree<ButterfacesTrivialTreeEntry>($input, {
            searchBarMode: options.searchBarMode,
            selectedEntryId: options.selectedEntryId,
            performanceOptimizationSettings: options.performanceOptimizationSettings,
            entryRenderingFunction: (entry, depth) => Mustache.render(options.templates[Math.min(options.templates.length - 1, depth)], entry),
            spinnerTemplate: options.spinnerTemplate,
            noEntriesTemplate: options.noEntriesTemplate,
            entries: options.entries,
            queryFunction: TrivialComponents.customTreeQueryFunctionFactory(options.entries, "children", "expanded",
                (entry: any, queryString: string, nodeDepth: number) => {
                    let titleMatches = entry.title && TrivialComponents.trivialMatch(entry.title, queryString, null /*TODO remove parameter*/).length > 0;
                    let descriptionMatches = entry.description && TrivialComponents.trivialMatch(entry.description, queryString, null /*TODO remove parameter*/).length > 0;
                    return titleMatches || descriptionMatches;
                })
        });
    }


    export function createTrivialComboBox($input,
                                          options: {
                                              inputTextProperty: string,
                                              emptyEntryTemplate: string,
                                              editingMode: TrivialComponents.EditingMode,
                                              showClearButton: boolean,
                                              selectedEntry: ButterfacesTrivialEntry,
                                              selectedEntryTemplate: string,
                                              template: string,
                                              spinnerTemplate: string,
                                              noEntriesTemplate: string,
                                              entries: ButterfacesTrivialEntry[]
                                          }): TrivialComponents.TrivialComboBox<ButterfacesTrivialEntry> {
        return new TrivialComponents.TrivialComboBox<ButterfacesTrivialEntry>($input, {
            allowFreeText: false,
            entryToEditorTextFunction: entry => entry[options.inputTextProperty],
            entryRenderingFunction: entry => {
                return Mustache.render(options.template, entry);
            },
            selectedEntryRenderingFunction: entry => {
                if (!entry || (entry as any)._isEmptyEntry) {
                    return options.emptyEntryTemplate || "";
                } else if (options.selectedEntryTemplate) {
                    return Mustache.render(options.selectedEntryTemplate, entry);
                } else {
                    return Mustache.render(options.template, entry);
                }
            },
            editingMode: options.editingMode,
            showClearButton: options.showClearButton,
            selectedEntry: options.selectedEntry,
            spinnerTemplate: options.spinnerTemplate,
            noEntriesTemplate: options.noEntriesTemplate,
            entries: options.entries
        });
    }


    export function createTrivialTreeComboBox($input,
                                              options: {
                                                  inputTextProperty: string,
                                                  emptyEntryTemplate: string,
                                                  editingMode: TrivialComponents.EditingMode,
                                                  showClearButton: boolean,
                                                  selectedEntry: ButterfacesTrivialEntry,
                                                  selectedEntryTemplate: string,
                                                  templates: string[],
                                                  spinnerTemplate: string,
                                                  noEntriesTemplate: string,
                                                  entries: ButterfacesTrivialEntry[]
                                              }): TrivialComponents.TrivialTreeComboBox<ButterfacesTrivialEntry> {
        return new TrivialComponents.TrivialTreeComboBox<ButterfacesTrivialEntry>($input, {
            allowFreeText: false,
            entryToEditorTextFunction: entry => entry[options.inputTextProperty],
            entryRenderingFunction: (entry, depth) => Mustache.render(options.templates[Math.min(options.templates.length - 1, depth)], entry),
            selectedEntryRenderingFunction: entry => {
                if (!entry || (entry as any)._isEmptyEntry) {
                    return options.emptyEntryTemplate || "";
                } else if (options.selectedEntryTemplate) {
                    return Mustache.render(options.selectedEntryTemplate, entry);
                } else {
                    return Mustache.render(options.templates[0], entry);
                }
            },
            editingMode: options.editingMode,
            showClearButton: options.showClearButton,
            selectedEntry: options.selectedEntry,
            spinnerTemplate: options.spinnerTemplate,
            noEntriesTemplate: options.noEntriesTemplate,
            entries: options.entries,
            queryFunction: TrivialComponents.customTreeQueryFunctionFactory(options.entries, "children", "expanded",
                (entry: any, queryString: string, nodeDepth: number) => {
                    let titleMatches = entry.title && TrivialComponents.trivialMatch(entry.title, queryString, null /*TODO remove parameter*/).length > 0;
                    let descriptionMatches = entry.description && TrivialComponents.trivialMatch(entry.description, queryString, null /*TODO remove parameter*/).length > 0;
                    return titleMatches || descriptionMatches;
                })
        });
    }


}