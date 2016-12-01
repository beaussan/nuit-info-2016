(function() {
    'use strict';

    angular
        .module('testJhApp')
        .factory('ContactRequestSearch', ContactRequestSearch);

    ContactRequestSearch.$inject = ['$resource'];

    function ContactRequestSearch($resource) {
        var resourceUrl =  'api/_search/contact-requests/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
