// this may help on MacOS when gyp / xcode errors occure on "npm install":
// https://github.com/schnerd/d3-scale-cluster/issues/7#issuecomment-550579897

// REQUIREMENTS ===============================================================================

const {src, dest, parallel, series} = require('gulp');
const clean = require('gulp-clean');
const debug = require('gulp-debug');
const concat = require('gulp-concat');
const rename = require("gulp-rename");
const sourcemaps = require("gulp-sourcemaps");
const sass = require("gulp-sass");
const postcss = require("gulp-postcss");
const cleanCSS = require("gulp-clean-css");
const autoprefixer = require("autoprefixer");
const multipipe = require("multipipe");
const mirror = require("gulp-mirror");
const ts = require("gulp-typescript");
const uglify = require("gulp-uglify");
const UglifyJS = require("uglify-js");
const gzip = require("gulp-gzip");
const stripDebug = require("gulp-strip-debug");

// PATHS ===============================================================================

const RESSOURCE_DIR = "../src/main/resources/META-INF/resources";
const NODEJS_RESSOURCE_DIR = "./src";

const paths = {
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

// TASK FUNCTIONS ===========================================================================

const cleanDist = () => {
    return src([
        paths.destination.root,
        paths.destination.css,
        paths.destination.js,
        paths.destination.bundle_js,
        paths.destination.bundle_dev_js,
        paths.destination.npm,
        paths.destination.npm_font
    ], {read: false, allowEmpty: true})
        .pipe(clean({force: true}));
};

const copyNpmDependenciesToDist = () => {
    return src([
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
        .pipe(dest(paths.destination.npm));
};

const copyGlyphiconsToDist = () => {
    return src([
        paths.npm.glyphicons_css
    ])
        .pipe(rename("glyphicons.css"))
        .pipe(dest(paths.destination.npm));
};

const copyFontDependenciesToDist = () => {
    return src([
        paths.npm.glyphicon_fonts
    ])
        .pipe(dest(paths.destination.npm_font));
};

const copyResources = parallel(copyNpmDependenciesToDist, copyGlyphiconsToDist, copyFontDependenciesToDist);

const compileAndCopyScssFiles = () => {
    return src([paths.source.sass])
        .pipe(sourcemaps.init())
        .pipe(sass())
        .pipe(postcss([autoprefixer({browsers: ["> 2%"]})]))
        .pipe(mirror(
            multipipe(
                rename(function (path) {
                    path.basename += ".min";
                }),
                cleanCSS()
            )
        ))
        .pipe(sourcemaps.write())
        .pipe(dest(paths.destination.css));
};

const compileTypescriptToBundle = () => {
    const tsResult = src(paths.source.typescripts)
        .pipe(sourcemaps.init())
        .pipe(ts({
            noImplicitAny: false,
            target: "es5"
        }));

    return tsResult.js
        .pipe(concat("butterfaces-ts-bundle.js"))
        .pipe(stripDebug())
        .pipe(mirror(
            multipipe(
                rename(function (path) {
                    path.basename += ".min";
                }),
                uglify()
            )
        ))
        .pipe(sourcemaps.write())
        .pipe(dest(paths.destination.bundle_js));
};

const compileTypescriptToSingleFiles = () => {
    // for development usage
    const tsResult = src(paths.source.typescripts)
        .pipe(sourcemaps.init())
        .pipe(ts({
            noImplicitAny: false,
            target: "es5"
        }));

    return tsResult.js
        .pipe(mirror(
            multipipe(
                rename(function (path) {
                    path.basename += ".min";
                }),
                uglify()
            )
        ))
        .pipe(sourcemaps.write())
        .pipe(dest(paths.destination.js));
};

// PUBLIC TASKS ===============================================================================

exports.cleanDist = cleanDist;
exports.default = series(copyResources, parallel(compileAndCopyScssFiles, compileTypescriptToBundle, compileTypescriptToSingleFiles));