(function() {
    'use strict';

    angular
        .module('meetupApp')
        .controller('HijoDialogController', HijoDialogController);

    HijoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Hijo', 'Padre'];

    function HijoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Hijo, Padre) {
        var vm = this;

        vm.hijo = entity;
        vm.clear = clear;
        vm.save = save;
        vm.padres = Padre.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.hijo.id !== null) {
                Hijo.update(vm.hijo, onSaveSuccess, onSaveError);
            } else {
                Hijo.save(vm.hijo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('meetupApp:hijoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
