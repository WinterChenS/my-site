#!/bin/bash

if [ X$1 = X ]; then
        read -p "请输入镜像版本号(按回车默认latest)：" version
else
        version=$1
fi

if [ X$version = X ]; then
version=latest
fi

echo -e "\n"
echo "------------------------"
echo "镜像版本为：$version"
echo "------------------------"


mvn -DskipTests=true package docker:build &&

docker tag winterchen/my-site:latest winterchen/my-site:$version &&

docker push winterchen/my-site:$version
echo "[上传完成]"
