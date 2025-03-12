pipeline {
    agent any

    environment {
        GITHUB_TOKEN = credentials('github-token')
    }

    stages {
        stage('Detect Changes') {
            steps {
                script {
                    def affectedServices = [] // Danh sách các service bị thay đổi
                    def changedFiles = sh(script: 'git diff --name-only HEAD~1', returnStdout: true).trim().split("\n")
                    echo "Changed files: ${changedFiles}"

                    for (file in changedFiles) {
                        if (file.startsWith("spring-petclinic-") && file.split("/").size() > 1) {
                            def service = file.split("/")[0]
                            if (!affectedServices.contains(service)) {
                                affectedServices << service
                            }
                        }
                    }

                    if (affectedServices.isEmpty()) {
                        echo "No relevant service changes detected. Skipping pipeline."
                        currentBuild.result = 'SUCCESS'
                        return
                    }

                    echo "Affected services: ${affectedServices}"
                    env.AFFECTED_SERVICES = affectedServices.join(',') // Lưu danh sách thành chuỗi cho các stage sau
                }
            }
        }

        stage('Test and Coverage') {
            when {
                expression { return env.AFFECTED_SERVICES != null && env.AFFECTED_SERVICES != "" }
            }
            steps {
                script {
                    def affectedServices = env.AFFECTED_SERVICES.split(',')
                    for (service in affectedServices) {
                        echo "Testing service: ${service} on ${env.NODE_NAME}"
                        dir(service) {
                            // Chạy test với JaCoCo
                            sh 'mvn clean test'
                            // Tạo báo cáo JaCoCo
                            sh 'mvn jacoco:report'
                        }
                    }
                }
            }
            post {
                always {
                    // Báo cáo kết quả test
                    junit '**/target/surefire-reports/*.xml'
                    // Phân tích và kiểm tra coverage với ngưỡng 70%
                    jacoco(
                        execPattern: '**/target/jacoco.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java',
                        exclusionPattern: '**/src/test/**',
                        minimumLineCoverage: '70', // Yêu cầu tối thiểu 70% line coverage
                        changeBuildStatus: true // Thất bại nếu không đạt ngưỡng
                    )
                }
            }
        }

        stage('Build') {
            when {
                expression { return env.AFFECTED_SERVICES != null && env.AFFECTED_SERVICES != "" }
            }
            steps {
                script {
                    def affectedServices = env.AFFECTED_SERVICES.split(',')
                    for (service in affectedServices) {
                        echo "Building service: ${service} on ${env.NODE_NAME}"
                        dir(service) {
                            sh 'mvn clean package -DskipTests'
                        }
                    }
                }
            }
        }
    }
}
