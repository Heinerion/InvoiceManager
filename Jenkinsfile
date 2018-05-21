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
                echo 'This is a minimal pipeline.'
            }
        }
    }
}