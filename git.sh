remoteOrigin="https://github.com/qwertycody/Simple_Selenium.git"

rm -Rf ./target

if [ "$1" == "push" ]; then
    git add *
    git remote add origin "$remoteOrigin"
    git commit -m "Automated Push"
    git push --force -u origin master    
fi

if [ "$1" == "pull" ]; then
    git remote add origin "$remoteOrigin"
    git pull origin master
fi

if [ "$1" == "setup" ]; then
    git init
    git remote add origin "$remoteOrigin"
    git pull origin master
fi
