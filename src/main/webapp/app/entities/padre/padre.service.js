(function() {
    'use strict';
    angular
        .module('meetupApp')
        .factory('Padre', Padre);

    Padre.$inject = ['$resource'];

    function Padre ($resource) {
        var resourceUrl =  'api/padres/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
