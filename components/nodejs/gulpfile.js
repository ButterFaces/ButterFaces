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
var minifyCSS = require('gulp-minify-css');
var stripDebug = require('gulp-strip-debug');

// CONSTANTS ===============================================================================

var paths = {
    bower: {
        jquery: './bower_components/jquery/dist/**/*.{js,map}'
    },
    source: {
        typescripts: '../src/main/resources/META-INF/resources/butterfaces-ts/**/*.ts',
        less: '../src/main/resources/META-INF/resources/butterfaces-less/*.less'
    },
    destination: {
        standard: '../src/main/resources/META-INF/resources//butterfaces-dist',
        bower: '../src/main/webapp/dist/bower'
    }
};

// DIST GOALS ===============================================================================

gulp.task('dist:_clean', function (cb) {
    del(paths.destination.standard, {force: true}, cb);
});

gulp.task('dist:_bower', function () {
    // https://github.com/bower/bower/issues/1019#issuecomment-52700170
    return bower({force: false})
        .pipe(gulp.dest('bower_components/'));
});

gulp.task('dist:_tslint', ['dist:_bower'], function () {
    return gulp.src(paths.source.typescripts)
        .pipe(tslint())
        .pipe(tslint.report('verbose'));
});

gulp.task('dist:_copyLibsToDist', ['dist:_bower'], function () {
    return gulp.src([
            './bower_components/jquery/dist/jquery.js',
            './bower_components/jquery/dist/jquery.min.js'
        ])
        .pipe(gulp.dest(paths.destination.standard + '/js/lib'));
});

//gulp.task('dist:_typescript', ['dist:_tslint', 'dist:_copyLibsToDist', 'dev:_tsd'], function () {
gulp.task('dist:_typescript', ['dist:_tslint', 'dist:_copyLibsToDist'], function () {
    var tsResult = gulp.src(paths.source.typescripts)
        .pipe(ts({
            noImplicitAny: true
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
        .pipe(gulp.dest(paths.destination.standard + '/js'));
});

gulp.task('dist:_less', function () {
    return gulp.src([paths.source.less])
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
        .pipe(gulp.dest(paths.destination.standard + '/css'));
});

gulp.task('dist:_compileRessources', ['dist:_less', 'dist:_typescript']);

// MAIN GOALS ===============================================================================

gulp.task('dist:build', ['dist:_clean', 'dist:_compileRessources']);

gulp.task('default', ['dist:build']);