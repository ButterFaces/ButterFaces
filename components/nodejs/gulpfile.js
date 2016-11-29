/*
 Recipes for using Gulp:
 https://github.com/gulpjs/gulp/tree/master/docs/recipes
 */

// DEPENDENCIES ===============================================================================

var gulp = require("gulp");
var gutil = require("gulp-util");
var merge = require("merge-stream");
var bower = require("gulp-bower");
var tsd = require("gulp-tsd");
var ts = require("gulp-typescript");
var concat = require("gulp-concat");
var sourcemaps = require("gulp-sourcemaps");
var del = require("del");
var pipe = require("multipipe");
var less = require("gulp-less");
var mirror = require("gulp-mirror");
var rename = require("gulp-rename");
var postcss = require("gulp-postcss");
var autoprefixer = require("autoprefixer-core");
var tslint = require("gulp-tslint");
var uglify = require("gulp-uglify");
var UglifyJS = require("uglify-js");
var minifyCSS = require("gulp-minify-css");
var gzip = require("gulp-gzip");
var stripDebug = require("gulp-strip-debug");
var sizereport = require("gulp-sizereport");

// CONSTANTS ===============================================================================

var RESSOURCE_DIR = "../src/main/resources/META-INF/resources";

var paths = {
    bower: {
        root: "./bower_components/",
        jquery: "./bower_components/jquery/dist/**/*.{js,map}",
        jqueryinputmask: "./bower_components/jquery.inputmask/dist/jquery.inputmask.bundle.js",
        prettify: "./bower_components/google-code-prettify/src/prettify.{js,css}",
        bootstrap_css: "./bower_components/bootstrap/dist/css/bootstrap.{css,css.map}",
        bootstrap_js: "./bower_components/bootstrap/dist/js/bootstrap.{js,map}",
        bootstrap_fonts: "./bower_components/bootstrap/fonts/**/*.*"
    },
    source: {
        typescripts: RESSOURCE_DIR + "/butterfaces-ts/**/*.ts",
        javascript: RESSOURCE_DIR + "/butterfaces-js/**/*.js",
        less: RESSOURCE_DIR + "/butterfaces-less/*.less"
    },
    destination: {
        root: RESSOURCE_DIR + "/butterfaces-dist",
        css: RESSOURCE_DIR + "/butterfaces-dist-css",
        js: RESSOURCE_DIR + "/butterfaces-dist-js",
        external: RESSOURCE_DIR + "/butterfaces-external",
        bundle_js: RESSOURCE_DIR + "/butterfaces-dist-bundle-js",
        bower: RESSOURCE_DIR + "/butterfaces-dist-bower",
        bower_font: RESSOURCE_DIR + "/fonts",
        ts_external_definitions: RESSOURCE_DIR + "/butterfaces-ts/definitions/external"
    }
};

// DIST GOALS ===============================================================================

gulp.task("clean", function (cb) {
    del([
            paths.destination.css,
            paths.destination.js,
            paths.destination.bower,
            paths.destination.bower_font,
            paths.destination.bundle_js,
            paths.destination.ts_external_definitions
        ],
        {force: true}, cb);
});

gulp.task("bower:loadDependencies", function () {
    // https://github.com/bower/bower/issues/1019#issuecomment-52700170
    return bower({force: false})
        .pipe(gulp.dest(paths.bower.root));
});

gulp.task("bower:copyDependenciesToDist", ["bower:loadDependencies"], function () {
    var copyDependenciesToDist = gulp.src([
            paths.bower.jquery,
            paths.bower.jqueryinputmask,
            paths.bower.prettify,
            paths.bower.bootstrap_css,
            paths.bower.bootstrap_js
        ])
        .pipe(gulp.dest(paths.destination.bower));

    var copyFontDependenciesToDist = gulp.src([
            paths.bower.bootstrap_fonts
        ])
        .pipe(gulp.dest(paths.destination.bower_font));

    return merge(copyDependenciesToDist, copyFontDependenciesToDist);
});

gulp.task("typescript:loadDefinitions", function (callback) {
    tsd({
        command: "reinstall",
        config: "./tsd.json"
    }, callback);
});

gulp.task("typescript:lint", ["typescript:loadDefinitions"], function () {
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

gulp.task("less:compile", ["bower:copyDependenciesToDist"], function () {
    return gulp.src([paths.source.less])
        .pipe(sourcemaps.init())
        .pipe(less())
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
        .pipe(gulp.dest(paths.destination.bundle_js));
});

gulp.task("compileResources", ["less:compile", "typescript:compileToBundle", "typescript:compileToSingleFiles", "javascript:buildComponentsBundle"]);

gulp.task("javascript:buildAllBundle", ["compileResources"], function () {
    var buildButterFacesOnlyBunde = gulp.src([
            paths.destination.bower + "/prettify.js",
            paths.destination.bower + "/jquery.inputmask.bundle.js",
            paths.destination.external + "/*.js",
            paths.destination.bundle_js + "/butterfaces-ts-bundle.min.js",
            paths.destination.bundle_js + "/butterfaces-js-bundle.min.js"
        ])
        .pipe(stripDebug())
        .pipe(uglify())
        .pipe(concat("butterfaces-all-bundle.min.js"))
        .pipe(gulp.dest(paths.destination.bundle_js));

    var buildAllWithJQueryBundle = gulp.src([
            paths.destination.bower + "/jquery.min.js",
            paths.destination.bower + "/prettify.js",
            paths.destination.bower + "/jquery.inputmask.bundle.js",
            paths.destination.external + "/*.js",
            paths.destination.bundle_js + "/butterfaces-ts-bundle.min.js",
            paths.destination.bundle_js + "/butterfaces-js-bundle.min.js"
        ])
        .pipe(stripDebug())
        .pipe(uglify())
        .pipe(concat("butterfaces-all-with-jquery-bundle.min.js"))
        .pipe(gulp.dest(paths.destination.bundle_js));

    var buildAllWithBootstrapBundle = gulp.src([
            paths.destination.bower + "/prettify.js",
            paths.destination.bower + "/bootstrap.js",
            paths.destination.bower + "/jquery.inputmask.bundle.js",
            paths.destination.external + "/*.js",
            paths.destination.bundle_js + "/butterfaces-ts-bundle.min.js",
            paths.destination.bundle_js + "/butterfaces-js-bundle.min.js"
        ])
        .pipe(stripDebug())
        .pipe(uglify())
        .pipe(concat("butterfaces-all-with-bootstrap-bundle.min.js"))
        .pipe(gulp.dest(paths.destination.bundle_js));

    var buildAllWithJQueryAndBootstrapBundle = gulp.src([
            paths.destination.bower + "/jquery.min.js",
            paths.destination.bower + "/prettify.js",
            paths.destination.bower + "/bootstrap.js",
            paths.destination.bower + "/jquery.inputmask.bundle.js",
            paths.destination.external + "/*.js",
            paths.destination.bundle_js + "/butterfaces-ts-bundle.min.js",
            paths.destination.bundle_js + "/butterfaces-js-bundle.min.js"
        ])
        .pipe(concat("butterfaces-all-with-jquery-and-bootstrap-bundle.min.js"))
        .pipe(stripDebug())
        .pipe(uglify())
        .pipe(gulp.dest(paths.destination.bundle_js));

    return merge(buildButterFacesOnlyBunde, buildAllWithJQueryBundle, buildAllWithBootstrapBundle, buildAllWithJQueryAndBootstrapBundle);
});

gulp.task("dist:zip", ["javascript:buildAllBundle"], function () {
    return gulp.src([
            paths.destination.css + "/**/*",
            paths.destination.js + "/**/*",
            paths.destination.bower + "/**/*",
            paths.destination.bundle_js + "/**/*",
            "!" + paths.destination.css + "/**/*.gz",
            "!" + paths.destination.js + "/**/*.gz",
            "!" + paths.destination.bower + "/**/*.gz",
            "!" + paths.destination.bundle_js + "/**/*.gz"
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