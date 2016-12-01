(function() {
    'use strict';

    angular
        .module('testJhApp')
        .controller('ExtandedUserDialogController', ExtandedUserDialogController);

    ExtandedUserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'ExtandedUser', 'User', 'ContactRequest', 'Conversation'];

    function ExtandedUserDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, ExtandedUser, User, ContactRequest, Conversation) {
        var vm = this;

        vm.extandedUser = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.contactrequests = ContactRequest.query();
        vm.conversations = Conversation.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.extandedUser.id !== null) {
                ExtandedUser.update(vm.extandedUser, onSaveSuccess, onSaveError);
            } else {
                ExtandedUser.save(vm.extandedUser, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('testJhApp:extandedUserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.lastConnection = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
