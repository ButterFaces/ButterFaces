// DEPENDENCIES ===============================================================================

var gulp = require('gulp');
var gutil = require('gulp-util');
var bower = require('gulp-bower');
var tsd = require('gulp-tsd');
var ts = require('gulp-typescript');
var concat = require('gulp-concat');
var sourcemaps = require('gulp-sourcemaps');
var del = require('del');
var watch = require('gulp-watch');
var pipe = require('multipipe');
var less = require('gulp-less');
var mirror = require('gulp-mirror');
var rename = require('gulp-rename');
var postcss = require('gulp-postcss');
var autoprefixer = require('autoprefixer-core');
var livereload = require('gulp-livereload');
var tslint = require('gulp-tslint');
var inject = require('gulp-inject');
var uglify = require('gulp-uglify');
var UglifyJS = require('uglify-js');
var minifyCSS = require('gulp-minify-css');
var gzip = require('gulp-gzip');
var stripDebug = require('gulp-strip-debug');
var sizereport = require('gulp-sizereport');

// CONSTANTS ===============================================================================

var paths = {
    bower: {
        root: './bower_components/',
        jquery: './bower_components/jquery/dist/**/*.{js,map}',
        prettify: './bower_components/google-code-prettify/src/prettify.{js,css}',
        bootstrap_css: './bower_components/bootstrap/dist/css/bootstrap.{css,css.map}',
        bootstrap_js: './bower_components/bootstrap/dist/js/bootstrap.{js,map}',
        bootstrap_fonts: './bower_components/bootstrap/fonts/**/*.*'
    },
    source: {
        typescripts: '../src/main/resources/META-INF/resources/butterfaces-ts/**/*.ts',
        javascript: '../src/main/resources/META-INF/resources/butterfaces-js/**/*.js',
        less: '../src/main/resources/META-INF/resources/butterfaces-less/*.less'
    },
    destination: {
        root: '../src/main/resources/META-INF/resources/butterfaces-dist',
        css: '../src/main/resources/META-INF/resources/butterfaces-dist-css',
        js: '../src/main/resources/META-INF/resources/butterfaces-dist-js',
        external: '../src/main/resources/META-INF/resources/butterfaces-external',
        bundle_js: '../src/main/resources/META-INF/resources/butterfaces-dist-bundle-js',
        bower: '../src/main/resources/META-INF/resources/butterfaces-dist-bower',
        bower_font: '../src/main/resources/META-INF/resources/fonts'
    }
};

// DIST GOALS ===============================================================================

gulp.task('clean', function (cb) {
    del([
            paths.destination.css,
            paths.destination.js,
            paths.destination.bower,
            paths.destination.bower_font,
            paths.destination.bundle_js
        ],
        {force: true}, cb);
});

gulp.task('dist:_bower', function () {
    // https://github.com/bower/bower/issues/1019#issuecomment-52700170
    return bower({force: false})
        .pipe(gulp.dest(paths.bower.root));
});

gulp.task('dist:_tslint', ['dist:_bower'], function () {
    return gulp.src(paths.source.typescripts)
        .pipe(tslint())
        .pipe(tslint.report('verbose'));
});

gulp.task('bower:copyBowerDependenciesToDist', ['dist:_bower'], function () {
    return gulp.src([
            paths.bower.jquery,
            paths.bower.prettify,
            paths.bower.bootstrap_css,
            paths.bower.bootstrap_js
        ])
        .pipe(gulp.dest(paths.destination.bower));
});

gulp.task('bower:copyBowerFontDependenciesToDist', ['dist:_bower'], function () {
    return gulp.src([
            paths.bower.bootstrap_fonts
        ])
        .pipe(gulp.dest(paths.destination.bower_font));
});

gulp.task('dist:_typescript_bundle', ['dist:_tslint', 'bower:copyBowerDependenciesToDist', 'bower:copyBowerFontDependenciesToDist'], function () {
    var tsResult = gulp.src(paths.source.typescripts)
        .pipe(ts({
            noImplicitAny: true,
            target: 'es5'
        }));

    return tsResult.js
        .pipe(concat('butterfaces-ts-bundle.js'))
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

gulp.task('javascript:buildComponentsBundle', function () {
    return gulp.src(paths.source.javascript)
        .pipe(concat('butterfaces-js-bundle.js'))
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

gulp.task('dist:_typescript_single', ['dist:_tslint', 'bower:copyBowerDependenciesToDist', 'bower:copyBowerFontDependenciesToDist'], function () {
    var tsResult = gulp.src(paths.source.typescripts)
        .pipe(sourcemaps.init())
        .pipe(ts({
            noImplicitAny: true,
            target: 'es5'
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

gulp.task('dist:_less', ['bower:copyBowerDependenciesToDist', 'bower:copyBowerFontDependenciesToDist'], function () {
    return gulp.src([paths.source.less])
        .pipe(sourcemaps.init())
        .pipe(less())
        .pipe(postcss([autoprefixer({browsers: ['> 2%']})]))
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

gulp.task('javascript:compileResources', ['dist:_less', 'dist:_typescript_bundle', 'dist:_typescript_single', 'javascript:buildComponentsBundle']);

gulp.task('javascript:buildAllBundle', ['javascript:compileResources'], function () {
    return gulp.src([
            paths.destination.bower + '/prettify.js',
            paths.destination.external + '/*.js',
            paths.destination.bundle_js + '/butterfaces-ts-bundle.min.js',
            paths.destination.bundle_js + '/butterfaces-js-bundle.min.js'
        ])
        .pipe(concat('butterfaces-all-bundle.min.js'))
        .pipe(gulp.dest(paths.destination.bundle_js));
});

gulp.task('javascript:buildAllWithJQueryBundle', ['javascript:compileResources'], function () {
    return gulp.src([
            paths.destination.bower + '/jquery.min.js',
            paths.destination.bower + '/prettify.js',
            paths.destination.external + '/*.js',
            paths.destination.bundle_js + '/butterfaces-ts-bundle.min.js',
            paths.destination.bundle_js + '/butterfaces-js-bundle.min.js'
        ])
        .pipe(concat('butterfaces-all-with-jquery-bundle.min.js'))
        .pipe(gulp.dest(paths.destination.bundle_js));
});

gulp.task('javascript:buildAllWithBootstrapBundle', ['javascript:compileResources'], function () {
    return gulp.src([
            paths.destination.bower + '/prettify.js',
            paths.destination.bower + '/bootstrap.js',
            paths.destination.external + '/*.js',
            paths.destination.bundle_js + '/butterfaces-ts-bundle.min.js',
            paths.destination.bundle_js + '/butterfaces-js-bundle.min.js'
        ])
        .pipe(stripDebug())
        .pipe(uglify())
        .pipe(concat('butterfaces-all-with-bootstrap-bundle.min.js'))
        .pipe(gulp.dest(paths.destination.bundle_js));
});

gulp.task('javascript:buildAllWithJQueryAndBootstrapBundle', ['javascript:compileResources'], function () {
    return gulp.src([
            paths.destination.bower + '/jquery.min.js',
            paths.destination.bower + '/prettify.js',
            paths.destination.bower + '/bootstrap.js',
            paths.destination.external + '/*.js',
            paths.destination.bundle_js + '/butterfaces-ts-bundle.min.js',
            paths.destination.bundle_js + '/butterfaces-js-bundle.min.js'
        ])
        .pipe(concat('butterfaces-all-with-jquery-and-bootstrap-bundle.min.js'))
        .pipe(stripDebug())
        .pipe(uglify())
        .pipe(gulp.dest(paths.destination.bundle_js));
});

gulp.task('zip-dist', ['javascript:buildAllBundle', 'javascript:buildAllWithJQueryBundle', 'javascript:buildAllWithBootstrapBundle', 'javascript:buildAllWithJQueryAndBootstrapBundle'], function () {
    return gulp.src([
            paths.destination.css + '/**/*',
            paths.destination.js + '/**/*',
            paths.destination.bower + '/**/*',
            paths.destination.bundle_js + '/**/*',
            '!' + paths.destination.css + '/**/*.gz',
            '!' + paths.destination.js + '/**/*.gz',
            '!' + paths.destination.bower + '/**/*.gz',
            '!' + paths.destination.bundle_js + '/**/*.gz'
        ], {base: '.'})
        .pipe(gzip())
        .pipe(gulp.dest('.'));
});

gulp.task('sizereport-css', function () {
    return gulp.src([
            paths.destination.css + '/*.css',
            '!' + paths.destination.css + '/*.sourcemaps.css'
        ])
        .pipe(sizereport({gzip: true}));
});

gulp.task('sizereport-js', function () {
    return gulp.src([
            paths.destination.js + '/*.js',
            '!' + paths.destination.js + '/*.min.js',
        ])
        .pipe(sizereport({
            gzip: true,
            minifier: function (contents, filepath) {
                if (filepath.match(/\.min\./g)) {
                    return contents
                }
                return UglifyJS.minify(contents, {fromString: true}).code;
            }
        }));
});

// MAIN GOALS ===============================================================================

gulp.task('dist:build', ['javascript:compileResources', 'zip-dist']);

gulp.task('sizereports', ['sizereport-js', 'sizereport-css', 'dist:build']);

// gulp.task('default', ['dist:build', 'sizereports']);
gulp.task('default', ['dist:build']);