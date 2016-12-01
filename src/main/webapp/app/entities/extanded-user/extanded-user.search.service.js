(function() {
    'use strict';

    angular
        .module('testJhApp')
        .factory('ExtandedUserSearch', ExtandedUserSearch);

    ExtandedUserSearch.$inject = ['$resource'];

    function ExtandedUserSearch($resource) {
        var resourceUrl =  'api/_search/extanded-users/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
