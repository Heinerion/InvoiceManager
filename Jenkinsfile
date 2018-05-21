#!groovy

pipeline {
    agent any

    options {
        timestamps()
        skipStagesAfterUnstable()
    }

    tools {
        maven 'mvn'
    }

    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }

        stage('Integrate') {
            steps {
                sh 'mvn verify'
            }
            post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }

        stage('SonarQube') {
            steps {
                sh 'mvn sonar:sonar'
            }
        }

        stage('Install') {
            steps {
                sh 'mvn install'
            }
        }
    }
}