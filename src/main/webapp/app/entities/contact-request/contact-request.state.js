(function() {
    'use strict';

    angular
        .module('testJhApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('contact-request', {
            parent: 'entity',
            url: '/contact-request?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'testJhApp.contactRequest.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contact-request/contact-requests.html',
                    controller: 'ContactRequestController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contactRequest');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('contact-request-detail', {
            parent: 'entity',
            url: '/contact-request/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'testJhApp.contactRequest.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contact-request/contact-request-detail.html',
                    controller: 'ContactRequestDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contactRequest');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ContactRequest', function($stateParams, ContactRequest) {
                    return ContactRequest.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'contact-request',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('contact-request-detail.edit', {
            parent: 'contact-request-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contact-request/contact-request-dialog.html',
                    controller: 'ContactRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ContactRequest', function(ContactRequest) {
                            return ContactRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contact-request.new', {
            parent: 'contact-request',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contact-request/contact-request-dialog.html',
                    controller: 'ContactRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                isAccepted: null,
                                dateAccepted: null,
                                dateAsked: null,
                                message: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('contact-request', null, { reload: 'contact-request' });
                }, function() {
                    $state.go('contact-request');
                });
            }]
        })
        .state('contact-request.edit', {
            parent: 'contact-request',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contact-request/contact-request-dialog.html',
                    controller: 'ContactRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ContactRequest', function(ContactRequest) {
                            return ContactRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('contact-request', null, { reload: 'contact-request' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contact-request.delete', {
            parent: 'contact-request',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contact-request/contact-request-delete-dialog.html',
                    controller: 'ContactRequestDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ContactRequest', function(ContactRequest) {
                            return ContactRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('contact-request', null, { reload: 'contact-request' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
