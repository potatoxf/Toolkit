@echo off
setlocal enabledelayedexpansion
set local_branch=%1
set remote_branch=%2
set remote=%3
if "!local_branch!" == "" (
    set local_branch=master
)
if "!remote_branch!" == "" (
    set remote_branch=master
)
if "!remote!" == "" (
    set remote=origin
)
git commit -a --amend -C head
git push !remote! !local_branch!:!remote_branch! -f