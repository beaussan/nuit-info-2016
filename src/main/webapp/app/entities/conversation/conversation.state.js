(function() {
    'use strict';

    angular
        .module('testJhApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('conversation', {
            parent: 'entity',
            url: '/conversation?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'testJhApp.conversation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/conversation/conversations.html',
                    controller: 'ConversationController',
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
                    $translatePartialLoader.addPart('conversation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('conversation-detail', {
            parent: 'entity',
            url: '/conversation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'testJhApp.conversation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/conversation/conversation-detail.html',
                    controller: 'ConversationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('conversation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Conversation', function($stateParams, Conversation) {
                    return Conversation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'conversation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('conversation-detail.edit', {
            parent: 'conversation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/conversation/conversation-dialog.html',
                    controller: 'ConversationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Conversation', function(Conversation) {
                            return Conversation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('conversation.new', {
            parent: 'conversation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/conversation/conversation-dialog.html',
                    controller: 'ConversationDialogController',
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
                    $state.go('conversation', null, { reload: 'conversation' });
                }, function() {
                    $state.go('conversation');
                });
            }]
        })
        .state('conversation.edit', {
            parent: 'conversation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/conversation/conversation-dialog.html',
                    controller: 'ConversationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Conversation', function(Conversation) {
                            return Conversation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('conversation', null, { reload: 'conversation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('conversation.delete', {
            parent: 'conversation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/conversation/conversation-delete-dialog.html',
                    controller: 'ConversationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Conversation', function(Conversation) {
                            return Conversation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('conversation', null, { reload: 'conversation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
