pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "arunkumar1611/demo-app"
    }

    stages {

        stage('Checkout') {
            steps {
                git 'https://github.com/Arunkumar-U/devops-demo-app.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('SonarQube Scan') {
            steps {
                sh 'mvn sonar:sonar'
            }
        }

        stage('Trivy Scan') {
            steps {
                sh 'docker run --rm aquasec/trivy image $DOCKER_IMAGE'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t $DOCKER_IMAGE:latest .'
            }
        }

        stage('Docker Push') {
            steps {
                sh 'docker push $DOCKER_IMAGE:latest'
            }
        }
    }

    post {
        always {
            mail to: 'u.arunkunar@gmail.com',
            subject: "CI Pipeline Result",
            body: "Build Completed"
        }
    }
}