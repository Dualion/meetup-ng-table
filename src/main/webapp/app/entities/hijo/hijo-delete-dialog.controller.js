(function() {
    'use strict';

    angular
        .module('meetupApp')
        .controller('HijoDeleteController',HijoDeleteController);

    HijoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Hijo'];

    function HijoDeleteController($uibModalInstance, entity, Hijo) {
        var vm = this;

        vm.hijo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Hijo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
