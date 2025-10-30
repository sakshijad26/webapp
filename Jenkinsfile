pipeline {
    agent { label 'Slave-01' }

    stages {
        stage('Checkout') {
            steps {
                git branch: env.BRANCH_NAME, url: 'https://github.com/sakshijad26/webapp.git'
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

        stage('SonarQube Analysis') {
            steps {
                script {
                    def sonarRunning = bat(returnStatus: true, script: 'curl -s http://localhost:9000 >nul 2>&1') == 0
                    if (sonarRunning) {
                        echo "SonarQube is running — starting analysis..."
                        bat 'mvn clean install sonar:sonar -Dsonar.host.url=http://localhost:9000'
                    } else {
                        echo "⚠️ SonarQube is not running. Skipping analysis."
                    }
                }
            }
        }
    }

    post {
        success {
            echo "✅ Build and test for branch '${env.BRANCH_NAME}' completed successfully!"
        }
        failure {
            echo "❌ Build failed for branch '${env.BRANCH_NAME}'"
        }
    }
}
