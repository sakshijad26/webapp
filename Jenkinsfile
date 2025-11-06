pipeline {
    agent any

    environment {
        SONARQUBE_URL = 'http://localhost:9000'
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'develop', url: 'https://github.com/sakshijad26/webapp.git'
            }
        }

        stage('Build') {
            steps {
                echo '🔧 Building project...'
                bat 'mvn -B -DskipTests clean package'
            }
        }

        stage('Test') {
            steps {
                echo '🧪 Running tests...'
                bat 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    def sonarRunning = bat(returnStatus: true, script: 'curl -s http://localhost:9000 >nul 2>&1') == 0
                    if (sonarRunning) {
                        echo '✅ SonarQube is running — analyzing code...'
                        withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')]) {
                            bat """
                                mvn clean install sonar:sonar ^
                                -Dsonar.host.url=${SONARQUBE_URL} ^
                                -Dsonar.token=%SONAR_TOKEN%
                            """
                        }
                    } else {
                        echo '⚠️ SonarQube not running. Skipping analysis.'
                    }
                }
            }
        }

        stage('Publish to Nexus') {
            steps {
                echo '📦 Uploading artifact to Nexus Repository...'
                bat 'mvn deploy -DskipTests'
            }
        }

        stage('Deploy Locally') {
            steps {
                echo '🚀 Deploying locally...'
                bat 'C:\\deployment\\deploy-webapp.bat'
            }
        }
    }

    post {
        success {
            echo '🎉 Build, Sonar, Nexus upload, and local deploy completed successfully!'
        }
        failure {
            echo '❌ Build failed — check console for details.'
        }
    }
}
