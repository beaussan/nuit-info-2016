(function() {
    'use strict';

    angular
        .module('testJhApp')
        .controller('MessageDialogController', MessageDialogController);

    MessageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Message', 'ExtandedUser', 'Conversation'];

    function MessageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Message, ExtandedUser, Conversation) {
        var vm = this;

        vm.message = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.sources = ExtandedUser.query({filter: 'message-is-null'});
        $q.all([vm.message.$promise, vm.sources.$promise]).then(function() {
            if (!vm.message.source || !vm.message.source.id) {
                return $q.reject();
            }
            return ExtandedUser.get({id : vm.message.source.id}).$promise;
        }).then(function(source) {
            vm.sources.push(source);
        });
        vm.conversations = Conversation.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.message.id !== null) {
                Message.update(vm.message, onSaveSuccess, onSaveError);
            } else {
                Message.save(vm.message, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('testJhApp:messageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateWriten = false;
        vm.datePickerOpenStatus.dateSeen = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
