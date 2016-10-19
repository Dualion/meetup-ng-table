(function() {
    'use strict';

    angular
        .module('meetupApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('padre', {
            parent: 'entity',
            url: '/padre?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'meetupApp.padre.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/padre/padres.html',
                    controller: 'PadreController',
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
                    $translatePartialLoader.addPart('padre');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('padre-detail', {
            parent: 'entity',
            url: '/padre/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'meetupApp.padre.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/padre/padre-detail.html',
                    controller: 'PadreDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('padre');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Padre', function($stateParams, Padre) {
                    return Padre.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'padre',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('padre-detail.edit', {
            parent: 'padre-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/padre/padre-dialog.html',
                    controller: 'PadreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Padre', function(Padre) {
                            return Padre.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('padre.new', {
            parent: 'padre',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/padre/padre-dialog.html',
                    controller: 'PadreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nombre: null,
                                apellidos: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('padre', null, { reload: 'padre' });
                }, function() {
                    $state.go('padre');
                });
            }]
        })
        .state('padre.edit', {
            parent: 'padre',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/padre/padre-dialog.html',
                    controller: 'PadreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Padre', function(Padre) {
                            return Padre.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('padre', null, { reload: 'padre' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('padre.delete', {
            parent: 'padre',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/padre/padre-delete-dialog.html',
                    controller: 'PadreDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Padre', function(Padre) {
                            return Padre.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('padre', null, { reload: 'padre' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
