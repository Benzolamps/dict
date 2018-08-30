$(function () {
    // noinspection SpellCheckingInspection
    var colors = [
        '#AAFF00',
        '#7CD14E',
        '#1CDCFF',
        '#FFFF00',
        '#FF0000',
        '#aee137',
        '#bef202',
        '#00b2ff',
        '#7fff24',
        '#13cd4b',
        '#c0fa38',
        '#FF00AA',
        '#AA00FF',
        '#00AAFF',
        '#222222'
    ];
    var canvas = document.getElementById('canvas');
    var context = canvas.getContext('2d');
    canvas.height = window.innerHeight;
    canvas.width = window.innerWidth;
    var mouse = {x: 0, y: 0};
    canvas.addEventListener("mousemove", function (a) {
        mouse.x = a.pageX - canvas.offsetLeft;
        mouse.y = a.pageY - canvas.offsetTop;
        mouseMove && (context.lineTo(mouseX, mouseY), context.stroke());
    }, false);
    canvas.addEventListener('mousedown', function () {
        mouseDown = true;
    }, false);
    canvas.addEventListener('mouseup', function () {
        mouseMove && (mouseMove = false);
        mouseDown && (mouseDown = false);
    }, false);
    window.addEventListener('resize', function () {
        canvas.height = window.innerHeight;
        canvas.width = window.innerWidth;
        cancelAnimationFrame(animate);
    }, false);
    var dotsHolder = [], mouseMove = false, mouseDown = false;
    requestAnimationFrame ||
    (requestAnimationFrame = msRequestAnimationFrame) ||
    (requestAnimationFrame = mozRequestAnimationFrame) ||
    (requestAnimationFrame = webkitRequestAnimationFrame)
    (requestAnimationFrame = oRequestAnimationFrame) ||
    (requestAnimationFrame = function (b) {
        var a, d = (new Date).getTime(), e = Math.max(0, 16 - (d - a)), f = window.setTimeout(function () {
            b(d + e);
        }, e);
        return a = d + e, f;
    });
    cancelAnimationFrame || (cancelAnimationFrame = function (a) {
        clearTimeout(a);
    });

    var dots = function () {
        var object = {
            xPos: Math.random() * canvas.width,
            yPos: Math.random() * canvas.height,
            color: colors[Math.floor(Math.random() * colors.length)],
            radius: 10 * Math.random(),
            stepSize: Math.random() / 10,
            step: 0,
            friction: 7
        };
        object.vx = Math.cos(object.radius);
        object.vy = Math.sin(object.radius);
        object.speedX = object.vx;
        object.speedY = object.vy;
        return object;
    };

    var init = function () {
        for (var i = 0; i < 1e3; i++) dotsHolder.push(dots());

    };

    var animate = function () {
        requestAnimationFrame(animate);
        var a, b, c, d, e, f, g, h, i, j, k;
        context.clearRect(0, 0, canvas.width, canvas.height);
        for (var a = 0; a < dotsHolder.length; a++) {
            var dot = dotsHolder[a];
            context.beginPath();
            f = dot.xPos - mouse.x;
            g = dot.yPos - mouse.y;
            b = Math.sqrt(f * f + g * g);
            c = Math.max(Math.min(75 / (b / dot.radius), 7), 1);
            context.fillStyle = dot.color;
            dot.xPos += dot.vx;
            dot.yPos += dot.vy;
            dot.xPos < -50 && (dot.xPos = canvas.width + 50);
            dot.yPos < -50 && (dot.yPos = canvas.height + 50);
            dot.xPos > canvas.width + 50 && (dot.xPos = -50);
            dot.yPos > canvas.height + 50 && (dot.yPos = -50);
            context.arc(dot.xPos, dot.yPos, dot.radius / 2.5 * c, 0, 2 * Math.PI, false);
            context.fill();
            if (mouseDown) {
                d = 164;
                e = Math.sqrt((dot.xPos - mouse.x) * (dot.xPos - mouse.x) + (dot.yPos - mouse.y) * (dot.yPos - mouse.y));
                f = dot.xPos - mouse.x;
                g = dot.yPos - mouse.y;
                if (d > e) {
                    h = d / (e * e);
                    i = (mouse.x - dot.xPos) % e / 7;
                    j = (mouse.y - dot.yPos) % e / dot.friction;
                    k = 2 * h / dot.friction;
                    dot.vx -= k * i;
                    dot.vy -= k * j;
                };
                dot.vx > dot.speed ?
                    (
                        dot.vx = dot.speed / dot.friction,
                        dot.vy = dot.speed / dot.friction
                    ) :
                    dot.vy > dot.speed && (dot.vy = dot.speed / dot.friction);
            };
        }
    };

    init();
    animate();
});