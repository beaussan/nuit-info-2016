(function() {
    'use strict';

    angular
        .module('testJhApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('directory', {
            parent: 'entity',
            url: '/directory?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'testJhApp.directory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/directory/directories.html',
                    controller: 'DirectoryController',
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
                    $translatePartialLoader.addPart('directory');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('directory-detail', {
            parent: 'entity',
            url: '/directory/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'testJhApp.directory.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/directory/directory-detail.html',
                    controller: 'DirectoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('directory');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Directory', function($stateParams, Directory) {
                    return Directory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'directory',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('directory-detail.edit', {
            parent: 'directory-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/directory/directory-dialog.html',
                    controller: 'DirectoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Directory', function(Directory) {
                            return Directory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('directory.new', {
            parent: 'directory',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/directory/directory-dialog.html',
                    controller: 'DirectoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                link: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('directory', null, { reload: 'directory' });
                }, function() {
                    $state.go('directory');
                });
            }]
        })
        .state('directory.edit', {
            parent: 'directory',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/directory/directory-dialog.html',
                    controller: 'DirectoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Directory', function(Directory) {
                            return Directory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('directory', null, { reload: 'directory' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('directory.delete', {
            parent: 'directory',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/directory/directory-delete-dialog.html',
                    controller: 'DirectoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Directory', function(Directory) {
                            return Directory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('directory', null, { reload: 'directory' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
