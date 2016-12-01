'use strict';

describe('Controller Tests', function() {

    describe('ExtandedUser Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockExtandedUser, MockUser, MockConversation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockExtandedUser = jasmine.createSpy('MockExtandedUser');
            MockUser = jasmine.createSpy('MockUser');
            MockConversation = jasmine.createSpy('MockConversation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ExtandedUser': MockExtandedUser,
                'User': MockUser,
                'Conversation': MockConversation
            };
            createController = function() {
                $injector.get('$controller')("ExtandedUserDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'testJhApp:extandedUserUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
