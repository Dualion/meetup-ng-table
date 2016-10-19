(function() {
    'use strict';

    angular
        .module('meetupApp')
        .controller('PadreDialogController', PadreDialogController);

    PadreDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Padre', 'Hijo'];

    function PadreDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Padre, Hijo) {
        var vm = this;

        vm.padre = entity;
        vm.clear = clear;
        vm.save = save;
        vm.hijos = Hijo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.padre.id !== null) {
                Padre.update(vm.padre, onSaveSuccess, onSaveError);
            } else {
                Padre.save(vm.padre, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('meetupApp:padreUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
