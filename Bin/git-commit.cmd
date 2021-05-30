@echo off
setlocal enabledelayedexpansion
set comment=%1
set branch=%2
set remote=%3
if "!comment!" == "" (
    set comment=Default Commit
)
if "!branch!" == "" (
    set branch=master
)
if "!remote!" == "" (
    set remote=origin
)
git add .
git commit -sm "!comment!"
git push !remote! !branch!