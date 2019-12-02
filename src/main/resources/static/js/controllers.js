'use strict';

angular

    .module('app', ['angularFileUpload'])

    .controller('UploadController', ['$scope', '$http', 'FileUploader', function ($scope, $http, FileUploader) {

        var uploader = $scope.uploader = new FileUploader({
            url: "/files"
        });

        // FILTERS

        // a sync filter
        uploader.filters.push({
            name: 'syncFilter',
            fn: function (item /*{File|FileLikeObject}*/, options) {
                console.log('syncFilter');
                return this.queue.length < 10;
            }
        });

        // an async filter
        uploader.filters.push({
            name: 'asyncFilter',
            fn: function (item /*{File|FileLikeObject}*/, options, deferred) {
                console.log('asyncFilter');
                setTimeout(deferred.resolve, 1e3);
            }
        });

        // CALLBACKS

        uploader.onWhenAddingFileFailed = function (item /*{File|FileLikeObject}*/, filter, options) {
            console.info('onWhenAddingFileFailed', item, filter, options);
        };
        uploader.onAfterAddingFile = function (fileItem) {
            console.info('onAfterAddingFile', fileItem);
        };
        uploader.onAfterAddingAll = function (addedFileItems) {
            console.info('onAfterAddingAll', addedFileItems);
        };
        uploader.onBeforeUploadItem = function (item) {
            console.info('onBeforeUploadItem', item);
        };
        uploader.onProgressItem = function (fileItem, progress) {
            console.info('onProgressItem', fileItem, progress);
        };
        uploader.onProgressAll = function (progress) {
            console.info('onProgressAll', progress);
        };
        uploader.onSuccessItem = function (fileItem, response, status, headers) {
            console.info('onSuccessItem', fileItem, response, status, headers);
        };
        uploader.onErrorItem = function (fileItem, response, status, headers) {
            console.info('onErrorItem', fileItem, response, status, headers);
        };
        uploader.onCancelItem = function (fileItem, response, status, headers) {
            console.info('onCancelItem', fileItem, response, status, headers);
        };
        uploader.onCompleteItem = function (fileItem, response, status, headers) {
            console.info('onCompleteItem', fileItem, response, status, headers);
        };
        uploader.onCompleteAll = function () {
            console.info('onCompleteAll');
        };

        console.info('uploader', uploader);

        /*Object info doc*/
        $scope.document = {
            id: "-1",
            name: ""
        };

        /*Create or Update document with files*/
        $scope.saveDocument = function () {
            $http({
                method: 'POST',
                url: '/documents',
                data: $scope.document
            }).then(function (response) {
                $scope.document = response.data;
                var newUrl = "/files2/"+$scope.document.id;
                if($scope.uploader.queue.length > 0){
                    $scope.uploader.queue.forEach(function (element) {
                        element.url = newUrl;
                    })
                    $scope.uploader.url = "/files2/"+$scope.document.id;
                    $scope.uploader.uploadAll();
                }
            })
        }
    }])
    .controller('FileController', function ($scope, $http) {

        var path = "/files";

        $http.get(path).then(function (response) {
            $scope.files = response.data;
        });

        $scope.atualizaLista = function () {
            $http.get(path).then(function (response) {
                $scope.files = response.data;
            });
        }

        $scope.removeFile = function (arquivo) {
            $http.get(path+"/" + arquivo.id + "/remove").then(function (response) {
                $scope.files = $scope.files.filter(function (item) {
                    return item.id != arquivo.id;
                });

            }).catch(function (erro) {
                console.log(erro)
            });
        }
        $scope.pathDownload = function (arquivo) {
            return path+"/" + arquivo.id + "/download";
        }

    });