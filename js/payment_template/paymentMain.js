ubsApp.getPayTemplate=function(templateConfig,tempVar){
    templateConfig.currentPlayerName = userArray[playerChance].getplayerName();
	tempVar.html+=ubsLuckTemplate(templateConfig);
}

ubsApp.payFromBank=function(pageName){

    var credit=ubsApp.pages[pageName].templates[0].credit;
    $.each(credit, function(key, value) {
        if(key=="bankBalance"){
            userArray[playerChance].setBankBalance(userArray[playerChance].getBankBalance()+value);     
                if(userArray[playerChance].getBankBalance()<0){
                    
                    let difference=userArray[playerChance].getBankBalance();
                    userArray[playerChance].setBankBalance(0);
                    
                    
                    difference=userArray[playerChance].getInventoryScore();
                    userArray[playerChance].setInventoryScore(0);
                    if(userArray[playerChance].getCredit()+(difference*(-1))<=userArray[playerChance].getCreditLimit()){
                        userArray[playerChance].setCredit(userArray[playerChance].getCredit()+(difference*(-1)));
                    }
                    else{
                        userArray[playerChance].setCredit(userArray[playerChance].getCreditLimit());
                    }
                }
        }
        else if(key=="reputationPoints"){
            userArray[playerChance].setReputationPts(userArray[playerChance].getReputationPts()+value);
        }
    });

    ubsApp.nextMove();

    // if(userArray[playerChance].getBankBalance()<amount){
    //     document.getElementById("result").innerHTML=ubsApp.translation["bankBalance"];
    // }
    // else{
    //     userArray[playerChance].setBankBalance(userArray[playerChance].getBankBalance()+amount);
    //     ubsApp.nextMove();
    // }

}