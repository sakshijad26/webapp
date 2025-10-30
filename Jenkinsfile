pipeline {
    agent any

    stages {
        stage('Declarative: Checkout SCM') {
            steps {
                git branch: 'develop', url: 'https://github.com/sakshijad26/webapp.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn -B -DskipTests clean package'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Sonar-Report') {
            steps {
                script {
                    // Check if SonarQube server is running
                    def sonarRunning = bat(returnStatus: true, script: 'curl -s http://localhost:9000 >nul 2>&1') == 0

                    if (sonarRunning) {
                        echo '✅ SonarQube is running — generating report...'
                        bat 'mvn clean install sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.analysis.mode=publish'
                    } else {
                        echo '⚠️ SonarQube server not running. Skipping analysis.'
                    }
                }
            }
        }
    }

    post {
        success {
            echo '🎉 Build, Test, and Sonar Analysis completed successfully!'
        }
        failure {
            echo '❌ Build failed — check console output for details.'
        }
    }
}
