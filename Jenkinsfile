pipeline {
    agent any

    options {
        timestamps()
    }

    stages {
        stage('Build') {
            step {
                withMaven {
                    sh 'mvn clean compile'
                }
            }
        }
        stage('Test') {
            step {
                withMaven {
                    sh 'mv test'
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        stage('Install') {
            step {
                withMaven {
                    sh 'mv install'
                }
            }
        }
    }
}