var gulp = require('gulp');
// npm install --save-dev gulp-autoprefixer
var autoprefixer = require('gulp-autoprefixer');

gulp.task("autoprefixCSS", function () {
    return gulp.src(["components/src/main/resources/META-INF/resources/butterfaces-css/*"])
            .pipe(autoprefixer({
                browsers: ['> 2%'],
                cascade: false
            }))
            .pipe(gulp.dest('components/src/main/resources/META-INF/resources/butterfaces-css-prefixed'));
});

gulp.task("default", ["autoprefixCSS"]);