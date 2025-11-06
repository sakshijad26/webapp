pipeline {
    agent any

    environment {
        SONARQUBE_URL = 'http://localhost:9000'
        SONAR_TOKEN = credentials('sonarqube-token')      // ✅ matches Jenkins credential ID
        NEXUS_URL = 'http://localhost:8081/repository/upes/'
        NEXUS_CRED = credentials('nexus-cred')            // ✅ matches Jenkins credential ID
    }

    stages {

        stage('Checkout Source') {
            steps {
                git branch: 'develop', url: 'https://github.com/sakshijad26/webapp.git'
            }
        }

        stage('Build') {
            steps {
                echo '🔧 Building the project...'
                bat 'mvn -B -DskipTests clean package'
            }
        }

        stage('Run Tests') {
            steps {
                echo '🧪 Running tests...'
                bat 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo '🔍 Running SonarQube code analysis...'
                bat """
                    mvn sonar:sonar ^
                    -Dsonar.projectKey=webapp ^
                    -Dsonar.host.url=${SONARQUBE_URL} ^
                    -Dsonar.login=${SONAR_TOKEN}
                """
            }
        }

        stage('Deploy to Nexus') {
            steps {
                echo '🚀 Deploying artifact to Nexus Repository...'
                bat """
                    mvn deploy -DskipTests ^
                    -DaltDeploymentRepository=upes::default::${NEXUS_URL} ^
                    -Dnexus.username=${NEXUS_CRED_USR} ^
                    -Dnexus.password=${NEXUS_CRED_PSW}
                """
            }
        }
    }

    post {
        success {
            echo '✅ Build, Test, Sonar Analysis, and Nexus Deployment completed successfully!'
        }
        failure {
            echo '❌ Build failed — check console output for details.'
        }
    }
}
