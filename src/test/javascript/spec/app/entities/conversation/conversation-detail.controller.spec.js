'use strict';

describe('Controller Tests', function() {

    describe('Conversation Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockConversation, MockMessage, MockExtandedUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockConversation = jasmine.createSpy('MockConversation');
            MockMessage = jasmine.createSpy('MockMessage');
            MockExtandedUser = jasmine.createSpy('MockExtandedUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Conversation': MockConversation,
                'Message': MockMessage,
                'ExtandedUser': MockExtandedUser
            };
            createController = function() {
                $injector.get('$controller')("ConversationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'testJhApp:conversationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
