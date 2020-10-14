#!/usr/bin/env bash
./gradlew build jibDockerBuild
docker tag krzpob/xenia-api registry.heroku.com/xenia-agile-ng-api/web
docker push registry.heroku.com/xenia-agile-ng-api/web
heroku container:release -a xenia-agile-ng-api web

