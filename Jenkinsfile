pipeline {
    agent any

    options {
        timestamps()
    }

    environment {
        //Use Pipeline Utility Steps plugin to read information from pom.xml into env variables
        IMAGE = readMavenPom().getArtifactId()
        VERSION = readMavenPom().getVersion()
      }

      stages {
        stage('Build') {
          agent {
            docker {
              /*
               * Reuse the workspace on the agent defined at top-level of Pipeline but run inside a container.
               * In this case we are running a container with maven so we don't have to install specific versions
               * of maven directly on the agent
               */
              reuseNode true
              image 'maven:3.5.0-jdk-8'
            }
          }
          steps {
            // using the Pipeline Maven plugin we can set maven configuration settings, publish test results, and annotate the Jenkins console
            withMaven(options: [findbugsPublisher(), junitPublisher(ignoreAttachments: false)]) {
              sh 'mvn clean findbugs:findbugs package'
            }
          }
          post {
            success {
              // we only worry about archiving the jar file if the build steps are successful
              archiveArtifacts(artifacts: '**/target/*.jar', allowEmptyArchive: true)
            }
          }
        }

        stage('Quality Analysis') {
          parallel {
            // run Sonar Scan and Integration tests in parallel. This syntax requires Declarative Pipeline 1.2 or higher
            stage ('Integration Test') {
              agent any  //run this stage on any available agent
              steps {
                echo 'Run integration tests here...'
              }
            }
            stage('Sonar Scan') {
              agent {
                docker {
                  // we can use the same image and workspace as we did previously
                  reuseNode true
                  image 'maven:3.5.0-jdk-8'
                }
              }
              environment {
                //use 'sonar' credentials scoped only to this stage
                SONAR = credentials('sonar')
              }
              steps {
                sh 'mvn sonar:sonar -Dsonar.login=$SONAR_PSW'
              }
            }
          }
    }
}