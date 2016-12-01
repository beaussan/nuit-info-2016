(function() {
    'use strict';

    angular
        .module('testJhApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('extanded-user', {
            parent: 'entity',
            url: '/extanded-user?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'testJhApp.extandedUser.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/extanded-user/extanded-users.html',
                    controller: 'ExtandedUserController',
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
                    $translatePartialLoader.addPart('extandedUser');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('extanded-user-detail', {
            parent: 'entity',
            url: '/extanded-user/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'testJhApp.extandedUser.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/extanded-user/extanded-user-detail.html',
                    controller: 'ExtandedUserDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('extandedUser');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ExtandedUser', function($stateParams, ExtandedUser) {
                    return ExtandedUser.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'extanded-user',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('extanded-user-detail.edit', {
            parent: 'extanded-user-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/extanded-user/extanded-user-dialog.html',
                    controller: 'ExtandedUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ExtandedUser', function(ExtandedUser) {
                            return ExtandedUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('extanded-user.new', {
            parent: 'extanded-user',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/extanded-user/extanded-user-dialog.html',
                    controller: 'ExtandedUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('extanded-user', null, { reload: 'extanded-user' });
                }, function() {
                    $state.go('extanded-user');
                });
            }]
        })
        .state('extanded-user.edit', {
            parent: 'extanded-user',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/extanded-user/extanded-user-dialog.html',
                    controller: 'ExtandedUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ExtandedUser', function(ExtandedUser) {
                            return ExtandedUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('extanded-user', null, { reload: 'extanded-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('extanded-user.delete', {
            parent: 'extanded-user',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/extanded-user/extanded-user-delete-dialog.html',
                    controller: 'ExtandedUserDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ExtandedUser', function(ExtandedUser) {
                            return ExtandedUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('extanded-user', null, { reload: 'extanded-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
