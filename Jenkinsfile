pipeline {
    agent any
    environment {
        SERVICE_NAME = "" 
    }
    stages {
        stage('Detect Changes') {
            steps {
                script {
                    def changedFiles = sh(script: 'git diff --name-only HEAD~1', returnStdout: true).trim().split("\n")
                    echo "Changed files: ${changedFiles}"

                    for (file in changedFiles) {
                        if (file.startsWith("spring-petclinic-") && file.split("/").size() > 1) {
                            SERVICE_NAME = file.split("/")[0]
                            break
                        }
                    }

                    if (SERVICE_NAME == "") {
                        echo "No relevant service changes detected. Skipping pipeline."
                        currentBuild.result = 'SUCCESS'
                        return
                    }

                    echo "Service to build: ${SERVICE_NAME}"
                }
            }
        }

        stage('Test') {
            when {
                expression { return SERVICE_NAME != "" }
            }
            steps {
                dir("${SERVICE_NAME}") {
                    sh 'mvn clean test'
                    junit 'target/surefire-reports/*.xml' // Upload test results
                }
            }
        }

        stage('Build') {
            when {
                expression { return SERVICE_NAME != "" }
            }
            steps {
                dir("${SERVICE_NAME}") {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
    }
}
