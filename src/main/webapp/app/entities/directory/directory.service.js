(function() {
    'use strict';
    angular
        .module('testJhApp')
        .factory('Directory', Directory);

    Directory.$inject = ['$resource'];

    function Directory ($resource) {
        var resourceUrl =  'api/directories/:id';

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
