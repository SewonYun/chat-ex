#!/bin/bash

# 하위 디렉토리 목록
# directories=("producer" "consumer" "warehouse")
directories=("producer" "consumer")

# 각 디렉토리 빌드
for directory in "${directories[@]}"
do
  echo "Building project in directory: $directory-img"
  cd ./$directory
  ./gradlew build
  docker build -t "localhost:5252/$directory:latest" --no-cache .
  docker push "localhost:5252/$directory:latest"
  cd ../
done

echo "All projects built successfully!"

exit
