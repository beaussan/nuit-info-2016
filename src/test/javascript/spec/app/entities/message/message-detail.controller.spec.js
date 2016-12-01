'use strict';

describe('Controller Tests', function() {

    describe('Message Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMessage, MockExtandedUser, MockConversation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMessage = jasmine.createSpy('MockMessage');
            MockExtandedUser = jasmine.createSpy('MockExtandedUser');
            MockConversation = jasmine.createSpy('MockConversation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Message': MockMessage,
                'ExtandedUser': MockExtandedUser,
                'Conversation': MockConversation
            };
            createController = function() {
                $injector.get('$controller')("MessageDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'testJhApp:messageUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
