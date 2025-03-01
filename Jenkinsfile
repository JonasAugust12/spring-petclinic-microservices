pipeline {
    agent any
    stages {
        stage('Clone Repository') {
            steps {
                checkout scm
            }
        }
        stage('Print Message') {
            steps {
                echo 'Jenkins trigger test successful!'
            }
        }
    }
}
