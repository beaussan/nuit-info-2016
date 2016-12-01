(function() {
    'use strict';

    angular
        .module('testJhApp')
        .factory('DirectorySearch', DirectorySearch);

    DirectorySearch.$inject = ['$resource'];

    function DirectorySearch($resource) {
        var resourceUrl =  'api/_search/directories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
