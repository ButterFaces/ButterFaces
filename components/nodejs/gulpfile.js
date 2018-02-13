/*
 Recipes for using Gulp:
 https://github.com/gulpjs/gulp/tree/master/docs/recipes
 */

// DEPENDENCIES ===============================================================================

var gulp = require("gulp");
var merge = require("merge-stream");
var ts = require("gulp-typescript");
var concat = require("gulp-concat");
var sourcemaps = require("gulp-sourcemaps");
var del = require("del");
var pipe = require("multipipe");
var sass = require("gulp-sass");
var mirror = require("gulp-mirror");
var rename = require("gulp-rename");
var postcss = require("gulp-postcss");
var autoprefixer = require("autoprefixer");
var tslint = require("gulp-tslint");
var uglify = require("gulp-uglify");
var UglifyJS = require("uglify-js");
var minifyCSS = require("gulp-minify-css");
var gzip = require("gulp-gzip");
var stripDebug = require("gulp-strip-debug");
var sizereport = require("gulp-sizereport");

// CONSTANTS ===============================================================================

var RESSOURCE_DIR = "../src/main/resources/META-INF/resources";
var NODEJS_RESSOURCE_DIR = "./src";

var paths = {
    npm: {
        jquery: "./node_modules/jquery/dist/**/*.{js,map}",
        jqueryinputmask: "./node_modules/inputmask/dist/jquery.inputmask.bundle.js",
        prettify: "./node_modules/google-code-prettify/src/prettify.{js,css}",
        bootstrap_css: "./node_modules/bootstrap/dist/css/bootstrap.{css,css.map}",
        bootstrap_js: "./node_modules/bootstrap/dist/js/bootstrap.{js,map}",
        showdown_js: "./node_modules/showdown/dist/showdown.{js,js.map}",
        markdown_js: "./node_modules/markdown/lib/markdown.{js,js.map}",
        to_markdown_js: "./node_modules/to-markdown/dist/to-markdown.{js,js.map}",
        bootstrap_markdown_css: "./node_modules/bootstrap-markdown/css/bootstrap-markdown.min.{css,css.map}",
        bootstrap_markdown_js: "./node_modules/bootstrap-markdown/js/bootstrap-markdown.{js,map}",
        bootstrap_markdown_locale_ar_js: "./node_modules/bootstrap-markdown/locale/bootstrap-markdown.ar.{js,map}",
        bootstrap_markdown_locale_en_js: "./node_modules/bootstrap-markdown/locale/bootstrap-markdown.en.{js,map}",
        bootstrap_markdown_locale_de_js: "./node_modules/bootstrap-markdown/locale/bootstrap-markdown.de.{js,map}",
        bootstrap_markdown_locale_fr_js: "./node_modules/bootstrap-markdown/locale/bootstrap-markdown.fr.{js,map}",
        bootstrap_markdown_locale_es_js: "./node_modules/bootstrap-markdown/locale/bootstrap-markdown.es.{js,map}",
        bootstrap_markdown_locale_nl_js: "./node_modules/bootstrap-markdown/locale/bootstrap-markdown.nl.{js,map}",
        bootstrap_markdown_locale_cs_js: "./node_modules/bootstrap-markdown/locale/bootstrap-markdown.cs.{js,map}",
        bootstrap_markdown_locale_da_js: "./node_modules/bootstrap-markdown/locale/bootstrap-markdown.da.{js,map}",
        bootstrap_markdown_locale_fa_js: "./node_modules/bootstrap-markdown/locale/bootstrap-markdown.fa.{js,map}",
        bootstrap_markdown_locale_ja_js: "./node_modules/bootstrap-markdown/locale/bootstrap-markdown.ja.{js,map}",
        bootstrap_markdown_locale_kr_js: "./node_modules/bootstrap-markdown/locale/bootstrap-markdown.kr.{js,map}",
        bootstrap_markdown_locale_nb_js: "./node_modules/bootstrap-markdown/locale/bootstrap-markdown.nb.{js,map}",
        bootstrap_markdown_locale_pl_js: "./node_modules/bootstrap-markdown/locale/bootstrap-markdown.pl.{js,map}",
        bootstrap_markdown_locale_sl_js: "./node_modules/bootstrap-markdown/locale/bootstrap-markdown.sl.{js,map}",
        bootstrap_markdown_locale_sv_js: "./node_modules/bootstrap-markdown/locale/bootstrap-markdown.sv.{js,map}",
        bootstrap_markdown_locale_tr_js: "./node_modules/bootstrap-markdown/locale/bootstrap-markdown.tr.{js,map}",
        //bootstrap_markdown_locale_ru_js: "./node_modules/bootstrap-markdown/locale/bootstrap-markdown.ru.{js,map}",
        //bootstrap_markdown_locale_ua_js: "./node_modules/bootstrap-markdown/locale/bootstrap-markdown.ua.{js,map}",
        bootstrap_markdown_locale_zh_js: "./node_modules/bootstrap-markdown/locale/bootstrap-markdown.zh.{js,map}",
        mustache: "./node_modules/mustache/mustache.min.js",
        jquery_ui_position: "./node_modules/jquery-ui/ui/position.js",
        jquery_ui_version: "./node_modules/jquery-ui/ui/version.js",
        trivial_components_css: "./node_modules/trivial-components/dist/css/trivial-components*.css",
        trivial_components_js: "./node_modules/trivial-components/dist/js/bundle/trivial-components.js",
        glyphicons_css: "./node_modules/glyphicons-only-bootstrap/css/bootstrap.css",
        glyphicon_fonts: "./node_modules/glyphicons-only-bootstrap/fonts/**/*.*",
        popperjs: "./node_modules/popper.js/dist/umd/popper.js",
        momentjs: "./node_modules/moment/min/moment-with-locales.js",
        moment_timezone_js: "./node_modules/moment-timezone/builds/moment-timezone.min.js",
        tempusdominus_core_js: "./node_modules/tempusdominus-core/build/js/tempusdominus-core.js",
        tempusdominus_bootstrap_js: "./node_modules/tempusdominus-bootstrap-4/build/js/tempusdominus-bootstrap-4.js",
        tempusdominus_bootstrap_css: "./node_modules/tempusdominus-bootstrap-4/build/css/tempusdominus-bootstrap-4.css"
    },
    source: {
        typescripts: NODEJS_RESSOURCE_DIR + "/butterfaces-ts/**/*.ts",
        javascript: RESSOURCE_DIR + "/butterfaces-js/**/*.js",
        sass: NODEJS_RESSOURCE_DIR + "/butterfaces-sass/*.scss"
    },
    destination: {
        root: RESSOURCE_DIR + "/butterfaces-dist",
        css: RESSOURCE_DIR + "/butterfaces-dist-css",
        js: RESSOURCE_DIR + "/butterfaces-dist-js",
        bundle_js: RESSOURCE_DIR + "/butterfaces-dist-bundle-js",
        bundle_dev_js: RESSOURCE_DIR + "/butterfaces-dist-bundle-dev-js",
        npm: NODEJS_RESSOURCE_DIR + "/butterfaces-dist-npm",
        npm_font: RESSOURCE_DIR + "/fonts",
    }
};

// DIST GOALS ===============================================================================

gulp.task("clean", function (cb) {
    del([
            paths.destination.root,
            paths.destination.css,
            paths.destination.js,
            paths.destination.npm,
            paths.destination.npm_font,
            paths.destination.bundle_js,
            paths.destination.bundle_dev_js
        ],
        {force: true}, cb);
});

gulp.task("npm:copyDependenciesToDist", function () {
    var copyDependenciesToDist = gulp.src([
        paths.npm.jquery,
        paths.npm.jqueryinputmask,
        paths.npm.jquery_ui_position,
        paths.npm.jquery_ui_version,
        paths.npm.prettify,
        paths.npm.bootstrap_css,
        paths.npm.bootstrap_js,
        paths.npm.showdown_js,
        paths.npm.markdown_js,
        paths.npm.to_markdown_js,
        paths.npm.bootstrap_markdown_css,
        paths.npm.bootstrap_markdown_js,
        paths.npm.bootstrap_markdown_locale_ar_js,
        paths.npm.bootstrap_markdown_locale_cs_js,
        paths.npm.bootstrap_markdown_locale_da_js,
        paths.npm.bootstrap_markdown_locale_en_js,
        paths.npm.bootstrap_markdown_locale_fa_js,
        paths.npm.bootstrap_markdown_locale_ja_js,
        paths.npm.bootstrap_markdown_locale_kr_js,
        paths.npm.bootstrap_markdown_locale_nb_js,
        paths.npm.bootstrap_markdown_locale_nl_js,
        paths.npm.bootstrap_markdown_locale_pl_js,
        paths.npm.bootstrap_markdown_locale_sl_js,
        paths.npm.bootstrap_markdown_locale_sv_js,
        paths.npm.bootstrap_markdown_locale_tr_js,
        //paths.npm.bootstrap_markdown_locale_ua_js,
        //paths.npm.bootstrap_markdown_locale_ru_js,
        paths.npm.bootstrap_markdown_locale_zh_js,
        paths.npm.bootstrap_markdown_locale_de_js,
        paths.npm.bootstrap_markdown_locale_es_js,
        paths.npm.bootstrap_markdown_locale_fr_js,
        paths.npm.bootstrap_markdown_locale_nl_js,
        paths.npm.trivial_components_css,
        paths.npm.trivial_components_js,
        paths.npm.mustache,
        paths.npm.popperjs,
        paths.npm.momentjs,
        paths.npm.moment_timezone_js,
        paths.npm.tempusdominus_core_js,
        paths.npm.tempusdominus_bootstrap_css,
        paths.npm.tempusdominus_bootstrap_js
    ])
        .pipe(gulp.dest(paths.destination.npm));

    var copyGlyphiconsToDist = gulp.src([
        paths.npm.glyphicons_css
    ])
        .pipe(rename("glyphicons.css"))
        .pipe(gulp.dest(paths.destination.npm));

    var copyFontDependenciesToDist = gulp.src([
        paths.npm.glyphicon_fonts
    ])
        .pipe(gulp.dest(paths.destination.npm_font));

    return merge(copyDependenciesToDist, copyGlyphiconsToDist, copyFontDependenciesToDist);
});

gulp.task("typescript:lint", function () {
    return gulp.src([
        paths.source.typescripts,
        "!" + paths.destination.ts_external_definitions + "/**/*.ts"
    ])
        .pipe(tslint({
            formatter: "verbose"
        }))
        .pipe(tslint.report())
});

gulp.task("typescript:compileToBundle", ["typescript:lint"], function () {
    var tsResult = gulp.src(paths.source.typescripts)
        .pipe(sourcemaps.init())
        .pipe(ts({
            noImplicitAny: true,
            target: "es5"
        }));

    return tsResult.js
        .pipe(concat("butterfaces-ts-bundle.js"))
        .pipe(stripDebug())
        .pipe(mirror(
            pipe(
                rename(function (path) {
                    path.basename += ".min";
                }),
                uglify()
            )
        ))
        .pipe(sourcemaps.write())
        .pipe(gulp.dest(paths.destination.bundle_js));
});

gulp.task("typescript:compileToSingleFiles", ["typescript:lint"], function () {
    var tsResult = gulp.src(paths.source.typescripts)
        .pipe(sourcemaps.init())
        .pipe(ts({
            noImplicitAny: true,
            target: "es5"
        }));

    return tsResult.js
        .pipe(mirror(
            pipe(
                rename(function (path) {
                    path.basename += ".min";
                }),
                uglify()
            )
        ))
        .pipe(sourcemaps.write())
        .pipe(gulp.dest(paths.destination.js));
});

gulp.task("sass:compile", ["npm:copyDependenciesToDist"], function () {
    return gulp.src([paths.source.sass])
        .pipe(sourcemaps.init())
        .pipe(sass())
        .pipe(postcss([autoprefixer({browsers: ["> 2%"]})]))
        .pipe(mirror(
            pipe(
                rename(function (path) {
                    path.basename += ".min";
                }),
                minifyCSS()
            )
        ))
        .pipe(sourcemaps.write())
        .pipe(gulp.dest(paths.destination.css));
});

gulp.task("javascript:buildComponentsBundle", function () {
    return gulp.src(paths.source.javascript)
        .pipe(sourcemaps.init())
        .pipe(concat("butterfaces-js-bundle.js"))
        .pipe(stripDebug())
        .pipe(mirror(
            pipe(
                rename(function (path) {
                    path.basename += ".min";
                }),
                uglify()
            )
        ))
        .pipe(sourcemaps.write())
        .pipe(gulp.dest(paths.destination.bundle_js));
});

gulp.task("compileResources", ["sass:compile", "typescript:compileToBundle", "typescript:compileToSingleFiles", "javascript:buildComponentsBundle"]);

gulp.task("javascript:buildAllBundle", ["compileResources"], function () {
    var buildButterFacesOnlyBundle = gulp.src([
        paths.destination.npm + "/prettify.js",
        paths.destination.npm + "/moment-with-locales.js",
        paths.destination.npm + "/moment-timezone.min.js",
        paths.destination.npm + "/tempusdominus-core.js",
        paths.destination.npm + "/tempusdominus-bootstrap-4.js",
        paths.destination.npm + "/jquery.inputmask.bundle.js",
        paths.destination.npm + "/version.js",
        paths.destination.npm + "/position.js",
        paths.destination.npm + "/showdown.js",
        paths.destination.npm + "/markdown.js",
        paths.destination.npm + "/to-markdown.js",
        paths.destination.npm + "/bootstrap-markdown.js",
        paths.destination.npm + "/bootstrap-markdown.*.js",
        paths.destination.npm + "/mustache.min.js",
        paths.destination.npm + "/trivial-components.js",
        paths.destination.bundle_js + "/butterfaces-ts-bundle.min.js",
        paths.destination.bundle_js + "/butterfaces-js-bundle.min.js"
    ])
        .pipe(sourcemaps.init())
        .pipe(stripDebug())
        .pipe(uglify())
        .pipe(concat("butterfaces-all-bundle.min.js"))
        .pipe(sourcemaps.write())
        .pipe(gulp.dest(paths.destination.bundle_js));

    var buildAllWithJQueryBundle = gulp.src([
        paths.destination.npm + "/jquery.min.js",
        paths.destination.npm + "/prettify.js",
        paths.destination.npm + "/moment-with-locales.js",
        paths.destination.npm + "/moment-timezone.min.js",
        paths.destination.npm + "/tempusdominus-core.js",
        paths.destination.npm + "/tempusdominus-bootstrap-4.js",
        paths.destination.npm + "/jquery.inputmask.bundle.js",
        paths.destination.npm + "/version.js",
        paths.destination.npm + "/position.js",
        paths.destination.npm + "/showdown.js",
        paths.destination.npm + "/markdown.js",
        paths.destination.npm + "/to-markdown.js",
        paths.destination.npm + "/bootstrap-markdown.js",
        paths.destination.npm + "/bootstrap-markdown.*.js",
        paths.destination.npm + "/mustache.min.js",
        paths.destination.npm + "/trivial-components.js",
        paths.destination.bundle_js + "/butterfaces-ts-bundle.min.js",
        paths.destination.bundle_js + "/butterfaces-js-bundle.min.js"
    ])
        .pipe(sourcemaps.init())
        .pipe(stripDebug())
        .pipe(uglify())
        .pipe(concat("butterfaces-all-with-jquery-bundle.min.js"))
        .pipe(sourcemaps.write())
        .pipe(gulp.dest(paths.destination.bundle_js));

    var buildAllWithBootstrapBundle = gulp.src([
        paths.destination.npm + "/prettify.js",
        paths.destination.npm + "/moment-with-locales.js",
        paths.destination.npm + "/moment-timezone.min.js",
        paths.destination.npm + "/tempusdominus-core.js",
        paths.destination.npm + "/tempusdominus-bootstrap-4.js",
        paths.destination.npm + "/popper.js",
        paths.destination.npm + "/bootstrap.js",
        paths.destination.npm + "/jquery.inputmask.bundle.js",
        paths.destination.npm + "/version.js",
        paths.destination.npm + "/position.js",
        paths.destination.npm + "/showdown.js",
        paths.destination.npm + "/markdown.js",
        paths.destination.npm + "/to-markdown.js",
        paths.destination.npm + "/bootstrap-markdown.js",
        paths.destination.npm + "/bootstrap-markdown.*.js",
        paths.destination.npm + "/mustache.min.js",
        paths.destination.npm + "/trivial-components.js",
        paths.destination.bundle_js + "/butterfaces-ts-bundle.min.js",
        paths.destination.bundle_js + "/butterfaces-js-bundle.min.js"
    ])
        .pipe(sourcemaps.init())
        .pipe(stripDebug())
        .pipe(uglify())
        .pipe(concat("butterfaces-all-with-bootstrap-bundle.min.js"))
        .pipe(sourcemaps.write())
        .pipe(gulp.dest(paths.destination.bundle_js));

    var buildAllWithJQueryAndBootstrapBundle = gulp.src([
        paths.destination.npm + "/jquery.min.js",
        paths.destination.npm + "/moment-with-locales.js",
        paths.destination.npm + "/moment-timezone.min.js",
        paths.destination.npm + "/tempusdominus-core.js",
        paths.destination.npm + "/tempusdominus-bootstrap-4.js",
        paths.destination.npm + "/prettify.js",
        paths.destination.npm + "/popper.js",
        paths.destination.npm + "/bootstrap.js",
        paths.destination.npm + "/jquery.inputmask.bundle.js",
        paths.destination.npm + "/version.js",
        paths.destination.npm + "/position.js",
        paths.destination.npm + "/showdown.js",
        paths.destination.npm + "/markdown.js",
        paths.destination.npm + "/to-markdown.js",
        paths.destination.npm + "/bootstrap-markdown.js",
        paths.destination.npm + "/bootstrap-markdown.*.js",
        paths.destination.npm + "/mustache.min.js",
        paths.destination.npm + "/trivial-components.js",
        paths.destination.bundle_js + "/butterfaces-ts-bundle.min.js",
        paths.destination.bundle_js + "/butterfaces-js-bundle.min.js"
    ])
        .pipe(sourcemaps.init())
        .pipe(concat("butterfaces-all-with-jquery-and-bootstrap-bundle.min.js"))
        .pipe(stripDebug())
        .pipe(uglify())
        .pipe(sourcemaps.write())
        .pipe(gulp.dest(paths.destination.bundle_js));

    return merge(buildButterFacesOnlyBundle, buildAllWithJQueryBundle, buildAllWithBootstrapBundle, buildAllWithJQueryAndBootstrapBundle);
})

gulp.task("javascript:buildAllDevBundle", ["compileResources"], function () {
    var thirdPartyBundle = gulp.src([
        paths.destination.npm + "/prettify.js",
        paths.destination.npm + "/moment-with-locales.js",
        paths.destination.npm + "/moment-timezone.min.js",
        paths.destination.npm + "/tempusdominus-core.js",
        paths.destination.npm + "/tempusdominus-bootstrap-4.js",
        paths.destination.npm + "/jquery.inputmask.bundle.js",
        paths.destination.npm + "/version.js",
        paths.destination.npm + "/position.js",
        paths.destination.npm + "/showdown.js",
        paths.destination.npm + "/markdown.js",
        paths.destination.npm + "/to-markdown.js",
        paths.destination.npm + "/bootstrap-markdown.js",
        paths.destination.npm + "/bootstrap-markdown.*.js",
        paths.destination.npm + "/mustache.min.js",
        paths.destination.npm + "/trivial-components.js"
    ])
        .pipe(sourcemaps.init())
        .pipe(concat("butterfaces-third-party.js"))
        .pipe(sourcemaps.write())
        .pipe(gulp.dest(paths.destination.bundle_dev_js));

    var thirdPartyJQueryBundle = gulp.src([
        paths.destination.npm + "/jquery.min.js"
    ])
        .pipe(sourcemaps.init())
        .pipe(concat("butterfaces-third-party-jquery.js"))
        .pipe(sourcemaps.write())
        .pipe(gulp.dest(paths.destination.bundle_dev_js));

    var thirdPartyBootstrapBundle = gulp.src([
        paths.destination.npm + "/popper.js",
        paths.destination.npm + "/bootstrap.js"
    ])
        .pipe(sourcemaps.init())
        .pipe(concat("butterfaces-third-party-bootstrap.js"))
        .pipe(sourcemaps.write())
        .pipe(gulp.dest(paths.destination.bundle_dev_js));

    return merge(thirdPartyBundle, thirdPartyJQueryBundle, thirdPartyBootstrapBundle);
});

gulp.task("dist:zip", ["javascript:buildAllBundle", "javascript:buildAllDevBundle"], function () {
    return gulp.src([
        paths.destination.css + "/**/*",
        paths.destination.js + "/**/*",
        paths.destination.bundle_js + "/**/*",
        paths.destination.bundle_dev_js + "/**/*",
        "!" + paths.destination.css + "/**/*.gz",
        "!" + paths.destination.js + "/**/*.gz",
        "!" + paths.destination.bundle_js + "/**/*.gz",
        "!" + paths.destination.bundle_dev_js + "/**/*.gz"
    ], {base: "."})
        .pipe(gzip())
        .pipe(gulp.dest("."));
});

gulp.task("sizereport:css", function () {
    return gulp.src([
        paths.destination.css + "/*.css",
        "!" + paths.destination.css + "/*.sourcemaps.css"
    ])
        .pipe(sizereport({gzip: true}));
});

gulp.task("sizereport:js", function () {
    return gulp.src([
        paths.destination.js + "/*.js",
        "!" + paths.destination.js + "/*.min.js",
    ])
        .pipe(sizereport({
            gzip: true,
            minifier: function (contents, filepath) {
                if (filepath.match(/\.min\./g)) {
                    return contents;
                }
                return UglifyJS.minify(contents, {fromString: true}).code;
            }
        }));
});

// MAIN GOALS ===============================================================================

gulp.task("dist:build", ["compileResources", "dist:zip"]);

gulp.task("sizereports", ["sizereport:js", "sizereport:css", "dist:build"]);

// gulp.task("default", ["dist:build", "sizereports"]);
gulp.task("default", ["dist:build"]);