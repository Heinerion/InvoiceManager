pipeline {
    agent any

    options {
        timestamps()
    }

    stages {
        stage('Build') {
            steps {
                withMaven {
                    sh 'mvn clean compile'
                }
            }
        }
        stage('Test') {
            steps {
                withMaven {
                    sh 'mv test'
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        stage('Install') {
            steps {
                withMaven {
                    sh 'mv install'
                }
            }
        }
    }
}