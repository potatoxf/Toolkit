@echo off
setlocal enabledelayedexpansion
set comment=%1
set branch=%2
set remote=%3
if "!comment!" == "" (
    set comment=clear history and caches
)
if "!branch!" == "" (
    set branch=master
)
if "!remote!" == "" (
    set remote=origin
)
git checkout --orphan new
git add -A
git commit -sm "!comment!"
git branch -D !branch!
git branch -m !branch!
git reflog expire --expire=now --all
git gc
git push !remote! !branch! -f
pause