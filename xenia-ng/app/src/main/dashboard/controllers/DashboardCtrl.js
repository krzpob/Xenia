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
            var file = $scope.evensModel;
            var uploadUrl=XENIA_API_URL+"/events/import";
            fileUpload.uploadFileToUrl(file,uploadUrl);
        };

        dashboard.importEvents = function(){
            $scope.evensModel=null;
            $("#eventsImportModal").modal("show");
        }
        dashboard.init();
    });
