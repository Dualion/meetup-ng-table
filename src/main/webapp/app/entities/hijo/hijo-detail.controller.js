(function() {
    'use strict';

    angular
        .module('meetupApp')
        .controller('HijoDetailController', HijoDetailController);

    HijoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Hijo', 'Padre'];

    function HijoDetailController($scope, $rootScope, $stateParams, previousState, entity, Hijo, Padre) {
        var vm = this;

        vm.hijo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('meetupApp:hijoUpdate', function(event, result) {
            vm.hijo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
