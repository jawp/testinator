# The testinator PRO

## Build status

| Branche  |      Status |
|----------|-------------|
| master   |  [![Build Status](https://travis-ci.org/jawp/testinator.svg?branch=master)](https://travis-ci.org/jawp/testinator) |
| development|[![Build Status](https://travis-ci.org/jawp/testinator.svg?branch=development)](https://travis-ci.org/jawp/testinator)  |


## Heroku pipelines

**Simple pipeline:**

*development* branch is for devs, every change is deployed automatically to UAT.

*master* contains production codebase. Merge development to master to promote to production.

**PROD:**

master branch auto-deployed to:

https://testinator-pro.herokuapp.com

**UAT:**

development branch auto-deployed to:

http://testinator-pro-uat.herokuapp.com
