let common = angular.module('Xenia.Common', []);
common.directive("fileModel",['$parse',function ($parse){
    return {
        restrict: 'A',
        link: function(scope, element, attrs){
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            element.bind('change', function() {
                scope.$apply(function() {
                    console.log("element: "); console.log(element[0].files);
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);
common.service('fileUpload',['$http', function ($http){
    this.uploadFileToUrl=function(file, uploadUrl){
        var fd = new FormData();
        console.log("File: ");
        console.log(file);
        fd.append('file', file);
        console.log(fd.getAll('file'));
        $http.post(uploadUrl,fd,{
            transformRequest: [],
            headers: {'Content-Type': undefined}
        }).then(function(response) {
            console.log("Success");
            return response.data;
        }).catch(function() {
        });
    }
}]);
