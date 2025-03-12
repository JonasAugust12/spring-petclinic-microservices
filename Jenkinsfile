pipeline {
    agent any

    environment {
        GITHUB_TOKEN = credentials('github-token')
    }

    stages {
        stage('Detect Changes') {
            steps {
                script {
                    def SERVICE_NAME = ""
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
                    
                    env.SERVICE_NAME = SERVICE_NAME
                }
            }
        }

        stage('Test') {
            when {
                expression { return env.SERVICE_NAME != null && env.SERVICE_NAME != "" }
            }
            steps {
                dir("${env.SERVICE_NAME}") {
                    sh 'mvn clean test'
                    junit 'target/surefire-reports/*.xml' 
                }
            }
        }

        stage('Build') {
            when {
                expression { return env.SERVICE_NAME != null && env.SERVICE_NAME != "" }
            }
            steps {
                dir("${env.SERVICE_NAME}") {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
    }
}
