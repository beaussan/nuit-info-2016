(function() {
    'use strict';

    angular
        .module('testJhApp')
        .factory('ConversationSearch', ConversationSearch);

    ConversationSearch.$inject = ['$resource'];

    function ConversationSearch($resource) {
        var resourceUrl =  'api/_search/conversations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
