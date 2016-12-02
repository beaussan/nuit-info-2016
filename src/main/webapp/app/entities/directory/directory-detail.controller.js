(function() {
    'use strict';

    angular
        .module('testJhApp')
        .controller('DirectoryDetailController', DirectoryDetailController);

    DirectoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Directory'];

    function DirectoryDetailController($scope, $rootScope, $stateParams, previousState, entity, Directory) {
        var vm = this;

        vm.directory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('testJhApp:directoryUpdate', function(event, result) {
            vm.directory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
