function init() {
    rollElement = document.getElementById("rolldice");
    console.log("code running")

}
let whoplays=true;
function diceRoll(){
    let valueOfDice=Math.ceil(Math.random()*6);
    let diceDisplay=document.getElementById("dice");
    diceDisplay.innerText=valueOfDice;
    presentScore();
}
//let rollElement.addEventListener("click", diceRoll());
function passTurn(){

    let score=Number(document.getElementById('presentScore').innerText);
    if(whoplays){
            
            document.getElementById('userScore').innerText = score;

    }
    else{
         
        document.getElementById('computerScore').innerText = score;
    }
    whoplays =!whoplays;
    document.getElementById('presentScore').innerText="";
}

function presentScore(){
    let value=Number(document.getElementById("presentScore").innerText);
    let diceValue = Number(document.getElementById("dice").innerText);
    if(diceValue!=1){
        value+=diceValue;
    }
    else{
        value=0;
        passTurn();
    }
    document.getElementById("presentScore").innerText=value;
   

}


init();
