pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "arunkumar1611/devops-demo-app"
        SONAR_SERVER = "sonar-server"
    }

    tools {
        maven 'Maven3'
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                url: 'https://github.com/Arunkumar-U/devops-demo-app.git'
            }
        }

        stage('Build Application') {
            steps {
                sh "mvn clean package -DskipTests"
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                    withSonarQubeEnv("${SONAR_SERVER}") {
                        sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=springboot-demo \
                        -Dsonar.login=${SONAR_TOKEN}
                        """
                    }
                }
            }
        }

        stage('Trivy File System Scan') {
            steps {
                sh """
                docker run --rm \
                -v \$PWD:/app \
                aquasec/trivy fs /app
                """
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE}:${BUILD_NUMBER} ."
            }
        }

        stage('Trivy Image Scan') {
            steps {
                sh """
                docker run --rm \
                -v /var/run/docker.sock:/var/run/docker.sock \
                aquasec/trivy image ${DOCKER_IMAGE}:${BUILD_NUMBER}
                """
            }
        }

        stage('Docker Login & Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                        echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin
                        docker push ${DOCKER_IMAGE}:${BUILD_NUMBER}
                        docker logout
                    """
                }
            }
        }
    }

    post {

        success {
            emailext(
                subject: "SUCCESS: ${JOB_NAME} - Build #${BUILD_NUMBER}",
                body: """
                ✅ Build Successful

                Project: ${JOB_NAME}
                Build Number: ${BUILD_NUMBER}
                Docker Image: ${DOCKER_IMAGE}:${BUILD_NUMBER}

                Good job Arunkumar 👌
                """,
                to: "yourmail@gmail.com"
            )
        }

        failure {
            emailext(
                subject: "FAILED: ${JOB_NAME} - Build #${BUILD_NUMBER}",
                body: """
                ❌ Build Failed

                Project: ${JOB_NAME}
                Build Number: ${BUILD_NUMBER}

                Please check Jenkins logs.
                """,
                to: "yourmail@gmail.com"
            )
        }
    }
}



