(function() {
    'use strict';
    angular
        .module('testJhApp')
        .factory('ContactRequest', ContactRequest);

    ContactRequest.$inject = ['$resource', 'DateUtils'];

    function ContactRequest ($resource, DateUtils) {
        var resourceUrl =  'api/contact-requests/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateAccepted = DateUtils.convertDateTimeFromServer(data.dateAccepted);
                        data.dateAsked = DateUtils.convertDateTimeFromServer(data.dateAsked);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
