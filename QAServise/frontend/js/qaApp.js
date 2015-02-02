var questionAnswerApp = angular.module('qaApp', ['ngRoute', 'ngTagsInput']);

questionAnswerApp.config(function($routeProvider) {
    // configure the routing rules here
    $routeProvider
        .when('/dashboard', {
            // note: manage.html is renamed to dashboard.html,
            //       the old dashboard is deprecated and will be removed
            templateUrl: 'dashboard.html',
            controller: 'DashboardCtrl'
        })
        .when('/preferences', {
            templateUrl: 'preferences.html',
            controller: 'PreferencesCtrl'
        })
        .when('/question', {
            templateUrl: 'askquestion.html',
            controller: 'AskquestionCtrl'
        })
        .when('/answers/:questionId?', {
            templateUrl: 'answers.html',
            controller: 'AnswersCtrl'
        })
        .when('/:accessToken?', {
            templateUrl: 'dashboard.html',
            controller: 'DashboardCtrl'
        })
        ;


    //routing DOESN'T work without html5Mode
    //$tagsProvider.html5Mode(true);
});

questionAnswerApp.controller('DashboardCtrl', function($rootScope, $scope, $routeParams, $route, $http) {

    $rootScope.page = {
        title: "Dashboard",
        dashboardActive: "active",
        preferencesActive: "",
        askQuestionActive: ""
    };

    $scope.questions = [{hashtags:[{text:"Java"},{text:"Programmierung"}], favourcount:2,timestamp:"30.01.2014 23:26:14", text:"How to write a for-loop in Java", idPost:1, isFavourite:true},
    {hashtags:[{text:"Javascript"},{text:"AngularJs"}, {text:"Frontend"}, {text:"ng-view"}], favourcount:4,timestamp:"28.01.2014 12:14:14", text:"Javascript AngularJs ng-view Problem", idPost:2, isFavourite:true}];

    //set questions async
    allQuestions($http,function(data) {
        $scope.questions = data;
    });


    //reqiures function
    $scope.allTags = function() {
      return hashtagCollection($http);
    }

    $scope.favour = function(questionId) {
      favourQuestion(questionId);
    }

    $scope.userExpertises = function(field) {
      getUserExpertises($http, function(data) {
        field = data;
      });
    }

    $scope.getQuestionHeadline = function(text) {
      var n = text.indexOf('?');
      var maxLength = 60;
      if(n > 0 && n <= maxLength) {
        return text.substring(0,n);
      } else {
        return text.substring(0,maxLength) + "...";
      }
    }

});

questionAnswerApp.controller('PreferencesCtrl', function($rootScope, $scope, $routeParams, $route, $http) {
    $rootScope.page = {
        title: "Preferences",
        dashboardActive: "",
        preferencesActive: "active",
        askQuestionActive: ""
    };

    //$scope.expertises = {};

    $scope.saveUser = function() {
        updateUser($http, $scope.expertises);
    }

    $scope.allTags = function() {
      return hashtagCollection($http);
    }
});

questionAnswerApp.controller('AnswersCtrl', function($rootScope, $scope, $routeParams, $route, $http) {
  $scope.question = {};
  $scope.answers = {};
  $scope.answer = {};

  getAnswers($http,$routeParams.questionId, function(data) {
    $scope.question = data.question;
    $scope.answers = data.answers;
    $scope.answer.idPost = data.question.idPost;
  });

  $scope.postAnswer = function() {
      answerQuestion($http, $scope.answer, function() {
        $route.reload();
      });
  }

  $scope.favour = function() {
      favourQuestion($http, $scope.question.idPost);
  }

  $scope.like = function(answerId) {
    upvoteAnswer($http, answerId, function() {
      $route.reload();
    });
  }
});
questionAnswerApp.controller('AskquestionCtrl', function($rootScope, $scope, $routeParams, $route, $http) {
  $rootScope.page = {
      title: "Ask",
      dashboardActive: "",
      preferencesActive: "",
      askQuestionActive: "active"
  };
  $scope.question = {};
  $scope.post = function () {
    askQuestion(question, function(questionId) {
        $route.reload();
    });
  }
});

//usefull helper functions here
questionAnswerApp.filter('filterByTags', function() {
    return function(items, tags) {
        var filtered = [];
        if(!tags){
            return filtered;
        }
        if (tags.length == 0) {
            filtered = items;
        }
        for (var i = items.length - 1; i >= 0; i--) {
            for (var j = tags.length - 1; j >= 0; j--) {
                if (items[i].hashtags) {
                    for (var k = items[i].hashtags.length - 1; k >= 0; k--) {
                        if (items[i].hashtags[k].text.toUpperCase().indexOf(tags[j].text.toUpperCase()) > -1) {
                            filtered.push(items[i]);
                        }
                    }
                }
            };
        };
        return filtered;
    };
});

questionAnswerApp.filter('orderObjectBy', function(){
 return function(input, attribute) {
    if (!angular.isObject(input)) return input;

    var array = [];
    for(var objectKey in input) {
        array.push(input[objectKey]);
    }

    array.sort(function(a, b){
        a = parseInt(a[attribute]);
        b = parseInt(b[attribute]);
        return b - a;
    });
    return array;
 }
});
questionAnswerApp.filter('setdate', function($filter) {
    return function(msAgo) {

        var msNow = Date.now();

        var minutes = Math.floor((msNow - msAgo) / 60000);
        var hours = Math.floor(minutes / 60);
        var days = Math.floor(hours / 60);

        t = days > 60
            ? $filter('date')(new Date(msAgo), 'd MMM yyyy') 
            : hours > 23 
                ? $filter('date')(new Date(msAgo), 'd MMM') 
                : minutes > 59 
                    ? hours + ' hours ago' 
                    : minutes + ' minutes ago';

        return t;
    };
});