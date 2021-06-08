#!/usr/bin/env groovy

node {
    def JAVA_HOME = '/opt/java/jdk-11.0.4'

    withEnv(["PATH=${JAVA_HOME}/bin:${PATH}"]) {
        stage('set environment variables') {
            echo "$JAVA_HOME"
            echo "$PATH"
        }

        stage('check environment variables') {
            echo "Path variable: $PATH"
            echo "Java Home variable: $JAVA_HOME"
            sh 'java -version'
        }

        stage('checkout project') {
            checkout scm
        }

        stage('clean project') {
            sh "chmod +x mvnw"
            sh "./mvnw -s /opt/maven/mvn3/conf/settings.xml clean"
        }

        stage('install node and npm tools') {
            sh "./mvnw -s /opt/maven/mvn3/conf/settings.xml com.github.eirslett:frontend-maven-plugin:install-node-and-npm -DnodeVersion=v14.15.1 -DnpmVersion=6.14.8"
        }


        stage('run all tests') {
            sh "./mvnw -s /opt/maven/mvn3/conf/settings.xml verify"
        }

        stage('run backend tests only') {
            try {
                sh "./mvnw -s /opt/maven/mvn3/conf/settings.xml test"
            } catch(err) {
                throw err
//            } finally {
//                junit '**/target/surefire-reports/TEST-*.xml'
            }
        }

        stage('run frontend tests only') {
            try {
                sh "./mvnw -s /opt/maven/mvn3/conf/settings.xml com.github.eirslett:frontend-maven-plugin:npm -Dfrontend.npm.arguments='run test'"
            } catch(err) {
                throw err
//            } finally {
//                junit '**/target/test-results/TESTS-*.xml'
            }
        }

        stage('build and package project') {
            sh "./mvnw -s /opt/maven/mvn3/conf/settings.xml clean verify -Pprod -DskipTests"
            //archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
        }

        stage('sonarqube quality analysis') {
            sh "cp /var/lib/jenkins/sonar_scripts/homeinv_sonar_dev.bash ."
            sh "./homeinv_sonar_dev.bash"
        }

        stage('build docker image') {
          sh "cp /root/HomeInventory/Dockerfile ./target"
          sh "docker build ./target"
        }

        stage('restart containers') {
            sh "docker-compose -f /root/HomeInventory/docker-compose.yml down"
            sh "docker-compose -f /root/HomeInventory/docker-compose.yml up -d"
        }
    }
}

