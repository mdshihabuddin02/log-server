@Library('lib2') _

pipeline {
    agent any
        tools {
            maven 'mvn3'
            jdk 'jdk11'
        }

    environment {
                DOCKER_REGISTRY = 'https://index.docker.io/v1/'
                DOCKER_IMAGE_NAME = 'mdshihabuddin/locallibrary2'
                DOCKER_IMAGE_TAG = 'latest'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', credentialsId: 'git-credential', url: 'https://github.com/mdshihabuddin02/log-server.git'
            }
        }


        stage('Maven Build') {
            steps {
                buildMyMaven()
            }
        }

        // stage('AV Scan') {
        //     steps {
        //             AVScan()
        //     }
        // }

        stage('SonarQube Scan') {
            steps {
                sonarScanMaven(installationName:'sona1', tool:'SonarScanner', projectName:'pro-spring05')
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def imageTag = "${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
                    dockerImage = docker.build(imageTag, '-f Dockerfile .')
                }
            }
        }

        stage('Trivy Scan') {
            steps {
                script {
                    trivyScan(path:'/home/shi/tools/trivy1/trivy', image:DOCKER_IMAGE_NAME, tag:DOCKER_IMAGE_TAG)
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                        withDockerRegistry(credentialsId: 'docker-cred') {
                            dockerImage.push(DOCKER_IMAGE_TAG)
                        }
                }
            }
        }

    }
}
