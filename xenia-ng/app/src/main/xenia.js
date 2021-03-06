var xenia = angular.module('xenia', [
    'ngRoute',
    'ngAnimate',
    'Xenia.Common',
    'Xenia.Navigation',
    'Xenia.Event',
    'Xenia.Prize',
    'Xenia.Dashboard',
    'Xenia.GoogleAuth'
]);

xenia.value('XENIA_API_URL', 'http://localhost:8080');
xenia.value('MEETUP', false);
xenia.config(function($routeProvider, $httpProvider) {
    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];

    $routeProvider
        .when('/dashboard', {
            controller: 'DashboardCtrl',
            controllerAs: 'dashboard',
            templateUrl: 'src/main/dashboard/views/dashboard.html'
        })
        .when('/event/:id', {
            controller: 'EventCtrl',
            controllerAs: 'event',
            templateUrl: 'src/main/event/views/event.html'
        })
        .when('/prizes', {
            controller: 'PrizeCtrl',
            controllerAs: 'prizes',
            templateUrl: 'src/main/prize/views/prizes.html'
        })
        .otherwise({
            redirectTo: '/dashboard'
        });
});



function signOut(){
    console.log('logout...');

    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
        angular.element($('#google-logged')).controller().logout();
    });

}
function signIn(){
    var auth2 = gapi.auth2.getAuthInstance()    ;
    auth2.signIn().then(function (){
        angular.element($('#google-logged')).controller().logged();
    })
}
