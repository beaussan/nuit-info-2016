(function() {
    'use strict';

    angular
        .module('testJhApp')
        .controller('DirectoryDeleteController',DirectoryDeleteController);

    DirectoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'Directory'];

    function DirectoryDeleteController($uibModalInstance, entity, Directory) {
        var vm = this;

        vm.directory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Directory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
