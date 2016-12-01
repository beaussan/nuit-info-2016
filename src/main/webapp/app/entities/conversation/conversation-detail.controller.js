(function() {
    'use strict';

    angular
        .module('testJhApp')
        .controller('ConversationDetailController', ConversationDetailController);

    ConversationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Conversation', 'Message', 'ExtandedUser'];

    function ConversationDetailController($scope, $rootScope, $stateParams, previousState, entity, Conversation, Message, ExtandedUser) {
        var vm = this;

        vm.conversation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('testJhApp:conversationUpdate', function(event, result) {
            vm.conversation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
