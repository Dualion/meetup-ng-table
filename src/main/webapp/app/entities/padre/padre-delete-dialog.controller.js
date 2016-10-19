(function() {
    'use strict';

    angular
        .module('meetupApp')
        .controller('PadreDeleteController',PadreDeleteController);

    PadreDeleteController.$inject = ['$uibModalInstance', 'entity', 'Padre'];

    function PadreDeleteController($uibModalInstance, entity, Padre) {
        var vm = this;

        vm.padre = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Padre.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
