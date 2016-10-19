(function() {
    'use strict';
    angular
        .module('meetupApp')
        .factory('Hijo', Hijo);

    Hijo.$inject = ['$resource'];

    function Hijo ($resource) {
        var resourceUrl =  'api/hijos/:id';

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
