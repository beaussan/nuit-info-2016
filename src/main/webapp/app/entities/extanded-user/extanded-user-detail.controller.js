(function() {
    'use strict';

    angular
        .module('testJhApp')
        .controller('ExtandedUserDetailController', ExtandedUserDetailController);

    ExtandedUserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ExtandedUser', 'User', 'ContactRequest', 'Conversation'];

    function ExtandedUserDetailController($scope, $rootScope, $stateParams, previousState, entity, ExtandedUser, User, ContactRequest, Conversation) {
        var vm = this;

        vm.extandedUser = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('testJhApp:extandedUserUpdate', function(event, result) {
            vm.extandedUser = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
