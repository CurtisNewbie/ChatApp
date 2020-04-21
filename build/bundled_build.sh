echo '>>>> Building Angular app <<<<'
(cd ../chatappfront/; ng build --prod)

echo '>>>> Moving Angular build files to Quarkus <<<<'
(cp ../chatappfront/dist/chatappfront/* ../chatapp/src/main/resources/META-INF/resources/)

echo '>>>> Building Quarkus app <<<<'
mvn -f ../chatapp/ clean package

echo '>>>> Remove Angular build files <<<<'
rm -rvf ../chatapp/src/main/resources/META-INF/resources/*

echo '>>>> Moving bundled build to root dir <<<<'
mv ../chatapp/target/chatapp-*-runner.jar ./
