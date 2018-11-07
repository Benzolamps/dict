@charset "UTF-8";

.required-field {
  color: #FF0033;
}

a {
  cursor: pointer;
}

:not(input), input[type=password] {
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}

textarea, .selectable {
  -webkit-user-select: text;
  -moz-user-select: text;
  -ms-user-select: text;
  user-select: text;
}
