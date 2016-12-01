(function() {
    'use strict';

    angular
        .module('testJhApp')
        .controller('DirectoryDialogController', DirectoryDialogController);

    DirectoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Directory'];

    function DirectoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Directory) {
        var vm = this;

        vm.directory = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.directory.id !== null) {
                Directory.update(vm.directory, onSaveSuccess, onSaveError);
            } else {
                Directory.save(vm.directory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('testJhApp:directoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
