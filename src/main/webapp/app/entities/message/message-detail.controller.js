(function() {
    'use strict';

    angular
        .module('testJhApp')
        .controller('MessageDetailController', MessageDetailController);

    MessageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Message', 'ExtandedUser', 'Conversation'];

    function MessageDetailController($scope, $rootScope, $stateParams, previousState, entity, Message, ExtandedUser, Conversation) {
        var vm = this;

        vm.message = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('testJhApp:messageUpdate', function(event, result) {
            vm.message = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
