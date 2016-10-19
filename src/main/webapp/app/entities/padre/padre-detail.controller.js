(function() {
    'use strict';

    angular
        .module('meetupApp')
        .controller('PadreDetailController', PadreDetailController);

    PadreDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Padre', 'Hijo'];

    function PadreDetailController($scope, $rootScope, $stateParams, previousState, entity, Padre, Hijo) {
        var vm = this;

        vm.padre = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('meetupApp:padreUpdate', function(event, result) {
            vm.padre = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
