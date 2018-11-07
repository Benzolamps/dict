$(function () {

  var password = $('.password');
  var leftHand = $('#left-hand');
  var rightHand = $('#right-hand');

  //得到焦点
  password.focus(function () {
    leftHand.attr('class', 'left-handing');
    leftHand.animate({
      left: "150",
      top: "-38"
    }, {
      step: function () {
        if (parseInt(leftHand.css("left")) > 140) {
          leftHand.attr("class", "left-hand");
        }
      }
    }, 2000);
    rightHand.attr('class', 'right-handing');
    rightHand.animate({
      right: "-64",
      top: "-38px"
    }, {
      step: function () {
        if (parseInt(rightHand.css("right")) > -70) {
          rightHand.attr("class", "right-hand");
        }
      }
    }, 2000);
  });

  //失去焦点
  password.blur(function () {
    leftHand.attr("class", "initial-left-hand");
    leftHand.attr("style", "left: 100px; top: -12px;");
    rightHand.attr("class", "initial-right-hand");
    rightHand.attr("style", "right: -112px; top: -12px");
  });
});