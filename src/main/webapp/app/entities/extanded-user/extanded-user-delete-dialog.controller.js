(function() {
    'use strict';

    angular
        .module('testJhApp')
        .controller('ExtandedUserDeleteController',ExtandedUserDeleteController);

    ExtandedUserDeleteController.$inject = ['$uibModalInstance', 'entity', 'ExtandedUser'];

    function ExtandedUserDeleteController($uibModalInstance, entity, ExtandedUser) {
        var vm = this;

        vm.extandedUser = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ExtandedUser.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
