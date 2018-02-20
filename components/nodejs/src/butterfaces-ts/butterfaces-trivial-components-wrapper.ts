///<reference path="../../node_modules/trivial-components/dist/js/bundle/trivial-components-global.d.ts"/>
///<reference path="../../node_modules/@types/mustache/index.d.ts"/>

namespace ButterFaces {

    interface ButterFacesTrivialEntry {
        displayValue: string;
    }


    export function createTrivialTagComponent($input: string,
                                              options: {
                                                  showTrigger: boolean,
                                                  autoComplete: boolean,
                                                  distinct: boolean,
                                                  editingMode: TrivialComponents.EditingMode,
                                                  maxSelectedEntries: number,
                                                  selectedEntries: ButterFacesTrivialEntry[],
                                                  freeTextSeparators: string[],
                                                  entries: ButterFacesTrivialEntry[]
                                              }): TrivialComponents.TrivialTagComboBox<ButterFacesTrivialEntry> {
        return new TrivialComponents.TrivialTagComboBox<ButterFacesTrivialEntry>($input, {

            autoComplete: options.autoComplete,
            allowFreeText: true,
            showTrigger: options.showTrigger,
            distinct: options.distinct,
            editingMode: options.editingMode,
            matchingOptions: {
                matchingMode: "contains",
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
                return {title: escapeHtml(freeText)};
            },
            entryRenderingFunction: entry => `<div>${(entry as any).title}</div>`, // TODO template parameter
        });

        function escapeHtml(source: string) {
            /* tslint:disable */
            const entityMap = {
                "&": "&amp;",
                "<": "&lt;",
                ">": "&gt;",
                '"': "&quot;",
                "'": "&#39;",
                "/": "&#x2F;"
            };
            /* tslint:enable */

            return source.replace(/[&<>"'\/]/g, s => entityMap[s]);
        }
    }

    interface ButterFacesTrivialTreeEntry {
        title: string;
        description: string;
        id: number;
    }

    export function createTrivialTreeComponent($input: string,
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
                                                   entries: ButterFacesTrivialTreeEntry[]
                                               }): TrivialComponents.TrivialTree<ButterFacesTrivialTreeEntry> {
        return new TrivialComponents.TrivialTree<ButterFacesTrivialTreeEntry>($input, {
            searchBarMode: options.searchBarMode,
            selectedEntryId: options.selectedEntryId,
            performanceOptimizationSettings: options.performanceOptimizationSettings,
            entryRenderingFunction: (entry, depth) => Mustache.render(options.templates[Math.min(options.templates.length - 1, depth)], entry),
            spinnerTemplate: options.spinnerTemplate,
            noEntriesTemplate: options.noEntriesTemplate,
            entries: options.entries,
            queryFunction: TrivialComponents.customTreeQueryFunctionFactory(options.entries, "children", "expanded",
                (entry: any, queryString: string, nodeDepth: number) => {
                    // TODO remove null parameter after trivial component is fixed
                    let titleMatches = entry.title && TrivialComponents.trivialMatch(entry.title, queryString, null).length > 0;
                    let descriptionMatches = entry.description && TrivialComponents.trivialMatch(entry.description, queryString, null).length > 0;
                    return titleMatches || descriptionMatches;
                })
        });
    }


    export function createTrivialComboBox($input: string,
                                          options: {
                                              inputTextProperty: string,
                                              emptyEntryTemplate: string,
                                              editingMode: TrivialComponents.EditingMode,
                                              showClearButton: boolean,
                                              selectedEntry: ButterFacesTrivialEntry,
                                              selectedEntryTemplate: string,
                                              template: string,
                                              spinnerTemplate: string,
                                              noEntriesTemplate: string,
                                              entries: ButterFacesTrivialEntry[]
                                          }): TrivialComponents.TrivialComboBox<ButterFacesTrivialEntry> {
        return new TrivialComponents.TrivialComboBox<ButterFacesTrivialEntry>($input, {
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


    export function createTrivialTreeComboBox($input: string,
                                              options: {
                                                  inputTextProperty: string,
                                                  emptyEntryTemplate: string,
                                                  editingMode: TrivialComponents.EditingMode,
                                                  showClearButton: boolean,
                                                  selectedEntry: ButterFacesTrivialEntry,
                                                  selectedEntryTemplate: string,
                                                  templates: string[],
                                                  spinnerTemplate: string,
                                                  noEntriesTemplate: string,
                                                  entries: ButterFacesTrivialEntry[]
                                              }): TrivialComponents.TrivialTreeComboBox<ButterFacesTrivialEntry> {
        return new TrivialComponents.TrivialTreeComboBox<ButterFacesTrivialEntry>($input, {
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
                    // TODO remove null parameter after trivial component is fixed
                    let titleMatches = entry.title && TrivialComponents.trivialMatch(entry.title, queryString, null).length > 0;
                    let descriptionMatches = entry.description && TrivialComponents.trivialMatch(entry.description, queryString, null).length > 0;
                    return titleMatches || descriptionMatches;
                })
        });
    }


}