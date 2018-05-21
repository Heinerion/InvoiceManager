pipeline {
    agent any

    options {
        timestamps()
    }

    stages {
        stage('Build') {
            git url: 'https://github.com/Heinerion/InvoiceManager/'

            withMaven(maven: 'mvn') {

              // Run the maven build
              sh "mvn clean install"

            }
        }
    }
}