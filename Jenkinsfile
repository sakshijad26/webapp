pipeline {
    agent any

    environment {
        SONAR_HOST_URL = 'http://localhost:9000'
        SONAR_TOKEN = credentials('sonar-token')
        NEXUS_URL = 'http://localhost:8081/repository/upes/'
        NEXUS_CRED = credentials('nexus-cred')
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'develop', url: 'https://github.com/sakshijad26/webapp.git'
            }
        }

        stage('Build') {
            steps {
                echo "🔧 Building project..."
                bat 'mvn -B -DskipTests clean package'
            }
        }

        stage('Test') {
            steps {
                echo "🧪 Running tests..."
                bat 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    echo "✅ Running SonarQube Analysis..."
                    bat """
                        mvn clean install sonar:sonar ^
                        -Dsonar.host.url=${SONAR_HOST_URL} ^
                        -Dsonar.token=${SONAR_TOKEN}
                    """
                }
            }
        }

        stage('Publish to Nexus') {
            steps {
                script {
                    echo "📦 Uploading artifact to Nexus Repository..."
                    bat """
                        mvn deploy -DskipTests ^
                        -DaltDeploymentRepository=upes::default::${NEXUS_URL} ^
                        -Dnexus.username=${NEXUS_CRED_USR} ^
                        -Dnexus.password=${NEXUS_CRED_PSW}
                    """
                }
            }
        }

        stage('Deploy Locally') {
            when {
                expression { currentBuild.result == null || currentBuild.result == 'SUCCESS' }
            }
            steps {
                echo "🚀 Deploying locally..."
                bat 'java -jar target/java-webapp-1.0.jar'
            }
        }
    }

    post {
        success {
            echo "✅ Build completed successfully!"
        }
        failure {
            echo "❌ Build failed — check console for details."
        }
    }
}
