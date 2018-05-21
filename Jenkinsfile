pipeline {
    agent any

    options {
        timestamps()
    }

    stages {
        stage('Build') {
            withMaven {
                sh 'mvn clean compile'
            }
        }
        stage('Test') {
            withMaven {
                sh 'mv test'
                junit '**/target/surefire-reports/*.xml'
            }
        }
        stage('Install') {
            withMaven {
                sh 'mv install'
            }
        }
    }
}