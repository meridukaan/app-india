
var currentClass = '';
var randomNumber = 0;
var rollingDiceConfig = {};
var diceVal = 0;

$(document).ready(function() {
    var windowHeight =  $(window).height();
    $('#rollscene').css('width',windowHeight/3+"px");
    $('#rollscene').css('height',windowHeight/3+"px");
});

function changeSide() {
  var cube = document.querySelector('.cube');
  diceVal = (Math.floor(Math.random()*6)+1);
  var showClass = 'show-' + diceVal;
  if ( currentClass ) {
    cube.classList.remove( currentClass );
  }
  cube.classList.add( showClass );
  currentClass = showClass;
  return diceVal;
  
}
// set initial side
changeSide();


function mainroll(){
  var diceValue;
  for(var i = 0; i < 10; i++){
    setTimeout(function(){ 
      diceValue = changeSide();
     }, i*190);
  } 
  
}



