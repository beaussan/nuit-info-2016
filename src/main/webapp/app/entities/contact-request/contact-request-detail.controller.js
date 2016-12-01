(function() {
    'use strict';

    angular
        .module('testJhApp')
        .controller('ContactRequestDetailController', ContactRequestDetailController);

    ContactRequestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ContactRequest', 'ExtandedUser'];

    function ContactRequestDetailController($scope, $rootScope, $stateParams, previousState, entity, ContactRequest, ExtandedUser) {
        var vm = this;

        vm.contactRequest = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('testJhApp:contactRequestUpdate', function(event, result) {
            vm.contactRequest = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
