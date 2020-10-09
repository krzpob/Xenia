

angular.module('Xenia.Dashboard')
    .controller('DashboardCtrl', function($scope, Event, fileUpload,XENIA_API_URL, MEETUP,$rootScope){
        var dashboard = this;

        dashboard.events = [];
        dashboard.isRefreshing = false;

        dashboard.init = function() {

            if($rootScope.logged) {
                dashboard.getEvents();
            }
            dashboard.meetup=MEETUP;
        };
        var counter=0;
        $rootScope.$watch('logged', function (n,o){
            console.log("change no "+counter++);

            console.log(o+"->"+n);

            if(n){
                dashboard.init();
                $('.g-signin2').hide();
            } else {
                $('.g-signin2').show();
            }



        });

        dashboard.getEvents = function() {
            Event.listAll().then(function(result){
                dashboard.events = result.data;
            });
        };

        dashboard.clearForm = function (){
            dashboard.editingEvent={};
        };

        dashboard.addNewEvent = function(){
            dashboard.clearForm();
            $("#eventModal").modal("show");
        };

        dashboard.createEvent = function (){
            dashboard.isRefreshing = true;
            Event.create(dashboard.editingEvent)
                .then(function (){
                    dashboard.getEvents();
                    dashboard.isRefreshing=false;
                    $("#eventModal").modal("hide");
                });
        }

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
