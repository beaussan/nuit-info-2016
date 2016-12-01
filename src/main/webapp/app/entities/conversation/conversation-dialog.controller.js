(function() {
    'use strict';

    angular
        .module('testJhApp')
        .controller('ConversationDialogController', ConversationDialogController);

    ConversationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Conversation', 'Message', 'ExtandedUser'];

    function ConversationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Conversation, Message, ExtandedUser) {
        var vm = this;

        vm.conversation = entity;
        vm.clear = clear;
        vm.save = save;
        vm.messages = Message.query();
        vm.extandedusers = ExtandedUser.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.conversation.id !== null) {
                Conversation.update(vm.conversation, onSaveSuccess, onSaveError);
            } else {
                Conversation.save(vm.conversation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('testJhApp:conversationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
