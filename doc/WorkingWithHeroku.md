# Quick start guide

1. It's assumed that you have access to heroku.
1. Download and install [Heroku Command Line](https://devcenter.heroku.com/articles/heroku-command-line)
1. Start new shell, `heroku` command has been added to path
1. After running `heroku` command it will continue installation and configuration (here in windows shell, cuz in cygwin it won't work):
```C:\Users\W541>heroku
   heroku-cli: Installing CLI... 17.56MB/17.56MB
   Enter your Heroku credentials.
   Email: pawel.pawel@gmail.com
   Password (typing will be hidden):
   Logged in as pawel.pawel@gmail.com
    !    Add apps to this dashboard by favoriting them with heroku
    !    apps:favorites:add
   See all add-ons with heroku addons
   See all apps with heroku apps --all
   
   See other CLI commands with heroku help
   
   
   C:\Users\W541>
```
1. Log in:
```
C:\Users\W541>heroku login
Enter your Heroku credentials.
Email: pawel.pawel@gmail.com
Password (typing will be hidden):
Logged in as pawel.pawel@gmail.com
```
1. TODO - Well, better than providing raw password goto [account page](https://dashboard.heroku.com/account) and setup your ssh keys. After that do `heroku logout`. Now heroku should use your ssh keys in order to authorise (in windows it doesn't work ad hoc ... )
1. Run example command: List list dynos for an app:
```
C:\Users\W541>heroku ps --app testinator-pro
Free dyno hours quota remaining this month: 550h 0m (100%)
For more information on dyno sleeping and how to upgrade, see:
https://devcenter.heroku.com/articles/dyno-sleeping

=== web (Free): target/universal/stage/bin/akka-http-microservice -Dhttp.port=$PORT (1)
web.1: up 2016/10/13 15:05:58 +0200 (~ 4m ago)
```

# Useful commands & tips

## general
- `heroku info --app testinator-pro` call it from particular folder
- `heroku releases --app testinator-pro`
- `heroku releases:rollback v21 --app testinator-pro`
- `heroku run bash --app testinator-pro` - spin up a new [one-off dyno](https://devcenter.heroku.com/articles/one-off-dynos) with attached i/o to console. After closing connection changes to filesystem will not be propagated!
- `heroku logs --app testinator-pro --tail`
- `heroku logs --app testinator-pro --ps web.1 --tail`
- `heroku config` lists config

## pipelines
- [heroku-pipelines](https://github.com/heroku/heroku-pipelines) 
- `heroku pipelines:info`
- `heroku pipelines:info testinator-pipeline` 

## Getting Started on Heroku with Scala and Play
- `git clone https://github.com/heroku/scala-getting-started.git`
- `heroku create`
- `git push heroku master`
- `heroku open`
- `sbt compile stage`
- `heroku local -f Procfile.windows`
- `heroku ps`
- `heroku ps:scale web=0` and refresh page
- `heroku ps:scale web=1`
- in `system.properties` man can define runtime java version
- `sbt compile stage` - local build
- `heroku addons` - list addons 
- `heroku addons:create papertrail` add logging addon
- `heroku config:set ENERGY="30 GeV"`
- `heroku config`
- `heroku pg`
- `heroku pg:sql`


