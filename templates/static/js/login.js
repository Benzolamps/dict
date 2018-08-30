$(function () {

    var password = $('.password');
    var leftHand = $('#left_hand');
    var rightHand = $('#right_hand');

    //得到焦点
    password.focus(function () {
        leftHand.attr('class', 'left_handing');
        leftHand.animate({
            left: "150",
            top: "-38"
        }, {
            step: function () {
                if (parseInt(leftHand.css("left")) > 140) {
                    leftHand.attr("class", "left_hand");
                }
            }
        }, 2000);
        rightHand.attr('class', 'right_handing');
        rightHand.animate({
            right: "-64",
            top: "-38px"
        }, {
            step: function () {
                if (parseInt(rightHand.css("right")) > -70) {
                    rightHand.attr("class", "right_hand");
                }
            }
        }, 2000);
    });

    //失去焦点
    password.blur(function () {
        leftHand.attr("class", "initial_left_hand");
        leftHand.attr("style", "left: 100px; top: -12px;");
        rightHand.attr("class", "initial_right_hand");
        rightHand.attr("style", "right: -112px; top: -12px");
    });
});