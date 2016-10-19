(function() {
    'use strict';

    angular
        .module('meetupApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('hijo', {
            parent: 'entity',
            url: '/hijo?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'meetupApp.hijo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hijo/hijos.html',
                    controller: 'HijoController',
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
                    $translatePartialLoader.addPart('hijo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('hijo-detail', {
            parent: 'entity',
            url: '/hijo/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'meetupApp.hijo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hijo/hijo-detail.html',
                    controller: 'HijoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hijo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Hijo', function($stateParams, Hijo) {
                    return Hijo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'hijo',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('hijo-detail.edit', {
            parent: 'hijo-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hijo/hijo-dialog.html',
                    controller: 'HijoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Hijo', function(Hijo) {
                            return Hijo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hijo.new', {
            parent: 'hijo',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hijo/hijo-dialog.html',
                    controller: 'HijoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                apellidos: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('hijo', null, { reload: 'hijo' });
                }, function() {
                    $state.go('hijo');
                });
            }]
        })
        .state('hijo.edit', {
            parent: 'hijo',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hijo/hijo-dialog.html',
                    controller: 'HijoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Hijo', function(Hijo) {
                            return Hijo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hijo', null, { reload: 'hijo' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hijo.delete', {
            parent: 'hijo',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hijo/hijo-delete-dialog.html',
                    controller: 'HijoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Hijo', function(Hijo) {
                            return Hijo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hijo', null, { reload: 'hijo' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
