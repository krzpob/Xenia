angular.module('Xenia.Dashboard')
    .controller('DashboardCtrl', function($scope, Event, fileUpload,XENIA_API_URL){
        var dashboard = this;

        dashboard.events = [];
        dashboard.isRefreshing = false;

        dashboard.init = function() {
            dashboard.getEvents();
        };

        dashboard.getEvents = function() {
            Event.listAll().then(function(result){
                dashboard.events = result.data;
            });
        };

        dashboard.refreshEvents = function() {
            dashboard.isRefreshing = true;
            Event.refreshAll().then(function(){
                dashboard.getEvents();
                dashboard.isRefreshing = false;
            });
        };

        dashboard.fileUpload = function (){
            var file = $scope.eventsModel;
            if(file==null){
                alert("Nie wybranu pliku!");
                return;
            }
            console.log("scope: "); console.log($scope);
            var uploadUrl=XENIA_API_URL+"/events/import";
            fileUpload.uploadFileToUrl(file,uploadUrl);
            $("#eventsImportModal").modal("hide");
            dashboard.getEvents();
        };

        dashboard.importEvents = function(){
            $scope.eventsModel=null;
            $("#eventsImportModal").modal("show");
        }
        dashboard.init();
    });
