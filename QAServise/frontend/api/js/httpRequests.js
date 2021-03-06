//This class serves all functionalities for our frontend-backend connection

//CHANGE THESE PORTS ACCORDINGLY TOU YOUR SETUP

//TODO what port does the buche.informatik.rwth-aachen.de/ugnm1415g2 have???

//CONFIG
//(Apache runs on 80 native, check your server.xml)
var frontendPort = 80; //Port number of whatever frontend server you use (e.g. Apache, Tomcat, Wildfly,...)!
var backendPort = 8080; //Port number of your local backend(start_network on native on Port 8080) or webserver service port!
var useLocalFrontendServer = true; //If you test on localhost, set true.
//Frontend & Backend Ports differ -> so absolute url is required!
var localFrontendURL = "http://buche.informatik.rwth-aachen.de:" + backendPort + "/";
//var localFrontendURL = "http://localhost:" + backendPort + "/";

function isAnonymous() {
  if(localStorage.getItem("access_token") === null)
    return true;
  else
    return false;
}


function getMyFavorites($http, callBack) {
  if(isAnonymous())
    alert('You have to be loged in!');
  else {
    var mUrl = localFrontendURL + "api/user/favouriteQuestions?access_token=" + localStorage.access_token;
    $http
      ({
        method: "GET", 
        url: mUrl, 
        headers: {'Content-Type': 'application/json'}
      }).then(function(result) {
        callBack(result.data);
      });
  }
}

//GET
function allQuestions($http, setQuestionsCallback) {

  var buildUrl;
  if(localStorage.getItem("access_token") === null)
    buildUrl = "api/questions";
  else
    buildUrl = "api/questions?access_token=" + localStorage.access_token;
  if(useLocalFrontendServer)
      buildUrl = localFrontendURL + buildUrl;
  $http({method: "GET", url: buildUrl, headers: {
		    'Content-Type': 'application/json'}})
		      .then(function(result) {
		          setQuestionsCallback(result.data);
		      });
}

function hashtagCollection($http) {
  var buildUrl = "api/hashtag";
  if(useLocalFrontendServer)
      buildUrl = localFrontendURL + buildUrl;
  return $http({method: "GET", url: buildUrl, headers: {
        'Content-Type': 'application/json'}})
          .then(function(result) {
               return result.data;
          });
}


function getUser($http, setUserCallback) {
  var buildUrl = "api/user?access_token="+ localStorage.access_token;
  if(useLocalFrontendServer)
      buildUrl = localFrontendURL + buildUrl;
  $http({method: "GET", url: buildUrl, headers: {
        'Content-Type': 'application/json'}})
          .then(function(result) {
              setUserCallback(result.data);
          });
}

function getUserExpertises($http, cb) {
  var buildUrl = "api/user?access_token="+ localStorage.access_token;
  if(useLocalFrontendServer)
      buildUrl = localFrontendURL + buildUrl;
   $http({method: "GET", url: buildUrl, headers: {
        'Content-Type': 'application/json'}})
          .then(function(result) {
              cb(result.data.hashtags);
          });
}

function getAnswers($http, questionId, cb) {
  var buildUrl = "api/answers/question/" + questionId;
  if(useLocalFrontendServer)
      buildUrl = localFrontendURL + buildUrl;
  return $http({method: "GET", url: buildUrl, headers: {
        'Content-Type': 'application/json'}})
          .then(function(result) {
              cb(result.data);
          });

}

//POST
function askQuestion($http, question, redirectCallback) {
  var buildUrl = "api/question?access_token="+ localStorage.access_token;
  if(useLocalFrontendServer)
      buildUrl = localFrontendURL + buildUrl;
  $http({method: "POST", url: buildUrl, headers: {
        'Content-Type': 'application/json'}, data : {text:question.text, hashtags:question.hashtags}})
        .then(function(result) {
          redirectCallback(result.data);
        });
}

function answerQuestion($http, answer, reloadCB) {
  var buildUrl = "api/answers/question/" + answer.idPost + "?access_token="+ localStorage.access_token;
  if(useLocalFrontendServer)
      buildUrl = localFrontendURL + buildUrl;
  $http({method: "POST", url: buildUrl, headers: {
        'Content-Type': 'application/json'}, data : {text:answer.text}})
        .then(function(result) {
          reloadCB();
        });
}

function upvoteAnswer($http, answerId, refreshCB) {
  var buildUrl = "api/user/upvote/" + answerId + "?access_token="+ localStorage.access_token;
  if(useLocalFrontendServer)
      buildUrl = localFrontendURL + buildUrl;
  $http({method: "POST", url: buildUrl, headers: {
        'Content-Type': 'application/json'}})
        .error(function(data, status, headers, config) {
          var errorMsg = JSON.stringify(data);
          if(errorMsg.indexOf("already") > -1) {
            alert("You already upvoted this answer");
          } else if(errorMsg.indexOf("User can't upvote") > -1) {
            alert("You can not upvote your own answer");
          }
        })
        .then(function(response) {
          refreshCB();
        });
}

function favourQuestion($http, questionId, cb) {
  var buildUrl = "api/user/bookmark/" + questionId + "?access_token="+ localStorage.access_token;
  if(useLocalFrontendServer)
    buildUrl = localFrontendURL + buildUrl;
  $http({method: "POST", url: buildUrl, headers: {
        'Content-Type': 'application/json'}})
        .error(function(data, status, headers, config) {
          alert( JSON.stringify(data));
        })
        .then(function(result) {
          cb();
        });
}

//PUT
function updateUser($http, hashtags) {
  var buildUrl = "api/user?access_token="+ localStorage.access_token;
  if(useLocalFrontendServer)
      buildUrl = localFrontendURL + buildUrl;
  $http({method: "PUT", url: buildUrl, headers: {
        'Content-Type': 'application/json'}, data : {hashtags: hashtags}}).success(
          function() {
            alert("Succesfull");
          });
}
