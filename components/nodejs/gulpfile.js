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
        jquery: './bower_components/jquery/dist/**/*.{js,map}'
    },
    source: {
        typescripts: '../src/main/resources/META-INF/resources/butterfaces-ts/**/*.ts',
        less: '../src/main/resources/META-INF/resources/butterfaces-less/*.less'
    },
    destination: {
        root: '../src/main/resources/META-INF/resources//butterfaces-dist',
        css: '../src/main/resources/META-INF/resources//butterfaces-dist-css',
        js: '../src/main/resources/META-INF/resources//butterfaces-dist-js',
        bower: '../src/main/resources/META-INF/resources//butterfaces-dist-configurable'
    }
};

// DIST GOALS ===============================================================================

gulp.task('clean', function (cb) {
    del([paths.destination.css, paths.destination.js, paths.destination.bower], {force: true}, cb);
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

gulp.task('dist:_copyLibsToDist', ['dist:_bower'], function () {
    return gulp.src([
                paths.bower.jquery
            ])
            .pipe(gulp.dest(paths.destination.bower));
});

gulp.task('dist:_typescript_bundle', ['dist:_tslint', 'dist:_copyLibsToDist'], function () {
    var tsResult = gulp.src(paths.source.typescripts)
            .pipe(ts({
                noImplicitAny: true,
                target: 'es5'
            }));

    return tsResult.js
            .pipe(concat('butterfaces.js'))
            .pipe(stripDebug())
            .pipe(mirror(
                    pipe(
                            rename(function (path) {
                                path.basename += ".min";
                            }),
                            uglify()
                    )
            ))
            .pipe(gulp.dest(paths.destination.js));
});

gulp.task('dist:_typescript_single', ['dist:_tslint', 'dist:_copyLibsToDist'], function () {
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
            .pipe(pipe(sourcemaps.write()))
            .pipe(gulp.dest(paths.destination.js));
});

gulp.task('dist:_less', function () {
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
            .pipe(pipe(sourcemaps.write()))
            .pipe(gulp.dest(paths.destination.css));
});

gulp.task('dist:_compileRessources', ['dist:_less', 'dist:_typescript_bundle', 'dist:_typescript_single']);

gulp.task('zip-dist', ['dist:_compileRessources'], function () {
    return gulp.src([
                paths.destination.css + '/**/*',
                paths.destination.js + '/**/*',
                paths.destination.bower + '/**/*',
                '!' + paths.destination.css + '/**/*.gz',
                '!' + paths.destination.js + '/**/*.gz',
                '!' + paths.destination.bower + '/**/*.gz'
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

gulp.task('dist:build', ['dist:_compileRessources', 'zip-dist']);

gulp.task('sizereports', ['sizereport-js', 'sizereport-css', 'dist:build']);

// gulp.task('default', ['dist:build', 'sizereports']);
gulp.task('default', ['dist:build']);