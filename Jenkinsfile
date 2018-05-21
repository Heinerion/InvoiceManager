pipeline {
    agent any

    options {
        timestamps()
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
                junit 'target/surefire-reports/**/*.xml'
            }
        }
    }
}