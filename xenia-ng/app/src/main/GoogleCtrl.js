angular.module("Xenia.GoogleAuth", ['Xenia.Common']).controller("googleAuthCtr", function($rootScope, $scope){
    var googleAuth = this;

    $rootScope.logged=false;
    this.logged=function(){
        $rootScope.logged=true;
        console.log("logged");
        $rootScope.$digest();
    };

    this.logout=function (){
        console.log("logout");
        $rootScope.logged=false;
        $rootScope.$digest();
    };

});
